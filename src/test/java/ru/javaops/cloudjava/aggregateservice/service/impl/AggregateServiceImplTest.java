package ru.javaops.cloudjava.aggregateservice.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.javaops.cloudjava.aggregateservice.BaseTest;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.MenuAggregate;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.MenuAggregateList;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.RatedMenuItem;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.RatedMenuSort;
import ru.javaops.cloudjava.aggregateservice.dto.menu.Category;
import ru.javaops.cloudjava.aggregateservice.dto.review.ReviewSort;
import ru.javaops.cloudjava.aggregateservice.exception.MenuAggregateException;

import java.util.Comparator;

import static ru.javaops.cloudjava.aggregateservice.testutil.TestConstants.MENU_ONE_ID;
import static ru.javaops.cloudjava.aggregateservice.testutil.TestDateProvider.*;

class AggregateServiceImplTest extends BaseTest {

    @Autowired
    private AggregateServiceImpl aggregateService;

    @Test
    void getMenuAggregateInfo_returnsCorrectResponse() {
        stubForCorrectRatedReviewsList();
        stubForCorrectMenuItemResponse();

        StepVerifier.create(getMenuAggregateMono())
                .expectNextMatches(response ->
                        response.equals(expectedMenuAggregate()))
                .verifyComplete();
    }

    @Test
    void getMenuAggregateInfo_returnsErrorWhenMenuServiceUnavailable() {
        stubForCorrectRatedReviewsList();
        stubForMenuItem500Error();

        StepVerifier.create(getMenuAggregateMono())
                .expectError(MenuAggregateException.class)
                .verify();
    }

    @Test
    void getMenuAggregateInfo_returnsFallbackWhenReviewServiceUnavailable() {
        stubForRatedReviewsList500Error();
        stubForCorrectMenuItemResponse();

        StepVerifier.create(getMenuAggregateMono())
                .expectNextMatches(response ->
                        response.equals(expectedMenuAggregateWithFallback()))
                .verifyComplete();
    }

    @Test
    void getMenuAggregateInfo_returnsErrorWhenMenuServiceTimedOut() {
        stubForCorrectRatedReviewsList();
        stubForMenuItemTimeout();

        StepVerifier.create(getMenuAggregateMono())
                .expectError(MenuAggregateException.class)
                .verify();
    }

    @Test
    void getMenuAggregateInfo_returnsFallbackWhenReviewServiceTimedOut() {
        stubForRatedReviewsListTimeout();
        stubForCorrectMenuItemResponse();

        StepVerifier.create(getMenuAggregateMono())
                .expectNextMatches(response ->
                        response.equals(expectedMenuAggregateWithFallback()))
                .verifyComplete();
    }

    @Test
    void getMenusWithRatings_returnsCorrectResponse() {
        stubForCorrectMenuListResponse();
        stubForCorrectMenuRatingsResponse();

        StepVerifier.create(getMenuRatingsMono())
                .expectNextMatches(response ->
                        response.equals(expectedMenuAggregateList(Comparator.comparing(RatedMenuItem::getCreatedAt).reversed())))
                .verifyComplete();
    }

    @Test
    void getMenusWithRatings_returnsErrorWhenMenuServiceUnavailable() {
        stubForMenuItemList500Error();
        stubForCorrectMenuRatingsResponse();

        StepVerifier.create(getMenuRatingsMono())
                .expectError(MenuAggregateException.class)
                .verify();
    }

    @Test
    void getMenusWithRatings_returnsErrorWhenReviewServiceUnavailable() {
        stubForCorrectMenuListResponse();
        stubForMenuRatings500Error();

        StepVerifier.create(getMenuRatingsMono())
                .expectError(MenuAggregateException.class)
                .verify();
    }

    @Test
    void getMenusWithRatings_returnsErrorWhenMenuServiceTimedOut() {
        stubForMenuItemListTimeout();
        stubForCorrectMenuRatingsResponse();

        StepVerifier.create(getMenuRatingsMono())
                .expectError(MenuAggregateException.class)
                .verify();
    }

    @Test
    void getMenusWithRatings_returnsErrorWhenReviewServiceTimedOut() {
        stubForCorrectMenuListResponse();
        stubForMenuRatingsTimeout();

        StepVerifier.create(getMenuRatingsMono())
                .expectError(MenuAggregateException.class)
                .verify();
    }

    private Mono<MenuAggregate> getMenuAggregateMono() {
        return aggregateService.getMenuAggregateInfo(MENU_ONE_ID, ReviewSort.DATE_ASC, 0, 10);
    }

    private Mono<MenuAggregateList> getMenuRatingsMono() {
        return aggregateService.getMenusWithRatings(Category.DRINKS, RatedMenuSort.DATE_DESC);
    }
}