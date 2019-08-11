package ru.mosolov.gku.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mosolov.gku.models.Company;
import ru.mosolov.gku.models.Document;
import ru.mosolov.gku.repository.DocumentRepository;
import ru.mosolov.gku.services.CompanyService;
import ru.mosolov.gku.services.DocumentService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private CompanyService companyService;
    private DocumentRepository documentRepository;

    @Autowired
    public DocumentServiceImpl(final CompanyService companyService
            , final DocumentRepository documentRepository){
        this.companyService = companyService;
        this.documentRepository = documentRepository;
    }
    @Override
    public Document getDocumentById(final Integer id) {
        return documentRepository.findById(id)
                .filter(doc -> doc.getDateDelete() == null)
                .orElseGet(() -> getErrorObject(String.format("Document with id = %d is not found", id)));
    }

    @Override
    public Document getDocumentByName(final String name) {

        return documentRepository
                .findByName(name)
                .filter(doc -> doc.getDateDelete() == null)
                .orElseGet(()->
                        getErrorObject(String.format("Document with name = %s is not found", name)));
    }

    @Override
    public List<Document> getDocumentsByIds(final List<Integer> ids) {

        return ids
                .stream()
                .map(this::getDocumentById)
                .collect(Collectors.toList());
    }

    @Override
    public List<Document> getDocumentsByNames(final List<String> names) {
        return names.stream()
                .map(this::getDocumentByName)
                .collect(Collectors.toList());
    }

    @Override
    public List<Document> getOpenDocs(final Company company) {
        return documentRepository.getOpenDocs(company.getId()).orElse(new ArrayList<>());
    }

    @Override
    public List<Document> getOpenDocsBetweenDates(Company company, LocalDateTime startDate, LocalDateTime endDate) {
        return documentRepository.getOpenDocsBetweenDates(company.getId(), startDate, endDate).orElse(new ArrayList<>());
    }

    @Override
    public List<Document> getOpenDocsBetweenTwoCompanies(Company one, Company two) {
        return documentRepository.getOpenDocsBetweenTwoCompanies(one.getId(), two.getId()).orElse(new ArrayList<>());
    }

    @Override
    public List<Document> getOpenDocs(Integer companyId) {
        return documentRepository
                .getOpenDocs(companyId)
                .orElse(new ArrayList<>(Collections.singleton(getErrorObject("Documents not found"))));
    }

    @Override
    public List<Document> getOpenDocs(String companyName) {
        final Company company = companyService.getCompany(companyName);
        if (!company.getSuccess()){
            return new ArrayList<>(Collections
                    .singleton(getErrorObject("Documents not found")));
        }

        return documentRepository.getOpenDocs(company.getId()).orElse(new ArrayList<>(Collections
                .singleton(getErrorObject("Documents not found"))));
    }

    private Document getErrorObject(final String errorMessage) {
        final Document result = new Document();
        result.setSuccess(false);
        result.setErrorMessage(errorMessage);
        return result;
    }
}
