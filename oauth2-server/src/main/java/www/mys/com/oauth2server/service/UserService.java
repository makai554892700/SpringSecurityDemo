package www.mys.com.oauth2server.service;

import org.springframework.security.core.userdetails.UserDetails;
import www.mys.com.oauth2server.vo.request.RequestRegister;
import www.mys.com.oauth2server.vo.request.RequestUpdateUser;
import www.mys.com.oauth2user.pojo.User;
import www.mys.com.oauth2user.vo.response.ResponseUser;
import www.mys.com.utils.vo.response.ResponsePage;

import java.util.List;

public interface UserService {

    public ResponseUser register(RequestRegister requestRegister) throws Exception;

    public ResponseUser getUserInfo() throws Exception;

    public ResponseUser update(RequestUpdateUser requestUpdateUser) throws Exception;

    public ResponsePage<ResponseUser> getUsers(Integer page, Integer count, Integer sortType);

    public void delUsers(List<Long> ids);

    public String getSMSCode(String phone, String userName);

    public void checkSMSCode(String phone, String smsCode);

    public User getUserByPhone(String phone);

    public User getUserByUserName(String userName);

    public User getUserByEBUserName(String userName);

    public UserDetails loadUser(String key, String type);

}
