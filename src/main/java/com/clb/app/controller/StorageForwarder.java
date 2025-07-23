package com.clb.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class StorageForwarder {
    public static boolean sendChunkToStorageNode(String urlStr, File chunkFile, String fileName, int part, int totalParts, String username) {
        try {
            URL url = new URL(urlStr);
            System.out.println("Forwarding chunk to: " + urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("X-File-Name", fileName);
            conn.setRequestProperty("X-Chunk-Number", String.valueOf(part));
            conn.setRequestProperty("X-Total-Chunks", String.valueOf(totalParts));
            conn.setRequestProperty("X-Username", username);

            try (OutputStream out = conn.getOutputStream();
                 FileInputStream fis = new FileInputStream(chunkFile)) {

                byte[] buffer = new byte[8192];
                int len;
                while ((len = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            }
            System.out.println("sendChunkToStorageNode returned with responseCode : " + conn.getResponseCode());
            return conn.getResponseCode() == 200;
        } catch (Exception e) {
            System.err.println("Forward failed: " + e.getMessage());
            return false;
        }
    }
}
