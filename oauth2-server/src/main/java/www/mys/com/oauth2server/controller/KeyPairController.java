package www.mys.com.oauth2server.controller;

import org.springframework.web.bind.annotation.RestController;
import www.mys.com.oauth2server.api.KeyPairApi;
import www.mys.com.oauth2server.base.JwtConfig;

import javax.annotation.Resource;

@RestController
public class KeyPairController implements KeyPairApi {

    @Resource
    private JwtConfig jwtConfig;

    @Override
    public String getPublicKey() {
        return jwtConfig.getPublicKey();
    }
}
