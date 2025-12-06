package pe.com.ecocleany.ecosmart.iam.domain.model.aggregates;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.ecocleany.ecosmart.iam.domain.model.entities.Role;
import pe.com.ecocleany.ecosmart.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
public class User extends AuditableAbstractAggregateRoot<User> {

    @NotBlank
    @Column(length = 50)
    private String username;

    @NotBlank
    @Email
    @Column(length = 50)
    private String email;

    @NotBlank
    @Column(length = 120)
    private String password;

    @NotBlank
    @Column(length = 50)
    private String firstName;

    @NotBlank
    @Column(length = 50)
    private String lastName;

    @NotBlank
    @Column(length = 50)
    private String district;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password,
                String firstName, String lastName, String district,
                Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.district = district;
        this.roles = roles;
    }


    public void addRole(Role role) {
        if (this.roles == null) this.roles = new HashSet<>();
        this.roles.add(role);
    }

    public void removeRole(Role role) {
        if (roles != null) roles.remove(role);
    }
}
