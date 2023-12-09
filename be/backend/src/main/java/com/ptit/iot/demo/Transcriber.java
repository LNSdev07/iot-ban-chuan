//package com.ptit.iot.demo;
//
//import java.net.*;
//import java.io.*;
//import java.util.concurrent.TimeUnit;
//import org.json.*;
//
//
//public class Transcriber {
//
//    public static void main(String[] args) throws Exception {
//        String secret_key = "d9d9a74fc8b64ca8bdf2e7e2fc44238d";
//        HttpURLConnection conn;
//
//        // endpoint and options to start a transcription task
//        URL endpoint = new URL("https://api.speechtext.ai/recognize?key=" + secret_key +"&language=vi-VN&punctuation=true&format=mp3");
//
//        // loads the audio into memory
//        File file = new File("D:\\HKI-2023-2024\\TaiLieuIOT\\service\\btl-iot-2023\\backend\\src\\main\\java\\com\\ptit\\iot\\demo\\mp3-output-ttsfree(dot)com.mp3");
//        RandomAccessFile f = new RandomAccessFile(file, "r");
//        long sz = f.length();
//        byte[] post_body = new byte[(int) sz];
//        f.readFully(post_body);
//        f.close();
//
//        // send an audio transcription request
//        conn = (HttpURLConnection) endpoint.openConnection();
//        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/octet-stream");
//
//        conn.setDoOutput(true);
//        conn.connect();
//        OutputStream os = conn.getOutputStream();
//        os.write(post_body);
//        os.flush();
//        os.close();
//
//        int responseCode = conn.getResponseCode();
//
//        if (responseCode == 200) {
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line;
//            StringBuffer response = new StringBuffer();
//            while ((line = in .readLine()) != null) {
//                response.append(line);
//            } in .close();
//            String result = response.toString();
//            JSONObject json = new JSONObject(result);
//            // get the id of the speech recognition task
//            String task = json.getString("id");
//            System.out.println("Task ID: " + task);
//            // endpoint to check status of the transcription task
//            URL res_endpoint = new URL("https://api.speechtext.ai/results?key=" +secret_key + "&task=" + task + "&language=vi-VN");
//            System.out.println("Get transcription results, summary, and highlights");
//            // use a loop to check if the task is finished
//            JSONObject results;
//            String resultText = "";
//            while (true) {
//                conn = (HttpURLConnection) res_endpoint.openConnection();
//                conn.setRequestMethod("GET");
//                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                response = new StringBuffer();
//                String res;
//                while ((res = in .readLine()) != null) {
//                    response.append(res);
//                } in .close();
//                results = new JSONObject(response.toString());
//                System.out.println("Task status: " + results.getString("status"));
//                if (results.getString("status").equals("failed")) {
//                    System.out.println("Failed to transcribe!");
//                    break;
//                }
//                if (results.getString("status").equals("finished")) {
//                    System.out.println(results);
//                    resultText =  results.getJSONObject("results").getString("transcript");
//                    break;
//                }
//                // sleep for 15 seconds if the task has the status - 'processing'
////                TimeUnit.SECONDS.sleep(15);
//
//            }
//            System.out.println("resultText = "+ resultText);
//        } else {
//            System.out.println("Failed to transcribe!");
//        }
//    }
//}