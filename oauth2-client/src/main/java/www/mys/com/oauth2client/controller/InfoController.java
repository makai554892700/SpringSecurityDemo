package www.mys.com.oauth2client.controller;

import org.springframework.web.bind.annotation.RestController;
import www.mys.com.oauth2client.api.InfoApi;

@RestController
public class InfoController implements InfoApi {

    @Override
    public String getInfo(Integer data) {
        return String.valueOf(data);
    }
}
