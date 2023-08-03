package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.domain.entity.UserRole;
import cn.edu.hzu.blog.mapper.UserRoleMapper;
import cn.edu.hzu.blog.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService{
}
