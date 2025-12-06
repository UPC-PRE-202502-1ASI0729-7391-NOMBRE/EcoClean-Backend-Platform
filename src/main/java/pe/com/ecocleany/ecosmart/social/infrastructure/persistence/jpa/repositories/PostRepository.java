package pe.com.ecocleany.ecosmart.social.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.social.domain.model.aggregates.Post;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByDistrictOrderByCreatedAtDesc(String district);
}