package www.mys.com.oauth2server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import www.mys.com.oauth2user.pojo.Permission;
import www.mys.com.oauth2user.pojo.Role;
import www.mys.com.oauth2user.pojo.User;

// 用户配置信息
@Component("defaultUserConf")
@ConfigurationProperties(prefix = "user.default")
public class DefaultUserConf {

    private User user;
    private Role role;
    private Permission permission;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "DefaultUserConf{" +
                "user=" + user +
                ", permission=" + permission +
                ", role=" + role +
                '}';
    }
}
