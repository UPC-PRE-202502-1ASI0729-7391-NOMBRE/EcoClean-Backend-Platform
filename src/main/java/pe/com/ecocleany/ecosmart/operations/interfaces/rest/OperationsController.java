package pe.com.ecocleany.ecosmart.operations.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.operations.application.internal.commandservices.SmartBinCommandServiceImpl;
import pe.com.ecocleany.ecosmart.operations.application.internal.queryservices.SmartBinQueryServiceImpl;
import pe.com.ecocleany.ecosmart.operations.domain.model.aggregates.SmartBin;
import pe.com.ecocleany.ecosmart.operations.domain.model.commands.CreateReportCommand;
import pe.com.ecocleany.ecosmart.operations.domain.model.commands.CreateSmartBinCommand;
import pe.com.ecocleany.ecosmart.operations.domain.model.commands.DispatchTruckCommand;
import pe.com.ecocleany.ecosmart.operations.domain.model.queries.GetAllReportsQuery;
import pe.com.ecocleany.ecosmart.operations.domain.model.queries.GetAllSmartBinsQuery;
import pe.com.ecocleany.ecosmart.operations.infrastructure.persistence.jpa.repositories.SmartBinRepository;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources.BinReportResource;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources.CreateReportResource;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources.CreateSmartBinResource;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources.SmartBinResource;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.transform.SmartBinResourceFromEntityAssembler;
import pe.com.ecocleany.ecosmart.shared.interfaces.rest.SecurityUtils;
import pe.com.ecocleany.ecosmart.shared.interfaces.rest.resources.MessageResource;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/operations/smartbins")
@Tag(name = "Operations", description = "Gestión de Tachos y Logística")
public class OperationsController {

    private final SmartBinCommandServiceImpl commandService;
    private final SmartBinQueryServiceImpl queryService;
    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final SmartBinRepository smartBinRepository;

    public OperationsController(SmartBinCommandServiceImpl commandService,
                                SmartBinQueryServiceImpl queryService,
                                SecurityUtils securityUtils,
                                UserRepository userRepository,
                                SmartBinRepository smartBinRepository) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.securityUtils = securityUtils;
        this.userRepository = userRepository;
        this.smartBinRepository = smartBinRepository;
    }

    @PostMapping
    public ResponseEntity<Long> createBin(@RequestBody CreateSmartBinResource resource) {
        var command = new CreateSmartBinCommand(
                resource.name(),
                resource.latitude(),
                resource.longitude(),
                resource.district()
        );
        var id = commandService.handle(command);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Operation(summary = "Usuario: Reportar un tacho lleno/dañado")
    @PostMapping("/reports")
    public ResponseEntity<?> createReport(@RequestBody CreateReportResource resource) {
        String username = securityUtils.getCurrentUsername();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        var command = new CreateReportCommand(
                resource.message(),
                resource.photoUrl(),
                resource.smartBinId(),
                user.getId(),
                resource.district()
        );
        var id = commandService.handle(command);
        return ResponseEntity.ok(new MessageResource("Reporte creado con ID: " + id));
    }

    @Operation(summary = "Empleado: Enviar alerta a camiones")
    @PostMapping("/dispatch-truck")
    public ResponseEntity<?> dispatchTruck(@RequestParam String district) {
        var command = new DispatchTruckCommand(district);
        commandService.handle(command);
        return ResponseEntity.ok(new MessageResource("Mandando reporte al camion recolector de basura más cercano"));
    }

    @GetMapping
    public ResponseEntity<List<SmartBinResource>> getAllBins() {
        var query = new GetAllSmartBinsQuery();
        var bins = queryService.handle(query);
        var resources = bins.stream()
                .map(SmartBinResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/reports")
    public ResponseEntity<List<BinReportResource>> getAllReports() {
        var query = new GetAllReportsQuery();
        var reports = queryService.handle(query);

        var resources = reports.stream().map(report -> {
            String reporterName = userRepository.findById(report.getReporterId())
                    .map(u -> u.getUsername())
                    .orElse("Usuario Desconocido");

            String binName = smartBinRepository.findById(report.getSmartBinId())
                    .map(SmartBin::getName)
                    .orElse("Tacho Desconocido");

            return new BinReportResource(
                    report.getId(),
                    report.getMessage(),
                    report.getPhotoUrl(),
                    binName,
                    reporterName,
                    report.getDistrict(),
                    report.getStatus(),
                    report.getCreatedAt()
            );
        }).collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }
}