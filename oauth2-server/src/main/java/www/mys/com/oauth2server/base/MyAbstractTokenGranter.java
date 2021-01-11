package www.mys.com.oauth2server.base;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Map;

public abstract class MyAbstractTokenGranter extends AbstractTokenGranter {

    private final OAuth2RequestFactory requestFactory;

    protected MyAbstractTokenGranter(AuthorizationServerTokenServices tokenServices
            , ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory
            , String grantType) {
        super(tokenServices, clientDetailsService, requestFactory, grantType);
        this.requestFactory = requestFactory;
    }

    @Override
    protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
        UserDetails userDetails = getCustomUser(tokenRequest.getRequestParameters());
        if (userDetails == null) {
            throw new InvalidGrantException("无法获取用户信息");
        }
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(
                userDetails, null, userDetails.getAuthorities()
        );
        authenticationToken.setDetails(userDetails);
        return new OAuth2Authentication(requestFactory.createOAuth2Request(client, tokenRequest)
                , authenticationToken);
    }

    protected abstract UserDetails getCustomUser(Map<String, String> parameters);

}
