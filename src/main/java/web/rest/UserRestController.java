package web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.model.User;
import web.service.UserService;

@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    private UserService userService;

    @GetMapping("/getCurrentUser")
    public ResponseEntity<User> getUser(Authentication authentication) {
        User user = new User();

        if (authentication != null) {
            String name = authentication.getName();
            user = userService.getUserByLogin(name);
        } else {
            user.setUsername("not authorized!");
        }
        return ResponseEntity.ok(user);
    }
}