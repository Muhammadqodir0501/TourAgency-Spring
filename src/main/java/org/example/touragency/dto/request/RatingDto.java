package org.example.touragency.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RatingDto {
    UUID tourId;
    UUID userId;
    Float rating;
}
