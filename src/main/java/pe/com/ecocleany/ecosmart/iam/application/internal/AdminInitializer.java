package pe.com.ecocleany.ecosmart.iam.application.internal;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pe.com.ecocleany.ecosmart.iam.domain.model.aggregates.User;
import pe.com.ecocleany.ecosmart.iam.domain.model.entities.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.valueobjects.Roles;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class AdminInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {

        Role adminRole = roleRepository.findByName(Roles.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(Roles.ROLE_ADMIN)));

        Optional<User> adminOpt = userRepository.findByUsername("admin");

        if (adminOpt.isEmpty()) {

            User admin = new User(
                    "admin",
                    "admin@ecosmart.com",
                    passwordEncoder.encode("admin123"),
                    "Administrador",
                    "Principal",
                    "Lima",
                    new HashSet<>(Set.of(adminRole))
            );

            userRepository.save(admin);
            System.out.println("🌟 ADMIN creado automáticamente");
        } else {
            System.out.println("✔ ADMIN ya existe, no se crea nuevamente");
        }
    }
}
