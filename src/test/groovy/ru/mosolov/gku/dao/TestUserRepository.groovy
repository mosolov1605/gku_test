package ru.mosolov.gku.dao

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import ru.mosolov.gku.GkuTest
import ru.mosolov.gku.models.Company
import ru.mosolov.gku.models.User
import ru.mosolov.gku.repository.CompanyRepository
import ru.mosolov.gku.repository.UserRepository
import ru.mosolov.gku.services.UserService
import spock.lang.Specification

@SpringBootTest(classes = [GkuTest.class],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
@Transactional
@Rollback
class TestUserRepository extends Specification{

    @Autowired
    UserRepository userRepository

    @Autowired
    UserService userService

    @Autowired
    CompanyRepository companyRepository

    def "find by name user from Repo" () {

        def compName = "TestTest"
        def comp = new Company()
        comp.name = compName
        companyRepository.save(comp)
        def usrName = "TestUsr"
        def pswUsr = "test"
        def user = new User();
        user.name = usrName
        user.password = pswUsr
        user.company = comp
        user.role = "USER"
        userRepository.save(user)

        when:
            def usrDb = userRepository.findByName(usrName).orElse(null)
        then:
            user.company.name == usrDb.company.name
            user.name == usrDb.name
    }

    def "find user by name from service"() {
        def compName = "TestTest"
        def comp = new Company()
        comp.name = compName
        companyRepository.save(comp)
        def usrName = "TestUsr"
        def pswUsr = "test"
        def user = new User();
        user.name = usrName
        user.password = pswUsr
        user.company = comp
        user.role = "USER"
        userRepository.save(user)

        when:
        def userSrv = userService.getUser(usrName)
        then:
        user.company.name == userSrv.company.name
        user.name == userSrv.name
    }

    def "error find user by name from service"(){
        def compName = "TestTest"
        def comp = new Company()
        comp.name = compName
        companyRepository.save(comp)
        def usrName = "TestUsr"
        def pswUsr = "test"
        def user = new User();
        user.name = usrName
        user.password = pswUsr
        user.company = comp
        user.role = "USER"
        userRepository.save(user)

        when:
            def userSrv = userService.getUser(usrName+"ERROR")
        then:
            !userSrv.success
    }
}
