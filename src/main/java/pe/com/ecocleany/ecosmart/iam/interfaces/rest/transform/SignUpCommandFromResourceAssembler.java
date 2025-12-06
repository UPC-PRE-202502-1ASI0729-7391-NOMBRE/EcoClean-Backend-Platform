package pe.com.ecocleany.ecosmart.iam.interfaces.rest.transform;

import pe.com.ecocleany.ecosmart.iam.domain.model.commands.SignUpCommand;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.resources.SignUpResource;

public class SignUpCommandFromResourceAssembler {

    public static SignUpCommand toCommandFromResource(SignUpResource resource) {

        return new SignUpCommand(
                resource.username(),
                resource.email(),
                resource.password(),
                resource.firstName(),
                resource.lastName(),
                resource.district()
        );
    }
}
