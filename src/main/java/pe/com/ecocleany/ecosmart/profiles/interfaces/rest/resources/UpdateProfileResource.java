package pe.com.ecocleany.ecosmart.profiles.interfaces.rest.resources;

public record UpdateProfileResource(
        String firstName,
        String lastName,
        String email,
        String district,
        String photoUrl,
        String password
) {}