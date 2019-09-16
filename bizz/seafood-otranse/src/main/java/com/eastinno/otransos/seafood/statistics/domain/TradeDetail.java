package com.eastinno.otransos.seafood.statistics.domain;

/**
 * @author dll 作者 E-mail：dongliangliang@teleinfo.cn
 * @date 创建时间：2017年1月24日 下午12:08:39
 * @version 1.0
 * @parameter
 * @since
 * @return
 */
public class TradeDetail {

	private String name;

	private String tid;
	private String ptid;
	private String pid;
	private String pname;
	private String total;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getPtid() {
		return ptid;
	}

	public void setPtid(String ptid) {
		this.ptid = ptid;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

}
