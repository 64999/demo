package com.mhx.test.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class SysUser implements Serializable {

    private Integer id;

    private String name;

    private String password;
}
