package com.eastinno.otransos.core.mvc.ajax;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.InjectDisable;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.IWebAction;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.ImageCode;

/**
 * @intro 动态验证码
 * @version v0.1
 * @author maowei
 * @since 2007年5月7日 上午11:09:01
 */
@Action
public class RandomCodeAction implements IWebAction {
    @InjectDisable
    private static final Log logger = LogFactory.getLog(RandomCodeAction.class);

    public Page execute(WebForm form, Module module) throws Exception {
        int number = 0;
        number = CommUtil.null2Int(form.get("number"));
        int max = 0;
        max = CommUtil.null2Int(form.get("max"));
        HttpServletResponse response = ActionContext.getContext().getResponse();
        response.setContentType("image/jpeg");
        ImageCode image = new ImageCode();
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
        try {
            BufferedImage img = null;
            if (number != 0) {
                if (max != 0)
                    img = image.creatImage3D(number, max);
                else
                    img = image.creatImage(number);
            } else {
                img = image.creatImage();
            }
            ActionContext.getContext().getSession().setAttribute("rand", image.getSRand());
            ImageIO.write(img, "JPEG", response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (Exception e) {
            logger.error("错误:" + e);
        }
        return null;
    }
}
