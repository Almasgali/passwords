package ru.almasgali.passwords.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.almasgali.passwords.model.data.PasswordEntity;
import ru.almasgali.passwords.model.dto.PasswordRequest;
import ru.almasgali.passwords.repository.PasswordRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class PasswordService {

    @PersistenceContext
    private EntityManager entityManager;
    private final PasswordRepository passwordRepository;

    @Autowired
    public PasswordService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    private AuditReader getAuditReader() {
        return AuditReaderFactory.get(entityManager);
    }

    public List<PasswordEntity> getPasswordRevs(long id) {
        AuditReader reader = getAuditReader();
        List<Number> revs = reader.getRevisions(PasswordEntity.class, id);
        List<PasswordEntity> result = new ArrayList<>();
        for (Number n : revs) {
            PasswordEntity password = reader.find(PasswordEntity.class, id, n);
            result.add(password);
        }
        result.sort(Comparator.comparing(PasswordEntity::getUpdatedAt).reversed());
        return result;
    }

    public PasswordEntity getPassword(long id) {
        return passwordRepository
                .findById(id)
                .orElseThrow();
    }

    public List<PasswordEntity> getPassword(String password, boolean contains) {
        if (contains) {
            return passwordRepository.findByPasswordContains(password);
        }
        return passwordRepository.findByPassword(password);
    }

    public void savePassword(PasswordRequest request) {
        PasswordEntity password = new PasswordEntity();
        password.setName(request.getName());
        password.setPassword(request.getPassword());
        password.setComment(request.getComment());
        password.setUpdatedAt(LocalDateTime.now());

        passwordRepository.save(password);
    }

    public void updatePassword(PasswordRequest request, long id) {
        PasswordEntity password = passwordRepository
                .findById(id)
                .orElseThrow();
        password.setName(request.getName());
        password.setPassword(request.getPassword());
        password.setComment(request.getComment());
        password.setUpdatedAt(LocalDateTime.now());

        passwordRepository.save(password);
    }

    public void deletePassword(long id) {
        passwordRepository.deleteById(id);
    }
}
