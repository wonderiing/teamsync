package com.tecmilenio.carlos.ms.training.clients;

import com.tecmilenio.carlos.ms.training.dto.CompanyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-companies")
public interface CompanyFeignClient {

    @GetMapping("/api/v1/companies/{id}")
    CompanyDto getCompanyById(@PathVariable("id") Long id);
}
