package project1.OurFit.service;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project1.OurFit.repository.JpaMemberRepository;
import project1.OurFit.repository.KakaoRepository;
import project1.OurFit.repository.MemberRepository;

@Configuration
public class SpringConfig {

    private EntityManager em;

    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository(), kakaoRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

    @Bean
    public KakaoRepository kakaoRepository() {
        return new KakaoRepository();
    }
}
