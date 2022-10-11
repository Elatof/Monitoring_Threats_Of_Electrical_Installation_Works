package com.korbiak.service.service;

import com.korbiak.service.dto.CompanyDto;
import com.korbiak.service.model.entities.Company;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CompanyService {
    CompanyDto getById(int id);

    List<CompanyDto> getAll();

    CompanyDto addNew(CompanyDto company, MultipartFile image);

    CompanyDto updateCompany(CompanyDto company, MultipartFile image);

    void removeCompany(int id);
}
