package www.mys.com.oauth2server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import www.mys.com.oauth2.pojo.OauthClientDetails;

import java.util.List;

// 权限配置信息
@Component("defaultOauthClientDetails")
@ConfigurationProperties(prefix = "app.default")
public class DefaultOauthClientDetails {

    private List<OauthClientDetails> oauthClientDetails;

    public List<OauthClientDetails> getOauthClientDetails() {
        return oauthClientDetails;
    }

    public void setOauthClientDetails(List<OauthClientDetails> oauthClientDetails) {
        this.oauthClientDetails = oauthClientDetails;
    }

    @Override
    public String toString() {
        return "DefaultOauthClientDetails{" +
                "oauthClientDetails=" + oauthClientDetails +
                '}';
    }
}
