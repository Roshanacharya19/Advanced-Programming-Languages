import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Random;

public class TaskProcessor implements Runnable {
    private final int id;
    private final BlockingQueue<Task> taskQueue;
    private final ConcurrentLinkedQueue<Result> resultQueue;
    private final Random random = new Random();
    private boolean running = true;

    // Constants for identifying shutdown task
    private static final int SHUTDOWN_TASK_ID = -1;
    private static final String SHUTDOWN_TASK_DATA = "SHUTDOWN";

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
                try {
                    Task task = taskQueue.take(); // Blocks if queue is empty
                    
                    // Check for poison pill (special task signals shutdown instead of null)
                    if (task.getId() == SHUTDOWN_TASK_ID && SHUTDOWN_TASK_DATA.equals(task.getData())) {
                        running = false;
                        Logger.log("Worker " + id + " received shutdown signal");
                        break;
                    }
                    
                    // Process the task
                    Logger.log("Worker " + id + " processing task " + task.getId());
                    
                    // Simulate processing time (100-500ms)
                    long processingTime = 100 + random.nextInt(400);
                    Thread.sleep(processingTime);
                    
                    // Create a result
                    String processedData = "Processed: " + task.getData().toUpperCase();
                    Result result = new Result(task.getId(), processedData, processingTime, id);
                    
                    // Add result to result queue
                    resultQueue.add(result);
                    
                    Logger.log("Worker " + id + " completed task " + task.getId());
                } catch (InterruptedException e) {
                    Logger.error("Worker " + id + " was interrupted", e);
                    Thread.currentThread().interrupt();
                    running = false;
                } catch (Exception e) {
                    Logger.error("Worker " + id + " encountered an error", e);
                }
            }
        } finally {
            Logger.log("Worker " + id + " shutting down");
        }
    }
    
    public void stop() {
        running = false;
    }
}