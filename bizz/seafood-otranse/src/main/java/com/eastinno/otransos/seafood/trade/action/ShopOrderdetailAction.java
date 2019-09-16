package com.eastinno.otransos.seafood.trade.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.eastinno.otransos.container.annonation.Action;
import com.eastinno.otransos.container.annonation.Inject;
import com.eastinno.otransos.core.support.query.QueryObject;
import com.eastinno.otransos.web.ajax.AjaxUtil;
import com.eastinno.otransos.web.core.AbstractPageCmdAction;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ActionContext;
import com.eastinno.otransos.web.Module;
import com.eastinno.otransos.web.Page;
import com.eastinno.otransos.web.WebForm;
import com.eastinno.otransos.web.tools.IPageList;
import com.eastinno.otransos.seafood.droduct.domain.ShopProduct;
import com.eastinno.otransos.seafood.droduct.service.IShopProductService;
import com.eastinno.otransos.seafood.trade.domain.CalculateDetail;
import com.eastinno.otransos.seafood.trade.domain.OrderDetailShow;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderInfo;
import com.eastinno.otransos.seafood.trade.domain.ShopOrderdetail;
import com.eastinno.otransos.seafood.trade.service.IShopOrderdetailService;
import com.eastinno.otransos.seafood.util.DateUtil;
import com.eastinno.otransos.seafood.util.FileUtils;

/**
 * ShopOrderdetailAction
 * @author nsz
 */
@Action
public class ShopOrderdetailAction extends AbstractPageCmdAction {
    @Inject
    private IShopOrderdetailService service;
    @Inject
    private IShopProductService shopProductService;
    /**
     * 默认方法
     * @param form
     * @param module
     * @return
     */
    public Page doInit(WebForm form, Module module) {
        return go("list");
    }
    /**
     * 列表页面
     * 
     * @param form
     */
    public Page doList(WebForm form) {
        QueryObject qo = (QueryObject) form.toPo(QueryObject.class);
        IPageList pl = this.service.getShopOrderdetailBy(qo);
        form.addResult("pl", pl);
        return new Page("/shopmanage/trade/shopOrderdetail/shopOrderdetailList.html");
    }
    /**
     * 进入添加页面
     * @param form
     * @return
     */
    public Page doToSave(WebForm form){
    	return new Page("/shopmanage/trade/shopOrderdetail/shopOrderdetailEdit.html");
    }
    /**
     * 保存数据
     * 
     * @param form
     */
    public Page doSave(WebForm form) {
        ShopOrderdetail entry = (ShopOrderdetail)form.toPo(ShopOrderdetail.class);
        form.toPo(entry);
        if (!hasErrors()) {
            Long id = this.service.addShopOrderdetail(entry);
            if (id != null) {
                form.addResult("msg", "添加成功");
            }
        }
        return go("list");
    }
    /**
     * 导入编辑页面，根据id值导入
     * 
     * @param form
     */
    public Page doToEdit(WebForm form) {
        String idStr = CommUtil.null2String(form.get("id"));
        if(!"".equals(idStr)){
            Long id = Long.valueOf(Long.parseLong(idStr));
            ShopOrderdetail entry = this.service.getShopOrderdetail(id);
            form.addResult("entry", entry);
        }
        return new Page("/shopmanage/trade/shopOrderdetail/shopOrderdetailEdit.html");
    }
    /**
     * 修改数据
     * 
     * @param form
     */
    public Page doUpdate(WebForm form) {
        Long id = Long.valueOf(Long.parseLong(CommUtil.null2String(form.get("id"))));
        ShopOrderdetail entry = this.service.getShopOrderdetail(id);
        form.toPo(entry);
        if (!hasErrors()) {
            boolean ret = service.updateShopOrderdetail(id, entry);
            if(ret){
                form.addResult("msg", "修改成功");
            }
        }
        return go("list");
    }
    
    /**
     * 删除数据
     * 
     * @param form
     */
    public Page doRemove(WebForm form) {
        Long id = new Long(CommUtil.null2String(form.get("id")));
        this.service.delShopOrderdetail(id);
        return go("list");
    }
    
