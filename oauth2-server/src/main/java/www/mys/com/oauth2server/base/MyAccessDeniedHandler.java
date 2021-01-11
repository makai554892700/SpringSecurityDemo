package www.mys.com.oauth2server.base;

import com.alibaba.fastjson.JSON;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import www.mys.com.utils.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component(value = "myAccessDeniedHandler")
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response
            , AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.log(Level.WARNING, "MyAccessDeniedHandler handle." + accessDeniedException.getMessage());
        response.addHeader("content-type", "application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(new Result<>(-3, accessDeniedException.getMessage(), "")));
    }
}
