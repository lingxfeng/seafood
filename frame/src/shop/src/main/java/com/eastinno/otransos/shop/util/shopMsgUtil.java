package com.eastinno.otransos.shop.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.shop.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.shop.usercenter.domain.ShopMember;

public class shopMsgUtil {
	public static String getPaySuccessMsg(ShopMember member,ShopOrderInfo order){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "【百春达电子商务有限公司】尊敬的客户"+member.getNickname()+"您好！您于"+sf.format(order.getCeateDate())+"所下订单:"+order.getCode()+"已成功，我们将及时为您发货，如需服务，请致电010-53646367";
	}
	
	public static void SendRefundInfo(ShopMember member,ShopOrderInfo order,String type){
		Follower f=member.getFollower();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if("1".equals(type)){
			if(f!=null){
				Account a = f.getAccount();
				WeixinBaseUtils.sendMsgToFollower(a, f, "【百春达电子商务有限公司】尊敬的客户"+member.getNickname()+"您好！您于"+sf.format(order.getCeateDate())+"所下订单:"+order.getCode()+"已退款\\退货，如需服务，请致电010-53646367；");
			}
		}else{
			if(f!=null){
				Account a = f.getAccount();
				WeixinBaseUtils.sendMsgToFollower(a, f, "【百春达电子商务有限公司】尊敬的客户"+member.getNickname()+"您好！您于"+sf.format(order.getCeateDate())+"所下订单:"+order.getCode()+"已拒绝退款\\退货，如需服务，请致电010-53646367；");
			}
		}
	}
	
	public static Double getPrice(Double d1,Double d2,Double d3){
		BigDecimal d1_ = new BigDecimal(String.valueOf(d1));
		BigDecimal d2_ = new BigDecimal(String.valueOf(d2));
		BigDecimal d3_ = new BigDecimal(String.valueOf(d3));
		return d1_.add(d2_).add(d3_).doubleValue();
	}
	
	public static void main(String[] args) {
		double d1=10;
		double d2=1;
		double d3=5;
		getPrice(d1,d2,d3);
	}
}
