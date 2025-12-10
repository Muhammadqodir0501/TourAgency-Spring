package org.example.touragency.model.enity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.touragency.model.base.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class City extends BaseEntity {

    private String name;
    private Country country;
}
