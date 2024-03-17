package server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import server.service.AdminTokenService;

@Component
public class AdminAuthProvider implements AuthenticationProvider {

    private final AdminTokenService tokenService;

    @Autowired
    public AdminAuthProvider(AdminTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        AuthenticationToken authenticationToken = (AuthenticationToken) authentication;

        if (!tokenService.verifyToken(authenticationToken.getCredentials())) {
            throw new BadCredentialsException("Invalid admin token!");
        }

        authenticationToken.setValid(true);
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(AuthenticationToken.class);
    }
}
