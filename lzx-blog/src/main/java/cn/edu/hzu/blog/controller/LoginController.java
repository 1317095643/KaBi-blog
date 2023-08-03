package cn.edu.hzu.blog.controller;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.entity.LoginUser;
import cn.edu.hzu.blog.domain.entity.User;
import cn.edu.hzu.blog.domain.vo.LoginVo;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.service.LoginService;
import cn.edu.hzu.blog.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;

@RestController
public class LoginController {

    @Resource
    LoginService loginService;

    @Resource
    RedisCache redisCache;

    @PostMapping("/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        LoginVo loginVo = loginService.login(user);
        return ResponseResult.okResult(loginVo);
    }

    @PostMapping("/logout")
    public ResponseResult logout(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        redisCache.deleteObject("login:" + userId);
        return ResponseResult.okResult();
    }

}
