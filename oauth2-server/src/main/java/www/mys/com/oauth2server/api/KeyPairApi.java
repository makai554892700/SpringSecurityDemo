package www.mys.com.oauth2server.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/oauth")
public interface KeyPairApi {

    @GetMapping("/pubkey")
    public String getPublicKey();

}
