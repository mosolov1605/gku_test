package ru.mosolov.gku.srv

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import ru.mosolov.gku.GkuTest
import ru.mosolov.gku.models.Company
import ru.mosolov.gku.models.Document
import ru.mosolov.gku.other.PropertySource
import ru.mosolov.gku.repository.CompanyRepository
import ru.mosolov.gku.repository.DocumentRepository
import ru.mosolov.gku.services.TxDocumentService
import spock.lang.Specification

import java.time.LocalDateTime

@SpringBootTest(classes = [GkuTest.class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@Transactional
@Rollback
class TestTxDocumentSrv extends Specification{

    @Autowired
    private DocumentRepository documentRepository

    @Autowired
    private CompanyRepository companyRepository

    @Autowired
    private TxDocumentService txDocumentService

    @Autowired
    private PropertySource propertySource

    private Company comp1
    private Company comp2
    private Company comp3
    def setup() {
        comp1 = new Company();
        comp2 = new Company();
        comp3 = new Company();

        comp1.name = "company"
        comp2.name = "counterCompany"
        comp3.name = "counterXoCompany"
        companyRepository.save(comp1)
        companyRepository.save(comp2)
        companyRepository.save(comp3)
    }

    def saveDocumentTest(){
        def doc = new Document()
        doc.company=comp1
        doc.counterCompany=comp2
        doc.setDateCreate(LocalDateTime.now())
        doc.setName("doc #")
        doc.setText("Testtest")

        when:
            def result = txDocumentService.saveDocument(doc)
        then:
            result.success
    }

    def saveDocumentTestErrorEqualsComps(){
        def doc = new Document()
        doc.company=comp1
        doc.counterCompany=comp1
        doc.setDateCreate(LocalDateTime.now())
        doc.setName("doc #")
        doc.setText("Testtest")

        when:
        def result = txDocumentService.saveDocument(doc)
        then:
        !result.success
    }

    def saveDocumentTestErrorEqualsName(){
        def doc = new Document()
        doc.company=comp1
        doc.counterCompany=comp2
        doc.setDateCreate(LocalDateTime.now())
        doc.setName("doc #")
        doc.setText("Testtest")
        documentRepository.saveAndFlush(doc)
        def doc2 = new Document()
        doc2.company=comp1
        doc2.counterCompany=comp2
        doc2.setDateCreate(LocalDateTime.now())
        doc2.setName("doc #")
        doc2.setText("Testtest")

        when:
        def result = txDocumentService.saveDocument(doc2)
        then:
        !result.success
    }

    def saveDocumentTestRestrBetweenComps(){
        def doc = new Document()
        doc.company=comp1
        doc.counterCompany=comp2
        doc.setDateCreate(LocalDateTime.now())
        doc.setName("doc 1")
        doc.setText("Testtest")
        documentRepository.saveAndFlush(doc)
        def doc2 = new Document()
        doc2.company=comp2
        doc2.counterCompany=comp1
        doc2.setDateCreate(LocalDateTime.now())
        doc2.setName("doc 2")
        doc2.setText("Testtest")

        when:
            def oldVar = propertySource.betweenDocWorkflow
            propertySource.betweenDocWorkflow = '1'
            def result = txDocumentService.saveDocument(doc2)
            propertySource.betweenDocWorkflow = oldVar
        then:
            !result.success
    }

    def saveDocumentTestRestrCountDocs(){

        List<Document> docs = []
        for (int i = 0; i<5; i++){
            def doc = new Document()

            doc.setCompany(comp1)
            doc.setCounterCompany(comp2)
            doc.setDateCreate(LocalDateTime.now())
            doc.setName("doc #"+i)
            doc.setText("Testtest")
            docs.add(doc)
        }
        documentRepository.saveAll(docs)

        def result = new Document()

        result.setCompany(comp1)
        result.setCounterCompany(comp2)
        result.setDateCreate(LocalDateTime.now())
        result.setName("doc TEST")
        result.setText("Testtest")

        when:
            def oldVarCount = propertySource.countRestrictionCreateDoc
            def oldVarTime = propertySource.minuteRestrictionCreateDoc
            def oldVar = propertySource.betweenDocWorkflow
            propertySource.betweenDocWorkflow = '100'
            propertySource.countRestrictionCreateDoc = '5'
            propertySource.minuteRestrictionCreateDoc = '60'
            def res = txDocumentService.saveDocument(result)
            propertySource.countRestrictionCreateDoc = oldVarCount
            propertySource.minuteRestrictionCreateDoc = oldVarTime
            propertySource.betweenDocWorkflow = oldVar
        then:
            !res.success
    }

    def saveDocumentsTest(){

    }
    def confirmDocumentTestCompany(){

    }
    def removeDocumentTestCompany(){

    }
    def changeDocumentTestCompany(){

    }
}
