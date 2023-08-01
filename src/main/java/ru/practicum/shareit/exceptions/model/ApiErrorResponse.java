package ru.practicum.shareit.exceptions.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApiErrorResponse {

    @JsonProperty("error")
    private String errorMessage;
}
