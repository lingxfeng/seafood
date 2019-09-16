package com.eastinno.otransos.core.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Globals;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.FrameworkEngine;
import com.eastinno.otransos.web.validate.ValidateType;
import com.eastinno.otransos.web.validate.ValidatorManager;

/**
 * <b> Intro: 实现文件的简单处理,判断文件类型、文件复制、目录复制等<b>
 * 
 * @Version v_0.1
 * @Author lengyu
 * @CreateDate 2012-11-02 上午9:26:26
 */
public abstract class FileUtil {
    public final static String DANGER_FILE_EXT = "exe;jsp;com;bat;cmd;";// 禁止或危险性的文件
    public final static String IMAGE_FILE_EXT = "jpg;jpeg;png;gif;bmp;ico;";// 图形文件
    public final static String ATTACHE_FILE_EXT = "txt;doc;docx;xls;zip;rar;pdf;xml;7z;";// 电子文档等其它类文件
    public final static String VIDIO_FILE_EXT = "wav;rm;rmvb;mpg;mpeg;asf;avi;swf;wmv;mp4;avi;flv;";// 视频文件
    public final static String AUDIO_FILE_EXT = "mp3;wma,mid;ogg;";// 音频文件
    public final static String ELDOC_FILE_EXT = "doc;docx;xls;pdf;xml;swf;ppt;";// 电子文档
    public final static String PAGE_FILE_EXT = "html;shtml;htm;xhtml;xml;json;txt;";// 静态页面

    /**
     * 处理上传文件
     * 
     * @param form WebForm对象
     * @param fileUploadField 上传文件字段
     * @param dir 保存目录
     * @param exp 合法的文件扩展名
     * @return 上传校验成功并保存后返回上传文件相对路径
     */
    public static String uploadFile(WebForm form, String fileUploadField, String dir, String exp) {
        return FileUtil.uploadFile(form, fileUploadField, dir, exp, null);
    }

    /**
     * 处理上传文件
     * 
     * @param form WebForm对象
     * @param fileUploadField 上传文件字段
     * @param dir 保存目录
     * @param exp 合法的文件扩展名
     * @param fileSingleSizeLimit 上传文件最大字节数,超过此值则不允许上传(1M=1024KB)
     * @return 上传校验成功并保存后返回上传文件相对路径
     */
    public static String uploadFile(WebForm form, String fileUploadField, String dir, String exp, Long fileSingleSizeLimit) {
        return FileUtil.uploadFile(form, fileUploadField, dir, exp, fileSingleSizeLimit, null);
    }

