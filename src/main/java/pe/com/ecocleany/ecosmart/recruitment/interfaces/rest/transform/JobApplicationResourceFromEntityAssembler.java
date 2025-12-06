package pe.com.ecocleany.ecosmart.recruitment.interfaces.rest.transform;

import org.springframework.stereotype.Component;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.aggregates.JobApplication;
import pe.com.ecocleany.ecosmart.recruitment.interfaces.rest.resources.JobApplicationResource;

@Component
public class JobApplicationResourceFromEntityAssembler {

    private final UserRepository userRepository;

    public JobApplicationResourceFromEntityAssembler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public JobApplicationResource toResource(JobApplication entity) {

        var user = userRepository.findById(entity.getApplicantId())
                .orElse(null);

        String name = (user != null) ? user.getUsername() : "Desconocido";
        String email = (user != null) ? user.getEmail() : "No disponible";

        return new JobApplicationResource(
                entity.getId(),
                entity.getApplicantId(),
                name,
                email,
                entity.getTargetMunicipality(),
                entity.getStatus(),
                entity.getDescription()
        );
    }
}
