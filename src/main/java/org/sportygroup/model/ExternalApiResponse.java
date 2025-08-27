package org.sportygroup.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExternalApiResponse {
    @JsonProperty("eventId")
    private String eventId;

    @JsonProperty("currentScore")
    private String currentScore;

}