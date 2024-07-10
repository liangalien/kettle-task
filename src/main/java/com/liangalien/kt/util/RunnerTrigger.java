package com.liangalien.kt.util;

public enum RunnerTrigger {
    MANUAL(1, "手动触发"),
    AUTO(2, "自动触发");

    private int value;
    private String desc;
    RunnerTrigger(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public int getValue() {
        return this.value;
    }

    public String getDesc() {
        return this.desc;
    }
}
