package pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.iam.domain.model.entities.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.valueobjects.Roles;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(Roles name);
}