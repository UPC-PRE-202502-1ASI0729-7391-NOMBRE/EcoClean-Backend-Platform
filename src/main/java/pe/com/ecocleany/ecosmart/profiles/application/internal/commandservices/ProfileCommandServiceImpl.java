package pe.com.ecocleany.ecosmart.profiles.application.internal.commandservices;

import pe.com.ecocleany.ecosmart.profiles.domain.model.commands.UpdateProfileMunicipalityCommand;
import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.profiles.domain.model.aggregates.Profile;
import pe.com.ecocleany.ecosmart.profiles.domain.model.commands.CreateProfileCommand;
import pe.com.ecocleany.ecosmart.profiles.domain.model.commands.UpdateProfileCommand;
import pe.com.ecocleany.ecosmart.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl {

    private final ProfileRepository profileRepository;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public Long handle(CreateProfileCommand command) {
        var profile = new Profile(
                command.firstName(),
                command.lastName(),
                command.email(),
                command.district(),
                command.photoUrl(),
                command.userId()
        );
        profileRepository.save(profile);
        return profile.getId();
    }

    public Optional<Profile> handle(UpdateProfileCommand command) {
        var profileOpt = profileRepository.findByUserId(command.userId());

        if (profileOpt.isEmpty()) return Optional.empty();

        var profile = profileOpt.get();

        if (command.firstName() != null) profile.updateFirstName(command.firstName());
        if (command.lastName() != null) profile.updateLastName(command.lastName());
        if (command.district() != null) profile.updateAddress(command.district());
        if (command.photoUrl() != null) profile.updatePhoto(command.photoUrl());

        profileRepository.save(profile);
        return Optional.of(profile);
    }

    public Optional<Profile> getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }

    public void handle(UpdateProfileMunicipalityCommand command) {
        var profileOpt = profileRepository.findByUserId(command.userId());

        if (profileOpt.isEmpty()) return;

        var profile = profileOpt.get();
        profile.setDistrict(command.newMunicipality());

        profileRepository.save(profile);
    }

}
