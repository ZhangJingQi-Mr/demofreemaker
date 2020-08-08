package com.bdqn.controller;

import com.alibaba.fastjson.JSON;
import com.bdqn.pojo.Role;
import com.bdqn.pojo.User;
import com.bdqn.service.role.RoleService;
import com.bdqn.service.user.UserService;
import com.bdqn.utils.Constants;
import com.bdqn.utils.PageSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.*;

/**
 * Created by Administrator on 2020/5/27.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    /**
     *  测试通过id查找用户列表
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/view/{uid}",method = RequestMethod.GET)
    public Object view2(@PathVariable("uid") Integer id) throws Exception {
        User user = userService.getUserById(id);
        return user;
    }

    /**
     *  测试页面展示字符串
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/test")
    public String test() throws Exception {
        List<Role> roleList = roleService.getRoleList();
        return JSON.toJSONString(roleList);
    }

    /**
     * 校验用户编码是否存在
     * @param userCode
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/ucexist",method = RequestMethod.GET)
    public String ucexist(@RequestParam("userCode") String userCode) throws Exception {
        User user = userService.getUserByUserCode(userCode);
        Map<String,String> map = new HashMap<>();
        if(user != null){
            map.put("userCode","exist");
        }else {
            map.put("userCode","notExist");
        }
        return JSON.toJSONString(map);
    }

    /**
     * 删除用户
     * @param id
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/{uid}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable("uid") Integer id) throws Exception {
        int count = userService.deleteUser(id);
        Map<String, String> map = new HashMap<>();
        if(count>0){
            map.put("delResult", "true");
        }else{
            map.put("delResult", "false");
        }
        return JSON.toJSONString(map);
    }

    /**
     * 查询用户
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{uid}",method = RequestMethod.GET)
    public String view(@PathVariable("uid") Integer id, Model model) throws Exception {
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        return "/user/userview";
    }

    /**
     * 到修改界面
     * @param id
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/toUserModify",method = RequestMethod.GET)
    public String toUserModify(@RequestParam(value = "uid",required = false) Integer id,
                               Model model) throws Exception {
        User user = userService.getUserById(id);
        model.addAttribute("user",user);
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList",roleList);
        return "/user/usermodify";
    }

    /**
     * 修改用户
     * @param user
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping("/modify")
    public String userModify(User user,
                             @RequestParam(value = "uid",required = false) Integer id) throws Exception {
        //通过用户id获取用户信息
        User userOriginal = userService.getUserById(id);
        //更新用户新
        userOriginal.setUserName(user.getUserName());
        userOriginal.setGender(user.getGender());
        userOriginal.setBirthday(user.getBirthday());
        userOriginal.setPhone(user.getPhone());
        userOriginal.setAddress(user.getAddress());
        userOriginal.setUserRole(user.getUserRole());
        int count = userService.modifyUser(userOriginal);
        if(count>0){
            return "redirect: /user/list";
        }else{
            return "/toUserModify";
        }
    }

    /**
     * 到添加界面
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/toAddUser")
    public String toAddUser(Model model) throws Exception {
        List<Role> roleList = roleService.getRoleList();
        model.addAttribute("roleList",roleList);
        return "/user/useradd";
    }

    /**
     * 添加用户
     * @param user
     * @param session
     * @return
     * @throws Exception
     */
    @RequestMapping(value = {"/",""}, method = RequestMethod.POST)
    public String userAdd(User user , HttpSession session,
                          HttpServletRequest request,
                          @RequestParam(value = "a_idPicPath", required = false) MultipartFile file) throws Exception {
        user.setCreationDate(new Date());
        User sessionUser =(User) session.getAttribute(Constants.USER_SESSION);
        if(sessionUser != null){
            user.setCreatedBy(sessionUser.getId());
        }
        //保存图片
        if(!file.isEmpty()){
            //生成图片路径
            String path = session.getServletContext().getRealPath("/upload");
            File filePath = new File(path);
            //判断路径是否存在
            if(!filePath.exists()){
                //创建目录
                filePath.mkdirs();
            }
            //1.创建文件名
            String oldName = file.getOriginalFilename();
            String suffix = oldName.substring(oldName.lastIndexOf(".")+1,oldName.length());//原文件后缀
            //String newFileName = System.currentTimeMillis()+ RandomUtils.nextInt(1000,9999)+suffix;
            String newFileName = UUID.randomUUID().toString()+"."+suffix;
            System.out.println("===========================================newFilePath:"+path+File.separator+newFileName);
            file.transferTo(new File(path+File.separator+newFileName));

            //第2步：给user的idPacPath赋值
            String protocal = request.getScheme();
            String serverName = request.getServerName();
            int port = request.getServerPort();
            String appName = request.getContextPath();
            user.setIdPicPath(protocal+"://"+serverName+":"+port+File.separator+appName+File.separator+"upload"+File.separator+newFileName);
        }
        int count = userService.addUser(user);
        if(count>0){
            return "redirect: /user/list";
        }else {
            return "/toAddUser";
        }
    }

    /**
     * 用户列表
     * @param pageIndex
     * @param userName
     * @param roleId
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("/list")
    public String userlist(@RequestParam(value = "pageIndex",required = false) Integer pageIndex,
                           @RequestParam(value = "queryname",required = false)String userName,
                           @RequestParam(value = "queryUserRole",required = false)Integer roleId,
                           Model model) throws Exception {
        PageSupport pageSupport = new PageSupport();
        if(pageIndex != null){
            pageSupport.setCurrentPageNo(pageIndex);
        }
        if(roleId==null){
            roleId = 0;
        }
        userService.getUsersPage(pageSupport, userName, roleId);
        List<Role> roleList = roleService.getRoleList();
        //把数据返回给页面
        model.addAttribute("roleList",roleList);
        model.addAttribute("queryUserName",userName);
        model.addAttribute("queryUserRole",roleId);
        model.addAttribute("userList", pageSupport.getList());
        model.addAttribute("totalPageCount", pageSupport.getTotalPageCount());
        model.addAttribute("totalCount", pageSupport.getTotalCount());
        model.addAttribute("currentPageNo", pageSupport.getCurrentPageNo());
        return "/userList";
    }
}
