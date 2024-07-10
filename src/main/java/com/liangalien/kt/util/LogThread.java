package com.liangalien.kt.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogThread {
    public static void create(Runnable task) {
        ExecutorService executor = Executors.newFixedThreadPool(1);
        executor.submit(task);
        executor.shutdown();
    }
}
