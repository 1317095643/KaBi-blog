package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.constants.SystemConstants;
import cn.edu.hzu.blog.domain.entity.Menu;
import cn.edu.hzu.blog.domain.vo.MenuListVo;
import cn.edu.hzu.blog.domain.vo.MenuTreeListVo;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.mapper.MenuMapper;
import cn.edu.hzu.blog.service.MenuService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import cn.edu.hzu.blog.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public List<String> selectPermsByUserId(Long userId) {
        if (SecurityUtils.isAdmin()){
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            queryWrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> list = list(queryWrapper);
            return list.stream().map(Menu::getPerms).collect(Collectors.toList());
        }
        return getBaseMapper().selectPermsByUserId(userId);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper mapper = getBaseMapper();
        List<Menu> menus;
        if (SecurityUtils.isAdmin()){
            menus = mapper.selectAllRouterMenu();
        }else{
            menus = mapper.selectRouterMenuTreeByUserId(userId);
        }
        return builderMenuTree(menus, 0L);
    }

    @Override
    public List<MenuListVo> getList(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(status), Menu::getStatus, status);
        queryWrapper.like(StringUtils.hasText(menuName), Menu::getMenuName, menuName);
        queryWrapper.orderByAsc(Menu::getParentId, Menu::getOrderNum);
        List<Menu> list = list(queryWrapper);
        return BeanCopyUtils.copyBean(list, MenuListVo.class);
    }

    @Override
    public MenuListVo getMenuVoById(Long id) {
        Menu menu  = getById(id);
        MenuListVo menuVo = BeanCopyUtils.copyBean(menu, MenuListVo.class);
        menuVo.setComponent(null);
        menuVo.setIsFrame(null);
        return menuVo;
    }

    @Override
    public void updateMenuById(Menu menu) {
        if (menu.getParentId().equals(menu.getId())){
            throw new SystemException(500, "修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        updateById(menu);
    }

    @Override
    public void deleteMenuById(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId, id);
        long count = count(queryWrapper);
        if (count != 0)throw new SystemException(500, "存在子菜单不允许删除");
        removeById(id);
    }

    @Override
    public List<MenuTreeListVo> treeSelect() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
        wrapper.select(Menu::getId, Menu::getParentId, Menu::getMenuName);
        List<Menu> menuList = list(wrapper);
        List<MenuTreeListVo> menuTreeListVos = menuList.stream()
                .map(obj -> new MenuTreeListVo(null, obj.getId(), obj.getMenuName(), obj.getParentId()))
                .collect(Collectors.toList());

        return menuTreeListVos.stream().filter(obj -> obj.getParentId().equals(0L))
                .map(menu -> menu.setChildren(getChildren(menu, menuTreeListVos)))
                .collect(Collectors.toList());
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        return menus.stream().filter(obj -> obj.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
    }

    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        return menus.stream().filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
    }

    private List<MenuTreeListVo> getChildren(MenuTreeListVo menu, List<MenuTreeListVo> menus) {
        return menus.stream().filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
    }
}
