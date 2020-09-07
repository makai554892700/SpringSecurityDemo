package www.mys.com.oauth2client.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import www.mys.com.common.LogUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Component(value = "yzTokenExtractor")
public class MYSTokenExtractor extends BearerTokenExtractor {

    @Value("${jwt.header}")
    private String header;

    @Override
    protected String extractHeaderToken(HttpServletRequest request) {
        String xAccessToken = request.getHeader(header);
        LogUtils.log("extractHeaderToken xAccessToken=" + xAccessToken);
        if (!StringUtils.isEmpty(xAccessToken)) {
            request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE, xAccessToken);
            return xAccessToken;
        } else {
            Enumeration<String> headers = request.getHeaders("Authorization");
            while (headers.hasMoreElements()) {
                String value = headers.nextElement();
                if ((value.toLowerCase().startsWith(OAuth2AccessToken.BEARER_TYPE.toLowerCase()))) {
                    String authHeaderValue = value.substring(OAuth2AccessToken.BEARER_TYPE.length()).trim();
                    request.setAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_TYPE,
                            value.substring(0, OAuth2AccessToken.BEARER_TYPE.length()).trim());
                    int commaIndex = authHeaderValue.indexOf(',');
                    if (commaIndex > 0) {
                        authHeaderValue = authHeaderValue.substring(0, commaIndex);
                    }
                    return authHeaderValue;
                }
            }
            return null;
        }

    }
}
