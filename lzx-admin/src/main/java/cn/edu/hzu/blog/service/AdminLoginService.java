package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.entity.User;

import java.util.Map;

public interface AdminLoginService {

    Map<String, String> login(User user);

    void logout();
}
