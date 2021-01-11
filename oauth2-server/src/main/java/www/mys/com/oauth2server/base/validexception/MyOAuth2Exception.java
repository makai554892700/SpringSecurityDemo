package www.mys.com.oauth2server.base.validexception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

@JsonSerialize(using = MyOauthExceptionSerializer.class)
public class MyOAuth2Exception extends OAuth2Exception {

    private String oAuth2ErrorCode;
    private int httpErrorCode;

    public String getoAuth2ErrorCode() {
        return oAuth2ErrorCode;
    }

    public void setoAuth2ErrorCode(String oAuth2ErrorCode) {
        this.oAuth2ErrorCode = oAuth2ErrorCode;
    }

    @Override
    public int getHttpErrorCode() {
        return httpErrorCode;
    }

    public void setHttpErrorCode(int httpErrorCode) {
        this.httpErrorCode = httpErrorCode;
    }

    public MyOAuth2Exception(String msg, String oAuth2ErrorCode, int httpErrorCode) {
        super(msg);
        this.oAuth2ErrorCode = oAuth2ErrorCode;
        this.httpErrorCode = httpErrorCode;
    }

}
