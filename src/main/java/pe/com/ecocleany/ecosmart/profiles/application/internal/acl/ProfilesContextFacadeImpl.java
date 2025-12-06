package pe.com.ecocleany.ecosmart.profiles.application.internal.acl;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;
import pe.com.ecocleany.ecosmart.profiles.interfaces.acl.ProfilesContextFacade;

@Service
public class ProfilesContextFacadeImpl implements ProfilesContextFacade {
    private final ProfileRepository profileRepository;

    public ProfilesContextFacadeImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public String getFullNameByUserId(Long userId) {
        return profileRepository.findByUserId(userId)
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .orElse("Usuario Desconocido");
    }

}