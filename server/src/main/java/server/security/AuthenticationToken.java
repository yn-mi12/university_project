package server.security;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;

/**
 * Simple Authentication object that supports a simple check to see if the
 * user making a request has admin privileges.
 *
 * @author Jake Nijssen
 */
public class AuthenticationToken extends AbstractAuthenticationToken {

    private final String adminToken;
    private final boolean isAdmin;
    private boolean isValid;

    /**
     * Constructor for an unverified admin authentication token.
     *
     * @param adminToken The user provided admin token.
     */
    public AuthenticationToken(@NotNull String adminToken) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.adminToken = adminToken;
        this.isAdmin = true;
        this.isValid = false;
    }

    /**
     * Constructor for an Authentication token for a user that is anonymous
     */
    public AuthenticationToken() {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.adminToken = "None";
        this.isAdmin = false;
        this.isValid = true;
    }

    /**
     * Returns the admin token or "None" if this token is for an unverified user.
     *
     * @return A string containing a token or "None" if anonymous.
     */
    @Override
    public @NotNull String getCredentials() {
        return this.adminToken;
    }

    /**
     * Returns the admin token or "None" if this token is for an unverified user.
     *
     * @return A string containing a token or "None" if anonymous.
     */
    @Override
    public @NotNull String getPrincipal() {
        return this.adminToken;
    }

    /**
     * Check if this user is an admin.
     *
     * @return If the user is a valid admin.
     */
    public boolean isAdmin() {
        return this.isAdmin && this.isValid;
    }

    /**
     * Change the validity of this token.
     *
     * @param isValid The validity of this token.
     */
    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

}
