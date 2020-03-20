package com.mhx.test.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Syspermission implements Serializable {

    private Integer id;

    private String url;

    private Integer roleId;

    private String permission;

    private List permissions;


    public List getPermissions() {
        return Arrays.asList(this.permission.trim().split(","));
    }

    public void setPermissions(List permissions) {
        this.permissions = permissions;
    }
}
