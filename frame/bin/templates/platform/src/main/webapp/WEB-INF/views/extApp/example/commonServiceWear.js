
//图片的自适应
function drawImage(ImgD, FitWidth, FitHeight) {
	var image = new Image();
	image.src = ImgD.src;
	if (image.width > 0 && image.height > 0) {
		if (image.width / image.height >= FitWidth / FitHeight) {
			if (image.width > FitWidth) {
				ImgD.width = FitWidth;
				ImgD.height = (image.height * FitWidth) / image.width;
			} else {
				ImgD.width = image.width;
				ImgD.height = image.height;
			}
		} else {
			if (image.height > FitHeight) {
				ImgD.height = FitHeight;
				ImgD.width = (image.width * FitHeight) / image.height;
			} else {
				ImgD.width = image.width;
				ImgD.height = image.height;
			}
		}
	}
}

if (!Global.theYear) {
	Ext.Ajax.request({
		url:"product.ejf",
		params:{cmd:"loadYears"},
		scope:this,
		success:function(req){
			var ret=Ext.decode(req.responseText);
			Global.theYear=[];
			if(ret && ret.length){
				for(var i=0;i<ret.length;i++)
					Global.theYear.push([ret[i],ret[i]]);
			}
		}
	});
}

Disco.Ext.Util.msg = (function(){
	var msgCt;
	 function createBox(t, s){
        return ['<div class="msg">',
                '<div class="x-box-tl"><div class="x-box-tr"><div class="x-box-tc"></div></div></div>',
                '<div class="x-box-ml"><div class="x-box-mr"><div class="x-box-mc"><h3>',t, '</h3><div style="font-size:25px;">', s, '</div></div></div></div>',
                '<div class="x-box-bl"><div class="x-box-br"><div class="x-box-bc"></div></div></div>',
                '</div>'].join('');
    }
    return 	  function(title, format,fadeout,timeout,align){
		            if(!msgCt){
		                msgCt = Ext.DomHelper.insertFirst(document.body, {id:'msg-div',style:'width:auto;'}, true);
		            }
		            msgCt.alignTo(document, align?align:'c-c');
		            var s = String.format.apply(String, Array.prototype.slice.call(arguments, 1));
		            var m = Ext.DomHelper.append(msgCt, {html:createBox(title, s)}, true);
		            if(!fadeout)
		            	m.slideIn('t').pause(timeout?timeout:4).ghost("t", {remove:true});
		        }
})();


/**
 * 服装业ERP销售出库单相关单据
 * @class BaseStockBillPanel
 * @extends BaseStockOutcomeBillPanel
 */
 ConfigConst.WEAR = {
 	productStatusRender:function(v){
    	if(v==0) return " ";
    	if(v==1) return "停产";
    	if(v==2) return "作废";
	},
 	product : {
		xtype : "productcombo",
		name : "product",
		hiddenName : "product",
		displayField : "fullName",
		valueField : "id",
		lazyRender : true,
		triggerAction : "all",
		typeAhead : true,
		editable : true,
		mode:"local",
	//	enableKeyEvents : true,
		minChars : 2,
	//	queryDelay:1000,
		queryParam : "searchKey",
		store : new Ext.data.JsonStore({
					id : "id",
					url : "product.ejf?cmd=autocompleteList",
					root : "result",
					totalProperty : "rowCount",
					mode:"local",
					remoteSort : false,
					baseParams : {
					},
					fields : ["id","attributeType","fullName","title","sn","keyword","simiProducts","brand","unit","spec","model","types","buyPrice","dir",{name:"dirId",mapping:"dir"},"reCost","salePrice","ratePrice","marketPrice","tradePrice","bottomPrice","stockWarnNum","leastOrderNum","encapNum","size","updateTime","provideType","virtualStock","storeNum","intro","content","pj","star","auditing","inputTime","readTimes","status","vdate","propertys","color","colorSn","stockMinNum","stockNO","stockMaxNum","encapUnit","encapSn","encaped","buyPeriod","colors","sizes","sizeGroup"]
				}),
		fieldLabel : '业务员'
	}	
 }
	
ProductComboBox= Ext.extend(Disco.Ext.SmartCombox, {
	choiceValue : Ext.emptyFn(),
	selectSingle:true,
	initLookUpWin:function(){
		if(!this.lookupWin){
				this.lookupWin = new ProductSelectWin({
					selectSingle : this.selectSingle,
					autoLoad:this.autoLoad
				});
				this.lookupWin.on("select", function(data) {
					if (data && data.length) {
						this.setValue({
							id : data[0].id,
							title : data[0][this.displayField
									? this.displayField
									: "title"]
						});
						if (this.choiceValue){
							this.choiceValue(data[0]);
						}
					}
				}, this);
			}
	},
	choiceProvider : function(data) {
		this.initLookUpWin();
		if(this.getValue() && this.getValue().text)
		this.lookupWin.keyword = this.getValue().text;  
		this.lookupWin.show();
		if(data && data.length){
			//this.lookupWin.baseQueryParams=Ext.apply({},this.store.baseParams);
			this.lookupWin.list.store.loadData({result:data,rowCount:data.length});
			Ext.apply(this.lookupWin.list.store.baseParams,this.store.baseParams,{});
		}
	},
	searchProductWin:function(){
		this.initLookUpWin();
		Ext.apply(this.lookupWin.list.store.baseParams,this.store.baseParams);
		this.lookupWin.list.store.baseParams['sn'] = Ext.value(this.getValue().text,'');
		this.lookupWin.list.store.baseParams['pageSize'] = 50;
		this.lookupWin.show();
		this.lookupWin.list.store.removeAll();
		this.lookupWin.list.store.reload();
	},
	selectProvider : function(c, r, o) {	
		if (this.choiceValue){
			this.choiceValue(r.data);
		}
	},
	autoSelectBySn:function(c,e){
		if (e.getKey() == Ext.EventObject.ENTER) {
			var t=c.el.dom.value;
			if(this.store.baseParams)
				Ext.apply(this.store.baseParams,{sn:t});
			Ext.Ajax.request({
				url : "product.ejf?cmd=loadBySn",
				params : this.store.baseParams,
				success : function(response) {
					var obj=Ext.decode(response.responseText);
					if(obj){
						this.selectProvider(this,{data:obj},-1);
					}else {
						this.choiceProvider();
					}
				},
				scope : this
			});
			//}
		}
	}, 
	triggerClass:'x-form-search-trigger',
	initTrigger:function(){
		ProductComboBox.superclass.initTrigger.call(this);
		this.trigger.on("click", this.searchProductWin, this, {preventDefault : true});
	},
	onRender : function(ct, position) {
		ProductComboBox.superclass.onRender.call(this, ct, position);
		var label = this.el.findParent('.x-form-element', 5, true) || this.el.findParent('.x-form-field-wrap', 5, true);
		this.on("select", this.selectProvider, this);
	    this.on("keypress", this.autoSelectBySn, this); 
		this.store.on("beforeload",function(store,ops){
			store.baseParams=store.baseParams||{};
			store.baseParams.client=Global.CLIENT.id;
		});
	}
})
Ext.reg('productcombo', ProductComboBox);	
	
ProductGridList = Ext.extend(BaseGridList, {
		border : false,
		gridForceFit : true,
		pagingToolbar:true,
		pageSize:50,
		selectSingle:false,
		url : "product.ejf?cmd=loadBySn",
		refresh : function() {
			var find=function(grid,name){
				return grid.getTopToolbar().items.find(function(c){
	            var n1=c.name;
	            if(n1==name)return true;
	        })};
			if(this.store.baseParams){
				this.el.mask("正在加载...","x-mask-loading");
				Ext.apply(this.grid.store.baseParams,{
					all:false,
					dir:find(this.grid,"dirId").getValue(),
					theYear:find(this.grid,"theYear").getValue(),
					season:find(this.grid,"season").getValue(),
					style:find(this.grid,"style").getValue(),
					brand:find(this.grid,"brand").getValue(),
					exact:find(this.grid,"exact").getValue(),
					haveStore:find(this.grid,"haveStore").getValue()
				});
				if(find(this.grid,"sn").getValue()){
					this.grid.store.baseParams.sn=find(this.grid,"sn").getValue();
				}
				Ext.Ajax.request({
					url : "product.ejf?cmd=loadBySn",
					scope:this,
					params:this.store.baseParams,
					success:function(req){
						var ret=Ext.decode(req.responseText);
						if(ret){
							this.grid.store.loadData(ret);
						}else{
							this.grid.store.removeAll();
						}
						this.el.unmask();
					}
				});
			}
		},
		reset : function() {
			this.btn_sn.reset();
			this.btn_title.reset();
			this.refresh();
		},
		createProduct:function(){
			Disco.Ext.Util.addObject("ProductManagePanel",this.refresh.createDelegate(this),"/stock/ProductManagePanel.js","fckeditor/fckeditor.js",function(win){
				if(this.currentDir)
				win.getComponent(0).findSomeThing("dirId").setOriginalValue(this.currentDir);
			}.createDelegate(this));
		},
		storeMapping : ["id","title","sn","attributeType","storeNum","theYear","brand","unit","types","buyPrice","dir","salePrice","tradePrice","encapNum","provideType","storeNum","color","colorSn","stockNO","encapUnit","encapSn","encaped","colors","sizes","sizeGroup","season","style","company"],
		initComponent : function() {
			/*this.keys = {
				key : Ext.EventObject.ENTER,
				fn : this.refresh,
				stopEvent:true,
				scope : this
			};*/
			var gridConfig={border:false,tbar:[
			{text:"货号"},{xtype:"textfield",name:"sn",width:60},
				{text:"品牌"},
                Ext.apply({},{editable:true,
									minChars:2,
									listWidth:150,
									width:60,
									allowBlank:true,
									listeners:{}},Disco.Ext.Util.buildRemoteCombox('brand', '品牌',
						"brand.ejf?cmd=listSelect", ["id", "name"],
						"name", "id", true)), 
				{text:"分类"},{
					xtype : "treecombo",
					fieldLabel : "分类",
					width:60,
					name : "dirId",
					hiddenName : "dirId",
					displayField : "title",
					listWidth:150,
					tree : new Ext.tree.TreePanel({
								root : new Ext.tree.AsyncTreeNode({
											id : "root",
											text : "产品分类",
											expanded : true,
											loader : Global.productDirLoader
										})
							})
				}, {text:"年份"},{
					xtype:"yearfield",
					width:60,
					fieldLabel : '年份',
					name : 'theYear',
					defaultValue:false
				}, {text:"季节"},{
					xtype : "treecombo",
					fieldLabel : "季节",
					name : "season",
					hiddenName : "season",
					displayField : "title",
					width:60,
					listWidth:150,
					tree : new Ext.tree.TreePanel({
								autoScroll:true,
								root : new Ext.tree.AsyncTreeNode({
											id : "root",
											text : "季节",
											expanded : true,
											loader : Global.seasonLoader
										})
							})
				}, {text:"款型"},{
					xtype : "treecombo",
					fieldLabel : "款型",
					name : "style",
					hiddenName : "style",
					width:60,
					listWidth:150,
					displayField : "title",
					tree : new Ext.tree.TreePanel({autoScroll:true,
								root : new Ext.tree.AsyncTreeNode({
											id : "root",
											text : "款型",
											expanded : true,
											loader : Global.styleTypeLoader
										})
							})
				},{text:"精确查询"},{xtype:"checkbox",name:"exact",inputValue:"true"},"-",{text:"有库存"},{xtype:"checkbox",name:"haveStore",inputValue:"true"},"->",{text:"查询",cls : "x-btn-text-icon",icon:"images/icons/page_find.png",scope:this,handler:this.refresh}
			]},chkM=null;
			if(this.selectSingle){
				gridConfig.sm=new Ext.grid.RowSelectionModel({singleSelect:true});
			}
			else{
				chkM=new Ext.grid.CheckboxSelectionModel();
				gridConfig.sm=chkM;
			}
			this.gridConfig=Ext.apply({},gridConfig);
			this.cm = new Ext.grid.ColumnModel([chkM?chkM:new Ext.grid.RowNumberer({header:"序号",width:35}),{
						header : "货号",
						sortable : true,
						width : 100,
						dataIndex : "sn"
					},{
						header : "品牌",
						sortable : true,
						width : 100,
						dataIndex : "brand",
						renderer : this.objectRender("name")
					},{
						header : "款型",
						sortable : true,
						width : 80,
						dataIndex : "style",
						renderer : Disco.Ext.Util.objectRender("title")
					},{
						header : "年份",
						sortable : true,
						width : 60,
						dataIndex : "theYear"
					},{
						header : "季节",
						sortable : true,
						width : 60,
						dataIndex : "season",
						renderer:Disco.Ext.Util.objectRender("title")
					},
					
					{
						header : "零售价",
						sortable : true,
						width : 60,
						dataIndex : "salePrice"
					},{
						header : "货品编码",
						sortable : true,
						width : 90,
						dataIndex : "stockNO"
					},{
						header : "库存",
						sortable : true,
						width : 50,
						dataIndex : "storeNum"
					},
					
					{
						header : "备注",
						sortable : true,
						width : 140,
						dataIndex : "remark"
					}]);
			this.btn_brand = new Disco.Ext.SmartCombox(Ext.apply({}, {
						width:100,
						listeners : {
							"select" : this.refresh,
							scope : this
						}
					}, ConfigConst.CRM.brand));
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
			/*this.tbar = [ "关键字",
					this.btn_sn, "型号:", this.btn_model, "名称:",
					this.btn_title, {
						text : "查询",
						handler : this.refresh,
						scope : this,
		cls:"x-btn-text-icon",
		icon:"images/icon-png/search.png"
					}, {
						text : "重置",
						handler : this.reset,
						scope : this,
						cls:"x-btn-text-icon",
		icon:"images/icons/arrow_undo.png"
					},Ext.apply({},{text:"新建商品",handler:this.createProduct,scope:this},ConfigConst.buttons.addChild)];*/
		ProductGridList.superclass.initComponent.call(this);
		this.store.on("load", function(s, rs) {
			if (rs && rs.length > 0) {
				(function(){
					this.grid.getSelectionModel().selectFirstRow();
					this.grid.getView().focusRow(0);
				}.createDelegate(this)).defer(500);
			}
		}, this);
		this.store.on("beforeload",function(store,ops){
			store.baseParams=store.baseParams||{};
		},this);
		}
});

ClientComboBox= Ext.extend(Disco.Ext.SmartCombox, {
	baseUrl:"client.ejf",
	choiceValue : Ext.emptyFn,
	selectSingle:true,
	disableChoice:false,
	mode:'local',
	loadBySn:'autocompleteList',
	setValue:function(v){
		if(v!="" && Ext.type(v)!="object") {
			if(this.store.getById(v))
				v=this.store.getById(v).data;
		}
		ClientComboBox.superclass.setValue.call(this, v);
		if(v&&typeof v=="object"){
			Global.setClient(v);
		}else {
			Global.setClient({id:v,fullName:""});
		}
	},
	clearLastValue:function(){
		this.lastValue=null;		
	},
	choiceProvider : function() {
		if(!this.lookupWin){
			this.lookupWin = new ClientSelectWin({
				baseUrl:(this.baseUrl+"?cmd="+this.loadBySn),
				selectSingle : this.selectSingle
			});
			this.lookupWin.on("select", function(data) {
				if (data && data.length) {
					this.setValue({
						id : data[0].id,
						fullName : data[0][this.displayField
								? this.displayField
								: "fullName"]
					});
					if (this.choiceValue)
						this.choiceValue(data[0]);
				}
			}, this);
		}
		this.lookupWin.show();
	},
	selectProvider : function(c, r, o) {
		if (this.choiceValue && this.choiceValue!=Ext.emptyFn){
			if(r.data)
				this.choiceValue(r.data);
			else
				this.choiceValue(r);
		}
	},
	autoSelectBySn:function(c,e){
		if(c.el.dom.value&&c.getValue()){
			
		}else if((e.getKey() == Ext.EventObject.ENTER||e.getKey() == Ext.EventObject.DOWN) && !this.isExpanded()) {
				var t=c.el.dom.value;
			//	var r=this.store.find("fullName",t);
				//if(r>=0){
					//this.selectProvider(this,this.store.getAt(r),r);
			//	}else {//根据编码查询数据
					Ext.Ajax.request({
						url : this.baseUrl+"?cmd="+this.loadBySn,
						params : {searchKey : t},
						success : function(response,opt) {
							this.lastValue={text:this.lastSelectionText,id:this.hiddenField.value};
							this.store.baseParams=Ext.apply({},opt.params);
							var obj=Ext.decode(response.responseText);
							this.store.removeAll();
							if(obj && obj.result){
								this.store.loadData({
									result:obj.result,
									rowCount:obj.rowCount
								});
							}
							if(this.store.getCount()==1){
								var data = this.store.getAt(0);
								this.select(0,true);
								this.expand();
							}else if(this.store.getCount()>1){
								this.select(0,true);
								this.expand();
							}else if(this.store.getCount()==0){
								Ext.Msg.alert("提示",(this.fieldLabel||"客户")+"编号不正确,请重新输入!",function(){
								this.focus(true,100);
								},this);
							}
						},
						scope : this
					});
			  }else if(e.getKey()==Ext.EventObject.PAGEDOWN){//使用PageDown激活窗口选择
				this.choiceProvider();
			}
		//e.stopEvent();
	},
	onRender : function(ct, position) {
		ClientComboBox.superclass.onRender.call(this, ct, position);
		this.trigger.on("click", function(e){
			e.keyCode = Ext.EventObject.ENTER;
			this.autoSelectBySn(this,e);
		}, this, {preventDefault : true});
		this.on("select", this.selectProvider, this);
		this.on("specialkey", this.autoSelectBySn, this);
		this.on("keypress",function(c,e){
    		if(!e.isSpecialKey())c.value=null;
    	},this)
	}
})
Ext.reg('clientcombo', ClientComboBox);	

