package ru.javaops.cloudjava.aggregateservice.dto.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {
    private String message;
    private String reason;
}
