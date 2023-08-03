package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.dto.UserDto;
import cn.edu.hzu.blog.domain.dto.UserStatusDto;
import cn.edu.hzu.blog.domain.entity.User;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.domain.vo.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface UserService extends IService<User> {
    UserInfoVo userInfo();

    void updateUserInfo(User user);

    void register(User user);

    PageVo queryUserListPage(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    void addUser(UserDto userDto);

    void deleteUserById(Long id);

    Map<String, Object> queryUserAndRole(Long id);

    void updateUserAndRole(UserDto userDto);

    void changeStatus(UserStatusDto user);
}
