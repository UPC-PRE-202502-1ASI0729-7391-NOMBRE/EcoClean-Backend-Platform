package pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources;

public record CreateSmartBinResource(
        String name,
        Double latitude,
        Double longitude,
        String district
) {}
