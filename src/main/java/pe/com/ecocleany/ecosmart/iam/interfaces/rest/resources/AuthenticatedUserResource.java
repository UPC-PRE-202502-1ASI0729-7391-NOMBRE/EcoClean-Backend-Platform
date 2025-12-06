package pe.com.ecocleany.ecosmart.iam.interfaces.rest.resources;
import java.util.Set;

public record AuthenticatedUserResource(
        Long id,
        String username,
        String token,
        Set<String> roles,
        String municipality
) {}
