package com.liangalien.kt.dao;

import com.liangalien.kt.dto.ProjectDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;


@Mapper
public interface ProjectDAO {
    ProjectDTO selectById(@Param("id") BigInteger id);
}
