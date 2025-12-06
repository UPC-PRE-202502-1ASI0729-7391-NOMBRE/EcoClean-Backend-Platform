package pe.com.ecocleany.ecosmart.recruitment.interfaces.rest.resources;

public record EmployeeResource(
        Long id,
        String name,
        String email,
        String municipality
) {}
