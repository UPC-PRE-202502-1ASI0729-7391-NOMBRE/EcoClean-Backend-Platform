package pe.com.ecocleany.ecosmart.recruitment.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.iam.domain.model.entities.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.valueobjects.Roles;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.aggregates.JobApplication;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.commands.CreateApplicationCommand;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.commands.FireEmployeeCommand;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.commands.UpdateApplicationStatusCommand;
import pe.com.ecocleany.ecosmart.recruitment.infrastructure.persistence.jpa.repositories.JobApplicationRepository;

import java.util.Optional;

@Service
public class RecruitmentCommandServiceImpl {

    private final JobApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileRepository profileRepository;

    public RecruitmentCommandServiceImpl(
            JobApplicationRepository applicationRepository,
            UserRepository userRepository,
            RoleRepository roleRepository,
            ProfileRepository profileRepository
    ) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileRepository = profileRepository;
    }

    public Long handle(CreateApplicationCommand command) {
        var application = new JobApplication(
                command.userId(),
                command.targetMunicipality(),
                command.description()
        );
        applicationRepository.save(application);
        return application.getId();
    }

    public Optional<JobApplication> handle(UpdateApplicationStatusCommand command) {
        return applicationRepository.findById(command.applicationId()).map(application -> {
            application.updateStatus(command.status());
            applicationRepository.save(application);

            if ("APPROVED".equalsIgnoreCase(command.status())) {
                contratarEmpleado(application.getApplicantId(), application.getTargetMunicipality());
            } else if ("REJECTED".equalsIgnoreCase(command.status())) {
            }
            return application;
        });
    }

    public void handle(FireEmployeeCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        var roleEmployee = roleRepository.findByName(Roles.ROLE_EMPLOYEE).orElseThrow();

        user.getRoles().remove(roleEmployee);
        userRepository.save(user);

        profileRepository.findByUserId(command.userId()).ifPresent(profile -> {
            profile.setWorkingMunicipality(null);
            profileRepository.save(profile);
        });
    }

    private void contratarEmpleado(Long userId, String municipality) {
        var user = userRepository.findById(userId).orElseThrow();
        var roleEmployee = roleRepository.findByName(Roles.ROLE_EMPLOYEE).orElseThrow();

        // 1. Dar Rol
        user.addRole(roleEmployee);
        userRepository.save(user);

        // 2. Actualizar Perfil (Asignar lugar de trabajo)
        profileRepository.findByUserId(userId).ifPresent(profile -> {
            profile.setWorkingMunicipality(municipality);
            profileRepository.save(profile);
        });
    }
}