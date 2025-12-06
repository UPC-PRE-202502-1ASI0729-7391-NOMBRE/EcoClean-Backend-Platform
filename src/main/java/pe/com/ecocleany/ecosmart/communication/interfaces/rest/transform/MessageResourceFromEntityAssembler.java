package pe.com.ecocleany.ecosmart.communication.interfaces.rest.transform;

import pe.com.ecocleany.ecosmart.communication.domain.model.aggregates.Message;
import pe.com.ecocleany.ecosmart.communication.interfaces.rest.resources.MessageResource;

public class MessageResourceFromEntityAssembler {
    public static MessageResource toResource(Message entity) {
        return new MessageResource(
                entity.getId(),
                entity.getContent(),
                entity.isResponse() ? "MUNICIPALIDAD" : "Usuario " + entity.getSenderId(),
                entity.isResponse()
        );
    }
}