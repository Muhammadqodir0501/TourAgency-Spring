package org.example.touragency.model.enity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.touragency.model.base.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseEntity {
    private Tour tour;
    private String text;
    private User user;

    private Boolean isActive = true;
}
