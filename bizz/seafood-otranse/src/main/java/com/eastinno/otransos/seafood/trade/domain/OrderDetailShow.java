package com.eastinno.otransos.seafood.trade.domain;

import java.util.Date;

/** 
*@author dll 作者 E-mail：dongliangliang@teleinfo.cn 
*@date 创建时间：2017年1月13日 下午10:35:42
*@version 1.0
*@parameter
*@since
*@return 
*/

public class OrderDetailShow {
	private String pro_name;//产品名称
	private Long pro_id;//产品ID
	private Integer num;//数量
	private Double singlePrice;//单价
	private Double totalPrice;//总额
	private String user_name;//用户昵称
	private Long user_id;//用户ID
	private String tradedate ;//交易时间
	private String ordercode;//订单号
	private String spec_name;//规格名称
	
	private String receveName ;//收货人名称
	private String telephone;//收货人电话
	private String address;//收货地址
	private String parent="总店";//上级名称
	private String type;//订单类型
	private String recievetype;//取货类型
	private String msg_self;//买家备注
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
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Double getSinglePrice() {
		return singlePrice;
	}
	public void setSinglePrice(Double singlePrice) {
		this.singlePrice = singlePrice;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getTradedate() {
		return tradedate;
	}
	public void setTradedate(String tradedate) {
		this.tradedate = tradedate;
	}
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public String getSpec_name() {
		return spec_name;
	}
	public void setSpec_name(String spec_name) {
		this.spec_name = spec_name;
	}
	public String getReceveName() {
		return receveName;
	}
	public void setReceveName(String receveName) {
		this.receveName = receveName;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRecievetype() {
		return recievetype;
	}
	public void setRecievetype(String recievetype) {
		this.recievetype = recievetype;
	}
	public String getMsg_self() {
		return msg_self;
	}
	public void setMsg_self(String msg_self) {
		this.msg_self = msg_self;
	}
}
