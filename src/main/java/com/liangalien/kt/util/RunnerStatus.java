package com.liangalien.kt.util;

public enum RunnerStatus {
    INIT(0, "未开始"),
    WAITING(1, "等待中"),
    READY(2, "已就绪"),
    RUNNING(3, "执行中"),
    SUCCESS(4, "成功"),
    FAILED(5, "失败"),
    STOPPED(6, "停止"),
    ERROR(7, "错误"),
    OTHER(99, "其它");

    private int value;
    private String desc;
    RunnerStatus(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
