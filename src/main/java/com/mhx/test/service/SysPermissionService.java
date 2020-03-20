package com.mhx.test.service;

import com.mhx.test.dao.SyspermissionDao;
import com.mhx.test.entity.Syspermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPermissionService {

    @Autowired(required = false)
    private SyspermissionDao syspermissionDao;

    /**
     * 获取指定角色所有权限
     */
    public List<Syspermission> listByRoleId(Integer roleId) {
        return syspermissionDao.listByRoleId(roleId);
    }

}
