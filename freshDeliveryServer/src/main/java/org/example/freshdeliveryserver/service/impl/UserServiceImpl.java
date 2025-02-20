package org.example.freshdeliveryserver.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.freshdeliveryserver.entity.Role;
import org.example.freshdeliveryserver.entity.User;
import org.example.freshdeliveryserver.exception.BizException;
import org.example.freshdeliveryserver.mappers.UserMapper;
import org.example.freshdeliveryserver.service.UserService;
import org.example.freshdeliveryserver.utils.JavaMailUtil;
import org.example.freshdeliveryserver.utils.JwtUtil;
import org.example.freshdeliveryserver.vo.RegisterRequest;
import org.example.freshdeliveryserver.vo.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {   
    @Resource
    private UserMapper userMapper;
    @Resource
    private JavaMailUtil javaMailUtil;
    @Resource
    private PasswordEncoder bCryptPasswordEncoder;
    @Resource
    JwtUtil jwtUtil;
    @Override
    public void getCodeForRegister(String email) {
        User userByEmail = userMapper.findUserByEmail(email);
        if(userByEmail == null){
            try{
                javaMailUtil.sendCode(email);
            }catch (Exception e){
                e.printStackTrace();
                throw new BizException("服务端验证码发送失败");
            }
        }else{
            throw new BizException("邮箱已存在");
        }   
    }

    @Override
    public UserResponse register(String email, String password, String code, String username, String phone) {
        boolean result = javaMailUtil.validateVerificationCode(email, code);
        if(result){
            String passwordResult = bCryptPasswordEncoder.encode(password);
            User user = new User(username, email,passwordResult, Role.配送员,phone,false);
            int addUser = userMapper.addUser(user);
            if(addUser > 0){
                try{
                    User userByEmail = userMapper.findUserByEmail(email);
                    User resultUser = new User(userByEmail.getUserId(),userByEmail.getUsername(),userByEmail.getEmail(),userByEmail.getRole(),userByEmail.getPhone(),userByEmail.getIsVerified());
                    String token = jwtUtil.createJwt(resultUser);
                    return new UserResponse(userByEmail.getUserId(),userByEmail.getUsername(),userByEmail.getEmail(),userByEmail.getRole(),userByEmail.getPhone(),userByEmail.getIsVerified(),token);
                } catch (JsonProcessingException e){
                    e.printStackTrace();
                    return null;        
                } 
            }                                                                                                                     
        }
        return null;    
    }

    @Override
    public UserResponse login(String email, String password) {
        User userByEmail = userMapper.findUserByEmail(email);
        if(userByEmail != null){
            if(bCryptPasswordEncoder.matches(password, userByEmail.getPassword())){
                try{        
                    String token = jwtUtil.createJwt(userByEmail);
                    return new UserResponse(userByEmail.getUserId(), userByEmail.getUsername(), userByEmail.getEmail(), userByEmail.getRole(), userByEmail.getPhone(), userByEmail.getIsVerified(),token);
                } catch (JsonProcessingException e){
                    e.printStackTrace();
                    return null;        
                }
            }
        }
        return null;
    }   

    @Override
    public boolean getCodeForUpdatePassword(String email) {
        User userByEmail = userMapper.findUserByEmail(email);
        if(userByEmail != null){
            javaMailUtil.sendCode(email);
            return true;
        }else{
            return false;
        }
    }           

    @Override
    public boolean updatePassword(String email, String password, String code) {
        boolean result = javaMailUtil.validateVerificationCode(email, code);
        if(result){
            String passwordResult = bCryptPasswordEncoder.encode(password);
            int updatePassword = userMapper.updatePassword(passwordResult,email);
            return updatePassword > 0;
        }
        return false;
        
    }

    @Override
    public boolean updateUser(Integer userId, String username, String phone) {
        int updateUser = userMapper.updateUser(userId, username, phone);
        if(updateUser > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateUserLocation(Integer userId, BigDecimal latitude, BigDecimal longitude, LocalDateTime loginTime) {
        int updateUserLocation = userMapper.updateUserLoginTime(userId, loginTime, latitude, longitude);
        if(updateUserLocation > 0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public List<User> getAllUsers(Integer pageNum, Integer pageSize) {
        List<User> users = userMapper.findAllUsers(pageNum, pageSize);
        if(users != null){
            return users;
        }else{
            return null;
        }
    }

    @Override
    public int getUserCount() {
        int userCount = userMapper.countUsers();
        if(userCount > 0){
            return userCount;
        }else{
            return 0;
        }
    }

    @Override
    public boolean deleteUserByUserId(Integer id) {
        int deleteUser = userMapper.deleteUserById(id);
        if(deleteUser > 0){
            return true;
        }else{
            return false;
        }               
    }


}
