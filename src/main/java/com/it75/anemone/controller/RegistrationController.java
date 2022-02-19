package com.it75.anemone.controller;

import com.it75.anemone.dto.UserDto;
import com.it75.anemone.entity.User;
import com.it75.anemone.service.UserConverter;
import com.it75.anemone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    private UserConverter userConverter;

    @GetMapping("/registration")
    public String registration(Model model) {
        if (userService.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("userForm", (new UserDto()));

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("userForm") @Valid UserDto userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        else if (!userForm.getPassword().equals(userForm.getPasswordConfirm())){
            model.addAttribute("passwordError", "Пароли не совпадают");
            return "registration";
        }
        else if (!userService.saveUser(userForm)){
            model.addAttribute("usernameError", "Пользователь с таким именем уже существует");
            return "registration";
        }else{
            userService.saveUser(userForm);
        }
        return "redirect:/";
    }

}
