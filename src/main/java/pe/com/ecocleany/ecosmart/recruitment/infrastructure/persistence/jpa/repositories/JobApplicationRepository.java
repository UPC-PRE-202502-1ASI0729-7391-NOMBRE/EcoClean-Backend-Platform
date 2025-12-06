package pe.com.ecocleany.ecosmart.recruitment.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.aggregates.JobApplication;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
}