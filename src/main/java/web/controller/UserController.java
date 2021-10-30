package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web.model.User;
import web.service.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    public final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String mainPage(Model model) {
        List<User> list = userService.getAllUsers();
        model.addAttribute("users", list);
        return "index";
    }

    @GetMapping("/{id}")
    public String changeUserPage(@PathVariable("id") long id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "change";
    }

    @GetMapping("/new")
    public String addUserPage(@ModelAttribute("user") User user) {
        return "change";
    }

    @PostMapping("/del")
    public String delUser(@RequestParam("user_id") long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }

    @PostMapping
    public String saveUser(@ModelAttribute("user") User user){
        userService.saveUser(user);
        return "redirect:/users";
    }

}