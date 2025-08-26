package org.sportygroup.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sportygroup.model.EventStatus;
import org.sportygroup.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/events")
public class EventsController {
    private static final Logger logger = LoggerFactory.getLogger(EventsController.class);

    @Autowired
    private EventService eventService;

    @PostMapping("/status")
    public ResponseEntity<String> updateEventStatus(@Valid @RequestBody EventStatus eventStatus) {
        try {
            eventService.updateEventStatus(eventStatus);
            return ResponseEntity.ok("Event status updated successfully");
        } catch (Exception e) {
            logger.error("Error updating event status: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Error updating event status");
        }
    }
    // We can create separate Controller for this or use spring default libraries
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        try {
            return ResponseEntity.ok("Events service is up");
        } catch (Exception e) {
            logger.error("Events service is Down: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Events service is Down");
        }
    }
}