package com.alexki.schedule.controller.auth;

import com.alexki.schedule.dto.UserDto;
import com.alexki.schedule.entities.User;
import com.alexki.schedule.mapper.UserMapper;
import com.alexki.schedule.services.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("register")
public class RegistrationController {

    private final UserService userService;
    private final UserMapper userMapper;

    public RegistrationController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public String register(Model model) {
        model.addAttribute("user", userMapper.toDto(new User()));
        return "auth/register";
    }

    @PostMapping()
    public String registerUser(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors())
            return "auth/register";

        try{
            userService.registerUser(userMapper.toEntity(userDto));
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.user", e.getMessage());
            return "auth/register";
        }

        userService.registerUser(userMapper.toEntity(userDto));

        return "redirect:/login";

    }


}
