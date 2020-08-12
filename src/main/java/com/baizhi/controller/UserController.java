package com.baizhi.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.baizhi.entity.User;
import com.baizhi.service.UserService;
import com.baizhi.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jin
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 根据用户名，密码进行认证
     * @param user
     * @return
     */
    @GetMapping("/user/login")
    public Map<String,Object> login(User user){
        log.info("用户名: [{}]",user.getName());
        log.info("密码: [{}]",user.getPassword());
        Map<String, Object> map = new HashMap<>();
        try{
            //根据用户名密码查询数据库
            User userDB = userService.login(user);
            //根据map生成JWT令牌
            Map<String,String> payload =  new HashMap<>();
            payload.put("id",userDB.getId());
            payload.put("name",userDB.getName());
            //生成JWT的令牌
            String token = JWTUtils.getToken(payload);
            //响应token给客户端
            map.put("state",true);
            map.put("msg","认证成功");
            map.put("token",token);
        }catch (Exception e){
            map.put("state",false);
            map.put("msg",e.getMessage());
        }
        return map;
    }


    @PostMapping("/user/test")
    public Map<String,Object> test(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        //处理自己业务逻辑
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtils.verify(token);
        log.info("用户id: [{}]",verify.getClaim("id").asString());
        log.info("用户name: [{}]",verify.getClaim("name").asString());
        map.put("state",true);
        map.put("msg","请求成功!");
        return map;
    }


}
