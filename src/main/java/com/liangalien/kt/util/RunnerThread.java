package com.liangalien.kt.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RunnerThread {
    private static Map<String, Future> futureMap = new HashMap<>();
    private static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static void create(String key, Runnable task) throws Exception {
        if (!isDone(key)) {
            throw new Exception("Task key=" + key + " is not done");
        }

        futureMap.put(key, executor.submit(task));

        clear();  // 清理
    }

    public static void clear() {
        List<String> doneKeys = new ArrayList<>();
        for (String key : futureMap.keySet()) {
            if (futureMap.get(key) != null && isDone(key)) {
                doneKeys.add(key);
            }
        }

        doneKeys.forEach(key -> {
            futureMap.remove(key);
        });
    }

    public static boolean isDone(String key) {
        Future future = futureMap.get(key);
        if (future == null) {
            return true;
        } else {
            return future.isDone();
        }

    }

    public static boolean stop(String key) {
        boolean done = isDone(key);
        if (!done) {
            futureMap.get(key).cancel(false);
        }
        return done;
    }
}
