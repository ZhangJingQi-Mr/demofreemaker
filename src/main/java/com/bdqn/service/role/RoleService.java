package com.bdqn.service.role;

import com.bdqn.pojo.Role;

import java.util.List;

/**
 * Created by Administrator on 2020/5/28.
 */
public interface RoleService {

    //获取角色列表
    public List<Role> getRoleList() throws Exception;


}
