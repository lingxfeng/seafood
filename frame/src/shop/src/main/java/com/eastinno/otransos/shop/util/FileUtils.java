package com.eastinno.otransos.shop.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtils {
	public void InputAndCover(String path,String string) throws IOException{
		try{
			File f = new File(path);
		    if (f.exists()){
		    	f.delete();
		    }
		   f.createNewFile();
		   FileOutputStream fos = new FileOutputStream(f);
		   PrintWriter pw = new PrintWriter(fos);
		   pw.write(string);
		   pw.flush();
		   fos.close();
		   pw.close();		
		}catch (Exception e){
			System.out.println("=========="+e+"========");
		}
	}
	public void InputNoCover(String path) throws IOException{
		FileWriter fileWriter=new FileWriter(path);
		   int [] a=new int[]{11112,222,333,444,555,666};
		   for (int i = 0; i < a.length; i++) {
		    fileWriter.write(String.valueOf(a[i])+" ");
		     }
		   fileWriter.flush();
		   fileWriter.close();
	}
	public String Read(String path){
		File file = new File(path);
		String result = "";
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
			String s = null;
			while((s = br.readLine())!=null){//使用readLine方法，一次读一行
			result = result + "\n" +s;
		}
			br.close();    
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
}
