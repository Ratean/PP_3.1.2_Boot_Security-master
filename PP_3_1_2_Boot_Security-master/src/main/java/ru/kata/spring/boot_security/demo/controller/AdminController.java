package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("admin/users")
public class AdminController {

    private UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("users", userService.getAllUser());
        return "index";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("user", new User());
        return "new";
    }

    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @RequestParam(value = "roleId", required = false) String roleName) {
        if (bindingResult.hasErrors()) {
            return "/new";
        }

        userService.save(user, roleName);
        return "redirect:/admin/users";
    }

    @GetMapping("/{id}/update")
    public String updateUser(Model model, @PathVariable("id") int id) {
        User user = userService.getUser(id);
        user.setPassword("*******");

        model.addAttribute("user", user);
        return "update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                         @RequestParam(value = "roleId", required = false) String roleName) {
        if (bindingResult.hasErrors()) {
            return "/update";
        }

        userService.update(user, roleName);
        return "redirect:/admin/users";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }

    @ModelAttribute
    public void addRoles(Model model) {
        model.addAttribute("roles", userService.getAllRole());
    }
}
