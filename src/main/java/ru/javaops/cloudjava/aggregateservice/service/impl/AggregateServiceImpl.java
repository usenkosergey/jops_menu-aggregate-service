package ru.javaops.cloudjava.aggregateservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.javaops.cloudjava.aggregateservice.client.MenuClient;
import ru.javaops.cloudjava.aggregateservice.client.ReviewsClient;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.MenuAggregate;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.MenuAggregateList;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.RatedMenuSort;
import ru.javaops.cloudjava.aggregateservice.dto.menu.Category;
import ru.javaops.cloudjava.aggregateservice.dto.menu.MenuItem;
import ru.javaops.cloudjava.aggregateservice.dto.review.GetRatingsRequest;
import ru.javaops.cloudjava.aggregateservice.dto.review.MenuRatingInfo;
import ru.javaops.cloudjava.aggregateservice.dto.review.RatedReviewsList;
import ru.javaops.cloudjava.aggregateservice.dto.review.ReviewSort;
import ru.javaops.cloudjava.aggregateservice.mapper.MenuAggregateMapper;
import ru.javaops.cloudjava.aggregateservice.service.AggregateService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AggregateServiceImpl implements AggregateService {

    private final MenuClient menuClient;
    private final ReviewsClient reviewsClient;
    private final MenuAggregateMapper mapper;

    @Override
    public Mono<MenuAggregate> getMenuAggregateInfo(Long menuId, ReviewSort sort, int from, int size) {
        return Mono.zip(
                        values -> mapper.createMenuAggregate((MenuItem) values[0], (RatedReviewsList) values[1]),
                        menuClient.getMenuItem(menuId),
                        reviewsClient.getReviewsWithMenuRating(menuId, from, size, sort)
                )
                .doOnError(t -> log.error("Failed to getMenuAggregateInfo: {}", t.toString()));
    }

    @Override
    public Mono<MenuAggregateList> getMenusWithRatings(Category category, RatedMenuSort sort) {
        return menuClient.getMenusForCategory(category, sort)
                .flatMap(items -> getRatingsForMenuItems(items)
                        .map(ratingsMap -> mapper.createMenuAggregateList(ratingsMap, items, sort)))
                .doOnError(t -> log.error("Failed to getMenusWithRatings: {}", t.toString()));
    }

    private Mono<Map<Long, MenuRatingInfo>> getRatingsForMenuItems(List<MenuItem> items) {
        Set<Long> ids = items.stream().map(MenuItem::getId).collect(Collectors.toSet());
        return reviewsClient.getMenuRatings(new GetRatingsRequest(ids))
                .map(response -> response.getMenuRatings().stream()
                        .collect(Collectors.toMap(MenuRatingInfo::getMenuId, Function.identity())));
    }
}
