package com.ptit.iot.repository;

import com.ptit.iot.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface ActionRepositoryTest extends JpaRepository<Action, Long>, JpaSpecificationExecutor<Action> {

}