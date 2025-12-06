package pe.com.ecocleany.ecosmart.social.application.internal.commandservices;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.social.domain.model.aggregates.Post;
import pe.com.ecocleany.ecosmart.social.domain.model.commands.CreatePostCommand;
import pe.com.ecocleany.ecosmart.social.domain.model.commands.DeletePostCommand;
import pe.com.ecocleany.ecosmart.social.domain.model.commands.LikePostCommand;
import pe.com.ecocleany.ecosmart.social.infrastructure.persistence.jpa.repositories.PostRepository;
import pe.com.ecocleany.ecosmart.social.interfaces.acl.IamContextFacade;

import java.util.Optional;

@Service
public class PostCommandServiceImpl {

    private final PostRepository postRepository;
    private final IamContextFacade iamContextFacade;

    public PostCommandServiceImpl(PostRepository postRepository, @Qualifier("SocialExternalIamService") IamContextFacade iamContextFacade) {
        this.postRepository = postRepository;
        this.iamContextFacade = iamContextFacade;
    }

    public Long handle(CreatePostCommand command) {
        boolean isOfficial = iamContextFacade.isUserEmployee(command.authorId());

        var post = new Post(
                command.content(),
                command.imageUrl(),
                command.district(),
                command.authorId(),
                isOfficial
        );
        postRepository.save(post);
        return post.getId();
    }

    public void handle(DeletePostCommand command) {
        if (postRepository.existsById(command.postId())) {
            postRepository.deleteById(command.postId());
        }
    }

    public Optional<Long> handle(LikePostCommand command) {
        return postRepository.findById(command.postId()).map(post -> {
            post.incrementLikes();
            postRepository.save(post);
            return post.getId();
        });
    }
}