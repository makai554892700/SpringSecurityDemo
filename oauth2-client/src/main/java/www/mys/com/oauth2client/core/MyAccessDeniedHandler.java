package www.mys.com.oauth2client.core;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component(value = "myAccessDeniedHandler")
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger log = Logger.getLogger(MyAccessDeniedHandler.class.getName());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response
            , AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.log(Level.WARNING, "MyAccessDeniedHandler handle." + accessDeniedException.getMessage());
        response.addHeader("content-type", "application/json");
        response.getWriter().write("{\"message\":\"" + accessDeniedException.getMessage() + "\",\"code\":-1}");
    }
}