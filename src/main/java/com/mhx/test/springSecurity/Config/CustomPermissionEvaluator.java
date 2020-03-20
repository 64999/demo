package com.mhx.test.springSecurity.Config;

import com.mhx.test.entity.Syspermission;
import com.mhx.test.service.SysPermissionService;
import com.mhx.test.service.SysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private SysRoleService roleService;

    /**
     * 参数 1 代表用户的权限身份
     * 参数 2 参数 3 分别和 @PreAuthorize("hasPermission('/admin','r')")
     * 中的参数对应，即访问 url 和权限
     * @param authentication
     * @param targetUrl
     * @param targetPermission
     * @return
     * 思路
     * 通过 Authentication 取出登录用户的所有 Role
     *
     * 遍历每一个 Role，获取到每个Role的所有 Permission
     *
     * 遍历每一个 Permission，只要有一个 Permission 的 url 和传入的url相同，
     * 且该 Permission 中包含传入的权限，返回 true
     *
     * 如果遍历都结束，还没有找到，返回false
     *
     * 1.authentication.getPrincipal()返回的是字符串而不是User对象
     * 主要是因为构造Token的参数出错了
     *
     *  问题：出现jjava.lang.String cannot be cast to org.springframework.security.core.userdetails.User
     *  解决：
     *  CustomAuthenticationProvider -->UsernamePasswordAuthenticationToken 参数组装出错
     *  源码继承
     * Authentication
     *      -->authentication.getPrincipal()
     *          -->UsernamePasswordAuthenticationToken
     *
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetUrl, Object targetPermission) {

        //获得loadUserByUserName()方法结果
        User user = (User) authentication.getPrincipal();//问题出现位置
        //获得loadUserByUserName()注入的角色
        Collection<GrantedAuthority> authorities = user.getAuthorities();

        for (GrantedAuthority authority : authorities) {
            String realName = authority.getAuthority();

            Integer roleId = roleService.selectByName(realName).getId();

            //得到角色所有权限
            List<Syspermission> permissionList = permissionService.listByRoleId(roleId);

            for (Syspermission syspermission : permissionList) {
                //获取权限集
                List permissions = syspermission.getPermissions();
                //若访问的url和权限用户符合，返回true
                if(targetUrl.equals(syspermission.getUrl())
                    && permissions.contains(targetPermission)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String s, Object o) {
        return false;
    }
}
