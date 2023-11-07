package project1.OurFit.jwtTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import project1.constant.exception.ExpiredJwtTokenException;
import project1.constant.exception.InvalidJwtException;
import project1.constant.exception.NotFoundException;
import project1.constant.response.JsonResponse;
import project1.constant.response.JsonResponseStatus;

import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Set<String> authenticationPaths = Set.of("/exercise/", "/post", "/mypage");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String path = httpServletRequest.getRequestURI();

        if (authenticationPaths.stream().anyMatch(path::startsWith)) {
            try {
                String jwt = getBearerToken(httpServletRequest.getHeader(AUTHORIZATION_HEADER));
                jwtTokenProvider.validateToken(jwt);
                Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(request, response);
            } catch (InvalidJwtException e) {
                sendResponse(httpServletResponse, JsonResponseStatus.INVALID_JWT);
            } catch (ExpiredJwtTokenException e) {
                sendResponse(httpServletResponse, JsonResponseStatus.ACCESS_TOKEN_EXPIRED);
            } catch (NotFoundException e) {
                sendResponse(httpServletResponse, JsonResponseStatus.MISSING_JWT);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void sendResponse(HttpServletResponse response, JsonResponseStatus status) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(convertObjectToJson(new JsonResponse<>(status)));
    }

    private String convertObjectToJson(Object object) throws IOException {
        if (object == null)
            return null;
        return objectMapper.writeValueAsString(object);
    }

    private String getBearerToken(String token) {
        if (StringUtils.hasText(token) && token.startsWith("Bearer "))
            return token.substring(7);
        throw new NotFoundException(JsonResponseStatus.MISSING_JWT);
    }
}
