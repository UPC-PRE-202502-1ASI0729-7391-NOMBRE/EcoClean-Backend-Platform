package pe.com.ecocleany.ecosmart.iam.application.internal.commandservices;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.iam.domain.model.aggregates.User;
import pe.com.ecocleany.ecosmart.iam.domain.model.commands.SignInCommand;
import pe.com.ecocleany.ecosmart.iam.domain.model.commands.SignUpCommand;
import pe.com.ecocleany.ecosmart.iam.domain.model.entities.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.valueobjects.Roles;
import pe.com.ecocleany.ecosmart.iam.domain.services.UserCommandService;
import pe.com.ecocleany.ecosmart.iam.infrastructure.hashing.bcrypt.BCryptHashingService;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.iam.infrastructure.tokens.jwt.BearerTokenService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptHashingService hashingService;
    private final BearerTokenService tokenService;

    public UserCommandServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            BCryptHashingService hashingService,
            BearerTokenService tokenService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {

        if (userRepository.existsByUsername(command.username()))
            return Optional.empty();

        if (userRepository.existsByEmail(command.email()))
            return Optional.empty();

        Role roleUser = roleRepository.findByName(Roles.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("❌ ROLE_USER no existe en la base de datos"));

        Set<Role> roles = new HashSet<>();
        roles.add(roleUser);

        var user = new User(
                command.username(),
                command.email(),
                hashingService.encode(command.password()),
                command.firstName(),
                command.lastName(),
                command.district(),
                roles
        );

        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand command) {

        var user = userRepository.findByUsername(command.username());

        if (user.isEmpty()
                || !hashingService.matches(command.password(), user.get().getPassword())) {

            return Optional.empty();
        }

        String token = tokenService.generateToken(command.username());

        return Optional.of(new ImmutablePair<>(user.get(), token));
    }
}
