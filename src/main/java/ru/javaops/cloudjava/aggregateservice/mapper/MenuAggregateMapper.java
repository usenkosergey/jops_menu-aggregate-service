package ru.javaops.cloudjava.aggregateservice.mapper;

import org.springframework.stereotype.Component;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.MenuAggregate;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.MenuAggregateList;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.RatedMenuItem;
import ru.javaops.cloudjava.aggregateservice.dto.aggregate.RatedMenuSort;
import ru.javaops.cloudjava.aggregateservice.dto.menu.MenuItem;
import ru.javaops.cloudjava.aggregateservice.dto.review.MenuRatingInfo;
import ru.javaops.cloudjava.aggregateservice.dto.review.RatedReviewsList;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static ru.javaops.cloudjava.aggregateservice.dto.aggregate.RatedMenuSort.RATE_ASC;
import static ru.javaops.cloudjava.aggregateservice.dto.aggregate.RatedMenuSort.RATE_DESC;

@Component
public class MenuAggregateMapper {

    private static final EnumSet<RatedMenuSort> IN_MEMORY_SORTS = EnumSet.of(RATE_ASC, RATE_DESC);

    public MenuAggregateList createMenuAggregateList(Map<Long, MenuRatingInfo> ratings,
                                                     List<MenuItem> items,
                                                     RatedMenuSort sort) {
        var itemsStream = items.stream().map(menuItem -> {
            MenuRatingInfo ratingInfo = ratings.get(menuItem.getId());
            return RatedMenuItem.builder()
                    .id(menuItem.getId())
                    .name(menuItem.getName())
                    .description(menuItem.getDescription())
                    .price(menuItem.getPrice())
                    .category(menuItem.getCategory())
                    .timeToCook(menuItem.getTimeToCook())
                    .weight(menuItem.getWeight())
                    .imageUrl(menuItem.getImageUrl())
                    .createdAt(menuItem.getCreatedAt())
                    .updatedAt(menuItem.getUpdatedAt())
                    .ingredientCollection(menuItem.getIngredientCollection())
                    .wilsonScore(ratingInfo.getWilsonScore())
                    .avgStars(ratingInfo.getAvgStars())
                    .build();
        });
        if (shouldApplyInMemorySort(sort)) {
            itemsStream = itemsStream.sorted(sort.getComparator());
        }
        var result = itemsStream.toList();

        return MenuAggregateList.builder().menuItems(result).build();
    }

    public MenuAggregate createMenuAggregate(MenuItem menuItem, RatedReviewsList ratedReviewsList) {
        return MenuAggregate.builder()
                .menuItem(menuItem)
                .reviews(ratedReviewsList.getReviews())
                .ratingInfo(ratedReviewsList.getMenuRating())
                .errorResponse(ratedReviewsList.getErrorResponse())
                .build();
    }

    private boolean shouldApplyInMemorySort(RatedMenuSort sort) {
        return IN_MEMORY_SORTS.contains(sort);
    }

}
