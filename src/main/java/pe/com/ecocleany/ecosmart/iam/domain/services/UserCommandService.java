package pe.com.ecocleany.ecosmart.iam.domain.services;

import org.apache.commons.lang3.tuple.ImmutablePair;
import pe.com.ecocleany.ecosmart.iam.domain.model.aggregates.User;
import pe.com.ecocleany.ecosmart.iam.domain.model.commands.SignInCommand;
import pe.com.ecocleany.ecosmart.iam.domain.model.commands.SignUpCommand;

import java.util.Optional;

public interface UserCommandService {
    Optional<User> handle(SignUpCommand command);
    Optional<ImmutablePair<User, String>> handle(SignInCommand command);
}