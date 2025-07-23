package com.clb.app.scheduler;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinScheduler implements Scheduler {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Override
    public String selectNode(String username, String fileName, int chunkNumber, int totalChunks, List<String> nodes) {
        return nodes.get(counter.getAndUpdate(i -> (i + 1) % nodes.size()));
    }
}
