package project1.OurFit.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;

import java.io.IOException;
import java.util.Objects;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider tokenProvider;
    public JwtFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (!StringUtils.hasText(jwt)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        if (tokenProvider.validateToken(jwt)) {
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        if (!httpServletResponse.isCommitted()) {
            ResponseEntity<String> responseEntity = createErrorResponse();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setStatus(responseEntity.getStatusCode().value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.getWriter().write(Objects.requireNonNull(responseEntity.getBody()));
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    private ResponseEntity<String> createErrorResponse() throws JsonProcessingException {
        JsonResponse<JsonResponseStatus> jsonResponse = new JsonResponse<>(JsonResponseStatus.ACCESS_TOKEN_EXPIRED);
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonBody = objectMapper.writeValueAsString(jsonResponse);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonBody);
    }
}
