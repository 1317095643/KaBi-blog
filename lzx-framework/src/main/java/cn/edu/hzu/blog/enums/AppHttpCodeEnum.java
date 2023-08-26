package cn.edu.hzu.blog.enums;

public enum AppHttpCodeEnum {
    SUCCESS(200, "操作成功"),
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATOR_AUTH(403, "无权限操作"),
    SYSTEM_ERROR(500, "出现错误"),
    USERNAME_EXIST(501, "用户名已存在"),
    PHONE_EXIST(502, "手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必须填写用户名"),
    LOGIN_ERROR(505, "用户名或密码错误"),
    CONTENT_NOT_NULL(506, "内容不能为空"),
    FILE_TYPE_ERROR(507, "文件类型错误"),
    USERNAME_NOT_NULL(508, "用户名不能为空"),
    NICKNAME_NOT_NULL(509, "昵称不能为空"),
    PASSWORD_NOT_NULL(510, "密码不能为空"),
    EMAIL_NOT_NULL(511, "邮箱不能为空"),
    NICKNAME_EXIST(512, "昵称已存在"),
    USER_BLOCK(513, "用户已停用"),
    TAG_EXIST(514, "标签已存在"),
    DELETE_ONESELF(515, "不能删除自己"),
    ROLE_NAME_EXIST(516, "角色名称已存在"),
    ROLE_KEY_EXIST(517, "权限字符已存在"),
    ROLE_SHOT_NOT_NULL(518, "角色顺序不能为空"),
    ROLE_NAME_NOT_NULL(519, "角色名称不能为空"),
    ROLE_KEY_NOT_NULL(520, "权限字符不能为空"),
    UPDATE_STATUS_ONESELF(521, "不能更新自己的状态"),
    CATEGORY_NAME_EXIST(522, "分类名称已存在"),
    ARTICLE_ID_NOT_EXIST(523, "文章不存在");
    int code;
    String msg;
    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode(){
        return code;
    }
    public String getMsg(){
        return msg;
    }
}

