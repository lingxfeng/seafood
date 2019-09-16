package com.eastinno.otransos.core.service;

import com.eastinno.otransos.web.tools.AutoChangeLink;

/**
 * 静态化处理 Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 * 
 * @Creation date: 2013年9月22日 上午9:48:43
 * @Intro
 */
public abstract interface IHtmlGeneratorService {
    public abstract void begin();

    public abstract void stop();

    public abstract void process(AutoChangeLink paramAutoChangeLink);

    public abstract void remove(AutoChangeLink paramAutoChangeLink);
}
