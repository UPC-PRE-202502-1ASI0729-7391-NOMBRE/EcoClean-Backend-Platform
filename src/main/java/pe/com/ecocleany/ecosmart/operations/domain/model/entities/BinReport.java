package pe.com.ecocleany.ecosmart.operations.domain.model.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.ecocleany.ecosmart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
public class BinReport extends AuditableAbstractAggregateRoot<BinReport> {

    private String message;
    private String photoUrl;
    private String status;
    private Long smartBinId;
    private Long reporterId;
    private String district;

    public BinReport(
            String message,
            String photoUrl,
            Long smartBinId,
            Long reporterId,
            String district
    ) {
        this.message = message;
        this.photoUrl = photoUrl;
        this.smartBinId = smartBinId;
        this.reporterId = reporterId;
        this.district = district;
        this.status = "PENDING";
    }
}
