package pe.com.ecocleany.ecosmart.profiles.interfaces.rest.resources;

public record CreateProfileResource(
        String firstName,
        String lastName,
        String email,
        String district,
        String photoUrl,
        Long userId
) {}
