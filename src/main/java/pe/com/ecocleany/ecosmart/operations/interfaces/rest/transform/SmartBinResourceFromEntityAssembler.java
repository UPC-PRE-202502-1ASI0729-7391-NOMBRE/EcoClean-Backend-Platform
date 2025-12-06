package pe.com.ecocleany.ecosmart.operations.interfaces.rest.transform;

import pe.com.ecocleany.ecosmart.operations.domain.model.aggregates.SmartBin;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.resources.SmartBinResource;

public class SmartBinResourceFromEntityAssembler {
    public static SmartBinResource toResource(SmartBin entity) {
        return new SmartBinResource(
                entity.getId(),
                entity.getName(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getFillLevel(),
                entity.getStatus()
        );
    }
}