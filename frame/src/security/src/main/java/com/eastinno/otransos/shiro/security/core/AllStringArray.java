package com.eastinno.otransos.shiro.security.core;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 
*@author dll 作者 E-mail：dongliangliang@teleinfo.cn 
*@date 创建时间：2017年1月18日 下午1:31:11
*@version 1.0
*@parameter
*@since
*@return 
*/

public class AllStringArray {
	 // 将NUM设置为待排列数组的长度即实现全排列
    private static int NUM = 6;
    private static final String str = "2c3a1338403d6b9c7450cee7a511637d";
    private static final String salt = "86fd6fddd0a7c0bb310fa7f5761bba46";
    /**
     * 递归算法：将数据分为两部分，递归将数据从左侧移右侧实现全排列
     * 
     * @param datas
     * @param target
     */
    private static void sort(List datas, List<String> target) {
    	ShiroUtils su = new ShiroUtils();
        if (target.size() == NUM) {
            for (String obj : target){
            	 String strs = su.getPassWord(obj, salt);
             	if(strs.equals(str)){
             		System.out.println(obj);
             	}
            }
            return;
        }
        for (int i = 0; i < datas.size(); i++) {
            List newDatas = new ArrayList(datas);
            List newTarget = new ArrayList(target);
            newTarget.add(newDatas.get(i));
            newDatas.remove(i);
            sort(newDatas, newTarget);
        }
    }

    public static void main(String[] args) {
        String[] datas = new String[] {"0","1","2","3","4","5","6","7","8","9","a", "b", "c", "d", "e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z" };
        ArrayList temp = new ArrayList();
        // asList用法将数组看做列表
        sort(Arrays.asList(datas), temp);
    }
}
