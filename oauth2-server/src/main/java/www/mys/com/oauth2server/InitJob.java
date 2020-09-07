package www.mys.com.oauth2server;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import www.mys.com.oauth2server.config.DefaultOauthClientDetails;
import www.mys.com.common.LogUtils;
import www.mys.com.oauth2server.config.DefaultUserConf;
import www.mys.com.oauth2.mapper.OauthClientDetailsMapper;
import www.mys.com.oauth2.pojo.OauthClientDetails;
import www.mys.com.oauth2user.mapper.PermissionMapper;
import www.mys.com.oauth2user.mapper.RoleMapper;
import www.mys.com.oauth2user.mapper.UserMapper;
import www.mys.com.oauth2user.pojo.Permission;
import www.mys.com.oauth2user.pojo.Role;
import www.mys.com.oauth2user.pojo.User;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class InitJob {

    @Resource(name = "defaultOauthClientDetails")
    private DefaultOauthClientDetails defaultOauthClientDetails;
    @Resource(name = "oauthClientDetailsMapper")
    private OauthClientDetailsMapper oauthClientDetailsMapper;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource(name = "permissionMapper")
    private PermissionMapper permissionMapper;
    @Resource(name = "roleMapper")
    private RoleMapper roleMapper;
    @Resource(name = "userMapper")
    private UserMapper userMapper;
    @Resource(name = "defaultUserConf")
    private DefaultUserConf defaultUserConf;

    @PostConstruct
    public void initProject() {
        LogUtils.log("Init project.");
        initUser();
        initOauthClientDetails();
    }


    private void initUser() {
        User tempUser = defaultUserConf.getUser();
        tempUser.setPass(passwordEncoder.encode(tempUser.getPass()));
        User user = userMapper.getUserByUserName(tempUser.getUserName());
        if (user != null) {
            LogUtils.log("User already exist.");
            return;
        }
        Permission tempPermission = defaultUserConf.getPermission();
        Permission permission = permissionMapper.getPermissionByPermissionName(tempPermission.getPermissionName());
        if (permission == null) {
            permission = permissionMapper.save(new Permission(tempPermission.getId()
                    , tempPermission.getPermissionMark(), tempPermission.getPermissionName()));
        }
        Role tempRole = defaultUserConf.getRole();
        Role role = roleMapper.getRoleByRoleName(tempRole.getRoleName());
        if (role == null) {
            List<Permission> permissions = new ArrayList<>();
            permissions.add(new Permission(permission.getId(), permission.getPermissionMark(), permission.getPermissionName()));
            tempRole.setPermissions(permissions);
            role = roleMapper.save(new Role(tempRole.getId(), tempRole.getRoleName(), tempRole.getRoleDesc()));
        }
        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role(role.getId(), role.getRoleName(), role.getRoleDesc()));
        tempUser.setRoles(roleList);
        tempUser.setLastLoginTime(new Date());
        userMapper.save(tempUser);
    }

    private void initOauthClientDetails() {
        List<OauthClientDetails> oauthClientDetails = defaultOauthClientDetails.getOauthClientDetails();
        if (oauthClientDetails != null) {
            for (OauthClientDetails clientDetails : oauthClientDetails) {
                OauthClientDetails dbOauthClientDetails = oauthClientDetailsMapper.getByClientId(clientDetails.getClientId());
                if (dbOauthClientDetails != null) {
                    LogUtils.log("OauthClientDetails already exist.");
                    return;
                }
                clientDetails.setClientSecret(passwordEncoder.encode(clientDetails.getClientSecret()));
                oauthClientDetailsMapper.save(clientDetails);
            }
        }
    }

}
