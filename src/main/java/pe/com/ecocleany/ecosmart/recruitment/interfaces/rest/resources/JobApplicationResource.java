package pe.com.ecocleany.ecosmart.recruitment.interfaces.rest.resources;

public record JobApplicationResource(
        Long id,
        Long applicantId,
        String applicantName,
        String applicantEmail,
        String municipality,
        String status,
        String description
) {}
