package pe.com.ecocleany.ecosmart.recruitment.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository; // Importar
import pe.com.ecocleany.ecosmart.recruitment.application.internal.commandservices.RecruitmentCommandServiceImpl;
import pe.com.ecocleany.ecosmart.recruitment.application.internal.queryservices.RecruitmentQueryServiceImpl;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.commands.CreateApplicationCommand;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.commands.FireEmployeeCommand;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.commands.UpdateApplicationStatusCommand;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.queries.GetAllApplicationsQuery;
import pe.com.ecocleany.ecosmart.recruitment.interfaces.rest.resources.CreateApplicationResource;
import pe.com.ecocleany.ecosmart.recruitment.interfaces.rest.resources.JobApplicationResource;
import pe.com.ecocleany.ecosmart.recruitment.interfaces.rest.resources.UpdateStatusResource;
import pe.com.ecocleany.ecosmart.recruitment.interfaces.rest.transform.JobApplicationResourceFromEntityAssembler;
import pe.com.ecocleany.ecosmart.shared.interfaces.rest.SecurityUtils;
import pe.com.ecocleany.ecosmart.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import pe.com.ecocleany.ecosmart.recruitment.interfaces.rest.resources.EmployeeResource;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/recruitment/applications")
@Tag(name = "Recruitment", description = "Solicitudes de Empleo (DDD Full)")
public class RecruitmentController {

    private final RecruitmentCommandServiceImpl commandService;
    private final RecruitmentQueryServiceImpl queryService;
    private final SecurityUtils securityUtils;
    private final JobApplicationResourceFromEntityAssembler assembler;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public RecruitmentController(
            RecruitmentCommandServiceImpl commandService,
            RecruitmentQueryServiceImpl queryService,
            SecurityUtils securityUtils,
            JobApplicationResourceFromEntityAssembler assembler,
            UserRepository userRepository,
            ProfileRepository profileRepository
    ) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.securityUtils = securityUtils;
        this.assembler = assembler;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @PostMapping
    public ResponseEntity<Long> apply(@RequestBody CreateApplicationResource resource) {

        String username = securityUtils.getCurrentUsername();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var command = new CreateApplicationCommand(
                user.getId(), // ID REAL
                resource.targetMunicipality(),
                resource.description()
        );

        Long applicationId = commandService.handle(command);
        return new ResponseEntity<>(applicationId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResource>> getAll() {

        var query = new GetAllApplicationsQuery();
        var applications = queryService.handle(query);

        var resources = applications.stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusResource resource) {

        var command = new UpdateApplicationStatusCommand(id, resource.status());
        var updatedApplication = commandService.handle(command);

        if (updatedApplication.isPresent()) {
            return ResponseEntity.ok("Estado actualizado exitosamente.");
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "ADMIN: Despedir empleado")
    @PostMapping("/employees/{userId}/fire")
    public ResponseEntity<?> fireEmployee(@PathVariable Long userId) {
        var command = new FireEmployeeCommand(userId);
        commandService.handle(command);
        return ResponseEntity.ok("Proceso de despido iniciado.");
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResource>> getEmployees() {
        var employees = profileRepository.findAllByWorkingMunicipalityIsNotNull()
                .stream()
                .map(p -> new EmployeeResource(
                        p.getUserId(),
                        p.getFullName(),
                        p.getEmail(),
                        p.getWorkingMunicipality()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }
}