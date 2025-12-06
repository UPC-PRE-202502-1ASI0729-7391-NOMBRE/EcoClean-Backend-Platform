package pe.com.ecocleany.ecosmart.operations.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.ecocleany.ecosmart.operations.application.internal.commandservices.SmartBinCommandServiceImpl;
import pe.com.ecocleany.ecosmart.operations.application.internal.queryservices.SmartBinQueryServiceImpl;
import pe.com.ecocleany.ecosmart.operations.domain.model.commands.CreateReportCommand;
import pe.com.ecocleany.ecosmart.operations.domain.model.commands.CreateSmartBinCommand;
import pe.com.ecocleany.ecosmart.operations.domain.model.commands.DispatchTruckCommand;
import pe.com.ecocleany.ecosmart.operations.domain.model.queries.GetAllReportsQuery;
import pe.com.ecocleany.ecosmart.operations.domain.model.queries.GetAllSmartBinsQuery;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources.CreateReportResource;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources.CreateSmartBinResource;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources.SmartBinResource;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.transform.SmartBinResourceFromEntityAssembler;
import pe.com.ecocleany.ecosmart.shared.interfaces.rest.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/operations/smartbins")
@Tag(name = "Operations", description = "Gestión de Tachos y Logística (DDD Full)")
public class OperationsController {

    private final SmartBinCommandServiceImpl commandService;
    private final SmartBinQueryServiceImpl queryService;
    private final SecurityUtils securityUtils;

    public OperationsController(SmartBinCommandServiceImpl commandService, SmartBinQueryServiceImpl queryService, SecurityUtils securityUtils) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.securityUtils = securityUtils;
    }

    @PostMapping
    public ResponseEntity<Long> createBin(@RequestBody CreateSmartBinResource resource) {
        var command = new CreateSmartBinCommand(
                resource.name(),
                resource.latitude(),
                resource.longitude(),
                resource.district()  // ⬅️ ahora sí
        );

        var id = commandService.handle(command);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    @Operation(summary = "Usuario: Reportar un tacho lleno/dañado")
    @PostMapping("/reports")
    public ResponseEntity<?> createReport(@RequestBody CreateReportResource resource) {

        Long reporterId = 1L;

        var command = new CreateReportCommand(
                resource.message(),
                resource.photoUrl(),
                resource.smartBinId(),
                reporterId,
                resource.district()
        );

        var id = commandService.handle(command);
        return ResponseEntity.ok("Reporte creado con ID: " + id);
    }


    @Operation(summary = "Empleado: Enviar alerta a camiones")
    @PostMapping("/dispatch-truck")
    public ResponseEntity<?> dispatchTruck(@RequestParam String district) {
        var command = new DispatchTruckCommand(district);
        String result = commandService.handle(command);
        return ResponseEntity.ok(result);
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
    public ResponseEntity<?> getAllReports() {
        var query = new GetAllReportsQuery();
        return ResponseEntity.ok(queryService.handle(query));
    }
}