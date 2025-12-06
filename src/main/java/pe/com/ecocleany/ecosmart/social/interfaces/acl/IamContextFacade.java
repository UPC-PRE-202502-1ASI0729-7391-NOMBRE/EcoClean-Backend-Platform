package pe.com.ecocleany.ecosmart.social.interfaces.acl;

public interface IamContextFacade {
    boolean isUserEmployee(Long userId);
    String getUsernameById(Long userId);
}
