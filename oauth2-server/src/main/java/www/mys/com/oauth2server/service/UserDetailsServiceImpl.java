package www.mys.com.oauth2server.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import www.mys.com.common.LogUtils;
import www.mys.com.oauth2server.base.MyUserDetails;
import www.mys.com.oauth2user.mapper.UserMapper;
import www.mys.com.oauth2user.pojo.Role;
import www.mys.com.oauth2user.pojo.User;

import javax.annotation.Resource;

@Service("myUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource(name = "userMapper")
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        LogUtils.log("loadUserByUsername username=" + username);
        long start = System.currentTimeMillis();
        User user = userMapper.getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        String[] roleStrs = new String[user.getRoles().size()];
        int i = 0;
        for (Role role : user.getRoles()) {
            roleStrs[i++] = role.getRoleName();
        }
        MyUserDetails userDetails = new MyUserDetails(user.getPass(), user.getUserName(), user.getRealName()
                , user.isAccountNonExpired(), user.isAccountNonLocked()
                , user.isCredentialsNonExpired(), user.isEnabled()
                , AuthorityUtils.createAuthorityList(roleStrs));
        LogUtils.log("loadUserByUsername time=" + (System.currentTimeMillis() - start));
        return userDetails;
    }

}