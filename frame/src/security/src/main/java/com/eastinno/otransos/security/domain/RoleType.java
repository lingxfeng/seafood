package com.eastinno.otransos.security.domain;

public enum RoleType {
    User("系统用户"), Supplier("供应商"), Customer("客户");

    private String chineseName;

    private RoleType() {
    }

    private RoleType(String name) {
        this.chineseName = name;
    }

    public String getText() {
        return this.chineseName;
    }

    public int getValue() {
        return ordinal();
    }

    public String toString() {
        return this.chineseName;
    }
}
