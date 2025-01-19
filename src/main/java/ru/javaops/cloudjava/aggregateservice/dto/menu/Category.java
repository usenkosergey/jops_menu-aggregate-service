package ru.javaops.cloudjava.aggregateservice.dto.menu;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;
import ru.javaops.cloudjava.aggregateservice.exception.MenuAggregateException;

public enum Category {
    BREAKFAST,
    LUNCH,
    DINNER,
    DRINKS,
    SNACKS,
    SALADS;

    @JsonCreator
    public static Category fromString(String str) {
        try {
            return Category.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            var msg = "Failed to create Category from string: %s".formatted(str);
            throw new MenuAggregateException(msg, HttpStatus.BAD_REQUEST);
        }
    }
}
