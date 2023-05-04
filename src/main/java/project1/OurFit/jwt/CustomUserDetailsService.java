package project1.OurFit.jwt;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import project1.OurFit.entity.Member;
import project1.OurFit.repository.MemberRepository;
import project1.constant.exception.BaseException;
import project1.constant.response.JsonResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CustomUserDetailsService implements AuthenticationProvider {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(final String email) {
//        return memberRepository.findByEmail(email)
//                .map(member -> createUser(email, member))
//                .orElseThrow(() -> new BaseException(JsonResponseStatus.UNAUTHORIZED));
//    }

//    private org.springframework.security.core.userdetails.User createUser(String username, Member member) {
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//
//        return new User(member.getEmail(), member.getPassword(), grantedAuthorities);
//    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        Member member = memberRepository.findByEmail(authentication.getName())
//                .orElseThrow(() -> new BaseException(JsonResponseStatus.UNAUTHORIZED));
//
//        if (!passwordEncoder.matches(authentication.getCredentials().toString(), member.getPassword()))
//            throw new BaseException(JsonResponseStatus.UNAUTHORIZED);

        return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials().toString(), null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
