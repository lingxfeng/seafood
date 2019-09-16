//奖品管理
if (typeof Global === "undefined") {
	Global = {};
}
Global.accountLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "account.java?cmd=getTree",
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

PrizeProGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "prizeProGridListPanel",
	baseUrl : "prizePro.java",
	showView: false,
	type:[["点击菜单","click"],["查看菜单","view"]],
	typeRender:function(v){
		if(v=="click"){
			return "点击菜单";
		}else{
			return "查看菜单";
		}
	},
	onCreate : function() {
		if(this.account){
			this.fp.form.findField("account").setOriginalValue(this.account);
		}
	},
	edit : function() {
		PrizeProGridListPanel.superclass.edit.call(this);
		var record = this.grid.getSelectionModel().getSelected();
		var account = record.get("account");
		account.title=account.name;
		this.fp.form.findField("account").setOriginalValue(account);
	},
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : true,
			labelWidth : 100,
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
					Disco.Ext.Util.twoColumnPanelBuild({
						fieldLabel : '所属微信号',
						name : 'account',
						xtype : "treecombo",
						hiddenName : "accountId",
						displayField : "title",
						valueField : "id",
						allowBlank : false,
						blankText : '所属微信号',
						tree : new Ext.tree.TreePanel({
							autoScroll : true,
							rootVisible:false,
							root : new Ext.tree.AsyncTreeNode({
								id : "root",
								text : "所有微信号",
								expanded : true,
								iconCls : 'treeroot-icon',
								loader : Global.accountLoader,
								types : "0"
							})
						})
					},{
						fieldLabel : "奖品名称",
						name : "name",
						emptyText : '奖品名称能为空',
						allowBlank : false,
						blankText : '奖品名称不能为空'
					},{
						fieldLabel : "获奖几率",
						xtype:"numberfield",
						name : "probability",
						emptyText : '获奖几率能为空',
						allowBlank : false,
						blankText : '获奖几率不能为空',
						listeners : {
							blur : function(v){
								if(v.value>100){
									v.setValue(0);
								}
							},
							scope : this
						}
					},{
						xtype : 'fileuploadfield',
						emptyText : '单击右侧按钮选择上传图片',
						fieldLabel : "奖品图片",
						name : "imgPath",
						buttonCfg : {
							text : '',
							iconCls : 'upload-icon'
						}
					},{
		                fieldLabel: '状态',
		                xtype: 'radiogroup',
		                name: 'status',
		                items: [{
		                    boxLabel: '未生效',
		                    name: 'status',
		                    inputValue: "0"
		                }, {
		                	checked: true,
		                    boxLabel: '生效',
		                    name: 'status',
		                    inputValue: "1"
		                }]
		            }),{
						xtype: "textarea",
						fieldLabel : "奖品简介",
						name : "intro"
					}]
		});
		return formPanel;
	},
	reloadLeftTree : function() {
		if (this.tree) {
			this.tree.root.reload();
			this.tree.expandAll();
		}
		if (this.fp) {
			var dirNode = this.fp.form.findField("dir");
			if (dirNode && dirNode.tree.rendered) {
				dirNode.tree.root.reload();
				dirNode.tree.expandAll();
			}
		}
	},
	createWin : function(callback, autoClose) {
		return this.initWin(550, 350, "奖品管理", callback, autoClose);
	},
	storeMapping : [ "id", "account", "name","imgPath", "status","intro","probability"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([{
			header : "奖品名称",
			sortable : true,
			width : 160,
			dataIndex : "name"
		},{
			header : "获奖几率",
			sortable : true,
			width : 160,
			dataIndex : "probability",
			renderer:function(v){
				return v+"%";
			}
		},{
			header : "奖品图片",
			sortable : true,
			width : 160,
			dataIndex : "imgPath"
		},{
			header : "所属微信号",
			sortable : true,
			width : 160,
			dataIndex : "account",
			renderer : this.objectRender("name")
		},{
			header : "奖品状态",
			sortable : true,
			width : 160,
			dataIndex : "status",
			renderer : function(v){return v=="1"?"是":"否"}
		}]);
		PrizeProGridListPanel.superclass.initComponent.call(this);
		this.on("saveobject", this.reloadLeftTree, this);
		this.on("removeobject", this.reloadLeftTree, this);
	}
});
// 模版栏目分类
PrizeProManagePanel = function() {
	this.list = new PrizeProGridListPanel({
		region : "center"
	});
	this.accountTree = new Ext.tree.TreePanel({
		title : "微信帐号信息",
		region : "west",
		autoScroll : true,
		width : 200,
		border : false,
		margins : '0 2 0 0',
		tools : [ {
			id : "refresh",
			handler : function() {
				this.accountTree.root.reload();
			},
			scope : this
		} ],
		root : new Ext.tree.AsyncTreeNode({
			id : "root",
			text : "所有微信帐号",
			iconCls : 'treeroot-icon',
			expanded : true,
			loader : Global.accountLoader
		})
	});
	this.list.tree = this.accountTree;
	this.accountTree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		this.list.store.baseParams.accountId = id;
		if (id) {
			this.list.account = {
				id : id,
				title : node.text
			};
		} else{
			this.list.account = null;
		}
		this.list.store.removeAll();
		this.list.store.load();
	}, this);
	PrizeProManagePanel.superclass.constructor.call(this, {
		id : "prizeProManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",
		items : [ this.accountTree, this.list]
	});
};

Ext.extend(PrizeProManagePanel, Ext.Panel, {});