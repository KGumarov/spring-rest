package web.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.List;


@Controller
@RequestMapping("/")
public class MainController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public MainController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("admin")
    public String adminPage(Model model) {
        List<User> list = userService.getAllUsers();
        model.addAttribute("users", list);
        return "admin";
    }

    @GetMapping("user")
    public String userPage(Model model, Authentication authentication) {
        model.addAttribute("user", userService.getUserByLogin(authentication.getName()));
        return "user";
    }
}