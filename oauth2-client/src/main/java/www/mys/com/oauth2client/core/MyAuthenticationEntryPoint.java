package www.mys.com.oauth2client.core;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component(value = "myAuthenticationEntryPoint")
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = Logger.getLogger(MyAccessDeniedHandler.class.getName());

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response
            , AuthenticationException authException) throws IOException, ServletException {
        log.log(Level.WARNING, "MyAuthenticationEntryPoint commence." + authException.getMessage());
        Throwable throwable = authException.getCause();
        response.addHeader("content-type", "application/json");
        if (throwable instanceof InvalidTokenException) {
            response.getWriter().write("{\"message\":\"token time out.\",\"code\":-1}");
        } else {
            response.getWriter().write("{\"message\":\"" + authException.getMessage() + "\",\"code\":-1}");
        }
    }
}