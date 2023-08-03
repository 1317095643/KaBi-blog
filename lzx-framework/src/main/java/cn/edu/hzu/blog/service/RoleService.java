package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.dto.RoleDto;
import cn.edu.hzu.blog.domain.dto.RoleStatusDto;
import cn.edu.hzu.blog.domain.entity.Role;
import cn.edu.hzu.blog.domain.vo.AllRoleListVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long userId);

    PageVo queryRoleList(Integer pageNum, Integer pageSize, String roleName, String status);

    void changeStatus(RoleStatusDto roleStatusDto);

    void addRole(RoleDto roleDto);

    void updateRoleById(RoleDto roleDto);

    List<AllRoleListVo> listAllRole();

}
