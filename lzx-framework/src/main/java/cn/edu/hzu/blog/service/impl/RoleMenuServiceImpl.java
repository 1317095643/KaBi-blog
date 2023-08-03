package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.domain.entity.RoleMenu;
import cn.edu.hzu.blog.mapper.RoleMenuMapper;
import cn.edu.hzu.blog.service.RoleMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
    @Override
    public List<Long> getMenusByRoleId(Long id) {
        return baseMapper.getMenusByRoleId(id);
    }
}
