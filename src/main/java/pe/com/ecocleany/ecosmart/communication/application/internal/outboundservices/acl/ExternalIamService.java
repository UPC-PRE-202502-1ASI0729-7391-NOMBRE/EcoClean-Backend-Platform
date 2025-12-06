package pe.com.ecocleany.ecosmart.communication.application.internal.outboundservices.acl;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.communication.interfaces.acl.IamContextFacade;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;

@Service
public class ExternalIamService implements IamContextFacade {

    private final UserRepository userRepository;

    public ExternalIamService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean existsUser(Long userId) {
        return userRepository.existsById(userId);
    }
}