package cn.edu.hzu.blog.service;

import cn.edu.hzu.blog.domain.entity.Menu;
import cn.edu.hzu.blog.domain.vo.MenuListVo;
import cn.edu.hzu.blog.domain.vo.MenuTreeListVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long userId);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<MenuListVo> getList(String status, String menuName);

    MenuListVo getMenuVoById(Long id);

    void updateMenuById(Menu menu);

    void deleteMenuById(Long id);

    List<MenuTreeListVo> treeSelect();

}
