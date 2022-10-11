package com.korbiak.service.repos;

import com.korbiak.service.model.entities.LightningCoordinate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface LightningCoordinateRepo extends JpaRepository<LightningCoordinate, Integer> {
    List<LightningCoordinate> findAllByDateTime(Timestamp dateTime);
}
