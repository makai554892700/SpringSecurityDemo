package www.mys.com.oauth2client.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.TokenExtractor;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import www.mys.com.common.LogUtils;
import www.mys.com.utils.CloseUtils;
import www.mys.com.utils.FileUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Value("${jwt.keyPath}")
    private String keyPath;
    @Resource(name = "yzTokenExtractor")
    private TokenExtractor tokenExtractor;
    @Resource(name = "myAuthenticationEntryPoint")
    private AuthenticationEntryPoint myAuthenticationEntryPoint;
    @Resource(name = "myAccessDeniedHandler")
    private AccessDeniedHandler myAccessDeniedHandler;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenExtractor(tokenExtractor)
                .accessDeniedHandler(myAccessDeniedHandler)
                .authenticationEntryPoint(myAuthenticationEntryPoint)
        ;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .anyRequest().authenticated()
        ;
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        String publicKey;
        File keyFile = new File(keyPath);
        LogUtils.log("keyFilePath=" + keyFile.getAbsolutePath());
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(keyFile);
            publicKey = new String(FileUtils.inputStream2Bytes(fileInputStream));
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } finally {
            CloseUtils.closeSilently(fileInputStream);
        }
        converter.setVerifierKey(publicKey);
        return converter;
    }

}
