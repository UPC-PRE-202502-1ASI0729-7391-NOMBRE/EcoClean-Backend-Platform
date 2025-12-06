package pe.com.ecocleany.ecosmart.operations.domain.model.commands;

public record CreateSmartBinCommand(String name, Double latitude, Double longitude, String district) {}
