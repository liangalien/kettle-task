package com.liangalien.kt.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;


@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)  // 使用小写下划线
public class ProjectDTO {
    private BigInteger id;
    private String key;
    private String name;
    private String createBy;
    private String updateBy;
    private String createByName;
    private String updateByName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    private int isDeleted;
}
