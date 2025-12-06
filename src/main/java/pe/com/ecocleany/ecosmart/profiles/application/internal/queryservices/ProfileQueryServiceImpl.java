package pe.com.ecocleany.ecosmart.profiles.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.profiles.domain.model.aggregates.Profile;
import pe.com.ecocleany.ecosmart.profiles.domain.model.queries.GetProfileByUserIdQuery;
import pe.com.ecocleany.ecosmart.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileQueryServiceImpl {
    private final ProfileRepository profileRepository;

    public ProfileQueryServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Optional<Profile> handle(GetProfileByUserIdQuery query) {
        return profileRepository.findByUserId(query.userId());
    }
}