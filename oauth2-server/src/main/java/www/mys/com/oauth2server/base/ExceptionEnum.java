package www.mys.com.oauth2server.base;

import www.mys.com.utils.ExceptionInfo;

public enum ExceptionEnum {
    DATA_ERROR(10000, "请求数据错误."),
    NO_DATA(10001, "没有这条数据."),
    DATA_ALREADY_EXIST(10002, "数据已存在."),
    SQL_ERROR(10003, "操作数据库错误."),
    NO_PERMISSION(10004, "没有权限."),
    NO_SUCH_USER(10005, "没有这个用户."),
    USER_ALREADY_EXIST(10006, "用户已存在."),
    CHECK_PHONE_ERROR(10007, "请输入合法的手机号."),
    NO_CLIENT_ID(10008, "没有这个clientId."),
    GET_SMS_CODE_FIRST(10009, "请先获取验证码."),
    SMS_CODE_ERROR(10010, "验证码错误."),
    SMS_CODE_TIME_OUT(10011, "超过有效期,请重新获取验证码.."),
    OUT_OF_MAX_TIMES(10012, "超过最大校验次数."),
    REGISTER_PHONE_FIRST(10013, "请先使用手机号注册."),
    NO_UNBIND_USER(10014, "未绑定用户."),
    LOGIN_SERVER_ERROR(10015, "第三方登录注册服务不可用."),
    LOGIN_ERROR(-3, "登录错误."),
    USER_NAME_PASS_ERROR(-3, "用户名或密码错误."),
    REGISTER_ERROR(-3, "注册错误."),
    UNKNOW_ERROR(-1, "未知错误.");

    private String msg;
    private Integer code;

    public Integer getCode() {
        return code;
    }

    ExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ExceptionInfo getExceptionInfo() {
        return new ExceptionInfo() {
            @Override
            public String getMsg() {
                return ExceptionEnum.this.getMsg();
            }

            @Override
            public Integer getCode() {
                return ExceptionEnum.this.getCode();
            }
        };
    }

}
