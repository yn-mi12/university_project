package server.service;

import jakarta.annotation.PostConstruct;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Simple admin token service, which prints and stores an admin token for
 * a runtime session which may be used to perform administration tasks.
 *
 * @author Jake Nijssen
 */
@Service
public class AdminTokenService {

    private String token;

    @PostConstruct
    public void init() {
        this.token = UUID.randomUUID().toString();
        System.out.println("Generated admin token: " + token);
    }

    public boolean verifyToken(@NotNull String adminToken) {
        return adminToken.equals(token);
    }

}
