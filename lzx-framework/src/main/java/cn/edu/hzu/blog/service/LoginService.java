package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.entity.User;
import cn.edu.hzu.blog.domain.vo.LoginVo;

public interface LoginService {
    LoginVo login(User user);
}
