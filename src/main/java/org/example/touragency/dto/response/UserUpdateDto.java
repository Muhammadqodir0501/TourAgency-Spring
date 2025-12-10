package org.example.touragency.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserUpdateDto {
    private UUID userId;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
}
