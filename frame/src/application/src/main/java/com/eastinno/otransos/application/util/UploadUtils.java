package com.eastinno.otransos.application.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;

import com.eastinno.otransos.application.core.domain.Attachment;

public class UploadUtils {

    /**
     * 如何得到上传的文件名, API没有提供直接的方法，只能从content-disposition属性中获取
     * 
     * @param part
     * @return
     */
    public static String getFileName(Part part) {
        if (part == null)
            return null;

        String fileName = part.getHeader("content-disposition");
        if (StringUtils.isBlank(fileName)) {
            return null;
        }

        return StringUtils.substringBetween(fileName, "filename=\"", "\"");
    }

    public static Attachment upload(HttpServletRequest request, MultipartConfig config) throws IOException {
        request.setCharacterEncoding("UTF-8");

        Part part = null;
        try {
            // <input name="file" size="50" type="file" />
            part = request.getPart("file");
        } catch (IllegalStateException ise) {
            // 上传文件超过注解所标注的maxRequestSize或maxFileSize值
            if (config.maxRequestSize() == -1L) {
                System.out.println("the Part in the request is larger than maxFileSize");
            } else if (config.maxFileSize() == -1L) {
                System.out.println("the request body is larger than maxRequestSize");
            } else {
                System.out.println("the request body is larger than maxRequestSize, or any Part in the request is larger than maxFileSize");
            }
            // TODO
            // forwardErrorPage(request, response, "上传文件过大，请检查输入是否有误！");
            return null;
        } catch (IOException ieo) {
            // 在接收数据时出现问题
            System.out.println("I/O error occurred during the retrieval of the requested Part");
        } catch (Exception e) {
            System.out.println(e.toString());
            e.printStackTrace();
        }

        if (part == null) {
            // TODO
            // forwardErrorPage(request, response, "上传文件出现异常，请检查输入是否有误！");
            return null;
        }

        // 得到文件的原始名称，eg ：测试文档.pdf
        String fileName = getFileName(part);
        System.out.println();
        System.out.println("contentType : " + part.getContentType());
        System.out.println("fileName : " + fileName);
        System.out.println("fileSize : " + part.getSize());
        System.out.println("header names : ");

        for (String headerName : part.getHeaderNames()) {
            System.out.println(headerName + " : " + part.getHeader(headerName));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String name = UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);
        String datePath = sdf.format(new Date());
        String saveName = datePath + "/" + name;

        System.out.println("save the file with new name : " + saveName);

        // 因在注解中指定了路径，这里可以指定要写入的文件名
        // 在未执行write方法之前，将会在注解指定location路径下生成一临时文件
        part.write(saveName);
        Attachment br = new Attachment();
        br.setCreateTime(new Date());
        br.setOldName(fileName);
        br.setFileName(name);
        br.setExt(FilenameUtils.getExtension(fileName));
        br.setPath(config.location() + datePath);
        br.setStatus(1);
        return br;
    }

    public static List<Attachment> uploads(HttpServletRequest request, MultipartConfig config) throws IOException {

        request.setCharacterEncoding("UTF-8");

        Collection<Part> parts = null;
        try {
            parts = request.getParts();
        } catch (IllegalStateException ise) {
            // 可能某个文件大于指定文件容量maxFileSize，或者提交数据大于maxRequestSize
            System.out
                    .println("maybe the request body is larger than maxRequestSize, or any Part in the request is larger than maxFileSize");
        } catch (IOException ioe) {
            // 在获取某个文件时遇到拉IO异常错误
            System.out.println("an I/O error occurred during the retrieval of the Part components of this request");
        } catch (Exception e) {
            System.out.println("the request body is larger than maxRequestSize, or any Part in the request is larger than maxFileSize");
            e.printStackTrace();
        }

        if (parts == null || parts.isEmpty()) {
            // doError(request, response, "上传文件为空！");
            return null;
        }

        // 前端具有几个file组件，这里会对应几个Part对象
        List<Attachment> files = new ArrayList<Attachment>();
        for (Part part : parts) {
            if (part == null) {
                continue;
            }
            // 这里直接以源文件名保存
            String fileName = UploadUtils.getFileName(part);

            if (StringUtils.isBlank(fileName)) {
                continue;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String name = UUID.randomUUID() + "." + FilenameUtils.getExtension(fileName);
            String datePath = sdf.format(new Date());
            String saveName = datePath + "/" + name;

            System.out.println("save the file with new name : " + saveName);

            // 因在注解中指定了路径，这里可以指定要写入的文件名
            // 在未执行write方法之前，将会在注解指定location路径下生成一临时文件
            part.write(saveName);
            Attachment br = new Attachment();
            br.setCreateTime(new Date());
            br.setOldName(fileName);
            br.setFileName(name);
            br.setExt(FilenameUtils.getExtension(fileName));
            br.setPath(datePath);
            br.setStatus(1);
            files.add(br);
        }

        return files;

    }
}
