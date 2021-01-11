package www.mys.com.oauth2server.vo.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RequestRegister implements Serializable {

    public static class RegisterType {
        public static final Integer USER_NAME_PASS = 0;
        public static final Integer PHONE = 1;
        public static final Integer WECHAT = 2;
        public static final Integer EVERY_BIM = 3;
        public static final int I_USER_NAME_PASS = 0;
        public static final int I_PHONE = 1;
        public static final int I_WECHAT = 2;
    }

    @NotEmpty(message = "缺少必传字段")
    private String checkKey;            //附带用户key(phone/openId/用户名)
    @NotEmpty(message = "缺少必传字段")
    private String checkValue;          //附带校验码(手机验证码/密码)
    @NotNull(message = "注册类型必传")
    private Integer userType;           //注册类型 int/ 0 用户名密码/ 1 手机号验证码/2 微信token...

    public String getCheckKey() {
        return checkKey;
    }

    public void setCheckKey(String checkKey) {
        this.checkKey = checkKey;
    }

    public String getCheckValue() {
        return checkValue;
    }

    public void setCheckValue(String checkValue) {
        this.checkValue = checkValue;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "RequestRegister{" +
                ", checkKey='" + checkKey + '\'' +
                ", checkValue='" + checkValue + '\'' +
                ", userType=" + userType +
                '}';
    }
}
