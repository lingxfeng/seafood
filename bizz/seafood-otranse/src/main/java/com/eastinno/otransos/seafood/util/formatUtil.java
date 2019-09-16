package com.eastinno.otransos.seafood.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class formatUtil {
	public static formatUtil fu = new formatUtil();
	public Float getYj(Float fvalue){
		if(fvalue!=null){
			BigDecimal b = new BigDecimal(fvalue);
			float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			return f1;
		}
		return (float) 0.0;
	}
	public Double getYj(Double dvalue){
		if(dvalue!=null){
			BigDecimal b = new BigDecimal(dvalue);
			double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1;
		}
		return 0.0;
	}
	public String getYj(String dvalue){
		if(dvalue!=null){
			BigDecimal b = new BigDecimal(dvalue);
			String f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			return f1;
		}
		return "0.0";
	}
	
	public Double add(Double d1,Double d2){
		BigDecimal d1_ = new BigDecimal(d1);
		BigDecimal d2_ = new BigDecimal(d2);
		return d1_.add(d2_).doubleValue();
	}
	
	public Double subtract(Double d1,Double d2){
		BigDecimal d1_ = new BigDecimal(d1);
		BigDecimal d2_ = new BigDecimal(d2);
		return d1_.subtract(d2_).doubleValue();
	}
	
	public Double add2(String d1,String d2){
		BigDecimal d1_ = new BigDecimal(d1);
		BigDecimal d2_ = new BigDecimal(d2);
		return d1_.add(d2_).doubleValue();
	}
	
	public Double subtract2(String d1,String d2){
		BigDecimal d1_ = new BigDecimal(d1);
		BigDecimal d2_ = new BigDecimal(d2);
		return d1_.subtract(d2_).doubleValue();
	}
	
	public String getDate(Long value){
		Date date = new Date(value);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}
	public String getDate(Date date){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(date);
	}
	public String getDate2(Date date){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(date);
	}
	public String getDate3(Date date){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sf.format(date);
	}
	public Double getYj(Double dvalue,Double dvalue2){
		Double yj = dvalue - dvalue2;
		if(dvalue!=null){
			BigDecimal b = new BigDecimal(yj);
			double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			return f1;
		}
		return 0.0;
	}
	public String add(String level1,String level2,String balance){
			BigDecimal b = new BigDecimal(Double.valueOf(level1)+Double.valueOf(level2)+Double.valueOf(balance));
			String f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
			return f1;
	}
}
