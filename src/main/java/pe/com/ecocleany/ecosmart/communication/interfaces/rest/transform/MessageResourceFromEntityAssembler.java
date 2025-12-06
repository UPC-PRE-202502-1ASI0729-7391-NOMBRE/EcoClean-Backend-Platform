package pe.com.ecocleany.ecosmart.communication.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.com.ecocleany.ecosmart.communication.domain.model.aggregates.Message;
import pe.com.ecocleany.ecosmart.communication.interfaces.rest.resources.MessageResource;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;

@Component
public class MessageResourceFromEntityAssembler {

    private final UserRepository userRepository;

    public MessageResourceFromEntityAssembler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public MessageResource toResource(Message entity) {

        String email = "Oficial";
        if (!entity.isResponse()) {
            email = userRepository.findById(entity.getSenderId())
                    .map(u -> u.getEmail())
                    .orElse("Usuario Desconocido");
        }

        return new MessageResource(
                entity.getId(),
                entity.getContent(),
                entity.isResponse() ? "MUNICIPALIDAD" : "Usuario " + entity.getSenderId(),
                entity.getSenderId(),
                email,
                entity.isResponse(),
                entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null
        );
    }
}