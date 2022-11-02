package com.korbiak.service.controller;

import com.korbiak.service.dto.CompanyDto;
import com.korbiak.service.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("service-api/companies/")
@CrossOrigin
public class CompanyController {
    private final CompanyService companyService;


    @GetMapping("{companyId}")
    public CompanyDto getCompany(@PathVariable int companyId) {
        return companyService.getById(companyId);
    }

    @GetMapping
    public List<CompanyDto> getAllCompanies() {
        return companyService.getAll();
    }


    @PostMapping
    public CompanyDto addNewCompany(@RequestParam String name,
                                    @RequestParam(value = "file") MultipartFile image) {
        return companyService.addNew(new CompanyDto(0, name, null), image);
    }

    @PutMapping
    public CompanyDto updateCompany(@RequestParam String name, @RequestParam int id,
                                    @RequestParam(value = "file", required = false) MultipartFile image) {
        return companyService.updateCompany(new CompanyDto(id, name, null), image);
    }

    @DeleteMapping("{companyId}")
    public void removeCompany(@PathVariable int companyId) {
        companyService.removeCompany(companyId);
    }
}
