package pe.com.ecocleany.ecosmart.social.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.ecocleany.ecosmart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
public class Post extends AuditableAbstractAggregateRoot<Post> {

    private String content;
    private String imageUrl;
    private String district;
    private Integer likesCount;
    private boolean isOfficialAnnouncement;
    private Long authorId;

    public Post(String content, String imageUrl, String district, Long authorId, boolean isOfficialAnnouncement) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.district = district;
        this.authorId = authorId;
        this.isOfficialAnnouncement = isOfficialAnnouncement;
        this.likesCount = 0;
    }

    public void incrementLikes() {
        if (this.likesCount == null) this.likesCount = 0;
        this.likesCount++;
    }
}