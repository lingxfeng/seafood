package com.eastinno.otransos.seafood.promotions.action;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/** 
*@author dll 作者 E-mail：dongliangliang@teleinfo.cn 
*@date 创建时间：2017年12月28日 上午10:51:20
*@version 1.0
*@parameter
*@since
*@return 
*/

public class UploadVideo extends HttpServlet{
	//上传路径  
    private File uploadPath;  
    //当文件过大时，需要设置一个临时路径  
    private File tempPath;  
      
    public void doPost(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException {  
          
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        // 内存存储的最大值  
        factory.setSizeThreshold(4096);  
          
        factory.setRepository(tempPath);  
  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        //设置文件上传大小  
        upload.setSizeMax(1000000 * 2000);  
        try {  
            List fileItems = upload.parseRequest(req);  
            String itemNo = "";  
            for (Iterator iter = fileItems.iterator(); iter.hasNext();) {  
                FileItem item = (FileItem) iter.next();
                //是普通的表单输入域  
                if(item.isFormField()) {  
                    if ("itemNo".equals(item.getFieldName())) {  
                        itemNo = item.getString();  
                    }  
                }  
                //是否为input="type"输入域  
                if (!item.isFormField()) {  
                    String fileName = item.getName();  
                    long size = item.getSize();  
                    if ((fileName == null || fileName.equals("")) && size == 0) {  
                        continue;  
                    }  
                    //截取字符串 如：C:\WINDOWS\Debug\PASSWD.LOG  
                    fileName = fileName.substring(fileName.lastIndexOf("\\") + 1, fileName.length());
                    item.write(new File(uploadPath, itemNo + fileName+".mp4"));  
                }  
            }
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    /** 
     * 初始化方法，设定目录 
     */  
    public void init() throws ServletException {  
        uploadPath = new File(getServletContext().getRealPath("upload"));  
        //System.out.println("uploadPath=====" + uploadPath);  
        //如果目录不存在  
        if (!uploadPath.exists()) {  
            //创建目录  
            uploadPath.mkdir();  
        }
        //临时目录  
        tempPath = new File(getServletContext().getRealPath("temp"));  
        if (!tempPath.exists()) {  
            tempPath.mkdir();  
        }  
    }  
}
