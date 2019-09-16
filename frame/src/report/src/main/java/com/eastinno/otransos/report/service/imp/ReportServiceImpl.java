package com.eastinno.otransos.report.service.imp;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.fop.svg.PDFTranscoder;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.eastinno.otransos.report.domain.ReportInfo;
import com.eastinno.otransos.report.domain.TableCellInfo;
import com.eastinno.otransos.report.service.ReportService;
import com.eastinno.otransos.report.util.ReportUtil;
import com.eastinno.otransos.report.util.StyleUtil;
import com.eastinno.otransos.web.ActionContext;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ReportServiceImpl implements ReportService {
    private static ByteArrayOutputStream baos = null;// 下载文件的流信息
    private static final Logger log = Logger.getLogger(ReportServiceImpl.class);

    @Override
    public Boolean html2Excel(String htmldom, String excelFileName) {
        Boolean success = true;
        try {
            if (StringUtils.isEmpty(htmldom) || StringUtils.isEmpty(excelFileName)) {
                return null;
            }
            ReportInfo reportInfos = this.html2Obj(htmldom);
            this.insert2Excel(reportInfos, excelFileName);
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public Boolean html2Pdf(String htmldom, String pdfFileName) {
        Boolean success = true;
        try {
            if (StringUtils.isEmpty(htmldom) || StringUtils.isEmpty(pdfFileName)) {
                return null;
            }
            ReportInfo reportInfos = this.html2Obj(htmldom);
            this.insert2Pdf(reportInfos, pdfFileName);
        } catch (Exception e) {
            success = false;
            e.printStackTrace();
        }
        return success;
    }

    @Override
    public ReportInfo html2Obj(String htmldom) {
        ReportInfo reportInfo = new ReportInfo();
        List<TableCellInfo> cells = new ArrayList<TableCellInfo>();
        List<TableCellInfo> theads = new ArrayList<TableCellInfo>();

        Document doc = Jsoup.parse(htmldom);
        Map<String, StyleUtil> globalStyle = ReportUtil.initGlobalStyle(doc);// 对全局样式进行解析

        Elements trs = doc.select("tr");
        int maxColumn = 0;// 计算表格最大列数
        for (int y = 0; y < trs.size(); y++) {
            Elements tds = trs.get(y).select("td");
            if (tds.size() > maxColumn) {
                maxColumn = tds.size();
            }
            for (int x = 0; x < tds.size(); x++) {
                Element td = tds.get(x);
                TableCellInfo cell = new TableCellInfo();
                cell.setY(y);// y为纵坐标系数
                cell.setX(this.horizontalDerivation(td));// 横坐标需要经过两次推导计算（当前是第一次）
                String colspan = td.attr("colspan");
                if (StringUtils.isEmpty(colspan)) {
                    colspan = "1";
                }
                String rowspan = td.attr("rowspan");
                if (StringUtils.isEmpty(rowspan)) {
                    rowspan = "1";
                }
                cell.setColspan(Integer.parseInt(colspan));
                cell.setRowspan(Integer.parseInt(rowspan));
                cell.setContent(this.getTDVal(td));// 单元格内容转为字节进行存储
                cell.setStyle(ReportUtil.parseCellStyle(td, globalStyle));
                if ("chart".equals(td.attr("type"))) {
                    cell.setType(2);// 是图表
                }
                Element thead = ReportUtil.searchUpThead(td);
                if (thead != null) {
                    if (!"chart".equals(td.attr("type"))) {
                        theads.add(cell);// 此单元格如果属于表头则加入theads容器中
                    }
                    cell.setThead(true);
                }
                cells.add(cell);
            }
        }
        this.verticalDerivation(cells);// 第二次推导
        reportInfo.setMaxColumn(maxColumn);
        reportInfo.setThead(theads);
        reportInfo.setCells(cells);
        return reportInfo;
    }

    /**
     * 获取table td单元格的值 目前有文本类型 OR 图形文件
     * 
     * @param td
     * @return 返回单元格的字节码
     */
    private byte[] getTDVal(Element td) {
        byte[] b = null;
        String type = td.attr("type");
        if (type.equals("chart")) {
            b = this.chart2Img(td.html(), "image/png").toByteArray();
        } else {
            String val = td.text();
            if (val == null) {
                val = "";
            }
            b = val.getBytes();
        }
        return b;
    }

    @Override
    public ByteArrayOutputStream chart2Img(String svg, String type) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            if (null != type && StringUtils.isNotEmpty(svg)) {
                svg = svg.replaceAll(":rect", "rect");
                svg = svg.replace("clippath", "clipPath");
                Transcoder t = null;
                if (type.equals("image/png")) {
                    t = new PNGTranscoder();
                } else if (type.equals("image/jpeg")) {
                    t = new JPEGTranscoder();
                } else if (type.equals("application/pdf")) {
                    t = new PDFTranscoder();
                }
                if (null != t) {
                    TranscoderInput input = new TranscoderInput(new StringReader(svg));
                    TranscoderOutput output = new TranscoderOutput(out);
                    t.transcode(input, output);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    /**
     * 创建PDF Table
     * 
     * @param cellList
     * @return
     */
    private void insert2Pdf(ReportInfo reportInfos, String pdfFileName) {
        try {
            List<TableCellInfo> cellList = reportInfos.getCells();
            com.itextpdf.text.Document doc = new com.itextpdf.text.Document(PageSize.A4.rotate());
            baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(doc, baos);
            doc.open();
            PdfPTable table = new PdfPTable(reportInfos.getMaxColumn());
            table.setHeaderRows(2);// 指定分页表头
            for (TableCellInfo tdInfo : cellList) {
                BaseFont bf = null;
                bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
                Font defaultfont = new Font(bf, 12, Font.NORMAL);
                String value = "";
                PdfPCell cell = null;
                if (tdInfo.getType() == 1) {
                    value = new String(tdInfo.getContent());
                    cell = new PdfPCell(new Phrase(value, defaultfont));
                    cell.setBorderWidth(1);
                    cell.setHorizontalAlignment(com.itextpdf.text.Element.ALIGN_CENTER);
                    if (tdInfo.getThead()) {
                        cell.setBackgroundColor(new BaseColor(235, 235, 235));// 背景颜色统一定义为灰色
                    }
                } else if (tdInfo.getType() == 2) {
                    Image img = Image.getInstance(tdInfo.getContent());
                    img.scalePercent(70f);// 图片缩放比例
                    img.setAlignment(Image.ALIGN_CENTER);
                    doc.add(img);
                    continue;
                }
                cell.setMinimumHeight(20);
                if (tdInfo.getRowspan() > 1) {
                    cell.setRowspan(tdInfo.getRowspan());
                }
                if (tdInfo.getColspan() > 1) {
                    cell.setColspan(tdInfo.getColspan());
                }
                table.addCell(cell);
            }
            doc.add(table);
            doc.close();
            this.download(pdfFileName, "pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * 
     * @param fileName 下载的Excel文件名
     * @param ext 下载文件扩展名
     * @return
     */
    private Boolean download(String fileName, String ext) {
        Boolean success = true;
        InputStream ips = null;
        HttpServletResponse resp = ActionContext.getContext().getResponse();
        try {
            if (baos != null) {
                byte[] ba = baos.toByteArray();
                ips = new ByteArrayInputStream(ba);

                // 以流的形式下载文件。
                if (ips != null) {
                    String type = "APPLICATION/OCTET-STREAM";
                    if (ext.equals("xls")) {
                        type = "application/xls";
                    } else if (ext.equals("pdf")) {
                        type = "application/pdf";
                    }

                    resp.setContentType(type);
                    resp.setContentLength(ips.available());
                    resp.setHeader("Content-Disposition",
                            "attachment; filename=\"" + URLEncoder.encode(fileName + "." + ext, "UTF-8") + "\"");
                    byte[] buff = new byte[1000];
                    OutputStream out = resp.getOutputStream();
                    out.flush();
                    int c;
                    while ((c = ips.read(buff, 0, 1000)) > 0) {
                        out.write(buff, 0, c);
                        out.flush();
                    }
                    out.close();
                    ips.close();
                }
            }
        } catch (IOException ex) {
            success = false;
            ex.printStackTrace();
        }
        return success;
    }

    /**
     * <p>
     * 第一次colspan推演，即横向推导 递归算法。
     * </p>
     * 
     * @param td
     * @return
     */
    private int horizontalDerivation(Element td) {
        Element prevEl = td.previousElementSibling();// 得到当前TD的前一个TD
        while (prevEl != null && prevEl.nodeName() != td.nodeName()) {
            prevEl = prevEl.previousElementSibling();
        }

        if (prevEl != null) {
            int colspan = 1;
            String val = prevEl.attr("colspan");
            if (StringUtils.isNotEmpty(val)) {
                colspan = Integer.parseInt(val);
            }
            int x = horizontalDerivation(prevEl);
            return x + colspan;
        }
        return 0;
    }

    // 第二次colspan推演，即递归纵向推导
    private void verticalDerivation(List<TableCellInfo> cells) {
        for (int i = 0; i < cells.size(); i++) {
            TableCellInfo currentCell = cells.get(i);
            boolean actedPush = false;
            do {
                int itemIndex = -1;
                for (int j = i - 1; j >= 0; j--) {
                    if (cells.get(j).getX() == currentCell.getX()) {
                        itemIndex = j;
                        break;
                    }
                }
                if (itemIndex >= 0) {
                    if (cells.get(itemIndex).getRowspan() > (currentCell.getY() - cells.get(itemIndex).getY())) {

                        Integer colspan = cells.get(itemIndex).getColspan();
                        if (colspan == null) {
                            colspan = 1;
                        }
                        colspan += currentCell.getX();
                        currentCell.setX(colspan);

                        actedPush = true;
                        for (int k = i + 1; k < cells.size(); k++) {
                            if (cells.get(k).getY() == currentCell.getY()) {
                                colspan = cells.get(itemIndex).getColspan();
                                if (colspan == null) {
                                    colspan = 1;
                                }
                                colspan += cells.get(k).getX();
                                cells.get(k).setX(colspan);
                            }
                        }
                    } else {
                        actedPush = false;
                    }
                } else {
                    actedPush = false;
                }
            } while (actedPush);
        }
    }

    /**
     * 向excel中插入数据
     * 
     * @param callList
     */
    private void insert2Excel(ReportInfo reportInfos, String excelFileName) {
        if (reportInfos != null && !reportInfos.getCells().isEmpty()) {
            List<TableCellInfo> cellList = reportInfos.getCells();
            WritableWorkbook book = this.buildExcel();
            WritableSheet sheet = book.getSheet(0);
            for (TableCellInfo cell : cellList) {
                int colspan = cell.getColspan();
                if (colspan >= 1) {
                    colspan -= 1;
                }
                int rowspan = cell.getRowspan();
                if (rowspan >= 1) {
                    rowspan -= 1;
                }
                int startCol = cell.getX();
                int startRow = cell.getY();
                int endCol = cell.getX() + colspan;
                int endRow = cell.getY() + rowspan;
                log.debug(("开始列，开始行，结束列，结束行" + startCol + "  " + startRow + "  " + endCol + "  " + endRow));
                try {
                    // 合并单元格，参数格式（开始列，开始行，结束列，结束行）
                    sheet.mergeCells(startCol, startRow, endCol, endRow);
                    if (cell.getType() == 1) {
                        sheet.addCell(new Label(cell.getX(), cell.getY(), new String(cell.getContent()), ReportUtil
                                .setCellStyle(cell)));// 修改单元格的值
                    } else if (cell.getType() == 2) {
                        BufferedImage img = ImageIO.read(new ByteArrayInputStream(cell.getContent()));
                        int imgWidth = img.getWidth();
                        int imgHeight = img.getHeight();
                        WritableImage image = new WritableImage(cell.getX(), cell.getY(), 7, 1, cell.getContent());
                        // image.setWidth(7);// 设计图片的宽度
                        // image.setHeight(2);
                        sheet.addImage(image);
                    }
                    ReportUtil.setCellWidthHeight(sheet, cell);
                } catch (Exception e) {
                    log.error("合并单元格 OR 修改单元格信息产生异常");
                    e.printStackTrace();
                }
            }
            this.closeExcel(book);// 关闭流
            this.download(excelFileName, "xls");
        }
    }

    private WritableWorkbook buildExcel() {
        WritableWorkbook book = null;
        try {
            baos = new ByteArrayOutputStream();
            book = Workbook.createWorkbook(baos);
            book.createSheet("Sheet1", 0);
        } catch (IOException e) {
            log.error("创建excel文档失败");
            e.printStackTrace();
        }
        return book;
    }

    private void closeExcel(WritableWorkbook book) {
        try {
            // book.getSheets().clone();
            book.write();
            book.close();
        } catch (IOException e) {
            log.error("EXCEL创建失败！");
            e.printStackTrace();
        } catch (WriteException e) {
            log.error("EXCEL写入失败！");
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
