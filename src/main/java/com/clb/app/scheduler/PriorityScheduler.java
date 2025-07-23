package com.clb.app.scheduler;

import java.util.List;

public class PriorityScheduler implements Scheduler {
    @Override
    public String selectNode(String username, String fileName, int chunkNumber, int totalChunks, List<String> nodes) {
        // Simple logic: if admin, always route to node 1; else round robin
        if (username.equalsIgnoreCase("admin")) {
            return nodes.get(0);
        } else {
            return nodes.get(chunkNumber % nodes.size());
        }
    }
}
