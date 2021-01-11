package www.mys.com.oauth2server.base;

import com.alibaba.fastjson.JSON;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import www.mys.com.utils.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component(value = "myAuthenticationEntryPoint")
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException authException) throws IOException, ServletException {
        log.log(Level.WARNING, "MyAuthenticationEntryPoint commence." + authException.getMessage());
        Throwable throwable = authException.getCause();
        response.addHeader("content-type", "application/json;charset=utf-8");
        if (throwable instanceof InvalidTokenException) {
            log.log(Level.WARNING, "token time out.");
            response.getWriter().write(JSON.toJSONString(new Result<>(-3, "token time out.", "")));
        } else {
            log.log(Level.WARNING, "check token error." + authException.getMessage());
            response.getWriter().write(JSON.toJSONString(new Result<>(-3, authException.getMessage(), "")));
        }
    }
}
