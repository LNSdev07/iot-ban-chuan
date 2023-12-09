package com.ptit.iot.service.impl;

import com.ptit.iot.service.SpeechToTextService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class SpeechToTextServiceImpl implements SpeechToTextService {

    @Value("${secret_key}")
    private String secretKey;

    public ResponseEntity<String> speechToText(File fileResource) {
        try {
            // Load audio file into memory
            byte[] audioData = loadAudioData(fileResource);
            // Send audio transcription request
            String taskId = sendTranscriptionRequest(audioData);
            // Check status of the transcription task
            String resultText = checkTranscriptionStatus(taskId);
            return ResponseEntity.ok(resultText);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to transcribe!");
            return ResponseEntity.badRequest().body("can not speech to text");
        }
    }

    private byte[] loadAudioData(File file) throws IOException {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            long fileSize = randomAccessFile.length();
            byte[] audioData = new byte[(int) fileSize];
            randomAccessFile.readFully(audioData);
            return audioData;
        }
    }

    private String sendTranscriptionRequest(byte[] audioData) throws IOException {
        URL endpoint = new URL("https://api.speechtext.ai/recognize?key=" + secretKey +
                "&language=vi-VN&punctuation=true&format=mp3");

        HttpURLConnection connection = (HttpURLConnection) endpoint.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
        connection.setDoOutput(true);
        connection.getOutputStream().write(audioData);

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.lines().reduce(String::concat).orElse("");
            reader.close();

            return new JSONObject(response).getString("id");
        } else {
            System.out.println("Failed to transcribe!");
            return null;
        }
    }

    private String checkTranscriptionStatus(String taskId) throws IOException, InterruptedException {
        URL resultEndpoint = new URL("https://api.speechtext.ai/results?key=" + secretKey +
                "&task=" + taskId + "&language=vi-VN");

        while (true) {
            HttpURLConnection connection = (HttpURLConnection) resultEndpoint.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = reader.lines().reduce(String::concat).orElse("");
            reader.close();

            JSONObject results = new JSONObject(response);
            System.out.println("Task status: " + results.getString("status"));

            if ("failed".equals(results.getString("status"))) {
                System.out.println("Failed to transcribe!");
                break;
            }

            if ("finished".equals(results.getString("status"))) {
                System.out.println(results);
                return results.getJSONObject("results").getString("transcript");
            }
            // Add a delay before checking again to avoid unnecessary requests
//            Thread.sleep(1000);
        }
        return null;
    }
}
