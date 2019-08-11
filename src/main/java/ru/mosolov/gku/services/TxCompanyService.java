package ru.mosolov.gku.services;

import ru.mosolov.gku.models.Company;
import ru.mosolov.gku.models.User;

public interface TxCompanyService {

    Boolean saveCompany(final Company company);
    Boolean saveCompany(final User user, final Company company);
    Boolean saveCompany(final User user, final String name);
    Boolean saveCompany(final Integer userId, final String name);
    Boolean saveCompany(final String userName, final String companyName);
}
