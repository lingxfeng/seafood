package com.eastinno.otransos.web;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.FrameworkEngine;
import com.eastinno.otransos.web.exception.FrameworkException;
import com.eastinno.otransos.web.exception.ValidateException;

/**
 * <p>
 * Title:Web表单类
 * </p>
 * <p>
 * Description:WebForm负责封装用于用户端显示的数据。 在Disco中，WebForm是一个非常重要，也是使用最为频繁的对象，他充当了在视图及程序之间传输、处理数据的媒介。 下面是WebForm中的常用用法：
 * 1、WebForm中包含了视图页面中传输进来的数据，可以通过form.get("键名")的方式来读取这些数据，也可以通过form.set("属性名",值)来改变视图中传过来的数据值。
 * 2、可以把表单中传来数据，通过WebForm提供的快捷方法toPo可以把表单中的数据快速存入模型
 * (域或Command)对象中。如form.toPo(Person.class)可以把表单中的属性值经过转换后存入到Person的对应属性中，并返回一个Person实例。
 * 3、WebForm的toPo方法还会数据校验，可以通过参数设置当校验未通过时是否回滚等。
 * 4、程序中通过WebForm把数据发送到视图模板。要在程序中把一个对象添加到视图，使用form.addResult("名称",obj)来添加，这里即可在视图模板中使用"$对象名"来访问该对象。
 * 5、WebForm的addPo方法可以把一个对象根据其中的属性名称，传到视图中。如form.addPo(person);则在视图可以通过$name来访问person对象的name属性值。 6、更多高级的用法，请参考Disco的教程
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.disco.org.cn
 * </p>
 * 
 * @author lengyu
 * @version 1.0
 */
public class WebForm extends PropertyInfo {
    /**
     * 存放字符串类型的参数值
     */
    private Map<String, Object> textElement = new HashMap<String, Object>();

    /**
     * 存放二进制类型的参数值
     */
    private Map<String, FileItem> fileElement;

    /**
     * 存放Form中的属性名称
     */
    private Map<String, Object> property;

    /**
     * 存放Form的配置文件
     */
    private FormConfig formConfig;

    /**
     * 服务器返回的结果集
     */
    private Map<String, Object> discoResult = new HashMap<String, Object>();

    /**
     * 是否自动验证
     */
    private boolean validate;

    // 请求的url
    private String url;

    private static final Logger logger = Logger.getLogger(WebForm.class);

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WebForm() {
    }

    /**
     * 对表单进行校验
     * 
     * @return 校验成功返回true，否则返回false
     */
    public boolean validate() {
        if (formConfig != null && (formConfig.getServerValidate().equals("true") || formConfig.getServerValidate().equals("yes"))) {
            return doValidate();
        } else
            return true;
    }

    /**
     * 在视图层及逻辑层传输数据，把视图层的对象(View Object)传入程序对象(Programe Object)中，若ignoreBlankString的值为true，则不传递值为空字符类型的数据
     * 
     * @param obj 要传入的目标对象
     * @param ignoreBlankString 是否忽略空字符串，特别当PO中的类型为非字符型的时候，此选项特别有用．
     * @param validateRollback 当校验未通过时，是否回滚对对象的赋值操作，为true表示回滚，即不修改obj中的任何值，为false则表示不回滚，默认值为false。
     * @return 经过处理后，包含了视图层数据的对象
     */
    public Object toPo(Object obj, boolean ignoreBlankString, boolean validateRollback) {
        FrameworkEngine.form2Obj(this.getTextElement(), obj, ignoreBlankString, validateRollback);
        if (validate && FrameworkEngine.getValidateManager().getErrors().hasError()) {
            throw new ValidateException(FrameworkEngine.getValidateManager().getErrors().getMessage());
        }
        return obj;
    }

