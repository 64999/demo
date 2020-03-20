package com.mhx.test.service;

import com.mhx.test.dao.SysUserRoleDao;
import com.mhx.test.entity.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysUserRoleService {

    @Autowired
    private SysUserRoleDao userRoleMapper;

    public List<SysUserRole> listByUserId(Integer userId) {
        return userRoleMapper.listByUserId(userId);
    }
}
