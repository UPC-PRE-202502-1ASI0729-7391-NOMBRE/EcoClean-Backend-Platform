package pe.com.ecocleany.ecosmart.iam.interfaces.rest.resources;

import java.util.Set;

public record SignUpResource(
        String username,
        String email,
        String password,
        String firstName,
        String lastName,
        String district
) {}
