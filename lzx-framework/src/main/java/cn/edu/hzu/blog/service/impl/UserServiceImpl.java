package cn.edu.hzu.blog.service.impl;

import cn.edu.hzu.blog.domain.dto.UserDto;
import cn.edu.hzu.blog.domain.dto.UserStatusDto;
import cn.edu.hzu.blog.domain.entity.User;
import cn.edu.hzu.blog.domain.entity.UserRole;
import cn.edu.hzu.blog.domain.vo.AllRoleListVo;
import cn.edu.hzu.blog.domain.vo.PageVo;
import cn.edu.hzu.blog.domain.vo.UserInfoVo;
import cn.edu.hzu.blog.domain.vo.UserVo;
import cn.edu.hzu.blog.enums.AppHttpCodeEnum;
import cn.edu.hzu.blog.exception.SystemException;
import cn.edu.hzu.blog.mapper.UserMapper;
import cn.edu.hzu.blog.service.RoleService;
import cn.edu.hzu.blog.service.UserRoleService;
import cn.edu.hzu.blog.service.UserService;
import cn.edu.hzu.blog.utils.BeanCopyUtils;
import cn.edu.hzu.blog.utils.SecurityUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private RoleService roleService;

    @Override
    public UserInfoVo userInfo() {
        Long userId = SecurityUtils.getUserId();
        User user = getById(userId);
        return BeanCopyUtils.copyBean(user, UserInfoVo.class);
    }

    @Override
    public void updateUserInfo(User user) {
        if (!Objects.equals(SecurityUtils.getUserId(), user.getId())) {
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(User::getId, user.getId());
        queryWrapper.eq(User::getNickName, user.getNickName());
        if (count(queryWrapper) > 0)throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        updateById(user);
    }

    @Resource
    private PasswordEncoder passwordEncoder;
    @Override
    public void register(User user) {
        registerFieldNotNull(user.getUserName(), user.getPassword(), user.getNickName(), user.getEmail());
        if (userNameExist(user.getUserName()))throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        if (nickNameExist(user.getNickName(), null))throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        if (emailExist(user.getEmail(), null))throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        save(user);
    }

    @Override
    public PageVo queryUserListPage(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status), User::getStatus, status);
        queryWrapper.like(StringUtils.hasText(phonenumber), User::getPhonenumber, phonenumber);
        queryWrapper.like(StringUtils.hasText(userName), User::getUserName, userName);
        Page<User> page = new Page<>(pageNum, pageSize);
        page(page, queryWrapper);
        List<UserVo> result = BeanCopyUtils.copyBean(page.getRecords(), UserVo.class);
        return new PageVo(result, page.getTotal());
    }

    @Override
    @Transactional
    public void addUser(UserDto user) {
        if (!StringUtils.hasText(user.getUserName()))throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        if (!StringUtils.hasText(user.getNickName()))throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        if (!StringUtils.hasText(user.getPassword()))throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        if (userNameExist(user.getUserName()))throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        if (nickNameExist(user.getNickName(), null))throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        if (StringUtils.hasText(user.getPhonenumber()) && phoneExist(user.getPhonenumber(), null))throw new SystemException(AppHttpCodeEnum.PHONE_EXIST);
        if (StringUtils.hasText(user.getEmail()) && emailExist(user.getEmail(), null))throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        String encode = passwordEncoder.encode(user.getPassword());
        user.setPassword(encode);
        // 添加用户信息
        User saveUser = BeanCopyUtils.copyBean(user, User.class);
        save(saveUser);
        // 给用户绑定角色
        Long userId = saveUser.getId();
        List<UserRole> list = user.getRoleIds().stream().map(obj -> new UserRole(userId, obj)).collect(Collectors.toList());
        userRoleService.saveBatch(list);
    }

    @Override
    public void deleteUserById(Long id) {
        if (SecurityUtils.getUserId().equals(id)) throw new SystemException(AppHttpCodeEnum.DELETE_ONESELF);
        removeById(id);
    }

    @Override
    public Map<String, Object> queryUserAndRole(Long id) {
        Map<String, Object> map = new HashMap<>();

        // 查询用户所关联角色id列表
        LambdaQueryWrapper<UserRole> userRoleWrapper = new LambdaQueryWrapper<>();
        userRoleWrapper.eq(UserRole::getUserId, id);
        List<UserRole> userRoles = userRoleService.list(userRoleWrapper);
        map.put("roleIds", userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList()));

        // 查询所有角色的列表
        List<AllRoleListVo> roleList = roleService.listAllRole();
        map.put("roles", roleList);

        // 查询用户信息
        User user = getById(id);
        UserInfoVo userInfo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        userInfo.setAvatar(null);
        map.put("user", userInfo);

        // 返回数据
        return map;
    }

    @Override
    @Transactional
    public void updateUserAndRole(UserDto user) {
        Long userId = user.getId();
        user.setUserName(null);
        if (!StringUtils.hasText(user.getNickName()))throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        user.setPassword(null);
        if (nickNameExist(user.getNickName(), userId))throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        if (StringUtils.hasText(user.getPhonenumber()) && phoneExist(user.getPhonenumber(), userId))throw new SystemException(AppHttpCodeEnum.PHONE_EXIST);
        if (StringUtils.hasText(user.getEmail()) && emailExist(user.getEmail(), userId))throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);

        // 更新角色信息
        User updateUser = BeanCopyUtils.copyBean(user, User.class);
        updateById(updateUser);


        LambdaQueryWrapper<UserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRole::getUserId, userId);
        userRoleService.remove(wrapper);
        List<UserRole> list = user.getRoleIds().stream().map(obj -> new UserRole(userId, obj)).collect(Collectors.toList());
        userRoleService.saveBatch(list);
    }

    @Override
    public void changeStatus(UserStatusDto user) {
        if (SecurityUtils.getUserId().equals(user.getUserId())) throw new SystemException(AppHttpCodeEnum.UPDATE_STATUS_ONESELF);
        User updateUser = new User();
        updateUser.setStatus(user.getStatus());
        updateUser.setId(user.getUserId());
        updateById(updateUser);
    }

    private void registerFieldNotNull(String username, String password, String nickname, String email){
        if (!StringUtils.hasText(username))throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        if (!StringUtils.hasText(password))throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        if (!StringUtils.hasText(nickname))throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        if (!StringUtils.hasText(email))throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return count(queryWrapper) > 0;
    }

    private boolean nickNameExist(String nickName, Long id){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        queryWrapper.ne(Objects.nonNull(id), User::getId, id);
        return count(queryWrapper) > 0;
    }

    private boolean emailExist(String email, Long id){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        queryWrapper.ne(Objects.nonNull(id), User::getId, id);
        return count(queryWrapper) > 0;
    }

    private boolean phoneExist(String phone, Long id){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber, phone);
        queryWrapper.ne(Objects.nonNull(id), User::getId, id);
        return count(queryWrapper) > 0;
    }
}
