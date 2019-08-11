package ru.mosolov.gku.services;

import ru.mosolov.gku.models.Company;

public interface CompanyService {

    Company getCompany(final Integer id);
    Company getCompany(final String name);
    Boolean ablyToCreateDoc(final Integer id);
    Boolean ablyToCreateDoc(final String name);
    Boolean ablyToChangeDoc(final Integer id);
    Boolean ablyToChangeDoc(final String name);
}
