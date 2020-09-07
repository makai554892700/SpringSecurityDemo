package www.mys.com.oauth2client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import www.mys.com.oauth2client.config.MyPropertySourceFactory;

@SpringBootApplication
@PropertySource(value = "file:${mys.config.file}", factory = MyPropertySourceFactory.class)
public class OAuth2ClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2ClientApplication.class, args);
    }

}
