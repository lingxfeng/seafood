//采购入库单
StockIncomePurchasePanel=Ext.extend(BaseStockOutcomeWearBillPanel,{
	printData:true,	   		
	id:"stockIncomePurchasePanel",
	//baseQueryParameter:{types:0,vdate3:new Date().format("Y-m-d"),defaultStatus:0},
	baseQueryParameter:{types:0,vdate3:new Date().format("Y-m-d")},
	viewWin:{width:Ext.getBody().dom.offsetWidth-20,height:Ext.getBody().dom.offsetHeight-20,title:"采购入库单详情"},
	addGridHotKey:true,
	types:0,
	baseUrl:"stockIncome.ejf",
	snType:"StockIncomePurchase",
	pitchProductSn:true,
	editGridViewConfig:{
    	//forceFit: true,
    	getRowClass:function(r){
			if(this.grid.store.productSn){
				var sn=r.data.productSn;
				if(sn.toUpperCase().indexOf(this.grid.store.productSn)>=0){
					return "x-grid3-row-selected";
				}
			}
		}
	},
	searchWin : {
		width : 550,
		height : 250,
		title : "高级查询"
	},
	formFocus:function(){
		//Disco.Ext.Util.setEnterNavigationKey(this.fp,["sn2","supplier","dept","depot","remark"],1,this.editGrid);
		this.fp.findSomeThing("sn2").focus("",100);
	},
	onEdit:function(ret){
		StockIncomePurchasePanel.superclass.onEdit.call(this,ret);
		if(ret){
			if(this.fp.findSomeThing("incomeSource").getValue()==1){
				this.editGridCanEdit=false;
				this.fp.findSomeThing("supplier").el.addClass("x-item-disabled");
				this.fp.findSomeThing("supplier").setEditable(false);
			}else{
				this.editGridCanEdit=true;
				this.fp.findSomeThing("supplier").setEditable(true);
			}
			if(this.fp.findSomeThing("supplier").getValue())
				this.productEditor.store.baseParams={client:this.fp.findSomeThing("supplier").getValue()};
			#if($!pu.showDistributors())
			var record = this.grid.getSelectionModel().getSelected();
			this.fp.findSomeThing("distributor").setValue(record.data.company);
			#end
		}
		this.fp.form.clearDirty();
	},
	/**
	autoCountNum:function(record,e){
		var product=record.get("product");
    	if(!product)return;
    	if(product.encaped){//按封装、手进行管理
    		if(e&&e.field=='encapNum'){
				if(record.get("encapNum"))    			
    		   		record.set("num",(record.get("encapNum")?record.get("encapNum"):0)*(product.encapNum?product.encapNum:1));
    		   	else
    		   	    record.set("num",null);
    		}
    	}else if(product.sizes && (product.attributeType==0||product.attributeType==2)){//按尺码进行管理
    		var t=0;
    		for(var i=0;i<this.editExtractField.length;i++){
    			var s=record.get("size-"+i);
    			if(s)t+=parseFloat(s);
    		}
    		record.set("num",t);
    	}
	},
	*/
	autoCountData:function(record,e){
    	this.autoCountNum(record,e);
        var p = record.get("price");
        var n = record.get("num");       
        var total = p && n ? p * n : 0;       
        record.set("saleAmount",parseFloat(total));
        record.set("totalAmount",parseFloat(total));
    },
    
    numEditRender:function(v,meta,r){
		var p=r.get("product");
		if(p&&(p.sizes&&p.sizes.length)){
			meta.attr = 'style="background-color:'+Disco.Ext.Util.readOnlyGridCellColor+';"';	
		}
		return v;
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
			viewConfig:this.editGridViewConfig,
			//autoExpandColumn:colM.getColumnCount()-1,
			plugins:[new Ext.ux.grid.GridSummary(),new Ext.ux.plugins.GroupHeaderCellGrid(this.groupHeaderCellConfig)]
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
	                    {xtype:"hidden",name:"id"},
	                    Disco.Ext.Util.columnPanelBuild(
	                    {columnWidth:.2,items:{xtype:"labelfield",fieldLabel:'入库单号',name:'sn'}},
	                    {columnWidth:.2,items:{xtype:"labelfield",fieldLabel:'手工单号',name:'sn2'}},
					 {columnWidth:.2,items:{xtype:"labelfield",fieldLabel:'供货商',name:'supplier',renderer:this.objectRender("name")}},
					 {columnWidth:.2,items:{xtype:"labelfield",fieldLabel:'业务时间',name:'vdate',anchor:"-20",renderer:this.dateRender("Y-m-d")}},
					 {columnWidth:.2,anchor:"-20",items:{xtype:"labelfield",fieldLabel:'部门',name:'dept',renderer:this.objectRender("title")}}	
						),
					Disco.Ext.Util.columnPanelBuild({columnWidth:.2,items:{xtype:"labelfield",fieldLabel:'仓库',name:'depot',renderer:this.objectRender("title")}},{columnWidth:.8,items:{xtype:"labelfield",anchor:"-20",fieldLabel:'备注',name:'remark'}})
	                    ]
			},
			{
				xtype:'panel',
				layout:"fit",
				items:this.viewGrid
			},{
				xtype:'fieldset',
				height:50,
				border:false,
				items : [
				Disco.Ext.Util.oneColumnPanelBuild(
					{
					xtype : "labelfield",
					fieldLabel : '制单',
					name : "inputUser",
					value : {trueName:"$!{session.EASYJF_SECURITY_USER.trueName}"},
					renderer:this.objectRender("trueName")
				},{
					xtype : "labelfield",
					fieldLabel : '审核',
					name : "auditor",
					renderer:this.objectRender("trueName")
				}, {
					xtype : "labelfield",
					fieldLabel : '主管',
					name : "keeper",
					renderer:this.objectRender("trueName"),
					listeners:{render:function(c){
					var height=this.viewPanel.el.getBox().height-(this.viewPanel.getComponent(0).el.getBox().height+this.viewPanel.getComponent(2).el.getBox().height+15);
					this.viewGrid.setHeight(height);
				},scope:this}
				})]
			}
			]
		});
		return formPanel;
   	},
	createForm:function(){
		this.depotLoader=new Ext.tree.TreeLoader({
			url : "depot.ejf?cmd=getDepotTree&pageSize=-1&treeData=true&all=true",
			listeners:{scope:this,"beforeload":function(){
				#if($!pu.showDistributors())
			 	this.depotLoader.baseParams["company"] = this.fp.findSomeThing("distributor").getValue();
			 	#end
			}}
		});
		this.deptLoader=new Ext.tree.TreeLoader({
			url : "department.ejf?cmd=getTree&pageSize=-1&treeData=true&all=true",
			listeners:{scope:this,"beforeload":function(){
				#if($!pu.showDistributors())
			 	this.deptLoader.baseParams["company"] = this.fp.findSomeThing("distributor").getValue();
			 	#end
			}}
		});
		this.createEditGrid();	
		var formPanel=new Ext.form.FormPanel({
			//frame:true,
			labelWidth:60,
			labelAlign:'right',
			tbar : this.createFormToolBar(),
			enterNavigationKey:true,
			navigationSequences:["sn2","supplier","dept","depot","remark",this.editGrid.id],
			layout:"border",
			items:[{region:"center",layout:"anchor",items:[{
				xtype:'panel',
				//title:'基本信息',
				style:"margin-bottom:5px;margin-top:10px",
				border:false,
				height:80,
				layout:"form",
				items:[
	                   {xtype:"hidden",name:"id"},
	                   {xtype:"hidden",name:"types"},
	             		{xtype:"hidden",name:"incomeSource"},
	                    Disco.Ext.Util.columnPanelBuild(
	                    {columnWidth:.2,items:{fieldLabel:'入库单号',name:'sn',allowBlank:false,readOnly:true,anchor:"-1"}},
	                    {columnWidth:.2,items:{fieldLabel:'手工单号',name:'sn2',anchor:"-1"}},
	                    {columnWidth:.2,items:{xtype:"datefield",fieldLabel:'业务时间',name:'vdate',format:"Y-m-d",anchor:"-1", allowBlank:false,value:new Date()}},
	                    {columnWidth:.2,items:Ext.apply({},{
	                    	allowBlank:false,
	                    	loadBySn:'autocompleteList&showSystemDis=true',
	                    	listeners:{
	                    		scope:this,
	                    		blur:function(){
		                    		if(!this.fp.findSomeThing("supplier").getValue()){
		                    			this.fp.findSomeThing("supplier").setValue("");
		                    			this.fp.findSomeThing("supplier").focus();
		                    		}
	                    		}.createDelegate(this),
	                    		keypress:function(c,e){
		                        		if(e.getKey()!=13)c.value=null;
		                        	},
	                    		"select":function(c,r){
	                    				this.productEditor.store.baseParams={client:r.get("id"),pageSize:15};
	                    				if(this.editGrid.store.getCount()>0){
	                     					this.editGrid.store.removeAll();
	                     					if(this.colorStore) this.colorStore.clear();
	                     					this.autoAddLastRow();
	                     				}}},
							 	scope:this},ConfigConst.CRM.supplier)}
					  #if($!pu.showDistributors())
						,{columnWidth:.2,items:Ext.apply({},{valueField:"company",fieldLabel:"加盟店",comboBlank:false,allowBlank:false,listeners:{
							scope:this,
							select:function(c,r){
									this.fp.findSomeThing("depot").setValue("");
								if(this.fp.findSomeThing("depot").tree.root.isLoaded()){
									this.fp.findSomeThing("depot").tree.root.reload();
								}
									this.fp.findSomeThing("dept").setValue("");
								if(this.fp.findSomeThing("dept").tree.root.isLoaded()){
									this.fp.findSomeThing("dept").tree.root.reload();
								}
									this.fp.findSomeThing("keeper").setValue("");
								if(this.fp.findSomeThing("keeper").store.getCount()>0){
									this.fp.findSomeThing("keeper").store.reload();
								}
									this.fp.findSomeThing("recipient").setValue("");
								if(this.fp.findSomeThing("recipient").store.getCount()>0){
									//this.fp.findSomeThing("recipient").store.reload();
								}
							}
						}},ConfigConst.CRM.distributor)}
						#end
					),
					Disco.Ext.Util.columnPanelBuild(
					 {columnWidth:.2,items:{xtype : "treecombo",
							fieldLabel : "部门",
							name : "dept",
							leafOnly:true,
							readOnly:false,
							hiddenName : "dept",
							displayField : "title",
							allowBlank:false,
							valueField:"id",
							width : 110,
							tree : new Ext.tree.TreePanel({
									autoScroll:true,
									root: new Ext.tree.AsyncTreeNode({
											id : "root",
											text : "所有部门",
											icon : "images/system/root.gif",
											loader : this.deptLoader
										})})
							}},
						{columnWidth:.2,items:{xtype : "treecombo",
							fieldLabel : "仓库",
							name : "depot",
							leafOnly:true,
							readOnly:false,
							hiddenName : "depot",
							allowBlank:false,
							displayField : "title",
							valueField:"id",
							width : 110,
							tree : new Ext.tree.TreePanel({
									autoScroll:true,
									root: new Ext.tree.AsyncTreeNode({
											id : "root",
											text : "所有仓库",
											icon : "images/system/root.gif",
											loader : this.depotLoader
										})})
						}},					 	
					 {columnWidth:.6,items:{xtype:"textfield",anchor:"-2",fieldLabel:'备注',name:'remark'}}		 	
					),
					Disco.Ext.Util.oneColumnPanelBuild(Ext.apply({}, {
					fieldLabel : '发货人',
					name:"keeper",
					hiddenName:"keeper"
					#if($!pu.showDistributors())
					,
					listeners:{
						scope:this,
						render:function(){
							this.fp.findSomeThing("keeper").store.on("beforeload",function(){this.fp.findSomeThing("keeper").store.baseParams["company"]=this.fp.findSomeThing("distributor").getValue();},this);
						}
					}
					#end
				}, ConfigConst.CRM.seller), Ext.apply({}, {
					fieldLabel : '收货人',
					name:"recipient",
					hiddenName:"recipient"
					#if($!pu.showDistributors())
					,
					listeners:{
						scope:this,
						render:function(){
							this.fp.findSomeThing("recipient").store.on("beforeload",function(){this.fp.findSomeThing("recipient").store.baseParams["company"]=this.fp.findSomeThing("distributor").getValue();},this);
						}
					}
					#end
				}, ConfigConst.CRM.seller), {
					xtype : "labelfield",
					fieldLabel : '制单',
					name : "inputUser",
					value : {trueName:"$!{session.EASYJF_SECURITY_USER.trueName}"},
					renderer:this.objectRender("trueName")
				}, {
					xtype : "labelfield",
					fieldLabel : '审核',
					name : "auditor",
					renderer : this.objectRender("trueName")
				})
					]
			},
			{
				xtype:'panel',
				anchor:"* -95",
				layout:"fit",
				items:this.editGrid
			}]}
			]
		});	
		return formPanel;
    },
    searchFormPanel : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : true,
			labelWidth : 80,
			labelAlign : "right",
			navigationSequences:["productSn","status","snStart","snEnd","vdate1","vdate2","supplier","deptId","confirm"],
			items : [{
				xtype : "fieldset",
				title : "查询条件",
				autoHeight:true,
				layout : 'column',
				items : [{
							columnWidth : .50,
							layout : 'form',
							defaultType : 'textfield',
							items : [{
										fieldLabel : "货&nbsp;&nbsp;&nbsp;&nbsp;号",
										name : "productSn",
										anchor : '90%'
									},{
										xtype : "hidden",
									    name:"advanced",
									    value:true
									}, {
									    fieldLabel : "订单号(始)",
										name : "snStart",
										anchor : '90%'
									}, {
										fieldLabel : "制单日期(始)",
										name : "vdate1",
										anchor : '90%',
										xtype : 'datefield',
										format : 'Y-m-d'
									},Ext.apply({},{emptyText:"--供应商--",allowBlank:false,width:200,anchor : '90%'},ConfigConst.CRM.supplier),{
										xtype : "combo",
										anchor : '90%',
										name : "confirm",
										hiddenName : "confirm",
										fieldLabel : "确认状态",
										displayField : "title",
										valueField : "value",
										value : 0,
										store : new Ext.data.SimpleStore({
													fields : ['title', 'value'],
													data : [["全部", 0], ["已确认", -1],["未确认", 1]]
												}),
										disableChoice : true,
										editable : false,
										mode : 'local',
										triggerAction : 'all',
										emptyText : '请选择...'
									}
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
											data : [["全部", 0], ["未审核", -1],["已审核", 1]]
										}),
								disableChoice : true,
								editable : false,
								mode : 'local',
								triggerAction : 'all',
								emptyText : '请选择...'
									},{
									    fieldLabel : "订单号(末)",
										name : "snEnd",
										anchor : '90%'
									},{
										fieldLabel : "制单日期(末)",
										name : "vdate2",
										anchor : '90%',
										xtype : 'datefield',
										format : 'Y-m-d'
									},{
										xtype : "treecombo",
										fieldLabel : "采购部门",
										displayField : "title",
										name : "deptId",
										hiddenName : "deptId",
										anchor : '90%',
										tree : new Ext.tree.TreePanel({
											root : new Ext.tree.AsyncTreeNode({
												id : "root",
												text : "选择部门",
												expanded : true,
												loader : Global.departmentLoader
											})
										})
									}
									#if($!pu.showDistributors())
									,Ext.apply({},{name:"company",hiddenName:"company",valueField:"company",fieldLabel:"所属店",anchor:"90%",comboBlank:false,allowBlank:false},ConfigConst.CRM.distributor),
									#end
							]
						}]
			}]
		});
		return formPanel;
	},
	/**
    baseEditGridListeners:{
			beforeedit : function(e) {
				var p=e.record.get("product");
				if(e.field!="productSn" && !p.id){
					return false;
				}
				if(e.field=="color"){
					if(this.colorStore && this.colorStore.get(p.id)){
			    		var filterout=this.colorStore.get(p.id);
				    	var colors=[];
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
				}else if(e.field=="encapNum" || e.field=="num"){
					if(!p.encaped && p.attributeType == 2) {
						return false;
					}
				}
				else if(e.field.indexOf("size-")>=0){
					if(p.encaped || !p.sizes.length)return false;
				 	else if(p.sizes.length>0){
				 		return this.beforeEditSizeType(p,e);
				 		} 
					else {
						var ret=false;
						if(p.sizeGroup)ret=true;
						return ret;
					}
				}
			},
			validateedit:function(e){
				if(e.field=="color"){
					var p=e.record.get("product");
					if(e.value){
						var product=e.record.get("product");
						if(product && this.colorStore.get(product.id) && p.colors.length>this.colorStore.get(p.id).length){
							this.colorStore.get(product.id).remove(e.record.get("color").value);
						}
					}
				}
			},
			afteredit : function(e) {
				if(e.field=="color"){
					if(e.value){
						var product=e.record.get("product");
						if(product){
							if(this.colorStore.get(product.id)){
								this.colorStore.get(product.id).push(e.record.get("color").value);
							}else{
								this.colorStore.add(product.id,[e.record.get("color").value]);
							}
						}
					}
					if(this.checkStock)
						this.doColorCheckStockNum(e);
				}
				
				var cm = e.grid.getColumnModel();
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
				}
				var s="price,num,salePrice,encapNum";
				if (s.indexOf(e.field)>=0 ||e.field.indexOf("size-")>=0) {// 计算合计
					this.autoCountData(e.record,e);
					if(this.checkStock)this.doColorCheckStockNum(e);
				} 
				if(e.record.isModified){
				 this.fp.getTopToolbar().items.get("tb_audit").disable();
				}
				if(this.afterEdit) this.afterEdit(e);
				if(e.field!='productSn')this.firstColumn(e.row,e.field,e.record);
			},
			cellcontextmenu:function(g,rowIndex,celIndex,e){
				var o=this.editGridMenu.items.get("menu_remove");
				if(o)o.enable();
				g.getSelectionModel().select(rowIndex,celIndex);
				this.editGridMenu.showAt(e.getPoint());
				e.preventDefault();
			},
			contextmenu:function(e){
				var o=this.editGridMenu.items.get("menu_remove");
				if(o && !this.editGrid.getSelectionModel().getSelectedCell())o.disable();
				this.editGridMenu.showAt(e.getPoint());
				e.preventDefault();
			 }
		},
		*/
 	getEditColumnModel:function(){
		this.createGridEditor();
		this.productEditor.on("focus",function(s){
			if(!this.fp.findSomeThing("supplier").getValue()){
				Ext.Msg.alert("提示","请先选择供应商！");
			}
		},this);
		var cms=[				
		 		 new Ext.grid.RowNumberer({header:"序号",dataIndex:"sequence",width:35}),
		 		 {header:"Id",dataIndex:"id",width:1,hidden:true,hideable:false},
		 		 {header:"产品",dataIndex:"product",width:0,hidden:true,hideable:false},
		 		 {header:"货号",dataIndex:"productSn",sortable:true,width:120,editor:this.productEditor,summaryType: 'count',summaryRenderer: function(v){return "合计("+v+")";}},
		 		 {header:"货品编号",dataIndex:"stockNO",sortable:true,width:80,hidden:true,renderer:this.readOnlyRender()},
		 		 {header:"产品名称",dataIndex:"productTitle",width:80,renderer:this.readOnlyRender()},
		 		 {header:"款型",dataIndex:"style",width:80,renderer:this.objectRender("title",Disco.Ext.Util.readOnlyGridCellColor)},
		 		 {header:"单位",dataIndex:"unit",width:50,renderer:this.objectRender("title",Disco.Ext.Util.readOnlyGridCellColor)},
		 		 {header:"颜色",dataIndex:"color",sortable:true,width:80,editor:this.colorEditor,renderer:Disco.Ext.Util.comboxRender},
		 		 {header:"手",dataIndex:"encapNum",hidden:true,width:35,editor:new Ext.form.NumberField({selectOnFocus:true}),renderer:this.encapNumEditRender,summaryType: 'sum',summaryRenderer: function(v){return v?v.toFixed(0):"";}}
		 		 ]
		for(var i=0;i<this.editExtractField.length;i++){
			cms[cms.length]={header:this.editExtractField[i].title,groupHeader:true,dataIndex:"size-"+this.editExtractField[i].id,width:35,hidden:true,editor:new Ext.form.TextField({selectOnFocus:true}),renderer:this.sizeNumEditRender("size-"+this.editExtractField[i].id),summaryType: 'sum',summaryRenderer: function(v){return v?v.toFixed(0):"";}};
		}
		Ext.each([
				 {header:"数量",dataIndex:"num",sortable:true,width:40,editor:new Ext.form.NumberField({selectOnFocus:true}),renderer:this.numEditRender,summaryType: 'sum',summaryRenderer: function(v){return v;}},
				 #if($!pu.showPrice())
				 {header:"单价",dataIndex:"price",width:60,editor:new Ext.form.NumberField({selectOnFocus:true})},
				 {header:"金额",dataIndex:"totalAmount",sortable:true,width:80,renderer:this.readOnlyRender(function(v){return v&&v.toFixed?v.toFixed(2):v;}),summaryType:'sum',summaryRenderer: function(v, params, data){return v?v.toFixed(2):"";}},
				 {header:"金额",dataIndex:"saleAmount",hidden:true,width:80,renderer:this.readOnlyRender(function(v){return v&&v.toFixed?v.toFixed(2):v;}),summaryType:'sum',summaryRenderer: function(v, params, data){return v?v.toFixed(2):"";}},
				 #end
				 #if($!session.COMPANY_TYPE!=1 && $!session.COMPANY_TYPE!=5)
				 {header:"零售价",dataIndex:"newPrice",width:60,renderer:this.readOnlyRender(function(v){return v&&v.toFixed?v.toFixed(2):v;})},
				 #else
				 {header:"零售价",dataIndex:"newPrice",hidden:true,width:60,renderer:this.readOnlyRender(function(v){return v&&v.toFixed?v.toFixed(2):v;})},
				 #end
				 {header:"备注",dataIndex:"remark",editor:new Ext.form.TextField()}],function(o){
				 cms[cms.length]=o;
				 });
		return new Ext.grid.ColumnModel(cms);
	},  
    selectRowDataHandler : function(obj,record,cell) {   
    	StockIncomePurchasePanel.superclass.selectRowDataHandler.call(this,obj,record);
        record.set("price", obj.buyPrice);
        record.set("marketPrice", obj.marketPrice);
    },
    confirmIncome:function(){
    	var record = this.grid.getSelectionModel().getSelected();
		if (!record) {
			Ext.Msg.alert("提示",
					"请先选择要操作的数据！");
			return false;
		}
    	if(!record.get("auditing")){
			Ext.Msg.alert("提示信息","请先审核入库单!");
			return false;
		}
		if(record.get("incomeUser")){
			Ext.Msg.alert("提示信息",record.get("incomeUser")+"已经确认过该入库单!");
			return false;
		}
		var id = record.get("id");
		if(record.get("auditing") && !record.get("incomeUser") && id){
			Ext.Ajax.request({url:this.baseUrl,params:{cmd:"confirmIncome",id:id},scope:this,success:function(req){
				Ext.Msg.alert("提示信息","确认成功!");
			}})
		}
    },
    edit : function() {
		if(this.btn_edit.disabled){this.view();return false;}
		var record = this.grid.getSelectionModel().getSelected();
		if (!record) {
			Ext.Msg.alert("提示",
					"请先选择要操作的数据！");
			return false;
		}
		#if($!ROLE.is("CASHIER"))
		if(1==1){
			this.view();
			return false;
		}
		#end
		if(record.get("auditing")){
			this.view();
			return false;
		}
		return StockIncomePurchasePanel.superclass.edit.call(this);
	},
	quickSearch : function() {
		var d1 = this.search_vdate1.getValue() ? this.search_vdate1.getValue().format("Y-m-d") : "";
		var d2 = this.search_vdate2.getValue() ? this.search_vdate2.getValue().format("Y-m-d") : "";
		var supplier=this.search_supplier.getRawValue()?this.search_supplier.getValue():"";
		this.store.baseParams = Ext.apply({}, {
			vdate1 : d1,
			vdate2 : d2,
			supplier : supplier
		},this.baseQueryParameter);
		if("defaultStatus" in this.store.baseParams) delete this.store.baseParams.defaultStatus
		if("advanced" in this.store.baseParams) delete this.store.baseParams.advanced
		this.refresh();
	},
	printSmall:function(){
		var record = this.grid.getSelectionModel().getSelected();
		if (!record) {
			Ext.Msg.alert("提示",
					"请先选择要操作的数据！");
			return false;
		}
		var id = record.get("id");
		if(id){
			Ext.Ajax.request({
				url:this.baseUrl,
				params:{id:id,cmd:'printSmall'},
				callback:function(options,success,response){
					Disco.Ext.Util.executeLocalCommand("printstockincome",response.responseText);
				}
			})
		}
	},
	afterList:function(){
		this.store.baseParams.defaultStatus=0;
		this.btn_refresh.hide();
		this.searchField.hide();
		this.colorStore=new Ext.util.MixedCollection();
		this.search_vdate1=new	Ext.form.DateField({fieldLabel:"开始时间",emptyText:"开始时间",width:90,format:"Y-m-d"});
		this.search_vdate2=new	Ext.form.DateField({fieldLabel:"结束时间",emptyText:"结束时间",width:90,format:"Y-m-d"});
		this.search_supplier=new ClientComboBox(Ext.apply({},{width:90},ConfigConst.CRM.supplier));
		this.grid.on("render",function(){
			this.grid.getTopToolbar().insert(9,[
			{text:"入库确认",cls : "x-btn-text-icon",icon : "images/icons/page_code.png",handler:this.confirmIncome,scope:this},
			//{text:"小票打印",cls : "x-btn-text-icon",	icon : "images/icons/printer.png",handler:this.printSmall,scope:this},
			this.search_vdate1,this.search_vdate2,{text:"供应商:"},this.search_supplier,{text:"查询",cls : "x-btn-text-icon",icon:"images/icons/page_find.png",handler:this.quickSearch,scope:this},
				{id:"btn_refresh",text : "重置",iconCls : 'f5',handler : function(){
					this.search_vdate1.setValue("");
					this.search_vdate2.setValue("");
					this.search_supplier.setValue("");
				},scope : this}
			]);
			this.menu.insert(4,new Ext.menu.Item({
			id:"menu_printSmail",
			text : "打印小票",
			handler : this.printSmall,
			scope : this,
			iconCls : "import-icon",
			hidden:false
		}))
				},this);	
	},
    loadProductStock:function(grid,row,col){},
    createWin:function(callback,autoClose){
    	return this.initWin(Ext.getBody().dom.offsetWidth-20,Ext.getBody().dom.offsetHeight-20,"采购入库单",callback,autoClose);
    },
    storeMapping:["id","sn","sn2","vdate","client","types","sender","deliver","depot","red","inputTime","inputUser","auditing","auditor",
    "recipient","keeper","manager","incomeSource","company","incomeUser","seller","sender","totalAmount","closed","status","remark","items","fee1","fee2","fee3","fee4","other1","other2","other3","other4","other5","other6","supplier","dept"],
	initComponent : function(){
		//this.store.baseParams.vdate3=new Date().format("Y-m-d");
		var chkM=new Ext.grid.CheckboxSelectionModel();
		this.gridConfig={sm:chkM};
	    this.cm=new Ext.grid.ColumnModel([	  
	    	chkM,
	    	{header: "入库单号", sortable:true,width: 100, locked:true,dataIndex:"sn"},
	    	{header: "手工单号", sortable:true,width: 100, dataIndex:"sn2"},
	    	{header: "供应商", sortable:true,width: 100, locked:true,dataIndex:"supplier",renderer:this.objectRender("name")},
	    	{header: "货品编码", sortable:true,width: 100, locked:true,dataIndex:"stockNO",hidden:true},
	    	{header:"发货通知单", sortable:true,width: 100,dataIndex:"sendInform",renderer:this.objectRender("sn"),hidden:true},
	    	{header:"订单", sortable:true,width: 100,hidden:true,dataIndex:"orders",renderer:this.objectRender("sn")},
	    	{header:"领料人", sortable:true,width: 60, dataIndex:"recipient",renderer:this.objectRender("trueName"),hidden:true},
	    	#if($!pu.showPrice())
	    	{header: "金额", sortable:true,width: 60, dataIndex:"totalAmount",hidden:false},
	    	#end
	    	{header:"仓库", sortable:true,width: 50,dataIndex:"depot",renderer:this.objectRender("title")},
	    	 {header: "审核", sortable:true,width: 100, dataIndex:"auditor",renderer:this.objectRender("trueName")},
            {header: "主管", sortable:true,width: 60, dataIndex:"manager",renderer:this.objectRender("trueName"),hidden:true},
            {header: "是否关闭", sortable:true,width: 60, dataIndex:"closed",hidden:true,renderer:this.booleanRender},
	    	{header: "备注", sortable:true,width: 150, dataIndex:"remark",hidden:true},
            {header: "制单", sortable:true,width: 60, dataIndex:"inputUser",renderer:this.objectRender("trueName")},
            #if($!pu.showDistributors())
            	{header: "所属店", sortable:false,width:100, dataIndex:"company",renderer:this.objectRender("title")},
            #end
            {header: "入库确认人", sortable:false,width: 80, dataIndex:"incomeUser"},
            {header: "录入时间", sortable:true,width: 120, dataIndex:"inputTime",renderer:this.dateRender("Y-m-d G:i")},
            {header: "项目数", sortable:true,width: 60, dataIndex:"items",hidden:true,renderer:this.objectRender("length")},
            {header: "状态", sortable:true,width: 60, dataIndex:"status",renderer:this.statusRender(this.status)},
			{
				header : "操作",
				sortable : true,
				width : 100,
				dataIndex : "status",
				renderer:this.operateRender
			}
	        ]);
	        
			StockIncomePurchasePanel.superclass.initComponent.call(this);
            if(this.editStoreMapping.indexOf("newPrice")<0)
            this.editStoreMapping=this.editStoreMapping.concat(["buyPrice","newPrice"]);
	}     
});