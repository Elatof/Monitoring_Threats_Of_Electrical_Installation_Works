package com.korbiak.service.repos;

import com.korbiak.service.model.entities.WeatherConditions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface WeatherCondRepo extends JpaRepository<WeatherConditions, Integer> {
    @Query("SELECT dateTime\n" +
            "\tFROM WeatherConditions \n" +
            "\tGROUP BY dateTime")
    List<Timestamp> getAllDateTimes();

    List<WeatherConditions> getAllByDateTime(Timestamp dateTime);
}
