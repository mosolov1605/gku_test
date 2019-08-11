package ru.mosolov.gku.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.mosolov.gku.models.Document;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Optional<Document> findByName(final String name);
    @Query(value = "select d " +
            "from Document d " +
            "where d.dateClose is null " +
            "and (" +
            "(d.company = ?1 and not d.confirmCompany) " +
            "or (d.counterCompany = ?1 and not d.confirmCounterCompany and d.confirmCompany))")
    Optional<List<Document>> getOpenDocs (final Integer companyId);

}
