package www.mys.com.oauth2server.api;

import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import www.mys.com.oauth2server.vo.request.RequestRegister;
import www.mys.com.oauth2server.vo.request.RequestUpdateUser;
import www.mys.com.oauth2user.vo.response.ResponseUser;
import www.mys.com.utils.RequestData;
import www.mys.com.utils.Result;
import www.mys.com.utils.vo.response.ResponsePage;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/api/user")
public interface UserApi {

    @GetMapping(value = "/check/{type}/{key}")
    public Result<Boolean> checkUser(@PathVariable("type") String type, @PathVariable("key") String key);

    @PostMapping(value = "/register")
    public Result<ResponseUser> register(@RequestBody @Valid RequestRegister requestRegister
            , BindingResult bindingResult) throws Exception;

    @GetMapping(value = "/info")
    public Result<ResponseUser> getUserInfo() throws Exception;

    @PostMapping(value = "/update")
    public Result<ResponseUser> update(@RequestBody @Valid RequestUpdateUser requestUpdateUser
            , BindingResult bindingResult) throws Exception;

    @GetMapping(value = "/list")
    @Secured({"admin"})
    public Result<ResponsePage<ResponseUser>> getUsers(@RequestParam(required = false) Integer page
            , @RequestParam(required = false) Integer count, @RequestParam(required = false) Integer sortType) throws Exception;

    @PostMapping(value = "/delete")
    @Secured({"admin"})
    public void delUsers(@RequestBody @Valid RequestData<List<Long>> ids
            , BindingResult bindingResult) throws Exception;

    @GetMapping(value = {"/sms/code/{phone}/{userName}", "/sms/code/{phone}"})
    public Result<String> getSMSCode(@PathVariable("phone") String phone
            , @PathVariable(value = "userName", required = false) String userName);

}
