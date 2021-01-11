package www.mys.com.oauth2server.core.sms;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public class MobileUser implements AuthenticatedPrincipal, Serializable {

    private String userName;
    private String phone;
    private Collection<? extends GrantedAuthority> authorities;

    public MobileUser() {
    }

    public MobileUser(String userName, String phone) {
        this.userName = userName;
        this.phone = phone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getName() {
        return userName;
    }

    @Override
    public String toString() {
        return "MobileUser{" +
                "userName='" + userName + '\'' +
                ", phone='" + phone + '\'' +
                ", authorities=" + authorities +
                '}';
    }
}
