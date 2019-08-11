package ru.mosolov.gku.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mosolov.gku.models.BaseDao;
import ru.mosolov.gku.models.Company;
import ru.mosolov.gku.models.Document;
import ru.mosolov.gku.repository.CompanyRepository;
import ru.mosolov.gku.repository.DocumentRepository;
import ru.mosolov.gku.services.DocumentService;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentServiceImpl implements DocumentService {

    private CompanyRepository companyRepository;
    private DocumentRepository documentRepository;
    @Autowired
    public DocumentServiceImpl(final CompanyRepository companyRepository
            , final DocumentRepository documentRepository){
        this.companyRepository = companyRepository;
        this.documentRepository = documentRepository;
    }
    @Override
    public Document getDocumentById(final Integer id) {
        return documentRepository.findById(id)
                .orElseGet(() -> getErrorObject(Document.class, String.format("Document with id = %d is not found", id)));
    }

    @Override
    public Document getDocumentByName(final String name) {

        return documentRepository.findByName(name)
                .orElseGet(()->getErrorObject(Document.class, String.format("Document with name = %s is not found", name)));
    }

    @Override
    public List<Document> getDocumentsByIds(final List<Integer> ids) {

        return ids
                .stream()
                .map(this::getDocumentById).collect(Collectors.toList());
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
    public List<Document> getOpenDocs(Integer companyId) {
        return documentRepository
                .getOpenDocs(companyId)
                .orElse(new ArrayList<>(Collections.singleton(getErrorObject(Document.class, "Documents not found"))));
    }

    @Override
    public List<Document> getOpenDocs(String companyName) {
        final Company company = companyRepository
                .findByName(companyName)
                .orElseGet(()-> getErrorObject(Company.class, String.format("Company with name = %s is not found", companyName)));

        if (company != null && company.getSuccess().equals(Boolean.TRUE)) {
            return documentRepository.getOpenDocs(company.getId()).orElse(new ArrayList<>());
        }
        return new ArrayList<>(Collections.singleton(getErrorObject(Document.class, "Documents not found")));
    }

    private <T extends BaseDao> T getErrorObject(Class<T> clazz, final String errorMessage) {
        final T result;
        try {
            result = clazz.getDeclaredConstructor().newInstance();
            result.setSuccess(false);
            result.setErrorMessage(errorMessage);
            return result;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}
