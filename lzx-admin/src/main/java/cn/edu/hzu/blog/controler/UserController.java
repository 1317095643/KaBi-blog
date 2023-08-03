package cn.edu.hzu.blog.controler;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.dto.UserDto;
import cn.edu.hzu.blog.domain.dto.UserStatusDto;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:user:query')")
    public ResponseResult list(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status){
        PageVo result = userService.queryUserListPage(pageNum, pageSize, userName, phonenumber, status);
        return ResponseResult.okResult(result);
    }

    @PostMapping
    @PreAuthorize("@ps.hasPermission('system:user:add')")
    public ResponseResult add(@RequestBody UserDto userDto){
        userService.addUser(userDto);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:user:remove')")
    public ResponseResult delete(@PathVariable("id") Long id){
        userService.deleteUserById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:user:query')")
    public ResponseResult query(@PathVariable("id") Long id){
        Map<String, Object> result = userService.queryUserAndRole(id);
        return ResponseResult.okResult(result);
    }

    @PutMapping
    @PreAuthorize("@ps.hasPermission('system:user:edit')")
    public ResponseResult update(@RequestBody UserDto userDto){
        userService.updateUserAndRole(userDto);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeStatus")
    @PreAuthorize("@ps.hasPermission('system:user:edit')")
    public ResponseResult changeStatus(@RequestBody UserStatusDto user){
        userService.changeStatus(user);
        return ResponseResult.okResult();
    }
}