    /**
     * 处理上传文件
     * 
     * @param form WebForm对象
     * @param fileUploadField 上传文件字段
     * @param dir 保存目录
     * @param exp 合法的文件扩展名
     * @param fileSingleSizeLimit 上传文件最大字节,超过此值则不允许上传(1M=1024KB)
     * @param prefix 给新的文件名加一个前缀名
     * @return 上传校验成功并保存后返回上传文件相对路径
     */
    public static String uploadFile(WebForm form, String fileUploadField, String dir, String exp, Long fileSingleSizeLimit, String prefix) {
        String path = "";
        ValidateType vt = ValidateType.Action;
        ValidatorManager vm = FrameworkEngine.findValidatorManager();
        FileItem item = (FileItem) form.getFileElement().get(fileUploadField);
        HttpServletRequest request = ActionContext.getContext().getRequest();
        if (!StringUtils.hasText(prefix)) {
            prefix = "";
        } else {
            prefix += "_";
        }
        if (item != null) {
            if (fileSingleSizeLimit != null && fileSingleSizeLimit > 0 && item.getSize() > fileSingleSizeLimit) {
                vm.addCustomError(fileUploadField, "上传文件的大小超出限制，最大只能上传:" + fileSingleSizeLimit / 1024 / 1024 + "M", vt);
                return path;
            }
            String fileName = item.getName();
            if (StringUtils.hasText(fileName)) {
                if (FileUtil.checkExtFile(fileName, exp)) {
                    String suffix = fileName.substring(fileName.lastIndexOf('.'));
                    path = dir + "/" + prefix + new Date().getTime() + suffix;
                    File f = new File(Globals.APP_BASE_DIR + path);
                    if (!f.getParentFile().exists())
                        f.getParentFile().mkdirs();
                    try {
                        item.write(f);
                    } catch (Exception e) {
                        vm.addCustomError(fileUploadField, "文件上传出错", vt);
                    }
                } else {
                    vm.addCustomError(fileUploadField, fileName + "为非法的文件上传格式,后续名必须为: [" + exp + "]", vt);
                }
            }
        } else if ("application/octet-stream".equals(request.getContentType())) {// HTML5方式上传
            try {
                String dispoString = request.getHeader("Content-Disposition");
                int iFindStart = dispoString.indexOf("name=\"") + 6;
                int iFindEnd = dispoString.indexOf("\"", iFindStart);

                iFindStart = dispoString.indexOf("filename=\"") + 10;
                iFindEnd = dispoString.indexOf("\"", iFindStart);
                String sFileName = dispoString.substring(iFindStart, iFindEnd);

                int i = request.getContentLength();
                byte[] buffer = new byte[i];
                int j = 0;
                while (j < i) {
                    int k = request.getInputStream().read(buffer, j, i - j);
                    j += k;
                }

                if (buffer.length == 0) {
                    vm.addCustomError(fileUploadField, "上传文件不能为空", vt);
                    return path;
                }
                if ((fileSingleSizeLimit != null && fileSingleSizeLimit > 0) && (buffer.length > fileSingleSizeLimit)) {
                    vm.addCustomError(fileUploadField, "上传文件的大小超出限制，最大只能上传:" + fileSingleSizeLimit / 1024 / 1024 + "M", vt);
                    return path;
                }

                if (!FileUtil.checkExtFile(sFileName, exp)) {
                    vm.addCustomError(fileUploadField, sFileName + "为非法的文件上传格式,后续名必须为: [" + exp + "]", vt);
                    return path;
                }
                String suffix = sFileName.substring(sFileName.lastIndexOf('.'));
                path = dir + "/" + prefix + new Date().getTime() + suffix;
                File f = new File(Globals.APP_BASE_DIR + path);
                if (!f.getParentFile().exists()) {
                    f.getParentFile().mkdirs();
                }
                OutputStream out = new BufferedOutputStream(new FileOutputStream(Globals.APP_BASE_DIR + path, true));
                out.write(buffer);
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                vm.addCustomError(fileUploadField, e.toString(), vt);
                return path;
            }
        } else {
            vm.addCustomError(fileUploadField, "上传文件处理发生系统级错误", vt);
        }
        return path;
    }

    /**
     * 根据文件名获取文件名对应的后缀名
     * 
     * @param fileName 文件名
     */
    public static String getFileExt(String fileName) {
        if (!StringUtils.hasText(fileName))
            return null;
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    /**
     * 检测文件的后缀名是否与指定后缀名一致
     * 
     * @param fileName 文件名全称
     * @param ext 指定的文件后缀名,多个文件后缀名使用分号(;)相隔
     * @return 检测一致后则返回true
     */
    public static boolean checkExtFile(String fileName, String ext) {
        if (ext == null)
            return false;
        String[] exts = ext.split(";");
        String file = fileName.toLowerCase();
        for (String element : exts)
            if (file.endsWith("." + element))
                return true;
        return false;
    }

    /**
     * 根据目录及指定文件后缀生成一个临时文件
     * 
     * @param dir
     * @param fileExt
     * @return
     */
    public static String getTempFile(String dir, String fileExt) {
        // String tempFileName = CommUtil.getRandString(8) + fileExt;
        String tempFileName = CommUtil.formatDate("yyMMdd-HHmmssS", new Date()) + fileExt;
        File file = new File(dir + "/" + tempFileName);
        if (file.exists())
            return getTempFile(dir, fileExt);
        else
            return tempFileName;
    }

    /**
     * 上传文件是否是附件允许的类型
     * 
     * @param fileName
     * @return
     */
    public static boolean isAttacheFile(String fileName) {
        return checkExtFile(ATTACHE_FILE_EXT, fileName);
    }

    /**
     * 是否是危险性的文件后缀
     * 
     * @param fileName
     * @return
     */
    public static boolean isDangerFile(String fileName) {
        return checkExtFile(DANGER_FILE_EXT, fileName);
    }

    /**
     * 上传文件是否是图形类文件
     * 
     * @param fileName
     * @return
     */
    public static boolean isImgageFile(String fileName) {
        return checkExtFile(IMAGE_FILE_EXT, fileName);
    }

    /**
     * 上传文件是否是音频类文件
     * 
     * @param fileName
     * @return
     */
    public static boolean isAudioFile(String fileName) {
        return checkExtFile(AUDIO_FILE_EXT, fileName);
    }

    /**
     * 上传文件是否是视频类文件
     * 
     * @param fileName
     * @return
     */
    public static boolean isVideoFile(String fileName) {
        return checkExtFile(VIDIO_FILE_EXT, fileName);
    }

    /**
     * 上传文件是否是电子文档类文件
     * 
     * @param fileName
     * @return
     */
    public static boolean isElDoc(String fileName) {
        return checkExtFile(ELDOC_FILE_EXT, fileName);
    }

    /**
     * 保存文件
     * 
     * @param in
     * @param fileName
     * @return
     */
    public static boolean saveFile(InputStream in, String fileName) {
        File outFile = new File(fileName);
        try {
            FileOutputStream out = new FileOutputStream(outFile);
            byte[] temp = new byte[11024];
            int length = -1;
            while ((length = in.read(temp)) > 0)
                out.write(temp, 0, length);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * 根据目录路径删除指定的文件
     * 
     * @param path
     */
    public static void delFile(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            for (String fileName : file.list()) {
                delFile(path + "/" + fileName);
            }
        }
        file.delete();
    }

    /**
     * 复制目录下的文件（不包括该目录）到指定目录，会连同子目录一起复制过去。
     * 
     * @param targetFile
     * @param path
     */
    public static void copyFileFromDir(String targetDir, String path) {
        File file = new File(path);
        createFile(targetDir, false);
        if (file.isDirectory()) {
            copyFileToDir(targetDir, listFile(file));
        }
    }

    /**
     * 复制目录下的文件（不包含该目录和子目录，只复制目录下的文件）到指定目录。
     * 
     * @param targetDir
     * @param path
     */
    public static void copyFileOnly(String targetDir, String path) {
        File file = new File(path);
        File targetFile = new File(targetDir);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File subFile : files) {
                if (subFile.isFile()) {
                    copyFile(targetFile, subFile);
                }
            }
        }
    }

