package pe.com.ecocleany.ecosmart.communication.interfaces.rest.resources;

public record MessageResource(
        Long id,
        String content,
        String sender,
        Long senderId,
        String senderEmail,
        boolean isOfficial,
        String createdAt
) {}