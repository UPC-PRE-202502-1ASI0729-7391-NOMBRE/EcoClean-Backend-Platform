package pe.com.ecocleany.ecosmart.communication.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.ecocleany.ecosmart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
public class Message extends AuditableAbstractAggregateRoot<Message> {
    private String content;
    private Long senderId;
    private String targetMunicipality;
    private boolean isResponse;

    public Message(String content, Long senderId, String targetMunicipality) {
        this.content = content;
        this.senderId = senderId;
        this.targetMunicipality = targetMunicipality;
        this.isResponse = false;
    }
    public void markAsResponse() {
        this.isResponse = true;
    }
}