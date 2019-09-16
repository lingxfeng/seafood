// 模版管理
if (typeof Global === "undefined") {
	Global = {};
}
//模版栏目分类树
Global.platformMenuLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "systemMenu.java?cmd=getTree&pageSize=-1",
	listeners : {
		'beforeload' : function(treeLoader, node) {
			treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id
					: "");
			if (typeof node.attributes.checked !== "undefined") {
				treeLoader.baseParams.checked = false;
			}
		}
	}
});
SystemMenuGridPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "systemMenuGridPanel",
	baseUrl : "systemMenu.java",
	baseQueryParameter : {
		orgType : 1
	},
	theStatus : [["启用", 0],["停用", -1]],
	theStatusRender:function(v){
		return v=="0"?"启用":"停用";
	},
	showView: false,
	onCreate:function(){
		if(this.parent){
			this.fp.form.findField("parent").setOriginalValue(this.parent);
		}						
	},
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : true,
			labelWidth : 70,
			labelAlign : 'right',
			fileUpload: true,
			defaultType : 'textfield',
			defaults : {
				anchor : "-20"
			},
			items : [
					{
						xtype : "hidden",
						name : "id"
					},
					Disco.Ext.Util.twoColumnPanelBuild(
							{fieldLabel : '菜单名称',name : 'title',allowBlank:false},
							{fieldLabel : '菜单编码',name : 'sn',allowBlank:false},
							
							{xtype : "treecombo",
						fieldLabel : "父菜单",
						name : "parent",
						hiddenName : "parent",
						displayField : "title",
						valueField:"id",
						width : 110,
						tree : new Ext.tree.TreePanel({
								autoScroll:true,
								root: new Ext.tree.AsyncTreeNode({
										id : "root",
										text : "所有菜单",
										iconCls : 'treeroot-icon',
										expanded : true,
										loader : Global.platformMenuLoader
									})})
						},
						
						Ext.apply({},{fieldLabel : "平台菜单",name : "system",hiddenName : "system",value:false},ConfigConst.BASE.yesNo),
						Disco.Ext.Util.buildCombox("status","状态",this.theStatus,0)
						,{fieldLabel : "应用程序类",name : "appClass"},
						{fieldLabel : "所在包",name : "pack"},
						{fieldLabel : "脚本地址",name : "url"}
						)]
		});
		return formPanel;
	},
	reloadLeftTree : function() {
		if (this.tree) {
			this.tree.root.reload();
			this.tree.expandAll();
		}
	},
	createWin : function(callback, autoClose) {
		return this.initWin(550, 250, "模版管理", callback, autoClose);
	},
	storeMapping : ["id","sn", "title","url","vrtype","roles","permissions","types","appClass","sequence","status","parent","parent","otherScripts","icon","pack","theRoles","fee","system","params","tenant"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([{
			header : "编号",
			sortable : true,
			width : 120,
			dataIndex : "sn"
		}, {
			header : "名称",
			sortable : true,
			width : 120,
			dataIndex : "title"
		}, {
			header : "应用程序类",
			sortable : true,
			width : 120,
			dataIndex : "appClass"
		},
		 {
			header : "访问角色",
			sortable : true,
			width : 120,
			dataIndex : "roles",
			hidden:true
		},
		 {
			header : "父级",
			sortable : true,
			width : 120,
			dataIndex : "parent",
			renderer : this.objectRender("title")
		},{
			header : "所属租户",
			sortable : true,
			width : 120,
			dataIndex : "tenant",
			renderer : this.objectRender("title")
		},{
			header : "状态",
			sortable : true,
			width : 60,
			dataIndex : "status",
			renderer:this.theStatusRender
		}]);
		this.gridButtons = [{
			text : "上移",
			cls : "x-btn-text-icon",
			icon : "img/core/up.gif",							
			handler : this.swapSequence(""),
			scope : this
		}, {
			text : "下移",
			cls : "x-btn-text-icon",
			icon : "img/core/down.gif",							
			handler : this.swapSequence(true),
			scope : this
		}];
		SystemMenuGridPanel.superclass.initComponent.call(this);
		this.on("saveobject", this.reloadLeftTree, this);
		this.on("removeobject", this.reloadLeftTree, this);
	},
	listeners : {
		render : function(e) {
		}
	}
});
// 模版栏目分类
SystemMenuManagePanel = function() {
	this.list = new SystemMenuGridPanel({
		region : "center"
	});
	this.tree = new Ext.tree.TreePanel({
		title : "模版信息",
		region : "west",
		autoScroll : true,
		width : 200,
		border : false,
		margins : '0 2 0 0',
		tools : [ {
			id : "refresh",
			handler : function() {
				this.tree.root.reload();
			},
			scope : this
		} ],
		root : new Ext.tree.AsyncTreeNode({
			id : "root",
			text : "所有栏目",
			iconCls : 'treeroot-icon',
			expanded : true,
			loader : Global.platformMenuLoader
		})
	});
	this.list.tree = this.tree;
	this.tree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		this.list.store.baseParams.parentId = id;
		if (id) {
			this.list.parent = {
				id : id,
				title : node.text
			};
		} else
			this.list.parent = null;
		this.list.store.removeAll();
		this.list.store.load();
	}, this);
	SystemMenuManagePanel.superclass.constructor.call(this, {
		id : "systemMenuManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",

		items : [ this.tree, this.list ]
	});
};

Ext.extend(SystemMenuManagePanel, Ext.Panel, {});