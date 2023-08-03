package cn.edu.hzu.blog.controller;

import cn.edu.hzu.blog.annotation.SystemLog;
import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.entity.User;
import cn.edu.hzu.blog.domain.vo.UserInfoVo;
import cn.edu.hzu.blog.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/userInfo")
    public ResponseResult userInfo(){
        UserInfoVo userInfo = userService.userInfo();
        return ResponseResult.okResult(userInfo);
    }

    @PutMapping("/userInfo")
    @SystemLog(businessName = "更新用户信息")
    public ResponseResult updateUserInfo(@RequestBody User user){
        userService.updateUserInfo(user);
        return ResponseResult.okResult();
    }

    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        userService.register(user);
        return ResponseResult.okResult();
    }

}
