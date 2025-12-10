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
@Builder
public class TourResponseDto {
    private UUID id;
    private String agencyName;
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
    private Integer discountPercent;
}
