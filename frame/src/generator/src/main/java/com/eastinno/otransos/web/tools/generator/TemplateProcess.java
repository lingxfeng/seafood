package com.eastinno.otransos.web.tools.generator;

import org.apache.velocity.context.Context;

public interface TemplateProcess {
    /**
     * 把数据保存到context中，以供生成引擎调用
     * 
     * @param context
     */
    void process(Context context) throws Exception;
}
