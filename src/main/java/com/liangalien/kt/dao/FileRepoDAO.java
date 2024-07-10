package com.liangalien.kt.dao;

import com.liangalien.kt.dto.FileRepoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


@Mapper
public interface FileRepoDAO {
    void insert(FileRepoDTO dto);
    void update(FileRepoDTO dto);
    void remove(@Param("id") BigInteger id, @Param("updateBy") String updateBy);
    FileRepoDTO selectById(@Param("id") BigInteger id);
    FileRepoDTO selectByName(@Param("projectId") BigInteger projectId, @Param("fileName") String fileName);
    List<FileRepoDTO> selectAll(@Param("search") Map<String, Object> search);
}
