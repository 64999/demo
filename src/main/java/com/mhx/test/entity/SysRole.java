package com.mhx.test.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class SysRole implements Serializable {

    private Integer id;

    private String name;
}
