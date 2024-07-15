package com.liangalien.kt.controller;


import com.liangalien.kt.dto.UserDTO;
import com.liangalien.kt.service.UserService;
import com.liangalien.kt.util.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/login")
    public Response login(@RequestBody Map<String, String> body) {
        return Response.success(userService.login(body.get("username"), body.get("password")));
    }

    @PostMapping("/register")
    public Response register(@RequestBody UserDTO body) {
        userService.register(body);
        return Response.success();
    }
}
