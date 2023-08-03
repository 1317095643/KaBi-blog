package cn.edu.hzu.blog.controler;

import cn.edu.hzu.blog.domain.ResponseResult;
import cn.edu.hzu.blog.domain.entity.Menu;
import cn.edu.hzu.blog.domain.vo.MenuListVo;
import cn.edu.hzu.blog.domain.vo.MenuTreeListVo;
import cn.edu.hzu.blog.domain.vo.MenuTreeSelectVo;
import cn.edu.hzu.blog.service.MenuService;
import cn.edu.hzu.blog.service.RoleMenuService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @Resource
    private RoleMenuService roleMenuService;

    @GetMapping("/list")
    @PreAuthorize("@ps.hasPermission('system:menu:query')")
    public ResponseResult list(String status, String menuName){
        List<MenuListVo> result = menuService.getList(status, menuName);
        return ResponseResult.okResult(result);
    }

    @PostMapping
    @PreAuthorize("@ps.hasPermission('system:menu:add')")
    public ResponseResult add(@RequestBody Menu menu){
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:menu:query')")
    public ResponseResult query(@PathVariable("id")Long id){
        return ResponseResult.okResult(menuService.getMenuVoById(id));
    }

    @PutMapping
    @PreAuthorize("@ps.hasPermission('system:menu:edit')")
    public ResponseResult update(@RequestBody Menu menu){
        menuService.updateMenuById(menu);
        return ResponseResult.okResult();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@ps.hasPermission('system:menu:remove')")
    public ResponseResult delete(@PathVariable("id")Long id){
        menuService.deleteMenuById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/treeselect")
    @PreAuthorize("@ps.hasPermission('system:menu:query')")
    public ResponseResult treeSelect(){
        List<MenuTreeListVo> result = menuService.treeSelect();
        return ResponseResult.okResult(result);
    }

    @GetMapping("/roleMenuTreeselect/{id}")
    @PreAuthorize("@ps.hasPermission('system:menu:query')")
    public ResponseResult roleMenuTreeSelect(@PathVariable("id")Long id){
        MenuTreeSelectVo result = new MenuTreeSelectVo();
        result.setMenus(menuService.treeSelect());
        result.setCheckedKeys(roleMenuService.getMenusByRoleId(id));
        return ResponseResult.okResult(result);
    }

}
