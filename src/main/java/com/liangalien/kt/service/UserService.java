package com.liangalien.kt.service;

import com.liangalien.kt.dao.UserDAO;
import com.liangalien.kt.dto.UserDTO;
import com.liangalien.kt.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDAO userDao;

    @Autowired
    private JWTUtil jwtUtil;

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDTO user = userDao.selectByName(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public UserDTO login(String username, String password) {
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new RuntimeException("用户名或密码错误", e);
        } catch (Exception e) {
            throw new RuntimeException("系统错误", e);
        }

        UserDTO loginUser = (UserDTO) authentication.getPrincipal();
        loginUser.setToken(jwtUtil.encode(loginUser));
        loginUser.setPassword(null);

        return loginUser;
    }


    public void register(UserDTO userDto) {
        if (userDao.selectByName(userDto.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));

        userDao.insert(userDto);
    }
}