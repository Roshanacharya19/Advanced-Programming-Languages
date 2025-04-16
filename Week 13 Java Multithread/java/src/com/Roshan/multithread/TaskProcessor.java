package com.Roshan.multithread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskProcessor implements Runnable {
    private final int id;
    private final BlockingQueue<Task> taskQueue;
    private final ConcurrentLinkedQueue<Result> resultQueue;
    private boolean running = true;

    public TaskProcessor(int id, BlockingQueue<Task> taskQueue, ConcurrentLinkedQueue<Result> resultQueue) {
        this.id = id;
        this.taskQueue = taskQueue;
        this.resultQueue = resultQueue;
    }

    @Override
    public void run() {
        Logger.log("Worker " + id + " started");
        
        try {
            while (running) {
                // Take a task from the queue (blocking operation)
                Task task = taskQueue.take();
                
                // Check if it's a shutdown signal
                if (task.getId() == -1) {
                    Logger.log("Worker " + id + " received shutdown signal");
                    break;
                }
                
                // Process the task
                Logger.log("Worker " + id + " processing task " + task.getId());
                
                // Simulate processing time
                Thread.sleep((long) (Math.random() * 1000));
                
                // Create a result
                String resultData = "Processed: " + task.getData() + " by Worker " + id;
                Result result = new Result(task.getId(), resultData);
                
                // Add to result queue
                resultQueue.add(result);
                Logger.log("Worker " + id + " completed task " + task.getId());
            }
        } catch (InterruptedException e) {
            Logger.log("Worker " + id + " was interrupted");
            Thread.currentThread().interrupt();
        }
        
        Logger.log("Worker " + id + " shutting down");
    }
}
