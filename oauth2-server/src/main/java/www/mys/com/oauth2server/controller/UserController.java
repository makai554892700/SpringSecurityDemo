package www.mys.com.oauth2server.controller;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import www.mys.com.oauth2server.api.UserApi;
import www.mys.com.oauth2server.base.ExceptionEnum;
import www.mys.com.oauth2server.service.UserService;
import www.mys.com.oauth2server.vo.request.RequestRegister;
import www.mys.com.oauth2server.vo.request.RequestUpdateUser;
import www.mys.com.oauth2user.vo.response.ResponseUser;
import www.mys.com.utils.*;
import www.mys.com.utils.vo.response.ResponsePage;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController implements UserApi {

    @Resource
    HttpServletRequest request;
    @Resource
    HttpServletResponse response;
    @Resource(name = "userService")
    private UserService userService;

    @Override
    public Result<Boolean> checkUser(String type, String key) {
        if (StringUtils.isEmpty(type) || StringUtils.isEmpty(key)) {
            throw new BaseException(ExceptionEnum.DATA_ERROR.getExceptionInfo());
        }
        switch (type) {
            case "phone":
                return ResultUtils.succeed(userService.getUserByPhone(key) != null);
            case "userName":
                return ResultUtils.succeed(userService.getUserByUserName(key) != null);
            default:
                throw new BaseException(ExceptionEnum.DATA_ERROR.getExceptionInfo());
        }
    }

    @Override
    public Result<ResponseUser> register(@RequestBody @Valid RequestRegister requestRegister
            , BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new Exception(bindingResult.getFieldError().getDefaultMessage());
        }
        return ResultUtils.succeed(userService.register(requestRegister));
    }

    @Override
    public Result<ResponseUser> getUserInfo() throws Exception {
        return ResultUtils.succeed(userService.getUserInfo());
    }

    @Override
    public Result<ResponseUser> update(@RequestBody @Valid RequestUpdateUser requestUpdateUser
            , BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new Exception(bindingResult.getFieldError().getDefaultMessage());
        }
        return ResultUtils.succeed(userService.update(requestUpdateUser));
    }

    @Override
    public Result<ResponsePage<ResponseUser>> getUsers(Integer page, Integer count, Integer sortType) throws Exception {
        return ResultUtils.succeed(userService.getUsers(page, count, sortType));
    }

    @Override
    public void delUsers(@RequestBody @Valid RequestData<List<Long>> ids
            , BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new Exception(bindingResult.getFieldError().getDefaultMessage());
        }
        userService.delUsers(ids.getData());
    }

    @Override
    public Result<String> getSMSCode(String phone, String userName) {
        return ResultUtils.succeed(userService.getSMSCode(phone, userName));
    }

}
