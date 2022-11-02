package com.korbiak.service.service.impl;

import com.korbiak.service.cloudinaryapi.CloudinaryManager;
import com.korbiak.service.dto.CompanyDto;
import com.korbiak.service.mapper.CompanyMapper;
import com.korbiak.service.model.entities.Company;
import com.korbiak.service.repos.CompanyRepo;
import com.korbiak.service.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepo companyRepo;
    private final CompanyMapper companyMapper;

    private final CloudinaryManager cloudinaryManager;

    @Override
    public CompanyDto getById(int id) {
        Optional<Company> byId = companyRepo.findById(id);
        if (byId.isEmpty()) {
            throw new IllegalArgumentException(String.format("Company with %s id not found", id));
        }

        return companyMapper.toDto(byId.get());
    }

    @Override
    public List<CompanyDto> getAll() {
        List<Company> companies = companyRepo.findAll();

        return companyMapper.toDtos(companies.stream()
                .filter(company -> company.getId() != 1) // filter out company for main admin
                .collect(Collectors.toList()));
    }

    @Override
    public CompanyDto addNew(CompanyDto company, MultipartFile image) {
        Company companyEntity = companyMapper.toEntity(company);
        companyEntity.setLogoPath("");
        companyRepo.save(companyEntity);
        return getCompanyDto(image, companyEntity);
    }

    @Override
    public CompanyDto updateCompany(CompanyDto company, MultipartFile image) {
        Optional<Company> byId = companyRepo.findById(company.getId());
        if (byId.isEmpty()) {
            throw new IllegalArgumentException("Not found company with id: " + company.getId());
        }
        Company companyEntity = byId.get();
        companyEntity.setName(company.getName());

        return getCompanyDto(image, companyEntity);
    }

    private CompanyDto getCompanyDto(MultipartFile image, Company companyEntity) {
        String url = "";
        if (image != null) {
            try {
                url = cloudinaryManager.uploadImage(image, "companies", companyEntity.getId());
            } catch (IOException e) {
                log.error("Error save image to cloudinary: {}", e.getMessage());
            }
            companyEntity.setLogoPath(url);
        }
        companyRepo.save(companyEntity);
        return companyMapper.toDto(companyEntity);
    }

    @Override
    public void removeCompany(int id) {
        companyRepo.deleteById(id);
    }
}
