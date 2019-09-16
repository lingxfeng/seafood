HotkeysInfo = function(){
	var data=[ /*['换功能页','Ctrl+F1'],*/
			   ['货码','Enter','enterFunction'], 
			   ['颜色','F11','updateColor'],
			   ['尺码','F12','updateSizeType'],
			   ['数量','Insert','updateNumber'],
			/* ['价格','Delete'],*/
			   ['折扣','Home','onDiscount'],
			   ['优惠价','End','onOffers'],
			   ['导购员','PageUp','onSelShippingGuide'],
			   ['付款','PageDown','onPayment'],
			   ['整单优惠','Ctrl+F12','onPreferential'],
			   ['现金','F1','onPaymentMoney'],
			 /*  ['支票','F2'],*/
			   ['信用卡','F3','createCreditCardTypeWin'],
			  /* ['购物券','F4'],
			   ['会员卡','F5'],*/
			   ['挂单','F6'],
			   ['打印','F7'],
			   /*['开钱箱','F8'],*/
			   ['退货','F9'],
			   ['不打印','F10','onNoPrint'],
			   ['补单','Ctrl+B'],
			   ['确认','Enter','enterFunction'],
			   ['取消','Esc','escFunction'],
			   ['删除','Back'],
			   ['上行','↑'],
			   ['下行','↓'],
			 /*  ['赠送','←'],*/
			   ['返现','Ctrl+F2'],
			   ['备用金','Ctrl+F3'],
			   ['交款','Ctrl+F5','onPay'],
			   /*['查询','Ctrl+F5'],
				['修改口令','Ctrl+F7'],
			   ['暂停','Ctrl+F8'],*/
			   ['交班','Ctrl+F9','doChangeCasher'],/*,
			   ['条码','→']*/
			   ['会员卡','Ctrl+F10','vipCard'],
			   ['赠品/销售','F8','gifts'],
			   ['选择赠品','Ctrl+F8']
			   ];
	var store=new  Ext.data.SimpleStore({data:data,fields:["description","hotkeys","action"]});
	HotkeysInfo.superclass.constructor.call(this, {
		id : 'hotkeysInfo',
		region : 'west',
		title : "功能键",
		split : true,
		collapseMode:"mini", 
		width : 160,
		minSize : 120,
		maxSize : 250,
		margins : '0 0 -1 1',
		collapsible : true,
		defaults:{autoScroll:true,border: false},
		layout:"fit",
		items:new Ext.grid.GridPanel({
			  store:store,
			  header:false,
			  hideHeaders:true,
			  //enableHdMenu:false,
			  //headerAsText:false,
			  viewConfig:{
				  getRowClass:function(){
				  			return "x-grid3-ro-pos";
						}
				  },
				  columns:[
					 {width:70,dataIndex:"description"},
					 {width:70,dataIndex:"hotkeys"}]
				  })
		});
		this.grid = this.getComponent(0);
}
Ext.extend(HotkeysInfo, Ext.Panel);

PosProductGridList = Ext.extend(BaseGridList, {
			border : false,
			gridForceFit : false,
			selectSingle:false,
			pageSize : -1,
			pagingToolbar:false,
			loadMask:{msg:"Please wait..."},
				url : "productStock.ejf?cmd=productStockWearByDepot",
			storeMapping : ["id", "sn", "title","producer","brand", "dir","product","dirTitle","salePrice","attributeType","style","theYear","season","encaped"],
			initComponent : function() {
				/*this.keys = {
					key : Ext.EventObject.ENTER,
					fn : this.refresh,
					stopEvent:true,
					scope : this
				};*/
				var gridConfig={border:false},chkM=null;
			if(this.selectSingle){
					gridConfig.sm=new Ext.grid.RowSelectionModel({singleSelect:true});
				}
				else{
					chkM=new Ext.grid.CheckboxSelectionModel();
					gridConfig.sm=chkM;
				}
				this.gridConfig=Ext.apply({},gridConfig);
				this.cm = new Ext.grid.ColumnModel([chkM?chkM:new Ext.grid.RowNumberer({header:"序号",width:40}),
					    {
							header : "货号",
							sortable : true,
							width : 70,
							dataIndex : "sn"
						},{
							header : "品牌",
							sortable : true,
							width : 80,
							dataIndex : "brand"
						}, {
							header : "类型",
							sortable : true,
							width : 60,
							dataIndex : "dirTitle"
						}, {
							header : "款型",
							sortable : true,
							width : 60,
							dataIndex : "style"
						}, {
							header : "年份",
							sortable : true,
							width : 60,
							dataIndex : "theYear"
						},{
							header : "季节",
							sortable : true,
							width : 60,
							dataIndex : "season"
						},{
							header : "零售价",
							sortable : true,
							width : 60,
							dataIndex : "salePrice"
						}]);
				this.btn_sn =new Ext.form.TextField({
							xtype : "textfield",
							width : 80
						});		

				// new
				// Ext.form.TextField({xtype:"textfield",width:80,listeners:{"change":this.refresh,scope:this}});
				this.btn_title = new Ext.form.TextField({
							xtype : "textfield",
							width : 100,
							listeners : {
								"change" : this.refresh,
								scope : this
							}
						});
				this.btn_model = new Ext.form.TextField({
							xtype : "textfield",
							width : 80,
							listeners : {
								"change" : this.refresh,
								scope : this
							}
						});
				PosProductGridList.superclass.initComponent.call(this);
			this.store.on("load", function(s, rs) {
				if (rs && rs.length > 0) {
					this.grid.getSelectionModel().selectFirstRow();
					this.grid.getView().focusRow(0);
				}
			}, this);
			this.store.on("beforeload",function(store,ops){
			store.baseParams=store.baseParams||{};
			store.baseParams.client=Global.CLIENT.id;
			},this);
			}
		});


PosProductSelectWin = Ext.extend(Ext.Window, {
			title : "选择产品",
			width : 550,
			height : 500,
			layout : "border",
			buttonAlign : "center",
			closeAction : "hide",
			modal:true,
			selectSingle:false,
			callback : Ext.emptyFn,
			maximizable:true,//this.enableMaxime,
			listeners:{maximize:function(win){win.doLayout();},
					restore:function(win){win.doLayout();}
			},
			choice : function(e) {
				var records = this.list.grid.getSelections();
				if (records.length < 1) {
					Ext.Msg.alert("提示","请选择一个产品!",this.focus,this);
					return false;
				}
				var datas = [];
				for (var i = 0; i < records.length; i++) {
					datas[i] = records[i].data;
				}
				this.hide();
				this.fireEvent('select', datas, this);
			},
			initComponent : function() {
				this.keys = [{
					key : Ext.EventObject.ENTER,
					fn : this.choice,
					stopEvent:true,
					scope : this
				},{
					key : Ext.EventObject.ESC,
					fn : function(){this.hide()},
					scope : this
				}];
				this.buttons = [{
							text : "确定",
							handler : this.choice,
							scope : this
						}, {
							text : "清空"
						}, {
							text : "取消",
							handler : function() {
								this.hide();
							},
							scope : this
						}];
				PosProductSelectWin.superclass.initComponent.call(this);
				this.list = new PosProductGridList({selectSingle:this.selectSingle,region:"center"});
/*	this.on("show",function(){
			this.list.grid.store.reload();//getSelectionModel().selectFirstRow();
			//this.list.grid.getView().focusRow(0);
		}, this);*/
				this.list.grid.on("rowdblclick", this.choice, this);
				this.add(this.list);
				this.addEvents("select");
			}
		});
		
ShoppingGuideGridList = Ext.extend(BaseGridList, {
			border : false,
			//gridForceFit : false,
			selectSingle:true,
			pagingToolbar:false,
			pageSize : -1,
			url : "employee.ejf?cmd=list&status=0",
			//baseQueryParams:{status:0},
			storeMapping : ["id", "name", "trueName", "email", "dept", "contractNo","sn",
			"duty", "tel", "registerTime","status","sex","workerOnly"],
			initComponent : function() {
			var gridConfig={border:false},chkM=null;
			if(this.selectSingle){
					gridConfig.sm=new Ext.grid.RowSelectionModel({singleSelect:true});
				}
				else{
					chkM=new Ext.grid.CheckboxSelectionModel();
					gridConfig.sm=chkM;
				}
			this.cm = new Ext.grid.ColumnModel([chkM?chkM:new Ext.grid.RowNumberer({header:"序号",width:35}),{
					header : "姓名",
					sortable : true,
					width : 100,
					dataIndex : "trueName"
				}, {
					header : "编码",
					sortable : true,
					width : 100,
					dataIndex : "name"
				},{
					header : "用户名",
					sortable : true,
					width : 100,
					hidden:true,
					dataIndex : "name"
				}, {
					header : "电话",
					sortable : true,
					width : 100,
					dataIndex : "tel"
				}]);
				
				this.gridConfig=gridConfig;
				ShoppingGuideGridList.superclass.initComponent.call(this);
//				this.store.load();
				this.store.on("load", function(s, rs) {
				if (rs && rs.length > 0) {
					this.grid.getSelectionModel().selectFirstRow();
					this.grid.getView().focusRow(0);
				}
			   }, this);
			}
		});
		
ShoppingGuideSelectWin = Ext.extend(Ext.Window, {
			title : "选择导购",
			width : 540,
			height : 400,
			layout : "fit",
			buttonAlign : "center",
			closeAction : "hide",
			modal:true,
			selectSingle:true,
			callback : Ext.emptyFn,
			choice : function(grid,rowIndex,e) {
				var records = this.list.grid.getSelections();
				if (!records || records.length < 1) {
					Ext.Msg.alert("提示","请选择一个导购员!",this.focus,this);
					return false;
				}
				var datas = [];
				Ext.each(records,function(record,i){datas[i] = records[i].data;},this);
				this.hide();
				this.fireEvent('select', datas[0], this,e);
			},
			initComponent : function() {
				this.keys = [{
					key : Ext.EventObject.ENTER,
					fn : this.choice,
					stopEvent:true,
					scope : this
				},{
					key : Ext.EventObject.ESC,
					fn : function(){this.hide()},
					scope : this
				}];
				this.buttons = [{
							text : "确定",
							handler : this.choice,
							scope : this
						}, {
							text : "取消",
							handler : function() {
								this.hide();
							},
							scope : this
						}];
				ShoppingGuideSelectWin.superclass.initComponent.call(this);
				this.list = new ShoppingGuideGridList();
				/*this.on("show",function(){
				this.list.grid.store.reload();
				}, this);*/
				this.list.grid.on("rowdblclick", this.choice, this);
				this.add(this.list);
				this.addEvents("select");
			}
		});

		
HangBillGridList = Ext.extend(BaseGridList, {
			border : false,
			//gridForceFit : false,
			selectSingle:true,
			pagingToolbar:false,
			pageSize : -1,
			storeMapping : ["time", "num", "totalAmount", "product","items"],
			initComponent : function() {
			var gridConfig={border:false},chkM=null;
			if(this.selectSingle){
					gridConfig.sm=new Ext.grid.RowSelectionModel({singleSelect:true});
				}
				else{
					chkM=new Ext.grid.CheckboxSelectionModel();
					gridConfig.sm=chkM;
				}
			this.cm = new Ext.grid.ColumnModel([chkM?chkM:new Ext.grid.RowNumberer({header:"序号",width:35}),{
					header : "时间",
					sortable : true,
					width : 100,
					dataIndex : "time",
					renderer:this.dateRender("Y-m-d H:i")
				}, {
					header : "数量",
					sortable : true,
					width : 100,
					dataIndex : "num"
				},{
					header : "总金额",
					sortable : true,
					width : 100,
					dataIndex : "totalAmount"
				}, {
					header : "主要产品",
					sortable : true,
					width : 100,
					dataIndex : "product",
					renderer:this.objectRender("title")
				}]);
				
				this.gridConfig=gridConfig;
				HangBillGridList.superclass.initComponent.call(this);
				this.store.on("load", function(s, rs) {
				if (rs && rs.length > 0) {
					(function(){
					this.grid.getSelectionModel().selectFirstRow();
					this.grid.getView().focusRow(0);
					}).defer(500,this);
				}
			   }, this);
			}
		});
		
HangBillSelectWin = Ext.extend(Ext.Window, {
			title : "选择挂单",
			width : 540,
			height : 400,
			layout : "fit",
			buttonAlign : "center",
			closeAction : "hide",
			modal:true,
			selectSingle:true,
			callback : Ext.emptyFn,
			choice : function(grid,rowIndex,e) {
				var records = this.list.grid.getSelections();
				if (!records || records.length < 1) {
					Ext.Msg.alert("提示","请选择一张单据!",this.focus,this);
					return false;
				}
				var datas = [];
				for (var i = 0; i < records.length; i++) {
					datas[i] = records[i].data;
				}
				this.hide();
				this.fireEvent('select', datas[0], this,e);
				this.list.grid.store.remove(records[0]);

			},
			initComponent : function() {
				this.keys = [{
					key : Ext.EventObject.ENTER,
					fn : this.choice,
					stopEvent:true,
					scope : this
				},{
					key : Ext.EventObject.ESC,
					fn : function(){this.hide()},
					scope : this
				}];
				this.buttons = [{
							text : "确定",
							handler : this.choice,
							scope : this
						}, {
							text : "取消",
							handler : function() {
								this.hide();
							},
							scope : this
						}];
				HangBillSelectWin.superclass.initComponent.call(this);
				this.list = new HangBillGridList();
				this.list.grid.on("rowdblclick", this.choice, this);
				this.add(this.list);
				this.addEvents("select");
			}
		});

