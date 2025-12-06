package pe.com.ecocleany.ecosmart.recruitment.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.ecocleany.ecosmart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
public class JobApplication extends AuditableAbstractAggregateRoot<JobApplication> {

    private Long applicantId;
    private String targetMunicipality;
    private String status;
    private String description;

    public JobApplication(Long applicantId, String targetMunicipality, String description) {
        this.applicantId = applicantId;
        this.targetMunicipality = targetMunicipality;
        this.description = description;
        this.status = "PENDING";
    }

    public void updateStatus(String status) {
        this.status = status;
    }
}