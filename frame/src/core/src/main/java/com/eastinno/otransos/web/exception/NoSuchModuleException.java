package com.eastinno.otransos.web.exception;

import com.eastinno.otransos.core.util.I18n;

/**
 * <p>
 * Title:模块找不到错误
 * </p>
 * <p>
 * Description:当用户企图访问不存在的Module时,抛出该错误!
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
public class NoSuchModuleException extends FrameworkException {
    static final long serialVersionUID = 88803L;

    public NoSuchModuleException() {
        super(
                I18n.getLocaleMessage("core.web.Template.does.not.exist.in.the.configuration.file.is.not.provided.with.the.Template"));
    }

    public NoSuchModuleException(String message) {
        super(I18n.getLocaleMessage("core.web.Template.does.not.exist") + message);
    }
}
