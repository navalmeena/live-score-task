package org.sportygroup.controller;

import org.sportygroup.model.ExternalApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/mock-api")
public class MockApiController {

    @GetMapping("/events/{eventId}")
    public ExternalApiResponse getEventScore(@PathVariable String eventId) {
        // Generate a random score for demonstration
        Random random = new Random();
        String score = random.nextInt(5) + ":" + random.nextInt(5);
        ExternalApiResponse response =  new ExternalApiResponse();
        response.setEventId(eventId);
        response.setCurrentScore(score);
        return response;
    }
}