package server.security;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

/**
 * Simple authorization manager which protects routes marked with {@link RequiresAdmin} and checks
 * if the provided {@link AuthenticationToken} has admin privileges.
 *
 * @author Jake Nijssen
 */
@Component
public class AdminAuthorizationManager implements AuthorizationManager<MethodInvocation> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, MethodInvocation method) {
        if (authentication.get() instanceof AuthenticationToken token) {
            if (method.getMethod().isAnnotationPresent(RequiresAdmin.class)) {
                return new AuthorizationDecision(token.isAdmin());
            } else {
                return new AuthorizationDecision(true);
            }
        }
        return null;
    }
}
