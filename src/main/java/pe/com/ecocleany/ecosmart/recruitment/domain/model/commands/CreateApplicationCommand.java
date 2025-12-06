package pe.com.ecocleany.ecosmart.recruitment.domain.model.commands;

public record CreateApplicationCommand(Long userId, String targetMunicipality, String description) {}
