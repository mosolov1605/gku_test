package ru.mosolov.gku.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.mosolov.gku.models.Company;
import ru.mosolov.gku.models.Document;
import ru.mosolov.gku.other.CheckUtils;
import ru.mosolov.gku.other.PropertySource;
import ru.mosolov.gku.repository.DocumentRepository;
import ru.mosolov.gku.services.CompanyService;
import ru.mosolov.gku.services.DocumentService;
import ru.mosolov.gku.services.TxDocumentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TxDocumentServiceImpl implements TxDocumentService {

    private DocumentRepository documentRepository;
    private CompanyService companyService;
    private DocumentService documentService;
    private PropertySource propertySource;

    @Autowired
    public TxDocumentServiceImpl(final DocumentRepository documentRepository,
                                 final CompanyService companyService,
                                 final PropertySource propertySource,
                                 final DocumentService documentService) {
        this.documentRepository = documentRepository;
        this.companyService = companyService;
        this.propertySource = propertySource;
        this.documentService = documentService;
    }

    @Override
    public Document saveDocument(final Document document) {

        if (document.getCompany().getId().equals(document.getCounterCompany().getId())) {
            document.setSuccess(false);
            document.setErrorMessage("Document can`t to have two equals companies for the docs workflow");
            return document;
        }
        final int restTotal = Integer.parseInt(StringUtils.trimAllWhitespace(propertySource.getTotalDocWorkflow()));
        final String restrictOne = checkTotalDocWorkFlows(document.getCompany(), restTotal);
        if (!restrictOne.equals("success")) {
            document.setSuccess(false);
            document.setErrorMessage(restrictOne);
            return document;
        }
        final String restrictTwo = checkTotalDocWorkFlows(document.getCounterCompany(), restTotal);
        if (!restrictTwo.equals("success")) {
            document.setSuccess(false);
            document.setErrorMessage(restrictTwo);
            return document;
        }

        final Document docExist = documentService.getDocumentByName(document.getName());

        if (docExist.getSuccess()){
            document.setSuccess(false);
            document.setErrorMessage(String.format("Document with name %s already exist", document.getName()));
            return document;
        }

        final int countDocWorkflow = documentService
                .getOpenDocsBetweenTwoCompanies(document.getCompany(), document.getCounterCompany()).size();
        if (countDocWorkflow >= Integer.parseInt(StringUtils.trimAllWhitespace(propertySource.getBetweenDocWorkflow()))) {
            document.setSuccess(false);
            document.setErrorMessage(String
                    .format("Companies already have creating %d docs workflow between it", countDocWorkflow));
        }
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime oldTime = now.minusMinutes(Integer.parseInt(StringUtils.trimAllWhitespace(propertySource.getMinuteRestrictionCreateDoc())));
        List<Document> listDoc = documentService
                .getOpenDocsBetweenDates(document.getCompany(), oldTime, now)
                .stream()
                .filter(doc -> doc
                        .getCompany().getId().equals(document.getCompany().getId())).collect(Collectors.toList());
        if (listDoc.size() >= Integer.parseInt(StringUtils.trimAllWhitespace(propertySource.getCountRestrictionCreateDoc()))) {
            document.setSuccess(false);
            document.setErrorMessage(String
                    .format("Company with name %s already have creating %s doc workflows for %s minute(s)." +
                            " This is overtime-restriction."
                            , document.getCompany().getName()
                            , propertySource.getCountRestrictionCreateDoc()
                            , propertySource.getMinuteRestrictionCreateDoc()));
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

        if (!CheckUtils.checkAbilityChangeDoc(propertySource.getStartLockTime(), propertySource.getEndLockTime())) {
            document.setSuccess(false);
            document.setErrorMessage(String.format("Prohibited confirm documents in period from %s to %s"
                    , propertySource.getStartLockChangeDoc(), propertySource.getEndLockChangeDoc()));
            return document;
        }

        if (document.isDeleted()) {
            document.setSuccess(false);
            document.setErrorMessage("Confirm is impossible. Document is deleted");
            return document;
        }

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
        final Company company = companyService.getCompany(companyName);
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
        Company company = companyService.getCompany(companyName);
        if (!company.getSuccess()) {
            document.setSuccess(false);
            document.setErrorMessage(company.getErrorMessage());
            return document;
        }
        return removeDocument(company.getId(), document);
    }

    @Override
    public Document changeDocument(final Company company,final  Document document) {

        return changeDocument(company.getId(), document);
    }

    @Override
    public Document changeDocument(final Integer companyId,final  Document document) {

        if (!CheckUtils.checkAbilityChangeDoc(propertySource.getStartLockTime(), propertySource.getEndLockTime())){
            document.setSuccess(false);
            document.setErrorMessage(String.format("Prohibited change documents in period from %s to %s"
                    , propertySource.getStartLockChangeDoc(), propertySource.getEndLockChangeDoc()));
            return document;
        }

        if (document.isDeleted()) {
            document.setSuccess(false);
            document.setErrorMessage("Change is impossible. Document is deleted");
            return document;
        }
        if (document.isClosed()) {
            document.setSuccess(false);
            document.setErrorMessage("Change is impossible. Document is closed");
            return document;
        }
        if (!companyId.equals(document.getHolderDoc().getId())) {
            document.setSuccess(false);
            document.setErrorMessage("Company can`t change this document, because is not an change-holder it in moment");
            return document;
        }

        document.setDateClose(LocalDateTime.now());
        documentRepository.saveAndFlush(document);
        final Document newDocument = new Document();
        newDocument.setCompany(document.getCounterCompany());
        newDocument.setCounterCompany(document.getCompany());
        newDocument.setConfirmCompany(false);
        newDocument.setConfirmCounterCompany(false);
        newDocument.setDateCreate(LocalDateTime.now());
        newDocument.setName(document.getName());
        newDocument.setText(document.getText());
        return documentRepository.save(document);
    }

    @Override
    public Document changeDocument(final String companyName,final  Document document) {

        final Company company = companyService.getCompany(companyName);
        if (!company.getSuccess()) {
            document.setSuccess(false);
            document.setErrorMessage(company.getErrorMessage());
            return document;
        }

        return changeDocument(company.getId(), document);
    }

    private String checkTotalDocWorkFlows(final Company company, final int restFlow) {
        String result = "success";
        if (documentService.getOpenDocs(company).size() > restFlow) {
            result = String.format("Company with name %s have doing greater than with %d doc workflow"
                            , company.getName(), restFlow);
        }
        return result;
    }
}