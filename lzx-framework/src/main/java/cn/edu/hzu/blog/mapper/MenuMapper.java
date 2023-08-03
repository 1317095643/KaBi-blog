package cn.edu.hzu.blog.mapper;

import cn.edu.hzu.blog.domain.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
//    @Select("""
//            SELECT
//                DISTINCT m.perms
//            FROM
//                user_role ur
//                LEFT JOIN role_menu rm on ur.role_id = rm.role_id
//                LEFT JOIN menu m on rm.menu_id = m.id
//            WHERE
//                ur.user_id = #{userId} AND
//                m.status = '0' AND
//                m.menu_type IN('C', 'F') AND
//                m.del_flag = '0'
//            """)
    List<String> selectPermsByUserId(Long userId);



    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);
}
