package com.mhx.test.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class role implements Serializable {

    private int id;
    private String name;
    private String nameZh;

}
