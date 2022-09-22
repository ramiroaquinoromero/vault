package com.example.velocity.limit.apis;

import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;

import static com.example.velocity.limit.services.VelocityLimitService.processJsonInformation;
import static com.example.velocity.limit.services.VelocityLimitService.readJsonToString;

@RestController
public class TestController {
    @GetMapping("/test-load")
    public String testLoadJsonFile() throws IOException, JSONException, ParseException {
        String jsonData = readJsonToString();
        if (jsonData.length()>0){
            Boolean response = processJsonInformation(jsonData);
            return jsonData;
        }
        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The file is empty.");
        }
    }

    @GetMapping("/")
    public String home(){
        return "THIS IS THE HOMEPAGE";
    }

}
