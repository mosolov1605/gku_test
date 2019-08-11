package ru.mosolov.gku.services;

import ru.mosolov.gku.models.Company;
import ru.mosolov.gku.models.Document;

import java.util.List;

public interface DocumentService {

    Document getDocumentById(final Integer id);
    Document getDocumentByName(final String name);
    List<Document> getDocumentsByIds (final List<Integer> ids);
    List<Document> getDocumentsByNames (final List<String> names);
    List<Document> getOpenDocs(final Company company);
    List<Document> getOpenDocs(final Integer companyId);
    List<Document> getOpenDocs(final String companyName);

}
