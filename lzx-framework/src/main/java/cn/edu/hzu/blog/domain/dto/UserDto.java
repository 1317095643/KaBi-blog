package cn.edu.hzu.blog.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Long id;
    String userName;
    String nickName;
    String password;
    String phonenumber;
    String email;
    String sex;
    String status;
    List<Long> roleIds;
}