    /**
     * 在视图层及逻辑层传输数据，把视图层的对象(View Object)传入程序对象(Programe Object)中，若ignoreBlankString的值为true，则不传递转换值为空字符类型的数据
     * 
     * @param obj 赋值PO的目标对象
     * @param ignoreBlankString 是否忽略空字符串，特别当PO中的类型为非字符型的时候，此选项特别有用．
     * @return 经过处理后，包含了视图层数据的对象
     */
    public Object toPo(Object obj, boolean ignoreBlankString) {
        return toPo(obj, ignoreBlankString, true);
    }

    /**
     * 在视图层及逻辑层传输数据，把视图层的对象(View Object)传入程序对象(Programe Object)中，若ignoreBlankString的值为true，则不传递转换值为空字符类型的数据
     * 
     * @param obj PO目标对象
     * @return 经过处理后，包含了视图层数据的对象
     */
    public Object toPo(Object obj) {
        return toPo(obj, true, true);
    }

    /**
     * 在视图层及逻辑层传输数据，把表单中的数据传入类型为classType的对象实例中。该方法将生成指定类型的一个新实例，并包含了相关数据。
     * 
     * @param classType 具体的类名
     * @param ignoreBlankString 在数据赋值的过程中是否忽略空字符
     * @return 经过处理后，包含了视图层数据的对象
     */
    public <T> T toPo(Class<T> classType, boolean ignoreBlankString) {
        T obj = null;
        try {
            obj = classType.newInstance();
            toPo(obj, ignoreBlankString, false);
        } catch (InstantiationException e) {
            throw new FrameworkException(I18n.getLocaleMessage("core.not.initialization.parameters"), e);
        } catch (IllegalAccessException arge) {
            throw new FrameworkException(I18n.getLocaleMessage("core.not.initialization.parameters"), arge);
        }
        return obj;
    }

    /**
     * 把form表单中的数据转换成classType类对象,默认将处理所有定义的数据，值为null的也将会被处理．
     * 
     * @param <T> 类模板
     * @param classType 类名
     * @return 返回指定类型并包含了具体值的对象
     */
    public <T> T toPo(Class<T> classType) {
        return toPo(classType, false);
    }

    /**
     * PO对象中的数据存放到FORM对象中 该方法将遍历obj中所有属性，并以属性名作为键值分别添加到结果集中。
     * 
     * @param obj 要传输到视图的程序对象
     */
    public void addPo(Object obj) {
        if (obj != null) {
            FrameworkEngine.po2form(obj, this.getTextElement());
            this.addResult("obj", obj);
        }
    }

    /**
     * 通过配置文件中的检验设置分别校验表单数据 该方法暂时未用
     * 
     * @return 若验证通过，则返回true，否则返回false
     */
    protected boolean doValidate() {
        return true;
    }

    /**
     * 读取从视图中传来的数据
     * 
     * @param name 表单或请求参数的键名称
     * @return 具体值
     */
    public Object get(String name) {
        if ((this.getClass() != WebForm.class) && (!property.containsKey(name)))
            return null;
        if (textElement.containsKey(name))
            return textElement.get(name);
        else
            return fileElement.get(name);

    }
    /**
     * REST之后,根据参数在url中的位置返回参数
     * @param keyIndex
     * @return
     */
    public Object get(int keyIndex){
    	return get("_pa_in"+keyIndex+"d_ex_");
    }
    /**
     * REST之后,根据参数在url中的位置返回参数,可以指定默认值
     * @param keyIndex
     * @return
     */
    public Object get(int keyIndex,String defaultValue){
    	Object obj = get("_pa_in"+keyIndex+"d_ex_");
    	if (obj == null) {
            obj = defaultValue;
        }
        return obj;
    }

    /**
     * 为了避免过多的非空判断，增加一个返回默认值的方法
     * 
     * @param name
     * @param defaultValue
     * @return
     */
    public Object get(String name, String defaultValue) {
        Object obj = get(name);
        if (obj == null) {
            obj = defaultValue;
        }
        return obj;
    }

