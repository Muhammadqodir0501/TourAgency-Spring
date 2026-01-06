package org.example.touragency.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.example.touragency.model.base.BaseEntity;

import java.util.UUID;

@Entity
@Table(name = "rating_counters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingCounter extends BaseEntity {

    @Column(name = "average_rating")
    private Float averageRating = 0f;

    @Column(name = "rating_amount")
    private Integer ratingAmount = 0;

    @Column(name = "tour_id", unique = true, nullable = false)
    private UUID tourId;
}