	/**
	 * 统计当月订单
	 * @param form
	 * @return
	 */
	public Page doCalculateProductByMonth(WebForm form){
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.DAY_OF_MONTH, 1);  
		//将小时至0  
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		//将分钟至0  
		calendar.set(Calendar.MINUTE, 0);  
		//将秒至0  
		calendar.set(Calendar.SECOND,0);  
		//将毫秒至0  
		calendar.set(Calendar.MILLISECOND, 0);  
		//获得当前月第一天  
		Date sdate = calendar.getTime();  
		
		
		//将当前月加1；  
		calendar.add(Calendar.MONTH, 1);  
		//在当前月的下一月基础上减去1毫秒  
		calendar.add(Calendar.MILLISECOND, -1);  
		//获得当前月最后一天  
		Date edate = calendar.getTime();
		QueryObject obj = new QueryObject();
		obj.setPageSize(-1);
		List<CalculateDetail> listc = new ArrayList<>();
		Long finalnum=0l;
		Double finalprice = 0.0;
		List<ShopProduct> listp = this.shopProductService.getShopProductBy(obj).getResult();
		if(listp != null && listp.size() != 0){
			for(ShopProduct pro:listp){
				List<ShopOrderdetail> list = this.service.getOrderDetailByMothPro(sdate, edate,pro.getId());
				Integer count = 0;
				Double countprice = 0.0;
				if(list != null && list.size() !=0 ){
					for(ShopOrderdetail detail:list){
						finalnum += detail.getNum();
						finalprice += detail.getGross_price();
						count += detail.getNum();
						countprice += detail.getGross_price();
					}
					CalculateDetail detail = new CalculateDetail();
					detail.setNum(count);
					detail.setTotalPrice(countprice);
					detail.setPro_name(pro.getName());
					detail.setPro_id(pro.getId());
					listc.add(detail);
				}
			}
		}
		form.addResult("list",listc);
		form.addResult("finalnum", finalnum);
		form.addResult("finalprice", finalprice);
		return new Page("/bcd/count/countprobymoth.html");  
	}
	/**
	 * 统计历史订单
	 * @param form
	 * @return
	 */
	public Page doCalculateProductAll(WebForm form){
		QueryObject obj = new QueryObject();
		obj.setPageSize(-1);
		List<CalculateDetail> listc = new ArrayList<>();
		List<ShopProduct> listp = this.shopProductService.getShopProductBy(obj).getResult();
		Long finalnum=0l;
		Double finalprice = 0.0;
		if(listp != null && listp.size() != 0){
			for(ShopProduct pro:listp){
				List<ShopOrderdetail> list = this.service.getOrderDetailAllPro(pro.getId());
				Integer count = 0;
				Double countprice = 0.0;
				if(list != null && list.size() !=0 ){
					for(ShopOrderdetail detail:list){
						finalnum += detail.getNum();
						finalprice += detail.getGross_price();
						count += detail.getNum();
						countprice += detail.getGross_price();
					}
					CalculateDetail detail = new CalculateDetail();
					detail.setNum(count);
					detail.setTotalPrice(countprice);
					detail.setPro_name(pro.getName());
					detail.setPro_id(pro.getId());
					listc.add(detail);
				}
			}
		}
		form.addResult("list",listc);
		form.addResult("finalnum", finalnum);
		form.addResult("finalprice", finalprice);
		return new Page("/bcd/count/countproall.html");  
	}
	
	public Page doGetOrderDetailByMoth(WebForm form){
		Calendar calendar = Calendar.getInstance();  
		calendar.set(Calendar.DAY_OF_MONTH, 1);  
		//将小时至0  
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		//将分钟至0  
		calendar.set(Calendar.MINUTE, 0);  
		//将秒至0  
		calendar.set(Calendar.SECOND,0);  
		//将毫秒至0  
		calendar.set(Calendar.MILLISECOND, 0);  
		//获得当前月第一天  
		Date sdate = calendar.getTime();  
		
		
		//将当前月加1；  
		calendar.add(Calendar.MONTH, 1);  
		//在当前月的下一月基础上减去1毫秒  
		calendar.add(Calendar.MILLISECOND, -1);  
		//获得当前月最后一天  
		Date edate = calendar.getTime();
		List<OrderDetailShow> listd = new ArrayList<>();
		List<ShopOrderdetail> list = this.service.getOrderDetailByMoth(sdate,edate);
		if(list != null && list.size() != 0){
			for(ShopOrderdetail order:list){
				OrderDetailShow show = new OrderDetailShow();
				ShopOrderInfo info = order.getOrderInfo();
				show.setTradedate(DateUtil.getDateStr(info.getTradeDate()));
				show.setOrdercode(info.getCode());
				
				if(order.getShopSpec() != null){
					show.setSpec_name(order.getShopSpec().getName());
				}
				show.setPro_name(order.getPro().getName());
				show.setPro_id(order.getPro().getId());
				show.setNum(order.getNum());
				show.setTotalPrice(order.getGross_price());
				show.setUser_id(order.getUser().getId());
				show.setUser_name(order.getUser().getNickname());
				
				listd.add(show);
			}
		}
		form.addResult("list",listd);
		return new Page("/bcd/count/detailbymonth.html");
	}
	public Page doGetOrderDetailAll(WebForm form){
		List<OrderDetailShow> listd = new ArrayList<>();
		List<ShopOrderdetail> list = this.service.getOrderDetailAll();
		if(list != null && list.size() != 0){
			for(ShopOrderdetail order:list){
				OrderDetailShow show = new OrderDetailShow();
				ShopOrderInfo info = order.getOrderInfo();
				show.setTradedate(DateUtil.getDateStr(info.getTradeDate()));
				show.setOrdercode(info.getCode());
				
				if(order.getShopSpec() != null){
					show.setSpec_name(order.getShopSpec().getName());
				}
				show.setPro_name(order.getPro().getName());
				show.setPro_id(order.getPro().getId());
				show.setNum(order.getNum());
				show.setTotalPrice(order.getGross_price());
				show.setUser_id(order.getUser().getId());
				show.setUser_name(order.getUser().getNickname());
				
				listd.add(show);
			}
		}
		form.addResult("list",listd);
		return new Page("/bcd/count/detailbyall.html");
	}
    
	@SuppressWarnings("deprecation")
	public Page doExport(WebForm form) throws ParseException{
		
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("订单明细");
        sheet.setDefaultColumnWidth((short)15);
        HSSFCellStyle style = wb.createCellStyle();
        HSSFRow row = sheet.createRow(0);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //给表头第一行一次创建单元格
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("序号"); 
        cell.setCellStyle(style);
        cell = row.createCell((short) 1);  
        cell.setCellValue("交易时间");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 2);  
        cell.setCellValue("订单类型");  
        cell.setCellStyle(style);
        cell.setCellStyle(style);
        cell = row.createCell((short) 3);  
        cell.setCellValue("取货类型");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 4);  
        cell.setCellValue("订单编号");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 5);  
        cell.setCellValue("客户昵称");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 6);  
        cell.setCellValue("收货人姓名");  
        cell.setCellStyle(style);
        cell = row.createCell( (short) 7);  
        cell.setCellValue("电话");  
        cell.setCellStyle(style);
        cell = row.createCell( (short) 8);  
        cell.setCellValue("地址");  
        cell.setCellStyle(style);
        cell = row.createCell( (short) 9);  
        cell.setCellValue("商品名称");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 10);  
        cell.setCellValue("规格名称");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 11);  
        cell.setCellValue("单价");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 12);  
        cell.setCellValue("数量");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 13);  
        cell.setCellValue("金额");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 14);  
        cell.setCellValue("买家备注");  
        cell.setCellStyle(style);
        cell = row.createCell((short) 15);  
        cell.setCellValue("上级");  
        cell.setCellStyle(style); 
        cell = row.createCell((short) 16);  
        cell.setCellValue("客户id");  
        cell.setCellStyle(style);
        Date sdate;
        Date edate;
        Calendar calendar = Calendar.getInstance();
        if(form.get("startDate") != null && !"".equals(form.get("startDate"))){
    		sdate = DateUtil.getStrDate(form.get("startDate").toString(),"yyyy-MM-dd HH:mm:ss");
        }else{  
    		calendar.set(Calendar.DAY_OF_MONTH, 1);  
    		calendar.set(Calendar.HOUR_OF_DAY, 0);  
    		calendar.set(Calendar.MINUTE, 0);  
    		calendar.set(Calendar.SECOND,0);  
    		calendar.set(Calendar.MILLISECOND, 0);  
    		sdate = calendar.getTime();  
        }
        
        if(form.get("endDate") != null && !"".equals(form.get("endDate"))){
    		edate = DateUtil.getStrDate(form.get("endDate").toString(),"yyyy-MM-dd HH:mm:ss");
        }else{
        	calendar.add(Calendar.MONTH, 1);  
    		calendar.add(Calendar.MILLISECOND, -1);  
    		edate = calendar.getTime();
        }

		
		List<OrderDetailShow> listd = new ArrayList<>();
		List<ShopOrderdetail> listo = this.service.getOrderDetailByMoth(sdate,edate);
		if(listo != null && listo.size() != 0){
			for(ShopOrderdetail order:listo){
				OrderDetailShow show = new OrderDetailShow();
				ShopOrderInfo info = order.getOrderInfo();
				show.setTradedate(DateUtil.getDateStr(info.getTradeDate()));
				show.setOrdercode(info.getCode());
				
				if(order.getShopSpec() != null){
					show.setSpec_name(order.getShopSpec().getName());
				}
				if(info.getShopSinceSome() != null){
					show.setRecievetype("自提");
					show.setReceveName(info.getShopSinceSome().getTrueName());
					show.setTelephone(info.getShopSinceSome().getTelephone());
					show.setAddress(info.getShopSinceSome().getShopDistributor().getArea().getFullTitle()+"  "+info.getShopSinceSome().getShopDistributor().getOpenAccountAddress());
				}else{
					show.setRecievetype("快递");
					show.setReceveName(info.getAddr().getTrueName());
					show.setTelephone(info.getAddr().getTelephone());
					show.setAddress(info.getAddr().getArea().getFullTitle()+"  "+info.getAddr().getArea_info());
				}
				if("timelimit".equals(info.getType())){
					show.setType("团购");
				}else if("normal".equals(info.getType())){
					show.setType("普通");
				}else if("seckill".equals(info.getType())){
					show.setType("抢购");
				}else{
					show.setType("其它");
				}
				show.setPro_name(order.getPro().getName());
				show.setPro_id(order.getPro().getId());
				show.setSinglePrice(order.getUnit_price());
				show.setNum(order.getNum());
				show.setTotalPrice(order.getGross_price());
				show.setUser_id(order.getUser().getId());
				show.setUser_name(order.getUser().getNickname());
				show.setMsg_self(info.getMsg_self());
				if(info.getTopDistributor() != null){
					show.setParent(info.getTopDistributor().getMember().getNickname());
				}
				listd.add(show);
			}
		}
		
	   //向单元格里填充数据
		for (short i = 0; i < listd.size(); i++) {
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(i+1);
			row.createCell(1).setCellValue(listd.get(i).getTradedate());
			row.createCell(2).setCellValue(listd.get(i).getType());
			row.createCell(3).setCellValue(listd.get(i).getRecievetype());
			row.createCell(4).setCellValue(listd.get(i).getOrdercode());
			row.createCell(5).setCellValue(listd.get(i).getUser_name());
			row.createCell(6).setCellValue(listd.get(i).getReceveName());
			row.createCell(7).setCellValue(listd.get(i).getTelephone());
			row.createCell(8).setCellValue(listd.get(i).getAddress());
			row.createCell(9).setCellValue(listd.get(i).getPro_name());
			row.createCell(10).setCellValue(listd.get(i).getSpec_name());
			row.createCell(11).setCellValue(listd.get(i).getSinglePrice());
			row.createCell(12).setCellValue(listd.get(i).getNum());
			row.createCell(13).setCellValue(listd.get(i).getTotalPrice());
			row.createCell(14).setCellValue(listd.get(i).getMsg_self());
			row.createCell(15).setCellValue(listd.get(i).getParent());
			row.createCell(16).setCellValue(listd.get(i).getUser_id());
			}
		try {
			String s=Thread.currentThread().getContextClassLoader().getResource("").getPath();
			System.out.println("================"+s+"====================");
	        s = s.substring(0,s.length()-17) + "/static/OrderDetailCurrentMonth("+DateUtil.getDateStr(sdate)+"-"+DateUtil.getDateStr(edate)+").xls";
			File f = new File(s);
		    if (f.exists()){
		    	f.delete();
		    }
		    f.createNewFile();	
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            System.out.println("导出成功!");
        } catch (FileNotFoundException e) {
            System.out.println("导出失败!");
            e.printStackTrace();
        } catch (IOException e) {
        	System.out.println("导出失败!");
            e.printStackTrace();
        }
		
		//ServletContext sc = ActionContext.getContext().getServletContext();
    	//String s = sc.getRealPath("/WEB-INF/static/OrderDetailCurrentMonth.xls");
		String s = "static/OrderDetailCurrentMonth("+DateUtil.getDateStr(sdate)+"-"+DateUtil.getDateStr(edate)+").xls";
		form.addResult("startDate",DateUtil.getDateStr(sdate));
		form.addResult("endDate",DateUtil.getDateStr(edate));
    	form.addResult("str",s);
    	form.addResult("list",listd);
		return new Page("/bcd/count/detailbymonth.html");
    }
	
	
	@SuppressWarnings("unchecked")
	public Page doExportAll(WebForm form){
		Map map = new HashMap<>();
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        //声明一个单子并命名
        HSSFSheet sheet = wb.createSheet("历史订单明细");
        //给单子名称一个长度
        sheet.setDefaultColumnWidth((short)15);
        // 生成一个样式  
        HSSFCellStyle style = wb.createCellStyle();
        //创建第一行（也可以称为表头）
        HSSFRow row = sheet.createRow(0);
        //样式字体居中
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        
        //给表头第一行一次创建单元格
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue("序号"); 
        cell.setCellStyle(style);
        
        cell = row.createCell( (short) 1);  
        cell.setCellValue("商品名称");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("商品id");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("规格名称");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("交易时间");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("订单编号");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("数量");  
        cell.setCellStyle(style);

        
        cell = row.createCell((short) 7);  
        cell.setCellValue("金额");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 8);  
        cell.setCellValue("客户昵称");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 9);  
        cell.setCellValue("客户id");  
        cell.setCellStyle(style); 

		List<OrderDetailShow> listd = new ArrayList<>();
		List<ShopOrderdetail> list = this.service.getOrderDetailAll();
		if(list != null && list.size() != 0){
			for(ShopOrderdetail order:list){
				OrderDetailShow show = new OrderDetailShow();
				ShopOrderInfo info = order.getOrderInfo();
				show.setTradedate(DateUtil.getDateStr(info.getTradeDate()));
				show.setOrdercode(info.getCode());
				
				if(order.getShopSpec() != null){
					show.setSpec_name(order.getShopSpec().getName());
				}
				show.setPro_name(order.getPro().getName());
				show.setPro_id(order.getPro().getId());
				show.setNum(order.getNum());
				show.setTotalPrice(order.getGross_price());
				show.setUser_id(order.getUser().getId());
				show.setUser_name(order.getUser().getNickname());
				
				listd.add(show);
			}
		}
		
	   //向单元格里填充数据
		for (short i = 0; i < listd.size(); i++) {
			row = sheet.createRow(i + 1);
			row.createCell(0).setCellValue(i+1);
			row.createCell(1).setCellValue(listd.get(i).getPro_name());
			row.createCell(2).setCellValue(listd.get(i).getPro_id());
			row.createCell(3).setCellValue(listd.get(i).getSpec_name());
			row.createCell(4).setCellValue(listd.get(i).getTradedate());
			row.createCell(5).setCellValue(listd.get(i).getOrdercode());
			row.createCell(6).setCellValue(listd.get(i).getNum());
			row.createCell(7).setCellValue(listd.get(i).getTotalPrice());
			row.createCell(8).setCellValue(listd.get(i).getUser_name());
			row.createCell(9).setCellValue(listd.get(i).getUser_id());
			}
		try {
			String s=Thread.currentThread().getContextClassLoader().getResource("").getPath();
	        s = s.substring(0,s.length()-17) + "/static/OrderDetail.xls";
	        
			File f = new File(s);
		    if (f.exists()){
		    	f.delete();
		    }
		    f.createNewFile();	
            FileOutputStream out = new FileOutputStream(f);
            wb.write(out);
            out.close();
            map.put("info", "导出成功!");
            System.out.println("导出成功!");
        } catch (FileNotFoundException e) {
        	map.put("info", "导出失败!");
            System.out.println("导出失败!");
            e.printStackTrace();
        } catch (IOException e) {
        	map.put("info", "导出失败!");
        	System.out.println("导出失败!");
            e.printStackTrace();
        }
		
		//ServletContext sc = ActionContext.getContext().getServletContext();
    	//String s = sc.getRealPath("/WEB-INF/static/OrderDetail.xls");
		String s = "static/OrderDetail.xls";
    	map.put("str", s);
    	form.jsonResult(map);
		return Page.JSONPage;
    }
	
	
    public void setService(IShopOrderdetailService service) {
        this.service = service;
    }
	public IShopProductService getShopProductService() {
		return shopProductService;
	}
	public void setShopProductService(IShopProductService shopProductService) {
		this.shopProductService = shopProductService;
	}
    
}