package pe.com.ecocleany.ecosmart.iam.domain.model.commands;
import pe.com.ecocleany.ecosmart.iam.domain.model.valueobjects.Roles;
import java.util.Set;

public record SignUpCommand(
        String username,
        String email,
        String password,
        String firstName,
        String lastName,
        String district
) {}
