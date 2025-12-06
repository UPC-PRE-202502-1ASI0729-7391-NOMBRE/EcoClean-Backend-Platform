package pe.com.ecocleany.ecosmart.profiles.domain.model.aggregates;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.ecocleany.ecosmart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

@Entity
@Getter
@NoArgsConstructor
public class Profile extends AuditableAbstractAggregateRoot<Profile> {

    private String firstName;
    private String lastName;
    private String email;
    private String district;
    private String photoUrl;
    private Long userId;

    public Profile(String firstName, String lastName, String email, String district, String photoUrl, Long userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.district = district;
        this.photoUrl = photoUrl;
        this.userId = userId;
    }

    public void updateFirstName(String firstName) { this.firstName = firstName; }

    public void updateLastName(String lastName) { this.lastName = lastName; }

    public void updateAddress(String district) { this.district = district; }

    public void updatePhoto(String photoUrl) { this.photoUrl = photoUrl; }

    public String getFullName() {
        return (firstName != null ? firstName : "") +
                (lastName != null ? " " + lastName : "");
    }

    public void setDistrict(String district) {
        this.district = district;
    }

}

