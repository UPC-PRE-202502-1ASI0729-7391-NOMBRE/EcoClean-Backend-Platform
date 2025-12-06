package pe.com.ecocleany.ecosmart.profiles.interfaces.rest.transform;

import pe.com.ecocleany.ecosmart.profiles.domain.model.aggregates.Profile;
import pe.com.ecocleany.ecosmart.profiles.interfaces.rest.resources.ProfileResource;

public class ProfileResourceFromEntityAssembler {
    public static ProfileResource toResource(Profile entity) {
        return new ProfileResource(
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getDistrict(),
                entity.getPhotoUrl()
        );
    }
}