package cn.edu.hzu.blog.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuTreeSelectVo {
    List<MenuTreeListVo> menus;
    List<Long> checkedKeys;
}
