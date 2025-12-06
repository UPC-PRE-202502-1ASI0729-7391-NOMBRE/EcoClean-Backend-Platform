package pe.com.ecocleany.ecosmart.recruitment.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.aggregates.JobApplication;
import pe.com.ecocleany.ecosmart.recruitment.domain.model.queries.GetAllApplicationsQuery;
import pe.com.ecocleany.ecosmart.recruitment.infrastructure.persistence.jpa.repositories.JobApplicationRepository;

import java.util.List;

@Service
public class RecruitmentQueryServiceImpl {
    private final JobApplicationRepository repository;

    public RecruitmentQueryServiceImpl(JobApplicationRepository repository) {
        this.repository = repository;
    }

    public List<JobApplication> handle(GetAllApplicationsQuery query) {
        return repository.findAll();
    }
}