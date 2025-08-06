package vn.tuantrung.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.tuantrung.jobhunter.domain.Company;
import vn.tuantrung.jobhunter.domain.User;
import vn.tuantrung.jobhunter.domain.dto.ResultPaginationDTO;
import vn.tuantrung.jobhunter.service.CompanyService;
import vn.tuantrung.jobhunter.util.annotation.ApiMessage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@Valid @RequestBody Company reqCompany) {
        // TODO: process POST request

        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(reqCompany));
    }

    @GetMapping("/companies")
    @ApiMessage("fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(@Filter Specification<Company> specification,
            Pageable pageable) {

        // return ResponseEntity.ok(companies);

        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.fetchAllCompanies(specification,pageable));
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping("companies")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
        // TODO: process PUT request
        Company reqCompany = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.ok(reqCompany);
    }

}
