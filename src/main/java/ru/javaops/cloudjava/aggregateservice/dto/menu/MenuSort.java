package ru.javaops.cloudjava.aggregateservice.dto.menu;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpStatus;
import ru.javaops.cloudjava.aggregateservice.exception.MenuAggregateException;

public enum MenuSort {
    AZ,
    ZA,
    DATE_ASC,
    DATE_DESC,
    PRICE_ASC,
    PRICE_DESC,
    RATE_ASC,
    RATE_DESC;

    @JsonCreator
    public static MenuSort fromString(String str) {
        try {
            return MenuSort.valueOf(str.toUpperCase());
        } catch (IllegalArgumentException e) {
            var msg = "Failed to create MenuSort from string: %s".formatted(str);
            throw new MenuAggregateException(msg, HttpStatus.BAD_REQUEST);
        }
    }
}
