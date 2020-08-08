package com.bdqn.mapper.role;

import com.bdqn.pojo.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface RoleMapper {

	// 	查询角色列表
	public List<Role> getRoleList()throws Exception;

}
