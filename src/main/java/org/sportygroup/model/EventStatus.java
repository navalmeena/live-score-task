package org.sportygroup.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventStatus {
    @NotBlank(message = "eventId is required")
    private String eventId;

    @NotNull(message = "status is required")
    private Boolean live;

    public Boolean isLive() { return live; }
}
