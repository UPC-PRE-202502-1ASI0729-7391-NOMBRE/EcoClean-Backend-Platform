package pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources;

public record SmartBinResource(Long id, String name, Double latitude, Double longitude, Double fillLevel, String status) {}