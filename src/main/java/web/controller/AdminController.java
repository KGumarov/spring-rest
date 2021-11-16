package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.User;
import web.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String adminPage(Model model) {
        List<User> list = userService.getAllUsers();
        model.addAttribute("users", list);
        return "admin";
    }

    @GetMapping("/{id}")
    public String changeUserPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("action", "Изменить");
        return "change";
    }

    @GetMapping("/new")
    public String addUserPage(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("action", "Добавить");
        return "change";
    }

    @PostMapping("/del")
    public String delUser(@RequestParam("user_id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam("role_select") Long[] roleIds,
                           @RequestParam("password") String password) {
        for (Long id : roleIds) {
            user.addRole(userService.getRole(id));
        }
        user.setPassword(passwordEncoder.encode(password));
        userService.saveUser(user);
        return "redirect:/admin";
    }
}