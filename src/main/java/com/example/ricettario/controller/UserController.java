package com.example.ricettario.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.ricettario.entities.Permission;
import com.example.ricettario.entities.User;
import com.example.ricettario.service.PermissionService;
import com.example.ricettario.service.UserService;
import com.example.ricettario.utilities.PermissionType;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/users")
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserService userService;

    private final PermissionService permissionService;

    public UserController(UserService userService, PermissionService permissionService,
            BCryptPasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.permissionService = permissionService;
        this.passwordEncoder = passwordEncoder;

    }

    // Register

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "auth/register";
        }

        // Controllo unicità username/email (oltre al vincolo UNIQUE nel DB)
        if (userService.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username già in uso");
            return "auth/register";
        }
        if (userService.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email già in uso");
            return "auth/register";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Permission userPermission = permissionService.findByType(PermissionType.USER);
        user.setPermission(userPermission);

        userService.create(user);

        return "redirect:/login?registered";
    }

    // USers management routes

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "0") int page, Model usersM) {

        Pageable pageable = PageRequest.of(page, 20, Sort.by("username").ascending());
        Page<User> users = userService.findAll(pageable);

        usersM.addAttribute("users", users);

        return "pages/user/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model userM) {

        userM.addAttribute("user", userService.findById(id));

        return "pages/user/user";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable int id, Model userM) {

        userM.addAttribute("permissions", permissionService.findAll());
        userM.addAttribute("user", userService.findById(id));

        return "pages/user/userForm";

    }

    @PutMapping("/{id}")
    public String update(
            @PathVariable int id, @Validated @ModelAttribute("user") User userU, BindingResult result,
            RedirectAttributes red, Model empModel) {

        boolean hasErrors = result.getFieldErrors().stream()
                .anyMatch(error -> !error.getField().equals("user.password"));

        if (hasErrors) {

            empModel.addAttribute("pass", false);

            return "pages/user/userForm";

        } else {

            User oldUser = userService.findById(id);

            if (userU.getPassword() == "") {

                userU.setPassword(oldUser.getPassword());

            } else {

                userU.setPassword(passwordEncoder.encode(userU.getPassword()));

            }

            if (oldUser.equals(userU)) {

                red.addFlashAttribute("msg", "Nessuna modifica apportata");

                return "redirect:/users/" + userU.getId();

            }

            userU.setPermission(oldUser.getPermission());

            userService.update(userU);

            red.addFlashAttribute("msg", "Utente modificato correttamente");

            return "redirect:/users/" + userU.getId();

        }

    }

    // Authenticated User routes

    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal UserDetails userD, Model userModel) {

        System.out.println("PROFILE");
        // System.out.println(userD.getUsername());

        User user = userService.findByUsername(userD.getUsername());

        userModel.addAttribute("user", user);

        return "pages/user/user";
    }

    @GetMapping("/update")
    public String updateUser(@AuthenticationPrincipal UserDetails userD, Model userM) {

        userM.addAttribute("permissions", permissionService.findAll());
        userM.addAttribute("user", userService.findByUsername(userD.getUsername()));

        return "pages/user/userForm";

    }

    @PutMapping("/update")
    public String putMethodName(
            @Validated @ModelAttribute("user") User userU, BindingResult result,
            @AuthenticationPrincipal User user, RedirectAttributes red, Model empModel,
            HttpServletRequest request) {

        String pass = user.getPassword();

        boolean hasErrors = result.getFieldErrors().stream()
                .anyMatch(error -> !error.getField().equals("user.password"));

        if (hasErrors) {

            empModel.addAttribute("pass", false);

            return "pages/user/userForm";

        } else {

            User oldUser = userService.findByUsername(user.getUsername());

            if (pass == "") {

                userU.setPassword(oldUser.getPassword());

            } else {

                userU.setPassword(passwordEncoder.encode(userU.getPassword()));

            }

            userU.setId(oldUser.getId());
            userU.setPermission(oldUser.getPermission());

            if (oldUser.equals(userU)) {

                red.addFlashAttribute("msg", "Nessuna modifica apportata");

                return "redirect:/users/profile";

            }

            if (userU.getUsername().equals(user.getUsername())
                    && !userU.getPermission().equals(user.getPermission())) {

                userService.update(userU);

                try {

                    request.logout();

                } catch (ServletException e) {

                    e.printStackTrace();

                }

            }

            userService.update(userU);

            red.addFlashAttribute("msg", "Utente modificato correttamente");

            return "redirect:/users/profile";

        }

    }

}
