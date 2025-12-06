package pe.com.ecocleany.ecosmart.operations.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.operations.domain.model.aggregates.SmartBin;
import pe.com.ecocleany.ecosmart.operations.domain.model.commands.CreateReportCommand;
import pe.com.ecocleany.ecosmart.operations.domain.model.commands.CreateSmartBinCommand;
import pe.com.ecocleany.ecosmart.operations.domain.model.commands.DispatchTruckCommand;
import pe.com.ecocleany.ecosmart.operations.domain.model.entities.BinReport;
import pe.com.ecocleany.ecosmart.operations.infrastructure.persistence.jpa.repositories.BinReportRepository;
import pe.com.ecocleany.ecosmart.operations.infrastructure.persistence.jpa.repositories.SmartBinRepository;

@Service
public class SmartBinCommandServiceImpl {

    private final SmartBinRepository smartBinRepository;
    private final BinReportRepository binReportRepository;

    public SmartBinCommandServiceImpl(SmartBinRepository smartBinRepository, BinReportRepository binReportRepository) {
        this.smartBinRepository = smartBinRepository;
        this.binReportRepository = binReportRepository;
    }

    public Long handle(CreateSmartBinCommand command) {
        var smartBin = new SmartBin(
                command.name(),
                command.latitude(),
                command.longitude(),
                command.district() // ⬅ NUEVO
        );
        smartBinRepository.save(smartBin);
        return smartBin.getId();
    }

    public Long handle(CreateReportCommand command) {
        var report = new BinReport(
                command.message(),
                command.photoUrl(),
                command.smartBinId(),
                command.reporterId(),
                command.district() // ⬅ NUEVO
        );
        binReportRepository.save(report);
        return report.getId();
    }

    public String handle(DispatchTruckCommand command) {
        return "🚚 Unidad de recolección despachada exitosamente al distrito: " + command.district();
    }
}
