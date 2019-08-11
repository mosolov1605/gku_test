package ru.mosolov.gku.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import ru.mosolov.gku.GkuTest
import ru.mosolov.gku.models.Company
import ru.mosolov.gku.models.Document
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
class TestDocumentRepo extends Specification{

    @Autowired
    private DocumentRepository documentRepository

    @Autowired
    private TxDocumentService txDocumentService

    @Autowired
    private CompanyRepository companyRepository

    private Company comp1
    private Company comp2
    private Company comp3

    def setup(){
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

    def "test getOpenDocs"() {

        List<Document> docs = []
        for (int i = 0; i<10; i++){
            def doc = new Document()
            if (i%2 == 0) {
                doc.setCompany(comp1)
                doc.setCounterCompany(comp2)
            } else {
                doc.setCompany(comp2)
                doc.setCounterCompany(comp1)
            }

            doc.setDateCreate(LocalDateTime.now())
            doc.setName("doc #"+i)
            doc.setText("Testtest")
            docs.add(doc)
        }
        docs.eachWithIndex{ doc, idx ->
                if (idx == 0){
                    doc.confirmCompany = true
                }
                documentRepository.save(doc)
        }

        when:
            List<Document> comp1Docs = documentRepository.getOpenDocs(comp1.id).orElse(new ArrayList<Document>())
            List<Document> comp1Docs2 = documentRepository.getOpenDocs(comp2.id).orElse(new ArrayList<Document>())
        then:
            comp1Docs?.size() == 4
            comp1Docs2?.size() == 6
    }

    def "test getOpenDocsBetweenTwoCompanies"() {
        List<Document> docs = []
        for (int i = 0; i<10; i++){
            def doc = new Document()
            if (i%2 == 0) {
                doc.setCompany(comp1)
                doc.setCounterCompany(comp2)
            } else if (i%3 == 0){
                doc.setCompany(comp3)
                doc.setCounterCompany(comp1)
            } else {
                doc.setCompany(comp2)
                doc.setCounterCompany(comp1)
            }

            doc.setDateCreate(LocalDateTime.now())
            doc.setName("doc #"+i)
            doc.setText("Testtest")
            docs.add(doc)
        }
        docs.eachWithIndex{ doc, idx ->
            if (idx == 0){
                doc.confirmCompany = true
            }
            documentRepository.save(doc)
        }

        when:
            List<Document> comp12Docs = documentRepository.getOpenDocsBetweenTwoCompanies(comp1.id,comp2.id).orElse(new ArrayList<Document>())
        then:
        comp12Docs?.size() == 8
    }

    def "test getOpenDocsBetweenDates"() {
        List<Document> docs = []
        for (int i = 0; i<10; i++){
            def doc = new Document()
            doc.setCompany(comp1)
            doc.setCounterCompany(comp2)
            doc.setDateCreate(LocalDateTime.now().minusMinutes(i*20))
            doc.setName("doc #"+i)
            doc.setText("Testtest")
            docs.add(doc)
        }
        docs.eachWithIndex{ doc, idx ->
            if (idx == 0){
                doc.confirmCompany = true
            }
            documentRepository.save(doc)
        }

        when:
        List<Document> comp1Docs = documentRepository.getOpenDocsBetweenDates(comp1.id
                , LocalDateTime.now().minusHours(1), LocalDateTime.now()).orElse(new ArrayList<Document>())
        then:
            comp1Docs?.size() == 2
    }
}
