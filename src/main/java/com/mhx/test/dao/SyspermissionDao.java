package com.mhx.test.dao;

import com.mhx.test.entity.Syspermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SyspermissionDao {

    @Select("SELECT * FROM sys_permission WHERE role_id=#{roleId}")
    List<Syspermission> listByRoleId(Integer roleId);
}
