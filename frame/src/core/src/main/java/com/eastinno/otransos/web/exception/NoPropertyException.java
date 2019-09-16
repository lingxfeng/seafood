package com.eastinno.otransos.web.exception;

import com.eastinno.otransos.core.util.I18n;

/**
 * <p>
 * Title:属性不存在错误
 * </p>
 * <p>
 * Description:当用户企图执Form中不存在VO对像访问,抛出该错误!
 * </p>
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
public class NoPropertyException extends FrameworkException {
    static final long serialVersionUID = 88802L;

    public NoPropertyException() {
        super(I18n.getLocaleMessage("core.web.Attribute.does.not.exist.the.WebForm.not.specified.attribute"));
    }

    public NoPropertyException(String message) {
        super(I18n.getLocaleMessage("core.web.Attribute.does.not.exist") + message);
    }
}
