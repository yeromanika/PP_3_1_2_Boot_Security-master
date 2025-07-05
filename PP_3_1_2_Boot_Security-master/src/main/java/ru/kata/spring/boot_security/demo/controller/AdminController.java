package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPanel(@AuthenticationPrincipal User currentUser,
                             Model model) {
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "admin";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam List<Long> roleIds,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("newUser", user);
            model.addAttribute("allRoles", roleService.getAllRoles());
            return "admin";
        }
        userService.saveUser(user, roleIds);
        return "redirect:/admin";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String username,
                             @RequestParam String email,
                             @RequestParam Integer age,
                             @RequestParam List<Long> roleIds,
                             RedirectAttributes redirectAttributes) {

        try {
            // 1. Находим пользователя
            User user = userService.findById(id);
            if (user == null) {
                throw new RuntimeException("User not found");
            }

            // 2. Обновляем основные данные
            user.setUsername(username);
            user.setEmail(email);
            user.setAge(age);

            // 3. Обновляем роли
            Set<Role> roles = roleService.findByIdIn(roleIds);
            user.setRoles(roles);

            // 4. Сохраняем изменения
            userService.update(user);

            redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно обновлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при обновлении: " + e.getMessage());
        }

        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}