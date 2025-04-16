package logger

import (
	"log"
	"os"
	"sync"
	"time"
)

// Logger provides synchronized logging functionality
type Logger struct {
	stdLog  *log.Logger
	errLog  *log.Logger
	logLock sync.Mutex
}

// New creates a new Logger instance
func New() *Logger {
	return &Logger{
		stdLog: log.New(os.Stdout, "", 0),
		errLog: log.New(os.Stderr, "", 0),
	}
}

// Log logs an informational message
func (l *Logger) Log(message string) {
	l.logLock.Lock()
	defer l.logLock.Unlock()
	l.stdLog.Printf("[%s] %s", time.Now().Format("2006-01-02 15:04:05"), message)
}

// Error logs an error message with optional error details
func (l *Logger) Error(message string, err error) {
	l.logLock.Lock()
	defer l.logLock.Unlock()
	l.errLog.Printf("[%s] ERROR: %s", time.Now().Format("2006-01-02 15:04:05"), message)
	if err != nil {
		l.errLog.Println(err)
	}
}

// Global logger instance
var defaultLogger = New()

// GetDefaultLogger returns the default logger instance
func GetDefaultLogger() *Logger {
	return defaultLogger
}