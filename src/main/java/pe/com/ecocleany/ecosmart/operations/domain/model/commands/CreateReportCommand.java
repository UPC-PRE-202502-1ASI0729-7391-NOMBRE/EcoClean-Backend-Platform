package pe.com.ecocleany.ecosmart.operations.domain.model.commands;

public record CreateReportCommand(
        String message,
        String photoUrl,
        Long smartBinId,
        Long reporterId,
        String district
) {}
