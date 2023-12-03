package com.ptit.iot.service.impl;

import com.ptit.iot.constant.BaseResponse;
import com.ptit.iot.dto.resp.DataResp;
import com.ptit.iot.model.Data;
import com.ptit.iot.repository.DataRepository;
import com.ptit.iot.service.DataService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import jakarta.persistence.criteria.Predicate;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DataServiceImpl implements DataService {
    private final DataRepository dataRepository;

    @Autowired
    public DataServiceImpl(DataRepository dataRepository)
    {
        this.dataRepository = dataRepository;
    }

    @Override
    public ResponseEntity<?> getAllResult(Pageable pageable, String keyword, Integer hornStatus, Integer waterStatus,
                                          Long begin, Long end)
    {
        BaseResponse baseResponse = new BaseResponse();
            try {
                Page<Data> page =  dataRepository.findAll(makeQueryData(keyword, hornStatus, waterStatus, begin, end), pageable);
                Long totalElements = page.getTotalElements();
                Integer totalPages = page.getTotalPages();
                List<DataResp> res = page.getContent()
                        .stream().map(DataResp::new)
                        .toList();
                BaseResponse.PageResponse pageResponse = BaseResponse.PageResponse
                        .builder()
                        .totalPages(totalPages)
                        .totalElements(totalElements)
                        .data(res)
                        .build();
                 baseResponse = BaseResponse
                        .builder()
                        .message("success")
                        .status(HttpStatus.OK.value())
                        .data(pageResponse)
                        .build();
                return ResponseEntity.ok(baseResponse);
            }catch (Exception e){
                baseResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                baseResponse.setMessage("failed");
                baseResponse.setData(null);
                log.error("can not get data {}", e.getMessage());
                return ResponseEntity.badRequest().body(baseResponse);
            }
    }

    @Override
    public void saveData(Data data) {
        dataRepository.save(data);
        log.info("save data to db success");
    }


    Specification<Data> makeQueryData(String keyword, Integer hornStatus,
                                   Integer waterStatus, Long begin, Long end){
        Specification<Data> specification = Specification.where(null);
        specification = specification.and((((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(keyword.isEmpty()){
//                predicates.add(criteriaBuilder.or(
//                        criteriaBuilder.equal(root.get(Data.Data_.TEMPERATURE), "%" + keyword +"%"),
//                        criteriaBuilder.equal(root.get(Data.Data_.HUMIDITY), "%" + keyword +"%"),
//                        criteriaBuilder.equal(root.get(Data.Data_.GAS), "%" + keyword +"%")
//                ));
            }
            if(begin != null || end != null){
                predicates.add(criteriaBuilder.between(
                        root.get(Data.Data_.TIME), begin, end
                ));
            }
            if(hornStatus != null){
                predicates.add(criteriaBuilder.equal(root.get(Data.Data_.HORN_STATUS), hornStatus));
            }
            if(waterStatus != null){
                predicates.add(criteriaBuilder.equal(root.get(Data.Data_.WATER_STATUS), waterStatus));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        })));
        return specification;
    }
}
