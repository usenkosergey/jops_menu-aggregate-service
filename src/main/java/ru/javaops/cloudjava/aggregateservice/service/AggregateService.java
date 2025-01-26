package ru.javaops.cloudjava.aggregateservice.service;

import reactor.core.publisher.Mono;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.MenuAggregate;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.MenuAggregateList;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.RatedMenuSort;
import ru.javaops.cloudjava.aggregateservice.dto.menu.Category;
import ru.javaops.cloudjava.aggregateservice.dto.review.ReviewSort;

public interface AggregateService {

    Mono<MenuAggregate> getMenuAggregateInfo(Long menuId, ReviewSort sort, int from, int size);

    Mono<MenuAggregateList> getMenusWithRatings(Category category, RatedMenuSort sort);
}
