package ru.javaops.cloudjava.aggregateservice.client;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import ru.javaops.cloudjava.aggregateservice.config.props.ExternalServiceProps;
import ru.javaops.cloudjava.aggregateservice.exception.MenuAggregateException;

public abstract class BaseClient {

    protected final ExternalServiceProps props;

    protected BaseClient(ExternalServiceProps props) {
        this.props = props;
    }

    protected <T> Mono<T> applyTimeoutAndHandleExceptions(Mono<T> mono) {
        return mono
                .timeout(props.getDefaultTimeout())
                .onErrorMap(this::handleThrowable);
    }

    private Throwable handleThrowable(Throwable t) {
        if (t instanceof MenuAggregateException) {
            return t;
        }
        HttpStatus status;
        // In JDK 21 "Pattern Matching for Switch" (https://openjdk.org/jeps/441) no preview feature anymore
        if (t instanceof WebClientResponseException.NotFound) {
            status = HttpStatus.NOT_FOUND;
        } else if (t instanceof WebClientResponseException.BadRequest) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new MenuAggregateException(t.getMessage(), status);
    }
}
