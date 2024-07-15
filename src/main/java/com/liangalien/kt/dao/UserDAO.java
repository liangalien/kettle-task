package com.liangalien.kt.dao;

import com.liangalien.kt.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;



@Mapper
public interface UserDAO {
    UserDTO selectByName(@Param("username") String username);

    void insert(UserDTO dto);
}
