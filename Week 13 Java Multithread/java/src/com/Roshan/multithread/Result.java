package com.Roshan.multithread;
public class Result {
    private final int taskId;
    private final String data;

    public Result(int taskId, String data) {
        this.taskId = taskId;
        this.data = data;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Result{taskId=" + taskId + ", data='" + data + "'}";
    }
}