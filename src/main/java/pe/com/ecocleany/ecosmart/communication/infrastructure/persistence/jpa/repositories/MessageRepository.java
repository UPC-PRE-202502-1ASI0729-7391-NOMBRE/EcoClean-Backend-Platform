package pe.com.ecocleany.ecosmart.communication.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.communication.domain.model.aggregates.Message;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByTargetMunicipality(String municipality);
}