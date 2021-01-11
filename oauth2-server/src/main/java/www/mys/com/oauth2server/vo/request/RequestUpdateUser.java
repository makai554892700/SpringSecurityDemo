package www.mys.com.oauth2server.vo.request;

public class RequestUpdateUser {

    private String userName;
    private String passWord;
    private String bindUser;//更换绑定的用户key(例如bindUserType为0时这里为手机号)
    private String bindCheck;//更换绑定的校验码(绑定手机时为验证码)
    private Integer bindUserType;//绑定用户类型 0 phone/ 1 userName/ 2 微信token

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getBindUser() {
        return bindUser;
    }

    public void setBindUser(String bindUser) {
        this.bindUser = bindUser;
    }

    public String getBindCheck() {
        return bindCheck;
    }

    public void setBindCheck(String bindCheck) {
        this.bindCheck = bindCheck;
    }

    public Integer getBindUserType() {
        return bindUserType;
    }

    public void setBindUserType(Integer bindUserType) {
        this.bindUserType = bindUserType;
    }

    @Override
    public String toString() {
        return "RequestUpdateUser{" +
                "userName='" + userName + '\'' +
                ", passWord='" + passWord + '\'' +
                ", bindUser='" + bindUser + '\'' +
                ", bindCheck='" + bindCheck + '\'' +
                ", bindUserType=" + bindUserType +
                '}';
    }
}
