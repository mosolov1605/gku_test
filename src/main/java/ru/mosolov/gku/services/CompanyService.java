package ru.mosolov.gku.services;

import ru.mosolov.gku.models.Company;

public interface CompanyService {

    Company getCompany(final Integer id);
    Company getCompany(final String name);
    Integer getCountDocWorkflow(final Company company, final Company counterCompany);
    Integer getTotalCountDocWorkflow (final Company company);
}
