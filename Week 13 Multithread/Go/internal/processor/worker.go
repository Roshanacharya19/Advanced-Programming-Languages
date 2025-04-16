package processor

import (
	"fmt"
	"math/rand"
	"time"

	"multithreading/internal/logger"
	"multithreading/internal/model"
)

// TaskProcessor processes tasks from a queue and produces results
type TaskProcessor struct {
	ID          int
	TaskChan    <-chan model.Task
	ResultChan  chan<- model.Result
	ShutdownSig <-chan struct{}
	logger      *logger.Logger
}

// NewTaskProcessor creates a new task processor
func NewTaskProcessor(id int, taskChan <-chan model.Task, resultChan chan<- model.Result, shutdownSig <-chan struct{}) *TaskProcessor {
	return &TaskProcessor{
		ID:          id,
		TaskChan:    taskChan,
		ResultChan:  resultChan,
		ShutdownSig: shutdownSig,
		logger:      logger.GetDefaultLogger(),
	}
}

// Process starts the task processor and processes tasks until shutdown
func (p *TaskProcessor) Process() {
	p.logger.Log(fmt.Sprintf("Worker %d started", p.ID))
	defer p.logger.Log(fmt.Sprintf("Worker %d shutting down", p.ID))

	// Create a random number generator with its own seed
	rng := rand.New(rand.NewSource(time.Now().UnixNano() + int64(p.ID)))

	for {
		select {
		case task, ok := <-p.TaskChan:
			if !ok {
				// Channel closed, worker should exit
				return
			}

			p.logger.Log(fmt.Sprintf("Worker %d processing task %d", p.ID, task.ID))

			// Simulate processing time (100-500ms)
			processingTime := int64(100 + rng.Intn(400))
			time.Sleep(time.Duration(processingTime) * time.Millisecond)

			// Create a result
			processedData := "Processed: " + task.Data
			result := model.Result{
				TaskID:         task.ID,
				ProcessedData:  processedData,
				ProcessingTime: processingTime,
				WorkerID:       p.ID,
			}

			// Send the result
			p.ResultChan <- result

			p.logger.Log(fmt.Sprintf("Worker %d completed task %d", p.ID, task.ID))

		case <-p.ShutdownSig:
			// Received shutdown signal
			p.logger.Log(fmt.Sprintf("Worker %d received shutdown signal", p.ID))
			return
		}
	}
}