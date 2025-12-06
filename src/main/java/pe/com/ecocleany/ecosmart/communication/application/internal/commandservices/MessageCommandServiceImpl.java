package pe.com.ecocleany.ecosmart.communication.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.communication.domain.model.aggregates.Message;
import pe.com.ecocleany.ecosmart.communication.domain.model.commands.CreateMessageCommand;
import pe.com.ecocleany.ecosmart.communication.domain.model.commands.ReplyMessageCommand;
import pe.com.ecocleany.ecosmart.communication.infrastructure.persistence.jpa.repositories.MessageRepository;
import pe.com.ecocleany.ecosmart.communication.interfaces.acl.IamContextFacade;

@Service
public class MessageCommandServiceImpl {

    private final MessageRepository messageRepository;
    private final IamContextFacade iamContextFacade;

    public MessageCommandServiceImpl(MessageRepository messageRepository, IamContextFacade iamContextFacade) {
        this.messageRepository = messageRepository;
        this.iamContextFacade = iamContextFacade;
    }

    public Long handle(CreateMessageCommand command) {
        if (!iamContextFacade.existsUser(command.senderId())) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        var message = new Message(command.content(), command.senderId(), command.targetMunicipality());
        messageRepository.save(message);
        return message.getId();
    }

    public Long handle(ReplyMessageCommand command) {
        var message = new Message(command.content(), command.employeeId(), command.targetMunicipality());
        message.markAsResponse();
        messageRepository.save(message);
        return message.getId();
    }
}