package pe.com.ecocleany.ecosmart.recruitment.domain.model.commands;

public record UpdateApplicationStatusCommand(Long applicationId, String status) {}