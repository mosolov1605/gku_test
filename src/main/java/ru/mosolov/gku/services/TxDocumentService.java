package ru.mosolov.gku.services;

import ru.mosolov.gku.models.Company;
import ru.mosolov.gku.models.Document;

import java.util.List;

public interface TxDocumentService {

    Document saveDocument(final Document document);
    List<Document> saveDocuments(final List<Document> documents);
    Document confirmDocument(final Company company, final Document document);
    Document confirmDocument(final Integer companyId, final Document document);
    Document confirmDocument(final String companyName, final Document document);
    Document removeDocument(final Company company, final Document document);
    Document removeDocument(final Integer companyId, final Document document);
    Document removeDocument(final String companyName, final Document document);
    Document changeDocument(final Company company, final Document document);
    Document changeDocument(final Integer companyId, final Document document);
    Document changeDocument(final String companyName, final Document document);
}
