package www.mys.com.oauth2server.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import www.mys.com.oauth2server.base.ExceptionEnum;
import www.mys.com.oauth2server.base.MyUserDetails;
import www.mys.com.oauth2server.mapper.AuthCheckerMapper;
import www.mys.com.oauth2server.mapper.CustomUserMapper;
import www.mys.com.oauth2server.mapper.SMSHistoryMapper;
import www.mys.com.oauth2server.pojo.AuthChecker;
import www.mys.com.oauth2server.pojo.CustomUser;
import www.mys.com.oauth2server.service.UserService;
import www.mys.com.oauth2server.utils.PhoneCheckUtils;
import www.mys.com.oauth2server.utils.SortUtils;
import www.mys.com.oauth2server.vo.request.RequestRegister;
import www.mys.com.oauth2server.vo.request.RequestUpdateUser;
import www.mys.com.oauth2user.mapper.UserMapper;
import www.mys.com.oauth2user.pojo.Role;
import www.mys.com.oauth2user.pojo.User;
import www.mys.com.oauth2user.vo.response.ResponseUser;
import www.mys.com.utils.*;
import www.mys.com.utils.vo.response.ResponsePage;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final Logger log = Logger.getLogger(this.getClass().getName());

    public static final String SSO_USER_KEY = "sso_user_";
    @Resource(name = "userMapper")
    private UserMapper userMapper;
    @Resource(name = "customUserMapper")
    private CustomUserMapper customUserMapper;
    @Value("${yizhu.login.sendSMS:false}")
    private boolean sendSMS;
    @Resource(name = "authCheckerMapper")
    private AuthCheckerMapper authCheckerMapper;
    @Resource(name = "smsHistoryMapper")
    private SMSHistoryMapper smsHistoryMapper;
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseUser register(RequestRegister requestRegister) throws Exception {
        User user = null;
        switch (requestRegister.getUserType()) {
            case RequestRegister.RegisterType.I_USER_NAME_PASS:
                user = userMapper.getUserByUserName(requestRegister.getCheckKey());
                break;
            case RequestRegister.RegisterType.I_PHONE:
                break;
            default:
                break;
        }
        if (user == null) {
            user = new User();
            user.setUserName(requestRegister.getCheckKey());
            user.setPass(passwordEncoder.encode(requestRegister.getCheckValue()));
            user.setLastLoginTime(new Date());
            userMapper.save(user);
            return userTransfer.real2Res(user);
        }
        throw new BaseException(ExceptionEnum.USER_ALREADY_EXIST.getExceptionInfo());
    }

    @Override
    public ResponseUser update(RequestUpdateUser requestUpdateUser) throws Exception {
//        User user = userMapper.getUserById(requestUpdateUser.getUserName());
//        if (user != null) {
//            boolean needUpdate = false;
//            if (!StringUtils.isEmpty(requestUpdateUser.getPassWord())) {
//                needUpdate = true;
//                user.setPass(passwordEncoder.encode(requestUpdateUser.getPassWord()));
//            }
//            if (!StringUtils.isEmpty(requestUpdateUser.getUserName())) {
//                needUpdate = true;
//                user.setUserName(requestUpdateUser.getUserName());
//            }
//            if (needUpdate) {
//                userMapper.save(user);
//            }
//            return userTransfer.real2Res(user);
//        }
        throw new BaseException(ExceptionEnum.NO_SUCH_USER.getExceptionInfo());
    }

    @Override
    public ResponsePage<ResponseUser> getUsers(Integer page, Integer count, Integer sortType) {
        if (page != null && count != null) {
            return PageUtils.transferPage(userMapper.findAll(SortUtils.getPageAble(sortType, page, count)), userTransfer);
        } else {
            return PageUtils.transferPage(userMapper.findAll(SortUtils.getPageAble(sortType, page, count)), userTransfer);
        }
    }

    @Override
    public void delUsers(List<Long> ids) {
        List<Integer> userIds = new ArrayList<>();
        for (Long id : ids) {
            userIds.add(id.intValue());
        }
        userMapper.deleteByIdIn(userIds);
    }

    @Override
    public ResponseUser getUserInfo() throws Exception {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (StringUtils.isEmpty(userName)) {
            throw new BaseException(ExceptionEnum.LOGIN_ERROR.getExceptionInfo());
        }
        User user = userMapper.getUserByUserName(userName);
        if (user == null) {
            throw new BaseException(ExceptionEnum.NO_SUCH_USER.getExceptionInfo());
        }
        return userTransfer.real2Res(user);
    }

    @Override
    public String getSMSCode(String phone, String userName) {
        log.log(Level.WARNING, "getSMSCode phone=" + phone);
        if (!PhoneCheckUtils.isChinaPhoneLegal(phone)) {
            throw new BaseException(ExceptionEnum.CHECK_PHONE_ERROR.getExceptionInfo());
        }
        AuthChecker authChecker = authCheckerMapper.getByCheckKey(phone);
        String smsCheckCode = StringUtils.getRandomStr(4, StringUtils.StrType.NUM);
        if (authChecker != null) {
            if (authChecker.getUpdatedAt().getTime() + 60 * 1000 > System.currentTimeMillis()) {
                throw new BaseException(String.format("请 %s S后重试", ((authChecker.getUpdatedAt().getTime() + 60 * 1000)
                        - System.currentTimeMillis()) / 1000), ResultUtils.ERROR_CODE);
            }
            authChecker.setTryTimes(0);
            authChecker.setCheckValue(smsCheckCode);
        } else {
            authChecker = new AuthChecker(phone, smsCheckCode
                    , 0, 3);
        }
        //耗时操作异步进行
//        smsAliyunUtils.saveSMSHistory(authCheckerMapper, authChecker, smsHistoryMapper, phone, smsCheckCode);
        if (sendSMS) {
//            String result = smsAliyunUtils.send(phone, String.format("{\"code\":\"%s\"}", smsCheckCode));
//            log.log(Level.WARNING, "send sms result=" + result);
            return "success";
        } else {
            return smsCheckCode;
        }
    }

    @Override
    public void checkSMSCode(String phone, String smsCode) {
        log.log(Level.WARNING, "checkSMS phone=" + phone + ";smsCode=" + smsCode);
        if (StringUtils.isEmpty(phone) || !PhoneCheckUtils.isChinaPhoneLegal(phone)) {
            throw new BaseException(ExceptionEnum.CHECK_PHONE_ERROR.getExceptionInfo());
        }
        if (StringUtils.isEmpty(smsCode)) {
            throw new BaseException(ExceptionEnum.DATA_ERROR.getExceptionInfo());
        }
        AuthChecker authChecker = authCheckerMapper.getByCheckKey(phone);
        log.log(Level.WARNING, "checkSMS smsChecker=" + authChecker);
        if (authChecker == null) {
            throw new BaseException(ExceptionEnum.GET_SMS_CODE_FIRST.getExceptionInfo());
        }
        if (authChecker.getTryTimes() > authChecker.getMaxTimes()) {
            authCheckerMapper.delete(authChecker);
            throw new BaseException(ExceptionEnum.OUT_OF_MAX_TIMES.getExceptionInfo());
        }
        if (authChecker.getUpdatedAt().getTime() + 10 * 60 * 1000 < System.currentTimeMillis()) {
            authCheckerMapper.delete(authChecker);
            throw new BaseException(ExceptionEnum.SMS_CODE_TIME_OUT.getExceptionInfo());
        }
        if (!authChecker.getCheckValue().equals(smsCode)) {
            authChecker.setTryTimes(authChecker.getTryTimes() + 1);
            authCheckerMapper.save(authChecker);
            throw new BaseException(ExceptionEnum.SMS_CODE_ERROR.getExceptionInfo());
        }
        authCheckerMapper.delete(authChecker);
    }

    @Override
    public User getUserByPhone(String phone) {
        if (StringUtils.isEmpty(phone) || !PhoneCheckUtils.isChinaPhoneLegal(phone)) {
            throw new BaseException(ExceptionEnum.CHECK_PHONE_ERROR.getExceptionInfo());
        }
        CustomUser customUser = customUserMapper.getByMarkTypeAndMark(CustomUser.MarkType.PHONE, phone);
        if (customUser == null) {
            throw new InvalidGrantException(ExceptionEnum.REGISTER_PHONE_FIRST.getMsg());
        }
        if (customUser.getBindUser() == null) {
            throw new InvalidGrantException(ExceptionEnum.NO_UNBIND_USER.getMsg());
        }
        return getUserById(customUser);
    }

    @Override
    public User getUserByEBUserName(String userName) {
        if (StringUtils.isEmpty(userName)) {
            throw new InvalidGrantException(ExceptionEnum.DATA_ERROR.getMsg());
        }
        CustomUser customUser = customUserMapper.getByMarkTypeAndMark(CustomUser.MarkType.EVERY_BIM, userName);
        if (customUser == null) {
            throw new InvalidGrantException(ExceptionEnum.NO_SUCH_USER.getMsg());
        }
        if (customUser.getBindUser() == null) {
            throw new InvalidGrantException(ExceptionEnum.NO_UNBIND_USER.getMsg());
        }
        return getUserById(customUser);
    }

    private User getUserById(CustomUser customUser) {
        User user = userMapper.getUserById(customUser.getBindUser().intValue());
        if (user == null) {
            customUser.setBindUser(null);
            customUserMapper.save(customUser);
            log.info("绑定的用户丢失.bindUserId=" + customUser.getBindUser());
            throw new InvalidGrantException(ExceptionEnum.UNKNOW_ERROR.getMsg());
        }
        return user;
    }

    @Override
    public User getUserByUserName(String userName) {
        return userMapper.getUserByUserName(userName);
    }

    @Override
    public UserDetails loadUser(String key, String type) {
        log.log(Level.WARNING, "loadUser type=" + type + ";key=" + key);
        long start = System.currentTimeMillis();
        String userKey = SSO_USER_KEY + MD5Utils.MD5(key, true);
        User user = null;
        switch (type) {
            case "phone":
                user = getUserByPhone(key);
                break;
            case "userName":
                user = getUserByUserName(key);
                break;
            case "everyBim":
                user = getUserByEBUserName(key);
                break;
        }
        if (user == null) {
            throw new UsernameNotFoundException(key);
        }
        String[] roleStrs = new String[user.getRoles().size()];
        int i = 0;
        for (Role role : user.getRoles()) {
            roleStrs[i++] = role.getRoleName();
        }
        MyUserDetails userDetails = new MyUserDetails(user.getPass(), user.getUserName()
                , user.isAccountNonExpired(), user.isAccountNonLocked()
                , user.isCredentialsNonExpired(), user.isEnabled()
                , AuthorityUtils.createAuthorityList(roleStrs));
        log.log(Level.WARNING, "loadUserByUsername time=" + (System.currentTimeMillis() - start));
        return userDetails;
    }

    private final DataTransfer<Object, ResponseUser, User> userTransfer =
            new DataTransfer<Object, ResponseUser, User>() {
                @Override
                public User req2Real(Object o) {
                    return null;
                }

                @Override
                public ResponseUser real2Res(User user) {
                    return new ResponseUser(user.getId(), user.getUserName(), user.getRealName()
                            , user.getLastLoginTime(), user.isAccountNonExpired(), user.isAccountNonLocked()
                            , user.isCredentialsNonExpired(), user.isEnabled());
                }

                @Override
                public void updateReal(Object o, User user) {

                }
            };

}
