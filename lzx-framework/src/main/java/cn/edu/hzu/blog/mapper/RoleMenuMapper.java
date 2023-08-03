package cn.edu.hzu.blog.mapper;

import cn.edu.hzu.blog.domain.entity.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface RoleMenuMapper extends BaseMapper<RoleMenu> {


    List<Long> getMenusByRoleId(Long roleId);

}
