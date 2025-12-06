package pe.com.ecocleany.ecosmart.communication.domain.model.commands;

public record CreateMessageCommand(String content, String targetMunicipality, Long senderId) {}