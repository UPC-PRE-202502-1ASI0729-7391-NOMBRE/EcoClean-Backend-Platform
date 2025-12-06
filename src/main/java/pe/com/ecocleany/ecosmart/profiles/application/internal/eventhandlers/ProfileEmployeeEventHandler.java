package pe.com.ecocleany.ecosmart.profiles.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pe.com.ecocleany.ecosmart.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import pe.com.ecocleany.ecosmart.shared.domain.model.events.EmployeeHiredEvent;

@Component
public class ProfileEmployeeEventHandler {

    private final ProfileRepository profileRepository;

    public ProfileEmployeeEventHandler(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @EventListener
    public void on(EmployeeHiredEvent event) {
        System.out.println("📝 PROFILES: Actualizando lugar de trabajo para usuario " + event.getUserId());
        profileRepository.findByUserId(event.getUserId()).ifPresent(profile -> {
            profile.updateAddress("Municipalidad de " + event.getMunicipality());
            profileRepository.save(profile);
        });
    }
}