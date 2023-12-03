package com.ptit.iot.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class DaoUtils {

    public static Pageable buildPageable(Integer page, Integer pageSize, String sortBy, String direction){
        if( page ==null || page < 0) page =0;
        if(pageSize ==null || pageSize <= 1) pageSize = 10;
        if(!direction.equalsIgnoreCase("desc") && !direction.equalsIgnoreCase("asc")){
            direction ="DESC";
        }
        return PageRequest.of(page-1, pageSize,
                Sort.Direction.valueOf(direction.toUpperCase()), sortBy);
    }
}