ClientGridList = Ext.extend(BaseGridList, {
	border : false,
	gridForceFit : false,
	selectSingle:false,
	columnLock:true,
	storeMapping : ["id","sn","name"],
	initComponent : function() {
		var gridConfig={border:false},chkM=null;
		if(this.selectSingle){
			gridConfig.sm=new Ext.grid.RowSelectionModel({singleSelect:true});
		}else{
			this.columnLock=false;
			chkM=new Ext.grid.CheckboxSelectionModel({chkLocked:true});
			gridConfig.sm=chkM;
		}
		this.gridConfig=Ext.apply({},gridConfig);
		this.cm = new Ext.grid.ColumnModel([chkM?chkM:new Ext.grid.RowNumberer({header:"序号",width:35,locked:true}),
		{
			header : "客户名称",
			sortable : true,
			width : 150,
			locked : true,
			dataIndex : "name"
		},{
			header : "编号",
			sortable : true,
			width : 100,
			dataIndex : "sn"
		}]);
		ClientGridList.superclass.initComponent.call(this);
		this.store.on("load", function(s, rs) {
			if(s.getCount())this.grid.getView().focusRow(0);
		}, this,{delay:333});
	}
});

ClientSelectWin = Ext.extend(Ext.Window, {
	title : "选择客户",
	width : 360,
	height : 300,
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
		var records = this.list.grid.getSelectionModel().getSelections();
		if (!records || records.length < 1) {
			Ext.Msg.alert("$!{lang.get('Prompt')}",
					"$!{lang.get('Select first')}");
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
			},{
				text : "取消",
				handler : function() {
					this.hide();
				},
				scope : this
			}];
		ClientSelectWin.superclass.initComponent.call(this);
		this.list = new ClientGridList({url:this.baseUrl,selectSingle:this.selectSingle,region:"center",menus:[{text:"选定客户",handler:this.choice,scope:this,cls:"x-btn-text-icon",icon:"images/icons/accept.png"}]});
		this.list.grid.on("rowdblclick", this.choice, this);
		this.add(this.list);
		this.addEvents("select");
	}
});

BaseAccountChartPanel=Ext.extend(Disco.Ext.CrudListPanel,{
	id:id,
	baseUrl:"chartHelper.ejf",
    storeMapping:this.storeMapping,
    autoLoad:false,
    edit:function(){},
    view:function(){},
    summaryGrid:true,
    moneyRender:Disco.Ext.Util.moneyRender,
	initComponent : function(){
	    this.cm=this.getColumnModel();
	    this.gridConfig={plugins:[new Ext.ux.grid.GridSummary()]};    
		BaseAccountChartPanel.superclass.initComponent.call(this);
	},
	setSearchData:function(data){
		this.searchData=data;
		if(this.quickSearch)this.quickSearch();
	},
	printList:function(){
		if(this.setPrintParams){
		  this.setPrintParams();	
		}
		var s = Ext.urlEncode(this.printParams);
		var win=new Ext.Window({title:"打印窗口",html:"<iframe width='100%' frameborder='no' style='background:#FFF' border=0 height='100%' src ='"+this.baseUrl+"?"+"cmd="+this.listCmd+"&print=true&"+s+"' >"});
		win.show();
		win.fitContainer();
		win.center();
	},
	list:function(){
		this.initComponent();
		var url=this.baseUrl + (this.listCmd?('?cmd='+this.listCmd):"list");
		if(!this.store){
			this.store = new Ext.data.JsonStore({
				id : "id",
				url : url,
				root : "result",
				totalProperty : "rowCount",
				autoLoad:this.autoLoad,
				remoteSort : true,
				fields : this.storeMapping
			});
			this.store.baseParams=this.baseQueryParameter;
			this.store.paramNames.sort = "orderBy";
			this.store.paramNames.dir = "orderType";
		}
		this.search_client=new ClientComboBox(Ext.apply({},{width:130,store : new Ext.data.JsonStore({
				autoLoad:true,
				id : Ext.id(),
				url : "distributor.ejf?cmd=autocompleteList&pageSize=15",
				root : "result",
				baseParams:{distributorType:2},
				totalProperty : "rowCount",
				remoteSort : true,
				fields : ["id", "fullName", "name", "tel", "address",
						"fax", "linkMan","accountBalance","qualityAssure","other1"]
		})},ConfigConst.CRM.distributor));
		this.btn_refresh = new Ext.Toolbar.Button({
			id:"btn_refresh",
			text : "刷新",
			iconCls : 'f5',
			handler : this.quickSearch,
			scope : this
		});
		var buttons = ['->'];
			
		var viewConfig = Ext.apply({
					forceFit : this.gridForceFit
				}, this.gridViewConfig);
		var gridConfig = Ext.apply(this.gridConfig, {
					store : this.store,
					stripeRows : true,
					trackMouseOver : false,
					loadMask : true,
					viewConfig : viewConfig,
					tbar:buttons,
					border : false,
					bbar : this.pagingToolbar ? new Ext.PagingToolbar({
						pageSize : this.pageSize,
						store : this.store,
						displayInfo : true
					}) : null
				});
		if(this.columns)gridConfig.columns=this.columns;
		else if(this.cm)gridConfig.cm=this.cm;
		this.grid=new Ext.grid.GridPanel(gridConfig);
		
		if(this.gridView)this.grid.view=this.gridView;
		
		this.grid.colModel.defaultSortable = true;// 设置表格默认排序
		this.grid.on("dblclick",this.gridDbclick,this);
		
		this.panel=new Ext.Panel({
			id:this.id,
			title:this.title,				
			closable :this.closable,
			autoScroll : this.autoScroll,
			layout : this.layout,
			border : this.border,
			listeners:{close:function(){delete this.panel;},scope:this}
			});
		this.panel.add(this.grid);
		this.panel.service=this;
		this.afterList();
		return this.panel;
	},
	afterList:function(){
		this.grid.on("render",function(){
			this.grid.getTopToolbar().insert(0,this.buttons);
			if(this.searchData)this.setSearchData(this.searchData);
		},this);	
	}     
});	


