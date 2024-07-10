package com.liangalien.kt.dao;

import com.liangalien.kt.dto.RunnerDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;


@Mapper
public interface RunnerDAO {
    void insert(RunnerDTO dto);

    void update(RunnerDTO dto);


    RunnerDTO selectById(@Param("id") BigInteger id);
    List<RunnerDTO> selectAll(@Param("search") Map<String, Object> search);
}
