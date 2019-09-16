if(typeof Global ==="undefined"){
	Global={};
}
if(!Global.productDirLoader){
	Global.productDirLoader = new Ext.tree.TreeLoader({
		iconCls : 'lanyo-tree-node-icon',
		url : "productDir.ejf?cmd=getProductDirTree&pageSize=-1&treeData=true",
		listeners : {
			'beforeload' : function(treeLoader, node) {
				treeLoader.baseParams.id = (node.id.indexOf('root')<0 ? node.id : "");
				if(typeof node.attributes.checked!=="undefined"){
					treeLoader.baseParams.checked=false;
				}
			}
		}
		});
}
ProductListPanel=Ext.extend(Disco.Ext.CrudPanel,{
	pageSize:20,
	id:"productListPanel",
	gridForceFit : false,
	baseUrl:"product.ejf",
	searchWin:{title:"产品高级查询",width:538,height:220},
	viewWin:function(){
    	var size = this.getDefaultWinSize();
    	return {
    		width : size.width ,
    		height : size.height+30,
    		title : '产品详情查看'
    	}
    },
	createForm:function(){
	    var formPanel=new Ext.form.FormPanel({
	        frame:true,
	        labelWidth:70,
	        labelAlign:'right',
	        fileUpload : true,
	        items:[
	       		{xtype:"hidden",name:"id"},
	  			Disco.Ext.Util.twoColumnPanelBuild(
				    {fieldLabel:'产品名称',name:'title',allowBlank:false},
				    {fieldLabel:'产品编号',name:'sn',allowBlank:false},
				    {
				    	 name:"dir",
						 allowBlank:false,
			    		 xtype:"treecombo",
						 fieldLabel:"所属目录",
						 hiddenName:"dir",
						 displayField:"title",
						 tree:new Ext.tree.TreePanel({
				 			root:new Ext.tree.AsyncTreeNode({
					 				id:"root",
					   				text:"产品目录",   	
					   				expanded:true,
					   				iconCls : 'treeroot-icon',
					   				loader:Global.productDirLoader
				   				})
			 			 })/*,
			 			 listeners:{
			 				 select:function(c,node){this.loadPropertys(c.getValue());},
			 				 scope:this	
			 			 }*/
				 	},
				    //ConfigConst.BASE.getDictionaryCombo("dir","分类","ProductDir",null,false),
				    ConfigConst.BASE.getDictionaryCombo("brand","品牌","ProductBrand"),
				    ConfigConst.BASE.getDictionaryCombo("unit","计量单","ProductUnit"),
				    {fieldLabel:'产品图片',name:'imgPath',xtype:"fileuploadfield"},
				    {fieldLabel:'规格',name:'spec'},
				    {fieldLabel:'型号',name:'model'},
				    {fieldLabel:'等级',name:'level'},
				    {fieldLabel:'进价',name:'buyPrice',xtype:"numberfield"},
				    {fieldLabel:'销售价',name:'salePrice',xtype:"numberfield"}
		   		),
	   		{fieldLabel:'产品描述',xtype:"textarea",name:'content',anchor:"-20",height:80}
	     ]
	    });
        return formPanel;
	},
	onView : function(win,r){
		var imgPath = r.imgPath ? "upload/product/"+r.imgPath : Ext.BLANK_IMAGE_URL;
		this.viewPanel.imgPath.el.dom.src = imgPath;
	},
	createViewPanel: function(){
		var objectRender = Disco.Ext.Util.objectRender("title");
		var viewPanel = new Ext.FormPanel({
		        frame:true,
		        labelWidth:70,
		        //labelAlign:'right',
		        bodyStyle:'padding:10px;',
		        items:[
		        	{xtype:"hidden",name:"id"},
		       		{
		       			height:170,
		       			xtype : 'panel',
		       			layout : 'hbox',
		       			layoutConfig : {align:'stretch'},
		       			defaults : {flex:1},
		       			items : [
		       				{
		       					layout : 'form',
		       					xtype:'panel',
		       					defaultType:'labelfield',
		       					items : [
		       						{fieldLabel:'产品名称',name:'title',allowBlank:false},
				       				{fieldLabel:'产品编号',name:'sn',allowBlank:false},
				       				{fieldLabel:'所属目录',name:'dir',renderer:objectRender,allowBlank:false},
				       				{fieldLabel:'品牌',name:'brand',renderer:objectRender},
				       				{fieldLabel:'计量单',name:'unit',renderer:objectRender},
				       				{fieldLabel:'规格',name:'spec'},
								    {fieldLabel:'型号',name:'model'},
								    {fieldLabel:'等级',name:'level'}
		       					] 
		       				},{
		       					xtype : 'fieldset',
		       					title : '产品图片',
		       					items : {
		       						xtype:'box',
		       						ref:'../../imgPath',
		       						autoEl:{tag:'img',style:'width:100%;height:100%;',src:Ext.BLANK_IMAGE_URL}
		       					}
		       				}
		       			]
		       		},Ext.apply(
		       			Disco.Ext.Util.buildColumnForm('labelfield',
		       		   		{fieldLabel:'进价',name:'buyPrice'},
					   		{fieldLabel:'销售价',name:'salePrice'}
					   	),
					   	{style:'margin-top:5px;'}
					 ),{
					 	fieldLabel:'产品描述',
					 	xtype : 'labelfield',
					 	name : 'content'
					 }
		     	]
		});
		return viewPanel;
	},
    addPropertysField:function(dp){
    	var p=dp.parameter;
    	var panel=this.fp.findById("propertysPanel");
    	var config={name:"propertys"+dp.id,fieldLabel:p.title};
    	if(!dp.canEmpty)config.allowBlank=false;
    	var bz=p.dw;
    	config.listeners={"render":function(f){
    		f.el.dom.parentNode.appendChild(document.createTextNode(bz));
    	}};
    	
    	if(p.theType=="0"){
    		config.xtype="textfield";
    	}
    	else if(p.theType=="1"){
    		config.xtype="numberfield";
    	}
    	else if(p.theType=="2"){
    		var values=p.theValue.split(",");
    		var vs=[];
    		for(var i=0;i<values.length;i++){
    		if(values[i]){
    		var index=vs.length;
    		vs[index]=[];
    		vs[index][0]=values[i];
    		vs[index][1]=values[i];
    		}
    		}
    		Ext.apply(config,{
    			xtype:"combo",
				hiddenName:config.name,				
				displayField:"title",
				valueField:"value",
				store: new Ext.data.SimpleStore({
				        fields: ['title', 'value'],
				        data : vs
				    }),
				editable:false,
	        	mode: 'local',
	        	triggerAction: 'all',
	        	emptyText:'请选择...'
				});				
    	}
    	else if(p.theType=="3"){
    		config.xtype="numberfield";
    		var values=p.theValue.split(",");
    		config.minValue=values[0];
    		config.maxValue=values[1];
    	}
    	else if(p.theType=="4"){
    		config.xtype="textarea";
    		config.height=50;
    	}   
    	else{
    		config.xtype="textfield";
		};
    	panel.add(config); 	
    },
    loadPropertys:function(id,callback){
    	var n=parseFloat(id);
    	if(n){
    	Ext.Ajax.request({url:"productDir.ejf?cmd=getDir",
    		params:{id:id},
    		success:function(response,options){
    			eval("var ret="+response.responseText);
    			var owner=this.fp.findById("propertysPanel").ownerCt;
    			var panel=owner.remove(0);
    			owner.add(panel.initialConfig);
    			var ps=ret.parameters;
    			for(var i=0;i<ps.length;i++){
    				this.addPropertysField(ps[i]);    				
    			}
    			owner.doLayout();	
    			if(callback)callback();
    		},
    		scope:this
    		}); 
    	}	   	
    },
    /**
     * 创建高级查询窗口
     * @return {Ext.FormPanel}
     */
   	searchFormPanel:function(){
		var formPanel = new Ext.form.FormPanel({
			frame : true,
			labelWidth : 80,
			labelAlign : "right",
			items : [{
				xtype : "fieldset",
				title : "查询条件",
				autoHeight : true,
				items : [
				Disco.Ext.Util.buildColumnForm({fieldLabel:'编码',name:'sn'},{fieldLabel:'材料名称',name:'title'},
				{
			    	 name:"dir",
					 allowBlank:false,
		    		 xtype:"treecombo",
					 fieldLabel:"所属目录",
					 hiddenName:"dir",
					 displayField:"title",
					 tree:new Ext.tree.TreePanel({
			 			root:new Ext.tree.AsyncTreeNode({
				 				id:"root",
				   				text:"产品目录",   	
				   				expanded:true,
				   				iconCls : 'treeroot-icon',
				   				loader:Global.productDirLoader
			   				})
		 			 })
			 	},
				 ConfigConst.BASE.getDictionaryCombo("brand","品牌","ProductBrand"),
					{fieldLabel:'产品规格',name:'spec'},
					{fieldLabel:'型号',name:'model'},
				 	{fieldLabel : "最低价格",name : "salePrice_start",xtype : 'numberfield'},
				 	{fieldLabel : "最高价格",name : "salePrice_end",xtype : 'numberfield'}
				)]
			}]
		});
		return formPanel;
	},
	getDefaultWinSize : function(){
		return {
			width : 538,
			height : 330
		}
	},
    createWin:function(callback, autoClose)    {
    	var s = this.getDefaultWinSize();
    	return this.initWin(s.width,s.height,"产品信息录入",callback, autoClose);
    },
    storeMapping:["id","title","sn","spec","model","level","buyPrice","salePrice","content","dir","brand","unit","imgPath"],
    beforeDestroy : function(){
    	Ext.destroy(this.gridTip);
    	delete this.gridTip;
    	ProductListPanel.superclass.beforeDestroy.call(this);
    },
    initRowTip : function(grid){
    	this.gridTip = new Ext.ToolTip({
	    	width : 200,
	    	height:150,
	    	autoHeight:false,
	    	target: grid.getView().mainBody,
	    	delegate: 'div.icon-img',
	    	tipAnchor : 'r',
	    	anchor : 'left',
	    	//anchorOffset: 3,
	    	mouseOffset : [5,0],
	    	onTargetOver : function(e){
	    		this.constructor.prototype.onTargetOver.call(this,e);
	    		var t = e.getTarget(this.delegate,null,true);
	    		if(t){
	    			var row = t.parent('div.x-grid3-row',true);
	    			this.targetRow = row.rowIndex;
	    		}
	    	},
	    	listeners:{
	    		scope : this,
	    		beforeshow : function(tip){
	    			if(Ext.isDefined(tip.targetRow)){
	    				var record = this.grid.getStore().getAt(tip.targetRow);
	    				var imgPath = record.get('imgPath');
	    				var t = String.format('<img style="width:100%;height:100%" src="upload/product/{0}" />',imgPath);
	    				if(tip.rendered){
	    					tip.update(t);
	    				}else{
	    					tip.html=t;
	    				}
	    			}
	    		}
	    	}
	    });
    },
    downloadProductImg : function(){
    	var records = this.grid.getSelections();
    	if(records && records.length){
    		var r = records[0];
    		window.open('product.ejf?cmd=downProductImg&id='+r.get('id'));
    	}
    },
    onCreate : function(){
		ProductListPanel.superclass.onCreate.call(this);
		this.fp.form.findField("dir").setValue(this.parentId);
    },
	initComponent : function(){
	    this.cm=new Ext.grid.ColumnModel([
			{header: "产品名称", sortable:true,width: 100, dataIndex:"title"},
			{
				header: "图片", 
				sortable:true,
				width: 35, 
				align:'center',
				dataIndex:"imgPath",
				renderer:function(v){
					if(v){
						return '<div style="width:12px;height:12px;" class="icon-img"></div>'
					}else{
						return ''
					}
				}
			},
			{header: "产品编号", sortable:true,width: 100, dataIndex:"sn"},
	        {header: "分类", sortable:true,width: 100, dataIndex:"dir",renderer:this.objectRender("title")},
	        {header: "品牌", sortable:true,width: 100, dataIndex:"brand",renderer:this.objectRender("title")},
	        {header: "单位", sortable:true,width: 100, dataIndex:"unit",renderer:this.objectRender("title")},
			{header: "规格", sortable:true,width: 100, dataIndex:"spec"},
			{header: "型号", sortable:true,width: 100, dataIndex:"model"},
			{header: "等级", sortable:true,width: 100, dataIndex:"level"},
			{header: "进价", sortable:true,width: 100, dataIndex:"buyPrice"},
			{header: "销售价", sortable:true,width: 100, dataIndex:"salePrice"},
			{header: "简介", sortable:true,width: 300, dataIndex:"content"}
	     ]);
	     this.gridButtons = [{
	     		text : '下载图片',
	     		scope : this,
	     		iconCls : 'down',
	     		handler: this.downloadProductImg
	     }];
		ProductListPanel.superclass.initComponent.call(this);
		this.grid.on('render',this.initRowTip,this);
	}     
});
ProductManagePanel = Ext.extend(Disco.Ext.TreeCascadePanel,{
	treeCfg : {
		rootText : '所有目录',
		rootIconCls : 'treeroot-icon',
		loader : Global.productDirLoader
	},
	queryParam : 'dir',
	onTreeClick : function(node){
		var id = (node.id != 'root' ? node.id : "");
		this.getPanel().parentId =id?{id:id,title:node.text}:null;
		ProductManagePanel.superclass.onTreeClick.apply(this,arguments);
	},
	getPanel : function(){
		if(!this.panel)
			this.panel = new ProductListPanel();
		return this.panel;
	}
});