if (typeof Global === "undefined") {
	Global = {};
}
if (!Global.productDirLoader) {
	Global.productDirLoader = new Disco.Ext.MemoryTreeLoader({
		varName : "Global.PRODUCT_DIR_NODES",
		url : "productDir.ejf?cmd=getProductDirTree&pageSize=-1&treeData=true&all=true",
		listeners : {
			'beforeload' : function(treeLoader, node) {
				treeLoader.baseParams.id = (node.id.indexOf('root') < 0
						? node.id
						: "");
				if (typeof node.attributes.checked !== "undefined") {
					treeLoader.baseParams.checked = false;
				}
			}
		}
	});
}
 


 if (!Global.seasonLoader) {
	Global.seasonLoader = new Disco.Ext.MemoryTreeLoader({
				varName : "Global.SEASON_NODES",
				url : "season.ejf?cmd=getTree&pageSize=-1&treeData=true&all=true",
				listeners : {
					'beforeload' : function(treeLoader, node) {
						treeLoader.baseParams.id = (node.id.indexOf('root') < 0
								? node.id
								: "");
						if (typeof node.attributes.checked !== "undefined") {
							treeLoader.baseParams.checked = false;
						}
					}
				}
			});
}
if (!Global.styleTypeLoader) {
	Global.styleTypeLoader = new Disco.Ext.MemoryTreeLoader({
		varName : "Global.STYLE_TYPE_NODES",
		url : "styleType.ejf?cmd=getTree&pageSize=-1&treeData=true&all=true",
		listeners : {
			'beforeload' : function(treeLoader, node) {
				treeLoader.baseParams.id = (node.id.indexOf('root') < 0
						? node.id
						: "");
				if (typeof node.attributes.checked !== "undefined") {
					treeLoader.baseParams.checked = false;
				}
			}
		}
	});
}
BaseOrderChartPanel = Ext.extend(BaseGridList,{
	autoLoadGridData : false,
	pagingToolbar : false,
	gridBorder:false,
	showMenu : false,
	ableShowPic : false,
	showPrint:true,
	showRefreshCache:true,
	readOnlyNumRender:function(v){
		if(v==0)return "";
		return v;
	},
	createQuery:function(){
		this.metting = new Disco.Ext.SmartCombox(Ext.apply({},{id:"mettingid",width:80,listWidth:120,emptyText:"订货会"},Disco.Ext.Util.buildRemoteCombox("metting","订货会","saleMetting.ejf",["id","title","sn"],"title","id",false)));
		this.distributor = new  ClientComboBox(Ext.apply({}, {
										width : 100,
										listWidth : 200,
										name : "client",
										hiddenName : "client",
										fieldLabel : "订货单位",
										emptyText : "订货单位",
										anchor : '90%',store : new Ext.data.JsonStore({
										autoLoad:true,
										id : "id",
										url : "distributor.ejf?cmd=autocompleteList&pageSize=15",
										root : "result",
										baseParams:{distributorType:2},
										totalProperty : "rowCount",
										remoteSort : true,
										fields : ["id", "fullName", "name", "tel", "address",
											"fax", "linkMan","accountBalance","qualityAssure","other1"]
							})
									}, ConfigConst.CRM.distributor));
		this.brand = new Disco.Ext.SmartCombox(Ext.apply({},{
											editable:true,
											minChars:2,
											width:80,
											emptyText : "品牌",
											listWidth:100,
											typeAhead:true, 
											enableKeyEvents:true,
											listeners:{
												scope:this,
												blur:function(c){
													if(!c.getValue()){
														c.setValue("");
													}
												},
					                    		keypress:function(c,e){
		                        					if(e.getKey()!=13){
		                        						c.value=null;
		                        					}
		                        				}
												}},Disco.Ext.Util.buildRemoteCombox(
												'brand', '品牌',
												"brand.ejf?cmd=listSelect", [
														"id", "name"], "name",
												"id", false)));
		var comeBackComb = [["全部",null],['审核',"1"],['未审核',"0"]];
		this.search_status = new Ext.form.ComboBox(Ext.apply({},{width:80},Disco.Ext.Util.buildCombox("status","",comeBackComb,null)));
		this.supplier = new  ClientComboBox(Ext.apply({},{allowBlank:false,
				loadBySn:'autocompleteList',
					width:100,
					emptyText:"供应商",
                	listeners:{
                		scope:this,
                		blur:function(field){
                    		if(!field.getValue()){
                    			field.setValue("");
                    			field.focus();
                    		}
                		}.createDelegate(this),
                		keypress:function(c,e){
                        		if(e.getKey()!=13)c.value=null;
                        	}},
					 	scope:this},ConfigConst.CRM.supplier));
		this.season = new Disco.Ext.TreeComboField({
											xtype : "treecombo",
											fieldLabel : "季节",
											emptyText : "季节",
											name : "season",
											listWidth:100,
											width:50,
											hiddenName : "season",
											leafOnly : true,
											editable : false,
											displayField : "title",
											allowBlank : false,
											tree : new Ext.tree.TreePanel({
												autoScroll:true,
												root : new Ext.tree.AsyncTreeNode(
														{
															id : "root",
															text : "季节",
															expanded : true,
															loader : Global.seasonLoader
														})
											}),
									 		 listeners:{
									 		 	beforeSetValue:function(combo,val){
									 		 		if(val==0){
									 		 			combo.clearValue();
									 		 			return false;
									 		 		}
									 		 	},
									 		 	scope:this
								 			 }
										});	
	 this.productDir = new Disco.Ext.TreeComboField({
											xtype : "treecombo",
											fieldLabel : "所属分类",
											emptyText : "分类",
											name : "dirId",
											leafOnly : false,
											width:60,
											listWidth:120,
											hiddenName : "dirId",
											displayField : "title",
											allowBlank : true,
											tree : new Ext.tree.TreePanel({
												autoScroll:true,
												root : new Ext.tree.AsyncTreeNode(
														{
															id : "root",
															text : "产品分类",
															expanded : true,
															loader : Global.productDirLoader
														})
											}),
									 		 listeners:{
									 		 	beforeSetValue:function(combo,val){
									 		 		if(val==0){
									 		 			combo.clearValue();
									 		 			return false;
									 		 		}
									 		 	},
									 		 	scope:this
								 		 }		
										});
	this.styleType = new Disco.Ext.TreeComboField({xtype:"treecombo",
											 fieldLabel:"款型",
											 emptyText:"款型",
											 name:"style",
											 leafOnly:false,
											 width:70,
											 listWidth:160,
											 hiddenName:"style",
											 displayField:"title",
											 autoScroll:true,
											 allowBlank:true,
											 tree:new Ext.tree.TreePanel({
											 	autoScroll:true,
								 				root:new Ext.tree.AsyncTreeNode({
								 				id:"root",
								   				text:"款型",   	
								   				expanded:true,
								   				loader:Global.styleTypeLoader
								   				})
								 				})});
	this.year = new Disco.Ext.YearField({name:"theYear",width:60,defaultValue:false,hiddenName:"theYear",emptyText:"年份"});
	this.sn_Search = new Ext.form.TextField({name:"sn",width:100,emptyText:"货号!"});
	},
	//子类需要实现,有什么查询条件,  返回一个包含查询条件的数组
    createButtonToolBar : function(){},
	createToolbar : function(){
		this.createQuery();
		var top_btn = this.createButtonToolBar();
		var fixBtn= ["-",{text:"查询",iconCls : 'advance-search-icon',
				handler : this.quickSearch,scope:this},{
				id:"btn_refresh",
				text : "重置",
				iconCls : 'f5',
				handler : this.resetSearch,
				scope : this
				}];
		if(this.showPrint)fixBtn.push({id:'btn_print',text : "打印",iconCls:"print-icon",handler :this.printList,scope:this});
		if(this.showRefreshCache)fixBtn.push({id:'btn_refreshCache',xtype : 'button',text : '更新缓存',handler:this.refreshCache,scope : this})
		Ext.each(fixBtn,function(o){
			top_btn.push(o);
			},this);
		if(this.createExpandingButton){
			Ext.each(this.createExpandingButton(),function(o){
				top_btn.push(o);
			})
		}
		this.gridTbar = new Ext.Toolbar(top_btn);
	},
	resetSearch : function(){
		this.gridTbar.items.each(function(o){
			if(o.isFormField){
				o.reset();
			}
		},this);
	},
	quickSearch:function(){
		var parsep = this.parseParams();
		var tag = parsep === undefined||parsep === true;
//		if(!(this.forceReload===true)&&this.store.searchKeys==Ext.urlEncode(this.store.baseParams)){
//			return false;
//		}else{
			this.store.proxy.getData().clear();
//		}
		this.store.searchKeys=Ext.urlEncode(this.store.baseParams);
		if(this.forceReload===true){
			this.store.baseParams.forceReload = true;
			this.forceReload = false;
		}
		if(!tag){
			return false;
		}
		this.refresh();
	},
	refreshCache:function(){
		this.forceReload = true;
		this.quickSearch();
	},
	printList:function(){
		var parsep = this.parseParams();
		var tag = parsep === undefined||parsep === true;
		if(!tag){
			return false;
		}
		var s = Ext.urlEncode(this.store.baseParams);
		var win=new Ext.Window({title:"打印窗口",html:"<iframe width='100%' frameborder='no' style='background:#FFF' border=0 height='100%' src ='"+this.url+"&print=true&"+s+"' >"});
		win.show();
		win.fitContainer();
		win.center();
	},
	showPic:function(grid, rowIndex, e) {
        var record = grid.getStore().getAt(rowIndex);  // Get the Record
        if(!record.get("productId")) return false;
		if(!this.chooser) {
			this.chooser = new ImageChooser({
						url : 'product.ejf?cmd=loadPic',
						width : 500,
						height : 400
					});
		 }
		this.chooser.setParams({
					id : record.get("productId")
				});
		this.chooser.show(Ext.fly(e.getRelatedTarget()));
    },
	initComponent:function(){
		this.cm  = this.getColumnModel();
		this.createToolbar();
		this.gridConfig={
			enableHdMenu:false,
			plugins:this.plugins?this.plugins:[new Ext.ux.grid.GridSummary()],
			bbar:this.pagingToolbar ? new  Ext.ux.PagingComBo({rowComboSelect:true,pageSize : this.pageSize,store : this.store,displayInfo :true}) : null
		};
		BaseOrderChartPanel.superclass.initComponent.call(this);
		if(this.ableShowPic){
			this.grid.on("rowdblclick",this.showPic,this);
		}
	}
});

	
BaseStockOutcomeWearBillPanel=Ext.extend(BaseStockOutcomeBillPanel,{
	printData:true,	
	storeMapping:["id","sn","vdate","client","distributor","orders","sendInform","transfer","types","sender","deliver","depot","red","inputTime","inputUser","auditing","auditor","recipient","keeper","manager","seller","sender","totalAmount","closed","status","remark","items","fee1","fee2","fee3","fee4","other1","other2","other3","other4","other5","other6"],
	baseUrl:"stockOutcome.ejf",
	closeSaveWin:false,
	editExtractField:[],
	pitchProductSn:false,
	enableProductSearch:true,
	showGroupHeader:true,
	//手和数量能否填负数
	negative:false,
	formFocus:function(){
		var firstField=this.fp.form.findField("client")||this.fp.form.findField("depot");
		if(firstField)firstField.focus("",100);
	},
	addItemFromBarCode:function(obj){
		if(this.editGrid){
			var flag=false;
			this.editGrid.store.each(function(r){
				//if(r.data.product.id===obj.product.id && r.data.color.id===obj.color.id)
			},this);
		}
	},
	auditing : function() {
		if(this.fp.form.isDirty()){
			Ext.Msg.alert("提示信息","请先保存后再审核！");
			return ;
		}
		if (this.fp.form.findField("id")) {
			this.executeUrl(this.baseUrl + "?cmd=auditing", {
				id : this.fp.form.findField("id").getValue()
			},function(){
				this.closeWin(true);
				if(this.closeSaveWin!==false)this.refresh();
			}).call(this);
		}
	},
	//关掉手及尺码
	initColumnDisplay:function(grid){
        //用来存放已经显示的尺码组的尺码
        this.showingSizeTypeNum={};
        //用来存放已经显示过尺码的产品
        this.displayingSizeTypeNum={};
		var rows=this.groupHeaderCellConfig.rows;
		for(var i=0;i<rows.length;i++){
			grid.getView().setGroupCellDisplay(rows[i].id,false);
		}
		for(var i=0,cm=grid.getColumnModel();i<cm.getColumnCount();i++){
			var index=cm.getDataIndex(i);
			if(index=="encapNum" || index.indexOf("size-")==0){
				cm.setHidden(i,true);
			}
		}
	},
	edit : function() {
            var record = this.grid.getSelectionModel().getSelected();
            if (!record) {
                Ext.Msg.alert("提示",
                        "请先选择要操作的数据！");
                return false;
            }
            if(record.get("auditing")){
                this.view();
                return false;
            }
            return BaseStockOutcomeWearBillPanel.superclass.edit.call(this);
        },
	onEdit:function(ret,data){
		this.cleanEditData();
		BaseStockOutcomeWearBillPanel.superclass.onEdit.call(this,ret,data);
		if(ret){
			this.fp.getTopToolbar().items.get("tb_audit").enable();
			if(this.fp.getTopToolbar().items.get("tb_print"))
				this.fp.getTopToolbar().items.get("tb_print").enable();
		}
	},
	onSave:function(form,action){
		(function(){
			var result = action.result;
			if(result && result.data){
				this.fp.findSomeThing("id").setValue(result.data.id);	
				this.fp.findSomeThing("id").enable();
				this.editGrid.store.removeAll();
				this.editGrid.store.loadData({
					rowCount : result.data.items.length,
					result : result.data.items
				});
                if(this.checkStock){
		            this.existStockCache=new Ext.util.MixedCollection();
		        }
				this.afterLoadItem(result.data.items,this.editGrid);
				Ext.Msg.alert("提示信息","保存成功！",this.formFocus,this);
			}
			this.fp.getTopToolbar().items.get("tb_audit").enable();
			if(this.fp.getTopToolbar().items.get("tb_print"))
				this.fp.getTopToolbar().items.get("tb_print").enable();
			this.fp.form.clearDirty();
		}).createInterceptor(BaseStockOutcomeBillPanel.superclass.onSave,this).call(this);
	},
	save:function(callback,autoClose,ignoreBeforeSave){
    	var o=Disco.Ext.Util.getEditGridData(this.editGrid,"item_","product",function(r){
    		if(r.get("num") && parseInt(r.get("num"))!=0){
    			return true;
    		}
    		return false;
    	});
    	var flag=false;
    	for(var p in o){
    		flag=true;
    		break;
    	}
    	if(!flag){
			Ext.Msg.alert("提示信息","该单据无需保存！");
			return ;
    	}
    	o.red=(this.red==true);
    	this.fp.form.baseParams=o;
    	var types=this.fp.form.findField("types");
    	if(types && types.getValue()==="")types.setValue(this.types);
    	BaseProductBillPanel.superclass.save.call(this,callback,autoClose,ignoreBeforeSave);
    },
    showViewWin : function(autoClose) {
		if (!this.viewPanel) {
			if(this.createViewPanel){
				this.viewPanel = this.createViewPanel();
				if(this.enableProductSearch){
					this.viewPanel.keys={
						key:Ext.EventObject.F,
						ctrlKey:true,
						stopEvent:true,
						fn:function(){this.productSearch(this.viewGrid)},
						scope:this
					};
				}
			}else{
				if(this.fp){
					this.viewPanel = this.fp;
				}else{
					this.viewPanel = this.createForm();
				}
			}
		}
		var win = this.getViewWin(autoClose);
		return win;
	},
	onView:function(win,data){
		if (win) {
			if(Global.iframe!==false){
				win.setWidth(Ext.getBody().dom.offsetWidth-20);
				win.setHeight(Ext.getBody().dom.offsetHeight-20);
				win.center();
				win.doLayout();
				var height=this.viewPanel.el.getBox().height-(this.viewPanel.getComponent(0).el.getBox().height+this.viewPanel.getComponent(2).el.getBox().height+15);
				this.viewGrid.setHeight(height);
			}
			var entrys=data.items;
			if(this.pitchProductSn && this.store){
				if(this.store.baseParams && this.store.baseParams.productSn){
					this.viewGrid.store.productSn=this.store.baseParams.productSn.toUpperCase();
				}
			}
			this.viewGrid.store.removeAll();	
			this.initColumnDisplay(this.viewGrid);
			if(entrys&&entrys.length){
				var pageList={rowCount:entrys.length,result:entrys};	
				this.viewGrid.store.loadData(pageList);
			}
			else {
				this.loadItems(data.id,this.viewGrid);
			}
		}
	},
	autoCountNum:function(record){
		var product=record.get("product");
    	if(!product)return;
    	if(product.encaped){//按封装、手进行管理
    		if(record.get("encapNum")){
    			var num = (record.get("encapNum")?record.get("encapNum"):0)*(product.encapNum?product.encapNum:1);
    			record.set("num",num);	
    		}
    	}else if(product.sizes && product.sizes.length && (product.attributeType==0||product.attributeType==2)){//按尺码进行管理
    		var t=0;
    		for(var i=0;i<this.editExtractField.length;i++){
    			var s=record.get("size-"+i);
    			if(s)t+=parseFloat(s);
    		}
    		record.set("num",t);
    	}
	},
    autoCountData:function(record){
    	this.autoCountNum(record);
        var p = record.get("salePrice");
        var n = record.get("num");
        var total = p && n ? p * n : 0;       
        record.set("saleAmount",parseFloat(total));
        record.set("totalAmount",parseFloat(total));
    },
    addRowDataHandler:function(r){
    	//this.editGrid.stopEditing(true);
    	var obj=BaseStockOutcomeWearBillPanel.superclass.addRowDataHandler.call(this,r);
    	obj.colors=r.colors;
        obj.colorGroup=r.colorGroup;
        obj.stockNO=r.stockNO;
        obj.encapNum=r.encapNum;
        obj.encaped=r.encaped;
        obj.sizes=r.sizes;
        obj.style = r.style;
        if(obj.encaped){
    		var index=this.editGrid.getColumnModel().findColumnIndex("encapNum");
    		if(index>=0)this.editGrid.getColumnModel().setHidden(index,false);
    	}
    	else if(obj.sizeGroup&&obj.sizes&&(obj.attributeType==0||obj.attributeType==2)){
            this.displaySizeTypeNum(this.editGrid,obj);
    		//this.editGrid.getView().setGroupCellDisplay(obj.sizeGroup.id,true);
    	}
    	return obj;
    },
	sizeNumEditRender:function(field,editorColor){
		return function(v,meta,r){
			var p=r.get("product");
			var ret = false;
			if(p){
			if(!p.encaped&&p.sizes && p.sizeGroup) {
			/*var hrows=service.groupHeaderCellConfig.rows;
			var sizeGroup=null;
			for(var i=0;hrows&&i<hrows.length;i++){
				if(hrows[i].id==p.sizeGroup.id){
					sizeGroup=hrows[i];
					break;
				}
			}*/
			var sizeGroup=r.data.groups;
			var sizeId=null;
			if(sizeGroup){
			var d=parseInt(field.substring(field.indexOf("-")+1));
			if(sizeGroup.headers.length>d){
				sizeId=sizeGroup.headers[d].sizeid;
			}
			}		
			for(var i=0;i<p.sizes.length;i++){
				if ((p.sizes[i].id)== sizeId) {
				ret = true;
				break;
				}
			}
			}
			}
			if(!ret)meta.attr = 'style="background-color:'+Disco.Ext.Util.readOnlyGridCellColor+';"';
            else if(editorColor)meta.attr = 'style="background-color:'+editorColor+';"'; 
			return v;
		}},
    selectRowDataSizeRenderHandler:function(obj,record,cell){
    	var group=null;
		for(var j=0;j<this.groupHeaderCellConfig.rows.length;j++){
			if(this.groupHeaderCellConfig.rows[j].id==obj.sizeGroup.id){
				group=this.groupHeaderCellConfig.rows[j];
				break;
			}
		}
		record.data.groups=group;
    },
    getProductGroupColById:function(product,sizeId){
    	var fn = function(obj){
    		if(obj.sizeid==sizeId){
    			return true;
    		}
    	}
    	var col = this.editGrid.getView().getGroupColByFn(product.sizeGroup.id,fn,this);
    	if(col!=null){
    		return 'size-'+col;
    	}
    },
    findProductByBarCode:function(obj){
    	var index = this.editGrid.getStore().findBy(function(record){
    		var data = record.data;
    		if(data.product){
    			if(data.product.id==obj.product.id){
    				if(data.color){
    					if(data.color.id==obj.color.id){
    						return data;				
    					}
    				}else{
    					return data;
    				}
    			}
    		}
    	},this);
    	if(index>=0){
    		return this.editGrid.getStore().getAt(index);
    	}
    },
    isProductSizeManager:function(reocrd){
    	var product = reocrd.data.product;
    	if(reocrd && (product=reocrd.data.product) && (product.sizeGroup&&product.sizes&& (product.attributeType==0||product.attributeType==2))){
    			return true;
    	}
    	return false;
    },
    barCodeAutoData:function(record,e,obj,check){
    	if(this.checkStock || this.loadStock){
			this.productStockGrid.store.removeAll();
			var fn = function(){
				if(this.checkStock){
		    		try{
						if(!this.doColorCheckStockNum(e)){
							return;
						}
		    		}catch(e){}
				}
			}.createDelegate(this);
			this.doLoadStock(record,true,fn);
		}
		if(check===true)fn();
    	this.autoCountData(record,e);
    },
    addProductByBarCode:function(obj){
    	var e ={field:'num'};
    	this.autoAddLastRow();
    	var record,product;
		record = this.findProductByBarCode(obj);
    	//如果是尺码管理，并且在列表中已经存在，则只是修改货品的数量!
    	if(record){
    		e.record = record;
    		product = record.get('product');
    		if(this.isProductSizeManager(record)){
	    		var col = this.getProductGroupColById(product,obj.size.id);
	    		if(col){
	    			e.field = col;
	    			record.set(col,Ext.num(record.get(col),0)+obj['num']);
	    			e.value = record.get(col);
	    		}
	    		this.editGrid.getSelectionModel().select(this.editGrid.getStore().indexOf(record),0);
	    		this.barCodeAutoData(record,e,obj,true);
	    		return ;
	    	}else if(!product.encaped){
	    		record.set('num',Ext.num(record.get('num'),0)+obj['num']);
	    		e.value = record.get('num');
	    		this.barCodeAutoData(record,e,obj,true);
	    		this.editGrid.getSelectionModel().select(this.editGrid.getStore().indexOf(record),0);
	    		return ;
	    	}
    	}
    	var store = this.editGrid.getStore();
    	record = store.getAt(store.getCount()-1);
    	product = obj['product'];
    	obj['recordId'] = record.id;
    	Ext.apply(record.data,product,{});
    	record.set('id',null);
    	e.record = record;
    	this.selectRowDataHandler(product,record,0);
    	this.autoAddLastRow();
    	if(this.isProductSizeManager(record)){
    		var col = this.getProductGroupColById(product,obj.size.id);
    		if(col)record.set(col,Ext.num(record.get(col),0)+obj['num']);	
    	}else if(!product.encaped){
    		record.set('num',Ext.num(record.get('num'),0)+obj['num']);
    		e.value = record.get('num');
    	}
    	this.barCodeAutoData(record,e,obj);
    	this.editGrid.getSelectionModel().select(this.editGrid.getStore().indexOf(record),0);
    	this.fireEvent('Code',obj,record);
   	},
    selectRowDataHandler:function(obj,record,cell){
    	BaseStockOutcomeWearBillPanel.superclass.selectRowDataHandler.call(this,obj,record);
    	record.set("colors",obj.colors);
    	record.set("stockNO",obj.stockNO);
    	record.set("tradePrice",obj.tradePrice);
        record.set("colorGroup",obj.colorGroup);
        record.set("encapNum",null);
        record.set("encaped",obj.encaped);
        record.set("sizes",obj.sizes);
        record.set("style",obj.style);
        record.set("attributeType",obj.attributeType);
    	if(obj.encaped){
    		var index=this.editGrid.getColumnModel().findColumnIndex("encapNum");
    		if(index>=0)this.editGrid.getColumnModel().setHidden(index,false);
    	}else if(obj.sizeGroup&&obj.sizes&& (obj.attributeType==0||obj.attributeType==2)){
            this.displaySizeTypeNum(this.editGrid,obj);
    		//this.editGrid.getView().setGroupCellDisplay(obj.sizeGroup.id,true,this.checkShowingSizeTypeNum(obj.sizeGroup.id,obj.sizes));
    		this.selectRowDataSizeRenderHandler(obj,record,cell);
    	}
		/*var cm = this.editGrid.getColumnModel();
		if(record.readonly){
			for (var d in record.readonly) {
				var c =	cm.findColumnIndex(d);
				if(this.editGrid.setCellEditable)
					this.editGrid.setCellEditable(cell[0],c,false);	
			}
		}*/
		//startEditing.defer(100,this.editGrid,[cell[0],cell[1],true]);
 		//this.createColumnSeq(obj,record,cell);
    },
    /**
     * 加载一条记录集对应的库存信息
     * @param {} record
     */
    loadProductStock:function(record){
    	if((this.checkStock||this.loadStock) && this.productStockGrid && record && record.get("product")){
    		var p=record.get("product");
            if(this.productStockGrid.productId==p.id)return;//不用再检查
            this.productStockGrid.store.removeAll();
    		if(this.fp.findSomeThing("id").getValue()){
    			var flag=false;
    			this.stockCache.each(function(item){
    				if(item.product==p.id) flag=true;
    			});
    			if(!flag){
    				this.doLoadStock(record,true);//重新加载
                    return;
    			}
    		}
            var datas=[];
            Disco.Ext.Util.setGridColumnHidden(this.productStockGrid.sizeFields,true,this.productStockGrid);
            //如果手管理否，并且管理颜色和尺码或者管理尺码，首先需要对货品/颜色和尺码进行分组
            if((!p.encaped && p.attributeType ==0)||(!p.encaped && p.attributeType ==2)){
                var tempCache=new Ext.util.MixedCollection(),sizes=[];
                this.stockCache.each(function(item){
                    var sizeKey="size-"+item.size;
                    if(item.product==p.id){
                        if(item.color && item.color!=-1){
                            var temp=tempCache.get(item.product+"@@"+item.color);
                            if(!temp){
                                temp={name:p.sn,colorName:item.colorName,num:0,storeNum:0};
                                tempCache.add(item.product+"@@"+item.color,temp);
                            }
                            if(item.num)
                            	temp.num+=item.num;
                            if(item.storeNum)
                            	temp.storeNum+=item.storeNum;
                            temp[sizeKey]=item.num;
                        }else{
                            var temp=tempCache.get(item.product);
                            if(!temp){
                                temp={name:p.sn,num:0,storeNum:0};
                                tempCache.add(item.product,temp);
                            }
                            temp.num+=item.num;
                            temp.storeNum+=item.storeNum;
                            temp[sizeKey]=item.num;
                        }
                    }
                   if(sizes.indexOf(sizeKey)<0)sizes.push(sizeKey); 
                },this);
                datas= tempCache.getRange();
                Disco.Ext.Util.setGridColumnHidden(sizes,false,this.productStockGrid);
            }
            else {//不管理尺码
                this.stockCache.each(function(item){
                    if(item.product==p.id){
                        if(!item.name)item.name=p.sn;
                        datas.push(item);
                    }
                },this);
            }
            this.productStockGrid.productId=p.id;
            this.productStockGrid.store.loadData(datas);
    	}
    },
    generatorSn:function(waitMsg){
    	Ext.Ajax.request({url:this.baseUrl+"?cmd=generatorSn",params:{types:this.types||"",snType:this.snType||""},waitMsg:waitMsg,success :function(response){
    		var sn=Ext.decode(response.responseText);
    		this.fp.form.findField("sn").setOriginalValue(sn);
    	},
    	scope:this
    	});
    },
    /**
     * 执行库存检查
     * @param {} record 当前记录
     * @param {} unCheck 
     * @param {} callback 
     * @param {} failure
     */
   	doLoadStock:function(record,unCheck,callback,failure){
   		var p=record.get("product");
		var depot=this.fp?this.fp.findSomeThing(this.checkDepot?this.checkDepot:"depot").getValue():null;
		var flag=false;
		if(this.stockCache){
			this.stockCache.each(function(item){
				if(item.product==p.id){
					flag=true;
				}
			});
			if(!flag){
				if(p&&depot){
					if(depot==-1){
						depot="";
					}
					Ext.Ajax.request({scope:this,url:this.stockCheckUrl||"productStock.ejf?cmd=colorCheckStock",params:Ext.apply({},{depot:depot,product:p.id},this.stockCheckBaseParameter||{}),success:function(req){
						var ret=Ext.decode(req.responseText);
						if(ret && ret.length && this.stockCache){
							for(var i=0;i<ret.length;i++){
								var temp=ret[i];
								var cache = {
								    product : temp.product.id,
								    color : temp.color ? temp.color.id : -1,
								    colorName : temp.color ? temp.color.title : "",
                                    num:temp.num,
                                    storeNum:temp.storeNum
								};
                                var key=cache.product;
                                if(cache.color&&cache.color!=-1)key+="@@"+cache.color;
								if(!temp.product.encaped && temp.size&&temp.size.id){
									cache.size=temp.size.id;
									key+="@@"+cache.size;
								}
                                if(this.existStockCache){
	                                var existItem=this.existStockCache.get(key);
	                                if(existItem)cache.num+=existItem.num;
                                }
                               
                                this.stockCache.add(key,cache);
							}
							this.loadProductStock(record);
							if(callback)callback.call(this);
						}else if(!ret||(!unCheck && !ret.uncheck)){
                            if(this.productStockGrid)delete this.productStockGrid.productId;//删除当前产品
							Ext.Msg.alert("提示信息","该产品在当前仓库中没有"+(this.stockCheckFieldLabel||"库存")+"！",function(){
								this.editGrid.getSelectionModel().select(this.editGrid.store.indexOf(record),this.editGrid.getColumnModel().findColumnIndex("product"));
								if(this.checkStock){
                                    if(failure)failure.call(this);
								};
								if(this.loadStock){
									if(callback)callback.call(this);
								}
							},this);
						}else{
							if(this.loadStock){
                                if(callback)callback.call(this);
							}
						}
					}})
				}
			}else{
				this.loadProductStock(record);
				if(callback)callback.call(this);
			}
		}
   	},
    focusToMsgBox : function(stopEditing) {
        (function() {
                    if (stopEditing && this.editGrid)
                        this.editGrid.stopEditing();
                    if(Ext.Msg.getDialog().buttons)Ext.Msg.getDialog().buttons[0].focus();
                }).defer(100, this);
    },
    findAndTotalProductStock:function(pkey){
        var num=0;
        this.stockCache.eachKey(function(id,item){
        if(id.indexOf(pkey)>=0)num+=item.num;
        });
        return num?{num:num}:null;
    },
    doColorCheckStockNum:function(e){
		//var id=this.fp.findSomeThing("id").getValue();    
		var flag=true;
		if (e.field) {
			var p=e.record.get("product");
			var color=e.record.data.color ? ((e.record.data.color.value||e.record.data.color.id)||-1) : -1;
			if(this.stockCache && (e.field=="num"||e.field=="encapNum"||e.field.indexOf("size-")>=0)){
                var pkey=p.id;
                if(color!=-1)pkey+="@@"+color;
                if(e.field.indexOf("size-")>=0){//只有在编辑尺码的时候才使用
                    var size=this.getEditingSize(e,p);
                        pkey+="@@"+size;
                }
                var item=this.stockCache.get(pkey);
                var errorMsg=false;
                if(!item)item=this.findAndTotalProductStock(pkey);
                if(item){
                    var num=e.value;
                    if(e.field=="encapNum"){
                        num=num*(p.encapNum||1);
                    }
                    if(num>item.num){
                        errorMsg="当前"+(this.stockCheckFieldLabel||"可开单量")+"最多为:"+item.num;
                    }
                }
                else if(e.value>0){//没有库存
                    errorMsg="当前货品没有"+(this.stockCheckFieldLabel||"可开单量");
                } 
                if(errorMsg){
                    Ext.Msg.alert("提示信息",errorMsg,function(){
                                    e.record.set(e.field,0);
                                    e.record.set("num",0);
                                    this.editGrid.startEditing(e.row,e.column);
                                },this);
                    this.focusToMsgBox(true);  
                    flag=false;
                }
			}
		}
		return flag;
	}, 
	getEditingSize:function(e,p){
		if(p.sizeGroup){
			var rows=this.groupHeaderCellConfig.rows;
			for(var i=0;i<rows.length;i++){
				if(rows[i].id==p.sizeGroup.id){
					var group=rows[i];
					var groupindex=e.field.substring(5,e.field.length);
					return group.headers[parseInt(groupindex)].sizeid;
				}
			}
		}
	},
    createProductStockGrid:function(){
        var cms=[new Ext.grid.RowNumberer({header:"编号",width:40}),
                     {header:"ID",dataIndex:"id",hideable:true,hidden:true},
                     {header:"名称",dataIndex:"name",width:100},
                     {header:"颜色",dataIndex:"colorName",width:100}
                 ];
        var fields=["id","name","colorName","num","storeNum"],sizeFields=[];
        for(var i=0;i<this.productStockExtractField.length;i++){
            cms[cms.length]={width:60,header:this.productStockExtractField[i].title,groupHeader:true,dataIndex:"size-"+this.productStockExtractField[i].id,hidden:true,summaryType: 'sum',summaryRenderer: function(v){return v.toFixed(2);}};
            sizeFields.push("size-"+this.productStockExtractField[i].id);
        }
        fields=fields.concat(sizeFields);
        Ext.each([{header:"可开单量",dataIndex:"num",width:60},
                  {header:"实际库存",dataIndex:"storeNum",width:60}],function(o){
                 cms[cms.length]=o;
        });              
        this.productStockGrid=new Ext.grid.GridPanel({
            title:"库存及可开单量",
            viewConfig : {forceFit:true},       
            cm:new Ext.grid.ColumnModel(cms),
            sizeFields:sizeFields,
            store:new  Ext.data.JsonStore({
                fields:fields
            }),
            keys:[{
                key:Ext.EventObject.INSERT,
                fn:function(){
                    this.autoAddLastRow();
                    var row=this.editGrid.store.getCount()-1;
                    var column=this.editGrid.getColumnModel(row).findColumnIndex("productSn");
                    this.editGrid.startEditing(row,column);
                },
                scope:this
            },{
                key:Ext.EventObject.DELETE,
                fn:this.removeRow,
                scope:this
            }],
            height:90
        });
    },
	/*createProductStockGrid2:function(){
		var cms=[
			 		 {header:"ID",dataIndex:"id",hideable:true,hidden:true},
		 		 	 {header:"名称",dataIndex:"name",width:100},
		 		 	 {header:"颜色",dataIndex:"colorName",width:100}
		 		 ];
		var gcs=[[]];
        gcs[0][0]={header:"基本信息", colspan: 3, align: 'center'};
		var fields=["id","name","colorName","num","storeNum"];
		for(var i=0;i<this.productStockExtractField.length;i++){
            cms.push({width:100,header:"库存",groupHeader:true,dataIndex:"size-"+this.productStockExtractField[i].id,hidden:true,editor:new Ext.form.NumberField()});
            cms.push({width:100,header:"可开单",groupHeader:true,dataIndex:"size-store-"+this.productStockExtractField[i].id,hidden:true,editor:new Ext.form.NumberField()});
            fields.push("size-"+this.productStockExtractField[i].id);
            fields.push("size-store-"+this.productStockExtractField[i].id);
            gcs[0].push({header:"size"+i, colspan: 2, align: 'center'});
		}
        cms=cms.concat([{header:"可开单量",dataIndex:"num",width:100},
                     {header:"实际库存",dataIndex:"storeNum",width:100}]);
		gcs[0].push({header:"库存及开单量信息", colspan: 2, align: 'center'});
		this.productStockGrid=new Ext.grid.GridPanel({
			title:"库存及可开单量",
			viewConfig : {forceFit:true},		
			cm:new Ext.grid.ColumnModel(cms),
			store:new  Ext.data.JsonStore({
				fields:fields
			}),
			keys:[{
				key:Ext.EventObject.INSERT,
				fn:function(){
					this.autoAddLastRow();
					var row=this.editGrid.store.getCount()-1;
					var column=this.editGrid.getColumnModel(row).findColumnIndex("productSn");
					this.editGrid.startEditing(row,column);
				},
				scope:this
			},{
				key:Ext.EventObject.DELETE,
				fn:this.removeRow,
				scope:this
			}],
			height:90,
            plugins: [new Ext.ux.plugins.GroupHeaderGrid({
                            rows:gcs,
                            hierarchicalColMenu: true
            })]
		});
	},*/
    beforeEditSizeType:function(p,e){  //编辑前来判断该尺码是否属于该货品的尺码
	    if(p.sizeGroup&&p.sizes&&p.sizes.length){
 		     var header = Ext.fly(e.grid.getView().getHeaderCell(e.column)).child('.group-'+p.sizeGroup.id).dom.innerHTML;
 		     var ret = false;
 		     for(var i =0;i<p.sizes.length;i++){
 		     	if(header == p.sizes[i].title){
 		   			ret = true;
 		   		}
 		   	}
 		    return ret;
		}
        else return false;
	},
	isEditableCell:function(grid,row,col){
        if(this.editGridCanEdit===false)return false;
		var field=grid.getColumnModel().getDataIndex(col);
		var record=grid.store.getAt(row),data=record.data;
		if(!field||!record || !data)return false;
		var p=data.product;
		if(field!="productSn" && (!p || !p.id)){
				return false;
		}
		if(field=="color" && p.attributeType==3)return false;//不管理颜色
		if(field=="encapNum" && !p.encaped)return false;//不管理手
		if(field=="num"){
             if(p.encaped)return !(data.encapNum || this.disableInputNum);//管理手
             else if((p.attributeType==0||p.attributeType==2)&&p.sizes&&p.sizes.length)return false;
		}
		if(field=="price" && this.disablePriceChange)return false;
		if(field.indexOf("size-")==0){
			if(p.encaped)return false;
			return this.beforeEditSizeType(p,{column:col,grid:grid});
		}		
		return true;
	},
	isRequiredCell:function(grid,row,col){
		var field=grid.getColumnModel().getDataIndex(col);
		var record=grid.store.getAt(row),data=record.data;
		if(!field||!record || !data)return false;
		if(field=="productSn")return true;		
		var p=data.product;
		if(!p || !p.id){//没有输入产品时,所有均为无需填写
				return false;
		}		
		if(field=="color"&& (p.attributeType===0||p.attributeType===1))return true;//不管理颜色者均要填写		
		else if(field=="encapNum" && p.encaped)return true;//管理手
		else if(field=="num" && (!p.encaped || (!data.encapNum && !this.disableInputNum)))return true;//管理手		
		else if(field=="price" &&!data.price && this.disablePriceChange!==true)return true;
		else if(field.indexOf("size-")==0){
			if(!p.encaped){
				return this.beforeEditSizeType(p,{column:col,grid:grid});
			}
		}
		return false;
	},
	createEditGridListeners:function(){
		return this.baseEditGridListeners;
	},
    baseEditGridListeners:{
    	beforerequirededit:function(grid,row,col){
    		var r=this.isRequiredCell(grid,row,col);
    		return r;
    	},
		beforeedit : function(e) {
			if(e.field!="color"){
				if(this.colorEditor.isExpanded())this.colorEditor.collapse();
			}
            if("num"==e.field){
                    var record = e.record ;
                    if(window.parseFloat(record.get('encapNum'))>0){
                        return false;
                    }
            }
			if(this.doBeforeEdit){
				var ret=this.doBeforeEdit(e);
				if(ret===false) return false;
			}
			var canEdit=this.isEditableCell(e.grid,e.row,e.column);
			if(canEdit){
				var p=e.record.get("product");
				if(e.field=="color"){
				if(this.colorStore && this.colorStore.get(p.id)){
		    		var filterout=this.colorStore.get(p.id);
			    	var colors=[];
			    	var currentColor=e.record.get("color");
			    	if(currentColor&&currentColor.value)colors.push({id:currentColor.value,title:currentColor.title||currentColor.text});			    	
			    	for(var i=0;i<p.colors.length;i++){
			    		var flag=false;
			    		for(var j=0;j<filterout.length;j++){
			    			if(p.colors[i].id ==filterout[j]){
			    				flag=true;
			    			}
			    		}
			    		if(!flag)
			    			colors.push(p.colors[i]);
			    	}
					this.colorEditor.store.loadData(colors);   
		    	}else
					this.colorEditor.store.loadData(e.record.get("product").colors?e.record.get("product").colors:null);   	
				}				
			}
			return canEdit;
			//alert(111);
			/*var ss="productSn,encapNum,color,product";
			if(ss.indexOf(e.field)>=0 && this.editGridCanEdit===false) return false;
			var p=e.record.get("product");
			if(e.field!="productSn" && (!p || !p.id)){
				return false;
			}
			else if( (e.field=="num" || e.field=="encapNum") && !p.encaped){
				if(p.attributeType!=1 && p.attributeType!=3)return false;
			}*/
		},		
		afteredit : function(e) {
			if(e.field=="color"){
				var product=e.record.get("product");
				if(e.value){
					if(product){
						if(this.colorStore && this.colorStore.get(product.id)){
							this.colorStore.get(product.id).push(e.record.get("color").value);
							this.colorStore.get(product.id).remove(e.originalValue.value||e.originalValue.id);
						}else{
							this.colorStore =this.colorStore || new Ext.util.MixedCollection();
							this.colorStore.add(product.id,[e.record.get("color").value]);
						}
					}
				}
				if(this.checkStock){
					this.doColorCheckStockNum(e);
				}
			}
			if("encapNum"==e.field){
				if(e.record.get("product") && e.record.get("product").encaped && e.originalValue!='' && e.originalValue!=0 && (e.value==0||e.value=='')){
					e.record.set("num",0);
				}
			}
			/*var cm = e.grid.getColumnModel();
			if("encapNum"==e.field){
				var col = cm.findColumnIndex('num');
				if(e.value!='' && e.value!=0){
					e.grid.setCellEditable(e.row,col);
				}else{
					e.grid.setCellEditable(e.row,col,true);
				}
			}
			
			if("num"==e.field){
				var col = cm.findColumnIndex('encapNum');
				if(e.value!='' && e.value!=0){
					e.grid.setCellEditable(e.row,col);
				}else{
					e.grid.setCellEditable(e.row,col,true);
				}
			}*/
			
			var s="price,salePrice,encapNum,num";
			if (s.indexOf(e.field)>=0 ||e.field.indexOf("size-")>=0) {// 计算合计
				if(parseFloat(e.value)<0){
					if(this.negative){
						if(!this.doChecknegative(e)){
							Ext.Msg.alert("提示信息","负数超过合法范围!",function(){
								this.editGrid.store.getAt(e.row).set(e.field,0);
								this.editGrid.startEditing(e.row,e.column);
								return;
							},this);
						}else{
							this.autoCountData(e.record);
							if(this.checkStock)
								if(!this.doColorCheckStockNum(e)) return;
						}
					}else{
						Ext.Msg.alert("提示信息","数量不能填负数!",function(){
							this.editGrid.store.getAt(e.row).set(e.field,0);
							this.editGrid.startEditing(e.row,e.column);
							return;
						},this)
					}
				}else{
					this.autoCountData(e.record);
					if(this.checkStock)
						if(!this.doColorCheckStockNum(e)) return;
				}
			}
			
			if(e.record.dirty){
				if(this.fp.getTopToolbar().items.get("tb_audit"))
					this.fp.getTopToolbar().items.get("tb_audit").disable();
				if(this.fp.getTopToolbar().items.get("tb_print"))
					this.fp.getTopToolbar().items.get("tb_print").disable();
			}
			if(this.afterEdit)this.afterEdit(e);
			/*if(this.afterEdit){
				this.afterEdit(e,(function(){
					this.firstColumn(e.row,e.field,e.record);
				}).createDelegate(this));
			} else{
				this.firstColumn(e.row,e.field,e.record);
			}*/
		},
		cellcontextmenu:function(g,rowIndex,celIndex,e){
			if(this.editGridMenu){			
				var o=this.editGridMenu.items.get("menu_remove");
				if(o)o.enable();
				g.getSelectionModel().select(rowIndex,celIndex);
				this.editGridMenu.showAt(e.getPoint());
				e.preventDefault();
			}
		},
		contextmenu:function(e){
			if(this.editGridMenu){
				var o=this.editGridMenu.items.get("menu_remove");
				if(o && !this.editGrid.getSelectionModel().getSelectedCell())o.disable();
				this.editGridMenu.showAt(e.getPoint());
				e.preventDefault();
			}
		 }
	},
	/**
	 * 获得所有尺码对象
	 * @return sizes[];
	 */
	/*getColumnSizes:function(){ 
		var sizes= this.editGrid.getColumnModel().getColumnsBy(function(c){
			if(c.dataIndex.indexOf('size-')===0){
				return true;
			}
		},this);
		return sizes;
	},
	firstColumn:function(row,field,record){
		var sqe = record.columnSeq;
		var cell = this.editGrid.getSelectionModel().getSelectedCell();
		if(cell){
			var dataIndex = this.editGrid.getColumnModel().getDataIndex(cell[1]);
			if(dataIndex!=field || cell[0]!=row){
				return ;
			}
		}
		if(sqe){
			var c=-1;
			for (var index = 0; index < sqe.length; index++) {
				var regExp = new RegExp(sqe[index]);
				if(regExp.test(field)){
					c = index;
					break;
				}
			}	
			if(c>=0){
				var c = sqe[c+1];
				if(c=='size-*' || (field.indexOf('size-')!=-1)){if(this.getNextSize(row,field))return;}
				if(c){
					var col = this.editGrid.getColumnModel().findColumnIndex(c);
					if(col>=0){
						this.editGrid.stopEditing(true);
						var editor=this.editGrid.getColumnModel().getCellEditor(col,row).field;
						if(editor.onTriggerClick){
							var view = this.editGrid.getView();
							var editor=this.editGrid.getColumnModel().getCellEditor(col,row).field;
							(function(){
								this.editGrid.startEditing(row,col);
								(function(){
									editor.onTriggerClick();
									var column = this.editGrid.getColumnModel().findColumnIndex(c);
									editor.list.alignTo(view.getCell(row,column), 'tl-bl?');
								}).defer(200,this);
							}).defer(30,this);
						}else{
							if(this.editGrid.getColumnModel().isCellEditable(col,row)){
								this.editGrid.getSelectionModel().select(row,col);
								this.editGrid.startEditing(row,col);
								if(this.editGrid.getColumnModel().getCellEditor(col,row).field.el)
								(function(){this.editGrid.getColumnModel().getCellEditor(col,row).field.el.dom.select()}).defer(100,this);
							}else{
								this.editGrid.getSelectionModel().select(row,col);
								this.firstColumn(row,c,record);
							}
						}
						return true;
					}
				}else{
					if(this.editGrid.activeEditor===null){
						this.editGrid.stopEditing(true);
						var column = this.editGrid.getColumnModel().findColumnIndex("productSn");
						this.editGrid.startEditing(row+1,column);
					}
				}
			}
		}
		return false;
	},*/
	/*getNextSize:function(row,col){
		var cell = this.editGrid.getSelectionModel().getSelectedCell();
		var cm = this.editGrid.getColumnModel();
		var regExp = /^size-/;
		var sizes = this.getColumnSizes();//获得所有可编辑尺码
		if(!regExp.test(col)){
			var columnIndex = cm.findColumnIndex(sizes[0].dataIndex);
		}else{
			var columnIndex =Ext.type(col)=='number'?col:cm.findColumnIndex(col);
		}
		var nextSize = -1;
		var currentColumn = cm.getColumnById(cm.getColumnId(columnIndex));
		var index =	sizes.indexOf(currentColumn);//获得当前尺码在尺码数组中的位置
		if(regExp.test(col)){index+=1}
		for (var i = index; i < sizes.length; i++) {
			var column=cm.findColumnIndex(sizes[i].dataIndex);
			if(cm.isCellEditable(column,row)){
				nextSize = column;
				break;
			}
		}
		if(nextSize>0){
			this.editGrid.stopEditing();
			if(index==0){
				this.editGrid.startEditing.defer(1,this.editGrid,[row,nextSize]);
			}else{
				this.editGrid.startEditing(row,nextSize);
			}
			(function(){this.editGrid.getColumnModel().getCellEditor(nextSize,row).field.el.dom.select()}).defer(100,this);
			return true;
		}
	},*/
	removeRow:function(){
		Disco.Ext.Util.removeGridRow(this.editGrid,this.beforeRemoveRow.createDelegate(this));
   	},
	beforeRemoveRow:function(){
		if(this.editGrid)
			this.editGrid.stopEditing(true);
		var row=this.editGrid.getSelectionModel().getSelectedCell()[0];
		var record=this.editGrid.store.getAt(row);
		if(record){
			var product = record.get("product") ? record.get("product").id : null;
			var color = record.get("color")?(record.get("color").value||record.get("color").id):null;
			if(color && product){
				if(this.colorStore&&color&&this.colorStore.get(product)){
					this.colorStore.get(product).remove(color);
				}
			}
		}
		if(this.fp.getTopToolbar().items.get("tb_audit"))
			this.fp.getTopToolbar().items.get("tb_audit").disable();
		if(this.fp.getTopToolbar().items.get("tb_print"))
			this.fp.getTopToolbar().items.get("tb_print").disable();
   	},
	encapNumEditRender:function(v,meta,r){	
		var p=r.get("product");
		if(p&&!p.encaped){
		meta.attr = 'style="background-color:'+Disco.Ext.Util.readOnlyGridCellColor+';"';	
		}
		return v;
	},
	numEditRender:function(v,meta,r){	
		var p=r.get("product");
		if(p){
			if((p.encaped && (r.get("encapNum")||this.disableInputNum)) || (p.sizes&&p.sizes.length)){
				meta.attr = 'style="background-color:'+Disco.Ext.Util.readOnlyGridCellColor+';"';
			}
		}
		return v;
	},
    //选中一个产品后进行进行相关运算
	selectRowData:function(r){
   		var cell = this.editGrid.getSelectionModel().getSelectedCell();
   		if(cell){
   				this.editGrid.stopEditing();
				var obj = Ext.apply({}, r);
				var record = this.editGrid.store.getAt(cell[0]);
				this.selectRowDataHandler(obj,record,cell);
				this.autoCountData(record);
				this.autoAddLastRow();
				var sm=this.editGrid.getSelectionModel();
				if(this.checkStock || this.loadStock){
					this.productStockGrid.store.removeAll();
					this.doLoadStock(record,true,function(){
						sm.tryEdit(cell[0],cell[1]+1,true);//编辑下一行
					},this.doBlank);
				}
				else {
					sm.tryEdit(cell[0],cell[1]+1,true);
				}
		}	  
   	},
   	doBlank:function(){
   		var cell = this.editGrid.getSelectionModel().getSelectedCell();
   		var column = this.editGrid.getColumnModel().findColumnIndex("productSn");
   		var record = this.editGrid.store.getAt(cell[0]);
   		var p=record.get("product");
   		if(p){
   			var color = record.get("color")?(record.get("color").value||record.get("color").id):null;
			if(color){
				if(this.colorStore&&color&&this.colorStore.get(p)){
					this.colorStore.get(p).remove(color);
				}
			}
   		}
   		for(var r in record.data){if(record.get(r)){record.set(r,'');}}
		this.editGrid.startEditing(cell[0],column);
   	},
	createGridEditor:function(){
		var service=this;
		this.productEditor=new ProductComboBox(Ext.apply({},{
				 returnObject:true,
				 name:"productInput",
				 autoLoad:false,
				 hiddenName:"productInput",
				 displayField:"sn",
				 valueField:"id",
				 pageSize:false,
				 width:300,	
				 mode:"local",
				 minChar:100,
				 disableEnterNavigation:true,
				 selectSingle:true,
				 enableKeyEvents:true,
				 choiceValue:this.selectRowData.createDelegate(this),
				 blank:this.doBlank.createDelegate(this),
				 onTriggerClick:Ext.emptyFn,
				 store : new Ext.data.JsonStore({
					id : "id",
					url : "product.ejf?cmd=autocompleteList",
					root : "result",
					totalProperty : "rowCount",
					remoteSort : true,
					baseParams : {},
					fields : ["id","fullName","attributeType","buyPrice","title","sn","keyword","simiProducts","brand","unit","spec","model","types","buyPrice","dir",{name:"dirId",mapping:"dir"},"reCost","salePrice","ratePrice","marketPrice","tradePrice","bottomPrice","stockWarnNum","leastOrderNum","encapNum","size","updateTime","provideType","virtualStock","storeNum","intro","content","pj","star","auditing","inputTime","readTimes","status","vdate","propertys","color","colorSn","stockMinNum","stockNO","stockMaxNum","encapUnit","encapSn","encaped","buyPeriod","colors","sizes","sizeGroup"]
				}),
				 autoSelectBySn:function(c,e){
					if (e.getKey() == Ext.EventObject.ENTER) {
						if(e.shiftKey){
							service.editGrid.stopEditing();
							service.editGrid.getSelectionModel().tryEdit(this.editor.row,this.editor.col-1,true,-1);
							return;
						}
						var t=c.el.dom.value;
						if(!t){
							return;
						}
						if(this.beingChoiceProduct){
						return;
						}
						
						if(this.editor.startValue==t){
							service.editGrid.stopEditing();
							service.editGrid.getSelectionModel().tryEdit(this.editor.row,this.editor.col+1);
							return;
						}
						this.beingChoiceProduct=true;
						//e.stopEvent();
						if(this.store.baseParams){
							Ext.apply(this.store.baseParams,{sn:t});
						}
						try{
							this.store.baseParams['pageSize'] = 50;
							var response=Ext.lib.Ajax.syncRequest("POST", "product.ejf?cmd=loadBySn",this.store.baseParams);
							var pageObject = Ext.decode(response.conn.responseText);
							var obj=pageObject['result'];
							if(obj && obj.length==1){
								this.selectProvider(this,{data:obj[0]},-1);
							}
								else if(obj && obj.length >1){
										this.choiceProvider(obj);
							}else{
										Ext.Msg.alert("提示信息","该编号没有货品对应",this.blank);
							}
						}catch(e){
                            alert(e);
						}
						this.beingChoiceProduct=false;
						/*Ext.Ajax.request({
							url : "product.ejf?cmd=loadBySn",
							params : this.store.baseParams,
							success : function(response) {
								var obj=Ext.decode(response.responseText);
								
							},
							scope : this
						});*/
					}
				}},ConfigConst.CRM.product));			
		this.productEditor.on('keypress',function(t,e){
			if(e.getKey()==43 && t.getRawValue().length==0){
				this.editGrid.stopEditing();
				this.copyTheLastRow();
				e.stopEvent();
			}
		},this);
		if(!this.depotLocationEditor)
		this.depotLocationEditor=new Disco.Ext.SmartCombox({
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
		);	
		
		this.colorEditor=new Disco.Ext.SmartCombox({
				returnObject:true,
				name : "colorInput",
				hiddenName : "colorInput",
				fieldLabel : "colorInput",
				displayField : "title",
				valueField : "id",
				allowBlank:false,
				selectedClass:'x-combo-color-selected', // icon.css
				store : new Ext.data.JsonStore({
					fields : ["id","sn","title"]
				}),
				listeners:{
					scope:this,
					expand:function(c){
						c.innerList.applyStyles("background:#BFCFB1 none repeat scroll 0 0");
					}
				},
				editable : false,
				mode : 'local',
				triggerAction : 'all',
				emptyText : '请选择...'
			}
		);
	},
	getEditColumnModel:function(){
		this.createGridEditor();
		var cms=[				
		 		 new Ext.grid.RowNumberer({header:"序号",dataIndex:"sequence",width:35}),
		 		 {header:"Id",dataIndex:"id",width:1,hidden:true,hideable:false},
		 		 {header:"产品",dataIndex:"product",width:0,hidden:true,hideable:false},
		 		 {header:"货号",dataIndex:"productSn",width:120,editor:this.productEditor,summaryType: 'count',summaryRenderer: function(v){return "合计("+v+")";}},
		 		 {header:"货品编号",dataIndex:"stockNO",width:80,hidden:true,renderer:this.readOnlyRender()},
		 		 {header:"产品名称",dataIndex:"productTitle",width:80,renderer:this.readOnlyRender()},
		 		 {header:"单位",dataIndex:"unit",width:50,renderer:this.objectRender("title",Disco.Ext.Util.readOnlyGridCellColor)},
		 		 {header:"颜色",dataIndex:"color",width:80,editor:this.colorEditor,renderer:Disco.Ext.Util.comboxRender},
		 		 {header:"手",dataIndex:"encapNum",hidden:true,width:35,editor:new Ext.form.NumberField({selectOnFocus:true}),renderer:this.encapNumEditRender,summaryType: 'sum',summaryRenderer: function(v){return v;}}
		 		 ]
		for(var i=0;i<this.editExtractField.length;i++){
			cms[cms.length]={header:this.editExtractField[i].title,groupHeader:true,dataIndex:"size-"+this.editExtractField[i].id,width:35,hidden:true,editor:new Ext.form.NumberField({selectOnFocus:true}),renderer:this.sizeNumEditRender("size-"+this.editExtractField[i].id),summaryType: 'sum',summaryRenderer: function(v){return v&&v.toFixed?v.toFixed(2):v;}};
		}
		Ext.each([{header:"单价",dataIndex:"price",width:60,hidden:true,renderer:this.readOnlyRender(function(v){return v&&v.toFixed?v.toFixed(2):v;})},
				 {header:"数量",dataIndex:"num",width:40,editor:new Ext.form.NumberField({selectOnFocus:true}),renderer:this.numEditRender,summaryType: 'sum',summaryRenderer: function(v){return v;}},
				 {header:"金额",dataIndex:"totalAmount",hidden:true,width:80,renderer:this.readOnlyRender(function(v){return v&&v.toFixed?v.toFixed(2):v;}),summaryType:'sum',summaryRenderer: function(v, params, data){return v&&v.toFixed?v.toFixed(2):v;}},
				 {header:"单价",dataIndex:"salePrice",width:60,editor:new Ext.form.NumberField({selectOnFocus:true})},
				 {header:"金额",dataIndex:"saleAmount",width:80,renderer:this.readOnlyRender(function(v){return v&&v.toFixed?v.toFixed(2):v;}),summaryType:'sum',summaryRenderer: function(v, params, data){return v&&v.toFixed?v.toFixed(2):v;}},
				 {header:"批次",dataIndex:"blockSn",hidden:true,width:50,editor:new Ext.form.NumberField({selectOnFocus:true})},
				 {header:"库位",dataIndex:"location",hidden:true,width:80,editor:this.depotLocationEditor,renderer:function(v){return v?(v.title||v.text||v):v;}},
				 {header:"出库日期",dataIndex:"vdate",hidden:true,width:100,editor:new Ext.form.DateField({format:"Y-m-d"}),renderer:this.dateRender("Y-m-d"),hidden:true},
				 {header:"出库数",dataIndex:"stockNum",width:80,hidden:true,summaryType: 'sum',summaryRenderer: function(v){return v&&v.toFixed?v.toFixed(2):v;}},
				 {header:"开票数	",dataIndex:"invoiceNum",width:80,editor:new Ext.form.NumberField({selectOnFocus:true}),hidden:true,summaryType: 'sum',summaryRenderer: function(v){return v&&v.toFixed?v.toFixed(2):v;}},
				 {header:"备注",dataIndex:"remark",editor:new Ext.form.TextField()}],function(o){
				 cms[cms.length]=o;
				 });
		return new Ext.grid.ColumnModel(cms);
	},
	printList:function(){
		var id=this.fp.findSomeThing("id").getValue();
		if(!id){
			Ext.Msg.alert("提示信息","修改时才能打印!");
			return;
		}
		var s = Ext.urlEncode({cmd:"print",id:id});
		var win=new Ext.Window({title:"打印窗口",html:"<iframe width='100%' frameborder='no' style='background:#FFF' border=0 height='100%' src ='"+this.baseUrl+"?"+s+"' >"});
		win.show();
		win.fitContainer();
		win.center();
	},
	printRecord:function(types){
		var record = this.grid.getSelectionModel().getSelected();
		if (!record) {
			Ext.Msg.alert("提示",
					"请先选择要操作的数据！");
			return false;
		}
		var win=new Ext.Window({title:"打印窗口",html:"<iframe width='100%' frameborder='no' style='background:#FFF' border=0 height='100%' src ='"+this.baseUrl+"?cmd=print&id=" + record.get("id")+"' >"});
		win.show();
		win.fitContainer();
		win.center();
	},
    displaySizeTypeNum:function(grid,p){
        if(!this.displayingSizeTypeNum)this.displayingSizeTypeNum={};
        if(!this.displayingSizeTypeNum["product_"+p.id]){
            var ns=this.checkShowingSizeTypeNum(p.sizeGroup.id,p.sizes);
            if(ns){
               grid.getView().setGroupCellDisplay(p.sizeGroup.id, true,ns);
            }
	        this.displayingSizeTypeNum["product_"+p.id]=true;
        }
        return false;
    },
    checkShowingSizeTypeNum:function(groupId,sizes){
        if(sizes&&sizes.length){
        this.showingSizeTypeNum = this.showingSizeTypeNum || {} ;
        var gs=(this.showingSizeTypeNum["group_"+groupId]||[]);
        var ns=[];
        for(var i=0;i<sizes.length;i++){
            var isExist=false;
            for(var j=0;j<gs.length;j++){
                if(gs[j].id==sizes[i].id){
                    isExist=true;
                    break;
                }
            }
            if(!isExist)ns.push(sizes[i]);
        }
        if(!ns.length)return false;
        gs=gs.concat(ns);
        this.showingSizeTypeNum["group_"+groupId]=gs;
        return gs
        }
    },
    beforeLoadItem:function(items,grid,ret){
        for (var i = 0; i < items.length; i++) {
            var p = items[i].product;
            if (p.encaped) {
                var index = grid.getColumnModel().findColumnIndex("encapNum");
                if (index >= 0)grid.getColumnModel().setHidden(index, false);
            }
            if (p.sizeGroup && !p.encaped && (p.attributeType==0 || p.attributeType==2)) {//管理尺码
                 var gf=this.displaySizeTypeNum(grid,p);
            }
        }
    },
	afterLoadItem : function(items,grid,ret) {
        //console.time("test");
       // alert(111);
        var groupFields=[];
		for (var i = 0; i < items.length; i++) {
			var record=grid.store.getAt(i);
			var p = items[i].product;
            var key=p.id;
            if(items[i].color&&items[i].color.id)key+="@@"+items[i].color.id;
			if (p.sizeGroup && !p.encaped && (p.attributeType==0 || p.attributeType==2)) {//管理尺码
				this.selectRowDataSizeRenderHandler(p,record);
				for (var j = 0; j < items[i].sizeNums.length; j++) {
					//grid.store.getAt(i).set("size-" + items[i].sizeNums[j].groupIndex,items[i].sizeNums[j].num);
                    if(this.existStockCache){
                    if(items[i].sizeNums[j].size&&items[i].sizeNums[j].size.id){
                    this.existStockCache.add(key+"@@"+items[i].sizeNums[j].size.id,{num:items[i].sizeNums[j].num});
                    }}
				}				
			}
            else {
                if(this.existStockCache)this.existStockCache.add(key,{encapNum:items[i].encapNum,num:items[i].num});
            }
			if(this.colorStore && p.colors && p.colors.length && items[i].color){
				var filterout=this.colorStore.get(p.id);
				if(!filterout){
		    		this.colorStore.add(p.id,[]);
				}
				this.colorStore.get(p.id).push(items[i].color.id);
			}
		}
       //console.dir(this.colorStore);
       /* try{
        grid.store.commitChanges();
        }
        catch(e){
         alert(e);
        }*/
       //console.timeEnd("test");
	},
	createCustomerPanel:function(){
		return new Ext.form.FieldSet({		
			labelWidth:70,
			autoScroll:true,
			defaults : {
				xtype : "labelfield"
			},
			items : [
			 Disco.Ext.Util.twoColumnPanelBuild(
				{
				xtype : "labelfield",
				fieldLabel : '名称',
				name : 'title'
			}, {
				xtype : "labelfield",
				fieldLabel : 'Fax',
				name : 'fax'
			}, {
				xtype : "labelfield",
				fieldLabel : '联系电话',
				name : 'tel'
			}, {
				xtype : "labelfield",
				fieldLabel : '联系人',
				name : 'linkMan'
			}, {
				xtype : "labelfield",
				fieldLabel : '地址',
				name : 'address'
			})]
		})
	},
    cleanEditData:function(){
        this.editGrid.store.removeAll();
        this.initColumnDisplay(this.editGrid);
        if(this.productStockGrid){
            this.productStockGrid.store.removeAll();
            delete this.productStockGrid.productId;
        }
        if(this.checkStock){
            this.stockCache=new Ext.util.MixedCollection();
            this.existStockCache=new Ext.util.MixedCollection();
        }
        this.colorStore = new Ext.util.MixedCollection();
    },
	onCreate:function(){		
		this.cleanEditData();	
		BaseStockOutcomeWearBillPanel.superclass.onCreate.call(this);
		this.fp.cascade(function(c) {
			if (c && c.isFormField && c.getValue) {
				c.enable();
			}
		}, this);
		/*if(this.colorStore)	this.colorStore.clear();
		if(this.productStockGrid) this.productStockGrid.store.removeAll();
		if(this.checkStock)
			this.stockCache=new Ext.util.MixedCollection();*/
	},
	beforeSave:function(){
		if(this.beforeSaveCheck()===false){
			return false;
		}
		BaseStockOutcomeWearBillPanel.superclass.beforeSave.call(this);
   	},
   	productSearch:function(grid){
   		if(this.enableProductSearch && grid && grid.store.getCount()>1){
   			Ext.Msg.prompt('查找', '请输入查询的货号', function(btn, text){
			    if (btn == 'ok'){
			       grid.store.each(function(r){
			       		var productsn=r.get("productSn")||r.get("product").sn;
			       		if((productsn.toUpperCase()).indexOf(text.toUpperCase())>=0){
							 grid.getView().focusRow(grid.store.indexOf(r));
			       			 Ext.Element.fly(grid.getView().getRow(grid.store.indexOf(r))).addClass("x-grid3-row-selected");
			       		}
			       },this);
			    }
			});
   		}
   	},
   	beforeSaveCheck:function(otherCheck){
   		var store=this.editGrid.store;
		if(store.getCount()==0 || (store.getCount()==1 && store.getAt(0).get("product") && isNaN(parseInt(store.getAt(0).get("product").id)))){
			Ext.Msg.alert("错误提示","没有单据明细，不能保存！");
			return false;
		}
		if(store && store.getCount()>=1){
			var notNull=[];
			var canSave=true;
			var alert=false;
			store.each(function(r,sequence){
				if(r.get("product")){
					var temp={};
					if(!r.get("color") && (r.get("product").attributeType==0 ||r.get("product").attributeType==1)){
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
							if(!alert) alert=true;
						}
					}
					if(otherCheck && otherCheck(r,temp)===false){
						temp.sequence=sequence+1;
						if(!alert) alert=true;
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
					Ext.Msg.alert("错误提示",s);
					return false;
				}
				if(alert){
					var s="以下数据可能有误,是否确定保存？<br>";
					for(var i=0;i<notNull.length;i++){
						var temp=notNull[i];
						s+="行 "+temp.sequence+" : "+temp.nf+" 为空 <br>";
					}
					Ext.Msg.show({
					   title:'提示',
					   msg:s,
					   buttons: Ext.Msg.YESNO,
					   scope:this,
					   fn: function(btn){
					   		if(btn=="yes"){
					   			this.save(null,false,true);
					   		}
					   },
					   animEl: 'elId',
					   icon: Ext.MessageBox.QUESTION
					});
					return false;
				}
			}
		}
   	},
   	copyTheLastRow:function(){
   		var cell = this.editGrid.getSelectionModel().getSelectedCell();
		var cm = this.editGrid.getColumnModel();
		if (cell) {
			var col = cm.getDataIndex(cell[1]);
			if (col == 'productSn') {
				this.editGrid.stopEditing();
				var r = this.editGrid.store.getAt(cell[0] - 1);
				if (r && r.data.product && r.data.productSn) {
					var newone = r.copy();
					newone.set('id', '');
					newone.set('color', '');
					if(this.unCopyFields && this.unCopyFields.length){
						for(var i=0;i<this.unCopyFields.length;i++)
							newone.set(this.unCopyFields[i],"");
					}
					newone.readonly = Ext.apply({}, r.readonly);// 拷贝只读					
					this.editGrid.store.insert(cell[0], newone);
					this.editGrid.getSelectionModel().select(cell[0], cell[1]);
					var sm=this.editGrid.getSelectionModel();
					sm.tryEdit(cell[0], cell[1]+1,true);
				}
			}
		}
   	},
   	appendRow:function(){
   		this.autoAddLastRow();
		var row = this.editGrid.store.getCount() - 1;
		var column = this.editGrid.getColumnModel(row).findColumnIndex("productSn");
		this.editGrid.startEditing(row, column);
   	},
	createEditGrid:function(){
		var store=new Ext.data.JsonStore({
			root : "result",
			totalProperty : "rowCount",
			remoteSort:false,
			fields:this.editStoreMapping
		});
		var colM=this.getEditColumnModel();
		this.editGridMenu=new Ext.menu.Menu({items:[Ext.apply({}, {
				id:"menu_add",
				text : "添加商品[Ins]",
				handler : this.autoAddLastRow,
				scope : this
			}, ConfigConst.buttons.addChild), Ext.apply({}, {
				id:"menu_remove",
				text : "删除商品[Del]",
				handler : this.removeRow,
				scope : this
			}, ConfigConst.buttons.removeChild)]});
		this.editGrid = new Ext.grid.EditorGridPanel({
				cm:colM,
				selModel:new  Ext.grid.CellSelectionModel({enterNavigation:true}),
				autoScroll:true,
				firstEditColumn:"productSn",
				viewConfig:Ext.apply({},this.editGridViewConfig?this.editGridViewConfig:{},{
					onCellSelect : function(row, col){
				        var cell = this.getCell(row, col);
				        if(cell){
							this.addRowClass(row, "x-grid3-row-selected");
				            this.fly(cell).addClass("x-grid3-cell-selected");
				        }
				    },				
				    onCellDeselect : function(row, col){
				        var cell = this.getCell(row, col);
				        if(cell){
				        	this.removeRowClass(row, "x-grid3-row-selected");
				            this.fly(cell).removeClass("x-grid3-cell-selected");
				        }
				    }
				}),
				focus:function(){
					var column = this.getColumnModel().findColumnIndex(this.firstEditColumn||"productSn");
			 		this.startEditing(0,column);
				},
				keys:[this.addGridHotKey===true?{
					key:Ext.EventObject.NUM_PLUS,
					fn:Ext.emptyFn,
					scope:this
			}:{},{
				key:Ext.EventObject.INSERT,
				fn:this.appendRow,
				scope:this
			},{
				key:Ext.EventObject.DELETE,
				fn:this.removeRow,
				scope:this
			},{
				key:Ext.EventObject.S,
				altKey:true,
				stopEvent:true,
				fn:function(){this.save()},
				scope:this
			},{
				key:Ext.EventObject.F,
				ctrlKey:true,
				stopEvent:true,
				fn:function(){this.productSearch(this.editGrid)},
				scope:this
			}],
			store:store,
			clicksToEdit:1,
			border:false,
			autoExpandColumn:colM.getColumnCount()-1,
			plugins:(this.showGroupHeader?[new Ext.ux.grid.GridSummary(),new Ext.ux.plugins.GroupHeaderCellGrid(this.groupHeaderCellConfig)]:[new Ext.ux.grid.GridSummary()]),
			listeners : Ext.apply({},{scope : this},this.createEditGridListeners())
			}	
		);
		if(this.checkStock || this.loadStock){
			this.editGrid.on("cellclick",this.loadProductStockCell,this);
		}
		
		if(!this.celldblclickShowPictrue)this.editGrid.on("celldblclick",this.showPic,this);
		if(!this.productStockGrid)this.productStockGrid = this.createProductStockGrid.call(this);	
		if(!this.customerGrid) this.customerGrid = this.createCustomerPanel.call(this); 
	},
	loadProductStockCell:function(g,r,c,e){
		this.loadProductStock(g.getStore().getAt(r));
	},
	//双击产品名称查看图片
	showPic:function(grid, rowIndex, columnIndex, e) {
        var record = grid.getStore().getAt(rowIndex);  // Get the Record
        if(!record.get("product")) return false;
        var fieldName = grid.getColumnModel().getDataIndex(columnIndex); // Get field name
        if(fieldName==="productTitle"){
		if (!this.chooser) {
			this.chooser = new ImageChooser({
						url : 'product.ejf?cmd=loadPic',
						width : 500,
						height : 400
					});
		}
		this.chooser.setParams({
					id : record.get("product").id
				});
		this.chooser.show(Ext.fly(e.getRelatedTarget()));
        }
    },
	quickSearch : function() {
		var d1 = this.search_vdate1.getValue() ? this.search_vdate1.getValue().format("Y-m-d") : "";
		var d2 = this.search_vdate2.getValue() ? this.search_vdate2.getValue().format("Y-m-d") : "";
		this.store.baseParams = Ext.apply({}, {
			vdate1 : d1,
			vdate2 : d2,
			client : this.search_client.getValue(),
			distributor:this.search_distributor.getValue()
		},this.baseQueryParameter);
		this.refresh();
	},
	/*
	reset : function() {
		if(!this.fp.findSomeThing("id").getValue()){	
			if(this.editGrid.store.getCount())
				this.editGrid.store.removeAll();
			if(this.colorStore) this.colorStore.clear();
			BaseStockOutcomeWearBillPanel.superclass.reset.call(this)
		}
	},
	*/
	afterList:function(){
		this.btn_refresh.hide();
		this.searchField.hide();
		this.search_vdate1=new	Ext.form.DateField({fieldLabel:"开始时间",emptyText:"开始时间",width:90,format:"Y-m-d"});
		this.search_vdate2=new	Ext.form.DateField({fieldLabel:"结束时间",emptyText:"结束时间",width:90,format:"Y-m-d"});
		this.search_client=new ClientComboBox(Ext.apply({},{emptyText:"客户",width:100},ConfigConst.CRM.client));
		this.search_distributor=new ClientComboBox(Ext.apply({},{emptyText:"分销商",width:100},ConfigConst.CRM.distributor));
		
		this.grid.on("render",function(){
			this.grid.getTopToolbar().insert(10,["-",
		/*	{
				text : "批量审核",
				cls : "x-btn-text-icon",
				icon : "images/icon-png/save.png",
				handler : this.executeMulitCmd("batchAuditing"),
				scope : this
			},*/"-","时间:从",this.search_vdate1,"到",this.search_vdate2,this.search_distributor,this.search_client,"-",{text:"查询",handler:this.quickSearch,scope:this}]);
			
			this.grid.getTopToolbar().insertButton(11,{text : "打印",
			 iconCls:"print-icon",
			 menu:new Ext.menu.Menu({items:[{text:"发货/出库单",handler:this.printRecord(0),scope:this},{text:"回执单",handler:this.printRecord(1),scope:this},{text:"利润分析单",handler:this.printRecord(3),scope:this,hidden:true}]})
			 });	
			
				},this);
	},
	getStockOutcomePrice:function(params,callback,scope){
		if(params){
			Ext.Ajax.request({
				url:'stockOutcomePrice.ejf?cmd=getOutcomePrice',
				params:params,
				scope:this,
				success:function(res,opt){
					var data = Ext.decode(res.responseText);
					var activeEditor;
					if(this.editGrid.activeEditor){
						activeEditor = this.editGrid.activeEditor;
					}
					Ext.callback(callback,scope||this,[data]) ;
					if(activeEditor){
						(function(){
							// activeEditor 
						}).defer(100,this);	
					}
				}
			});
		}
	},//提示用戶該金額是进价金额
	priceRenderer:function(v,meta,r){
		if(r.get('isBuyPrice')){
			meta.attr = 'style="background:red;"';
		}
		return v;
	},
	getProductById:function(productId){
		var store = this.editGrid.store;
		var index = store.findBy(function(o){
			if(o.get('product')){
				if(o.get('product').id==productId){
					return true;
				}
			}
		},this);
		if(index>=0){
			return store.getAt(index);
		}
		return null;
	},
    autoLoadFieldMapping:function(){
        if(!this.groupHeaderCellConfig){
            var response=Ext.lib.Ajax.syncRequest("POST", "stockOutcome.ejf",{cmd:"getGroupFieldMapping"});
            var map=Ext.decode(response.conn.responseText);
            Ext.apply(BaseStockOutcomeWearBillPanel.prototype,map);
            Global.WearEditStoreMapping=map;
           
        }
    },   
	initComponent : function(){
		this.autoLoadFieldMapping();
		BaseStockOutcomeWearBillPanel.superclass.initComponent.call(this);
		this.addEvents('addBarcode');
	}
});






ProductWearGirdPanel=Ext.extend(Ext.grid.EditorGridPanel,{
	autoScroll:true,
	border:false,
	loadMask : true,
	showGroupHeader:true,
	editExtractField:[],
	girdStoreMapping:["id","product","price","num","sizeNums", "remark","location","color","depot","blockSn","encapNum","totalAmount","vdate", "totalAmount","salePrice","saleAmount"],
	autoCountNum:function(record){
		var product=record.get("product");
    	if(!product)return;
    	if(product.encaped){//按封装、手进行管理
    		record.set("num",(record.get("encapNum")?record.get("encapNum"):0)*(product.encapNum?product.encapNum:1));
    	}else if(product.sizes){//按尺码进行管理
    		var t=0;
    		for(var i=0;i<this.editExtractField.length;i++){
    			var s=record.get("size-"+i);
    			if(s)t+=s;
    		}
    		record.set("num",t);
    	}
	},
	sizeNumEditRender:function(field,editorColor){
		return function(v,meta,r){
			var p=r.get("product");
            var ret = false;
			if(p&&p.encaped){
			}
			else if(p && p.sizeGroup) {
			var ret = true;
			}
            if(!ret)meta.attr = 'style="background-color:'+Disco.Ext.Util.readOnlyGridCellColor+';"';  
			return v;
		}},
	encapNumEditRender:function(v,meta,r){	
		var p=r.get("product");
		if(p&&!p.encaped){
		meta.attr = 'style="background-color:'+Disco.Ext.Util.readOnlyGridCellColor+';"';	
		}
		return v;
	},
	numEditRender:function(v,meta,r){	
		var p=r.get("product");
		if(p&&(p.encaped||(p.sizes&&p.sizes.length))){
		meta.attr = 'style="background-color:'+Disco.Ext.Util.readOnlyGridCellColor+';"';	
		}
		return v;
	},
	getColumnModel:function(){
		var cms=[				
		 		 new Ext.grid.RowNumberer({header:"序号",dataIndex:"sequence",width:35}),
		 		 {header:"Id",dataIndex:"id",width:1,hidden:true,hideable:false},
		 		 {header:"货品编号",dataIndex:"product",width:140,renderer:Disco.Ext.Util.objectRender("stockNO",Disco.Ext.Util.readOnlyGridCellColor)},
		 		 {header:"货号",dataIndex:"product",width:100,renderer:Disco.Ext.Util.objectRender("sn",Disco.Ext.Util.readOnlyGridCellColor)},
		 		 {header:"产品名称",dataIndex:"product",width:160,renderer:Disco.Ext.Util.objectRender("title",Disco.Ext.Util.readOnlyGridCellColor)},
		 		 {header:"单位",dataIndex:"product",width:50,renderer:Disco.Ext.Util.objectRender("unit.title",Disco.Ext.Util.readOnlyGridCellColor)},
		 		 {header:"颜色",dataIndex:"color",width:80,renderer:Disco.Ext.Util.objectRender("title",Disco.Ext.Util.readOnlyGridCellColor)},
		 		 {header:"手",dataIndex:"encapNum",width:35,renderer:this.encapNumEditRender,summaryType: 'sum',summaryRenderer: function(v){return v?v.toFixed(0):"";}}
		 		 ]
		for(var i=0;i<this.editExtractField.length;i++){
			cms[cms.length]={header:this.editExtractField[i].title,groupHeader:true,dataIndex:"size-"+this.editExtractField[i].id,width:35,hidden:true,renderer:this.sizeNumEditRender("size-"+this.editExtractField[i].id),summaryType: 'sum',summaryRenderer: function(v){return v?v.toFixed(0):"";}};
		}
		Ext.each([
				 {header:"数量",dataIndex:"num",width:40,renderer:this.numEditRender,summaryType: 'sum',summaryRenderer: function(v){return v?v.toFixed(0):"";}}
				  ],function(o){
				 cms[cms.length]=o;
				 });
		return new Ext.grid.ColumnModel(cms);
	},
	initComponent : function(){
		this.store=new Ext.data.JsonStore({
			root : "result",
			totalProperty : "rowCount",
			fields:this.girdStoreMapping
		});
		if(this.showGroupHeader)
		this.plugins=[new Ext.ux.grid.GridSummary(),new Ext.ux.plugins.GroupHeaderCellGrid(this.groupHeaderCellConfig)];
		else
		this.plugins=[new Ext.ux.grid.GridSummary()];
		this.cm=this.getColumnModel();
		this.autoExpandColumn=this.getColumnModel().getColumnCount()-1;		
		ProductWearGirdPanel.superclass.initComponent.call(this);
		this.store.on("load",function(s,r,o){
			for (var i = 0; i < r.length; i++) {
				var p = r[i].get("product");
				if (p&&p.encaped) {
					var index = this.getColumnModel().findColumnIndex("encapNum");
					if (index >= 0)
						this.getColumnModel().setHidden(index, false);
				} else if (p&&p.sizeGroup) {
					this.store.getAt(i).set("encapNum","");
					this.getView().setGroupCellDisplay(p.sizeGroup.id, true);
					if(r[i].get("sizeNums")){
						for (var j = 0; j < r[i].get("sizeNums").length; j++) {
							this.store.getAt(i).set(
									"size-" + r[i].get("sizeNums")[j].groupIndex,
									r[i].get("sizeNums")[j].num);
						}
					}
				}
			}
		},this);
	}
});


/*(function(){
if(!Global.WearEditStoreMapping){
Ext.Ajax.request({url:"stockOutcome.ejf",params:{cmd:"getGroupFieldMapping"},callback :function(ops,success,response){
	var map=Ext.decode(response.responseText);
	Ext.apply(ProductWearGirdPanel.prototype,map);
},scope:this});
}else{
	Ext.apply(ProductWearGirdPanel.prototype,Global.WearEditStoreMapping);
}
}).defer(1000,Global)*/

if (!Global.WearEditStoreMapping) {
		Ext.Ajax.request({url:"stockOutcome.ejf",params:{cmd:"getGroupFieldMapping"},callback :function(ops,success,response){
		  var map=Ext.decode(response.responseText);
		  if(map && map.groupHeaderCellConfig){
		  	if(map.groupHeaderCellConfig.rows&&map.groupHeaderCellConfig.rows.length){
		  		for(var i=0;i<map.groupHeaderCellConfig.rows.length;i++){
		  			var temp=map.groupHeaderCellConfig.rows[i];
		  			if(temp.headers && temp.headers.length){
		  				for(var j=0;j<temp.headers.length;j++){
		  					if(typeof temp.headers[j] == "object"){
		  						temp.headers[j].toString=function(){return this.title};
		  					}
		  				}
		  			}
		  		}
		  	}
		  }
		  Global.WearEditStoreMapping = map;
		  Ext.apply(ProductWearGirdPanel.prototype,Global.WearEditStoreMapping);
		  Ext.apply(BaseStockOutcomeWearBillPanel.prototype,Global.WearEditStoreMapping);
		},scope:this});
}
//直接引用Global里面的
		
var ImageChooser = function(config){
	this.config = config;
}

ImageChooser.prototype = {
    setParams:function(v){
      this.configParams = v
    },
	show : function(el, callback){
		if(!this.win){
	 		  this.store = new Ext.data.JsonStore({
			    url: this.config.url,
			    root : 'data.pagelist.result',
			    fields: ['title', 'path'],
			    listeners: {
			    	'load': {fn:function(store,records){
			    			var imgs = this.win.body;
			    		    Ext.each(records,function(record){
			    		    	 imgs.createChild({tag:"img",src:record.get("path"),title:record.get("title"),onload:"javascript:drawImage(this,490,330)"})
			    		    },this);
			    		 this.carousel = new Ext.ux.Carousel(imgs,{hideNavigation: true,transitionType: 'fade',transitionDuration: 0.5});
			    			},
			    	scope:this}
			    }
			})
		  if(this.configParams){
		  	this.store.on("beforeload",function(){
			    	 	Ext.apply(this.store.baseParams,this.configParams);
			    	 	},this);
		  }
			var cfg = {
		    	title: '图片展示',
			    width:400,
			    height:400,
				modal: true,
				draggable:true, 
				bodyStyle:'background-color:#FFFFFF',
				closeAction: 'close',
				border: false,
				resizable:false,
				listeners:{"destroy":function(win){
					delete this.win;
				},scope:this},
				buttons: [{text:'上一个',handler:function(){this.carousel.prev()},scope:this},{text:'下一个',handler:function(){this.carousel.next()},scope:this},{
					text: '取消',
					handler: function(){
					  this.win.close();
					  delete this.win;
					  },
					scope: this
				}],
				keys: {
					key: 27, // Esc key
					handler: function(){ this.win.close();
					delete this.win;
					},
					scope: this
				}
			};
			Ext.apply(cfg, this.config);
		    this.win = new Ext.Window(cfg);
		}
		this.win.on('show',function(){
	    	 this.store.removeAll();
	    	 this.store.load();
	    },this);
        this.win.show(el);
	}
	
};

String.prototype.ellipse = function(maxLength){
    if(this.length > maxLength){
        return this.substr(0, maxLength-3) + '...';
    }
    return this;
};
/***
 * 重写ColumnModel isCellEditable
 * 	该方法主要用于判断该列是否可编辑！
 *  原始: return (this.config[colIndex].editable || (typeof this.config[colIndex].editable == "undefined" && this.config[colIndex].editor))? true: false;
 *  只能判断某一列,不能判断 "某行" 的某一列
 * */
Ext.override(Ext.grid.ColumnModel, {
	isCellEditable : function(colIndex, rowIndex) {
		var t = (this.config[colIndex].editable || (typeof this.config[colIndex].editable == "undefined" && this.config[colIndex].editor)) ? true : false;
		if (t===true && this.editCofigs) {
			if (this.editCofigs.containsKey(rowIndex)) {
				var row = this.editCofigs.get(rowIndex);
				if(row.cols.indexOf(colIndex)>=0){
					return false;
				}
			}
		}
		return t;
	}
})
/**
 * @method setCellEditable
 * 该方法主要用于启用 禁用 某行某列是否可编辑
 * rowIndex 行
 * colIndex 列
 * action 启用(true) 禁用(false)
 * editorGrid.setCellEditable(0,2);禁用
 * editorGrid.setCellEditable(0,2,true);启用
 */
Ext.apply(Ext.grid.EditorGridPanel.prototype, {
			_enableSetCellEditable:false,
//			postEditValue:function(value, originalValue, r, field){
//				this.fireEvent("afteredit", e);
//				return this.autoEncode && typeof value == 'string' ? Ext.util.Format.htmlEncode(value) : value;
//			},
			onEditComplete : function(ed, value, startValue){
	        this.editing = false;
	        this.activeEditor = null;
	        ed.un("specialkey", this.selModel.onEditorKey, this.selModel);
			var r = ed.record;
			
	        var field = this.colModel.getDataIndex(ed.col);
	        value = this.postEditValue(value, startValue, r, field);
	        var e = {
	                grid: this,
	                record: r,
	                field: field,
	                originalValue: startValue,
	                value: value,
	                row: ed.row,
	                column: ed.col,
	                cancel:false
	        };
            if(this.fireEvent("validateedit", e) !== false && !e.cancel){
                r.set(field, e.value);
                delete e.cancel;
                this.fireEvent("afteredit", e);
            }
	        this.view.focusCell(ed.row, ed.col);
    	},
		setCellEditable : function(rowIndex, colIndex, action) {
				if(!this._enableSetCellEditable){
					this._enableSetCellEditable=true;
					this.store.on('remove',function(s,r,i){
						this.colModel.editCofigs.removeKey(i);
					},this);
					this.store.on('load',function(s,r,i){
						this.colModel.editCofigs.clear();
					},this);
				}
				if (!this.colModel.editCofigs) {
					this.colModel.editCofigs = new Ext.util.MixedCollection(
							true, function(o) {
								return o.row;
							});
				}
				var contains = this.colModel.editCofigs.containsKey(rowIndex);
				if (!contains && !action) {
					this.colModel.editCofigs.add({
								row : rowIndex,
								cols:[colIndex]
							});
				}else if (contains && !action){
					 var row = this.colModel.editCofigs.get(rowIndex);
					 if(!row.cols)row.cols=[];
					 if(row.cols.indexOf(colIndex)<0){
					 	row.cols.push(colIndex);
					 }
				}else if (contains && action){
					 var row = this.colModel.editCofigs.get(rowIndex);
					 if(!row.cols)row.cols=[];
					 row.cols.remove(colIndex);
				}
			}
		});
Ext.override(Ext.Editor,{
		startEdit:function(el, value){
			if(this.editing){
           		 this.completeEdit();
       		}
	        this.boundEl = Ext.get(el);
	        var v = value !== undefined ? value : (this.boundEl.dom.innerHTML==='&nbsp;'?'':this.boundEl.dom.innerHTML);
	        if(!this.rendered){
	            this.render(this.parentEl || document.body);
	        }
	        if(this.fireEvent("beforestartedit", this, this.boundEl, v) === false){
	            return;
	        }
	        this.startValue = v;
	        this.field.setValue(v);
	        this.doAutoSize();
	        this.el.alignTo(this.boundEl, this.alignment);
	        this.editing = true;
	        this.show();
		}
	});
	
//服装项目中的年份
Disco.Ext.YearField = Ext.extend(Ext.form.ComboBox,{
	 mode : 'local',
	 triggerAction : 'all',
	 editable : false,
	 defaultValue:true,
	 initComponent : function(){
		 Disco.Ext.YearField.superclass.initComponent.call(this);
	     if(!this.store){
	        this.store = new Ext.data.SimpleStore({
	            fields: ['title', 'value'],
	            data : Global.theYear
	        });
	        this.displayField = 'title';
	        this.valueField = 'value';
	        
	        if(this.defaultValue){
	        	var currentServerTime = "$!{session.currentServerTime}";
		        if(currentServerTime!==""){
					var now = "$!{session.currentServerTime}";
		        }else{
					var dt = new Date();
					var now = dt.getFullYear();
		        }
		        this.setValue(now);
	        }
	    }
    }
})
Ext.reg('yearfield',Disco.Ext.YearField);

Ext.ChinaPagingToolbar =  Ext.extend(Ext.PagingToolbar,{
	onRender : function(ct, position){
	    Ext.PagingToolbar.superclass.onRender.call(this, ct, position);
	    this.first = this.addButton({
	        tooltip: this.firstText,
	        iconCls: "x-tbar-page-first",
	        text:"第一页",
	        disabled: true,
	        handler: this.onClick.createDelegate(this, ["first"])
	    });
	    this.prev = this.addButton({
	        tooltip: this.prevText,
	        iconCls: "x-tbar-page-prev",
	        disabled: true,
	        text:"前一页",
	        handler: this.onClick.createDelegate(this, ["prev"])
	    });
	    this.addSeparator();
	    this.add(this.beforePageText);
	    this.field = Ext.get(this.addDom({
	       tag: "input",
	       type: "text",
	       size: "3",
	       value: "1",
	       cls: "x-tbar-page-number"
	    }).el);
	    this.field.on("keydown", this.onPagingKeydown, this);
	    this.field.on("focus", function(){this.dom.select();});
	    this.afterTextEl = this.addText(String.format(this.afterPageText, 1));
	    this.field.setHeight(18);
	    this.addSeparator();
	    this.next = this.addButton({
	        tooltip: this.nextText,
	        iconCls: "x-tbar-page-next",
	        text:"后一页",
	        disabled: true,
	        handler: this.onClick.createDelegate(this, ["next"])
	    });
	    this.last = this.addButton({
	        tooltip: this.lastText,
	        iconCls: "x-tbar-page-last",
	        text:"最后页",
	        disabled: true,
	        handler: this.onClick.createDelegate(this, ["last"])
	    });
	    this.addSeparator();
	    this.loading = this.addButton({
	        tooltip: this.refreshText,
	        iconCls: "x-tbar-loading",
	        handler: this.onClick.createDelegate(this, ["refresh"])
	    });
	
	    if(this.displayInfo){
	        this.displayEl = Ext.fly(this.el.dom).createChild({cls:'x-paging-info'});
	    }
	    if(this.dsLoaded){
	        this.onLoad.apply(this, this.dsLoaded);
	    }
}
})

/**
* 表单全键盘导航功能
* index:可选参数，用于设定页面加载完成后默认获取焦点的表单项，支持索引号和id/dom类型参数传入。
*/

Disco.Ext.Util.setEnterNavigationKey=function(formpanel,focusItem,defaultFocus,editGrid,editColumn){
	var all = [];
		Ext.each(focusItem,function(c){
			  var formFiled = formpanel.form.findField(c);
			if(formFiled.isFormField&&!/hidden|labelfield/.test(formFiled.getXType())){
				all.push(formFiled);
			}
		},this);
    Ext.each(all,function(o,i,all){ //遍历并添加enter的监听
       o.on("specialkey",function(field,eventobject){
    	   if(eventobject.getKey()==Ext.EventObject.ENTER){
    		   if(field.allowBlank===false){
    			   if(field.getValue()){
    				   try{
    	    			   all[i+1].focus();
    	    			   }catch(e){
    	    			eventobject.keyCode=9
    	    			} 
    			   }else{
    				   return false;
    			   }
    		   }else{
    			   try{
	    			   all[i+1].focus();
	    			   }catch(e){
	    				eventobject.keyCode=9
	    			}  
    		   }
    		   if(editGrid){
    		   	 	if(i+1==all.length){ 
    	    		  var column = editGrid.getColumnModel().findColumnIndex(editColumn||"productSn");
    				  editGrid.startEditing(0,column);
    	    	   }
    		   }
    	   }
        },this);
    });
    try{
    	if(Ext.type(defaultFocus)=="string"){
    		formpanel.form.findField(defaultFocus).focus("",200);
    	}else if(Ext.type(defaultFocus)=="number"){
    		formpanel.form.findField(focusItem[defaultFocus-1]).focus("",200);
    	}
    }catch(e){}
}
superSearchWinOverride = {superSearchWin:function(width, height, title){
			var isNew = !Disco.Ext.CrudSearchWindow;			
			if (!this.searchPanel) {
				if (this.searchFP || this.searchFormPanel) {
					this.searchPanel = this.searchFP ? this.searchFP() : this
							.searchFormPanel();
					this.searchPanel.form.enterNavigationKey = true;
				}
			}
			if (!this.searchPanel)
				return null;// 如果没有定义searchFP或searchFormPanel，则返回
			var win=this.createGlobalWin("CrudSearchWindow",width,height,title,this.searchPanel,null,"searchPanel",[{
								id:"tb_search",
								text : "查询",
								handler : this.doSearch,
	                            iconCls : 'search',
	                            scope:this
							}, {
								text : "重置",
	                            iconCls : 'clean',
								handler : function() {
									Disco.Ext.CrudSearchWindow.getComponent(0).form
											.reset();
								}
							}, {
								text : "关闭",
								iconCls : 'delete',
								handler : function() {
									Disco.Ext.CrudSearchWindow.hide()
								}
							}]);
			if(isNew){
				var map = new Ext.KeyMap(win.el,{
				    key: Ext.EventObject.ENTER,
				    ctrl:true,
				    fn: this.doSearch
				});
			}											
			return win;				
		}}
Ext.override(Disco.Ext.CrudPanel,superSearchWinOverride)
Ext.override(Disco.Ext.CrudListPanel,superSearchWinOverride)

//会计期间及帐户初始化，当进入进销存系统时自动执行
StockAccountPeriodWindow=Ext.extend(Ext.Window,{
	id:"stockAccountPeriodWindow",
	title:"会计期间基本属性",
	baseUrl:"stockAccount.ejf",
	width:300,
	height:210,
	border:false,
	layout : "fit",
	modal:true,
	buttonAlign:"center",			
	load : function() {
		this.fp.form.load({
			waitMsg : "正在加载数据，请稍候...",
			waitTitle:"提示",
			url :this.baseUrl+ "?cmd=load",
			scope : this
		});
	},			
	save : function() {
		var id=this.fp.form.findField("id").getValue();
		this.fp.form.submit({
			waitMsg : "正在保存",
			waitTitle:"提示",
			url : this.baseUrl+"?cmd="+(id?"updatePeriod":"save"),
			method : 'POST',
			success : function(form,action) {
				Ext.MessageBox.alert("保存成功", "数据已经成功保存!", function() {
							var ret=action.result;
							if(!id && ret.data && ret.data.id){
								form.findField("id").setValue(ret.data.id);		
								form.submit({
									url : this.baseUrl+"?cmd=updatePeriod",
									success:function(){
										this.close();
									},
									scope:this
								});
							}				
							else {
							this.close();
							}
						}, this);
			},
			failure : function(form, action) {
				var msg = "";
				if (action.failureType == Ext.form.Action.SERVER_INVALID) {
					for (var p in action.result.errors) {
						msg += action.result.errors[p] + ";";
					}
				}
				Ext.MessageBox.alert("$!{lang.get('Save failed')}", msg);
			},
			scope : this
		});
	},
	createForm:function(){
	var formPanel=new Ext.form.FormPanel({
		frame:true,
		labelWidth:70,
		labelAlign:'right',
		items:[{
			xtype:'fieldset',
			title:'',
			autoHeight:true,
			items:[{xtype:"hidden",name:"id"},
	{xtype:"textfield",fieldLabel:'公司名称',name:'title',allowBlank:false,anchor:"-1"},
	{fieldLabel:'启用时间',name:'createTime',xtype:"datefield",format : "Y-m-d",allowBlank:false},
	Ext.apply({},{fieldLabel : '按月结账',
				name : 'monthPeriod',
				hiddenName : "monthPeriod",
				width:100,
				value : true},ConfigConst.BASE.yesNo),
	{fieldLabel:'起始日期',name:'settlementDay',width:100,xtype:"numberfield"}]
		}]
	});
		return formPanel;
    },
  	initComponent : function(){
  	this.buttons = [{
							text : "保存",
							handler : this.save,
							scope : this
						}, {
							text : "取消",
							handler : this.close,
							scope : this
						}];
	StockAccountPeriodWindow.superclass.initComponent.call(this);
	this.fp=this.createForm();
	this.add(this.fp);
	this.on("render", this.load, this);
}     
});


Global.CommonFunction = new Ext.util.MixedCollection({},function(o){
    return o.inputValue || o.value;
});

//常用功能选择项
Global.CommonFunction.addAll([
    {text:'订货会订单',boxLabel:'订货会订单', inputValue:1},
    {text:'补货订单',boxLabel:'补货订单', inputValue:2},
    {text:'货品分货',boxLabel:'货品分货', inputValue:3},
    {text:'配货单',boxLabel:'配货单', inputValue:4},
    {text:'货品调价',boxLabel:'货品调价', inputValue:5},
    {text:'采购入库',boxLabel:'采购入库', inputValue:6},
    {text:'货品退厂',boxLabel:'货品退厂', inputValue:7},
    {text:'销售出库',boxLabel:'销售出库', inputValue:8},
    {text:'即时库存',boxLabel:'即时库存', inputValue:9},
    {text:'调拨单',boxLabel:'调拨单', inputValue:10},
    {text:'零售统计',boxLabel:'零售统计', inputValue:11},
    {text:'零售收款对账',boxLabel:'零售收款对账', inputValue:12},
    {text:'客户往来账',boxLabel:'客户往来账', inputValue:13},
    {text:'供应商往来帐',boxLabel:'供应商往来帐', inputValue:14},
    {text:'经营历程',boxLabel:'经营历程', inputValue:15}
]);
Global.CommonFunctionDefault = [9,5,8,6,15]; 
//常用功能菜单图标配置
Global.CommonFunctionIcons = {
    1:{icon:'commonFunction_saleMettingOrder',title:'订货会订单',appClass:"SaleMettingOrderPanel",script : "wear/SaleMettingOrderPanel.js"},
    2:{icon:'commonFunction_renewOrder',title:'补货订单',appClass : "RenewOrderPanel",script:"wear/RenewOrderPanel.js"},
    3:{icon:'commonFunction_distributeProductOrder',title : "货品分货",appClass:"DistributeProductOrderPanel",script : "wear/DistributeProductOrderPanel.js"},
    4:{icon:'commonFunction_wearOrders',title:'配货单',appClass:"WearOrdersPanel",script : "wear/WearOrdersPanel.js"},
    5:{icon:'commonFunction_adjustmentPrice',title:'调价单',appClass:"AdjustmentPricePanel",script:"wear/AdjustmentPricePanel.js"},
    6:{icon:'commonFunction_stockincome',title:'采购入库',appClass : "StockIncomePurchasePanel",script:"stock/StockIncomePurchasePanel.js"},
    7:{icon:'commonFunction_stockStockRetreat',title:'货品退厂',appClass:"StockRetreatPanel",script:"stock/StockRetreatPanel.js"},
    8:{icon:'commonFunction_stockoutcome',title : "销售出库单",appClass:"StockOutcomeOrdersPanel",script : "stock/StockOutcomeOrdersPanel.js"},
    9:{icon:'commonFunction_productStock',title : "即时库存",appClass:"ProductStockPanel",script : "/stock/ProductStockPanel.js"},
    10:{icon:'commonFunction_stockTransfer',title:"仓库调拨单",appClass : "StockTransferPanel",script:"stock/StockTransferPanel.js"},
    11:{icon:'commonFunction_retailBillItemDetailTotal',title : "零售统计",appClass:"RetailBillItemDetailTotalPanel",script : "wear/RetailBillItemDetailPanel.js",inWin:true},
    12:{icon:'commonFunction_retailBillInfoChat',title:"零售收款对账报表",appClass:"RetailBillInfoChat",script:"wear/chart/RetailBillInfoChat.js",inWin:true},
    13:{icon:'commonFunction_clientAccountDetailChartMain',title : "客户明细账报表",appClass:"ClientAccountDetailChartMainPanel",script:"wear/chart/ClientAccountDetailChartPanel.js"},
    14:{icon:'commonFunction_supplierAccountDetailChartMain',title: "供应商明细账报表",appClass:"SupplierAccountDetailChartMainPanel",script:"wear/chart/SupplierAccountDetailChartPanel.js"},
    15:{icon:'commonFunction_businessProcess',title:'经营历程'}
};

SystemConfigWindow=Ext.extend(Ext.Window,{
    title : '系统设置',
    modal:true,
    iconCls:'icon-password',
    width : 580,            
    autoHeight:true,    
    desktopTypes:[["默认主页(Portal)","menu"],["视窗桌面模式","menu-desktop"],["进销存主界面","menu-stock"],["客户关系主界面","menu-crm"],["财务管理主界面","menu-crm"],["人力资源主界面","menu-hr"],["服装业主界面","menu-wear"]],
    styleDatas:[["默认风格","default"],["银白风格","xtheme-gray"],["紫色风格","xtheme-purple"],["绿色风格","xtheme-green"],["灰色风格","xtheme-darkgray"],["黑色风格","xtheme-black"],["深蓝风格","xtheme-slate"]],
    appTypes:[["禁用IFrame(OPOA模式)",false],["启用IFrame(IFrame模式)",true]],
    tabTypes:[["多窗体",false],["单窗体",true]],
    defaults : {border : false},
    buttonAlign : 'center', 
    createFormPanel :function() {
        return new Ext.form.FormPanel({
            bodyStyle : 'padding-top:6px',
            defaultType : 'textfield',
            labelAlign : 'right',
            labelWidth : 70,
            labelPad : 0,
            frame : true,
            defaults : {
                width : 158,
                selectOnFocus:true
            },
            items : [{
                xtype:'fieldset',
                title:'窗口設置',
                anchor:'100% *',
                autoHeight:true,
                items:[
                    Disco.Ext.Util.oneColumnPanelBuild(
                        Disco.Ext.Util.buildCombox('homePage','首页设置',this.desktopTypes,main.homeMenu),
                        Disco.Ext.Util.buildCombox('tabType','窗体支持',this.tabTypes,main.singleTabMode)  
                    ),
                    Disco.Ext.Util.oneColumnPanelBuild(
                        Disco.Ext.Util.buildCombox('appType','IFrame支持',this.appTypes,Global.iframe),
                        Disco.Ext.Util.buildCombox('style','皮肤',this.styleDatas,main.theStyle||"default")  
                    ),
                    Disco.Ext.Util.oneColumnPanelBuild(
                        Ext.apply({},{fieldLabel:'开取动画',name:'enableAnimate',hiddenName:'enableAnimate',allowBlank:false,value:main.enableAnimate},ConfigConst.BASE.yesNo),
                        {
                            name : 'maxTab',
                            xtype:'textfield',
                            fieldLabel : '最大Tab数',
                            value:main.maxTabs
                        }   
                    )
                ]
            },{
                xtype:'fieldset',
                title:'常用功能',
                anchor:'100% *',
                autoHeight:true,
                items:new Ext.form.CheckboxGroup({
                    columns:3,
                    vertical:true,
                    hideLabel:true,
                    value:main.commonFunction,
                    name:'commonFunction',
                    defaults:{hideLabel:true},
                    items:Global.CommonFunction.items
                })
            }]
        });
    },                  
    save:function() {
            var appType=this.fp.form.findField("appType").getValue();
            var tabType=this.fp.form.findField("tabType").getValue();
            var maxTab=this.fp.form.findField("maxTab").getValue();
            var homeMenu=this.fp.form.findField("homePage").getValue();
            var commonFunction=this.fp.form.findField("commonFunction").getValue();
            var enableAnimate=this.fp.form.findField("enableAnimate").getValue();
            var style=this.fp.form.findField("style").getValue();
            if(typeof main !== "undefined" && main){
            main.iframe=Global.iframe=appType;
            main.singleTabMode=tabType;
            main.maxTabs=maxTab;
            main.enableAnimate=enableAnimate;   
            main.homeMenu=homeMenu;
            main.theStyle=style;
            main.commonFunction=commonFunction;
            main.savePersonality(function(){
                    var isDirty=this.fp.form.findField("tabType").isDirty()||this.fp.form.findField("homePage").isDirty();
                    this.close();
                    if(isDirty){
                    Ext.Msg.confirm("提示","IFrame支持或系统主页已经修改,只有重新加载页面新的设置才会生效,是否要重新加载系统?",function(button){
                        if(button=="yes")window.location.reload();
                    })}
                    if(this.fp.form.findField("style").isDirty()){
                        if(main.changeSkin)main.changeSkin(style);
                    }
                    main.buildCommonFunctionIcon(main);
                }.createDelegate(this));
            
            /*
            Ext.Msg.alert("提示","你的设置已经成功！",function(){
                if(main.singleTabMode){ 
                    Ext.Msg.confirm("提示","你使用了单Tab模式，是否关闭多余的面板？",function(button){
                        if(button=="yes")main.closeAll(main.getActiveTab());
                    });
                }
                
                },this);*/  
            }
    },
    initComponent : function(){
        this.keys={
            key: Ext.EventObject.ENTER,
            fn: this.save,
            scope: this};
        SystemConfigWindow.superclass.initComponent.call(this);
        this.fp = this.createFormPanel();
        this.add(this.fp);
        this.addButton('保存',this.save,this);
        this.addButton('取消', function(){this.close();},this);
     }  
 });

/*
 * 改进
 * */
Disco.Ext.CacheFilter.add('cache_employee_store',function(o,p){
	var searchKeys = ['name','trueName'];
	var objs = o;
	if(p['searchType']=='advancedSearch'){
		Ext.del(p,'pageSize','searchType','limit');
		return o.filterBy(function(obj){
			var isFilter = true;
			if(!Ext.isEmpty(p['inputEndTime']) && !Ext.isEmpty(p['inputStartTime'])){
				var startTime =Date.parseDate(p['inputStartTime'],'Y-m-d');
				var endTime =Date.parseDate(p['inputEndTime'],'Y-m-d');
				if(obj['registerTime']>=startTime && obj['registerTime']<=endTime){
					isFilter = true;
				}else{
					isFilter =false;
				}
				if(!isFilter)return;
			}
			if(p['deptId']){
				if(!obj['dept'] || (obj['dept']['id']!=p['deptId'])){
					isFilter=false;	
				}
				if(!isFilter)return;
			}
			if(p['trueName']){
			 	isFilter = Disco.Ext.CacheFilter.firstSearch(obj['trueName'],p['trueName']);
			 	if(!isFilter)return;
			}
			if(p['name']){
				isFilter = Disco.Ext.CacheFilter.firstSearch(obj['name'],p['name']);
				if(!isFilter)return;
			}
			if(p['email']){
				isFilter = Disco.Ext.CacheFilter.firstSearch(obj['email'],p['email']);
				if(!isFilter)return false;
			}
			if(p['tel']){
				isFilter =	Disco.Ext.CacheFilter.firstSearch(obj['tel'],p['tel']);
				if(!isFilter)return false;
			}
			if(p['sn']){
				isFilter =	Disco.Ext.CacheFilter.firstSearch(obj['sn'],p['sn']);
				if(!isFilter)return false;
			}
			return obj;
		},this);
	}else if((p['searchType'] == 'simple') && p['searchKey']){
		var objs = new Ext.util.MixedCollection();
		var val = p['searchKey']; 
		Ext.each(searchKeys,function(key){
			objs.addAll(o.filter(key,val).items);
		});
		return objs;
	}else if(p['deptId']){
		return o.filterBy(function(obj){
			if(!obj['dept'] || (obj['dept']['id']!=p['deptId'])){
				return false;	
			}
			return obj;
		});
	}
	return objs;
});

function pos(){
	var posPanel = main.getComponent('PosPanel');
	if(!posPanel){
		var node ={text:"POS机",attributes:{
			appClass:"PosPanel",
			script : "wear/PosPanel.js"
		}};
		Global.openExtAppNode(node, null);
	}else{
		var contentPanel = posPanel.getComponent(0);
		contentPanel.service.create();
	}
}	