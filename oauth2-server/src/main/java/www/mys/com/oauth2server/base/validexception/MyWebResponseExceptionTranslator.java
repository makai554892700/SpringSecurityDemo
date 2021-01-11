package www.mys.com.oauth2server.base.validexception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;
import www.mys.com.utils.ResultUtils;

@Component(value = "myWebResponseExceptionTranslator")
public class MyWebResponseExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
        ResponseEntity<OAuth2Exception> translate = super.translate(e);
        OAuth2Exception body = translate.getBody();
        return new ResponseEntity<>(
                body == null ? new MyOAuth2Exception("unknow error", "unknow error", ResultUtils.FIELD_CODE)
                        : new MyOAuth2Exception(body.getMessage(), body.getOAuth2ErrorCode(), body.getHttpErrorCode())
                , translate.getHeaders(),
                translate.getStatusCode());
    }
}
