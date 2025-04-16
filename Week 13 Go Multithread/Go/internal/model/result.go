package model

import "fmt"

// Result represents the output of a processed task
type Result struct {
	TaskID         int
	ProcessedData  string
	ProcessingTime int64
	WorkerID       int
}

// String returns a string representation of the result
func (r Result) String() string {
	return fmt.Sprintf("Result{taskId=%d, processedData='%s', processingTime=%dms, workerId=%d}",
		r.TaskID, r.ProcessedData, r.ProcessingTime, r.WorkerID)
}