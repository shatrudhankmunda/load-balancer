package com.clb.app.scheduler;

import java.util.List;

public class FCFSScheduler implements Scheduler {
    private int pointer = 0;

    @Override
    public synchronized String selectNode(String username, String fileName, int chunkNumber, int totalChunks, List<String> nodes) {
        String node = nodes.get(pointer);
        pointer = (pointer + 1) % nodes.size();
        return node;
    }
}
