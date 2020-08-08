package com.bdqn.service.user;


import com.bdqn.pojo.User;
import com.bdqn.utils.PageSupport;

public interface UserService {

    //登录
    public User login(String userCode, String userPassword) throws Exception;

    //获取用户列表实现分页
    public void getUsersPage(PageSupport pageSupport, String userName, int roleId) throws Exception;

    //添加用户
    public int addUser(User user) throws Exception;

    //获取用户id
    public User getUserById(int uid) throws Exception;

    //通过用户id修改用户
    public int modifyUser(User user) throws Exception;

    //删除用户
    public int deleteUser(int id) throws Exception;

    //通过用户编码获取用户
    public User getUserByUserCode(String userCode) throws Exception;
}
