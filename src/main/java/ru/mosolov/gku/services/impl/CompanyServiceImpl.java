package ru.mosolov.gku.services.impl;

import org.springframework.stereotype.Service;
import ru.mosolov.gku.models.Company;
import ru.mosolov.gku.repository.CompanyRepository;
import ru.mosolov.gku.services.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    private CompanyRepository companyRepository;

    public CompanyServiceImpl (final CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company getCompany(final Integer id) {
        return companyRepository
                .findById(id)
                .orElseGet(() ->
                    getErrorObject(String.format("Company with id = %d is not found",id)));
    }

    @Override
    public Company getCompany(final String name) {
        return companyRepository
                .findByName(name)
                .orElseGet(() ->
                        getErrorObject(String.format("Company with name %s is not found", name)));
    }

    @Override
    public Integer getCountDocWorkflow(Company company, Company counterCompany) {
        return null;
    }

    @Override
    public Integer getTotalCountDocWorkflow(Company company) {
        return null;
    }

    private Company getErrorObject(final String errorMessage) {
        final Company result = new Company();
        result.setSuccess(false);
        result.setErrorMessage(errorMessage);
        return result;
    }
}
