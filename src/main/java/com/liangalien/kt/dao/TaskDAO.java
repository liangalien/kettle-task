package com.liangalien.kt.dao;


import com.liangalien.kt.dto.TaskDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Mapper
public interface TaskDAO {
    void insert(TaskDTO dto);
    void update(TaskDTO dto);

    void remove(@Param("id") BigInteger id, @Param("updateBy") String updateBy);
    void removeByRepoId(@Param("repoId") BigInteger repoId, @Param("updateBy") String updateBy);

    void updateStatus(@Param("id") BigInteger id, @Param("status") int status);

    TaskDTO selectById(@Param("id") BigInteger id);
    List<TaskDTO> selectAll(@Param("search") Map<String, Object> search);

}
