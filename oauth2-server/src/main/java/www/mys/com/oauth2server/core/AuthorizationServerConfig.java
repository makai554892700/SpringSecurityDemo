package www.mys.com.oauth2server.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import www.mys.com.oauth2server.core.sms.SMSCodeTokenGranter;
import www.mys.com.oauth2server.service.UserService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Resource(name = "myAuthenticationManager")
    private AuthenticationManager authenticationManager;
    @Resource(name = "myTokenStore")
    private TokenStore tokenStore;
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "myClientDetailsService")
    private ClientDetailsService clientDetailsService;
    @Resource(name = "myUserDetailsService")
    private UserDetailsService userDetailsService;
    @Resource(name = "smsDetailsService")
    private UserDetailsService smsDetailsService;
    @Resource(name = "myJwtAccessTokenConverter")
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Resource(name = "myWebResponseExceptionTranslator")
    private DefaultWebResponseExceptionTranslator webResponseExceptionTranslator;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .tokenStore(tokenStore)
                .accessTokenConverter(jwtAccessTokenConverter)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .tokenServices(defaultTokenServices())
                .exceptionTranslator(webResponseExceptionTranslator)
        ;
        //自定义登录方式
        endpoints.tokenGranter(new CompositeTokenGranter(getTokenGranters(
                endpoints.getAuthorizationCodeServices(), endpoints.getTokenStore()
                , endpoints.getTokenServices(), endpoints.getClientDetailsService()
                , endpoints.getOAuth2RequestFactory()
        )));
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .allowFormAuthenticationForClients()
//                .addTokenEndpointAuthenticationFilter()
        ;
    }

    private List<TokenGranter> getTokenGranters(AuthorizationCodeServices authorizationCodeServices
            , TokenStore tokenStore, AuthorizationServerTokenServices tokenServices
            , ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        return new ArrayList<TokenGranter>() {{
            add(new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService
                    , requestFactory));
            add(new RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory));
            add(new ResourceOwnerPasswordTokenGranter(authenticationManager, tokenServices, clientDetailsService
                    , requestFactory));
            add(new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory));
            add(new SMSCodeTokenGranter(userService, smsDetailsService, tokenServices, clientDetailsService
                    , requestFactory));
        }};
    }

    @Value("${jwt.expiration}")
    private Integer expiration;
    @Value("${jwt.refreshExpiration}")
    private Integer refreshExpiration;

    @Primary
    @Bean
    public DefaultTokenServices defaultTokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore);
        tokenServices.setSupportRefreshToken(true);
        // token有效期自定义设置，7天
        tokenServices.setAccessTokenValiditySeconds(expiration);
        // refresh_token 14天
        tokenServices.setRefreshTokenValiditySeconds(refreshExpiration);
        tokenServices.setTokenEnhancer(jwtAccessTokenConverter);
        tokenServices.setClientDetailsService(clientDetailsService);
        tokenServices.setAuthenticationManager(authenticationManager);
        return tokenServices;
    }

}
