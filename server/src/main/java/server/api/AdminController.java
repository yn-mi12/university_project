package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import server.security.RequiresAdmin;
import server.service.AdminTokenService;

@Controller
@RequestMapping("api/admin")
public class AdminController {

    private final AdminTokenService tokenService;

    @Autowired
    public AdminController(AdminTokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/verify/{token}")
    public ResponseEntity<Boolean> verifyToken(@PathVariable("token") String token) {
        return ResponseEntity.ok(tokenService.verifyToken(token));
    }

    @RequiresAdmin
    @GetMapping("/")
    public ResponseEntity<String> someProtectedRoute() {
        return ResponseEntity.ok("You got some juicy secrets :O");
    }

}
