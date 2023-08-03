package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.domain.entity.LoginUser;
import cn.edu.hzu.blog.domain.entity.User;
import cn.edu.hzu.blog.domain.vo.LoginVo;
import cn.edu.hzu.blog.domain.vo.UserInfoVo;
import cn.edu.hzu.blog.service.LoginService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import cn.edu.hzu.blog.utils.JwtUtil;
import cn.edu.hzu.blog.utils.RedisCache;
import javax.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    RedisCache redisCache;

    @Override
    public LoginVo login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 判断是否认证成功
        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        // 获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        // 把用户信息存入redis
        redisCache.setCacheObject("login:" + userId, loginUser);
        // 把token和userinfo封装返回
        return new LoginVo(jwt, BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class));
    }
}
