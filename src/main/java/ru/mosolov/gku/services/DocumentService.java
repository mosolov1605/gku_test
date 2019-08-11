package ru.mosolov.gku.services;

import ru.mosolov.gku.models.Company;
import ru.mosolov.gku.models.Document;

import java.time.LocalDateTime;
import java.util.List;

public interface DocumentService {

    Document getDocumentById(final Integer id);
    Document getDocumentByName(final String name);
    List<Document> getDocumentsByIds (final List<Integer> ids);
    List<Document> getDocumentsByNames (final List<String> names);
    List<Document> getOpenDocs(final Company company);
    List<Document> getOpenDocsBetweenDates(final Company company, final LocalDateTime startDate, final LocalDateTime endDate);
    List<Document> getOpenDocsBetweenTwoCompanies (final Company one, final Company two);
    List<Document> getOpenDocs(final Integer companyId);
    List<Document> getOpenDocs(final String companyName);

}
