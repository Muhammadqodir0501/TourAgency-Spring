package org.example.touragency.model.enity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.touragency.model.base.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tour extends BaseEntity {
    private UUID agencyId;
    private String title;
    private String description;
    private Integer nights;
    private LocalDate startDate;
    private LocalDate returnDate;
    private BigDecimal price;
    private String hotel;
    private City city;
    private Integer seatsTotal;
    private Integer seatsAvailable;
    private Long views;
    private Float rating;
    private boolean isAvailable = true;
    private Integer discountPercent;


}
