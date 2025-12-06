package pe.com.ecocleany.ecosmart.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.ecocleany.ecosmart.iam.domain.services.UserCommandService;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.resources.AuthenticatedUserResource;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.resources.SignInResource;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.resources.SignUpResource;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;
import pe.com.ecocleany.ecosmart.profiles.application.internal.commandservices.ProfileCommandServiceImpl;
import pe.com.ecocleany.ecosmart.profiles.domain.model.commands.CreateProfileCommand;
import pe.com.ecocleany.ecosmart.shared.interfaces.rest.resources.MessageResource;

@RestController
@RequestMapping("/api/v1/authentication")
@Tag(name = "Authentication", description = "Login y Registro Seguro")
public class AuthenticationController {

    private final UserCommandService userCommandService;
    private final ProfileCommandServiceImpl profileCommandService;

    public AuthenticationController(UserCommandService userCommandService,
                                    ProfileCommandServiceImpl profileCommandService) {
        this.userCommandService = userCommandService;
        this.profileCommandService = profileCommandService;
    }

    @Operation(summary = "Registrar nuevo usuario")
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpResource resource) {
        var command = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResource("El usuario o email ya existe."));
        }

        var createdUser = user.get();

        var profileCommand = new CreateProfileCommand(
                resource.firstName(),
                resource.lastName(),
                resource.email(),
                resource.district(),
                null,
                createdUser.getId()
        );

        profileCommandService.handle(profileCommand);

        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResource("Usuario creado exitosamente."));
    }

    @Operation(summary = "Iniciar sesión (Obtener Token)")
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignInResource resource) {

        var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(command);

        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResource("Credenciales inválidas."));

        var userEntity = result.get().getLeft();
        var token = result.get().getRight();

        var profileOpt = profileCommandService.getProfileByUserId(userEntity.getId());

        String municipality = profileOpt
                .map(p -> p.getDistrict())
                .orElse(null);

        var authenticatedUserResource =
                AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(
                        userEntity,
                        token,
                        municipality
                );

        return ResponseEntity.ok(authenticatedUserResource);
    }
}