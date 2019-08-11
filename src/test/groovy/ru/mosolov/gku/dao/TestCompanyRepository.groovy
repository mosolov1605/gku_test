package ru.mosolov.gku.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import ru.mosolov.gku.GkuTest
import ru.mosolov.gku.models.Company
import ru.mosolov.gku.repository.CompanyRepository
import ru.mosolov.gku.services.CompanyService
import spock.lang.Specification

@SpringBootTest(classes = [GkuTest.class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@Transactional
@Rollback
class TestCompanyRepository extends Specification {

    @Autowired
    CompanyRepository companyRepository

    @Autowired
    CompanyService companyService

    def "find by name company" () {
        def nameComp = "TestTest"
        def company = new Company()
        company.name = nameComp
        def savedCompany = companyRepository.save(company)

        when: "load company by name from repo"
            def companyFromDbRepo = companyRepository.findByName(nameComp).orElse(null)
        then: "company names should be equals"
            savedCompany.name == companyFromDbRepo?.name
            savedCompany.id == companyFromDbRepo.id

        when: "load company by name from service"
            def companyFromDbSrv = companyService.getCompany(nameComp)
        then:
            savedCompany.name == companyFromDbSrv?.name
            savedCompany.id == companyFromDbSrv.id
    }

    def "error find company by name"() {
        def nameComp = "TestTest"
        def company = new Company()
        company.name = nameComp
        def savedCompany = companyRepository.save(company)

        when:
            def companyFromDbSrv = companyService.getCompany(nameComp+"Error")
        then:
            savedCompany.name != companyFromDbSrv?.name
            !companyFromDbSrv.success
    }
}
