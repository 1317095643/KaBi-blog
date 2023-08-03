package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service("ps")
public class PermissionService {

    public boolean hasPermission(String permission){
        if (SecurityUtils.isAdmin())return true;
        return SecurityUtils.getLoginUser().getPermission().contains(permission);
    }

}
