public class Result {
    private final int taskId;
    private final String processedData;
    private final long processingTime;
    private final int workerId;

    public Result(int taskId, String processedData, long processingTime, int workerId) {
        this.taskId = taskId;
        this.processedData = processedData;
        this.processingTime = processingTime;
        this.workerId = workerId;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getProcessedData() {
        return processedData;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public int getWorkerId() {
        return workerId;
    }

    @Override
    public String toString() {
        return "Result{taskId=" + taskId + 
               ", processedData='" + processedData + 
               "', processingTime=" + processingTime + 
               "ms, workerId=" + workerId + "}";
    }
}