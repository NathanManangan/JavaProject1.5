package com.nighthawk.spring_portfolio.controllers;
/* MVC code that shows defining a simple Model, calling View, and this file serving as Controller
 * Web Content with Spring MVCSpring Example: https://spring.io/guides/gs/serving-web-con
 */

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nighthawk.spring_portfolio.models.calculator.*;

@Controller // HTTP requests are handled as a controller, using the @Controller annotation
public class GuessingController {

    // @GetMapping handles GET request for /greet, maps it to greeting() method
    @GetMapping("/guessing")
    // @RequestParam handles variables binding to frontend, defaults, etc
    public String greeting(@RequestParam(name = "name", required = false, defaultValue = "0") String name,
            Model model) {
        
        String output;
        Guessing.randomNumber();
        output = Guessing.guessing(name);

        // model attributes are visible to Thymeleaf when HTML is "pre-processed"
        model.addAttribute("name", output);


        // load HTML VIEW (greet.html)
        return "guessing";

    }
 
    @PostMapping("/new-number")
    public String newNumber() {
        Guessing.newNumber();
        return "guessing";
    }

}