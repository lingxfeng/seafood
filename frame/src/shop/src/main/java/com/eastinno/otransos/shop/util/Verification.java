package com.eastinno.otransos.shop.util;

import java.util.regex.Pattern;

public class Verification {
	public static boolean checkName(String name) {
        String regex = "^[a-zA-Z0-9\u4e00-\u9fa5]{4,20}$";
        return Pattern.matches(regex, name);
    }
	
	public static boolean checkPassword(String password) {
        String regex = "^[A-Za-z0-9]{6,18}+$";
        return Pattern.matches(regex, password);
    }
	
	public static void main(String[] args){
		System.out.println(checkName("cl01"));
	}
}
