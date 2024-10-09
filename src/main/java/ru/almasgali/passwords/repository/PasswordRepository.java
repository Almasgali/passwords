package ru.almasgali.passwords.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.almasgali.passwords.model.data.PasswordEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PasswordRepository extends JpaRepository<PasswordEntity, Long> {
    List<PasswordEntity> findByPassword(String password);

    List<PasswordEntity> findByPasswordContains(String password);
}
