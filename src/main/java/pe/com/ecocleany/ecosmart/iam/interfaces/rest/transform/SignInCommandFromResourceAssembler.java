package pe.com.ecocleany.ecosmart.iam.interfaces.rest.transform;

import pe.com.ecocleany.ecosmart.iam.domain.model.commands.SignInCommand;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.username(), resource.password());
    }
}