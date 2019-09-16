package com.eastinno.otransos.web.core;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * Title: 用户提交Id的验证 *
 * </p>
 * <p>
 * Description: 用户提交Id的验证
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: www.disco.org.cn
 * </p>
 * 
 * @author 张钰
 * @version 0.1
 */
public class ForbitRepAction {

    private static ForbitRepProcessor forbit = ForbitRepProcessor.getInstance();

    protected String generateForbitRep(HttpServletRequest request) {
        return forbit.generateForbitRep(request);
    }

    protected boolean isForbitRepValid(HttpServletRequest request) {

        return forbit.isForbitRepValid(request, false);

    }

    protected boolean isForbitRepValid(HttpServletRequest request, boolean reset) {

        return forbit.isForbitRepValid(request, reset);

    }

    protected void resetForbitRep(HttpServletRequest request) {

        forbit.resetForbitRep(request);

    }

    protected void saveForbitRep(HttpServletRequest request) {
        forbit.saveForbitRep(request);
    }

}
