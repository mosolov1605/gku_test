package ru.mosolov.gku.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mosolov.gku.models.Company;
import ru.mosolov.gku.models.Document;
import ru.mosolov.gku.repository.CompanyRepository;
import ru.mosolov.gku.repository.DocumentRepository;
import ru.mosolov.gku.services.TxDocumentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TxDocumentServiceImpl implements TxDocumentService {

    private DocumentRepository documentRepository;
    private CompanyRepository companyRepository;

    @Autowired
    public TxDocumentServiceImpl(final DocumentRepository documentRepository, CompanyRepository companyRepository) {
        this.documentRepository = documentRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Document saveDocument(final Document document) {
        if (document.getCompany().getId().equals(document.getCounterCompany().getId())) {
            document.setSuccess(false);
            document.setErrorMessage("Document can`t to have two equals companies for the docs workflow");
            return document;
        }
        return documentRepository.save(document);
    }

    @Override
    public List<Document> saveDocuments(final List<Document> documents) {
        return documentRepository.saveAll(documents);
    }

    @Override
    public Document confirmDocument(final Company company, final Document document) {

        return confirmDocument(company.getId(), document);
    }

    @Override
    public Document confirmDocument(final Integer companyId,final Document document) {
        if (companyId.equals(document.getCompany().getId())) {
            if (document.getConfirmCompany()) {
                document.setSuccess(false);
                document.setErrorMessage(String.format("Document with id = %d already confirmed", document.getId()));
                return document;
            }
            document.setConfirmCompany(true);
            document.setSuccess(true);
        }

        if (companyId.equals(document.getCounterCompany().getId())) {
            if (!document.getConfirmCompany()) {
                document.setSuccess(false);
                document.setErrorMessage(String
                        .format("Document with id = %d was confirmed yet with company %s"
                                , document.getId()
                                , document.getCompany().getName()));
                return document;
            }
            if (document.getConfirmCounterCompany()) {
                document.setSuccess(false);
                document.setErrorMessage(String.format("Document with id = %d already confirmed", document.getId()));
            }
            document.setSuccess(true);
            document.setConfirmCounterCompany(true);
        }

        if (document.getConfirmCompany() && document.getConfirmCounterCompany()) {
            document.setDateClose(LocalDateTime.now());
        }
        return saveDocument(document);
    }

    @Override
    public Document confirmDocument(final String companyName, final Document document) {
        final Company company = companyRepository.findByName(companyName).orElseGet(()-> {
            final Company errorCompany = new Company();
            errorCompany.setSuccess(false);
            errorCompany.setErrorMessage(String.format("Company with name = %s not found", companyName));
            return errorCompany;
        });
        if (!company.getSuccess()) {
            document.setSuccess(false);
            document.setErrorMessage(company.getErrorMessage());
            return document;
        }

        return confirmDocument(company.getId(), document);
    }

    @Override
    public Document removeDocument(final Company company, final Document document) {

        return removeDocument(company.getId(), document);
    }

    @Override
    public Document removeDocument(final Integer companyId,final Document document) {

        if (companyId.equals(document.getCounterCompany().getId())) {
            document.setSuccess(false);
            document.setErrorMessage(String
                    .format("Company with name = %s can`t remove the document, because was not creating it"
                            , document.getCounterCompany().getName()));
            return document;
        }

        if (!companyId.equals(document.getCompany().getId())){
            document.setSuccess(false);
            document.setErrorMessage(String
                    .format("Company with id = %d can`t remove the document id = %d, " +
                                    "because not assigned as participant to the workflow with it"
                            , companyId, document.getId()));
        }

        if (document.getDateDelete() != null) {
            document.setSuccess(false);
            document.setErrorMessage(String.format("Document with id = %d is already deleted", document.getId()));
            return document;
        }

        if (document.isLockRemove()) {
            document.setSuccess(false);
            document.setErrorMessage(String.format("Document with id = %d is locked for remove", document.getId()));
            return document;
        }

        document.setSuccess(true);
        if (document.getDateClose() == null) {
            document.setDateClose(LocalDateTime.now());
        }
        document.setDateDelete(LocalDateTime.now());
        saveDocument(document);
        return null;
    }

    @Override
    public Document removeDocument(final String companyName,final Document document) {
        Company company = companyRepository.findByName(companyName).orElse(null);
        if (company == null) {
            document.setSuccess(false);
            document.setErrorMessage(String.format("Company with name %s is not found", companyName));
            return document;
        }
        return removeDocument(company.getId(), document);
    }

    @Override
    public Document changeDocument(final Company company,final  Document document) {
        return null;
    }

    @Override
    public Document changeDocument(final Integer companyId,final  Document document) {

        if (document.isDeleted()) {
            document.setSuccess(false);
            document.setErrorMessage("Change is impossible. Document is deleted");
            return document;
        }
        if (document.isClosed()) {
            document.setSuccess(false);
            document.setErrorMessage("Change is impossible. Document is closed");
        }
        if (!companyId.equals(document.getHolderDoc().getId())) {
            document.setSuccess(false);
            document.setErrorMessage("Company can`t change this document, because is not an change-holder it in moment");
        }

        return null;
    }

    @Override
    public Document changeDocument(final String companyName,final  Document document) {
        return null;
    }
}
