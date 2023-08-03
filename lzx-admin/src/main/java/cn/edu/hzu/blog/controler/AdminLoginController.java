package cn.edu.hzu.blog.controler;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.entity.LoginUser;
import cn.edu.hzu.blog.domain.entity.Menu;
import cn.edu.hzu.blog.domain.entity.User;
import cn.edu.hzu.blog.domain.vo.AdminUserInfoVo;
import cn.edu.hzu.blog.domain.vo.RoutersVo;
import cn.edu.hzu.blog.domain.vo.UserInfoVo;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.service.AdminLoginService;
import cn.edu.hzu.blog.service.MenuService;
import cn.edu.hzu.blog.service.RoleService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import cn.edu.hzu.blog.utils.RedisCache;
import cn.edu.hzu.blog.utils.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AdminLoginController {

    @Resource
    private AdminLoginService loginService;

    @Resource
    private MenuService menuService;

    @Resource
    private RoleService roleService;

    @Resource
    private RedisCache redisCache;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if (!StringUtils.hasText(user.getUserName()))throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        return ResponseResult.okResult(loginService.login(user));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout(){
        loginService.logout();
        return ResponseResult.okResult();
    }

    @GetMapping("/getInfo")
    public ResponseResult getInfo(){
        // 获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        User user = loginUser.getUser();
        Long userId = user.getId();
        // 根据用户id查询权限信息
        List<String> perms = menuService.selectPermsByUserId(userId);
        // 根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(userId);
        // 封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms, roleKeyList, BeanCopyUtils.copyBean(user, UserInfoVo.class));
        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("/getRouters")
    public ResponseResult getRouters(){
        Long userId = SecurityUtils.getUserId();
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        return ResponseResult.okResult(new RoutersVo(menus));
    }

}
