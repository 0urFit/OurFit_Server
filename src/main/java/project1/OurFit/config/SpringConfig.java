package project1.OurFit.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext // -> 엔티티매니저 주입해줌 (생성자 주입 안 써도 됨)
    private EntityManager em;

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
        registry.addMapping("/**").allowedOrigins("http://localhost:3000");
    }
}
