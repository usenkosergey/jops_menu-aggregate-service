package ru.javaops.cloudjava.aggregateservice.dto.aggregate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.javaops.cloudjava.aggregateservice.dto.menu.MenuItem;
import ru.javaops.cloudjava.aggregateservice.dto.review.ErrorResponse;
import ru.javaops.cloudjava.aggregateservice.dto.review.MenuRatingInfo;
import ru.javaops.cloudjava.aggregateservice.dto.review.Review;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuAggregate {
    private MenuItem menuItem;
    private List<Review> reviews;
    private MenuRatingInfo ratingInfo;
    private ErrorResponse errorResponse;
}
