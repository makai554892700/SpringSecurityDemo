package www.mys.com.oauth2server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import www.mys.com.oauth2server.config.MyPropertySourceFactory;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@PropertySource(value = {
        "classpath:/properties/user.properties",
        "classpath:/properties/app.properties",
}, encoding = "UTF-8")
@PropertySource(value = "file:${mys.config.file}", factory = MyPropertySourceFactory.class)
@EntityScan(basePackages = {
        "www.mys.com.oauth2.pojo",
        "www.mys.com.oauth2user.pojo",
        "www.mys.com.oauth2server.pojo",
})
@EnableJpaRepositories(basePackages = {
        "www.mys.com.oauth2.mapper",
        "www.mys.com.oauth2user.mapper",
        "www.mys.com.oauth2server.mapper",
})
public class OAuth2ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2ServerApplication.class, args);
    }

}
