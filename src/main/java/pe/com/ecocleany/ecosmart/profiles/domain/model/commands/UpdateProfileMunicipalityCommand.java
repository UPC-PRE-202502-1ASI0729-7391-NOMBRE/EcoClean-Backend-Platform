package pe.com.ecocleany.ecosmart.profiles.domain.model.commands;

public record UpdateProfileMunicipalityCommand(
        Long userId,
        String newMunicipality
) {}
