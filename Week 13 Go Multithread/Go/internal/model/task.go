package model

import "fmt"

// Task represents a unit of work to be processed
type Task struct {
	ID   int
	Data string
}

// String returns a string representation of the task
func (t Task) String() string {
	return fmt.Sprintf("Task{id=%d, data='%s'}", t.ID, t.Data)
}