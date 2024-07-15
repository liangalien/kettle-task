package com.liangalien.kt.dao;

import com.liangalien.kt.dto.ProjectDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


@Mapper
public interface ProjectDAO {
    ProjectDTO selectById(@Param("id") BigInteger id);
    void insert(ProjectDTO dto);
    void update(ProjectDTO dto);
    void remove(@Param("id") BigInteger id, @Param("updateBy") String updateBy);

    List<ProjectDTO> selectAll(@Param("search") Map<String, Object> search);
}
