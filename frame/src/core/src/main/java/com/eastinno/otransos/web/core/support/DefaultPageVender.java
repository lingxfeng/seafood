package com.eastinno.otransos.web.core.support;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.io.VelocityWriter;
import org.apache.velocity.util.SimplePool;

import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebInvocationParam;
import com.eastinno.otransos.web.core.FrameworkEngine;

public class DefaultPageVender extends BasePageVender {

    private static final Logger logger = Logger.getLogger(DefaultPageVender.class);

    private final static SimplePool writerPool = new SimplePool(60);

    protected boolean mergeTemplate(Template template, Context context, HttpServletResponse response, Page page,
            HttpServletRequest request, WebInvocationParam param) {
        VelocityWriter vw = null;
        Writer writer = null;
        try {
            response.setCharacterEncoding(template.getEncoding());
            response.setContentType(page.getContentType());
            writer = ActionContext.getContext().getCustomWriter();// 首先判断是否重定向了writer
            if (writer == null)
                writer = FrameworkEngine.getResponseWriter(response);
            vw = (VelocityWriter) writerPool.get();
            if (vw == null) {
                vw = new VelocityWriter(writer, 4 * 1024, true);
            } else {
                vw.recycle(writer);
            }
            template.merge(context, vw);
        } catch (Exception e) {
            logger.error(e);
        } finally {
            if (vw != null) {
                try {
                    vw.flush();
                    vw.recycle(null);
                    writerPool.put(vw);
                    writer.close();
                } catch (Exception e) {
                    logger.error("Trouble releasing VelocityWriter: " + e.getMessage());
                }
            }
        }
        return true;
    }

    /**
     * Disco中使用Velocity模板引擎来处理所有页面的输出。
     */
    @Override
    public boolean supports(String suffix) {
        return true;
    }

}
