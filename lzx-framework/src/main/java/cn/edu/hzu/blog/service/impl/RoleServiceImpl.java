package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.constants.SystemConstants;
import cn.edu.hzu.blog.domain.dto.RoleDto;
import cn.edu.hzu.blog.domain.dto.RoleStatusDto;
import cn.edu.hzu.blog.domain.entity.Role;
import cn.edu.hzu.blog.domain.entity.RoleMenu;
import cn.edu.hzu.blog.domain.vo.AllRoleListVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.domain.vo.RoleVo;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.mapper.RoleMapper;
import cn.edu.hzu.blog.service.RoleMenuService;
import cn.edu.hzu.blog.service.RoleService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMenuService roleMenuService;

    @Override
    public List<String> selectRoleKeyByUserId(Long userId) {
        if (userId == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        return getBaseMapper().selectRoleKeyByUserId(userId);
    }

    @Override
    public PageVo queryRoleList(Integer pageNum, Integer pageSize, String roleName, String status) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status), Role::getStatus, status);
        queryWrapper.like(StringUtils.hasText(roleName), Role::getRoleName, roleName);
        queryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        return new PageVo(BeanCopyUtils.copyBean(page.getRecords(), RoleVo.class), page.getTotal());
    }

    @Override
    public void changeStatus(RoleStatusDto roleStatusDto) {
        Role role = new Role();
        role.setStatus(roleStatusDto.getStatus());
        role.setId(roleStatusDto.getRoleId());
        updateById(role);
    }

    @Override
    @Transactional
    public void addRole(RoleDto roleDto) {
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        if (!StringUtils.hasText(role.getRoleName()))throw new SystemException(AppHttpCodeEnum.ROLE_NAME_NOT_NULL);
        if (!StringUtils.hasText(role.getRoleKey()))throw new SystemException(AppHttpCodeEnum.ROLE_KEY_NOT_NULL);
        if (Objects.isNull(role.getRoleSort()))throw new SystemException(AppHttpCodeEnum.ROLE_SHOT_NOT_NULL);
        if (roleNameExist(role.getRoleName(), null))throw new SystemException(AppHttpCodeEnum.ROLE_NAME_EXIST);
        if (roleKeyExist(role.getRoleKey(), null))throw new SystemException(AppHttpCodeEnum.ROLE_KEY_EXIST);
        save(role);
        List<RoleMenu> roleMenus = roleDto.getMenuIds().stream().map(obj -> new RoleMenu(role.getId(), obj))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenus);
    }

    @Override
    @Transactional
    public void updateRoleById(RoleDto roleDto) {
        Long roleId = roleDto.getId();
        Role role = BeanCopyUtils.copyBean(roleDto, Role.class);
        if (!StringUtils.hasText(role.getRoleName()))throw new SystemException(AppHttpCodeEnum.ROLE_NAME_NOT_NULL);
        if (!StringUtils.hasText(role.getRoleKey()))throw new SystemException(AppHttpCodeEnum.ROLE_KEY_NOT_NULL);
        if (Objects.isNull(role.getRoleSort()))throw new SystemException(AppHttpCodeEnum.ROLE_SHOT_NOT_NULL);
        if (roleNameExist(role.getRoleName(), roleId))throw new SystemException(AppHttpCodeEnum.ROLE_NAME_EXIST);
        if (roleKeyExist(role.getRoleKey(), roleId))throw new SystemException(AppHttpCodeEnum.ROLE_KEY_EXIST);
        updateById(role);
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId, roleId);
        roleMenuService.remove(wrapper);
        List<Long> updateData = roleDto.getMenuIds();
        List<RoleMenu> list = updateData.stream().map(obj -> new RoleMenu(roleId, obj)).collect(Collectors.toList());
        roleMenuService.saveBatch(list);
    }

    @Override
    public List<AllRoleListVo> listAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, SystemConstants.NORMAL);
        return BeanCopyUtils.copyBean(list(wrapper), AllRoleListVo.class);
    }

    private boolean roleNameExist(String roleName, Long id){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleName, roleName);
        wrapper.ne(Objects.nonNull(id), Role::getId, id);
        return count(wrapper) > 0;
    }

    private boolean roleKeyExist(String roleKey, Long id){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getRoleKey, roleKey);
        wrapper.ne(Objects.nonNull(id), Role::getId, id);
        return count(wrapper) > 0;
    }
}
