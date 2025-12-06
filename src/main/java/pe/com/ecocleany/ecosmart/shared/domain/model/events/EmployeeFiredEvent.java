package pe.com.ecocleany.ecosmart.shared.domain.model.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class EmployeeFiredEvent extends ApplicationEvent {
    private final Long userId;

    public EmployeeFiredEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
    }
}