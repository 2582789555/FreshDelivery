package org.example.freshdeliveryserver.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.freshdeliveryserver.entity.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Integer userId;
    private String username;
    private String email;
    private Role role;
    private String phone;
    private Boolean isVerified;
    private String token;
}
