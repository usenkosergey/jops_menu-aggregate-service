package ru.javaops.cloudjava.aggregateservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MenuAggregate4xxException extends RuntimeException {

    private final HttpStatus status;

    public MenuAggregate4xxException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
