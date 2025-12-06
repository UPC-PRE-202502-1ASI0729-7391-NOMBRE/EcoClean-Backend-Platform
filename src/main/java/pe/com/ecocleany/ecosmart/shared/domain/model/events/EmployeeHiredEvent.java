package pe.com.ecocleany.ecosmart.shared.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmployeeHiredEvent extends ApplicationEvent {
    private final Long userId;
    private final String municipality;

    public EmployeeHiredEvent(Object source, Long userId, String municipality) {
        super(source);
        this.userId = userId;
        this.municipality = municipality;
    }
}