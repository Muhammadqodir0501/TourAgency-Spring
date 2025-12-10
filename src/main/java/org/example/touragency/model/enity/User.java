package org.example.touragency.model.enity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.touragency.model.Role;
import org.example.touragency.model.base.BaseEntity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private Role role;



/*

    private Role role = Role.USER;
*/

}
