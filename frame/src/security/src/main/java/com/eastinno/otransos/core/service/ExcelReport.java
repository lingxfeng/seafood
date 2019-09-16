package com.eastinno.otransos.core.service;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.web.ActionContext;

public class ExcelReport {
    private String title;
    private String[] lables;
    private String[] fields;
    private List list;
    private OutputStream out;

    public ExcelReport(OutputStream out, String title, String[] lables, String[] fields, List list) {
        this.out = out;
        this.title = title;
        this.lables = lables;
        this.fields = fields;
        this.list = list;
    }

    public ExcelReport(OutputStream out, String[] fields, List list) {
        this(out, "", fields, fields, list);
    }

    public ExcelReport(String title, String[] lables, String[] fields, List list) {
        this(null, title, lables, fields, list);
    }

    public ExcelReport(String title, String[] fields, List list) {
        this(null, title, fields, fields, list);
    }

    public ExcelReport(List list) {
        this(null, null, null, null, list);
    }

    public void export() throws Exception {
        if (this.out == null) {
            this.out = ActionContext.getContext().getResponse().getOutputStream();
            ActionContext.getContext().getResponse().setContentType("APPLICATION/OCTET-STREAM");
            ActionContext
                    .getContext()
                    .getResponse()
                    .setHeader(
                            "Content-Disposition",
                            "attachment; filename=\""
                                    + new String(new StringBuilder(String.valueOf(this.title)).append(".xls")
                                            .toString().getBytes(), "iso8859-1") + "\"");
        }
        if (((this.fields == null) || (this.fields.length < 1)) && (this.list != null) && (!this.list.isEmpty())) {
            Object obj = this.list.get(0);
            if ((obj instanceof Map)) {
                Map m = (Map) obj;
                Iterator it = m.keySet().iterator();
                this.fields = new String[m.size()];
                int i = 0;
                while (it.hasNext())
                    this.fields[(i++)] = it.next().toString();
            } else {
                BeanWrapper wrapper = new BeanWrapperImpl(obj);
                this.fields = new String[wrapper.getPropertyDescriptors().length];
                for (int i = 0; i < this.fields.length; i++) {
                    this.fields[i] = wrapper.getPropertyDescriptors()[i].getName();
                }
            }

        }

        if ((this.lables == null) || (this.lables.length < 1))
            this.lables = this.fields;
        if ((!StringUtils.hasLength(this.title)) && (this.list != null) && (!this.list.isEmpty())) {
            Object obj = this.list.get(0);
            this.title = obj.getClass().getSimpleName();
        }

        WritableWorkbook book = Workbook.createWorkbook(this.out);

        WritableSheet sheet = book.createSheet(this.title, 0);
        if (this.lables != null) {
            for (int l = 0; l < this.lables.length; l++) {
                sheet.addCell(new Label(l, 0, this.lables[l]));
            }

            int row = 1;
            if ((this.list != null) && (!this.list.isEmpty())) {
                for (int i = 0; i < this.list.size(); row++) {
                    Object obj = this.list.get(i);
                    for (int j = 0; j < this.fields.length; j++) {
                        Object v = null;
                        if ((obj instanceof Map)) {
                            Map m = (Map) obj;
                            v = m.get(this.fields[j]);
                        } else {
                            BeanWrapper wrapper = new BeanWrapperImpl(obj);
                            try {
                                if (wrapper.isReadableProperty(this.fields[j]))
                                    v = wrapper.getPropertyValue(this.fields[j]);
                            } catch (Exception e) {
                                v = "";
                            }
                        }
                        v = filter(v, this.fields[j]);
                        if ((v != null) && ((v instanceof Number)))
                            sheet.addCell(new XltNumber(j, row, (Number) v));
                        else
                            sheet.addCell(new Label(j, row, v == null ? "" : formate(v)));
                    }
                    i++;
                }

            }

        }

        book.write();
        book.close();
    }

    public Object filter(Object v, String field) {
        return v;
    }

    public String formate(Object v) {
        if ((v instanceof Boolean)) {
            Boolean b = (Boolean) v;
            if (b.booleanValue())
                return "是";
            if (!b.booleanValue())
                return "否";
            return "";
        }
        if ((v instanceof Date)) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd:hh:mm");
            Date d = (Date) v;
            return f.format(d);
        }
        return v.toString();
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getLables() {
        return this.lables;
    }

    public void setLables(String[] lables) {
        this.lables = lables;
    }

    public String[] getFields() {
        return this.fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public List getList() {
        return this.list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public OutputStream getOut() {
        return this.out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    private class XltNumber extends jxl.write.Number {
        public XltNumber(int col, int row, Number n) {
            super(col, row, n);
        }
    }
}
