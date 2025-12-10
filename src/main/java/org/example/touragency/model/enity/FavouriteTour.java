package org.example.touragency.model.enity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.touragency.model.base.BaseEntity;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavouriteTour extends BaseEntity {

    private UUID userId;
    private UUID tourId;

}
