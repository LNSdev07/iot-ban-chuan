package com.ptit.iot.repository;

import com.ptit.iot.model.Action;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActionRepository extends JpaRepository<Action, Integer> {
    @Query("Select a from Action a order by a.id desc")
    List<Action> getAllAction();
}
