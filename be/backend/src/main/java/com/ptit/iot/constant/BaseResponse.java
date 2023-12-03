package com.ptit.iot.constant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse {
    private int status;
    private String message;
    private Object data;

    @Data
    @Builder
    public static class PageResponse{
         private Long totalElements;
         private Integer totalPages;
         private Object data;
    }
}
