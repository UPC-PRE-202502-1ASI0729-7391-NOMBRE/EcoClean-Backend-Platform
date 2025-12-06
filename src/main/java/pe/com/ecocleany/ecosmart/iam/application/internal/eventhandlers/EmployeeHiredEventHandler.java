package pe.com.ecocleany.ecosmart.iam.application.internal.eventhandlers;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pe.com.ecocleany.ecosmart.iam.domain.model.valueobjects.Roles;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import pe.com.ecocleany.ecosmart.shared.domain.model.events.EmployeeFiredEvent;
import pe.com.ecocleany.ecosmart.shared.domain.model.events.EmployeeHiredEvent;

@Component
public class EmployeeHiredEventHandler {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;

    public EmployeeHiredEventHandler(UserRepository userRepository, RoleRepository roleRepository, ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
    }

    @EventListener
    public void onHired(EmployeeHiredEvent event) {
        System.out.println("🔔 CONTRATADO: Usuario " + event.getUserId());
        var userOpt = userRepository.findById(event.getUserId());
        var roleOpt = roleRepository.findByName(Roles.ROLE_EMPLOYEE);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            var user = userOpt.get();
            user.getRoles().add(roleOpt.get());
            userRepository.save(user);

            profileRepository.findByUserId(event.getUserId()).ifPresent(profile -> {
                profile.updateAddress("Municipalidad de " + event.getMunicipality());
                profileRepository.save(profile);
            });
        }
    }

    @EventListener
    public void onFired(EmployeeFiredEvent event) {
        System.out.println("🔥 DESPEDIDO: Usuario " + event.getUserId());
        var userOpt = userRepository.findById(event.getUserId());
        var roleOpt = roleRepository.findByName(Roles.ROLE_EMPLOYEE);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            var user = userOpt.get();
            user.getRoles().remove(roleOpt.get());
            userRepository.save(user);

            profileRepository.findByUserId(event.getUserId()).ifPresent(profile -> {
                profile.updateAddress("Ciudadano (Ex-Empleado)");
                profileRepository.save(profile);
            });
        }
    }
}