package org.example.touragency.dto.request;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TourDeleteDto {
    UUID tourId;
    UUID agencyId;
}
