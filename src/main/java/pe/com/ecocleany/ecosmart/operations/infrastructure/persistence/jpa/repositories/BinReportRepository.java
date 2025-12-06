package pe.com.ecocleany.ecosmart.operations.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.operations.domain.model.entities.BinReport;
import java.util.List;

public interface BinReportRepository extends JpaRepository<BinReport, Long> {
    List<BinReport> findBySmartBinId(Long smartBinId);
}