package pe.com.ecocleany.ecosmart.social.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.iam.domain.model.valueobjects.Roles;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.profiles.domain.model.aggregates.Profile;
import pe.com.ecocleany.ecosmart.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import pe.com.ecocleany.ecosmart.social.interfaces.acl.IamContextFacade;

@Service("SocialExternalIamService")
public class ExternalIamService implements IamContextFacade {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public ExternalIamService(UserRepository userRepository,
                              ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    public boolean isUserEmployee(Long userId) {
        return userRepository.findById(userId)
                .map(user -> user.getRoles().stream()
                        .anyMatch(role -> role.getName() == Roles.ROLE_EMPLOYEE
                                || role.getName() == Roles.ROLE_ADMIN))
                .orElse(false);
    }
    @Override
    public String getUsernameById(Long userId) {
        return profileRepository.findByUserId(userId)
                .map(Profile::getFullName)
                .orElseGet(() -> userRepository.findById(userId)
                        .map(user -> user.getUsername())
                        .orElse("Usuario desconocido"));
    }

}
