package com.eastinno.otransos.seafood.trade.domain;
/** 
*@author dll 作者 E-mail：dongliangliang@teleinfo.cn 
*@date 创建时间：2017年1月13日 下午10:35:42
*@version 1.0
*@parameter
*@since
*@return 
*/

public class CalculateDetail {
	private String pro_name;
	private Long pro_id;
	private String spec_name;
	private Integer num;
	private Double totalPrice;
	private Long finalnum;
	private Double finalPrice;
	public String getPro_name() {
		return pro_name;
	}
	public void setPro_name(String pro_name) {
		this.pro_name = pro_name;
	}
	
	public Long getPro_id() {
		return pro_id;
	}
	public void setPro_id(Long pro_id) {
		this.pro_id = pro_id;
	}
	
	public String getSpec_name() {
		return spec_name;
	}
	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	public Long getFinalnum() {
		return finalnum;
	}
	public void setFinalnum(Long finalnum) {
		this.finalnum = finalnum;
	}
	public Double getFinalPrice() {
		return finalPrice;
	}
	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}
	
}
