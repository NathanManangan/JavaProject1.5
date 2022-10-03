package com.nighthawk.spring_portfolio.controllers;
/* MVC code that shows defining a simple Model, calling View, and this file serving as Controller
 * Web Content with Spring MVCSpring Example: https://spring.io/guides/gs/serving-web-con
 */

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Controller // HTTP requests are handled as a controller, using the @Controller annotation
public class StockController {

    private JSONObject body; // last run result
    private HttpStatus status; // last run status
    String last_run = null; // last run day of month

    // @GetMapping handles GET request for /greet, maps it to greeting() method
    @GetMapping("/stock")
    // @RequestParam handles variables binding to frontend, defaults, etc
    public String stock(@RequestParam(name = "name", required = false, defaultValue = "AAPL") String name,
            Model model) {
        String result = "";

        // RapidAPI header
        // https://rapidapi.com/spamakashrajtech/api/corona-virus-world-and-india-data
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://yahoo-finance97.p.rapidapi.com/stock-info"))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("X-RapidAPI-Key", "fcb0180c86msh6769be30d1432b1p12b89fjsndca9d1297057")
                .header("X-RapidAPI-Host", "yahoo-finance97.p.rapidapi.com")
                .method("POST", HttpRequest.BodyPublishers.ofString("symbol=" + name))
                .build();

        // RapidAPI request and response
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            // RapidAPI Body
            this.body = (JSONObject) new JSONParser().parse(response.body());
            this.status = HttpStatus.OK; // 200 success

            JSONObject data = (JSONObject) this.body.get("data");
            result = data.get("longBusinessSummary").toString();

        } catch (Exception e) {
            result = "Invalid Input, try again";
        }

        // model attributes are visible to Thymeleaf when HTML is "pre-processed"
        model.addAttribute("result", result);

        // load HTML VIEW (greet.html)
        return "stock";

    }

}