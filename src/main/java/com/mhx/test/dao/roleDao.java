package com.mhx.test.dao;

import com.mhx.test.entity.role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @Repository需要在Spring中配置扫描地址，然后生成Dao层的Bean才能被注入到Service层中。
 *
 * @Mapper不需要配置扫描地址，通过xml里面的namespace里面的接口地址，生成了Bean后注入到Service层中。
 */
@Mapper
public interface roleDao {

    int deleteByPrimaryKey(Integer id);

    int insert(role record);

    int insertSelective(role record);

    role selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(role record);

    int updateByPrimaryKey(role record);

    List<role> getAllRoles();

}
