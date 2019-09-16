package com.eastinno.otransos.seafood.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import com.eastinno.otransos.core.domain.SystemRegion;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.domain.Follower;
import com.eastinno.otransos.platform.weixin.util.WeixinBaseUtils;
import com.eastinno.otransos.seafood.distribu.domain.ShopDistributor;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.usercenter.domain.ShopMember;
import com.eastinno.otransos.seafood.usercenter.domain.ShopSinceSome;

public class shopMsgUtil {
	public static String getPaySuccessMsg(ShopMember member,ShopOrderInfo order){
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ShopSinceSome ss = order.getShopSinceSome();
		if(ss != null){
			ShopDistributor dis = ss.getShopDistributor();
			if(dis != null){
				String address = "【地址";
				SystemRegion region = ss.getArea();
				if(region != null){
					SystemRegion pregion = region.getParent();
					if(pregion != null){
						SystemRegion ppregion = pregion.getParent();
						if(ppregion != null){
							address = address + ":"+ppregion.getTitle()+"-"+pregion.getTitle()+"-"+region.getTitle()+"-"+dis.getOpenAccountAddress()+"】";
						}else{
							address = address + ":"+pregion.getTitle()+"-"+region.getTitle()+"-"+dis.getOpenAccountAddress()+"】";
						}
					}else{
						address = address + ":"+region.getTitle()+"-"+dis.getOpenAccountAddress()+"】";
					}
				}else{
					address = address + ":"+dis.getOpenAccountAddress()+"】";
				}
				String tel = "【店家联系电话："+dis.getMember().getTel()+"】";
				return "【舟山海鲜之家控股有限公司】尊敬的客户"+member.getNickname()+"您好！您于"+sf.format(order.getCeateDate())+"所下订单:"+order.getCode()+"已成功了，"+address+tel+"，如需服务，请致电400-9261088";
			}
			return "【舟山海鲜之家控股有限公司】尊敬的客户"+member.getNickname()+"您好！您于"+sf.format(order.getCeateDate())+"所下订单:"+order.getCode()+"已成功，如需服务，请致电400-9261088";
		}else{
			return "【舟山海鲜之家控股有限公司】尊敬的客户"+member.getNickname()+"您好！您于"+sf.format(order.getCeateDate())+"所下订单:"+order.getCode()+"已成功，我们将及时为您发货，如需服务，请致电400-9261088";
		}
	}
	
	public static void SendRefundInfo(ShopMember member,ShopOrderInfo order,String type){
		Follower f=member.getFollower();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if("1".equals(type)){
			if(f!=null){
				Account a = f.getAccount();
				WeixinBaseUtils.sendMsgToFollower(a, f, "【舟山海鲜之家控股有限公司】尊敬的客户"+member.getNickname()+"您好！您于"+sf.format(order.getCeateDate())+"所下订单:"+order.getCode()+"已退款\\退货，如需服务，请致电400-9261088；");
			}
		}else{
			if(f!=null){
				Account a = f.getAccount();
				WeixinBaseUtils.sendMsgToFollower(a, f, "【舟山海鲜之家控股有限公司】尊敬的客户"+member.getNickname()+"您好！您于"+sf.format(order.getCeateDate())+"所下订单:"+order.getCode()+"已拒绝退款\\退货，如需服务，请致电400-9261088；");
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
