package pe.com.ecocleany.ecosmart.social.interfaces.rest.resources;

import java.util.Date;

public record PostResource(
        Long id,
        String content,
        String imageUrl,
        String district,
        Integer likes,
        boolean isOfficial,
        String authorName,
        Long authorId,
        Date createdAt
) {}