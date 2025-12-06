package pe.com.ecocleany.ecosmart.social.interfaces.rest.transform;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pe.com.ecocleany.ecosmart.social.domain.model.aggregates.Post;
import pe.com.ecocleany.ecosmart.social.interfaces.acl.IamContextFacade;
import pe.com.ecocleany.ecosmart.social.interfaces.rest.resources.PostResource;

@Component
public class PostResourceFromEntityAssembler {

    private final IamContextFacade iamContextFacade;

    public PostResourceFromEntityAssembler(
            @Qualifier("SocialExternalIamService") IamContextFacade iamContextFacade) {
        this.iamContextFacade = iamContextFacade;
    }

    public PostResource toResource(Post entity) {

        String authorDisplay;

        if (entity.isOfficialAnnouncement()) {
            authorDisplay = "MUNICIPALIDAD DE " + entity.getDistrict().toUpperCase();
        } else {
            authorDisplay = iamContextFacade.getUsernameById(entity.getAuthorId());
        }

        return new PostResource(
                entity.getId(),
                entity.getContent(),
                entity.getImageUrl(),
                entity.getDistrict(),
                entity.getLikesCount(),
                entity.isOfficialAnnouncement(),
                authorDisplay,
                entity.getAuthorId(),
                entity.getCreatedAt()
        );
    }
}