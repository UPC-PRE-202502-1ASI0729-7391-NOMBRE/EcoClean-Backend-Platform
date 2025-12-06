package pe.com.ecocleany.ecosmart.communication.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.communication.domain.model.aggregates.Message;
import pe.com.ecocleany.ecosmart.communication.domain.model.queries.GetMessagesByMunicipalityQuery;
import pe.com.ecocleany.ecosmart.communication.infrastructure.persistence.jpa.repositories.MessageRepository;

import java.util.List;

@Service
public class MessageQueryServiceImpl {

    private final MessageRepository messageRepository;

    public MessageQueryServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> handle(GetMessagesByMunicipalityQuery query) {
        return messageRepository.findByTargetMunicipality(query.municipality());
    }
}