    /**
     * 复制目录到指定目录。targetDir是目标目录，path是源目录。 该方法会将path以及path下的文件和子目录全部复制到目标目录
     * 
     * @param targetDir
     * @param path
     */
    public static void copyDir(String targetDir, String path) {
        File targetFile = new File(targetDir);
        createFile(targetFile, false);
        File file = new File(path);
        if (targetFile.isDirectory() && file.isDirectory()) {
            copyFileToDir(targetFile.getAbsolutePath() + "/" + file.getName(), listFile(file));
        }
    }

    /**
     * 复制一组文件到指定目录。targetDir是目标目录，filePath是需要复制的文件路径
     * 
     * @param targetDir
     * @param filePath
     */
    public static void copyFileToDir(String targetDir, String... filePath) {
        if (targetDir == null || "".equals(targetDir)) {
            System.out.println("参数错误，目标路径不能为空");
            return;
        }
        File targetFile = new File(targetDir);
        if (!targetFile.exists()) {
            targetFile.mkdir();
        } else {
            if (!targetFile.isDirectory()) {
                System.out.println("参数错误，目标路径指向的不是一个目录！");
                return;
            }
        }
        for (String path : filePath) {
            File file = new File(path);
            if (file.isDirectory()) {
                copyFileToDir(targetDir + "/" + file.getName(), listFile(file));
            } else {
                copyFileToDir(targetDir, file, "");
            }
        }
    }

    /**
     * 复制文件到指定目录。targetDir是目标目录，file是源文件名，newName是重命名的名字。
     * 
     * @param targetFile
     * @param file
     * @param newName
     */
    public static void copyFileToDir(String targetDir, File file, String newName) {
        String newFile = "";
        if (newName != null && !"".equals(newName)) {
            newFile = targetDir + "/" + newName;
        } else {
            newFile = targetDir + "/" + file.getName();
        }
        File tFile = new File(newFile);
        copyFile(tFile, file);
    }