    /**
     * 更改从视图中传过来的表单属性的参数值
     * 
     * @param key 表单或请求参数的键值
     * @param value 需要设置的新值
     */
    public void set(String key, Object value) {
        if (textElement.containsKey(key))
            textElement.put(key, value);
        else if (fileElement.containsKey(key))
            fileElement.put(key, (FileItem) value);
        else
            logger.error(I18n.getLocaleMessage("core.the.form.does.not.attribute"));
    }

    /**
     * 获得表单中上传过来的二进制文件对象
     * 
     * @return 若有二进制文件，则返回具体的文件对象
     */
    public Map<String, FileItem> getFileElement() {
        return fileElement;
    }

    /**
     * 设置表单中的二进制文件数据对象
     * 
     * @param fileElement 二进制文件数据
     */
    public void setFileElement(Map<String, FileItem> fileElement) {
        this.fileElement = fileElement;
    }

    /**
     * 获取表单中所有文本性质的域内容对象
     * 
     * @return 表单中的所有文本性质的域对象
     */
    public Map<String, Object> getTextElement() {
        return textElement;
    }

    /**
     * 设置WebForm中文本域内容集
     * 
     * @param textElement 文本域的内容集合
     */
    public void setTextElement(Map<String, Object> textElement) {
        this.textElement = textElement;
    }

    /**
     * 获取到程序中添加到视图层的属性及值集合
     * 
     * @return 程序中要传输给视图层的属性及值集合
     */
    public Map<String, Object> getProperty() {
        return property;
    }

    /**
     * 设置程序中要添加到视图层的属性及值集合
     * 
     * @param property 属性及值集合
     */
    public void setProperty(Map<String, Object> property) {
        this.property = property;
    }

    /**
     * 获取该WebForm对象的配置信息
     * 
     * @return WebForm的具体配置信息
     */
    public FormConfig getFormConfig() {
        return formConfig;
    }

    /**
     * 设置WebForm的对象配置信息
     * 
     * @param formConfig WebForm的具体配置信息
     */
    public void setFormConfig(FormConfig formConfig) {
        this.formConfig = formConfig;
    }

    /**
     * 返回从程序中通过addResult或addPo添加到视图层的所有结果集。
     * 
     * @return 程序中的所有结果集
     */
    public Map<String, Object> getDiscoResult() {
        return discoResult;
    }

    /**
     * 设置系统中的结果集
     * 
     * @param discoResult 结果集
     */
    public void setDiscoResult(Map<String, Object> discoResult) {
        this.discoResult = discoResult;
    }

    /**
     * 该WebForm是否需要强制验证
     * 
     * @return 若为true表示需要强制验证,否则为false
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * 设置是否进行强制验证
     * 
     * @param validate true表示需要强制验证，false表示不需要
     */
    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    /**
     * 往结果集中添加对象
     * 
     * @param key 视图中使用逻辑键名
     * @param value 要添加到结果集中的具体对象
     */
    public void addResult(String key, Object value) {
        if (!key.equals("discoResult"))
            discoResult.put(key, value);
        else
            logger.error(I18n.getLocaleMessage("core.attempted.to.use.keyword.discoResult.Disco.save.data.system.failure"));
    }

    /**
     * 把obj对象转换为json字符串并存放在discoResult结果集中
     * 
     * @param obj 需输出的对象
     */
    public void jsonResult(Object obj) {
        this.jsonResult(obj, AjaxUtil.JSON_OBJECT_MAX_DEPTH);
    }

    /**
     * 把obj对象转换为json字符串并存放在discoResult结果集中
     * 
     * @param obj 需输出的对象
     * @param maxDepth 指定json转换的层级 默认为5层不建议设置为>5的值
     */
    public void jsonResult(Object obj, int maxDepth) {
        this.addResult("javaObj", obj);
        this.addResult("jsonStr", AjaxUtil.getJSON(obj, maxDepth));
    }

    public static void main(String[] args) {
        // String s = "{\"name\":\"中国\ 要 要\"}";
        JSONObject obj = new JSONObject();
        obj.put("name", "中国 要 要");

        System.out.println(AjaxUtil.getJSON(obj, 5));
    }
}
