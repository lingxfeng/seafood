package com.test;

import com.eastinno.otransos.security.domain.Permission;
import com.eastinno.otransos.security.domain.Role;
import com.eastinno.otransos.security.domain.SystemMenu;

public class CreateResource {
	public BeanUtils beanUtil = new BeanUtils();
	public static void main(String[] args) throws Exception{
		CreateResource c =new CreateResource();
		c.creatFile(Role.class);
		c.creatFile(SystemMenu.class);
		c.creatFile(Permission.class);
	}
	public void creatFile(Class clazz) throws Exception{
		createDao(clazz);
		createIService(clazz);
		createServiceImpl(clazz);
		createAction(clazz);
		crateListHtml(clazz);
		crateEditHtml(clazz);
		createJs(clazz);
	}
	public void createDao(Class clazz) throws Exception{
		String filePath = clazz.getName().replace(".", "/");
		filePath = System.getProperty("user.dir") + "/src/main/java/"+filePath.substring(0,(filePath.indexOf("domain")))+"dao/";
		beanUtil.createFile(clazz,filePath, "I"+clazz.getSimpleName()+"DAO.java", BeanUtils.daoUrl);
	}
	public void createIService(Class clazz) throws Exception{
		String filePath = clazz.getName().replace(".", "/");
		filePath = System.getProperty("user.dir") + "/src/main/java/"+filePath.substring(0,(filePath.indexOf("domain")))+"service/";
		beanUtil.createFile(clazz,filePath, "I"+clazz.getSimpleName()+"Service.java", BeanUtils.IServiceUrl);
	}
	public void createServiceImpl(Class clazz) throws Exception{
		String filePath = clazz.getName().replace(".", "/");
		filePath = System.getProperty("user.dir") + "/src/main/java/"+filePath.substring(0,(filePath.indexOf("domain")))+"service/impl/";
		beanUtil.createFile(clazz,filePath, clazz.getSimpleName()+"ServiceImpl.java", BeanUtils.ServiceImplUrl);
	}
	public void createAction(Class clazz) throws Exception{
		String filePath = clazz.getName().replace(".", "/");
		filePath = System.getProperty("user.dir") + "/src/main/java/"+filePath.substring(0,(filePath.indexOf("domain")))+"action/";
		beanUtil.createFile(clazz,filePath, clazz.getSimpleName()+"Action.java", BeanUtils.actionUrl);
	}
	public void crateListHtml(Class clazz)throws Exception{
		String filePath = clazz.getName().replace(".", "/");
		filePath = System.getProperty("user.dir") + "/src/main/webapp/WEB-INF/views/shopmanage/trade/"+clazz.getSimpleName()+"/";
		beanUtil.createFile2(clazz,filePath, clazz.getSimpleName()+"List.html", BeanUtils.listHtmlUrl);
	}
	public void crateEditHtml(Class clazz)throws Exception{
		String filePath = clazz.getName().replace(".", "/");
		filePath = System.getProperty("user.dir") + "/src/main/webapp/WEB-INF/views/shopmanage/trade/"+clazz.getSimpleName()+"/";
		beanUtil.createFile2(clazz,filePath, clazz.getSimpleName()+"Edit.html", BeanUtils.EditHtmlUrl);
	}
	public void createJs(Class clazz)throws Exception{
		String filePath = clazz.getName().replace(".", "/");
		filePath = System.getProperty("user.dir") + "/src/main/webapp/WEB-INF/temp/";
		beanUtil.createFile3(clazz,filePath, clazz.getSimpleName()+"ManagePanel.js", BeanUtils.JSUrl);
	}
}
