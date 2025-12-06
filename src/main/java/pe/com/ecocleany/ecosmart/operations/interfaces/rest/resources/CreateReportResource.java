package pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources;

public record CreateReportResource(
        String message,
        String photoUrl,
        Long smartBinId,
        String district
) {}
