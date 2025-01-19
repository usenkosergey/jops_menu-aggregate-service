package ru.javaops.cloudjava.aggregateservice.dto.aggregate;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.javaops.cloudjava.aggregateservice.dto.menu.Category;
import ru.javaops.cloudjava.aggregateservice.dto.menu.IngredientCollection;
import ru.javaops.cloudjava.aggregateservice.dto.menu.MenuItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class RatedMenuItem extends MenuItem {
    private Float wilsonScore;
    private Float avgStars;

    @Builder
    public RatedMenuItem(Long id,
                         String name,
                         String description,
                         BigDecimal price,
                         Category category,
                         long timeToCook,
                         double weight,
                         String imageUrl,
                         LocalDateTime updatedAt,
                         LocalDateTime createdAt,
                         IngredientCollection ingredientCollection,
                         Float wilsonScore,
                         Float avgStars) {
        super(id, name, description, price, category, timeToCook, weight, imageUrl, updatedAt, createdAt, ingredientCollection);
        this.wilsonScore = wilsonScore;
        this.avgStars = avgStars;
    }
}
