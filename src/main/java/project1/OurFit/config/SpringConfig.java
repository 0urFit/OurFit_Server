package project1.OurFit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project1.OurFit.service.KakaoService;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

    @PersistenceContext // -> 엔티티매니저 주입해줌 (생성자 주입 안 써도 됨)
    private EntityManager em;

    @Bean
    public KakaoService kakaoService() {
        return new KakaoService(new RestTemplate(), new ObjectMapper());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }
}