PosPanel=Ext.extend(BaseStockOutcomeWearBillPanel,{
	id:"posPanel",
	baseQueryParameter:{types:3,vdate3:new Date().format("Y-m-d")},
	types:3,
	singleWindowMode:true,
	printData:true,
	baseUrl:"retailBill.ejf",
	autoLoadGridData:true,
	disable_operators:["btn_edit","btn_remove"],
	searchWin : {
		width : 550,
		height : 220,
		title : "高级查询"
	},
	initColumnDisplay:function(){}, //覆盖该方法. 这里不管理尺码和手
	toFixed:function(v){return v&&v.toFixed?v.toFixed(2):v;},
	viewWin:{width:738,height:600,title:"Pos收银"},
	editEmptyObj:{"id":null,"num7":null,"product":{},residualStoreNum:null,sizeType:null,shoppingGuide:{},productTitle:null,productSn:null,"brand":null, "spec":null, "model":null, "unit":null, "price":null,"num":null, "remark":null, "location":null, "depot":null,"blockSn":null,"totalAmount":null,"vdate":null,"salePrice":null,"discount":null,"saleAmount":null,colorSn:"","other1":null,"other2":null,"other3":null},
   	searchFormPanel : function(){
		var formPanel = new Ext.form.FormPanel({
			frame : true,
			labelWidth : 80,
			labelAlign : "right",
			navigationSequences:["productSn","status","snStart","snEnd","vdate1","vdate2","brand","shoppingGuide"],
			items : [{
				xtype : "fieldset",
				title : "查询条件",
				height : 140,
				layout : 'column',
				items : [{
							columnWidth : .50,
							layout : 'form',
							defaultType : 'textfield',
							items : [{
										fieldLabel : "货&nbsp;&nbsp;&nbsp;&nbsp;号",
										name : "productSn",
										anchor : '90%'
									}, {
									    fieldLabel : "流水单号(始)",
										name : "snStart",
										anchor : '90%'
									},{
										xtype : "hidden",
									    name:"advanced",
									    value:true
									}, {
										fieldLabel : "制单日期(始)",
										name : "vdate1",
										anchor : '90%',
										xtype : 'datefield',
										format : 'Y-m-d'
									}
									 , Ext.apply({},{emptyText:"--请选择--",width:200,anchor : '90%'},Disco.Ext.Util.buildRemoteCombox('brand', '品牌',"brand.ejf?cmd=listSelect", ["id", "name"],"name", "id", true))
									]
						}, {
							columnWidth : .50,
							layout : 'form',
							defaultType : 'textfield',
							defaults : {
								width : 130
							},
							items : [{
								xtype : "combo",
								anchor : '90%',
								name : "status",
								hiddenName : "status",
								fieldLabel : "状&nbsp;&nbsp;&nbsp;&nbsp;态",
								displayField : "title",
								valueField : "value",
								value : 0,
								store : new Ext.data.SimpleStore({
											fields : ['title', 'value'],
											data : [ ["正常销售", 0],["质量问题", 1],["客户不满意", 2],["换货", 3],["未知原因", 4]]
										}),
								disableChoice : true,
								editable : false,
								mode : 'local',
								triggerAction : 'all',
								emptyText : '请选择...'
									}, {
									    fieldLabel : "流水单号(末)",
										name : "snEnd",
										anchor : '90%'
									},{
										fieldLabel : "制单日期(末)",
										name : "vdate2",
										anchor : '90%',
										xtype : 'datefield',
										format : 'Y-m-d'
									},Ext.apply({},{anchor:"-1",width:95,fieldLabel : '导购员',hiddenName:'shoppingGuide',name:'shoppingGuide'},ConfigConst.CRM.user)  
						]
						}]
			}]
		});
		return formPanel;
	},
	initWin : function(width, height, title,callback,autoClose,resizable,maximizable) {
			this.winWidth = width;
			this.winHeight = height;
			this.winTitle = title;
			var winName=autoClose?"CrudEditNewWindow":"CrudEditWindow";
			if(this.singleWindowMode)winName=winName+this.id;
		 if(!this.win)
			this.win = new Ext.Window({
				modal : true,
				width : width,
				height : height,
				title : title,
				layout : "fit",
				border:false,
				items : this.fp,
				listeners:{
					hide:function(){
						if(this.vipWin)this.vipWin.hide();
					},
					scope:this
				}
			});
			this.win.confirmSave=this.dirtyFormCheck;
			return this.win;
		},
   	onCreate:function(){
   		Ext.EventManager.onWindowResize(this.win.fitContainer, this.win);
   		this.win.on("show",function(){
	    	var key = this.win.getKeyMap();
	    	key.disable();
	    	this.fp.form.findField("key").focus(false,300);
    	},this);
    	
    	this.win.getEl().on("click",this.formFocus,this);
    	this.win.getEl().on("contextmenu",function(e){
    		e.stopEvent();
        	this.formFocus();
    	},this);
    	this.tempData.clear("firstShippingGuide");
   		this.paid = 0.00; //初始化付款金额
   		this.setAction('shippingGuide');
   		this.payment=false; //初始化开始付款
   		this.complete=false; // 初始化付款完成
   		this.giftsPattern=false;
   		this.haveProducts = false;
   		this.append= false;
   		this.noPrint=false;
/*
		this.append= false;//添加货品,为true的时候连续添加货品
		this.haveProducts = false; //是否有在EditPanel是否有货品
   		this.complete=false; // 初始化付款完成
   		this.noPrint=false;
   		this.patchOrder = false; //补单
*/    	this.editGrid.store.removeAll();
    	this.paymentGrid.store.removeAll();
    	this.editGrid.getSelectionModel().on("beforecellselect",function(){
    			this.customSelectRow(this.selectedRow);
    			return false;
    	},this);
    	this.fp.form.findField("key").enable();
    	this.fp.form.findField("vdate").setValue("");
    	var retailType = this.fp.form.findField("retailType");
    	retailType.el.parent('div.x-form-item').first('label').dom.innerHTML = "销售";
    	this.fp.form.findField("status").setValue(0);
    	this.fp.form.findField("remark2").setValue("");
    	//this.fp.form.findField("key").focus("",200);
		this.fp.form.findField("key").reset();
		//this.fp.form.find.defer(200);
   	},
   	edit:function(){
		this.view();
   	},
   	showViewWin : function(autoClose) {
		if(this.createViewPanel){
			this.viewPanel = this.createViewPanel();
		}
		var win = this.getViewWin(autoClose);
		return win;
	},
    createViewPanel:function(){
	    var store=new  Ext.data.JsonStore({
			root : "result",
			totalProperty : "rowCount",
			fields:this.editStoreMapping
		});
	   	var colM=this.getEditColumnModel();
		this.viewGrid = new Ext.grid.GridPanel({
			//title:"采购入库单详",
			cm:colM,
			store:store,
			border:false,
			autoExpandColumn:colM.getColumnCount()-1,
			plugins:[new Ext.ux.grid.GridSummary()]
		});
		if(!this.celldblclickShowPictrue)this.viewGrid.on("celldblclick",this.showPic,this);  //查看图片
		var formPanel=new Ext.form.FormPanel({
			frame:false,
			labelWidth:70,
			labelAlign:'right',
			items:[{
				xtype:'panel',
				//title:'基本信息',
				style:"margin-bottom:5px;margin-top:10px",
				border:false,
				autoHeight:true,
				layout:"form",
				items:[
	                    Disco.Ext.Util.columnPanelBuild(
	                    {columnWidth:.2,items:{xtype:"labelfield",fieldLabel:'编号',name:'sn'}},
	                    {columnWidth:.2,items:{xtype:"labelfield",fieldLabel:'时间',name:'vdate',renderer:this.dateRender("Y-m-d")}},
						{columnWidth:.3,items:{xtype:"labelfield",fieldLabel:'收银员',name:'cashier',renderer:this.objectRender("trueName")}},
						{columnWidth:.3,items:{xtype:"labelfield",fieldLabel:'付款总数',name:'receive',anchor:"-20"}},
						{columnWidth:.2,items:{xtype:"labelfield",fieldLabel:'合计',name:'total'}},
						{columnWidth:.2,items:{xtype:"labelfield",fieldLabel:'找零',name:'charge'}}
						)
	                    ]
			},{
				xtype:'panel',
				autoHeight:true,
				border:false,
				listeners:{render:function(c){
					var height=this.viewPanel.el.getBox().height-(this.viewPanel.getComponent(0).el.getBox().height+68);
					this.viewGrid.setHeight(height);
				},scope:this},
				items:this.viewGrid
			}]
		});
		return formPanel;
   	},
   	escFunction:function(){
   		var action = this.getAction();
   		if(action){
   			if(action == 'preferential'){
				if(this.fp.findSomeThing("preferential").getValue())
					this.cancelPreferential();
				else if(this.payment)
					this.cancelPayment();
	   		}else if(action == 'onMember'){
				  this.setAction(null);
	   		}
	   		return false;
   		}
   		
   		if(this.complete){
   		   this.fp.form.findField("remark1").setValue("取消交易!");
   		   this.complete = false;
   		}else if(this.payment){
   			this.cancelPayment();
   		}else if(this.haveProducts){
   			 this.delProduct();
   			 var  count = this.editGrid.store.getCount();
   			 if(count<1){
   			 	 this.haveProducts = false;
   			 	 this.append = false;
   			 	 this.fp.form.findField("remark1").setValue("请先输入编码后按[确认]键选择购员!");
   			 }
   		}
   		this.setAction();
   	},
   	cancelPayment:function(){
   		var paymentCount = this.paymentGrid.store.getCount();
		if(paymentCount>1){
			var record  = this.paymentGrid.store.getAt(paymentCount-1);	   				
			this.paymentGrid.store.remove(record);
			this.countPay();
			this.formFocus();
		}else{
   			this.payment = false;
   			this.fp.form.findField("remark1").setValue("取消付款!");
   			this.fp.form.findField("received").setValue(null);
   			this.fp.form.findField("receive").setValue(null);
   			this.fp.form.findField("unpaid").setValue(null);
   			this.fp.form.findField("key").setValue(null);
   			this.paid = 0.00;
   			this.paymentGrid.store.removeAll();
		}
   	},
   	createPaymentGrid: function(){
	   	 var cms = [new Ext.grid.RowNumberer({header:"<font size='4' color='000000'>编号</font>",width:50}),
	   	 			{header:"支付类型",dataIndex:"paymentType",width:80,hidden:true},
	   	 			{header:"卡号",dataIndex:"cardNO",width:80,hidden:true},
	   	 			{header:"信用卡类型",dataIndex:"cardType",hidden:true},
	   	 			{header:"<font size='4' color='000000'>付款方式<font>",dataIndex:"paymentTypeTitle",width:80},
	   	 			{header:"<font size='4' color='000000'>金额<font>",dataIndex:"paymentSum",width:80}
	   	 			];
	   	var  fields = ["paymentType","paymentTypeTitle","paymentSum","cardNO","cardType"];
	   	return new Ext.grid.GridPanel({
	   	     viewConfig:{forceFit:true},
	   	     cm: new Ext.grid.ColumnModel(cms),
	   	     store:new Ext.data.JsonStore({
	   	     	   fields:fields
	   	     })
	   	});
   	},
   	addRowDataHandler:function(r){
	 	var obj=Ext.apply({},r,this.editEmptyObj);
		if(r){
			obj.shoppingGuide=r;
		}
		delete obj.id;
		return obj;
	},
	addRowForProductDataHandler:function(r){
	//var obj=Ext.apply({},r,this.editEmptyObj);
		var obj={};
		if(r){
				obj.shoppingGuide=this.tempData.get("firstShippingGuide");
				obj.salePrice=this.formatMoney(r.salePrice);
				obj.product=r;
				obj.product.toString=Disco.Ext.Util.objectToString;
				obj.productTitle=r.title;
				obj.productSn=r.sn;
				obj.spec=r.spec;
				obj.stockNO=r.stockNO;
	            obj.colorSn=r.colorSn;
	            obj.attributeType = r.attributeType;
	            obj.encaped = r.encaped;
	            obj.color = r.colors;
	            //obj.color.toString=function(){return this.title?this.title:this};
	            obj.sizeType = r.sizes;
	            obj.status = r.status;
				/*obj.marketPrice=r.marketPrice;
				obj.brand=r.brand;
				obj.model=r.model;*/
	            
				obj.unit=r.unit;
				obj.num=1;
				obj.style=r.style;
				obj.gifts=r.gifts;	
				if(this.vipUser){
					obj['discount'] = parseFloat(this.vipUser['discount']/10) ;
					obj['discountPrice'] = this.formatMoney(obj['discount'] * parseFloat(obj['salePrice']));
					obj.totalAmount = obj['discountPrice'];
		   		}else{
		   			obj.totalAmount = this.formatMoney(r.salePrice);
					obj.discountPrice = this.formatMoney(r.salePrice);
					obj.discount = 1.0;	
		   		}
			}
		Ext.del(obj,'id');
		return obj;
	},
   	append: false, //连续性加入商品
   	payment:false,//是否需要付款
   	paid :0.00,
   	paymentTypeTitle:"现金",
   	checkCreditCard:function(){
   		var index = this.paymentGrid.store.find("paymentTypeTitle","信用卡");
   		if(index===-1){
   			return false;
   		}
   		return true
   	},
   	deleteCreditCard:function(paymentTypeTitle){
   		var index = this.paymentGrid.store.find("paymentTypeTitle",paymentTypeTitle);
   		if(index===-1){
   			return false;
   		}else{
			this.paymentGrid.store.getAt(index).set("paymentSum",0);			
   		}
   	},
   	setAction:function(action){
   		if(action===null || action===window.undefined){
   			if(!this.editGrid.getStore().getCount())action = 'shippingGuide';	
   		}
   		this._action = action;
   	},
   	getAction:function(){
   		return this._action;
   	},
   	isAction:function(action){
  		return (this._action==action);
   	},
   	//付款
   	doPayment:function(){
   		var  payments  = this.fp.form.findField("key").getValue(); //获得输入的金额
		var count = this.editGrid.store.getCount();
		var sum = parseFloat(this.fp.form.findField("receivable").getValue());
		if(!payments){
			Disco.Ext.Util.msg("提示","请输入金额!");
			return false;
		}else if(!Disco.Ext.Util.isNumber(payments)){
		 	Disco.Ext.Util.msg("提示","请输入数字!");
			return false;
		}
		payments = parseFloat(payments);
		
		var status = this.fp.form.findField("status").getValue();
	    status = parseInt(status);
		if(status==0&&sum<0&&sum-payments!=0){
			Disco.Ext.Util.msg("提示","如有退货只能录入和付款相同的金额!");
			return false;
		}else if(status>0&&sum-payments!=0){
			Disco.Ext.Util.msg("提示","在退货模式只能录入和付款相同的金额!");
			return false;
		}
		if(!this.paymentTypeId){
		  Ext.each(this.paymentTypes,function(o){
		            if(o.name=="现金"){
			          this.paymentTypeTitle = "现金";
			          this.paymentTypeId = o.id;
		            }
		          },this);
		}
		 if(this.paymentTypeId){
		 	var index = this.paymentGrid.store.find("paymentType",this.paymentTypeId);
		 	 if(index!==-1){
		 	 	var record  = this.paymentGrid.store.getAt(index);
		 	 	record.set("paymentSum",record.get("paymentSum")+payments);
		 	 }else{
				 this.paymentGrid.store.loadData([{paymentType:this.paymentTypeId,paymentTypeTitle:this.paymentTypeTitle,paymentSum:payments,cardNO:"",cardType:""}],true);
		 	 }
		  }
		  
		 this.fp.form.findField("key").setValue(null);
		 this.paid = this.paymentGrid.store.sum("paymentSum");
		 var  p =  this.paid - sum;
		 if(p<0){
		 	Disco.Ext.Util.msg("提示","未付款:"+ -this.toFixed(p));
		 	this.fp.form.findField("unpaid").setValue(-this.toFixed(p));
		 	this.fp.form.findField("receive").setValue(this.toFixed(this.paid));
		 	this.fp.form.findField("received").setValue(this.toFixed(this.paid));
		 	this.paymentTypeId = false;
		    this.formFocus();
		    return false;
		 }else if(p > 0){
		 	if(this.checkCreditCard()){
	    		Ext.Msg.alert("错误提示","有信用卡支付的不能超出应收账款!将取消该现金支付",function(){
				 if(this.paymentTypeId){ //再把此次输入的金额减掉
					 	var index = this.paymentGrid.store.find("paymentType",this.paymentTypeId);
					 	 if(index!==-1){
					 	 	var record  = this.paymentGrid.store.getAt(index);
					 	 	record.set("paymentSum",record.get("paymentSum")-payments);
					 	 }
					 	 this.countPay();
					 	 this.formFocus();
					  }
	    		},this);
		 		return false;
		 	 }
	 	  if(p>=100){
	 	  	Ext.MessageBox.alert("提示","找零金额不能够大于100,请确认!",function(){this.formFocus()},this);
	 	    this.deleteCreditCard("现金");
	 	    //this.paid=this.paid-payments;
	 	    this.countPay();
		 	/*this.fp.form.findField("charge").setValue(this.toFixed(p));
			this.fp.form.findField("total").setValue(this.toFixed(sum));
		 	this.fp.form.findField("received").setValue(this.toFixed(this.paid));
			this.fp.form.findField("receive").setValue(this.toFixed(this.paid));*/
	 	  	return false
	 	  }else{
		 	Disco.Ext.Util.msg("提示","&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;找&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;零&nbsp;&nbsp&nbsp;&nbsp;&nbsp;&nbsp&nbsp;&nbsp;" + this.toFixed(p));
		 	this.fp.findField("remark2").setValue("找零"+this.toFixed(p))
		 	this.fp.findField("unpaid").setValue(-this.toFixed(0));
		 	this.complete = true;
		 	this.fp.findField("charge").setValue(this.toFixed(p));
			this.fp.findField("total").setValue(this.toFixed(sum));
			this.fp.findField("receive").setValue(this.toFixed(this.paid));
		 	this.formFocus();
	 	  }
		 }
	 	this.fp.findField("charge").setValue(this.toFixed(p));
		this.fp.findField("total").setValue(this.toFixed(sum));
		this.fp.findField("receive").setValue(this.toFixed(this.paid));
		this.fp.findField("received").setValue(this.toFixed(this.paid));
		Disco.Ext.Util.msg("提示","支付成功！请按任意键保存数据并且重新开单!");
		this.fp.form.findField("remark1").setValue("支付成功！请按任意键保存数据并且重新开单!按[取消]键取消交易!");
		this.complete = true; 			 //标记交易成功
	    this.formFocus();
	    return false;
   	},
   	enterFunction:function(){
   		 if(this.complete) return false;
   		 var key = this.fp.form.findField("key").getValue();
   		 var action = this.getAction();
   		 if(action){
   			switch(action){
	   			case 'patchOrder' :
	   					this.onPatchOrder();
	   				break;
	   			case 'reserve' :
	   					this.doReserve();
	   				break;
	   			case 'pay' :
	   					this.doPay();
	   				break;
	   			case 'preferential':
	   				if(!this.fp.findSomeThing("preferential").getValue())
	   					this.doPreferential();
	   				else	
	   					this.doPayment();
	   				break;
	   			case 'onMember' :
	   					if(!key){
			   				Ext.MessageBox.alert("提示","请输入会员卡!",this.formFocus,this);
			   				return false;
			   			}
			   			this.getVip(key,this.showVipPanel);
	   				break;
	   			case 'shippingGuide' :
	   				this.seachShippingGuide();
	   				break;
/*	   			case 'gifts' :
	   				if(this.tempData.containsKey("firstShippingGuide")){
   						if('$!session.COMPANYSETTING_IN_SESSION.productInputType'==2){
	   						this.seachProductByBarCode('productBarCode.ejf?cmd=getProductPosBarCodeByCode',{gifts:true},this.seachProductGiftsByBarCode);
			   			}else{
			   				this.seachProductGifts();
			   			}
	   				}else{
	   					this.setAction("shippingGuide");
	   					this.seachShippingGuide();
	   				}
	   				break;
	   			break;*/
	   		}
	   		return;
   		}
   		if(this.payment && !this.complete){
   		 	this.doPayment();
   		}else if (this.tempData.containsKey("firstShippingGuide")){
   			if(!key){
   				Disco.Ext.Msg.alert({fn:this.formFocus,scope:this,msg:"请输入货号或条码"});
   				return false;
   			}
   			if('$!session.COMPANYSETTING_IN_SESSION.productInputType'==2){
   				var args = [] ;
   				this.seachProductByBarCode('productBarCode.ejf?cmd=getProductPosBarCodeByCode',{})
   			}else{
   				this.seachProductWear();
   			}
   			return false;
   		}
   	},
   	seachProductByBarCode:function(url,params,formatFn){
   		var key = this.fp.findField('key').getValue().trim();
   		if(key&&key.trim().length<2){
    		 Disco.Ext.Util.msg("","输入正确的条码!");
    		 this.formFocus();
    		return false;
    	}
    	Ext.Ajax.request({
   			scope:this,
   			url:url,
   			params:Ext.apply({code:key},{action:'pos'},params),
   			success:function(response,options){
   				var data = Ext.decode(response.responseText);
   				if(data.success==true && data.data){
   					var obj = data.data;
   					var dest = Ext.copyTo({},obj,'attributeType brand;sizeType dir;dirTitle,encaped,id,producer,product,salePrice,season,sn,style,theYear,title');
   					dest.residualStoreNum = obj.num; 
   					dest.sizes = dest.sizeType;
   					obj.sizeType = obj.size;
					if(formatFn)formatFn(obj);   					
					
					var fn = function(){
						var o = this.addRowForProductDataHandler(obj);
						Ext.copyTo(o,obj,"color,sizeType");
						if(o.color)o.color.value = o.color.id;
						if(o.sizeType)o.sizeType.value = o.sizeType.id;
						this.editGrid.store.loadData({result:[o]},this.append);
				    	this.append = true;
				    	var lastRow = this.editGrid.store.getCount()-1;
				    	this.editGrid.getSelectionModel().select(lastRow,0);
						var m = new Ext.util.MixedCollection();m.addAll([obj.num]);
						this.editGrid.store.getAt(lastRow).set("residualStoreNum",obj.num);
					    this.editGrid.store.getAt(lastRow).stockInfo=m;
					    
			            this.fp.form.findField("key").reset();
			            this.countAll(false);
			          	this.autoColumn(lastRow);
			            this.haveProducts = true;
			            this.fp.findField('key').focus(true);
					}
					
					if(this.giftsPattern){
						obj.salePrice = 0 ;
   						obj.status = 5 ;
   						this.giftsWin = new Ext.Window({
							width:300,height:110,
							title:'积分兑换',
							layout:'fit',
							modal:true,
							border:false,
							hideBorders:true,
							resizable:false,
							items:{
								layout:'form',
								xtype:'form',
								items:[{
									style:'margin:5px;',
									fieldLabel:'积分',
									hideLabel:true,
									anchor:'96% *',
									name:'gifts',
									minValue:0,
									allowDecimals:false,
									autoCreate:{
										tag : "input",
										type : "text",
										autocomplete : "off",
										style:"font-weight: bold; font-size: 54px; width: 288px; height: 66px;"
									},
									xtype:'numberfield',
									listeners:{
										scope:this,
										specialkey:function(field,e){
											if(e.getKey()==13){
												if(field.getValue()==""){
													Disco.Ext.Msg.alert("请输入兑换积分!",false,function(){
														field.focus()
													});
													return ;
												}
												obj.gifts = field.getValue();
												this.giftsWin.close();
												fn.call(this);
											}
										}
									}
								}]
							},
							listeners:{
								scope:this,
								show:function(win){
									var form = win.getComponent(0);
									form.findField('gifts').focus(true,100);
								}
							}
						});
						this.giftsWin.show();
					}else{
						fn.call(this);
					}
   				}else{
   					Disco.Ext.Util.msg('','没有找到该条码对应的商品!');
   					this.fp.findField('key').focus(true);
   				}
   			}
   		});
   	}
   	,
   	buildProductSelectWin:function(){
   		if(!this.productSelectWin)
   				this.productSelectWin = new PosProductSelectWin({selectSingle:true});
		if(!this.productSelectWin.hasListener('select')){
			this.productSelectWin.on("select",function(r){
				var data = r[0];
				if(this.giftsPattern){
					this.giftsWin = new Ext.Window({
						width:300,height:110,
						title:'积分兑换',
						layout:'fit',
						modal:true,
						border:false,
						hideBorders:true,
						resizable:false,
						items:{
							layout:'form',
							xtype:'form',
							items:[{
								style:'margin:5px;',
								fieldLabel:'积分',
								hideLabel:true,
								anchor:'96% *',
								name:'gifts',
								minValue:0,
								allowDecimals:false,
								autoCreate:{
									tag : "input",
									type : "text",
									autocomplete : "off",
									style:"font-weight: bold; font-size: 54px; width: 288px; height: 66px;"
								},
								xtype:'numberfield',
								listeners:{
									scope:this,
									specialkey:function(field,e){
										if(e.getKey()==13){
											if(field.getValue()==""){
												Disco.Ext.Msg.alert("请输入兑换积分!",false,function(){
													field.focus()
												});
												return ;
											}
											data.gifts = field.getValue();
											this.giftsWin.close();
											this.selectProductDataHandler(data);
										}
									}
								}
							}]
						},
						listeners:{
							scope:this,
							show:function(win){
								var form = win.getComponent(0);
								form.findField('gifts').focus(true,100);
							}
						}
					});
					this.giftsWin.show();
				}else{
					this.selectProductDataHandler(data);
				}
			},this);
   	    }
       	this.productSelectWin.on("hide",function(){
       		this.productSelectWin.list.grid.store.removeAll();
       		this.formFocus();
       	},this,{single:true});
   	},
   	seachProductGifts:function(){
    	this.buildProductSelectWin();
    	if(key&&key.trim().length<2){
    		 Disco.Ext.Util.msg("","输入货号的长度至少两个字符!");
    		 this.formFocus();
    		return false;
    	}
		this.productSelectWin.show();
   	    this.productSelectWin.list.grid.getGridEl().mask("加载库存商品...","x-mask-loading");
   	    
   	    Ext.Ajax.request({url:"productStock.ejf?cmd=productGiftsWithStock",
                params:{sn:key},
   	            callback :function(ops,success,response){
			    var map=Ext.decode(response.responseText);
			    var tag = false;
			     for (var n in map) {
			     	  if(n==="tag")tag = true;
			     }
			     if(tag){
			     	 Disco.Ext.Util.msg("","请设置零售库,请设置零售库!");
			    	 this.productSelectWin.hide();
			    	 this.formFocus();
				     this.productSelectWin.list.grid.getGridEl().unmask();
			    	return false;
			     }else  if(!map || !map.result){
			    	 Disco.Ext.Util.msg("","该货品不存在或者是库存中没有了!");
			    	 this.productSelectWin.hide();
			    	 this.formFocus();
				     this.productSelectWin.list.grid.getGridEl().unmask();
			    	 return false
			    }else{
			    	this.productSelectWin.list.grid.store.removeAll();
					this.productSelectWin.list.grid.store.loadData(map);
			    }
				this.productSelectWin.list.grid.getGridEl().unmask();
	     },scope:this});
   	},
   	onPatchOrder:function(){
   		var  vdate  = this.fp.form.findField("key").getValue(); //获得输入的金额
		var  regular = /^(\d{4})\.(\d|0\d{1}|1[0-2])\.(\d|0\d{1}|[12]\d{1}|3[01])$/;
		if(!regular.test(vdate)){
		   Ext.MessageBox.alert("提示","请确定输入时间的格式!例如:2009.08.08",this.formFocus,this);				
		}else{
			this.fp.form.findField("remark2").setValue("补单时间:"+vdate);
			this.fp.form.findField("vdate").setValue(vdate);
			this.formFocus();
			this.setAction(null);
		}
   	},
   	getVip:function(key,callback){
   		this.vipUser = null;
   		Ext.Ajax.request({
   			url:'member.ejf?cmd=getMemberByCode',
   			params:{code:key},
   			success:callback,
   			scope:this
   		});
   	},
   	updateVipDiscountPrice:function(){
   		 var storeCount = this.editGrid.getStore().getCount();
   		 if(storeCount){
   		 	var sm = this.editGrid.getSelectionModel();
   		 	var selectRow = (sm.getSelectedCell()||[0,storeCount-1])[1];
   		 	this.editGrid.getStore().each(function(record){
   		 		if(record.get('product') && record.get('product').id){
   		 			var obj = record.data;
   		 			obj['salePrice'] = this.formatMoney(obj['salePrice']);
   		 			obj['discount'] = parseFloat(this.vipUser['discount']) / 10 || 1;
   		 			obj['discountPrice'] = Ext.num(parseFloat(this.formatMoney(obj['discount']*parseFloat(obj['salePrice']).toFixed(2))),0);
					obj.totalAmount = obj['discountPrice'];
					record.commit();
   		 		}
   		 	},this);
   		 	sm.select(selectRow,false);
   		 }
   		 this.countAll();
   	},
   	showVipPanel:function(response,options){
   		var data = Ext.decode(response.responseText);
   		if(!data || !data.success || !data.data){
   			if(this.vipWin)this.vipWin.hide();
   			Disco.Ext.Msg.alert('没有找到该会员!',null,this.formFocus,this);
   			return ;
   		}
   		this.vipUser = data.data;
   		this.vipUser['discount'] = this.vipUser['discount'] || 1;
   		this.setAction(null);
		if(!this.vipWin){
   			this.vipWin = new Ext.Window({
   				title:'会员信息',
   				width:250,
   				height:120,
   				//closable:false,
   				resizable:false,
   				closeAction:'hide',
   				collapsible:true,
   				animCollapse:false,
   				manager:new Ext.WindowGroup,
   				autoHeight:true,
   				x:Ext.getBody().getViewSize().width-255,
   				y:Ext.getBody().getViewSize().height-125,
   				hideBorders:true,
   				moveWindow:function(){
   					var viewSize = Ext.getBody().getViewSize();
   					this.vipWin.el.moveTo(viewSize['width']-this.vipWin.getBox().width-5,
   										  viewSize['height']-this.vipWin.getBox().height-5);
   				},
   				items:{
   					xtype:'form',
   					defaultType:'labelfield',
   					bodyStyle:'padding-left:20px;',
   					items:[
   						{fieldLabel:'会员姓名',name:'name'},
   						{fieldLabel:'卡&emsp;&emsp;号',name:'cardNumber',renderer:function(v){
   							return Ext.util.Format.ellipsis(v,10);
   						}},
   						{fieldLabel:'折&emsp;&emsp;扣',name:'discount'},
   						{fieldLabel:'积&emsp;&emsp;分',name:'integration'}
   					]
   				}
   			});
   			this.vipWin.on('render',function(win){
   				win.el.on('dblclick',function(){
    					win.collapsed ? win.expand(false) : win.collapse(false);
   				},this);
   			},this);
   			this.vipWin.on({
   				scope:this,
   				hide:function(){
   					this.vipUser = null;
   				},
   				expand:this.vipWin.moveWindow.createDelegate(this,['expand'],0),
   				collapse:this.vipWin.moveWindow.createDelegate(this,['collapse'],0)
   			});
   			this.vipWin.fp = this.vipWin.getComponent(0);
   		} 
   		this.vipWin.show();
   		this.vipWin.fp.getForm().setValues(this.vipUser);
   		this.vipWin.setZIndex(99000);
   		this.updateVipDiscountPrice();//修改當前貨品的折扣價格
   		this.formFocus();
   	},
   	doChangeCasher:function(){
   		if(!this.casherSelectWin){
	   		this.casherSelectWin = new ShoppingGuideSelectWin({title:"选择交班收银员"});
	   		this.casherSelectWin.on("select",this.selectCasher,this);
	   		this.casherSelectWin.on("show",function(){
	   		Ext.Ajax.request({url:"dutyInfo.ejf?cmd=listCasher&pageSize=-1&orderBy=sn&orderType=asc&status=0",
				callback :function(ops,success,response){
					var map=Ext.decode(response.responseText);
					this.casherSelectWin.list.grid.store.removeAll();
					if(map && map.result.length){
	   					this.casherSelectWin.list.grid.store.loadData(map);
					}else{
						this.casherSelectWin.hide();
						Disco.Ext.Util.msg("","不存在其他收银员!");
					}
				},scope:this});
	   		  },this);
	   		  this.casherSelectWin.on("hide",this.formFocus,this);
	   	   }
	      this.casherSelectWin.show();
   	},
   	selectCasher:function(d){
   		if(d && d.id){
   			if(!this.loginWin){
   				this.loginWin=new CasherLoginWindow();
   				this.loginWin.on("hide",this.formFocus(),this);
   			}
   			this.loginWin.show();
   			this.loginWin.fp.findSomeThing("user").setValue(d.id);
   			this.loginWin.fp.findSomeThing("userName").setValue(d.name);
   			this.loginWin.fp.findSomeThing("psw").focus("",200);
   		}
   	},
   	doPay:function(){
   		var key = this.fp.form.findField("key").getValue();
   		if(!key || isNaN(parseFloat(key))){
   			Ext.MessageBox.alert("提示","请输入正确的缴款数量!",function(){
   				this.formFocus();
   			},this);
   			return false;
   		}else{
   			Ext.Ajax.request({
   				scope:this,
   				url:"dutyInfo.ejf",
   				params:{cmd:"save",pay:key},
   				success:function(req){
   					var msg=Ext.decode(req.responseText);
   					if(msg && msg.msg){
   						Ext.Msg.alert("提示信息",msg.msg);
   					}else{
   						Disco.Ext.Util.msg("提示","缴款完成!");
   						this.formFocus();
   						this.pay=false;
   					}
   				}
   			})
   		}
   	},
   	doReserve:function(){
   		var key = this.fp.form.findField("key").getValue();
   		if(!key || isNaN(parseFloat(key))){
   			Ext.MessageBox.alert("提示","请输入正确的备用金数量!",function(){
   				this.formFocus();
   			},this);
   			return false;
   		}else{
   			Ext.Ajax.request({
   				scope:this,
   				url:"dutyInfo.ejf",
   				params:{cmd:"save",reserve:key},
   				success:function(req){
   					var msg=Ext.decode(req.responseText);
   					if(msg && msg.msg){
   						Ext.Msg.alert("提示信息",msg.msg);
   					}else{
   						Disco.Ext.Util.msg("提示","备用金输入完成!");
   						this.formFocus();
   						this.reserve=false;
   					}
   				}
   			})
   		}
   	},   	
   	autoPrice:function(record,currentChanges){
   		var discount = parseFloat(record.get("discount")) || 10;
   		var price = parseFloat(record.get("salePrice"));
   		var discountPrice = parseFloat(record.get("discountPrice"));
   		var num = parseFloat(record.get("num"));
        if(currentChanges=="discount"){
        	record.set("discountPrice",(discount*price).toFixed(0));
        }else if(currentChanges=="salePrice"){
        	record.set("discountPrice",(discount*price).toFixed(0));
        }else if(currentChanges=="discountPrice"){
        	if(price!=0)record.set("discount",(discountPrice/price).toFixed(2));
        }
        var to=num*parseFloat(record.get("discountPrice"));
   		record.set("totalAmount",parseFloat(to.toFixed(2)));
   	},
   	selectedShippingGuide:function(d,grid,e){
   		if(this.editGrid.store.getCount()>0){
   			 var selectRecord = this.getSelectedRecord();
   			 d.toString=function(){return this.id}; 
		  	 selectRecord.set("shoppingGuide",d);
   		}
   		else {
	   		this.editGrid.store.loadData({result:[this.addRowDataHandler(d)]},true);
		   	this.customSelectLastRow();
   		}
   		this.tempData.add("firstShippingGuide",d);//缓存起来
	   	this.fp.form.findField("key").reset();
	   	this.fp.form.findField("remark1").setValue("请先输入货号后按[确认]键选择货品!");
	   	this.setAction(null);
	   	this.formFocus();
   	},
   	seachShippingGuide:function(){
	   	  if(!this.shoppingGuideSelectWin){
	   		  this.shoppingGuideSelectWin = new ShoppingGuideSelectWin();
	   		  this.shoppingGuideSelectWin.on("select",this.selectedShippingGuide,this);
	   		  this.shoppingGuideSelectWin.on("show",function(){
	   		  	var key =  this.fp.form.findField("key").getValue();
	   	 		Ext.Ajax.request({url:"employee.ejf?cmd=list&pageSize=-1&orderBy=sn&orderType=asc&status=0",
	   	                    params:{sn:key},
			   	            callback :function(ops,success,response){
							    var map=Ext.decode(response.responseText);
								this.shoppingGuideSelectWin.list.grid.store.removeAll();
								if(map.result){
		   						 	this.shoppingGuideSelectWin.list.grid.store.loadData(map);
								}else{
								  this.shoppingGuideSelectWin.hide();
								  Disco.Ext.Util.msg("","不存在该导购员!");
								}
				    		 },scope:this});
	   		  },this);
	   		  this.shoppingGuideSelectWin.on("hide",this.formFocus.createSequence(function(){this.setAction(null)},this),this);
	   	   }
	      this.shoppingGuideSelectWin.show();
   	  },
    autoColumn : function(lastRow){
		var record = this.editGrid.store.getAt(lastRow);
      	var obj = record.get("product");
		if(obj.attributeType===0 && obj.encaped){
        	record.columnSeq = ['color'];
        	#if(!$!session.COMPANYSETTING_IN_SESSION.useColor)
        		record.columnSeq = [];
        	#end
        }else if(obj.attributeType===0 && !obj.encaped){
        	//record.columnSeq = ['color','sizeType'];
        	record.columnSeq=[];
        	#if($!session.COMPANYSETTING_IN_SESSION.useColor)
        	record.columnSeq = record.columnSeq.concat("color");
        	#end
        	#if($!session.COMPANYSETTING_IN_SESSION.useSize)
        	record.columnSeq = record.columnSeq.concat("sizeType");
        	#end
        }else if(obj.attributeType===1 && obj.encaped){
        	record.columnSeq = ['color'];
        	#if(!$!session.COMPANYSETTING_IN_SESSION.useColor)
        	record.columnSeq = [];
        	#end
        }else if(obj.attributeType===1 && !obj.encaped){
        	record.columnSeq = ['color'];
        	#if(!$!session.COMPANYSETTING_IN_SESSION.useColor)
        	record.columnSeq = [];
        	#end
        }else if(obj.attributeType===2 && !obj.encaped){
        	record.columnSeq = ['sizeType'];
        	#if(!$!session.COMPANYSETTING_IN_SESSION.useSize)
        	record.columnSeq = [];
        	#end
        }else{
        	record.columnSeq = [];
        }
        if(record.columnSeq[0]){
	      	var column = this.editGrid.getColumnModel().findColumnIndex(record.columnSeq[0]);
	      	/**
			(function(){ 
			 this.editGrid.startEditing(lastRow,column);
			 var editor=this.editGrid.getColumnModel().getCellEditor(column,lastRow).field;
			 if(editor.store.getCount()>0){
			 	var view = this.editGrid.getView();
			 	
				(function(){
					editor.onTriggerClick();
					editor.list.alignTo(view.getCell(lastRow,column), 'tl-bl?');
				}).defer(1000,this);
				
			 	this.editGrid.startEditing(lastRow,column,true);
			};
			}).defer(1000,this);
			*/
	      	if('$!session.COMPANYSETTING_IN_SESSION.productInputType'!=2){
	      		this.editGrid.startEditing(lastRow,column,true);
	      	//	this.editGrid.view.ensureVisible(lastRow,column,true);
	      	}else{
	      		this.customSelectRow(this.editGrid.store.getCount());
	      	}
	      	//(function(){this.editGrid.getSelectionModel().tryEdit(lastRow,column,true)}).defer(1000,this);//编辑下一行
		 }else{
		 	this.fp.form.findField("remark1").setValue("当前库存为"+record.stockInfo.get(0));
      		record.set("residualStoreNum",parseInt(record.stockInfo.get(0)));
      		this.customSelectRow(this.editGrid.store.getCount());
		 	this.formFocus();
		 }
  	},
   formatMoney:function(v){
   	    var fm ={1:2,2:1,3:0,4:1,5:0};
   	 	var mantissa = parseInt(fm[('$!session.COMPANYSETTING_IN_SESSION.mantissa' || 1)]||2);
   	 	if(mantissa>3)
   	 		return parseFloat(Ext.formatMoney(v,mantissa).toFixed(2));
   	 	else
   	 	  return isNaN(parseFloat(v)) ? v : parseFloat((parseInt(v*Math.pow(10,mantissa))/Math.pow(10,mantissa).toFixed(2)));
   },
   selectProductDataHandler:function(data){
   		if(data && this.giftsPattern){
			data.salePrice = 0;
			data.status = 5;
		} 
		var o = this.addRowForProductDataHandler(data);
    	this.editGrid.store.loadData({result:[o]},this.append);
    	this.append = true;
    	var lastRow = this.editGrid.store.getCount()-1;
    	this.editGrid.getSelectionModel().select(lastRow,0);
   		var status = parseInt(this.fp.form.findField("status").getValue());
   		var getProductStockWithStockUrl = "productStock.ejf?cmd=getProductStockWithStock" 
    	if(status>0){
    		getProductStockWithStockUrl = "productStock.ejf?cmd=getProductStockWithStock&comeback=true"
    	}
   		Ext.Ajax.request({url:getProductStockWithStockUrl,params:{product:data.id},callback :function(ops,success,response){
			var map=Ext.decode(response.responseText);
			var m = new Ext.util.MixedCollection();
			m.addAll(map);
		    this.editGrid.store.getAt(lastRow).stockInfo=m;
            this.fp.form.findField("key").reset();
            this.countAll(false);
          	this.autoColumn(lastRow);
            //this.customSelectRow(lastRow+1);
            this.haveProducts = true;
 		},scope:this});
   },
   seachProductWear:function(){
   			this.buildProductSelectWin();
   	       	this.productSelectWin.on("hide",function(){
   	       		this.productSelectWin.list.grid.store.removeAll();
   	       		this.formFocus();
   	       	},this,{single:true});
   	    	var key =  this.fp.form.findField("key").getValue();
   	    	if(key&&key.trim().length<2){
   	    		 Disco.Ext.Util.msg("","输入货号的长度至少两个字符!");
   	    		 this.formFocus();
   	    		return false;
   	    	}
			this.productSelectWin.show();
	   	    this.productSelectWin.list.grid.getGridEl().mask("加载库存商品...","x-mask-loading");
	   	    var status = this.fp.form.findField("status").getValue();
	   	    var getProductUrl = "productStock.ejf?cmd=productStockWearByDepot" 
   	    	status = parseInt(status);
   	      	if(status>0){
   	       		 getProductUrl = "productStock.ejf?cmd=productStockWearByDepot&comeback=true"
   	        }
 	   	    Ext.Ajax.request({url:getProductUrl,
	   	                    params:{sn:key},
			   	            callback :function(ops,success,response){
						    var map=Ext.decode(response.responseText);
						    var tag = false;
						     for (var n in map) {
						     	  if(n==="tag")tag = true;
						     }
						     if(tag){
						     	 Disco.Ext.Util.msg("","请设置零售库,请设置零售库!");
						    	 this.productSelectWin.hide();
						    	 this.formFocus();
							     this.productSelectWin.list.grid.getGridEl().unmask();
						    	return false;
						     }else  if(!map || !map.result){
						    	 Disco.Ext.Util.msg("","该货品不存在或者是库存中没有了!");
						    	 this.productSelectWin.hide();
						    	 this.formFocus();
							     this.productSelectWin.list.grid.getGridEl().unmask();
						    	 return false
						    }else{
						    	this.productSelectWin.list.grid.store.removeAll();
								this.productSelectWin.list.grid.store.loadData(map);
						    }
							this.productSelectWin.list.grid.getGridEl().unmask();
				     		},scope:this});
   		},
   	formFocus:function(){
		this.fp.form.findField("key").focus(false,100);
		this.fp.form.findField("key").reset();
	},
   	customSelectRow:function(row){
   		this.clearRowSelections();
   		this.selectedRow=row;//从一开始
   		//this.editGrid.getSelectionModel().select(row-1,0);
   		this.editGrid.getView().onRowSelect(row-1);
   		this.editGrid.getView().focusRow(row-1);
   		 var record =this.getSelectedRecord();
   		 if(record&&record.get("product").id){
   		 	this.fp.form.findField("remark1").setValue("当前库存为"+record.get("residualStoreNum"));
   		 }
   	},
   	customSelectFirstRow:function(){  //down
   		 if(!this.selectedRow)
   			  this.customSelectRow(1);
   		   else{
   		  	 if(this.selectedRow==this.editGrid.store.getCount()){
   		  	 	this.customSelectRow(1);
   		  	 }else{
   		  	  	this.customSelectRow(this.selectedRow+1);
   		  	 }
   		  }
   	},
   	customSelectLastRow:function(){  //up
   		if(!this.selectedRow){
	   		this.customSelectRow(this.editGrid.store.getCount());
   		}else{
   		  	 if(this.selectedRow==1){
   		  	 	this.customSelectRow(this.editGrid.store.getCount());
   		  	 }else{
   		  	  	this.customSelectRow(this.selectedRow-1);
   		  	 }
   		  }
   	},
   	clearRowSelections : function(){
   		var count = this.editGrid.store.getCount();
        for (var index = 0; index < count; index++) {
           this.editGrid.getView().onRowDeselect(index);
      	}
      	this.editGrid.getSelectionModel().clearSelections();
	  },
	 baseEditGridListeners:{	
		 	beforerequirededit:function(grid,row,col){
	    		return true;
	    	},
			beforeedit : function(e) {
				if(e.record.columnSeq.indexOf(e.field)==-1){
					return false;
				}
				if(e.field=="color" ){
					var stockInfo = e.record.stockInfo;
					var  colors  = [];
					stockInfo.each(function(o){
						if(o[0] && colors.indexOf(o[0])<0){
					 		colors.push(o[0]);
					 	}
					});
					this.colorEditor.store.loadData(colors);
				}else if(e.field=="sizeType"){
					 var stockInfo = e.record.stockInfo;
					 var  sizeTypes  = [];
					 if(e.record.get("color")){
					 stockInfo= stockInfo.filterBy(function(o){
					 	if(o[0].id==e.record.get("color").value){
					 		return o;
					 	}
					 },this);
					 stockInfo.each(function(o){
						 if(o[1] && sizeTypes.indexOf(o[1])){
						      sizeTypes.push(o[1]);
						  }
					 })
					 }else{
						 stockInfo.each(function(o){
					 	if(o[0] && sizeTypes.indexOf(o[0])<0){
					 		sizeTypes.push(o[0]);
					 	}
					 	});
					}
					 this.sizeTypeEditor.store.loadData(sizeTypes);
				}
			},
			afteredit : function(e) {                   
				var s="price,num,salePrice,encapNum";
				if (s.indexOf(e.field)>=0 ||e.field.indexOf("size-")>=0) {// 计算合计
					this.autoCountData(e.record);
				} 
				if(e.field == "color"){
					if(e.record.columnSeq[1]){
						var column = this.editGrid.getColumnModel().findColumnIndex(e.record.columnSeq[1]);
						this.editGrid.startEditing(e.row,column,true);
					}
				}
				
				if(e.record.columnSeq.length>0&&e.record.columnSeq.length==1){
					var lastField = e.record.columnSeq[e.record.columnSeq.length-1];
				    if(e.field == lastField){
				    	var stockInfo = e.record.stockInfo;
						var index = stockInfo.findIndexBy(function(o){
							if(o[0] && o[0].id == e.record.get(lastField).value){
								return true;
							}
						});
					 if(index>=0){
					 	var o = stockInfo.itemAt(index);
					 	  var remark1 = o[1];
					 	  this.fp.form.findField("remark1").setValue("当前库存为"+remark1);
						  e.record.set("residualStoreNum",remark1);
						  this.formFocus();
					 }
					}
				}else if(e.record.columnSeq.length>0&&e.record.columnSeq.length==2){
				  var lastField = e.record.columnSeq[e.record.columnSeq.length-1];
					if(e.field == lastField){
						var color = e.record.get("color")?e.record.get("color").value:null;
						var sizeType = e.record.get("sizeType")?e.record.get("sizeType").value:null;
						if(color||sizeType){
							var stockInfo = e.record.stockInfo;
							var index = stockInfo.findIndexBy(function(o){
								if(o[0] && o[0].id==color && (o[1].id||false)==sizeType){
									return true;
								}
							});
						}
						var remark1 = "";
						if(index>=0){
							var o = stockInfo.itemAt(index);
							 remark1 = o[2];
						}else{
						  	if(e.field=='sizeType'){
						  		e.record.set("color",null);
						  	}else{
						  		e.record.set("sizeType",null);
						  	}
						}
						this.fp.form.findField("remark1").setValue("当前库存为"+remark1);
						e.record.set("residualStoreNum",remark1);
						this.formFocus();
					}
				}
			/*	if(e.field == "sizeType"){
					if(!e.record.data.color){
						var stockInfo = e.record.stockInfo;
						var index = stockInfo.findIndexBy(function(o){
							if(o[0].id == e.record.get("sizeType").value){
								return true;
							}
						});
						
					 if(index>=0){
					 	var o = stockInfo.itemAt(index);
					 	  remark1 = o[1];
					 	  this.fp.form.findField("remark1").setValue("当前库存为"+remark1);
							e.record.set("residualStoreNum",remark1);
					 }
					}
				}*/
				if(this.afterEdit) this.afterEdit(e);
			}
	},
	afterEdit:function(e){
		this.customSelectRow(e.row+1);
	},
	onSave:function(form,action){
   		this.tempData.clear();
   		var ret = Ext.decode(action.response.responseText);
		 if(ret.data.yanzhi){   		
		 	Ext.MessageBox.alert("提示",ret.data.msg,function(){
		 		this.fp.form.findField("key").setDisabled(false);
		 	},this);
   		    this.complete = false;
   		    this.payment = false;
   			var paymentCount = this.paymentGrid.store.getCount();
   			this.fp.form.findField("remark1").setValue("取消付款!");
   			this.fp.form.findField("received").setValue(null);
   			this.fp.form.findField("receive").setValue(null);
   			this.fp.form.findField("unpaid").setValue(null);
   			this.fp.form.findField("key").setValue(null);
   			this.paid = 0.00;
   			this.paymentGrid.store.removeAll();
   			this.countPay();
		 	return false;
		 }
   		if(this.win){
   		   (function(){this.create()}).defer(1000,this);
   		}
   		if(ret.success&&!this.noPrint)
	   		if(ret.data){
	   			if(!ret.data['xml'] && !Ext.isIE){
	   			 	window.location.href="Lanyowebprinter://webprint/"+ret.data
	   			}else if(ret.data['xml']){
	   				Ext.Ajax.request({url:this.baseUrl,params:{cmd:"print",id:ret.data.id},callback:function(options,success,response){
						Disco.Ext.Util.executeLocalCommand("retailPostPrint",response.responseText);
					}});
	   				//Disco.Ext.Util.executeLocalCommand("retailPostPrint",ret.data.xml);
	   			}
	   		}
	   this.vipUser = null;
	   if(this.vipWin)this.vipWin.hide();
   	},
   	beforeSave:function(){
		var o = Disco.Ext.Util.getEditGridData(this.paymentGrid,"payment_");
		var charge  = this.fp.form.findField("charge").getValue();
		o.payment_paymentSum[0]=o.payment_paymentSum[0]-charge;
		this.fp.form.baseParams = Ext.applyIf(this.fp.form.baseParams,o);
		if(this.vipUser)
			Ext.apply(this.fp.form.baseParams,{cardNumber:this.vipUser.cardNumber});	
   	 	if(this.beforeSaveCheck()===false){
			return false;
		}
   	},
   	comboGetValue:function(obj){
   		return obj ? Ext.value((obj.id || obj.value),'') : '';
   	},
   	//检查销售数量和库存数量
   	otherCheckNum:function(){
   		var checkDataMx = new Ext.util.MixedCollection(false);
   		var store=this.editGrid.store;
   		
   		store.each(function(record){
			var product  = this.comboGetValue(record.get("product"));
	   		var color    = this.comboGetValue(record.get("color"));
	   		var sizeType = this.comboGetValue(record.get("sizeType"));
	   		var num = record.get("num");
	   		var key = (product+"_"+color+"_"+sizeType).toString();
	   		if(checkDataMx.containsKey(key)){
	   			var previousNum = checkDataMx.key(key);
		   	   checkDataMx.replace(key,previousNum+num);
	   		  }else{
	           checkDataMx.add(key,num);
	   		   }
   		},this);
   		
   		var mc = checkDataMx.getCount();
   		var o = {};
   		if(mc>0){
   			o["item_product"] = [];
   			o["item_color"] = [];
   			o["item_sizeType"] = [];
   			o["item_num"] = [];
   		}
   		checkDataMx.eachKey(function(key,item,index){
   			 var param = key.split("_");
   			 var num = checkDataMx.get(key);
   			o["item_product"][index] = param[0];
   			o["item_color"][index] = param[1];
   			o["item_sizeType"][index] = param[2];
   			o["item_num"][index] =num;
   		},this);
   		Ext.Ajax.request({url:"retailBill.ejf?cmd=checkProductStock",
   						 method:"POST",
   						 params:o,
   						 success:function(response){
	   						var ret = Ext.decode(response.responseText);
							if(!ret.success){
								var errors = "";
								Ext.each(ret.data,function(error){
								  		 errors+=error.sn;
  								  		 if(error.color){
								  		   errors+=("-"+error.color);
								  		 }else{
										   errors+=";"								  		 
								  		 }
								  		 if(error.sizeType){
								  		   errors+=("-"+error.sizeType);
								  		 }else{
								  		 	errors+=";"
								  		 }
								});
								errors+="销售的数量不能够大于库存!请确定销售数量";
								Ext.MessageBox.alert("提示",errors,function(){
									 this.formFocus();
								},this);
								this.payment=false;
							}else{
								this.readyPay();
							}
   						 },
   						 scope:this
   						});
   	},
   	readyPay : function(){
   		this.payment=true;
		var count = this.editGrid.store.getCount();
		var sum = this.editGrid.store.sum("totalAmount",0,count-1);
		Disco.Ext.Util.msg("提示","未付款         "+ this.toFixed(sum));
		this.fp.form.findField("unpaid").setValue(this.toFixed(sum));
		this.fp.form.findField("remark1").setValue("货品输入完毕,请开始付款");
	    this.formFocus();	
   	},
   	cancelPreferential:function(){
   		this.payment=true;
		var count = this.editGrid.store.getCount();
		var sum = this.editGrid.store.sum("totalAmount",0,count-1);
		Disco.Ext.Util.msg("提示","未付款         "+ this.toFixed(sum));
		this.fp.form.findField("receivable").setValue(this.toFixed(sum));
		this.fp.form.findField("unpaid").setValue(this.toFixed(sum));
		this.fp.form.findField("remark1").setValue("货品输入完毕,请开始付款");
		this.fp.form.findField("preferential").setValue("");
	    this.formFocus();	
   	},
   	onPreferential:function(){
   		var status = parseInt(this.fp.form.findField("status").getValue());
   		if(status==0 && this.payment && !this.fp.findSomeThing("preferential").getValue()){
   			Disco.Ext.Util.msg("提示","请输入整单优惠后价格");
   			this.setAction("preferential");
   			this.payment=false;
   		}
   	},
   	doPreferential:function(){
   		var key = this.fp.form.findField("key").getValue();
   		if(!key || isNaN(parseFloat(key))){
   			Ext.MessageBox.alert("提示","请输入正确的金额!",function(){
   				this.formFocus();
   			},this);
   			return false;
   		}else{
   			this.payment=true;
			Disco.Ext.Util.msg("提示","未付款         "+ key);
			this.fp.form.findField("unpaid").setValue(this.toFixed(key));
			this.fp.form.findField("receivable").setValue(this.toFixed(key));
			this.fp.form.findField("remark1").setValue("开始付款");
			var sum = this.editGrid.store.sum("totalAmount",0,this.editGrid.store.getCount()-1);
			this.fp.form.findField("preferential").setValue(this.toFixed(sum-key));
		    this.formFocus();
   		}
   	},
   	beforeSaveCheck:function(otherCheck){
   		var store=this.editGrid.store;
		if(store.getCount()==0 || (store.getCount()==1 && store.getAt(0).get("product") && isNaN(parseInt(store.getAt(0).get("product").id)))){
			Ext.Msg.alert("错误提示","没有单据明细，不能保存！");
			return false;
		}
		var sumGifts = 0;
		if(store && store.getCount()>=1){
			var notNull=[];
			var canSave=true;
			store.each(function(record,sequence){
				if(record.get('status')==5 && record.get('gifts')){
					sumGifts+=(isNaN(parseFloat(record.get('gifts'))) ? 0 : parseFloat(record.get('gifts'))) ;
				}
				if(!isNaN(parseInt(record.get("product")))){
					var temp={};
					if(record.columnSeq.length>0){
						Ext.each(record.columnSeq,function(o){
							if(!record.get(o).value){
								temp.sequence=sequence+1;
								if(0=="sizeType"){
									temp.nf = "尺码";
								}else{
									temp.nf = "颜色";
								}
								if(canSave) canSave=false;
							}
						},this)
					}
					/*if(!r.get("color") && (r.get("product").attributeType==0 ||r.get("product").attributeType==1)){
						temp.sequence=sequence+1;
						temp.nf=["颜色"];
						if(canSave) canSave=false;
					}
					if(!r.get("num") || r.get("num")==0 || r.get("num")=='0'){
						if(temp.sequence){
							temp.nf.push("数量");
						}else{
							temp.sequence=sequence+1;
							temp.nf=["数量"];
						}
					}
					*/
					if(otherCheck && otherCheck.call(this,record,temp)===false){
						temp.sequence=sequence+1;
						if(canSave) canSave=false;
					}
					if(temp.sequence)
						notNull.push(temp);
				};
			},this);
			if(notNull.length>0){
				if(!canSave){
					var s="以下行数据错误，不能保存：<br>";
					for(var i=0;i<notNull.length;i++){
						var temp=notNull[i];
						s+="行 "+temp.sequence+" : "+temp.nf+" 不能为空！ <br>";
					}
					Ext.Msg.alert("错误提示",s,function(){
					   this.formFocus();
					   this.fp.form.findField("key").setDisabled(false);
					},this);
					return false;
				}
			}
		}
		if(sumGifts>0){
			if(!this.vipUser){
					Disco.Ext.Msg.error("表单中存在赠送商品!<br\>请你先选择会员在保存!",false,this.formFocus,this);
				return false;
			}else{
				this.vipUser.integration = parseFloat(this.vipUser.integration) || 0;
				if(this.vipUser.integration<sumGifts){
					Disco.Ext.Msg.error("会员的积分不足!",false,this.formFocus,this);
				return false;
				}
			}
		}
	 	return BaseStockOutcomeWearBillPanel.superclass.beforeSave.call(this);   
   	},
   	countPay:function(){
	   	 var count = this.paymentGrid.store.getCount();
	   	 this.paid = this.paymentGrid.store.sum("paymentSum",0,count-1);
	     var count = this.editGrid.store.getCount();
   		 var sum = parseFloat(this.fp.findSomeThing("receivable").getValue());
		 if(this.paid>sum){
		   		return false;
		  }else if(this.paid==sum){ //完成付款了
		  	this.complete = true;
		  	Disco.Ext.Util.msg("提示","支付成功！请按任意键保存数据并且重新开单!");
   			this.fp.form.findField("remark1").setValue("支付成功！请按任意键保存数据并且重新开单!按[取消]键取消交易!");
		  	}
	   	 this.fp.form.findField("receive").setValue(this.toFixed(this.paid));
	     this.fp.form.findField("received").setValue(this.toFixed(this.paid));
   		 this.fp.form.findField("unpaid").setValue(sum-this.paid);
  	  	 this.fp.form.findField("total").setValue(this.toFixed(sum));
   	},
   	countReceivable:function(){ //计算应售
   		var receivableTotal = 0.0;
	 	this.editGrid.store.each(function(record){
			if(record.get("num")&&record.get("salePrice")){
		  		receivableTotal+=Ext.num(parseFloat(record.get("num")),0)* Ext.num(parseFloat(record.get("salePrice")));
			}
	    },this);
   		this.fp.form.findField("behoveSell").setValue(this.toFixed(receivableTotal));
   	},
   	countRealizedSell:function(){ //计算实售
		   	var count = this.editGrid.store.getCount();
		    var sum = this.editGrid.store.sum("totalAmount",0,count-1);
		    this.fp.form.findField("realizedSell").setValue(this.toFixed(sum));
		    this.fp.form.findField("receivable").setValue(this.toFixed(sum));
   	},
   	countNumber : function(){ //计算卖货数量
		   	var count = this.editGrid.store.getCount();
		    var sum = this.editGrid.store.sum("num",0,count-1);
		    this.fp.form.findField("number").setValue(sum);
   	},
   	countPrivilege:function(){ //计算优惠
   		var count = this.editGrid.store.getCount();
		 var   receivableTotal = 0.0;
		this.editGrid.store.each(function(record){
			if(record.get("num")&&record.get("salePrice")){
			  receivableTotal+=parseFloat(record.get("num"))*parseFloat(record.get("salePrice"));
			}
		},this)
      var factReceivable = this.editGrid.store.sum("totalAmount",0,count-1);
      this.fp.form.findField("privilege").setValue(this.toFixed((receivableTotal-parseFloat(factReceivable))));
   	},
   	getSelectedRecord:function(){
   		if(this.selectedRow){
   			return this.editGrid.store.getAt(this.selectedRow-1);
   		}
     	return this.editGrid.getSelectionModel().getEditRowRecord()
   	},
   	getDisCount:function(dis){
   		
   		return ;
   	},
   	countAll:function(focus){
   		this.countNumber();
		this.countReceivable();
   	    this.countRealizedSell();
   	    this.countPrivilege();
		if(focus)this.formFocus();
   	},
   	updateNumber:function(){
   		if(!this.haveProducts) return false;
   		if(this.payment) return false;
   		 var selectRecord =this.getSelectedRecord();
   		 var  num  = this.fp.form.findField("key").getValue();
   		 var status = this.fp.form.findField("status").getValue();
   		  if(!Disco.Ext.Util.isNumber(num)){
   			 	Disco.Ext.Util.msg("提示","请输入数字!");
   				return false;
   			}
   			//退货不检查输入的数量
   		 if(parseInt(status)==0&&selectRecord.get("residualStoreNum")<parseInt(num)){
   		 	Disco.Ext.Util.msg("提示","输入的数量大于当前库存数量,请重新输入!");
   		 	return false;
   		 }else if(parseInt(status)>0&&parseInt(num)<0){
   		 	Disco.Ext.Util.msg("提示","在退货模式下面不能录入负数!");
   		 	return false;
   		 }
		 selectRecord.set("num",parseInt(num));
		 this.autoPrice(selectRecord,"num");
		 this.countAll(true);
   	},
   	delProduct:function(){ //按下Esc时删除
   		if(!this.haveProducts) return false;
   		if(this.payment) return false;
   		/* var selectRecord = this.getSelectedRecord();
   		 this.editGrid.store.remove(selectRecord);*/
   		 this.removeGridRow(this.editGrid);
   		 this.editGrid.getView().refresh();
   		 this.countAll(true);
   		 if(this.editGrid.store.getCount()<1)this.tempData.clear("firstShippingGuide");
   	},
   	removeGridRow:function(grid,callback){   
		if(this.selectedRow){
    	var r=this.getSelectedRecord();
		grid.store.remove(r);
		if(grid.store.getCount()<this.selectedRow){
		this.selectedRow--;
		this.customSelectRow(this.selectedRow);
		}
		
		}
		else Ext.MessageBox.alert("提示","请选择要删除的记录!");
   	},
   	updateColor:function(){
   		if(!this.haveProducts) return false;
   		if(this.payment) return false;
   		var selectRecord = this.getSelectedRecord();
   		var rowIndex =  this.editGrid.store.indexOf(selectRecord);
   		var column = this.editGrid.getColumnModel().findColumnIndex("color");
		this.editGrid.startEditing(rowIndex,column,false);
		/**
		var colorEditor=this.editGrid.getColumnModel().getCellEditor(column,rowIndex).field;
		 if(colorEditor.store.getCount()>0){
		 	var view = this.editGrid.getView();
			(function(){
				colorEditor.onTriggerClick();
				colorEditor.list.alignTo(view.getCell(rowIndex,column), 'tl-bl?');
			}).defer(1000,this);
		 }
		 */
   	},
   	updateSizeType:function(){
   		if(!this.haveProducts) return false;
   		if(this.payment) return false;
   		var selectRecord = this.getSelectedRecord();
   		var rowIndex =  this.editGrid.store.indexOf(selectRecord);
   		var column = this.editGrid.getColumnModel().findColumnIndex("sizeType");
		this.editGrid.startEditing(rowIndex,column,false);
		/**
		var sizeTypeEditor=this.editGrid.getColumnModel().getCellEditor(column,rowIndex).field;
		 if(sizeTypeEditor.store.getCount()>0){
		 	var view = this.editGrid.getView();
			(function(){
				sizeTypeEditor.onTriggerClick();
				sizeTypeEditor.list.alignTo(view.getCell(rowIndex,column), 'tl-bl?');
			}).defer(1000,this);
		 }
   		*/
   		},
   	updateDiscount:function(){  //选中一行,按home键修改折扣
   		if(!this.haveProducts) return false;
   		if(this.payment) return false;
   		var selectRecord = this.getSelectedRecord();
   		var discount  = this.fp.form.findField("key").getValue();
   		if(!Disco.Ext.Util.isNumber(discount)){
   			 	Disco.Ext.Util.msg("提示","请输入数字!");
   				return false;
   		}
		discount = parseFloat(discount);
		selectRecord.set("discount",discount.toFixed(2));
		this.autoPrice(selectRecord,"discount");
	  	this.countAll(true);
   		},
   	updateDiscountPrice:function(){  //选中一行,按End键修改优惠价
   		if(!this.haveProducts) return false;
   		if(this.payment) return false;
   		var selectRecord = this.getSelectedRecord();
   		var discountPrice  = this.fp.form.findField("key").getValue();
   		if(!Disco.Ext.Util.isNumber(discountPrice)){
   			 	Disco.Ext.Util.msg("提示","请输入数字!");
   				return false;
   			}
   			discountPrice = parseFloat(discountPrice);
   			selectRecord.set("discountPrice",discountPrice.toFixed(0));
   			this.autoPrice(selectRecord,"discountPrice");
   		    this.countAll(true);
   		},
   	updateSalePrice:function(){ },
   	comeback:function(){
   	   	var data=[[1,'质量问题'],[2,'客户不满意'],[3,'换货'],[4,'未知原因']];
			var store=new  Ext.data.SimpleStore({fields:["id","name"]});
			var grid = new Ext.grid.GridPanel({
				border:false,
				frame:false,
				selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
				height:130,
				width:180,	
				columns:[{header:"No",dataIndex:"id"},
						{header:"退货原因",dataIndex:"name"}],
				store:store,
				autoExpandColumn:1
		});
		var choice = function(grid2,rowIndex,e) {
		var record = grid.getSelectionModel().getSelected();
		if(record){
			var retailType = this.fp.form.findField("retailType");
		    retailType.setValue(record.get("name"));
			retailType.el.parent('div.x-form-item').first('label').dom.innerHTML = "退货";
			this.fp.form.findField("behoveSell").el.parent('div.x-form-item').first('label').dom.innerHTML = "应收:";
			this.fp.form.findField("realizedSell").el.parent('div.x-form-item').first('label').dom.innerHTML = "实收:";
			this.fp.form.findField("receivable").el.parent('div.x-form-item').first('label').dom.innerHTML = "应付:";
			this.fp.form.findField("received").el.parent('div.x-form-item').first('label').dom.innerHTML = "已付:";
			this.fp.form.findField("status").setValue(record.get("id"));
		}
		   win.hide();
		};
		grid.on("rowdblclick",choice, this);
	    store.on("load", function() {
	    	(function(){grid.getSelectionModel().selectRow(0);
			grid.getView().focusRow(0);}).defer(200);
		},this);
      	if(!win)
      	 var win =  new Ext.Window({
                    width:250,
                    autoHeight:true,
                    layout:'fit',
                    modal: true,
                    closeAction:"hide",
                    shim:true,
                    items:grid,
                    onEsc : function(){
    					    this[this.closeAction]();
    				},
                    keys:[{key : Ext.EventObject.ENTER,
					fn : choice,
					scope:this,
					stopEvent:true}]
          });
        win.on("show",function(){
	        win.getEl().setOpacity(0.9);
	        store.loadData(data);
        },this);
      return (function(k,e){
      			if(e.ctrlKey) return false;
		      	//if(!this.tempData.containsKey("firstShippingGuide")) return false;
		      		//如果有了货品就不能够再改变销售或者退货的状态了.
		      	if(this.editGrid.getStore().getCount()>0){
			      	var product = this.editGrid.getStore().getAt(0).get("product");
			        if(product.id) return false;
		      	}
		        win.on("hide",function(){
			         this.formFocus.defer(50,this);
			        },this)
		        win.show(this.fp.form.findField("key").getEl());
      			});
   	},
    createCreditCardTypeWin : function(k,e){
      			if(e && e.ctrlKey) return false;
      			if(!this.payment) return false;
      			var status = this.fp.form.findField("status").getValue();
	   	    	status = parseInt(status);
	   	    	if(status>0){
	   	    		Disco.Ext.Util.msg("提示","在退货模式只能用信用卡!");
	   	    	   return false;
	   	    	}
      			Ext.each(this.paymentTypes,function(o){
	            if(o.name=="信用卡"){
	            	this.paymentTypeTitle = "信用卡";
	            	Disco.Ext.Util.msg("提示","信用卡支付!");
	        		this.paymentTypeId = o.id;
	            }
	          	},this);
          		var store=new  Ext.data.JsonStore({fields:["id","title"]});
				var grid = new Ext.grid.GridPanel({
					border:false,
					frame:false,
					selModel: new Ext.grid.RowSelectionModel({singleSelect:true}),
					height:130,
					width:180,	
					columns:[ 
							new Ext.grid.RowNumberer({header:"序号",width:33}),
							{header:"信用卡类型",dataIndex:"title"},
							{header:"id",dataIndex:"id",hidden:true}
								],
					store:store,
					autoExpandColumn:1
				});
			var choice = function(grid2,rowIndex,e) {
			this.cardFormPanel  = new Ext.form.FormPanel({
				labelWidth:60,
				labelAlign:"right",
				height:160,
				width:180,	
				autoShow:true,
				defaults:{style:"font-size:25px;font-weight:bold;padding-top:3px",labelStyle:"font-weight:bold;font-size:25px;width:65px;"},
				items:[{xtype:"hidden",name:"cardTypeId"},
  					   {xtype:"labelfield",fieldLabel:"类型",name:"cardType"},
					   {xtype:"numberfield",fieldLabel:"卡号",name:"cardNO",enableKeyEvents:true,width:300,height:50,
					   		listeners:{
					   			  "keypress":function(field,e){
					   			    if(e.getKey() == Ext.EventObject.ENTER){
					   			    	if(field.getValue()){
						   			        this.cardFormPanel.form.findField("cardAmount").focus();
					   			    	}else{
					   			    	   Ext.MessageBox.alert("提示","信用卡号不能够为空!",function(){
					   			    	   		field.focus();
					   			    	   },this);
					   			    	}
					   			    }
					   			  },
					   			  scope:this
					   			
					   			}},
					   {xtype:"numberfield",fieldLabel:"金额",name:"cardAmount",enableKeyEvents:true,width:300,height:50,
					   	listeners:{
					   			  "keypress":function(field,e){
					   			    if(e.getKey() == Ext.EventObject.ENTER){
					   			    	var cardTypeId = this.cardFormPanel.form.findField("cardTypeId").getValue();
										var cardNO = this.cardFormPanel.form.findField("cardNO").getValue();
										var cardAmount = this.cardFormPanel.form.findField("cardAmount").getValue();
										if(cardNO&&cardAmount){
						   			    	this.paymentGrid.store.loadData([{paymentType:this.paymentTypeId,paymentTypeTitle:this.paymentTypeTitle,paymentSum:cardAmount,cardType:cardTypeId,cardNO:cardNO}],true);
						   			    	if(this.countPay()===false){
						   			    		var paymentCount = this.paymentGrid.store.getCount();
						   			    		var record  = this.paymentGrid.store.getAt(paymentCount-1);	   				
												this.paymentGrid.store.remove(record);
												this.countPay();
						   			    		Ext.Msg.alert("错误提示","有信用卡支付的不能超出应收账款!将取消该信用卡支付",function(){
						   			    			this.formFocus();
						   			    		},this);
						   			    	   win.purgeListeners();
						   			    	}
						   			    	this.paymentTypeId = false; //设置为false 是为了再次付款为现金的形式
						   			      	win.hide();
										}else{
											return false;
										}
					   			    }
					   			  },
					   			   scope:this
					   			}
					   }]
			});
			var p=win.remove(0);
			win.add(this.cardFormPanel);
			win.setHeight(180);
			win.setWidth(400);
			win.doLayout();
			win.mask.on("click",function(){this.cardFormPanel.form.findField("cardNO").focus();},this)
			var record = grid.getSelectionModel().getSelected();
			if(record){
				this.cardFormPanel.form.findField("cardTypeId").setValue(record.get("id"));
				this.cardFormPanel.form.findField("cardType").setValue(record.get("title"));
				this.cardFormPanel.form.findField("cardNO").focus();
			}
		};
		grid.on("rowdblclick",choice, this);
		grid.on("keypress",function(e){
			 if(e.getKey() == Ext.EventObject.ENTER){
			 		choice.call(this);
			 }
		},this);
	    store.on("load", function() {
	    	(function(){
				grid.getSelectionModel().selectRow(0);
				grid.getView().focusRow(0);
			}).defer(100);
		},this);
		if(!win)
      	 var win =  new Ext.Window({
                    width:300,
                    height:150,
                    autoHeight:true,
                    layout:'fit',
                    modal: true,
                    closeAction:"hide",
                    shim:true,
                    items:grid,
                    onEsc : function(){
    					    this[this.closeAction]();
    				}
          });
        win.on("show",function(){
        	Ext.Ajax.request({url:"companyDictionary.ejf?cmd=getDictionaryItemBySn",
        				    params:{sn:"creditCardType"},
        				    scope:this,
        				    success:function(response){
        				    	var ret  =  Ext.decode(response.responseText);
        				    	 win.getEl().setOpacity(0.9);
        				    	 if(ret&&ret.result&&ret.result.length>0){
		       						 store.loadData(ret.result);
        				    	 }else{
        				    	 	Ext.MessageBox.alert("提示","请设置信用卡的类型!",function(){
        				    	 		this.paymentTypeId = false;
        				    	 		win.hide();
        				    	 	},this)
        				    	 }
        				    	}
        					})
        },this,{single:true});
       win.on("hide",function(){
       	 this.paymentTypeId = false;
		 this.formFocus.defer(50,this);
	    },this,{single:true});
	   if(win.getComponent(0).getXType()==="form"){
	  		win.remove(0);
	  		win.setHeight(180);
			win.setWidth(130);
	  		win.add(this.cardFormPanel);
	  		win.doLayout();
		  }
		 win.show();
         var xy = win.el.getAlignToXY(win.container, 'c-r',[-260, -130]);
   		 win.setPagePosition(xy[0], xy[1]);
    },   	
   	getEditColumnModel:function(){
		this.productEditor=new ProductComboBox(Ext.apply({},{
				 returnObject:true,
				 name:"productInput",
				 hiddenName:"productInput",
				 displayField:"sn",
				 valueField:"id",			
				 width:300,	
				 selectSingle:true,
				 mode:"local", //禁止远程调用,
				 choiceValue:this.selectRowData.createDelegate(this)
				 },ConfigConst.CRM.product));
		/*this.depotLocationEditor=new Disco.Ext.SmartCombox({
								returnObject:true,
								name : "depotLocationInput",
								hiddenName : "depotLocationInput",
								fieldLabel : "depotLocationInput",
								displayField : "title",
								valueField : "id",
								store : new Ext.data.JsonStore({
									fields : ["id",'title']
								}),
								editable : false,
								mode : 'local',
								triggerAction : 'all',
								emptyText : '请选择...'
							}
		);	*/	
		this.colorEditor=new Disco.Ext.SmartCombox({
								returnObject:true,
								name : "colorInput",
								hiddenName : "colorInput",
								fieldLabel : "colorInput",
								displayField : "title",
								valueField : "id",
								allowBlank:false,
								store : new Ext.data.JsonStore({
									fields : ["id","sn","title"]
								}),
								editable : false,
								mode : 'local',
								triggerAction : 'all',
								emptyText : '请选择...'
							});
		this.sizeTypeEditor=new Disco.Ext.SmartCombox({
								returnObject:true,
								name : "sizeTypeInput",
								hiddenName : "sizeTypeInput",
								fieldLabel : "sizeTypeInput",
								displayField : "title",
								valueField : "id",
								allowBlank:false,
								typeAhead:true,
								store : new Ext.data.JsonStore({
									fields : ["id","sn","title"]
								}),
								editable : false,
								mode : 'local',
								triggerAction : 'all',
								emptyText : '请选择...',
								listeners:{"blur":function(){
										this.formFocus();
										},scope:this}
							});
		var cms=[				
		 		 new Ext.grid.RowNumberer({header:"<font size='3' color='000000'>序号</font>",dataIndex:"sequence",width:40}),
		 		 {header:"Id",dataIndex:"id",width:1,hidden:true,hideable:false},
		 		 {dataIndex:"status",width:1,hidden:true,hideable:false},
		 		 {dataIndex:"gifts",width:1,hidden:true,hideable:false},
		 		 {header:"<font size='3' color='000000'>导购员</font>",dataIndex:"shoppingGuide",width:60,hidden:false,renderer:this.objectRender("trueName")},
		 		 {header:"<font size='3' color='000000'>产品</font>",dataIndex:"product",width:0,hidden:true,hideable:false},
		 		// {header:"<font size='3' color='000000'>货品编号</font>",dataIndex:"stockNO",width:70,editor:this.productEditor,summaryType: 'count',summaryRenderer: function(v){return "合计("+v+")";}},
		 		 {header:"<font size='3' color='000000'>货号</font>",dataIndex:"productSn",width:80},
		 		 {header:"<font size='3' color='000000'>产品名称</font>",dataIndex:"productTitle",width:80},
                 {header:"<font size='3' color='000000'>款型</font>",dataIndex:"product",width:80,renderer:this.objectRender("style")},
		 		 {header:"<font size='3' color='000000'>颜色</font>",dataIndex:"color",width:70,editor:this.colorEditor,renderer:Disco.Ext.Util.comboxRender},
		 		 {header:"<font size='3' color='000000'>尺码</font>",dataIndex:"sizeType",width:50,editor:this.sizeTypeEditor,renderer:Disco.Ext.Util.comboxRender}
		 		 ]
		Ext.each([{header:"<font size='3' color='000000'>单价</font>",dataIndex:"price",width:50,hidden:true,renderer:this.readOnlyRender(function(v){return v&&v.toFixed?v.toFixed(2):v;})},
				 {header:"<font size='3' color='000000'>数量</font>",dataIndex:"num",width:50,editor:new Ext.form.NumberField({selectOnFocus:true}),renderer:this.numEditRender,summaryType: 'sum',summaryRenderer: function(v){return v?v.toFixed(2):"";}},
				 {header:"<font size='3' color='000000'>零售价</font>",dataIndex:"salePrice",width:70,renderer:function(v){return v&&v.toFixed?v.toFixed(2):v;}},
				 {header:"<font size='3' color='000000'>折扣</font>",dataIndex:"discount",width:50,editor:new Ext.form.NumberField({selectOnFocus:true})},
				 {header:"<font size='3' color='000000'>实售价</font>",dataIndex:"discountPrice",width:70,editor:new Ext.form.NumberField({selectOnFocus:true})},
				 {header:"<font size='3' color='000000'>金额</font>",dataIndex:"totalAmount",width:70,renderer:function(v){return v&&v.toFixed?v.toFixed(2):v;},summaryType:'sum',summaryRenderer: function(v, params, data){return v?v.toFixed(2):"";}},
				{header:"<font size='3' color='000000'>剩余库存</font>",dataIndex:"residualStoreNum",width:70,hidden:true,summaryType: 'sum',summaryRenderer: function(v){return v?v.toFixed(2):"";}},
				 {header:"<font size='3' color='000000'>备注</font>",renderer:function(v,m,r){
				 		if(r.get('status')==5){
				 			return  String.format("<font style='font-weight:bold;color:orangered;'>赠品(积分{0}兑换)</font>",!isNaN(parseInt(r.get('gifts'))) ? parseInt(r.get('gifts')) : 0);
				 		}
				 },dataIndex:"remark",editor:new Ext.form.TextField()}],function(o){
				 cms[cms.length]=o;
				 });
		return new Ext.grid.ColumnModel(cms);
	},
	createEditGrid:function(){
		 PosPanel.superclass.createEditGrid.call(this);
		 this.paymentGrid = this.createPaymentGrid.call(this);	
		 this.paymentGrid.width = 300;
		 this.paymentGrid.border = false;
		 this.paymentGrid.height=95;
	},
	otherKeyFn:function(action){
		switch(action){
			case 'vipCard':
				this.setAction('onMember');
			break;
		}
	},
	onHotKeySrowClick:function(grid,row,e){
		var record = grid.getStore().getAt(row);
		var action = record.get('action');
		if(typeof this[action] == 'function'){
			this[action]();return;
		}else{
			this.otherKeyFn(action);
		}
	},
	createForm:function(){	
		this.createEditGrid();
		var hotkeysInfo = new HotkeysInfo();
		hotkeysInfo.grid.on('rowclick',this.onHotKeySrowClick,this);
		this.editGrid.anchor="* -95";
		this.editGrid.selModel = new Ext.grid.CellSelectionModel({singleSelect:true,
																getEditRowRecord:function(){
																	return  this.selection? this.selection.record : null;
																},
																moveEditorOnEnter:false});
		this.editGrid.selModel.on("cellselect",function(sm,row,colum){
			 if(colum==0){
					 this.editGrid.getView().onRowSelect(row);
					 this.editGrid.getView().focusRow(row);
             }
		},this);
		this.editGrid.on("cellclick",function(grid,rowIndex,columnIndex){
				if(columnIndex == 0){
				this.customSelectRow(rowIndex + 1);			
				}
		},this);
		var formPanel=new Ext.form.FormPanel({
			//frame:true,
			labelWidth:20,
			labelAlign:'right',
			layout:"border",
			items:[{xtype:"hidden",name:"saveAndPrint",value:"true"},{xtype:"hidden",name:"preferential"},{xtype:"hidden",name:"vdate"},{xtype:"hidden",name:"id"},{xtype:"hidden",name:"total"}/*,{xtype:"hidden",name:"paymentType"}*/,{xtype:"hidden",name:"charge"},{xtype:"hidden",name:"receive"},{xtype:"hidden",name:"status",value:0},
	                   {xtype:"hidden",name:"types"},hotkeysInfo,{region:"center",layout:"border",border:false,items:[{region:"center",layout:"fit",items:[this.editGrid]},
				{border:false,region:"south",height:280,items:[{xtype:'panel',layout:"column",items:[{
						    columnWidth: .7,
						    layout:"column",
						    height:150,
						    border:false,                                                                     //有两种状态, 一种是退货, 一种是售出
						    items:[{columnWidth:0.33,layout:"form",height:145,items:[
						                                {xtype:"labelfield",fieldLabel:"销售",name:"retailType",anchor:"100%",labelStyle:"font-weight:bold;font-size:30px;color:#00F",labelSeparator:""},
						    							{xtype:"labelfield",fieldLabel:"VIP",name:"vip",anchor:"100%",labelStyle:"font-weight:bold;font-size:25px",style:"font-size:25px;padding-top:3px"},
						    							{xtype:"labelfield",fieldLabel:"数量",name:"number",anchor:"100%",labelStyle:"font-weight:bold;font-size:25px",style:"font-size:25px;padding-top:3px"}]},
						    	   {columnWidth:0.33,layout:"form",height:145,defaults:{anchor:"100%",labelStyle:"font-weight:bold;font-size:25px;width:65px;",style:"font-size:25px;padding-top:3px"},items:[{xtype:"labelfield",fieldLabel:"应售",name:"behoveSell",anchor:"100%"},
						    							{xtype:"labelfield",fieldLabel:"实售",anchor:"100%",name:"realizedSell"},
						    							{xtype:"labelfield",fieldLabel:"优惠",anchor:"100%",name:"privilege"}]},
						    	   {columnWidth:0.34,layout:"form",defaults:{anchor:"100%",labelStyle:"font-weight:bold;font-size:25px;width:65px;",style:"font-size:25px;padding-top:3px"},height:145,items:[
						    	 	 					{xtype:"labelfield",fieldLabel:"应收",name:"receivable"},
						    							{xtype:"labelfield",fieldLabel:"已收",name:"received"},
						    	 	 					{xtype:"labelfield",fieldLabel:"未付",name:"unpaid"}
						    							]}]
						},{
							height:145,
						    columnWidth: .3,
						    layout:"fit",
						    items:this.paymentGrid
						}]},
				{height:100,xtype:'panel',bodyStyle:"padding-buttom:17px;padding-top:17px;padding-left:50px",items:[
				     Disco.Ext.Util.columnPanelBuild(
					 {columnWidth:.4,items:new Ext.form.TextField({name:"key",
																 width:250,height:70,
																 allowBlank: false,
																 hideLabel:true,
																 anchor:"-2",
																 enableKeyEvents:true,
																 style:"font-weight:bold;font-size:55px",
																 listeners:{keyup:function(field,event){
																 if(field.getValue()){
																 	field.setValue(field.getValue().trim().toUpperCase());
																 }
																 if(this.complete&&event.getKey()!=Ext.EventObject.ESC){
																 	 field.setDisabled(true);
																	 this.save();
																 	 	/*Ext.MessageBox.confirm("请确认","你确定要提交该次交易吗？",function(ret){
																		if(ret=="yes"){
																			 field.setDisabled(true);
																			 this.save();
																		}else{
																			field.setDisabled(false);
																			this.formFocus();
																		}
																		},this);
																	 if(event.getKey()==Ext.EventObject.ENTER){
																	   field.setDisabled(true);
																	   this.save();
																	 }*/
																 }
																 },render:this.bindQuickKeys,scope:this}
																 })},						 	
					 {columnWidth:.4,items:{xtype:"labelfield",anchor:"-2",name:'remark1',value:"请先输入编码后按[确认]键选择购员!",style:"font-size:18px;padding-top:3px",hideLabel:true}},		 	
					 {columnWidth:.2,items:{xtype:"labelfield",anchor:"-2",name:'remark2',value:"",hideLabel:true}}		 	
					)]}]}]}
			]
		});
		return formPanel;
    },
    onDiscount:function(e){
   		if(!this.haveProducts) return false;
		if(this.payment) return false;
		this.updateDiscount();
		this.formFocus();
	},
	onOffers:function(){
		if(!this.haveProducts) return false;
		if(this.payment) return false;
		this.updateDiscountPrice();
		this.formFocus();
	},
	onSelShippingGuide:function(){
		if(this.payment) return false;
		this.setAction('shippingGuide');
		this.seachShippingGuide();
	},
	onPayment:function(){
	  if(!this.haveProducts) return false;
	  if(this.payment) return false;
		var count = this.editGrid.store.getCount();
		if(count<0){
			Disco.Ext.Util.msg("提示","请先选择商品再结账!");
			return  false;
		}
		if(this.beforeSaveCheck()===false){
			return false;
		}else{
		   var status = this.fp.form.findField("status").getValue();
	    	   status = parseInt(status);
	      	if(status>0){ //说明是退货,不用判断库存!
	      	 	this.readyPay();
	      	 }else{
				this.otherCheckNum();
	      	 }
		}
	},
	onNoPrint:function(){
		this.noPrint=true;
		Disco.Ext.Util.msg("提示","不需要打印了!");	
	},
	onPrint:function(){
		this.noPrint=false;
		Disco.Ext.Util.msg("提示","不需要打印了!");	
	},
	onPay:function(){
		this.pay = true;
	    Disco.Ext.Util.msg("提示","请输入缴款金额");
	},
	onPaymentCheck:function(){
		if(!this.payment) return false;
         Ext.each(this.paymentTypes,function(o){
	         if(o.name=="支票"){
	           this.paymentTypeTitle = "支票";
	           Disco.Ext.Util.msg("提示","支票支付!");
	           this.paymentTypeId = o.id;
	           }
          },this);
	},
	onPaymentMoney:function(){
		 if(!this.payment) return false;
	      Ext.each(this.paymentTypes,function(o){
	        if(o.name=="现金"){
	          this.paymentTypeTitle = "现金";
	          Disco.Ext.Util.msg("提示","现金支付!");
	          this.paymentTypeId = o.id;
	        }
	      },this);
	},
    bindQuickKeys:function(textfield){
   	var keys=[{
   		key: Ext.EventObject.LEFT,
   		fn: function(e){
   		// alert("left");
   		}},
   		{
   		key: Ext.EventObject.RIGHT,
   		fn: function(e){
   		// alert("right");
   		}},
   		{
   		key: Ext.EventObject.UP,
   		fn: function(e){
   		if(!this.haveProducts) return false;
   		this.customSelectLastRow();
   		this.formFocus.defer(50,this);
		}},
		{
   		key: Ext.EventObject.DOWN,
   		fn: function(e){
   		if(!this.haveProducts) return false;
   		this.customSelectFirstRow();
		this.formFocus.defer(50,this);
		}},
		{key: Ext.EventObject.ENTER,fn: this.enterFunction},
		{
	   		key: Ext.EventObject.PAGE_DOWN,
	   		fn:this.onPayment
   		},
		{
   		key: Ext.EventObject.PAGE_UP,
   		fn: function(e){
		   		if(!e.shiftKey)this.onSelShippingGuide();
			}
		},
		{
   		key: Ext.EventObject.ESC,
   		fn: this.escFunction},
		{
   		key: Ext.EventObject.END,
   		fn: function(e){
		   		if(!e.shiftKey){
					this.onOffers();
				}else{
					var key = this.fp.form.findField("key").getValue();
					this.fp.form.findField("key").setValue(key+"#");
				}
			}
		},
		{
	   		key: Ext.EventObject.HOME,
	   		fn: this.onDiscount,
	   		scope:this
		},
		{
		    key: Ext.EventObject.INSERT, // or Ext.EventObject.ENTER
		    fn: this.updateNumber,
		    scope: this
		},{key: Ext.EventObject.F11,
		    fn: this.updateColor
		},{key: Ext.EventObject.F12,
		    fn: this.updateSizeType
		},{key: Ext.EventObject.F1,
		    fn: this.onPaymentMoney
		},{key: Ext.EventObject.F2,
		    fn:this.onPaymentCheck
		},{key: Ext.EventObject.F3,
		 	fn: this.createCreditCardTypeWin
		},{key: Ext.EventObject.F12,
		 	fn: this.onPreferential
		},{
			key: Ext.EventObject.F3,
			ctrl:true,
		    fn: function(){
		    	this.setAction('reserve');
	        	Disco.Ext.Util.msg("提示","请输入备用金额");
		    }
		},{
			key: Ext.EventObject.F5,
			ctrl:true,
		    fn: this.onPay
		},{
			key: Ext.EventObject.F9,
			ctrl:true,
		    fn: this.doChangeCasher,
		    scope:this
		},{key: Ext.EventObject.F4,
		    fn: function(k,e){
		    	  if(!this.payment || e.ctrlKey) return false;
		          Ext.each(this.paymentTypes,function(o){
		            if(o.name=="购物券"){
			          this.paymentTypeTitle = "购物券";
			          Disco.Ext.Util.msg("提示","购物券支付!");
			          this.paymentTypeId = o.id;
		            }
		          },this)
		    }
		},{key: Ext.EventObject.DELETE,
		    //fn: this.updateSalePrice,
		    fn: function(){return false}
		},{key: Ext.EventObject.F5,
		    fn: function(k,e){
		    	if(!this.payment || e.ctrlKey) return false;
		          Ext.each(this.paymentTypes,function(o){
		            if(o.name=="会员卡"){
			          this.paymentTypeTitle = "会员卡";
			          Disco.Ext.Util.msg("提示","会员卡支付!");
			          this.paymentTypeId = o.id;
		            }
		          },this);
		    }
		},{key: Ext.EventObject.F6,
		    fn: this.hangBill
		},{key: Ext.EventObject.F7,
		    fn: function(){
		    //window.location.href="Lanyowebprinter://webprint/65536"																	    
//																	    //window.external.LanyoWebPrint("196608");
		    this.formFocus();
		    }
		},{
			key: Ext.EventObject.F9,
		    fn: this.comeback()
		},{
			key: Ext.EventObject.F10,
			scope:this,
		    fn: function(k,e){
		    	if(!e.ctrlKey){
		    		this.onNoPrint();
		    	}else{
		    		Disco.Ext.Util.msg("提示","请输入会员卡号!");
		    		this.setAction('onMember');
		    	}
		    }
		},{
	        key: Ext.EventObject.B, //补货快捷键
	        ctrl:true,
	        fn: function(){
	        	//this.patchOrder = true;
	        	this.setAction('patchOrder');
	        	this.fp.form.findField("remark2").setValue("该单为补单");
	        	Disco.Ext.Util.msg("提示","该单为补单");
	        }
	    },{
	    	key: Ext.EventObject.F8,
	        fn: function(k,e){
	        	if(e.ctrlKey && this.giftsPattern){
	        		this.giftsSelectWin();
	        	}else{
	        		if(this.giftsPattern){
	        			this.giftsPattern=false;
	        			this.fp.form.findField("retailType").setValue("销售模式");
	        		}else{
	        			this.giftsPattern=true;
	        			this.fp.form.findField("retailType").setValue("赠品模式");
	        		}
	        	}
	        }
	    }
];
		for(var i=0;i<keys.length;i++)Ext.apply(keys[i],{ stopEvent : true,scope: this});														
		var map = new Ext.KeyMap(textfield.el,keys);
    },
    giftsSelectData:function(){
		var records = this.giftsProductWin.grid.getSelections();
		if(records && records.length){
			var data = Ext.apply({},records[0].data);
			data.id = data.product;
			this.selectProductDataHandler(data);
			this.giftsProductWin.hide();
		}else{
			Disco.Ext.Msg.alert('请选择赠品!',false,function(){
				 this.giftsProductWin.grid.getSelectionModel().selectRow(0);
			},this);
		}
	},
    giftsSelectWin:function(){
    	if(!this.giftsProductWin){
    		var store = new Ext.data.JsonStore({
				url :"productGifts.ejf?cmd=list",
				totalProperty:'rowCount',
				root:'result',
				fields:["id", "sn", "title","gifts","brand","stockNO","num","dir","product","dirTitle","salePrice","attributeType","style","theYear","season","encaped"],
				listeners:{
					load:function(){
						this.giftsProductWin.grid.getSelectionModel().selectRow(0);
						this.giftsProductWin.grid.getView().focusRow(0);
					},
					scope:this
				}
			});
    		this.giftsProductWin = new Ext.Window({
    			title:'赠品列表',
    			closeAction:'hide',
    			hideBorders:true,
    			layout:'fit',
    			modal:true,
    			resizable:false,
    			width:500,
    			height:300,
    			items:{
    				xtype:'grid',sm:new Ext.grid.RowSelectionModel({singleSelect:true}),
    				loadMask:true,
    				store:store,
    				columns:[new Ext.grid.RowNumberer({header:"序号",width:40}),{header: "ID", hidden:true,width: 100, dataIndex:"id"},
    					{header: "货号", sortable:true,width: 100, dataIndex:"sn"},
						{sortable:true,hidden:true,width: 120, dataIndex:"product"},
						{header: "货品编码", sortable:true,width: 120, dataIndex:"stockNO"},
						{header: "货品名称", sortable:true,width: 120, dataIndex:"title"},
						{header: "库存量", sortable:true,width: 80, dataIndex:"num"}
				    ],
				    keys:[{
				    	key:13,
				    	fn:this.giftsSelectData,
				    	scope:this
				    }]
    			},
    			buttonAlign:'center',
    			listeners:{
    				scope:this,
    				hide:this.formFocus,
    				show:function(){
    					this.giftsProductWin.grid.getStore().removeAll();
    					this.giftsProductWin.grid.getStore().load();
    				}
    			},
    			buttons:[{text:'确定',handler:this.giftsSelectData,scope:this},
    					 {text:'取消',handler:function(){
							this.giftsProductWin.hide();    					 	
    					 },scope:this}
    					]
    		});
    		this.giftsProductWin.grid =  this.giftsProductWin.findByType('grid')[0];
    	}
    	this.giftsProductWin.show();
    },
    hangBill:function(){
    	if(!this.haveProducts && this.hangBills && this.hangBills.length){
    		if(!this.hangBillWin){
    		this.hangBillWin=new HangBillSelectWin();
    		this.hangBillWin.on("select",function(r, win,e){
    			this.editGrid.store.add(r.items);
    			this.hangBills.removeKey(r.time);
    			//设置this.haveProducts\this.append及导购员的值
    			this.haveProducts=true;
    			this.append=true;
    			this.tempData.add("firstShippingGuide",r.items[0].get("shoppingGuide"));//缓存起来
    			this.countAll(true);
    		},this);
    		this.hangBillWin.on("hide",this.formFocus,this);
    		}
    		this.hangBillWin.show();
    		this.hangBillWin.list.grid.store.loadData({result:this.hangBills.items});
    	}
    	else {
    	Ext.Msg.confirm("挂单提示!","确认要挂起当前单据,录入另外一张订单吗?",function(btn){
    		if(btn=="yes"){
		    	if(!this.hangBills)this.hangBills=new Ext.util.MixedCollection();
		    	//time表示挂单时间,num表示总数量,product表示代表产品,totalAmount表示总金额,items:表示record数据
		    	var records=this.editGrid.store.data.items;
		    	
		    	var nums =0;
		    	Ext.each(records,function(record){
		    		nums += record.get("num");
		    	},this);
		    	
		    	var totalAmounts = 0.00;
		    	Ext.each(records,function(record){
		    		totalAmounts += record.get("totalAmount");
		    	},this);
		    	var obj={time:new Date(),num:nums,totalAmount:totalAmounts,product:records[0].get("product"),items:records};
		    	this.hangBills.add(obj.time,obj);
				this.create();
    		}
    		else {
    			this.formFocus();
    		}
    	},this)
    	}
	},
    loadProductStock:function(grid,row,col){},
    
    createWin:function(callback,autoClose){
    	return this.initWin(Ext.getBody().getViewSize().width,Ext.getBody().getViewSize().height,"Pos收银",callback,autoClose);
    },
    storeMapping:["id","sn","cashier","paymentType","vdate","receive","total","charge"],
	initComponent : function(){
    	this.viewWin={width:Ext.getBody().dom.offsetWidth-20,height:Ext.getBody().dom.offsetHeight-20,title:"零售详细"};
		var chkM=new Ext.grid.CheckboxSelectionModel();
		this.gridConfig={sm:chkM};
	    this.cm=new Ext.grid.ColumnModel([
	    	chkM,
	    	{header: "流水编号",sortable:true,width: 80, locked:true,dataIndex:"sn"},
	    	{header: "收银员", sortable:true,width: 90,locked:true, dataIndex:"cashier",renderer:this.objectRender("trueName")},
	    	//{header:"支付方式",sortable:true,width: 100,dataIndex:"paymentType",renderer:this.objectRender("name")},
	    	{header:"金额", sortable:true,width: 80,dataIndex:"total"},
	    	{header:"日期", sortable:true,width: 120,dataIndex:"vdate",renderer:this.dateRender()}
	        ]);
	    if(Global.WearEditStoreMapping ){
			Ext.apply(PosPanel.prototype,Global.WearEditStoreMapping);
		}else{
			var response=Ext.lib.Ajax.syncRequest("POST", "stockOutcome.ejf",{cmd:"getGroupFieldMapping"});
			var map=Ext.decode(response.conn.responseText);
			Ext.apply(PosPanel.prototype,map);
		}		
		if(this.editStoreMapping.indexOf("shoppingGuide")<0){
			this.editStoreMapping=this.editStoreMapping.concat(["shoppingGuide","gifts","status","stockNO","totalAmount","sizeType","discount","discountPrice","residualStoreNum"]);
		}
	        
		PosPanel.superclass.initComponent.call(this);
		Ext.Ajax.request({url:"paymentType.ejf",params:{cmd:"list"},callback :function(ops,success,response){
			var map=Ext.decode(response.responseText);
			this.paymentTypes =  map.result;
		},scope:this});
		this.tempData = new Ext.util.MixedCollection(false);
	},
	quickSearch : function() {
		var d1 = this.search_vdate1.getValue() ? this.search_vdate1.getValue().format("Y-m-d") : "";
		var d2 = this.search_vdate2.getValue() ? this.search_vdate2.getValue().format("Y-m-d") : "";
		this.store.baseParams = Ext.apply({}, {
			vdate1 : d1,
			vdate2 : d2
		},this.baseQueryParameter);
		if("defaultStatus" in this.store.baseParams) delete this.store.baseParams.defaultStatus
		if("advanced" in this.store.baseParams) delete this.store.baseParams.advanced
		this.refresh();
	},
	printRecord:function(){
			var record = this.grid.getSelectionModel().getSelected();
			if (!record) {
				Ext.Msg.alert("提示",
						"请先选择要操作的数据！");
				return false;
			}
			Ext.Ajax.request({url:this.baseUrl,params:{cmd:"print",id:record.get("id")},callback:function(options,success,response){
					Disco.Ext.Util.executeLocalCommand("retailPostPrint",response.responseText);
			}});
		   //window.location.href="Lanyowebprinter://webprint/"+record.get("id");
		},
	afterList:function(){
		this.store.baseParams.defaultStatus=0;
		this.btn_refresh.hide();
		this.searchField.hide();
		this.search_vdate1=new	Ext.form.DateField({fieldLabel:"开始时间",emptyText:"开始时间",width:90,format:"Y-m-d"});
		this.search_vdate2=new	Ext.form.DateField({fieldLabel:"结束时间",emptyText:"结束时间",width:90,format:"Y-m-d"});
	/*	this.btn_remove.hide();
		this.btn_edit.hide();*/
		this.btn_print.setHandler(this.printRecord,this);
		this.grid.on("render",function(){
			this.grid.getTopToolbar().insert(10,["-",this.search_vdate1,this.search_vdate2,"-",{text:"查询",cls : "x-btn-text-icon",icon:"images/icons/page_find.png",handler:this.quickSearch,scope:this},
				{id:"btn_refresh",text : "重置",iconCls : 'f5',handler : function(){
					this.search_vdate1.setValue("");
					this.search_vdate2.setValue("");
				}}
			]);
			//得到服务器
		   /* Ext.TaskMgr.start({
					        run: function(){
					         Ext.Ajax.request({url:this.baseUrl,params:{cmd:"getService"},callback :function(ops,success,response){
								 var map=Ext.decode(response.responseText);
							},scope:this});
					        },
					        interval: 600000,
					        scope:this
					    });	*/
			},this);
		this.create();
	}
});

