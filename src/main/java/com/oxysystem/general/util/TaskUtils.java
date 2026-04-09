package com.oxysystem.general.util;

import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

public class TaskUtils {
    public static void cancelIfRunning(String taskName, Map<String, AtomicReference<Future<?>>> taskMap) {
        AtomicReference<Future<?>> currentTaskReference = taskMap.get(taskName);
        if (currentTaskReference != null) {
            Future<?> currentTask = currentTaskReference.get();
            if (currentTask != null && !currentTask.isDone()) {
                // Jika task sebelumnya belum selesai, batalkan
                currentTask.cancel(true);
                System.out.println("Previous task '" + taskName + "' canceled.");
            }
        }
    }
}
