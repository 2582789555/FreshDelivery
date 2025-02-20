package org.example.freshdeliveryserver.mappers;

import org.apache.ibatis.annotations.Param;
import org.example.freshdeliveryserver.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface UserMapper {
    User findUserByEmail(@Param("email") String email);
    User findUserByUserId(@Param("userId") int userId);
    List<User> findAllUsers(@Param("pageNum")Integer pageNum,@Param("pageSize")Integer pageSize);
    int countUsers(); //获取用户总数

    int addUser(@Param("user") User user);

    int updatePassword(@Param("password") String password,@Param("email") String email);
    int updateUser(@Param("userId")Integer userId, @Param("username")String username, @Param("phone")String phone);
    int updateUserLoginTime(@Param("userId") int userId, @Param("loginTime") LocalDateTime localDateTime, @Param("latitude") BigDecimal latitude, @Param("longitude") BigDecimal longitude) ;

    int deleteUserById(@Param("userId")Integer userId);
    

}