package com.ptit.iot.repository;

import com.ptit.iot.model.InformationDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InformationDeviceRepository extends JpaRepository<InformationDevice, Long>, JpaSpecificationExecutor<InformationDevice> {

}