package server.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.util.Optional;

public class AdminAuthFilter extends AbstractAuthenticationProcessingFilter {

    protected AdminAuthFilter(final AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("**"), authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        Optional<String> header = Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));
        if (header.isPresent()) {
            String adminToken = header.get().replaceFirst("Bearer ", "").trim();
            AuthenticationToken authenticationToken = new AuthenticationToken(adminToken);
            return getAuthenticationManager().authenticate(authenticationToken);
        }

        // Default return anonymous token
        return new AuthenticationToken();
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
