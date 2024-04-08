package server.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import server.service.AdminTokenService;

import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    AdminTokenService tokenService;
    AdminController adminc;
    @Test
    void verifyToken() {
        tokenService = new TestAdminTokenService();
        adminc = new AdminController(tokenService);
        assertFalse(adminc.verifyToken("Secret!").getBody());
        tokenService.init();
        assertTrue(adminc.verifyToken("Secret!").getBody());
        assertFalse(adminc.verifyToken("BadToken").getBody());
    }
}
