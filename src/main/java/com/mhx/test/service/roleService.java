package com.mhx.test.service;


import com.mhx.test.dao.roleDao;
import com.mhx.test.entity.role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class roleService {
    @Autowired(required = false)
    private roleDao roleDao;

    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    public List<role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    public Integer addRole(role role) {

        return roleDao.insert(role);
    }

    public Integer deleteRoleById(Integer rid) {
        return roleDao.deleteByPrimaryKey(rid);
    }

    public role findRoleById(int id){
        String key = "role_" + id;

        ValueOperations<String, role> operations = redisTemplate.opsForValue();

        boolean hasKey = redisTemplate.hasKey(key);
        if (hasKey) {
            role role = operations.get(key);
            System.out.println("==========从缓存中获得数据=========");
            System.out.println(role.getName());
            System.out.println("==============================");
            return role;
        } else {
            role role = roleDao.selectByPrimaryKey(id);
            System.out.println("==========从数据表中获得数据=========");
            System.out.println(role.getName());
            System.out.println("==============================");

            // 写入缓存
            operations.set(key, role, 5, TimeUnit.HOURS);
            return role;
        }
    }

    /**
     * 更新策略：先更新数据表，成功之后，删除原来的缓存，再更新缓存
     */
    public int updateRole(role role){
        ValueOperations<String, role> operations = redisTemplate.opsForValue();
        int result = roleDao.updateByPrimaryKey(role);

        if(result > 0){
            String key = "role_"+ role.getId();
            boolean haskey = redisTemplate.hasKey(key);
            if(haskey){
                redisTemplate.delete(key);
                System.out.println("更新缓存..."+key);
            }
            //将更新的数据加入缓存中
            role NewRole = roleDao.selectByPrimaryKey(role.getId());
            if(NewRole != null){
                //对应setex命令 将值 value 关联到 key ，并将 key 的生存时间设为 seconds (以秒为单位)
                operations.set(key,NewRole,3,TimeUnit.HOURS);
            }
        }
        return result;
    }

    /**
     * 删除策略：删除数据表中数据，然后删除缓存
     */
    public int deleteRole(int id){
        ValueOperations<String, role> operations = redisTemplate.opsForValue();
        int res = roleDao.deleteByPrimaryKey(id);
        String key = "role_"+id;
        if(res > 0){
            boolean haskey = redisTemplate.hasKey(key);
            if(haskey){
                redisTemplate.delete(key);
                System.out.println("删除缓存..."+key);
            }
        }
        return res;
    }
}
