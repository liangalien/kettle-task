package com.liangalien.kt.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class QuartzDetailDTO {
    private BigInteger id;
    private String name;
    private String groupName;
    private String description;
    private String cron;
    private String bean;
    private String method;
    private String params;
    private int disabled;
    private String createBy;
    private String updateBy;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public JSONObject getParams() {
        return this.params == null ? null : JSONObject.parseObject(this.params);
    }

    public void setParams(JSONObject params) {
        if (params != null && !"".equals(params)) {
            this.params = params.toJSONString();
        }
    }
}
