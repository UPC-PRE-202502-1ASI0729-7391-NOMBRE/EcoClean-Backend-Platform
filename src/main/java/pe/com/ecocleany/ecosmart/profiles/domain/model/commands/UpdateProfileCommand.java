package pe.com.ecocleany.ecosmart.profiles.domain.model.commands;

public record UpdateProfileCommand(
        Long userId,
        String firstName,
        String lastName,
        String district,
        String photoUrl
) {}
