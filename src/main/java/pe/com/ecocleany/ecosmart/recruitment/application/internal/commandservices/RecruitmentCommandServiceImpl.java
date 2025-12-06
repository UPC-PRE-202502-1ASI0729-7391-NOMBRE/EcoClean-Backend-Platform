package pe.com.ecocleany.ecosmart.recruitment.application.internal.commandservices;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import pe.com.ecocleany.ecosmart.iam.domain.model.entities.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.valueobjects.Roles;

import pe.com.ecocleany.ecosmart.profiles.application.internal.commandservices.ProfileCommandServiceImpl;
import pe.com.ecocleany.ecosmart.profiles.domain.model.commands.UpdateProfileMunicipalityCommand;

import pe.com.ecocleany.ecosmart.recruitment.domain.model.aggregates.JobApplication;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.commands.CreateApplicationCommand;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.commands.FireEmployeeCommand;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.commands.UpdateApplicationStatusCommand;
import pe.com.ecocleany.ecosmart.recruitment.infrastructure.persistence.jpa.repositories.JobApplicationRepository;

import pe.com.ecocleany.ecosmart.shared.domain.model.events.EmployeeFiredEvent;
import pe.com.ecocleany.ecosmart.shared.domain.model.events.EmployeeHiredEvent;

import java.util.Optional;

@Service
public class RecruitmentCommandServiceImpl {

    private final JobApplicationRepository repository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProfileCommandServiceImpl profileService;

    public RecruitmentCommandServiceImpl(
            JobApplicationRepository repository,
            ApplicationEventPublisher eventPublisher,
            UserRepository userRepository,
            RoleRepository roleRepository,
            ProfileCommandServiceImpl profileService
    ) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.profileService = profileService;
    }

    public Long handle(CreateApplicationCommand command) {
        var application = new JobApplication(
                command.userId(),
                command.targetMunicipality(),
                command.description()
        );
        repository.save(application);
        return application.getId();
    }

    public Optional<JobApplication> handle(UpdateApplicationStatusCommand command) {

        return repository.findById(command.applicationId()).map(application -> {

            application.updateStatus(command.status());
            repository.save(application);

            if ("APPROVED".equalsIgnoreCase(command.status())) {

                var user = userRepository.findById(application.getApplicantId())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // 1) Obtener rol desde la BD
                Role employeeRole = roleRepository
                        .findByName(Roles.ROLE_EMPLOYEE)
                        .orElseThrow(() -> new RuntimeException("ROLE_EMPLOYEE no existe en la BD"));

                // 2) Asignar rol EMPLOYEE al usuario
                user.addRole(employeeRole);
                userRepository.save(user);

                // 3) Actualizar municipio en el perfil
                profileService.handle(
                        new UpdateProfileMunicipalityCommand(
                                application.getApplicantId(),
                                application.getTargetMunicipality()
                        )
                );

                // 4) Publicar evento
                eventPublisher.publishEvent(
                        new EmployeeHiredEvent(this, application.getApplicantId(), application.getTargetMunicipality())
                );
            }

            return application;
        });
    }

    public void handle(FireEmployeeCommand command) {

        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 1) Obtener rol EMPLOYEE
        Role employeeRole = roleRepository
                .findByName(Roles.ROLE_EMPLOYEE)
                .orElseThrow(() -> new RuntimeException("ROLE_EMPLOYEE no existe"));

        // 2) Remover rol
        user.getRoles().remove(employeeRole);
        userRepository.save(user);

        // 3) Evento
        eventPublisher.publishEvent(new EmployeeFiredEvent(this, command.userId()));
    }
}
