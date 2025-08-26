package org.sportygroup.model;


import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EventStatus {
    @NotBlank(message = "eventId is required")
    private String eventId;

    @NotNull(message = "status is required")
    private Boolean live;

    public Boolean isLive() { return live; }
}
