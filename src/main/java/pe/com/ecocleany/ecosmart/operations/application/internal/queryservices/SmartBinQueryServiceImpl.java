package pe.com.ecocleany.ecosmart.operations.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.operations.domain.model.aggregates.SmartBin;
import pe.com.ecocleany.ecosmart.operations.domain.model.entities.BinReport;
import pe.com.ecocleany.ecosmart.operations.domain.model.queries.GetAllReportsQuery;
import pe.com.ecocleany.ecosmart.operations.domain.model.queries.GetAllSmartBinsQuery;
import pe.com.ecocleany.ecosmart.operations.infrastructure.persistence.jpa.repositories.BinReportRepository;
import pe.com.ecocleany.ecosmart.operations.infrastructure.persistence.jpa.repositories.SmartBinRepository;

import java.util.List;

@Service
public class SmartBinQueryServiceImpl {

    private final SmartBinRepository smartBinRepository;
    private final BinReportRepository binReportRepository;

    public SmartBinQueryServiceImpl(SmartBinRepository smartBinRepository, BinReportRepository binReportRepository) {
        this.smartBinRepository = smartBinRepository;
        this.binReportRepository = binReportRepository;
    }

    public List<SmartBin> handle(GetAllSmartBinsQuery query) {
        return smartBinRepository.findAll();
    }

    public List<BinReport> handle(GetAllReportsQuery query) {
        return binReportRepository.findAll();
    }
}