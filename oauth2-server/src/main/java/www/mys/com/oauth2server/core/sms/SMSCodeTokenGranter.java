package www.mys.com.oauth2server.core.sms;

import com.thoughtworks.xstream.core.BaseException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import www.mys.com.oauth2server.base.MyAbstractTokenGranter;
import www.mys.com.oauth2server.service.UserService;

import java.util.Map;

public class SMSCodeTokenGranter extends MyAbstractTokenGranter {

    private static final String GRANT_TYPE = "sms_code";
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    public SMSCodeTokenGranter(UserService userService, UserDetailsService userDetailsService
            , AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService
            , OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, GRANT_TYPE);
        this.userService = userService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected UserDetails getCustomUser(Map<String, String> parameters) {
        String phone = parameters.get("phone");
        String smsCode = parameters.get("smsCode");
        try {
            userService.checkSMSCode(phone, smsCode);
        } catch (BaseException e) {
            throw new InvalidGrantException(e.getMessage());
        }
        return userDetailsService.loadUserByUsername(phone);
    }

}
