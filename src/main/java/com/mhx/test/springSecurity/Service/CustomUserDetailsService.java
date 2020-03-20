package com.mhx.test.springSecurity.Service;

import com.mhx.test.entity.SysRole;
import com.mhx.test.entity.SysUser;
import com.mhx.test.entity.SysUserRole;
import com.mhx.test.service.SysRoleService;
import com.mhx.test.service.SysUserRoleService;
import com.mhx.test.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        Collection<GrantedAuthority> authorities = new ArrayList<>();

        //从数据库中取出用户信息
        SysUser user = userService.selectByName(username);

        if(user == null){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //添加权限
        List<SysUserRole> roleList = userRoleService.listByUserId(user.getId());

        for (SysUserRole userRole : roleList) {
            SysRole role = roleService.selectById(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        //返回UserDetaill实现类
        return new User(user.getName(),user.getPassword(),authorities);
    }
}
