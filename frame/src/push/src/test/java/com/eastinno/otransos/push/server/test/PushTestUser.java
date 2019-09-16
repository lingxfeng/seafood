package com.eastinno.otransos.push.server.test;

import java.io.Serializable;

public class PushTestUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int age;

    public PushTestUser() {

    }

    public PushTestUser(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

}