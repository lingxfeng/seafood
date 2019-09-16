if(typeof Global ==="undefined"){
	Global={};
}
if(!Global.productDirLoader){
	Global.productDirLoader =new Ext.tree.TreeLoader({
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
ProductDirListPanel=Ext.extend(Disco.Ext.CrudListPanel,{
	id:"productDirListPanel",
	//title:"分类详情",
	baseUrl:"productDir.ejf",	
	parameterTypes:[["继承父类",true],["重新设置",false]],
	viewWin:{width:600,height:300,title:"查看"},
	searchWin:{width:600,height:300,title:"高级查询"},	
	editEmptyObj:{"parameter":null,"defaultValue":null,"flow":null,"sequence":null,"canEmpty":true,canChange:true,queryEnable:true,"parameterId":null,sequence:null,remark:null},
	editStoreMapping:["parameter","defaultValue","canEmpty","canChange","queryEnable","sequence","remark"],
	comboxRender:function(v){		
		if(v){
			if(v.text)return v.text;
			else if(v.title)return v.title;
			else return v;
		}
	},
	createForm:function(){
		var store=new  Ext.data.JsonStore({
			data:[],
			fields:this.editStoreMapping
		});	
		var chartEditor1=new Disco.Ext.SmartCombox({
				 returnObject:true,
				 displayField:"title",
				 valueField:"id",
				 lazyRender:true,
				 triggerAction:"all",
				 typeAhead: false,
				 editable:false,
				 allowBlank:false,
				 width:300,
				 store:new Ext.data.JsonStore({		
				  		id:"id",
				  		url:"productParameter.ejf?cmd=list",  		
				  		root:"result",
				  		totalProperty:"rowCount",
				  		remoteSort:true,  	
				  		baseParams:{pageSize:"-1"},	
				    	fields:["id","title"]
				  		}),
				  listeners:{
				 	focus:function(f){
				 		f.store.reload();
				 	}
				 }
		});
		var formPanel=new Ext.form.FormPanel({
			frame:true,
			labelWidth:60,
			labelAlign:'right',
			layout:"fit",
	       	items:{
					xtype:'fieldset',
					title:'基本信息',
					height:280,
					defaultType:'textfield',
					items:[{xtype:"hidden",name:"id"},
					Disco.Ext.Util.twoColumnPanelBuild({xtype:'textfield',fieldLabel:'名称',name:'title'},
					{xtype:'textfield',fieldLabel:'编码',name:'sn'},
					{xtype : "treecombo",
						fieldLabel : "父目录",
						name : "parentId",
						hiddenName : "parentId",
						displayField : "title",
						valueField:"id",
						width : 110,
						tree : new Ext.tree.TreePanel({
						autoScroll:true,
						root: new Ext.tree.AsyncTreeNode({
								id : "root",
								text : "所有目录",
								expanded : true,
								iconCls : "treeroot-icon",
								loader : Global.productDirLoader
							})})
					},
					{xtype:'numberfield',fieldLabel:'顺序',name:'sequence'},
					{
							xtype : "combo",
							name : "status",
							hiddenName : "status",
							fieldLabel : "状态",
							displayField : "title",
							valueField : "value",
							width : 80,
							value:0,
							store : new Ext.data.SimpleStore({
								fields : ['title', 'value'],
								data : Disco.Ext.Util.theStatus
							}),
							editable : false,
							mode : 'local',
							triggerAction : 'all',
							emptyText : '请选择...'
				 }),
				{xtype:"textarea",fieldLabel:'简介',name:'intro',height:160,anchor:"99%"}]
	       		}
			});
			return formPanel;
    },
    reloadTree:function(){
    	if(this.tree){
    		var p=this.tree.getSelectionModel().getSelectedNode();
    		if(p&&p.parentNode)p=p.parentNode;
    		else p=this.tree.root;
    		if(p&&p.reload)p.reload.defer(100,p);
    		var parent=this.fp.findField('parentId');
    		if(parent && Ext.getObjVal(parent,'tree.root.rendered')){
    			parent.tree.getRootNode().reload();
    		}
    	}   
    },
    onSave : function(){
    	this.reloadTree(); 	
    },
    create : function() {
		var ret = ProductDirListPanel.superclass.create.call(this);
		this.fp.form.findField("parentId").setValue(this.panel.parentId);
     },     
    createWin:function() {
    	return this.initWin(600,400,"设备目录管理");
    },	
    storeMapping:["id","sn","title","dirPath","createTime","types","parent",{name:"parentId",mapping:"parent"},"sequence","intro","children","parent","parameters","inherit"],
	initComponent : function(){
	    this.cm=new Ext.grid.ColumnModel([
			{header: "名称", sortable:true,width: 120, dataIndex:"title"},    
			{header: "编号", sortable:true,width: 100, dataIndex:"sn"},						
			{header: "父目录", sortable:true,width: 100, dataIndex:"parent",renderer:this.objectRender("title")},
			{header: "排序", sortable:true,width: 80, dataIndex:"sequence"},			
			{header: "简介", sortable:true,width: 120, dataIndex:"intro"}
	    ]);
	    this.gridButtons=[{text:"上移",cls:"x-btn-text-icon",icon:"images/core/up.gif",handler:this.swapSequence(""),scope:this},
	      	{text:"下移",cls:"x-btn-text-icon",icon:"images/core/down.gif",handler:this.swapSequence(true),scope:this},new Ext.Toolbar.Separator()];      	    
		ProductDirListPanel.superclass.initComponent.call(this);	
	}     
});
//信息分类栏目管理
ProductDirPanel = Ext.extendX(Disco.Ext.TreeCascadePanel,function(superclass){
	return {
		queryParam:'parentId',
		treeCfg : {
			title :"产品分类",
			rootText : '分类',
			rootIconCls : 'treeroot-icon',
			loader : Global.productDirLoader
		},
		onTreeClick : function(node){
			var id = (node.id != 'root' ? node.id : "");
			this.listPanel.parentId = id;
			this.getPanel().parentId =id?{id:id,title:node.text}:null;
			superclass.onTreeClick.apply(this,arguments);
		},
		getPanel : function(){
			if(!this.panel){
				var plp = new ProductDirListPanel();
				plp.tree = this.tree; 
				this.listPanel = plp;
				this.panel = plp.list();
				this.panel.grid = plp.grid; 
			}
			return this.panel;
		}
	}
});
/*
//信息分类栏目管理
ProductDirPanel = function() {
	this.list = new ProductDirListPanel();
	this.tree = new Ext.tree.TreePanel( {
		title :"产品分类",
		region :"west",
		width :150,
		tools:[{id:"refresh",handler:function(){this.tree.root.reload();},scope:this}],
		root :new Ext.tree.AsyncTreeNode( {
			id :"root",
			text :"分类",
			expanded :true,
			iconCls : 'treeroot-icon',
			loader :Global.productDirLoader
		})
	});
	this.list.tree=this.tree;
	this.tree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		this.list.parentId =id?{id:id,title:node.text}:null;
		this.list.store.baseParams.parentId = id;
		this.list.store.removeAll();
		this.list.store.load();
	}, this);
	ProductDirPanel.superclass.constructor.call(this, {
		id :"productDirPanel",
		//title :"设备分类管理",
		closable :true,
		autoScroll :true,
		border:false,
		layout :"border",
		items : [ this.tree,{border:false,layout:"fit",region:"center",items:this.list.list()} ]
	});

};
Ext.extend(ProductDirPanel, Ext.Panel,{});*/