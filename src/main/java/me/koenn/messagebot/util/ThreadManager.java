package me.koenn.messagebot.util;

import java.util.ArrayList;
import java.util.List;

public class ThreadManager implements Thread.UncaughtExceptionHandler {

    private final List<Thread> activeThreads;
    private boolean enabled;

    public ThreadManager() {
        this.activeThreads = new ArrayList<>();
        this.enabled = true;
    }

    public void createThread(String name, Runnable target) {
        this.createThread(name, target, false);
    }

    public void createThread(String name, Runnable target, boolean loop) {
        Thread thread;
        if (loop) {
            thread = new Thread(() -> {
                while (this.enabled) {
                    target.run();
                }
            });
        } else {
            thread = new Thread(target);
        }

        thread.setUncaughtExceptionHandler(this);
        thread.setName(name);

        this.activeThreads.add(thread);
        thread.start();
    }

    public void disable() {
        Logger.info("Stopping all threads...");
        this.enabled = false;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public int getActiveThreadCount() {
        return this.activeThreads.size();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable exception) {
        Logger.error("Exception in thread \'" + thread.getName() + "\': " + exception);
        exception.printStackTrace();
    }
}
