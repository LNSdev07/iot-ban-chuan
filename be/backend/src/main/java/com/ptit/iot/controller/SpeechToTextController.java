package com.ptit.iot.controller;


import com.ptit.iot.service.SpeechToTextService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/speech-to-text")
@Log4j2
public class SpeechToTextController {

    @Autowired
    private SpeechToTextService speechToTextService;


    @PostMapping("/remote")
    public ResponseEntity speechToText(@RequestParam("file") MultipartFile multipartFile){
        try {
            ResponseEntity<String> response =  speechToTextService.speechToText(convertMultipartFileToFile(multipartFile));
            return new ResponseEntity<>(response.getBody(), HttpStatus.CREATED); // Use HttpStatus.CREATED for success
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        File file = new File(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            FileCopyUtils.copy(multipartFile.getBytes(), fos);
        }
        return file;
    }
}
