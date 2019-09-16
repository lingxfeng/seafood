// 模版栏目分类管理
if (typeof Global === "undefined") {
	Global = {};
}
// 模版栏目分类树
Global.templateDirLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "templateDir.java?cmd=getTemplateDirTree&pageSize=-1&treeData=true&all=true",
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
TemplateDirGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "templateDirGridListPanel",
	baseUrl : "templateDir.java",
	baseQueryParameter : {
		orgType : 1
	},
	viewWin : {
		width : 400,
		height : 310,
		title : '模版分类信息'
	},
	showView: false,
	statusRender : function(v) {
		if (v == 1) {
			return "正常"
		} else if (v == 0) {
			return "锁定"
		} else {
			return "删除"
		}
	},
	onCreate : function() {
		if (this.parent) {
			this.fp.form.findField("parent").setOriginalValue(this.parent);
		}else{
			this.fp.form.findField("parent").setOriginalValue(null);
		}
		this.fp.form.findField('sn').setDisabled(false);
	},
	edit : function() {
		var win = TemplateDirGridListPanel.superclass.edit.call(this);
		var parentDir = this.fp.form.findField('parent');
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
			var parentObj = record.get('parent');
			// 编辑回显时如果有父级栏目及回显数据
			if (parentObj) {
				parentDir.setOriginalValue(parentObj);
			}
		}else{
			parentDir.setOriginalValue(null);
		}
		this.fp.form.findField('sn').setDisabled(true);
	},
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : true,
			labelWidth : 70,
			labelAlign : 'right',
			defaultType : 'textfield',
			defaults : {
				anchor : "-20"
			},
			items : [
					{
						xtype : "hidden",
						name : "id"
					},{
						xtype : "hidden",
						name : "path"
					},
					Disco.Ext.Util.twoColumnPanelBuild({
						fieldLabel : "分类编号",
						name : "sn",
						emptyText : '分类编号不能为空',
						allowBlank : false,
						blankText : '分类编号不能为空'
					}, {
						fieldLabel : "分类名称",
						name : "title",
						emptyText : '分类名称不能为空',
						allowBlank : false,
						blankText : '分类名称不能为空'
					},{
						fieldLabel : '父级分类',
						name : 'parent',
						xtype : "treecombo",
						hiddenName : "parentId",
						displayField : "title",
						valueField : "id",
						tree : new Ext.tree.TreePanel({
							autoScroll : true,
							root : new Ext.tree.AsyncTreeNode({
								id : "root",
								text : "所有栏目",
								expanded : true,
								iconCls : 'treeroot-icon',
								loader : Global.templateDirLoader,
								types : "0"
							})
						})
					},{
						fieldLabel : '分类状态',
						xtype : 'radiogroup',
						name : 'status',
						items : [ {
							checked : true,
							boxLabel : '正常',
							name : 'status',
							inputValue : 1
						}, {
							boxLabel : '锁定',
							name : 'status',
							inputValue : 0
						} ,{
							boxLabel : '删除',
							name : 'status',
							inputValue : -1
						} ]
					}),{	
						xtype: "textarea",
						fieldLabel : "分类描述",
						name : "description"
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
			var parentNode = this.fp.form.findField("parent");
			if (parentNode && parentNode.tree.rendered) {
				parentNode.tree.root.reload();
				parentNode.tree.expandAll();
			}
		}
	},
	createWin : function(callback, autoClose) {
		return this.initWin(550, 340, "模版栏目分类管理", callback, autoClose);
	},
	storeMapping : [ "id", "sn", "title", "description", "path", "parent", "sequence","inputDate", "status"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([ {
			header : "序号",
			sortable : true,
			width : 100,
			dataIndex : "sequence"
		},  {
			header : "模版栏目分类编号",
			sortable : true,
			width : 160,
			dataIndex : "sn"
		}, {
			header : "模版栏目分类名称",
			sortable : true,
			width : 160,
			dataIndex : "title"
		}, {
			header : "父级分类",
			sortable : true,
			width : 100,
			dataIndex : "parent",
			renderer : this.objectRender("title")
		}, {
			header : "创建日期",
			sortable : true,
			width : 100,
			dataIndex : "inputDate",
			renderer : this.dateRender()
		}, {
			width : 80,
			header : "状态",
			sortable : true,
			dataIndex : "status",
			renderer : this.statusRender
		} ]);
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
		TemplateDirGridListPanel.superclass.initComponent.call(this);
		this.on("saveobject", this.reloadLeftTree, this);
		this.on("removeobject", this.reloadLeftTree, this);
	},
	listeners : {
		render : function(e) {
		}
	}
});
// 系统菜单管理
TemplateDirManagePanel = function() {
	this.list = new TemplateDirGridListPanel({
		region : "center"
	});
	this.tree = new Ext.tree.TreePanel({
		title : "栏目信息",
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
			loader : Global.templateDirLoader
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
	TemplateDirManagePanel.superclass.constructor.call(this, {
		id : "templateDirManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",

		items : [ this.tree, this.list ]
	});
};

Ext.extend(TemplateDirManagePanel, Ext.Panel, {});