package org.example.freshdeliveryserver.service;

import org.example.freshdeliveryserver.entity.User;
import org.example.freshdeliveryserver.vo.RegisterRequest;
import org.example.freshdeliveryserver.vo.UserResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    // 注册(配送员注册)
    void getCodeForRegister(String email);
    UserResponse register(String email, String password, String code, String username, String phone);
    // 登录
    UserResponse login(String email, String password);
    // 找回密码
    boolean getCodeForUpdatePassword(String email);
    boolean updatePassword(String email, String password, String code);

    boolean updateUser(Integer userId, String username, String phone);

    boolean updateUserLocation(Integer userId, BigDecimal latitude, BigDecimal longitude, LocalDateTime loginTime);

    List<User> getAllUsers(Integer pageNum, Integer pageSize);

    int getUserCount();

    boolean deleteUserByUserId(Integer id);

}
