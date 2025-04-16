package main

import (
	"fmt"
	"time"

	"multithreading/internal/logger"
	"multithreading/internal/model"
	"multithreading/internal/processor"
)

func main() {
	log := logger.GetDefaultLogger()

	try := func() {
		// Initialize the system with 4 worker goroutines
		system := processor.NewSystem(4)

		// Submit tasks
		for i := 0; i < 20; i++ {
			system.SubmitTask(model.Task{
				ID:   i,
				Data: fmt.Sprintf("Task data %d", i),
			})
		}

		// Wait for some time to allow processing
		time.Sleep(3 * time.Second)

		// Get and print results
		results := system.GetResults()
		log.Log(fmt.Sprintf("Collected %d results:", len(results)))
		for _, result := range results {
			log.Log(result.String())
		}

		// Shutdown the system
		system.Shutdown()

		log.Log("Main program exiting")
	}

	// Catch panics in main
	defer func() {
		if r := recover(); r != nil {
			log.Error("Error in main", fmt.Errorf("%v", r))
		}
	}()

	try()
}