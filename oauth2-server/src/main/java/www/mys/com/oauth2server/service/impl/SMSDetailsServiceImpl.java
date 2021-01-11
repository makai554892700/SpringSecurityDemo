package www.mys.com.oauth2server.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import www.mys.com.oauth2server.service.UserService;

import javax.annotation.Resource;
import java.util.logging.Logger;

@Service("smsDetailsService")
public class SMSDetailsServiceImpl implements UserDetailsService {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Resource(name = "userService")
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return userService.loadUser(username, "phone");
    }
}
