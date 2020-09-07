package www.mys.com.oauth2client.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/info")
public interface InfoApi {

    @GetMapping(value = "/{data}")
    public String getInfo(@PathVariable("data") Integer data);

}
