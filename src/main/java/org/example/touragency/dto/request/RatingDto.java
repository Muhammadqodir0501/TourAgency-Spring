package org.example.touragency.dto.request;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RatingDto {
    private UUID tourId;
    private UUID userId;
    private float rate;
}