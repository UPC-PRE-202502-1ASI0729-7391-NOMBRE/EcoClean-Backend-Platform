package pe.com.ecocleany.ecosmart.profiles.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.ecocleany.ecosmart.profiles.application.internal.commandservices.ProfileCommandServiceImpl;
import pe.com.ecocleany.ecosmart.profiles.application.internal.queryservices.ProfileQueryServiceImpl;
import pe.com.ecocleany.ecosmart.profiles.domain.model.commands.CreateProfileCommand;
import pe.com.ecocleany.ecosmart.profiles.domain.model.commands.UpdateProfileCommand;
import pe.com.ecocleany.ecosmart.profiles.domain.model.queries.GetProfileByUserIdQuery;
import pe.com.ecocleany.ecosmart.profiles.interfaces.rest.resources.CreateProfileResource;
import pe.com.ecocleany.ecosmart.profiles.interfaces.rest.resources.ProfileResource;
import pe.com.ecocleany.ecosmart.profiles.interfaces.rest.resources.UpdateProfileResource;
import pe.com.ecocleany.ecosmart.profiles.interfaces.rest.transform.ProfileResourceFromEntityAssembler;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "Profiles", description = "Gestión de Perfiles")
public class ProfileController {

    private final ProfileCommandServiceImpl commandService;
    private final ProfileQueryServiceImpl queryService;

    public ProfileController(ProfileCommandServiceImpl commandService, ProfileQueryServiceImpl queryService) {
        this.commandService = commandService;
        this.queryService = queryService;
    }

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CreateProfileResource resource) {

        var command = new CreateProfileCommand(
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.district(),
                resource.photoUrl(),
                resource.userId()
        );

        Long profileId = commandService.handle(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(profileId);
    }

    @Operation(summary = "Obtener perfil por userId")
    @GetMapping("/{userId}")
    public ResponseEntity<ProfileResource> getByUserId(@PathVariable Long userId) {
        var query = new GetProfileByUserIdQuery(userId);
        return queryService.handle(query)
                .map(profile -> ResponseEntity.ok(ProfileResourceFromEntityAssembler.toResource(profile)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Actualizar perfil")
    @PutMapping("/{userId}")
    public ResponseEntity<ProfileResource> update(
            @PathVariable Long userId,
            @RequestBody UpdateProfileResource resource) {

        var command = new UpdateProfileCommand(
                userId,
                resource.firstName(),
                resource.lastName(),
                resource.district(),
                resource.photoUrl()
        );

        return commandService.handle(command)
                .map(updated -> ResponseEntity.ok(ProfileResourceFromEntityAssembler.toResource(updated)))
                .orElse(ResponseEntity.notFound().build());
    }

}
