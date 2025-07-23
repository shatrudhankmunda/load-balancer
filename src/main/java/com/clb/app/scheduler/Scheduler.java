package com.clb.app.scheduler;

import java.util.List;

public interface Scheduler {
    String selectNode(String username, String fileName, int chunkNumber, int totalChunks, List<String> nodes);
}
