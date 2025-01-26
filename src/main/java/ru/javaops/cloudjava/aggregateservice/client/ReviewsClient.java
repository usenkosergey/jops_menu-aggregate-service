package ru.javaops.cloudjava.aggregateservice.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.javaops.cloudjava.aggregateservice.config.props.ExternalServiceProps;
import ru.javaops.cloudjava.aggregateservice.dto.review.GetRatingsRequest;
import ru.javaops.cloudjava.aggregateservice.dto.review.RatedReviewsList;
import ru.javaops.cloudjava.aggregateservice.dto.review.RatingsList;
import ru.javaops.cloudjava.aggregateservice.dto.review.ReviewSort;

@Component
public class ReviewsClient extends BaseClient {

    private final WebClient webClient;

    public ReviewsClient(WebClient.Builder webclientBuilder,
                         ExternalServiceProps props) {
        super(props);
        this.webClient = webclientBuilder
                .baseUrl(props.getReviewServiceUrl())
                .build();
    }

    public Mono<RatedReviewsList> getReviewsWithMenuRating(Long menuId, int from, int size, ReviewSort sort) {
        // TODO
        return null;
    }

    public Mono<RatingsList> getMenuRatings(GetRatingsRequest request) {
        // TODO
        return null;
    }
}