package pe.com.ecocleany.ecosmart.profiles.application.internal.commandservices;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.profiles.domain.model.aggregates.Profile;
import pe.com.ecocleany.ecosmart.profiles.domain.model.commands.CreateProfileCommand;
import pe.com.ecocleany.ecosmart.profiles.domain.model.commands.UpdateProfileCommand;
import pe.com.ecocleany.ecosmart.profiles.infrastructure.persistence.jpa.repositories.ProfileRepository;

import java.util.Optional;

@Service
public class ProfileCommandServiceImpl {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileCommandServiceImpl(ProfileRepository profileRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Long handle(CreateProfileCommand command) {
        var profile = new Profile(
                command.firstName(), command.lastName(), command.email(),
                command.district(), command.photoUrl(), command.userId()
        );
        profileRepository.save(profile);
        return profile.getId();
    }

    public Optional<Profile> handle(UpdateProfileCommand command) {
        var profileOpt = profileRepository.findByUserId(command.userId());
        if (profileOpt.isEmpty()) return Optional.empty();

        var profile = profileOpt.get();
        var user = userRepository.findById(command.userId()).orElseThrow();

        if (command.firstName() != null) profile.updateFirstName(command.firstName());
        if (command.lastName() != null) profile.updateLastName(command.lastName());
        if (command.district() != null) profile.updateAddress(command.district());
        if (command.photoUrl() != null) profile.updatePhoto(command.photoUrl());
        if (command.email() != null) profile.updateEmail(command.email());

        profileRepository.save(profile);

        if (command.email() != null) user.setEmail(command.email());
        if (command.firstName() != null) user.setFirstName(command.firstName());
        if (command.lastName() != null) user.setLastName(command.lastName());

        // Actualizar Contraseña si se envía
        if (command.password() != null && !command.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(command.password()));
        }

        userRepository.save(user);

        return Optional.of(profile);
    }

    public Optional<Profile> getProfileByUserId(Long userId) {
        return profileRepository.findByUserId(userId);
    }
}