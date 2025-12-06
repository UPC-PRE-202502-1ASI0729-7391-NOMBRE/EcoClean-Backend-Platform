package pe.com.ecocleany.ecosmart.profiles.domain.model.commands;

public record CreateProfileCommand(
        String firstName,
        String lastName,
        String email,
        String district,
        String photoUrl,
        Long userId
) {}
