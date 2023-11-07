package project1.OurFit.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SpringConfig {

    @PersistenceContext // -> 엔티티매니저 주입해줌 (생성자 주입 안 써도 됨)
    private EntityManager em;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
