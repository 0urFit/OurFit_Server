package project1.OurFit.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        return http.requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .build();
    }
}
