package com.korbiak.service.repos;

import com.korbiak.service.model.entities.AlertInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertInfoRepo extends JpaRepository<AlertInfo, Integer> {
    List<AlertInfo> findAlertInfoByUserFirstNameAndUserSecondNameOrderByDateTimeDesc(String firstname, String secondName);
}
