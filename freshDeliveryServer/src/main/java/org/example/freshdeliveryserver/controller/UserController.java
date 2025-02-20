package org.example.freshdeliveryserver.controller;


import org.example.freshdeliveryserver.entity.CustomUserDetails;
import org.example.freshdeliveryserver.entity.User;
import org.example.freshdeliveryserver.exception.BizException;
import org.example.freshdeliveryserver.service.UserService;
import org.example.freshdeliveryserver.utils.JavaMailUtil;
import org.example.freshdeliveryserver.utils.ResultVOUtil;
import org.example.freshdeliveryserver.vo.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    public Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer userId = customUserDetails.getUserId();
            return userId;
        }
        return 0;
    }

    // 已测
    @PostMapping("/auth/login")
    public Result login(@RequestBody LoginRequest loginRequest){
        if(StringUtils.isEmpty(loginRequest.getEmail()) || 
           StringUtils.isEmpty(loginRequest.getPassword())){
            throw new BizException("请填写完整信息!");
        }
        UserResponse userResponse = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResultVOUtil.success(userResponse);
    }
    // 已测
    @GetMapping("/auth/getCodeForRegister")
    public Result getRegisterCode(String email){    
        try{
            if(StringUtils.isEmpty(email)){
                throw new BizException("邮箱为空");
            }
            userService.getCodeForRegister(email);
            return ResultVOUtil.success("发送成功！");
        }catch (BizException e){
            return ResultVOUtil.error(e.getMessage());
        }
    }
    // 已测
    @PostMapping("/auth/register")
    public Result register(@RequestBody RegisterRequest registerRequest){
        if(StringUtils.isEmpty(registerRequest.getEmail()) || 
           StringUtils.isEmpty(registerRequest.getPassword()) ||
           StringUtils.isEmpty(registerRequest.getUsername()) ||
           StringUtils.isEmpty(registerRequest.getPhone()) ||
           StringUtils.isEmpty(registerRequest.getCode())){
            throw new BizException("请填写完整信息!");
        }
        UserResponse userResponse = userService.register(registerRequest.getEmail(), registerRequest.getPassword(), registerRequest.getCode(), registerRequest.getUsername(), registerRequest.getPhone());
        if(userResponse != null){
            return ResultVOUtil.success(userResponse);
        }
        return ResultVOUtil.error("注册失败!");
    }
    // 已测
    @GetMapping("/auth/getCodeForUpdatePassword")
    public Result getRegisterCodeForUpdatePassword(String email){       
        if(StringUtils.isEmpty(email)){     
            throw new BizException("邮箱为空");
        }   
        boolean result = userService.getCodeForUpdatePassword(email);
        if(result){
            return ResultVOUtil.success("发送成功！");
        }
        return ResultVOUtil.error("该邮箱未注册！");
    }
    // 已测
    @PostMapping("/auth/updatePassword")
    public Result updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest){
        if(StringUtils.isEmpty(updatePasswordRequest.getEmail()) || 
           StringUtils.isEmpty(updatePasswordRequest.getPassword()) || 
           StringUtils.isEmpty(updatePasswordRequest.getCode())){
            throw new BizException("请填写完整信息!");
        }
        boolean result = userService.updatePassword(updatePasswordRequest.getEmail(), 
                                                  updatePasswordRequest.getPassword(), 
                                                  updatePasswordRequest.getCode());
        if(result){
            return ResultVOUtil.success("修改密码成功!");
        }
        return ResultVOUtil.error("与原始密码相同!");
    }

    @PostMapping("/updateUser")
    public Result updateUser(@RequestBody UpdateUserRequest updateUserRequest) {
        if (StringUtils.isEmpty(updateUserRequest.getUsername()) || 
            StringUtils.isEmpty(updateUserRequest.getPhone())) {
            throw new BizException("请填写完整信息!");
        }
        boolean result = userService.updateUser(getCurrentUserId(), 
                                              updateUserRequest.getUsername(),
                                              updateUserRequest.getPhone());
        if(result){
            return ResultVOUtil.success("更新成功!");
        }
        return ResultVOUtil.error("更新失败!");
    }

    @PostMapping("/updateLoginTime")
    public Result updateLoginTime(@RequestBody UpdateLoginTimeRequest updateLoginTimeRequest) {
        if (updateLoginTimeRequest.getLatitude() == null || 
            updateLoginTimeRequest.getLongitude() == null) {
            throw new BizException("信息不完整!");
        }
        boolean result = userService.updateUserLocation(
            getCurrentUserId(),
            updateLoginTimeRequest.getLatitude(),
            updateLoginTimeRequest.getLongitude(),
            LocalDateTime.now()
        );
        if (result) {
            return ResultVOUtil.success("更新成功!");
        }
        return ResultVOUtil.error("更新失败!");
    }

    @GetMapping("/findAllUsers")
    public Result getAllUsers(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize){
        if(pageNum < 1 || pageSize < 1){
            throw new BizException("页码或每页数量应大于0");
        }
        List<User> users = userService.getAllUsers(pageNum, pageSize);
        return ResultVOUtil.success(users != null ? users : new ArrayList<>());
    }

    /**
     * 获取所有用户数量
     * @return
     */
    @GetMapping("/countUsers")
    public Result countUsers(){
        int userNum = userService.getUserCount();
        return ResultVOUtil.success(userNum);
    }

    @DeleteMapping("/deleteUserById/{userId}")
    public Result deleteUserById(@PathVariable Integer userId) {
        if (userId == null) {
            throw new BizException("用户ID不能为空!");
        }
        boolean result = userService.deleteUserByUserId(userId);
        if (result) {
            return ResultVOUtil.success("删除用户成功!");
        }
        return ResultVOUtil.error("删除用户失败!");
    }
    
}  