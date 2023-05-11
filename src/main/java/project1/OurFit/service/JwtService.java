package project1.OurFit.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project1.OurFit.jwt.JwtTokenProvider;
import project1.OurFit.response.PostLoginDto;

@Service
public class JwtService {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthenticationManager authenticationManager;

    public JwtService(JwtTokenProvider jwtTokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.authenticationManager = authenticationManager;
    }

    public PostLoginDto authorize(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.createToken(authentication);
        return new PostLoginDto(token);
    }

    public PostLoginDto authorize(String email) {
        return authorize(email, "");
    }
}
