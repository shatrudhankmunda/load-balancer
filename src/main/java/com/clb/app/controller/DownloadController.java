package com.clb.app.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

@RestController
public class DownloadController {

    private final String[] storageNodes = {
        "http://storage-node-1:9001",
        "http://storage-node-2:9002",
        "http://storage-node-3:9003",
        "http://storage-node-4:9004"
    };

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadFile(
            @RequestParam String username,
            @RequestParam String file
    ) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int chunk = 0;

        while (true) {
            boolean chunkFound = false;
            for (String node : storageNodes) {
                try {
                    String url = node + "/chunk?username=" + username + "&file=" + file + "&part=" + chunk;
                    ResponseEntity<byte[]> response = new RestTemplate().getForEntity(url, byte[].class);
                    if (response.getStatusCode() == HttpStatus.OK) {
                        outputStream.write(response.getBody());
                        chunkFound = true;
                        break;
                    }
                } catch (Exception ignored) {}
            }

            if (!chunkFound) break;
            chunk++;
        }

        if (chunk == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file)
            .body(outputStream.toByteArray());
    }
}
