package ru.javaops.cloudjava.aggregateservice.client;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.javaops.cloudjava.aggregateservice.config.props.ExternalServiceProps;
import ru.javaops.cloudjava.aggregateservice.dto.review.GetRatingsRequest;
import ru.javaops.cloudjava.aggregateservice.dto.review.RatedReviewsList;
import ru.javaops.cloudjava.aggregateservice.dto.review.RatingsList;
import ru.javaops.cloudjava.aggregateservice.dto.review.ReviewSort;
import ru.javaops.cloudjava.aggregateservice.exception.MenuAggregateException;

@Component
public class ReviewsClient extends BaseClient {

    private static final String REVIEW_BACKEND = "reviewBackend";

    private final WebClient webClient;

    public ReviewsClient(WebClient.Builder webclientBuilder,
                         ExternalServiceProps props) {
        super(props);
        this.webClient = webclientBuilder
                .baseUrl(props.getReviewServiceUrl())
                .build();
    }

    @CircuitBreaker(name = REVIEW_BACKEND, fallbackMethod = "getReviewsWithMenuRatingCBFallback")
    @Retry(name = REVIEW_BACKEND, fallbackMethod = "getReviewsWithMenuRatingRetryFallback")
    public Mono<RatedReviewsList> getReviewsWithMenuRating(Long menuId, int from, int size, ReviewSort sort) {
        // TODO
        return null;
    }

    @CircuitBreaker(name = REVIEW_BACKEND)
    @Retry(name = REVIEW_BACKEND)
    public Mono<RatingsList> getMenuRatings(GetRatingsRequest request) {
        // TODO
        return null;
    }

    private Mono<RatedReviewsList> getReviewsWithMenuRatingCBFallback(Long menuId, int from, int size, ReviewSort sort, CallNotPermittedException e) {
        return Mono.just(RatedReviewsList.fallbackResponse(menuId));
    }

    private Mono<RatedReviewsList> getReviewsWithMenuRatingRetryFallback(Long menuId, int from, int size, ReviewSort sort, MenuAggregateException e) {
        return Mono.just(RatedReviewsList.fallbackResponse(menuId));
    }
}