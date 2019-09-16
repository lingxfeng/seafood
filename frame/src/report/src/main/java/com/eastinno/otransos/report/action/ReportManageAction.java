package com.eastinno.otransos.report.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.report.service.ReportService;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;

@Action
public class ReportManageAction extends AbstractPageCmdAction {
    @Resource
    private ReportService reportService;

    /**
     * 解析HTML导出Excel
     * 
     * @return
     */
    public Page doToExcel(WebForm form) {
        HttpServletRequest req = this.getRequest();
        String html = req.getParameter("html");// 需解析的DOM片段
        String filename = req.getParameter("filename");// 生成的报表名称
        reportService.html2Excel(html, filename);
        return null;
    }

    /**
     * 解析HTML转PDF
     * 
     * @return
     */
    public Page doToPdf(WebForm form) {
        HttpServletRequest req = this.getRequest();
        String html = req.getParameter("html");// 需解析的DOM片段
        String filename = req.getParameter("filename");// 生成的报表名称
        reportService.html2Pdf(html, filename);
        return null;
    }

    /**
     * 根据SVG生成图像文件
     * 
     * @return
     */
    public Page doToImg(WebForm form) {
        try {
            HttpServletRequest request = getRequest();
            String type = request.getParameter("type");
            String svg = request.getParameter("svg");
            String filename = request.getParameter("filename");
            filename = filename == null ? "chart" : filename;
            String ext = type.substring(type.lastIndexOf("/") + 1, type.length());
            if (null != type && null != svg) {
                ByteArrayOutputStream out = this.reportService.chart2Img(svg, type);
                HttpServletResponse resp = ActionContext.getContext().getResponse();
                resp.getOutputStream().write(out.toByteArray());
                resp.addHeader("Content-Disposition",
                        "attachment; filename=" + URLEncoder.encode(filename + "." + ext, "UTF-8"));
                resp.addHeader("Content-Type", type);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setReportService(ReportService reportService) {
        this.reportService = reportService;
    }
}
