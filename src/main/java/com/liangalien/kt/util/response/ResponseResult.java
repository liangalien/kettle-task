package com.liangalien.kt.util.response;

public enum ResponseResult {
    SUCCESS(true),
    FAIL(false);

    private boolean value;
    ResponseResult(boolean value) {
        this.value = value;
    }
    public boolean getValue() {
        return value;
    }
}
