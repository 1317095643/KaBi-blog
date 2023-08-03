package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.domain.entity.LoginUser;
import cn.edu.hzu.blog.domain.entity.User;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.service.AdminLoginService;
import cn.edu.hzu.blog.utils.JwtUtil;
import cn.edu.hzu.blog.utils.RedisCache;
import cn.edu.hzu.blog.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    @Override
    public Map<String, String> login(User user) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate))throw new SystemException(AppHttpCodeEnum.LOGIN_ERROR);
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        redisCache.setCacheObject("adminLogin:" + userId, loginUser);
        Map<String, String> map = new HashMap<>();
        map.put("token", jwt);
        return map;
    }

    @Override
    public void logout() {
        String userId = SecurityUtils.getUserId().toString();
        redisCache.deleteObject("adminLogin:" + userId);
    }
}
