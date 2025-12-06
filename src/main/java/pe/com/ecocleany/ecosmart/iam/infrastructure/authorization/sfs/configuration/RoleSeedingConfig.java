package pe.com.ecocleany.ecosmart.iam.infrastructure.authorization.sfs.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.com.ecocleany.ecosmart.iam.domain.model.entities.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.valueobjects.Roles;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.RoleRepository;

import java.util.Arrays;

@Configuration
public class RoleSeedingConfig {

    private static final Logger logger = LoggerFactory.getLogger(RoleSeedingConfig.class);

    @Bean
    public CommandLineRunner seedRoles(RoleRepository roleRepository) {
        return args -> {
            Arrays.stream(Roles.values()).forEach(roleName -> {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    roleRepository.save(new Role(roleName));
                    logger.info("Seeded role: " + roleName);
                }
            });
        };
    }
}