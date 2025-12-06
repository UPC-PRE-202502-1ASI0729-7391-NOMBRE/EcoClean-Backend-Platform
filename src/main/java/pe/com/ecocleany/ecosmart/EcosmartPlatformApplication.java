package pe.com.ecocleany.ecosmart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EcosmartPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcosmartPlatformApplication.class, args);
    }

}
