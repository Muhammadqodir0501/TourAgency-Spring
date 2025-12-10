package org.example.touragency.dto.request;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TourAddDto {
    private UUID agencyId;
    private String agencyName;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate returnDate;
    private BigDecimal price;
    private String hotel;
    private String city;
    private Integer seatsTotal;
}
