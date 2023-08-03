package cn.edu.hzu.blog.exception;

import cn.edu.hzu.blog.enums.AppHttpCodeEnum;

public class SystemException extends RuntimeException{
    private int code;
    private String msg;
    public int getCode(){return code;}

    public String getMsg(){return msg;}

    public SystemException(AppHttpCodeEnum httpCodeEnum){
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
        this.msg = httpCodeEnum.getMsg();
    }

    public SystemException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
