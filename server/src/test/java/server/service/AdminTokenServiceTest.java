package server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AdminTokenServiceTest {

    @Autowired
    private AdminTokenService adminTokenService;
    String adminToken;
    @BeforeEach
    void setUp() throws Exception {
        Field tokenField = AdminTokenService.class.getDeclaredField("token");
        tokenField.setAccessible(true);
        adminToken = (String) tokenField.get(adminTokenService);
    }

    @Test
    void init() throws Exception {
        adminTokenService.init();
        assertFalse(adminTokenService.verifyToken(adminToken));
    }

    @Test
    void verifyToken() {
        // Verify the token
        assertTrue(adminTokenService.verifyToken(adminToken));

        // Verify invalid token
        assertFalse(adminTokenService.verifyToken("invalid_token"));

    }
}