package pe.com.ecocleany.ecosmart.communication.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.ecocleany.ecosmart.communication.application.internal.commandservices.MessageCommandServiceImpl;
import pe.com.ecocleany.ecosmart.communication.application.internal.queryservices.MessageQueryServiceImpl;
import pe.com.ecocleany.ecosmart.communication.domain.model.commands.CreateMessageCommand;
import pe.com.ecocleany.ecosmart.communication.domain.model.commands.ReplyMessageCommand;
import pe.com.ecocleany.ecosmart.communication.domain.model.queries.GetMessagesByMunicipalityQuery;
import pe.com.ecocleany.ecosmart.communication.interfaces.rest.resources.MessageResource;
import pe.com.ecocleany.ecosmart.communication.interfaces.rest.resources.ReplyMessageResource;
import pe.com.ecocleany.ecosmart.communication.interfaces.rest.resources.SendMessageResource;
import pe.com.ecocleany.ecosmart.communication.interfaces.rest.transform.MessageResourceFromEntityAssembler;
import pe.com.ecocleany.ecosmart.shared.interfaces.rest.SecurityUtils;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/communication/messages")
@Tag(name = "Communication", description = "Chat Vecino-Municipalidad (DDD Full)")
public class CommunicationController {

    private final MessageCommandServiceImpl commandService;
    private final MessageQueryServiceImpl queryService;
    private final SecurityUtils securityUtils;

    public CommunicationController(MessageCommandServiceImpl commandService, MessageQueryServiceImpl queryService, SecurityUtils securityUtils) {
        this.commandService = commandService;
        this.queryService = queryService;
        this.securityUtils = securityUtils;
    }

    @Operation(summary = "Usuario: Enviar mensaje")
    @PostMapping("/send")
    public ResponseEntity<Long> sendMessage(@RequestBody SendMessageResource resource) {
        Long userId = 1L;
        var command = new CreateMessageCommand(resource.content(), resource.targetMunicipality(), userId);
        Long messageId = commandService.handle(command);
        return ResponseEntity.ok(messageId);
    }

    @Operation(summary = "Empleado: Responder")
    @PostMapping("/reply")
    public ResponseEntity<Long> replyMessage(@RequestBody ReplyMessageResource resource) {
        Long employeeId = 2L;
        var command = new ReplyMessageCommand(resource.content(), employeeId);
        Long messageId = commandService.handle(command);
        return ResponseEntity.ok(messageId);
    }

    @GetMapping("/{municipality}")
    public ResponseEntity<List<MessageResource>> getMessages(@PathVariable String municipality) {
        var query = new GetMessagesByMunicipalityQuery(municipality);
        var messages = queryService.handle(query);

        var resources = messages.stream()
                .map(MessageResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());

        return ResponseEntity.ok(resources);
    }
}