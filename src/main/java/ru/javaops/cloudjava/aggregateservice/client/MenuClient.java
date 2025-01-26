package ru.javaops.cloudjava.aggregateservice.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.javaops.cloudjava.aggregateservice.config.props.ExternalServiceProps;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.RatedMenuSort;
import ru.javaops.cloudjava.aggregateservice.dto.menu.Category;
import ru.javaops.cloudjava.aggregateservice.dto.menu.MenuItem;
import ru.javaops.cloudjava.aggregateservice.exception.MenuAggregateException;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

import static ru.javaops.cloudjava.aggregateservice.dto.aggregate.RatedMenuSort.*;

@Component
public class MenuClient extends BaseClient {

    private static final EnumSet<RatedMenuSort> SUPPORTED_SORTS = EnumSet.of(AZ, ZA, DATE_ASC, DATE_DESC, PRICE_ASC, PRICE_DESC);

    private final WebClient webClient;

    public MenuClient(WebClient.Builder clientBuilder,
                      ExternalServiceProps props) {
        super(props);
        this.webClient = clientBuilder
                .baseUrl(props.getMenuServiceUrl())
                .build();
    }

    public Mono<MenuItem> getMenuItem(Long menuId) {
        var mono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(props.getMenuItemPath())
                        .path("/{menuId}")
                        .build(menuId))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new MenuAggregateException("Menu Service unavailable", HttpStatus.SERVICE_UNAVAILABLE)))
                .bodyToMono(MenuItem.class);

        return applyTimeoutAndHandleExceptions(mono);
    }

    public Mono<List<MenuItem>> getMenusForCategory(Category category, RatedMenuSort sort) {
        var mono = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(props.getMenuItemPath())
                        .queryParam("category", category)
                        .queryParamIfPresent("sort",
                                Optional.of(sort).filter(this::supported))
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new MenuAggregateException("Menu Service unavailable", HttpStatus.SERVICE_UNAVAILABLE)))
                .bodyToMono(new ParameterizedTypeReference<List<MenuItem>>() {
                });

        return applyTimeoutAndHandleExceptions(mono);
    }

    private boolean supported(RatedMenuSort sort) {
        return SUPPORTED_SORTS.contains(sort);
    }
}
