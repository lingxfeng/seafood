package com.eastinno.otransos.core.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.springframework.util.StringUtils;

/**
 * 处理导入Excel
 * 
 * @version 2.0
 * @author lengyu
 * @date 2011年4月12日-下午11:50:48
 */
public class ExcelImport {
    private String title;// 工作表标题(优先通过工作表标题获取工作表)
    private InputStream in;
    private int beginRow = 1;// 从指定行开始读取
    private String[][] fields;
    private int sheetIndex = 0;// 工作表序号索引
    private ImportService service;
    private String splitTag = null;
    private String splitField = null;

    public ExcelImport(InputStream in, ImportService service, String[] fields) {
        this.in = in;
        this.service = service;
        this.fields = new String[fields.length][2];
        for (int i = 0; i < fields.length; i++) {
            this.fields[i][0] = i + "";
            this.fields[i][1] = fields[i];
        }
    }

    public ExcelImport(InputStream in, ImportService service, String[][] fields) {
        this.in = in;
        this.service = service;
        this.fields = fields;
    }

    /**
     * 开始执行解析
     * 
     * @throws Exception
     */
    public void run() throws Exception {
        if ((this.in == null) || (this.service == null))
            return;
        WorkbookSettings workbooksetting = new WorkbookSettings();
        workbooksetting.setCellValidationDisabled(true);
        Workbook book = Workbook.getWorkbook(this.in, workbooksetting);
        Sheet sheet = book.getSheet(this.title);
        if (sheet == null)
            sheet = book.getSheet(this.sheetIndex);
        String split = null;
        Integer sequence = Integer.valueOf(0);
        for (int i = this.beginRow; i < sheet.getRows(); i++) {
            try {
                Map map = new HashMap();
                for (int j = 0; j < this.fields.length; j++) {
                    int c = new Integer(this.fields[j][0]).intValue();
                    if (c < sheet.getColumns()) {
                        Cell cell = sheet.getCell(c, i);
                        map.put(this.fields[j][1], cell.getContents());
                    }
                }
                if ((this.splitField != null) && (this.splitTag != null) && (!StringUtils.hasLength((String) map.get(this.splitTag)))) {
                    split = (String) map.get(this.fields[0][1]);
                    sequence = Integer.valueOf(sequence.intValue() + 1);
                } else {
                    if ((this.splitField != null) && (split != null))
                        map.put(this.splitField, split);
                    map.put("typesSequence", sequence);
                    map.put("excelCell", sheet.getRow(i));// 当前行
                    this.service.doImport(map);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        book.close();
    }

    public int getSheetIndex() {
        return this.sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public void setBeginRow(int beginRow) {
        this.beginRow = beginRow;
    }

    public void setFields(String[][] fields) {
        this.fields = fields;
    }

    public void setSplitTag(String splitTag) {
        this.splitTag = splitTag;
    }

    public void setSplitField(String splitField) {
        this.splitField = splitField;
    }

    public static abstract interface ImportService {
        public abstract void doImport(Map paramMap);
    }
}
