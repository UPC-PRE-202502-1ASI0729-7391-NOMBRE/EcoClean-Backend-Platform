package pe.com.ecocleany.ecosmart.social.application.internal.queryservices;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.social.domain.model.aggregates.Post;
import pe.com.ecocleany.ecosmart.social.domain.model.queries.GetAllPostsQuery;
import pe.com.ecocleany.ecosmart.social.infrastructure.persistence.jpa.repositories.PostRepository;

import java.util.List;

@Service
public class PostQueryServiceImpl {

    private final PostRepository postRepository;

    public PostQueryServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> handle(GetAllPostsQuery query) {
        if (query.district() != null && !query.district().isEmpty()) {
            return postRepository.findAllByDistrictOrderByCreatedAtDesc(query.district());
        }
        if (query.isPopular()) {
            return postRepository.findAll(Sort.by(Sort.Direction.DESC, "likesCount"));
        }
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}