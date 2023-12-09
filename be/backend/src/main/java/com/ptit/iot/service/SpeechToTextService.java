package com.ptit.iot.service;

import org.springframework.http.ResponseEntity;

import java.io.File;

public interface SpeechToTextService {
    ResponseEntity<String> speechToText(File file) throws Exception ;
}
