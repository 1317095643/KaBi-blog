package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.entity.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleMenuService extends IService<RoleMenu> {
    List<Long> getMenusByRoleId(Long id);
}
