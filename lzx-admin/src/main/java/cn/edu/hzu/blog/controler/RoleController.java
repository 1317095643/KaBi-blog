package cn.edu.hzu.blog.controler;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.dto.RoleDto;
import cn.edu.hzu.blog.domain.dto.RoleStatusDto;
import cn.edu.hzu.blog.domain.entity.Role;
import cn.edu.hzu.blog.domain.vo.AllRoleListVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.service.RoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {

    @Resource
    private RoleService roleService;

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:role:query')")
    public ResponseResult list(Integer pageNum, Integer pageSize, String roleName, String status){
        PageVo result = roleService.queryRoleList(pageNum, pageSize, roleName, status);
        return ResponseResult.okResult(result);
    }

    @PutMapping("/changeStatus")
    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    public ResponseResult changeStatus(@RequestBody RoleStatusDto roleStatusDto){
        roleService.changeStatus(roleStatusDto);
        return ResponseResult.okResult();
    }

    @PostMapping
    @PreAuthorize("@ps.hasPermission('system:role:add')")
    public ResponseResult add(@RequestBody RoleDto roleDto){
        roleService.addRole(roleDto);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:role:query')")
    public ResponseResult query(@PathVariable("id") Long id){
        Role role = roleService.getById(id);
        role.setCreateBy(null);
        role.setCreateTime(null);
        role.setUpdateBy(null);
        role.setUpdateTime(null);
        return ResponseResult.okResult(role);
    }

    @PutMapping
    @PreAuthorize("@ps.hasPermission('system:role:edit')")
    public ResponseResult update(@RequestBody RoleDto roleDto){
        roleService.updateRoleById(roleDto);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:role:remove')")
    public ResponseResult delete(@PathVariable("id")Long id){
        roleService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/listAllRole")
    @PreAuthorize("@ps.hasPermission('system:role:query')")
    public ResponseResult listAllRole(){
        List<AllRoleListVo> data = roleService.listAllRole();
        return ResponseResult.okResult(data);
    }

}
