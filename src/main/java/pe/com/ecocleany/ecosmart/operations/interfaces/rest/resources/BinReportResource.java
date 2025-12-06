package pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources;

import java.util.Date;

public record BinReportResource(
        Long id,
        String message,
        String photoUrl,
        String smartBinName,
        String reporterName,
        String district,
        String status,
        Date createdAt
) {}