package pe.com.ecocleany.ecosmart.recruitment.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    public RecruitmentController(
            RecruitmentCommandServiceImpl commandService,
            RecruitmentQueryServiceImpl queryService,
            SecurityUtils securityUtils,
            JobApplicationResourceFromEntityAssembler assembler
    ) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.securityUtils = securityUtils;
        this.assembler = assembler;
    }

    @PostMapping
    public ResponseEntity<Long> apply(@RequestBody CreateApplicationResource resource) {

        Long userId = 1L; // luego reemplazar por securityUtils.getUserId()

        var command = new CreateApplicationCommand(
                userId,
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
}
