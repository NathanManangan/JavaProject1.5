package com.nighthawk.spring_portfolio.mvc.translate;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // annotation to create a RESTful web services
@RequestMapping("/api/google-translate1")  //prefix of API
public class translate {
    private JSONObject body; //last run result
    private HttpStatus status; //last run status
    String last_run = null; //last run day of month

    // GET Covid 19 Stats
    @GetMapping("/daily")   //added to end of prefix as endpoint
    public ResponseEntity<JSONObject> getTranslate() {

        //calls API once a day, sets body and status properties
        String today = new Date().toString().substring(0,10); 
        if (last_run == null || !today.equals(last_run))
        {
            try {  //APIs can fail (ie Internet or Service down)

                //RapidAPI header
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://google-translate1.p.rapidapi.com/language/translate/v2/detect"))
                        .header("content-type", "application/x-www-form-urlencoded")
                        .header("Accept-Encoding", "application/gzip")
                        .header("X-RapidAPI-Key", "1549fa8a93msh73da228ed60b0a1p1beb30jsn1480b3d082a4")
                        .header("X-RapidAPI-Host", "google-translate1.p.rapidapi.com")
                        .method("POST", HttpRequest.BodyPublishers.ofString("q=English%20is%20hard%2C%20but%20detectably%20so"))
                        .build();
                HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

                //JSONParser extracts text body and parses to JSONObject
                this.body = (JSONObject) new JSONParser().parse(response.body());
                this.status = HttpStatus.OK;  //200 success
                this.last_run = today;
            }
            catch (Exception e) {  //capture failure info
                HashMap<String, String> status = new HashMap<>();
                status.put("status", "RapidApi failure: " + e);

                //Setup object for error
                this.body = (JSONObject) status;
                this.status = HttpStatus.INTERNAL_SERVER_ERROR; //500 error
                this.last_run = null;
            }
        }

        //return JSONObject in RESTful style
        return new ResponseEntity<>(body, status);
    }
}