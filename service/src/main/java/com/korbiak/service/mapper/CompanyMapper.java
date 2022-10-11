package com.korbiak.service.mapper;

import com.korbiak.service.dto.CompanyDto;
import com.korbiak.service.model.entities.Company;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper implements Mapper<CompanyDto, Company> {

    @Override
    public CompanyDto toDto(Company input) {
        return CompanyDto.builder()
                .id(input.getId())
                .name(input.getName())
                .logoPath(input.getLogoPath())
                .build();
    }

    @Override
    public Company toEntity(CompanyDto input) {
        Company entity = new Company();
        entity.setId(input.getId());
        entity.setName(input.getName());
        entity.setLogoPath(input.getLogoPath());
        return entity;
    }
}
