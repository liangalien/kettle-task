package com.liangalien.kt.dao;

import com.liangalien.kt.dto.ProjectVarDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;


@Mapper
public interface ProjectVarDAO {
    void insert(ProjectVarDTO dto);
    void update(ProjectVarDTO dto);
    void inserts(@Param("items") List<ProjectVarDTO> items);
    void remove(@Param("projectId") BigInteger projectId);
    List<ProjectVarDTO> selectAll(@Param("projectId") BigInteger projectId, @Param("excludeDeleted") boolean excludeDeleted);
}
