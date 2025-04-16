import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize the system with 4 worker threads
            DataProcessingSystem system = new DataProcessingSystem(4);
            
            // Submit tasks
            for (int i = 0; i < 20; i++) {
                system.submitTask(new Task(i, "Task data " + i));
            }
            
            // Wait for some time to allow processing
            Thread.sleep(3000);
            
            // Get and print results
            List<Result> results = system.getResults();
            Logger.log("Collected " + results.size() + " results:");
            for (Result result : results) {
                Logger.log(result.toString());
            }
            
            // Shutdown the system
            system.shutdown();
            
            Logger.log("Main program exiting");
        } catch (Exception e) {
            Logger.error("Error in main", e);
        }
    }
}