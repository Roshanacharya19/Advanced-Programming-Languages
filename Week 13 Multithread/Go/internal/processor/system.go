package processor

import (
	"fmt"
	"sync"
	"time"

	"multithreading/internal/logger"
	"multithreading/internal/model"
)

// DataProcessingSystem manages workers and coordinates task processing
type DataProcessingSystem struct {
	numWorkers   int
	taskChan     chan model.Task
	resultChan   chan model.Result
	results      []model.Result
	shutdownChan chan struct{}
	resultsMutex sync.Mutex
	wg           sync.WaitGroup
	logger       *logger.Logger
}

// NewSystem creates a new data processing system with the specified number of workers
func NewSystem(numWorkers int) *DataProcessingSystem {
	system := &DataProcessingSystem{
		numWorkers:   numWorkers,
		taskChan:     make(chan model.Task, 100), // Buffer size to prevent blocking
		resultChan:   make(chan model.Result, 100),
		shutdownChan: make(chan struct{}),
		results:      make([]model.Result, 0),
		logger:       logger.GetDefaultLogger(),
	}

	system.initializeWorkers()
	system.startResultCollector()

	return system
}

// initializeWorkers starts worker goroutines
func (dps *DataProcessingSystem) initializeWorkers() {
	for i := 0; i < dps.numWorkers; i++ {
		worker := NewTaskProcessor(i, dps.taskChan, dps.resultChan, dps.shutdownChan)

		dps.wg.Add(1)
		go func(w *TaskProcessor) {
			defer dps.wg.Done()
			w.Process()
		}(worker)
	}
	dps.logger.Log(fmt.Sprintf("Initialized %d worker goroutines", dps.numWorkers))
}

// startResultCollector starts a goroutine to collect results
func (dps *DataProcessingSystem) startResultCollector() {
	dps.wg.Add(1)
	go func() {
		defer dps.wg.Done()
		for result := range dps.resultChan {
			dps.resultsMutex.Lock()
			dps.results = append(dps.results, result)
			dps.resultsMutex.Unlock()
		}
	}()
}

// SubmitTask adds a task to the processing queue
func (dps *DataProcessingSystem) SubmitTask(task model.Task) {
	dps.taskChan <- task
	dps.logger.Log(fmt.Sprintf("Submitted task: %d", task.ID))
}

// GetResults returns a copy of all collected results
func (dps *DataProcessingSystem) GetResults() []model.Result {
	dps.resultsMutex.Lock()
	defer dps.resultsMutex.Unlock()

	// Create a copy to return
	resultsCopy := make([]model.Result, len(dps.results))
	copy(resultsCopy, dps.results)
	return resultsCopy
}

// Shutdown gracefully shuts down the processing system
func (dps *DataProcessingSystem) Shutdown() {
	dps.logger.Log("Initiating system shutdown")

	// Signal all workers to shut down
	close(dps.shutdownChan)

	// Wait for workers to finish (with timeout)
	waitCh := make(chan struct{})
	go func() {
		dps.wg.Wait()
		close(waitCh)
	}()

	select {
	case <-waitCh:
		// Workers shut down gracefully
	case <-time.After(10 * time.Second):
		dps.logger.Log("Forcing shutdown after timeout")
	}

	// Close channels
	close(dps.taskChan)
	close(dps.resultChan)

	dps.logger.Log("System shutdown complete")
}