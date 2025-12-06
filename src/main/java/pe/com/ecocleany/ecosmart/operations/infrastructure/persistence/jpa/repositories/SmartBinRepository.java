package pe.com.ecocleany.ecosmart.operations.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.operations.domain.model.aggregates.SmartBin;

public interface SmartBinRepository extends JpaRepository<SmartBin, Long> {
}