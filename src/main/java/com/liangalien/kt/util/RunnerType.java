package com.liangalien.kt.util;

public enum RunnerType {
    TRANS("trans", "转换"),
    JOB("job", "作业");

    private String value;
    private String desc;
    RunnerType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public String getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }
}
