package org.example.freshdeliveryserver.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User implements Serializable {
    private Integer userId;
    private String username;
    private String email;
    private String password;
    private Role role;
    private String phone;
    private Boolean isVerified;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime logintime;

    public User(Integer userId, String username, String email, Role role, String phone, Boolean isVerified) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.isVerified = isVerified;
    }

    public User(String username, String email, String password, Role role, String phone, Boolean isVerified) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.phone = phone;
        this.isVerified = isVerified;
    }
}
