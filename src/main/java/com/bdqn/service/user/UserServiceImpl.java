package com.bdqn.service.user;

import com.bdqn.mapper.user.UserMapper;
import com.bdqn.pojo.User;
import com.bdqn.utils.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 登录
     * @param userCode
     * @param userPassword
     * @return
     * @throws Exception
     */
    @Override
    public User login(String userCode, String userPassword) throws Exception {
        return userMapper.getLoginUser(userCode,userPassword);
    }

    /**
     * 获取用户列表实现分页
     * @param pageSupport
     * @param userName
     * @param roleId
     * @throws Exception
     */
    @Override
    public void getUsersPage(PageSupport pageSupport, String userName, int roleId) throws Exception {
        int userCount = userMapper.getUserCount(userName, roleId);
        //1. 获取总页数
        int totalPage = userCount% pageSupport.getPageSize() == 0?
                userCount/pageSupport.getPageSize() : userCount/pageSupport.getPageSize()+1;
        //页码的兼容性处理
        if(pageSupport.getCurrentPageNo() > totalPage){
            pageSupport.setCurrentPageNo(totalPage);
        }else if(pageSupport.getCurrentPageNo()<1) {
            pageSupport.setCurrentPageNo(1);
        }
        //2. 获取当前页的用户列表
        List<User> userList = userMapper.getUserList(userName, roleId, (pageSupport.getCurrentPageNo()-1)*pageSupport.getPageSize(), pageSupport.getPageSize());
        pageSupport.setList(userList);
        //把总页码放入pageSupport
        pageSupport.setTotalCount(userCount);
        pageSupport.setTotalPageCount(totalPage);
    }

    /**
     * 添加用户
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public int addUser(User user) throws Exception {
        return userMapper.add(user);
    }

    /**
     * 获取用户id
     * @param uid
     * @return
     * @throws Exception
     */
    @Override
    public User getUserById(int uid) throws Exception {
        return userMapper.getUserById(String.valueOf(uid));
    }

    /**
     * 修改用户
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public int modifyUser(User user) throws Exception {
        return userMapper.modify(user);
    }

    /**
     * 删除用户
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public int deleteUser(int id) throws Exception {
        return userMapper.deleteUserById(id);
    }

    /**
     * 通过用户编码获取用户
     * @param userCode
     * @return
     * @throws Exception
     */
    @Override
    public User getUserByUserCode(String userCode) throws Exception {
        return userMapper.getUserByUserCode(userCode);
    }
}
