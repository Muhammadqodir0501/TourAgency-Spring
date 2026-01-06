package org.example.touragency.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RatingResponseDto {
    private UUID ratingId;
    private UUID tourId;
    private UUID userId;
    private Float userRating;
    private Float averageRating;
    private Integer ratingCount;
    private String message;
}