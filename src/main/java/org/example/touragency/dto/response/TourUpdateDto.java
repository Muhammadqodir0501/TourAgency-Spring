package org.example.touragency.dto.response;

import lombok.*;
import org.example.touragency.model.enity.City;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TourUpdateDto {
    private UUID tourId;
    private UUID agencyId;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate returnDate;
    private BigDecimal price;
    private String hotel;
    private City city;
    private Integer seatsTotal;
    private Integer seatsAvailable;
    private boolean isAvailable = true;
    private Integer discountPercent;
}
