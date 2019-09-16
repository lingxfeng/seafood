// 模版管理
if (typeof Global === "undefined") {
	Global = {};
}
//模版栏目分类树
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
TemplateFileGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "templateFileGridListPanel",
	baseUrl : "templateFile.java",
	baseQueryParameter : {
		orgType : 1
	},
	viewWin : {
		width : 400,
		height : 310,
		title : '模版信息'
	},
	showView: false,
	onCreate : function() {
		if (this.dir) {
			this.fp.form.findField("dir").setOriginalValue(this.dir);
		}
	},
	edit : function() {
		var win = TemplateFileGridListPanel.superclass.edit.call(this);
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
			var dirObj = record.get('dir');
			if (dirObj) {
				this.fp.form.findField('dir').setOriginalValue(dirObj);
			}
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
					Disco.Ext.Util.twoColumnPanelBuild({
						fieldLabel : "模版名称",
						name : "title",
						emptyText : '模版名称不能为空',
						allowBlank : false,
						blankText : '模版名称不能为空'
					},{
						fieldLabel : '模版类别',
						name : 'dir',
						xtype : "treecombo",
						hiddenName : "dirId",
						displayField : "title",
						valueField : "id",
						allowBlank : false,
						blankText : '模版类别不能为空或所有类别',
						tree : new Ext.tree.TreePanel({
							autoScroll : true,
							root : new Ext.tree.AsyncTreeNode({
								id : "root",
								text : "所有类别",
								expanded : true,
								iconCls : 'treeroot-icon',
								loader : Global.templateDirLoader,
								types : "0"
							})
						})
					},{
						xtype : 'fileuploadfield',
						emptyText : '单击右侧上传按钮选择需上传的模板文件......',
						fieldLabel : '上传模板',
						allowBlank : false,
						blankText : '上传文件不能为空',
						name : 'path',
						buttonCfg : {
							text : '',
							iconCls : 'upload-icon'
						}
					}),{
						xtype: "textarea",
						fieldLabel : "模版简介",
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
			var dirNode = this.fp.form.findField("dir");
			if (dirNode && dirNode.tree.rendered) {
				dirNode.tree.root.reload();
				dirNode.tree.expandAll();
			}
		}
	},
	createWin : function(callback, autoClose) {
		return this.initWin(550, 250, "模版管理", callback, autoClose);
	},
	storeMapping : [ "id", "title", "dir", "path", "description", "inputTime", "status",
			"dirPath", "sequence"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([ {
			header : "序号",
			sortable : true,
			width : 100,
			dataIndex : "sequence"
		}, {
			header : "模版名称",
			sortable : true,
			width : 160,
			dataIndex : "title"
		},{
			header : "上传路径",
			sortable : true,
			width : 160,
			dataIndex : "path"
		},{
			header : "模版类别",
			sortable : true,
			width : 100,
			dataIndex : "dir",
			renderer : this.objectRender("title")
		},{
			width : 65,
			sortable : true,
			header : "创建日期",
			dataIndex : "inputTime",
			renderer : this.dateRender()
		}, {
			header : "深度路径",
			sortable : true,
			width : 160,
			dataIndex : "dirPath"
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
		TemplateFileGridListPanel.superclass.initComponent.call(this);
		this.on("saveobject", this.reloadLeftTree, this);
		this.on("removeobject", this.reloadLeftTree, this);
	},
	listeners : {
		render : function(e) {
		}
	}
});
// 模版栏目分类
TemplateFileManagePanel = function() {
	this.list = new TemplateFileGridListPanel({
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
			loader : Global.templateDirLoader
		})
	});
	this.list.tree = this.tree;
	this.tree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		this.list.store.baseParams.dirId = id;
		if (id) {
			this.list.dir = {
				id : id,
				title : node.text
			};
		} else
			this.list.parent = null;
		this.list.store.removeAll();
		this.list.store.load();
	}, this);
	TemplateFileManagePanel.superclass.constructor.call(this, {
		id : "templateFileManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",

		items : [ this.tree, this.list ]
	});
};

Ext.extend(TemplateFileManagePanel, Ext.Panel, {});