package com.mhx.test.controller;

import com.mhx.test.entity.role;
import com.mhx.test.service.roleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class roleController {

    @Autowired(required = false)
    private roleService roleService;

    @GetMapping("/getRole")
    public Map<String,Object> getRole(int id){
        role role = roleService.findRoleById(id);
        Map<String,Object> result = new HashMap<>();
        result.put("name",role.getName());
        result.put("id",role.getId());
        result.put("nameZh",role.getNameZh());
        return result;
    }

    @PostMapping("/updateRole")
    public int updateRole(role role){
        int n = roleService.updateRole(role);
        return n;
    }

    @PostMapping("/delRole")
    public int delRole(int id){
        return roleService.deleteRoleById(id);
    }
}
