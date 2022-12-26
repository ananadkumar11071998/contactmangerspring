package com.java.server.main.controller;

import javax.validation.Valid;

import com.java.server.main.entity.PersonFrom;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class homecontroller {
	@GetMapping("/form")
    public String showForm(PersonFrom personFrom) {
        return "form";
    }

    @PostMapping("/")
    public String checkPersonInfo(@Valid PersonFrom personFrom, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "form";
        }

        return "redirect:/result";
    }
    
    @GetMapping("/index")
    public String indexform()
    {
    	return "index";
    }
}

