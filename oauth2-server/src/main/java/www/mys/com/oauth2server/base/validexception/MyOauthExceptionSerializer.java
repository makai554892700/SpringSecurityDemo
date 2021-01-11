package www.mys.com.oauth2server.base.validexception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import www.mys.com.utils.ResultUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

public class MyOauthExceptionSerializer extends StdSerializer<MyOAuth2Exception> {
    private static final long serialVersionUID = 2652127645704345563L;

    public MyOauthExceptionSerializer() {
        super(MyOAuth2Exception.class);
    }

    @Override
    public void serialize(MyOAuth2Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getRequest();
        gen.writeObjectField("code", ResultUtils.FIELD_CODE);
        gen.writeObjectField("message", value.getMessage());
        gen.writeObjectField("error_desc", value.getOAuth2ErrorCode());
        gen.writeObjectField("path", request.getServletPath());
        if (value.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                gen.writeObjectField(key, add);
            }
        }
        gen.writeEndObject();
    }
}
