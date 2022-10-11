package com.korbiak.service.model.entities;

import com.korbiak.service.model.PartOfCountry;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coordinate")
@Data
public class Coordinate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "lat")
    private double lat;

    @Column(name = "lon")
    private double lon;

    @Column(name = "part_of_country")
    private PartOfCountry partOfCountry;

    @OneToMany(mappedBy = "coordinate", cascade = CascadeType.MERGE)
    private List<WeatherConditions> conditionReports = new ArrayList<>();
}
