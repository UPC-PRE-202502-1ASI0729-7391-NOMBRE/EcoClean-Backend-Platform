package pe.com.ecocleany.ecosmart.profiles.interfaces.rest.resources;

public record UpdateProfileResource(
        String firstName,
        String lastName,
        String district,
        String photoUrl
) {}
