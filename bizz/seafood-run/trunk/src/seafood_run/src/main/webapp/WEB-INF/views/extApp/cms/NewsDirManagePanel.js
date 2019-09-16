// 栏目管理
if (typeof Global === "undefined") {
	Global = {};
}
// 栏目树
Global.newsDirLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "newsDir.java?cmd=getNewDirTree&pageSize=-1&treeData=true&all=true",
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

// 文章模版树
Global.templateFileDoc = new Ext.tree.TreeLoader(
		{
			iconCls : 'disco-tree-node-icon',
			varName : "Global.DEPT_DIR_LOADER",// 缓存Key
			url : "templateFile.java?cmd=getTemplateFileTree&pageSize=-1&treeData=true&dir=wenzhang",
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

// 栏目模版树
Global.templateFileDir = new Ext.tree.TreeLoader(
		{
			iconCls : 'disco-tree-node-icon',
			varName : "Global.DEPT_DIR_LOADER",// 缓存Key
			url : "templateFile.java?cmd=getTemplateFileTree&pageSize=-1&treeData=true&dir=lanmu",
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
// PPT树
Global.pPTTypeLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "linkImgType.java?cmd=getPPTTypeTree&pageSize=-1&treeData=true",
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
NewsDirGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "newsDirGridListPanel",
	baseUrl : "newsDir.java",
	baseQueryParameter : {
		orgType : 1
	},
	viewWin : {
		width : 400,
		height : 310,
		title : '分类信息'
	},
	showView : false,
	statusRender : function(v) {
		if (v == 1) {
			return "启用"
		} else if (v == 0) {
			return "停用"
		} else {
			return "未知"
		}
	},
	types : [ [ "实体栏目", "0" ], [ "单页栏目", "1" ], [ "链接栏目", "2" ] ],
	typesFormat : function(v) {
		if (v == 0) {
			return "实体栏目";
		} else if (v == 1) {
			return "单页栏目";
		} else {
			return "链接栏目";
		}
	},
	bannerImgRender: function(value, p, record) {
		if(value !=null && value !=""){
			return String.format('{1}<b><a style="color:green" href="'+value+'" target="_blank">&nbsp查看</a></b><br/>', "", "")
		}       
    },
	onCreate : function() {
		if (this.parent && this.parentType == "0") {
			this.fp.form.findField("parent").setOriginalValue(this.parent);
		}
		this.fp.form.findField('code').setDisabled(false);
	},
	removeData : function() {
		var record = this.grid.getSelectionModel().getSelected();
		var docNum = record.get('docNum');
		if (docNum > 0) {
			Ext.MessageBox.show({
				title : "警告",
				msg : "该栏目下文章数量大于0，不可删除！！！",
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.WARNING
			});
			return false;
		}
		var win = NewsDirGridListPanel.superclass.removeData.call(this);

	},
	edit : function() {
		var win = NewsDirGridListPanel.superclass.edit.call(this);
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
			var parentObj = record.get('parent');
			var parentDir = this.fp.form.findField('parent');
			// 编辑回显时如果有父级栏目及回显数据
			if (parentObj) {
				parentObj.title || (parentObj.title = parentObj.name);
				parentDir.setOriginalValue(parentObj);
			}
			this.fp.form.findField('code').setDisabled(true);
		}
	},
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : true,
			labelWidth : 70,
			labelAlign : 'right',
			defaultType : 'textfield',
			fileUpload : true,
			defaults : {
				anchor : "-20"
			},
			items : [ {
				xtype : "hidden",
				name : "id"
			}, {
				xtype : "hidden",
				name : "docNum"
			}, {
				xtype : "hidden",
				name : "sequence"
			}, {
				xtype : "hidden",
				name : "dirPath"
			}, Disco.Ext.Util.twoColumnPanelBuild({
				fieldLabel : "栏目编码",
				name : "code",
				emptyText : '栏目编码不能为空',
				allowBlank : false,
				blankText : '栏目编码不能为空'
			}, {
				fieldLabel : "栏目名称",
				name : "name",
				emptyText : '名称不能为空',
				allowBlank : false,
				blankText : '名称不能为空'
			}, {
				xtype : "combo",
				name : "types",
				hiddenName : "types",
				fieldLabel : "栏目类型",
				allowBlank : false,
				blankText : '栏目类型不能为空',
				displayField : "title",
				valueField : "value",
				store : new Ext.data.SimpleStore({
					fields : [ 'title', 'value' ],
					data : this.types
				}),
				editable : false,
				mode : 'local',
				triggerAction : 'all'
			}, {
				fieldLabel : '上级栏目',
				name : 'parent',
				xtype : "treecombo",
				hiddenName : "parentId",
				displayField : "title",
				valueField : "id",
				tree : new Ext.tree.TreePanel({
					// rootVisible: false,
					autoScroll : true,
					root : new Ext.tree.AsyncTreeNode({
						id : "root",
						text : "所有栏目",
						expanded : true,
						iconCls : 'treeroot-icon',
						loader : Global.newsDirLoader,
						types : "0",
						dirPath:""
					})
				}),
				listeners:{
					select:function(c,r,o){
						var dirPathForm = formPanel.form.findField('dirPath').value;
						var dirPathCur = r.attributes.dirPath;
						var index = dirPathCur.indexOf(dirPathForm);
						if(index!=-1){
							formPanel.form.findField('parent').setOriginalValue(null);
						} 
					}
				}
			}, {
				xtype : 'fileuploadfield',
				emptyText : '单击右侧按钮选择上传的图片',
				fieldLabel : "导航横幅",
				name : "bannerImg",
				buttonCfg : {
					text : '',
					iconCls : 'upload-icon'
				}
			}, {
				fieldLabel : '链接图库',
				name : 'pPtType',
				xtype : "treecombo",
				hiddenName : "pPtTypeId",
				displayField : "title",
				valueField : "id",
				tree : new Ext.tree.TreePanel({
					autoScroll : true,
					root : new Ext.tree.AsyncTreeNode({
						id : "root",
						text : "所有PPT",
						expanded : true,
						iconCls : 'treeroot-icon',
						loader : Global.pPTTypeLoader,
						types : "0"
					})
				})
			}, {
				fieldLabel : '文章模版',
				name : 'docTpl',
				xtype : "treecombo",
				hiddenName : "docTplId",
				displayField : "title",
				valueField : "id",
				tree : new Ext.tree.TreePanel({
					autoScroll : true,
					root : new Ext.tree.AsyncTreeNode({
						id : "root",
						text : "所有文章模版",
						expanded : true,
						iconCls : 'treeroot-icon',
						loader : Global.templateFileDoc,
						types : "0"
					})
				})
			}, {
				fieldLabel : '栏目模版',
				name : 'dirTpl',
				xtype : "treecombo",
				hiddenName : "dirTplId",
				displayField : "title",
				valueField : "id",
				tree : new Ext.tree.TreePanel({
					autoScroll : true,
					root : new Ext.tree.AsyncTreeNode({
						id : "root",
						text : "所有栏目模版",
						expanded : true,
						iconCls : 'treeroot-icon',
						loader : Global.templateFileDir,
						types : "0"
					})
				})
			}, {
				fieldLabel : "关键字",
				name : "keywords"
			},{
				fieldLabel : "栏目描述",
				name : "description"
			},{
				fieldLabel : "外链url地址",
				name : "url"
			}, {
				fieldLabel : '栏目状态',
				xtype : 'radiogroup',
				name : 'status',
				items : [ {
					checked : true,
					boxLabel : '启用',
					name : 'status',
					inputValue : 1
				}, {
					boxLabel : '停用',
					name : 'status',
					inputValue : 0
				} ]
			}) ]
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
		return this.initWin(550, 260, "导航栏目管理", callback, autoClose);
	},
	mulisStatic : function() {
		Ext.Ajax.request({
			waitMsg : "正在执行操作，请稍候...",
			url : this.baseUrl + "?cmd=static",
			method : 'POST',
			params : "type=mulit",
			success : function(response) {
				var r = Ext.decode(response.responseText);
				Ext.Msg.alert("提示", r.data, this);
			},
			scope : this
		});
	},
	storeMapping : [ "id", "code", "name", "dirPath", "docNum", "url", "types",
			"createDate", "sequence", "status", "parent", "docNum",
			"bannerImg", "docTpl", "dirTpl", "pPtType","keywords","description" ],
	initComponent : function() {
		this.batchRemoveMode = true;
		this.cm = new Ext.grid.ColumnModel([ {
			header : "序号",
			sortable : true,
			width : 100,
			dataIndex : "sequence"
		}, {
			header : "名称",
			sortable : true,
			width : 160,
			dataIndex : "name"
		}, {
			header : "上级栏目",
			sortable : true,
			width : 100,
			dataIndex : "parent",
			renderer : this.objectRender("name")
		}, {
			header : "文章模版",
			sortable : true,
			width : 100,
			dataIndex : "docTpl",
			renderer : this.objectRender("title")
		}, {
			header : "栏目模版",
			sortable : true,
			width : 100,
			dataIndex : "dirTpl",
			renderer : this.objectRender("title")
		}, {
			header : "导航条",
			sortable : true,
			width : 100,
			dataIndex : "bannerImg",
            renderer: this.bannerImgRender
		}, {
			header : "链接图库",
			sortable : true,
			width : 100,
			dataIndex : "pPtType",
			renderer : this.objectRender("title")
		}, {
			header : "栏目类型",
			sortable : true,
			width : 100,
			dataIndex : "types",
			renderer : this.typesFormat
		}, {
			header : "文章数量",
			sortable : true,
			width : 160,
			dataIndex : "docNum"
		}, {
			width : 65,
			sortable : true,
			header : "创建日期",
			dataIndex : "createDate",
			renderer : this.dateRender()
		}, {
			width : 80,
			header : "状态",
			sortable : true,
			dataIndex : "status",
			renderer : this.statusRender
		} ]);
		this.cm.setHidden(1, false);
		this.gridButtons = [ {
			text : "页面静态化",
			iconCls : "upload-icon",
			menu : [ {
				text : "生成所选",
				handler : this.executeCmd("static&type=single"),
				scope : this
			}, {
				text : "全部生成(慎重)",
				handler : this.mulisStatic,
				scope : this
			} ]
		}, {
			text : "上移",
			iconCls: "upload-icon",
			handler : this.swapSequence(""),
			scope : this
		}, {
			text : "下移",
			iconCls: "down",
			handler : this.swapSequence(true),
			scope : this
		} ];
		NewsDirGridListPanel.superclass.initComponent.call(this);
		this.on("saveobject", this.reloadLeftTree, this);
		this.on("removeobject", this.reloadLeftTree, this);
	}
});
// 系统菜单管理
NewsDirManagePanel = function() {
	this.list = new NewsDirGridListPanel({
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
			loader : Global.newsDirLoader
		})
	});
	this.list.tree = this.tree;
	this.tree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		this.list.parentId = id;
		this.list.parentType = node.attributes.types;
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
	NewsDirManagePanel.superclass.constructor.call(this, {
		id : "newsDirManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",

		items : [ this.tree, this.list ]
	});
};

Ext.extend(NewsDirManagePanel, Ext.Panel, {});