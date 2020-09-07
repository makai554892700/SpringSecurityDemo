package www.mys.com.oauth2server.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import www.mys.com.common.LogUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;
import java.security.KeyPair;

@Configuration
public class JwtConfig {

    @Value("${jwt.keyPath}")
    private String keyPath;
    @Value("${jwt.password}")
    private String password;
    @Value("${jwt.keypair}")
    private String keypair;
    @Resource
    private DataSource dataSource;
    @Resource(name = "myJwtTokenEnhancer")
    private TokenEnhancer jwtTokenEnhancer;

    @Bean
    public PasswordEncoder myPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ClientDetailsService myClientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }

    @Bean
    public TokenStore myTokenStore() {
        return new JwtTokenStore(myJwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter myJwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                jwtTokenEnhancer.enhance(accessToken, authentication);
                return super.enhance(accessToken, authentication);
            }
        };
        String keyPathStr = new File(keyPath).getAbsolutePath();
        LogUtils.log("keyPathStr=" + keyPathStr);
        KeyPair keyPair = new KeyStoreKeyFactory(new PathResource(keyPathStr)
                , password.toCharArray()).getKeyPair(keypair);
        converter.setKeyPair(keyPair);
        return converter;
    }

}
