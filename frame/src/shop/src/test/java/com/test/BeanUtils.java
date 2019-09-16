package com.test;

import java.io.File;
import java.io.FileWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * 自动生成文件工具
 * @author sasa
 *
 */
public class BeanUtils {
	public static String daoUrl = "src/test/resources/daotemplate.java.vm";
	public static String IServiceUrl="src/test/resources/IserviceTemplate.java.vm";
	public static String ServiceImplUrl = "src/test/resources/serviceImplTemplate.java.vm";
	public static String actionUrl = "src/test/resources/actionTemplate.java.vm";
	public static String listHtmlUrl = "src/test/resources/list.html.vm";
	public static String EditHtmlUrl = "src/test/resources/edit.html.vm";
	public static String JSUrl = "src/test/resources/jsManagePanel.js.vm";
	public void createFile(Class<?> c,String toUrl,String fileName,String vmPath)throws Exception{
		File filePath = new File(toUrl);
		createFilePath(filePath);
		fileName = toUrl+fileName;
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		String pacakageName = c.getName().substring(0,(c.getName().indexOf("domain")-1));
		String domainName = c.getSimpleName();
		fw.write(createCode(vmPath,pacakageName,domainName));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}
	public void createFile2(Class<?> c,String toUrl,String fileName,String vmPath)throws Exception{
		File filePath = new File(toUrl);
		createFilePath(filePath);
		fileName = toUrl+fileName;
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		String pacakageName = c.getName().substring(0,(c.getName().indexOf("domain")-1));
		String domainName = c.getSimpleName();
		Field[] fs = c.getDeclaredFields();
		fw.write(createCode(vmPath,pacakageName,domainName,fs));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}
	public void createFile3(Class<?> c,String toUrl,String fileName,String vmPath)throws Exception{
		File filePath = new File(toUrl);
		createFilePath(filePath);
		fileName = toUrl+fileName;
		File file = new File(fileName);
		FileWriter fw = new FileWriter(file);
		String pacakageName = c.getName().substring(0,(c.getName().indexOf("domain")-1));
		String domainName = c.getSimpleName();
		Field[] fs = c.getDeclaredFields();
		fw.write(createCode3(vmPath,pacakageName,domainName,fs));
		fw.flush();
		fw.close();
		showInfo(fileName);
	}
	/**
	 * 根据模板生成代码
	 * 
	 * @param fileVMPath
	 *            模板路径
	 * @param bean
	 *            目标bean
	 * @param annotation
	 *            注释
	 * @return
	 * @throws Exception
	 */
	public String createCode(String fileVMPath,String pacakageName,String domainName)
			throws Exception {
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("input.encoding", "UTF-8");
		velocityEngine.setProperty("output.encoding", "UTF-8");
		velocityEngine.init();
		Template template = velocityEngine.getTemplate(fileVMPath);
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("packageName", pacakageName);
		velocityContext.put("domainName", domainName);
		velocityContext.put("idType", "Long");
		velocityContext.put("domain", domainName.substring(0, 1).toLowerCase()+domainName.substring(1));
		velocityContext.put("id", "Id");
		StringWriter stringWriter = new StringWriter();
		template.merge(velocityContext, stringWriter);
		return stringWriter.toString();
	}
	public String createCode(String fileVMPath,String pacakageName,String domainName,Field[] fs)
			throws Exception {
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("input.encoding", "UTF-8");
		velocityEngine.setProperty("output.encoding", "UTF-8");
		velocityEngine.init();
		Template template = velocityEngine.getTemplate(fileVMPath);
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("packageName", pacakageName);
		velocityContext.put("domainName", domainName);
		velocityContext.put("idType", "Long");
		velocityContext.put("fs",fs);
		velocityContext.put("domain", domainName.substring(0, 1).toLowerCase()+domainName.substring(1));
		velocityContext.put("id", "Id");
		StringWriter stringWriter = new StringWriter();
		template.merge(velocityContext, stringWriter);
		return stringWriter.toString();
	}
	public String createCode3(String fileVMPath,String pacakageName,String domainName,Field[] fs)
			throws Exception {
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("input.encoding", "UTF-8");
		velocityEngine.setProperty("output.encoding", "UTF-8");
		velocityEngine.init();
		Template template = velocityEngine.getTemplate(fileVMPath);
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("packageName", pacakageName);
		velocityContext.put("domainName", domainName);
		velocityContext.put("idType", "Long");
		String filedsStr="[";
		for(Field f:fs){
			filedsStr += "\""+f.getName()+"\",";
		}
		filedsStr = filedsStr.substring(0, filedsStr.length()-1);
		filedsStr += "]";
		velocityContext.put("fStr", filedsStr);
		velocityContext.put("fs",fs);
		velocityContext.put("domain", domainName.substring(0, 1).toLowerCase()+domainName.substring(1));
		velocityContext.put("id", "Id");
		StringWriter stringWriter = new StringWriter();
		template.merge(velocityContext, stringWriter);
		return stringWriter.toString();
	}

	/**
	 * 创建文件
	 * 
	 * @param file
	 */
	public void createFilePath(File file) {
		if (!file.exists()) {
			System.out.println("创建[" + file.getAbsolutePath() + "]情况："
					+ file.mkdirs());
		} else {
			System.out.println("存在目录：" + file.getAbsolutePath());
		}
	}

	/**
	 * 获取路径的最后面字符串<br>
	 * 如：<br>
	 * <code>str = "com.b510.base.bean.User"</code><br>
	 * <code> return "User";<code>
	 * 
	 * @param str
	 * @return
	 */
	public String getLastChar(String str) {
		if ((str != null) && (str.length() > 0)) {
			int dot = str.lastIndexOf('.');
			if ((dot > -1) && (dot < (str.length() - 1))) {
				return str.substring(dot + 1);
			}
		}
		return str;
	}

	/**
	 * 把第一个字母变为小写<br>
	 * 如：<br>
	 * <code>str = "UserDao";</code><br>
	 * <code>return "userDao";</code>
	 * 
	 * @param str
	 * @return
	 */
	public String getLowercaseChar(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	/**
	 * 显示信息
	 * 
	 * @param info
	 */
	public void showInfo(String info) {
		System.out.println("创建文件：" + info + "成功！");
	}

	/**
	 * 获取系统时间
	 * 
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date());
	}
}
