package com.Roshan.multithread;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DataProcessingSystem {
    private final BlockingQueue<Task> taskQueue;
    private final ConcurrentLinkedQueue<Result> resultQueue;
    private final ExecutorService executorService;
    private final List<TaskProcessor> workers;
    private final int numWorkers;
    
    // Define a special task ID to use as a shutdown signal
    private static final int SHUTDOWN_TASK_ID = -1;
    private static final String SHUTDOWN_TASK_DATA = "SHUTDOWN";
    
    public DataProcessingSystem(int numWorkers) {
        this.numWorkers = numWorkers;
        this.taskQueue = new LinkedBlockingQueue<>();
        this.resultQueue = new ConcurrentLinkedQueue<>();
        this.executorService = Executors.newFixedThreadPool(numWorkers);
        this.workers = new ArrayList<>(numWorkers);
        
        initializeWorkers();
    }
    
    private void initializeWorkers() {
        for (int i = 0; i < numWorkers; i++) {
            TaskProcessor worker = new TaskProcessor(i, taskQueue, resultQueue);
            workers.add(worker);
            executorService.submit(worker);
        }
        Logger.log("Initialized " + numWorkers + " worker threads");
    }
    
    public void submitTask(Task task) {
        try {
            taskQueue.put(task);
            Logger.log("Submitted task: " + task.getId());
        } catch (InterruptedException e) {
            Logger.error("Failed to submit task: " + task.getId(), e);
            Thread.currentThread().interrupt();
        }
    }
    
    public List<Result> getResults() {
        List<Result> results = new ArrayList<>();
        resultQueue.forEach(results::add);
        return results;
    }
    
    public void shutdown() {
        Logger.log("Initiating system shutdown");
        
        try {
            // Create a special shutdown task to use as a poison pill
            Task shutdownTask = new Task(SHUTDOWN_TASK_ID, SHUTDOWN_TASK_DATA);
            
            // Submit poison pills to stop workers
            for (int i = 0; i < numWorkers; i++) {
                taskQueue.put(shutdownTask);
            }
            
            // Wait for shutdown
            executorService.shutdown();
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                Logger.log("Forcing shutdown after timeout");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Logger.error("Shutdown interrupted", e);
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        Logger.log("System shutdown complete");
    }
}