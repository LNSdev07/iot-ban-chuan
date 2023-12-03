package com.ptit.iot.controller;

import com.ptit.iot.auth.CheckPermissionUser;
import com.ptit.iot.auth.Payload;
import com.ptit.iot.auth.Role;
import com.ptit.iot.constant.Definition;
import com.ptit.iot.exceptions.ResErrorCode;
import com.ptit.iot.exceptions.ResException;
import com.ptit.iot.service.DataService;
import com.ptit.iot.utils.DaoUtils;
import com.ptit.iot.utils.ValidateUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@CrossOrigin("*")
@Log4j2
public class AllDataController {
    @Autowired
    private DataService dataService;

    @Autowired
    private CheckPermissionUser checkPermissionUser;

    @GetMapping("/get-all-data")
    public ResponseEntity getAllDataView(@RequestParam(value = "page", required = false, defaultValue = "1") Integer pageIndex,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                         @RequestParam(value = "sortBy", required = false, defaultValue = "time") String sortBy,
                                         @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction,
                                         @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                         @RequestParam(value = "hornStatus", required = false) Integer hornStatus,
                                         @RequestParam(value = "waterStatus", required = false) Integer waterStatus,
                                         @RequestParam(value = "begin", required = false) Long begin,
                                         @RequestParam(value = "end", required = false) Long end,
                                         @RequestAttribute(name = Definition.PAYLOAD, required = false) Payload payload)
    {
        ValidateUtils.validatePayload(payload);
        if(!checkPermissionUser.isPermissionUser(payload.getUserId(), Set.of(new Role(Role.ADMIN),
                new Role(Role.SUPER_ADMIN),
                new Role(Role.VISITOR)))
        ){
            throw new ResException(ResErrorCode.PERMISSION_DENIED);
        }
        Pageable pageable = DaoUtils.buildPageable(pageIndex, pageSize, sortBy, direction);
        return dataService.getAllResult(pageable, keyword,hornStatus, waterStatus, begin, end );
    }
}
