package cn.edu.hzu.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {
    Long id;
    String status;
    String roleName;
    String roleKey;
    Integer roleSort;
    List<Long> menuIds;
    String remark;
}
