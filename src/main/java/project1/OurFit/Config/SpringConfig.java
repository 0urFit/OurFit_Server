package project1.OurFit.Config;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project1.OurFit.repository.JpaMemberRepository;
import project1.OurFit.repository.MemberRepository;
import project1.OurFit.service.KakaoService;
import project1.OurFit.service.MemberService;

@Configuration
public class SpringConfig implements WebMvcConfigurer {

    private EntityManager em;


    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

    @Bean
    public KakaoService kakaoService() {
        return new KakaoService();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("https://localhost:3000");
    }
}
