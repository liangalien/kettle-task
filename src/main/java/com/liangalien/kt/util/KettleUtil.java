package com.liangalien.kt.util;

public class KettleUtil {
    public static RunnerStatus getStatus(String message) {
        if ("Waiting".equals(message)) {
            return RunnerStatus.WAITING;
        } else if ("Halting".equals(message) || "Stopped".equals(message)) {
            return RunnerStatus.STOPPED;
        } else if ("Finished".equals(message)) {
            return RunnerStatus.SUCCESS;
        } else if ("Finished (with errors)".equals(message) || "Stopped (with errors)".equals(message)) {
            return RunnerStatus.ERROR;
        } else if ("Running".equals(message)) {
            return RunnerStatus.RUNNING;
        }
        return RunnerStatus.OTHER;
    }
}
