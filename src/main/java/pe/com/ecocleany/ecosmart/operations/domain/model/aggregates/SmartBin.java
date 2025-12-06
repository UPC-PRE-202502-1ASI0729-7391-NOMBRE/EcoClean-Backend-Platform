package pe.com.ecocleany.ecosmart.operations.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import pe.com.ecocleany.ecosmart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
public class SmartBin extends AuditableAbstractAggregateRoot<SmartBin> {

    private String name;
    private Double latitude;
    private Double longitude;
    private Double fillLevel;
    private String status;

    private String district;
    public SmartBin() {}

    public SmartBin(String name, Double latitude, Double longitude, String district) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.district = district;
        this.fillLevel = 0.0;
        this.status = "NORMAL";
    }

    public void updateFillLevel(Double newLevel) {
        this.fillLevel = newLevel;
        if (this.fillLevel >= 90.0) {
            this.status = "FULL";
        } else {
            this.status = "NORMAL";
        }
    }
}
