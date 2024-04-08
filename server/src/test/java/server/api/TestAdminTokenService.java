package server.api;

import org.jetbrains.annotations.NotNull;
import server.service.AdminTokenService;

public class TestAdminTokenService extends AdminTokenService {

    private String token;

    @Override
    public void init() {
        token = "Secret!";
        System.out.println("Generated admin token: " + token);
    }
    @Override
    public boolean verifyToken(@NotNull String adminToken) {
        return adminToken.equals(token);
    }
}
