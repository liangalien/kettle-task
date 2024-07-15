package com.liangalien.kt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;


@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)  // 使用小写下划线
public class RunnerDTO {
    private BigInteger id;
    private BigInteger taskId;
    private String projectKey;
    private String taskName;
    private String repoName;
    private String repoType;
    private int trigger;
    private int status;
    private String createBy;
    private String createByName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
    private long durations;
    private String key;

    public String getKey() {
        return String.format("%s-%s-%s-%s", projectKey.toLowerCase(), repoType, taskId, id);
    }
}
