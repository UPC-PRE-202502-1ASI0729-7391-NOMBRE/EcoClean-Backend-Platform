package pe.com.ecocleany.ecosmart.iam.interfaces.rest.transform;

import pe.com.ecocleany.ecosmart.iam.domain.model.aggregates.User;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.resources.AuthenticatedUserResource;

import java.util.Set;
import java.util.stream.Collectors;

public class AuthenticatedUserResourceFromEntityAssembler {

    public static AuthenticatedUserResource toResourceFromEntity(
            User user,
            String token,
            String municipality
    ) {

        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toSet());

        return new AuthenticatedUserResource(
                user.getId(),
                user.getUsername(),
                token,
                roles,
                municipality
        );
    }
}