    /**
     * 复制文件。targetFile为目标文件，file为源文件
     * 
     * @param targetFile
     * @param file
     */
    public static void copyFile(File targetFile, File file) {
        if (targetFile.exists()) {
            System.out.println("文件" + targetFile.getAbsolutePath() + "已经存在，跳过该文件！");
            return;
        } else {
            createFile(targetFile, true);
        }
        System.out.println("复制文件" + file.getAbsolutePath() + "到" + targetFile.getAbsolutePath());
        try {
            InputStream is = new FileInputStream(file);
            FileOutputStream fos = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            while (is.read(buffer) != -1) {
                fos.write(buffer);
            }
            is.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cutFile(String targetFile, String file) {

    }

    /**
     * 获取文件下所有子文件的绝对路径
     * 
     * @param dir
     * @return
     */
    public static String[] listFile(File dir) {
        String absolutPath = dir.getAbsolutePath();
        String[] paths = dir.list();
        String[] files = new String[paths.length];
        for (int i = 0; i < paths.length; i++) {
            files[i] = absolutPath + "/" + paths[i];
        }
        return files;
    }

    /**
     * 创建文件
     * 
     * @param path
     * @param isFile
     */
    public static void createFile(String path, boolean isFile) {
        createFile(new File(path), isFile);
    }

    /**
     * 创建文件
     * 
     * @param file
     * @param isFile
     */
    public static void createFile(File file, boolean isFile) {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                createFile(file.getParentFile(), false);
            } else {
                if (isFile) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    file.mkdir();
                }
            }
        }
    }

    /**
     * 根据文件名检查是否是一个文件
     * 
     * @param fileName
     * @return
     */
    public static boolean isVidioFile(String fileName) {
        return checkExtFile(VIDIO_FILE_EXT, fileName);
    }

    public static String moveFile(String shortPath, String baseDir) {
        File file = new File(Globals.APP_BASE_DIR + shortPath);
        if (file.exists()) {
            String target = Globals.APP_BASE_DIR + baseDir + "/" + file.getName();
            File targetFile = new File(target);
            if (!targetFile.getParentFile().exists())
                targetFile.getParentFile().mkdirs();
            if (!targetFile.exists()) {
                if (file.renameTo(targetFile)) {
                    moveFile(smallPath(shortPath), baseDir);// 移动小图片
                    return baseDir.replaceAll("\\\\", "/") + "/" + file.getName();
                }
            }
        }
        return shortPath;
    }

    private static String smallPath(String path) {
        int dotIndex = path.lastIndexOf('.');
        String preffix = path.substring(0, dotIndex);
        String suffix = path.substring(dotIndex);
        String name = preffix + "_small" + suffix;
        return name;
    }

    /**
     * 删除文件
     * 
     * @param shortPath
     * @return
     */
    public static boolean removeFile(String shortPath) {
        File file = new File(Globals.APP_BASE_DIR + shortPath);
        if (file.exists())
            return file.delete();
        return false;
    }

    public static String getBaseDir(String shortPath) {
        int rootLenth = (int) new File(Globals.APP_BASE_DIR).getAbsolutePath().length();
        File file = new File(Globals.APP_BASE_DIR + shortPath);
        String path = "";
        System.out.println(file.getAbsolutePath());
        if (file.exists()) {
            path = file.getParentFile().getAbsolutePath().substring(rootLenth);
        }
        return path;
    }

    /**
     * 获取系统信息
     * 
     * @param parent
     * @param dirOnly
     * @return
     */
    public static List<Map<String, Object>> getSystemFile(String parent, boolean dirOnly) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String baseDir = parent;
        if (baseDir == null || "".equals(baseDir))
            baseDir = "";
        File dir = new File(Globals.APP_BASE_DIR + baseDir);
        int rootLenth = (int) new File(Globals.APP_BASE_DIR).getAbsolutePath().length();
        if (dir.exists() && dir.isDirectory()) {
            if (dir.getAbsolutePath().length() >= rootLenth) {
                File[] ts = dir.listFiles();
                if (ts != null) {
                    for (int i = 0; i < ts.length; i++) {
                        if (!dirOnly || ts[i].isDirectory()) {
                            String path = ts[i].getPath().substring(rootLenth);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("id", path);
                            map.put("title", path);
                            if (ts[i].isDirectory() && ts[i].listFiles(new java.io.FileFilter() {
                                public boolean accept(File pathname) {
                                    return pathname.isDirectory();
                                }

                            }).length > 0) {
                                List<String> alist = new ArrayList<String>();
                                alist.add(ts[i].list()[0]);
                                map.put("children", alist);
                            }
                            list.add(map);
                        }
                    }
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String a = "13.21.jpgsdfa";
        System.out.println(a.substring(a.lastIndexOf("."), a.length()));
        // copyDir("i:/t3", "i:/t2");
        // copyFileToDir("i:/t3", "i:/t2/1.txt");
        // File file = new File("i:/t2/1.txt");
        // System.out.println(file.getAbsolutePath());
        // File file2 = new File("i:/t3/1.txt");
        // copyFileToDir("i:/t3/", file, "");
        // File file = new File("i:/t2");
        // for(String filepath:file.list()){
        // System.out.println(filepath);
        // }
    }

}
