package www.mys.com.oauth2server.base;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;
import www.mys.com.common.LogUtils;

import java.util.HashMap;
import java.util.Map;

// 自定义 userDetails
@Component(value = "myJwtTokenEnhancer")
public class JwtTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken
            , OAuth2Authentication authentication) {
        final Map<String, Object> additionalInformation = new HashMap<>();
        Object userDetails = authentication.getUserAuthentication().getPrincipal();
        LogUtils.log("enhance userDetails=" + userDetails);
//        additionalInformation.put(JwtTokenUtils.REAL_USER, userDetails.getRealUser());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
        return accessToken;
    }
}
