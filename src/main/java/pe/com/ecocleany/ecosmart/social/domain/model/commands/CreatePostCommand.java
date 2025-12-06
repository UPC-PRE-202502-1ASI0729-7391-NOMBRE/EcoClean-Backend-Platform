package pe.com.ecocleany.ecosmart.social.domain.model.commands;

public record CreatePostCommand(String content, String imageUrl, String district, Long authorId) {}