CasherLoginWindow=Ext.extend(Ext.Window,{
 	title : '收银员登录',		
	width : 265,			
	height : 140,
	collapsible : true,
	closable:false,
	modal:true,
	defaults : {			
		border : false
	},
	buttonAlign : 'center',	
	createFormPanel :function() {
		return new Ext.form.FormPanel({
			bodyStyle : 'padding-top:6px',
			defaultType : 'textfield',
			labelAlign : 'right',
			labelWidth : 55,
			labelPad : 0,
			frame : true,
			defaults : {
				allowBlank : false,
				width : 158,
				selectOnFocus:true
			},
			items : [{name:"user",xtype:"hidden"},{
					cls : 'user',
					name : 'userName',
					fieldLabel : '帐号',
					blankText : '帐号不能为空'
					}, {
					cls : 'key',
					name : 'psw',
					fieldLabel : '密码',
					blankText : '密码不能为空',
					inputType : 'password'
				}]
		});
	},					
	login:function() {
		Ext.Ajax.request({
			waitTitle:"请稍候",
			waitMsg : '正在登录......',
			url : '/dutyInfo.ejf?cmd=casherLogin',
			params:{user:this.fp.findSomeThing("user").getValue(),psw:this.fp.findSomeThing("psw").getValue()},
			scope:this,
			success:function(req){
				try{
					var ret=Ext.decode(req.responseText);
					if(ret && ret.errors){
						Ext.MessageBox.alert('警告', ret.errors.msg,function(){
							this.fp.findSomeThing("psw").focus('200',true);
						},this);
					}else{
						Disco.Ext.Util.executeLocalCommand("printhandover",req.responseText);
						window.location.href="manage.ejf";
					}
				}catch(err){
					Disco.Ext.Util.executeLocalCommand("printhandover",req.responseText);
					window.location.href="manage.ejf";
				}
			}
		});
		/**
		this.fp.form.submit({
				waitTitle:"请稍候",
				waitMsg : '正在登录......',
				url : '/dutyInfo.ejf?cmd=casherLogin',
				success : function(form, action) {
					Disco.Ext.Util.executeLocalCommand("dutyInfoPrint",action.response.responseText,function(){
						window.location.href="manage.ejf";
					});
				},
				failure : function(form, action) {
					if (action.failureType == Ext.form.Action.SERVER_INVALID)
						Ext.MessageBox.alert('警告', action.result.errors.msg);
					form.findField("psw").focus(true);
				},
				scope:this
			});
		*/
	},
	initComponent : function(){
		this.keys={
			key: Ext.EventObject.ENTER,
		    fn: this.login,
		    scope: this};
        CasherLoginWindow.superclass.initComponent.call(this);       
        this.fp=this.createFormPanel();
        this.add(this.fp);
        this.addButton('登陆',this.login,this);
        this.addButton('取消',function(){
        	Ext.Msg.confirm("提示","确认要取消交班吗？",function(btn){
	        	if(btn=="yes"){
	        		this.hide();
	        	}
        	},this)
        },this);
	 } 	
 });