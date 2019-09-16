//销售订单
TabOrdersPanel=Ext.extend(BaseProductBillPanel,{
    id:"ordersPanel",
    baseUrl:"orders.ejf",
    gridForceFit:true,
    baseQueryParameter:{types:5},
    viewWin:{width:700,height:400,title:"销售订单详情"},
    onView:function(win,data){
   		if (win) {
			win.setWidth(700);
			win.setHeight(400);
			win.center();
			win.doLayout();
			var entrys=data.items;
			this.viewGrid.store.removeAll();			
			if(entrys&&entrys.length){
				var pageList={rowCount:entrys.length,result:entrys};	
				this.viewGrid.store.loadData(pageList);
			}
			else {
				this.loadItems(data.id,this.viewGrid);
			}
		}
   	},
    onCreate:function(){
        TabOrdersPanel.superclass.onCreate.call(this);
        this.fp.form.findField("buyTime").clearDirty();
    },
    createForm : function(){
    	var editGrid =this.createEditGrid();
    	var objectRender = this.objectRender('name');
    	var buildMainTab = function(){
			var Util = Disco.Ext.Util;
			return {
				frame:true,
				border:false,
				layout:'form',
				items : [
					{xtype:"hidden",name:"id"},
                    Util.twoColumnPanelBuild({FC:{labelWidth:80,anchor:"-20"}},
                        {fieldLabel:'编号',name:'sn',allowBlank:false,msgTarget:'qtip',readOnly:true},
                        Ext.apply({},{
                        	allowBlank:false,
                        	msgTarget:'qtip',
                        	choiceValue:function(o){
	                        	var fp = formPanel.form;
	                            if(!fp.findField("consigneeAddress").getValue()){
	                                fp.findField("consigneeAddress").setValue(o.address||"");
	                            }
	                            if(!fp.findField("consigneePhone").getValue()){
	                                fp.findField("consigneePhone").setValue(o.tel||"");
	                            }
                        	}.createDelegate(this)
                       	},ConfigConst.CRM.client),
                        {xtype:"datefield",fieldLabel:'下单时间',name:'buyTime',format:"Y-m-d",msgTarget:'qtip',allowBlank:false,value:new Date()},
                        {xtype:"datefield",fieldLabel:'交货时间',name:'deliveryTime',format:"Y-m-d",msgTarget:'qtip',allowBlank:false},
                        {fieldLabel:'联系电话',name:'consigneePhone',msgTarget:'qtip',allowBlank:false},
	               		{fieldLabel:'联系地址',name:'consigneeAddress',msgTarget:'qtip',allowBlank:false},
	               		{fieldLabel:'备注',name:'remark',msgTarget:'qtip',allowBlank:false},
	               		Ext.apply({},{fieldLabel : '业务员'}, ConfigConst.CRM.seller), 
	               		{
	               			xtype : "labelfield",
	               			fieldLabel : '制单',
	               			name : "inputUser",
	               			value : {trueName:"$!{session.EASYJF_SECURITY_USER.trueName}"},
	               			renderer:objectRender
	               		},{
	               			xtype : "labelfield",
	                    	fieldLabel : '审核',
	                    	name : "auditor",
	                    	renderer:objectRender
	                	}
                    )
				]
			}
		}
    	var formPanel = new Ext.form.FormPanel({
    		layout:'fit',
            enterNavigationKey:true,
           	navigationSequences:["client","deliveryTime",this.editGrid.id],
            tbar : this.createFormToolBar(),
			items:[{
				xtype:"tabpanel",
				activeTab: 0,
				deferredRender:false,
				items:[{
					title:"主单内容",
					layout:'fit',
					items:buildMainTab()
				},{
					title:"订单明细",
					layout:'fit',
					items:editGrid
				}]
			}]
    	});
    	return formPanel;
    },
    createWin:function(callback,autoClose){
        return this.initWin(700,400,"销售订单",callback,autoClose);
    },
    storeMapping:["id","sn","types","user","client","totalSum","moneyType","buyTime","deliveryTime","consigneeAddress","consigneeName","consigneeZipCode","consigneePhone","isInvoice","invoiceContent","consigneeEmail","consigneeMobile","payType","payment","deliveryType","delivery","ip","remark","status","storefront","seller","inputUser","inputTime","status","items","auditor","distributor"],
    initEditGridListeners:function(){
    	this.baseEditGridListeners = {
    		scope : this,
    		afteredit : function(e){
    			var fields = ['price','num'];
    			if(fields.contains(e.field)){
    				var record = e.record ;
    				this.autoCountData(record);
    			}
    		}
    	}
    },
    initComponent : function(){
    	this.initEditGridListeners();
        this.cm=new Ext.grid.ColumnModel([
            {header: "编号", sortable:true,width: 80, locked:true,dataIndex:"sn"},
            {header:"客户名称", sortable:true,width:120, locked:true, dataIndex:"client",renderer:this.objectRender("name")},
            {header:"业务员", sortable:true,width: 60, dataIndex:"seller",renderer:this.objectRender("trueName")},
            {header: "金额", sortable:true,width: 80, dataIndex:"totalSum"},
            {header: "下单时间", sortable:true,width: 90, dataIndex:"buyTime",renderer:this.dateRender("Y-m-d")},
            {header: "交货时间", sortable:true,width: 90, dataIndex:"deliveryTime",renderer:this.dateRender("Y-m-d")},
            {header: "订单类型", sortable:true,hidden:true,width: 90, dataIndex:"types",renderer:this.objectRender("title")},
            {header: "备注", sortable:true,width: 200, dataIndex:"remark"},
            {header: "传真", sortable:true,width: 80, dataIndex:"fax",hidden:true},
            {header: "地址", sortable:true,width: 120, dataIndex:"address",hidden:true},
            {header: "邮编", sortable:true,width: 60, dataIndex:"zip",hidden:true},
            {header: "状态", sortable:true,width: 60, dataIndex:"status",renderer:this.statusRender(this.status)},
            {
                header : "操作",
                sortable : true,
                width : 100,
                dataIndex : "status",
                renderer:this.operateRender
            }
        ]);
        TabOrdersPanel.superclass.initComponent.call(this);
    }     
});