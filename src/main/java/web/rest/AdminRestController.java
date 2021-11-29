package web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/list")
    public ResponseEntity<List<User>> listUsers() {
        final List<User> users = userService.getAllUsers();
        return users != null
                ? new ResponseEntity<>(users, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> listRoles() {
        final List<Role> roles = roleService.getAllRoles();
        return roles != null
                ? new ResponseEntity<>(roles, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/saveUser")
    public ResponseEntity<?> saveUser(@RequestBody User theUser) {

        String encode = theUser.getPassword();
        System.out.println(encode);
        if (theUser.getId() != 0) {
            if (encode.isEmpty()) {
                theUser.setPassword(userService.getUser(theUser.getId()).getPassword());
            } else {
                passwordChanged(theUser, encode);
            }
        } else {
            passwordChanged(theUser, encode);
        }
        userService.saveUser(theUser);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void passwordChanged(User theUser, String encode) {
        encode = passwordEncoder.encode(encode);
        theUser.setPassword(encode);
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestBody User theUser) {
        userService.deleteUser(theUser.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
