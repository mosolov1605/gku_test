package ru.mosolov.gku.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mosolov.gku.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
