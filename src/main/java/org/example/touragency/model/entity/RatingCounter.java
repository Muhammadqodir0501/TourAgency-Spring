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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RatingCounter extends BaseEntity {
    Float averageRating;
    Float ratingAmount;
    UUID tourId;
}