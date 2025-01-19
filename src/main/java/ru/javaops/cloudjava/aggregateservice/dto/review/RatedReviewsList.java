package ru.javaops.cloudjava.aggregateservice.dto.review;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatedReviewsList {
    private List<Review> reviews;
    private MenuRatingInfo menuRating;
}
