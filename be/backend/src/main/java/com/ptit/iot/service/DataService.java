package com.ptit.iot.service;

import com.ptit.iot.model.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DataService {
    ResponseEntity<?> getAllResult(Pageable pageable, String keyword, Integer hornStatus, Integer waterStatus,
                                   Long begin, Long end);
    void saveData(Data data);
}
