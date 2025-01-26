package ru.javaops.cloudjava.aggregateservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.javaops.cloudjava.aggregateservice.BaseTest;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.MenuAggregate;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static ru.javaops.cloudjava.aggregateservice.testutil.TestConstants.BASE_URL;
import static ru.javaops.cloudjava.aggregateservice.testutil.TestConstants.MENU_ONE_ID;
import static ru.javaops.cloudjava.aggregateservice.testutil.TestDateProvider.expectedMenuAggregate;
import static ru.javaops.cloudjava.aggregateservice.testutil.TestDateProvider.expectedMenuAggregateWithFallback;

@AutoConfigureWebTestClient(timeout = "20000")
class MenuAggregateControllerTest extends BaseTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void getMenuAggregateInfo_returnsCorrectResponse() {
        stubForCorrectMenuItemResponse();
        stubForCorrectRatedReviewsList();

        webTestClient.get()
                .uri(BASE_URL + "/" + MENU_ONE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MenuAggregate.class)
                .value(response -> {
                    assertThat(response).isEqualTo(expectedMenuAggregate());
                });
    }

    @Test
    void getMenuAggregateInfo_returnsNotFoundWhenMenuItemNotFound() {
        stubForMenuItemNotFound();
        stubForCorrectRatedReviewsList();

        webTestClient.get()
                .uri(BASE_URL + "/1000")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getMenuAggregateInfo_returnsCorrectResponse_withDefaultRatingAndReviewsInfo_whenReviewServiceUnavailable() {
        stubForRatedReviewsList500Error();
        stubForCorrectMenuItemResponse();

        webTestClient.get()
                .uri(BASE_URL + "/" + MENU_ONE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(MenuAggregate.class)
                .value(response -> {
                    assertThat(response).isEqualTo(expectedMenuAggregateWithFallback());
                });
    }

    @Test
    void getMenuAggregateInfo_returns503_whenMenuServiceUnavailable() {
        stubForMenuItem500Error();
        stubForCorrectRatedReviewsList();

        webTestClient.get()
                .uri(BASE_URL + "/" + MENU_ONE_ID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE.value());
    }
}