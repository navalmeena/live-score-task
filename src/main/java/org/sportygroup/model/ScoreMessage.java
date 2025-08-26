package org.sportygroup.model;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScoreMessage {
    private String eventId;
    private String score;
    private LocalDateTime timestamp = LocalDateTime.now();
}