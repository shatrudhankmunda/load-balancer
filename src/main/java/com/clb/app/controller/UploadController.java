package com.clb.app.controller;

import com.clb.app.scheduler.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class UploadController {

    private final List<String> storageNodes = List.of(
            "http://storage-node-1:9001/store",
            "http://storage-node-2:9002/store",
            "http://storage-node-3:9003/store",
            "http://storage-node-4:9004/store"
    );
    private final Scheduler scheduler = new RoundRobinScheduler();
    @PostMapping("/upload")
    public ResponseEntity<String> receiveChunk(
            @RequestHeader("X-File-Name") String fileName,
            @RequestHeader("X-Chunk-Number") int chunkNumber,
            @RequestHeader("X-Total-Chunks") int totalChunks,
            @RequestHeader("X-Username") String username,
            InputStream bodyStream
    ) {
        try {
            //int selectedNode = roundRobinCounter.getAndUpdate(i -> (i + 1) % storageNodes.length);
            String targetNode = scheduler.selectNode(username, fileName, chunkNumber, totalChunks, storageNodes);
            System.out.println("Selected storage node: " + targetNode);
            File tempChunk = File.createTempFile("chunk_", ".enc");
            try (FileOutputStream out = new FileOutputStream(tempChunk)) {
                bodyStream.transferTo(out);
            }

            boolean forwarded = StorageForwarder.sendChunkToStorageNode(
                targetNode,
                tempChunk,
                fileName,
                chunkNumber,
                totalChunks,
                username
            );

            if (forwarded) {
                return ResponseEntity.ok("Chunk forwarded to node " + targetNode);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Failed to forward");
            }

        } catch (Exception e) {
            System.err.println("Error in upload: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
