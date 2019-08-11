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
            "inner join d.company c "+
            "inner join d.counterCompany cc " +
            "where d.dateClose is null " +
            "and d.dateDelete is null "+
            "and (" +
            "(c.id = ?1 and d.confirmCompany <> TRUE) " +
            "or (cc.id = ?1 and d.confirmCounterCompany <> TRUE and d.confirmCompany = TRUE))")
    Optional<List<Document>> getOpenDocs (final Integer companyId);

    @Query(value = "select d " +
            "from Document d " +
            "inner join d.company c "+
            "inner join d.counterCompany cc " +
            "where d.dateClose is null " +
            "and d.dateDelete is null " +
            "and (" +
            "(c.id = ?1 and d.confirmCompany <> TRUE) " +
            "or (cc.id = ?1 and d.confirmCounterCompany <> TRUE and d.confirmCompany = TRUE)) " +
            "and d.dateCreate between ?2 and ?3")
    Optional<List<Document>> getOpenDocsBetweenDates(final Integer companyId
            , final LocalDateTime startDate, final LocalDateTime endDate);

    @Query(value = "select d " +
            "from Document d " +
            "inner join d.company c "+
            "inner join d.counterCompany cc " +
            "where d.dateClose is null " +
            "and d.dateDelete is null " +
            "and (" +
            "(c.id = ?1 and cc.id = ?2) " +
            "or (c.id = ?2 and cc.id = ?1))")
    Optional<List<Document>> getOpenDocsBetweenTwoCompanies(final Integer compOneId, final Integer compTwoId);

}
