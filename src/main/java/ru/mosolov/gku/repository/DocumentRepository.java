package ru.mosolov.gku.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mosolov.gku.models.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Optional<Document> findByName(final String name);
    @Query(value = "select d " +
            "from Document d " +
            "where d.dateClose is null " +
            "and d.dateDelete is null " +
            "and (" +
            "(d.company.id = ?1 and not d.confirmCompany) " +
            "or (d.counterCompany.id = ?1 and not d.confirmCounterCompany and d.confirmCompany))")
    Optional<List<Document>> getOpenDocs (final Integer companyId);

    @Query(value = "select d " +
            "from Document d " +
            "where d.dateClose is null " +
            "and d.dateDelete is null " +
            "and (" +
            "(d.company.id = ?1 and not d.confirmCompany) " +
            "or (d.counterCompany.id = ?1 and not d.confirmCounterCompany and d.confirmCompany)) " +
            "and d.dateCreate between ?2 and ?3")
    Optional<List<Document>> getOpenDocsBetweenDates(final Integer companyId
            , final LocalDateTime startDate, final LocalDateTime endDate);

    @Query(value = "select d " +
            "from Document d " +
            "where d.dateClose is null " +
            "and d.dateDelete is null " +
            "and (" +
            "(d.company.id = ?1 and d.counterCompany.id = ?2) " +
            "or (d.company.id = ?2 and d.counterCompany.id = ?1))")
    Optional<List<Document>> getOpenDocsBetweenTwoCompanies(final Integer compOneId, final Integer compTwoId);

}
