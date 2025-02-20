package org.example.freshdeliveryserver.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.Collection;

public class CustomUserDetails extends User {
    private Integer userId;

    public CustomUserDetails(org.example.freshdeliveryserver.entity.User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), "", authorities);
        this.userId = user.getUserId();
    }

    public Integer getUserId() {
        return userId;
    }
}
