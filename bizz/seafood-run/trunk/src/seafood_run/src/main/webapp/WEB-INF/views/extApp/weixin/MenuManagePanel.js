//微信菜单管理
if (typeof Global === "undefined") {
	Global = {};
}
// 微信帐号数
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
// 获取分类
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
// 微信帐号数
Global.menuLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "menu.java?cmd=getTree",
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
MenuGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "menuGridListPanel",
	baseUrl : "menu.java",
	showView : false,
	type : [ [ "点击菜单", "click" ], [ "查看菜单(内部链接)", "viewin" ],
			[ "查看菜单(外部链接)", "viewout" ] ],
	typeRender : function(v) {
		if (v == "click") {
			return "点击菜单";
		} else if (v == "viewin") {
			return "查看菜单(内部链接)";
		} else {
			return "查看菜单(外部链接)";
		}
	},
	onCreate : function() {
		if (this.parent) {
			this.fp.form.findField("parent").setOriginalValue(this.parent);
		}
		this.fp.form.findField('account').setDisabled(false);
		if (this.account) {
			this.fp.form.findField("account").setOriginalValue(this.account);
		}
	},
	edit : function() {
		var win = MenuGridListPanel.superclass.edit.call(this);
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
			var account = record.get("account");
			account.title = account.name;
			this.fp.form.findField("account").setOriginalValue(account);
			this.fp.form.findField('account').setDisabled(true);
			var parent = record.get("parent");
			if (parent) {
				parent.title = parent.name;
				this.fp.form.findField("parent").setOriginalValue(parent);
			}
			var type = record.get("type")
			var templateele = this.fp.findField("templateName");
			var newsDirteele = this.fp.findField("newsDir");
			var newsdocele = this.fp.findField("newsDocOrderBy");
			var urlele = this.fp.findField("url");
			if (type == "click") {
				urlele.setOriginalValue();
				urlele.setDisabled(true);
				if(record.get("template")){
					templateele.setOriginalValue(record.get("template").title);
				}else{
					templateele.setOriginalValue("");
				}
				newsDirteele.setDisabled(false);
				newsdocele.setDisabled(false);
			} else {
				urlele.setDisabled(false);

				templateele.setOriginalValue();

				newsDirteele.setOriginalValue();
				newsDirteele.setDisabled(true);

				newsdocele.setOriginalValue();
				newsdocele.setDisabled(true);
			}
		}
	},
	// 发布
	fabutoweixin : function(v) {
		if (!this.account) {
			Ext.Msg.alert("提示", "请选中一条微信帐号！！！", this);
			return;
		}
		Ext.Ajax.request({
			url : "menu.java?cmd=createMenuToweixin&accountId="
					+ this.account.id,
			method : "GET",
			success : function(response) {
				var r = Ext.decode(response.responseText);
				if(r.errcode=="0"){
					Ext.Msg.alert("提示", "发布成功！！", this);
				}else{
					Ext.Msg.alert("提示", r.errmsg, this);
				}
			},
			scope : this
		});
	},
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : true,
			labelWidth : 72,
			labelAlign : 'right',
			fileUpload : true,
			defaultType : 'textfield',
			defaults : {
				anchor : "-20"
			},
			items : [
					{
						xtype : "hidden",
						name : "id"
					},{xtype:"hidden",name:'templateId'},
					{
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
					},
					{
						fieldLabel : '父级菜单',
						name : 'parent',
						xtype : "treecombo",
						hiddenName : "parentId",
						displayField : "title",
						valueField : "id",
						tree : new Ext.tree.TreePanel({
							autoScroll : true,
							rootVisible:false,
							root : new Ext.tree.AsyncTreeNode({
								id : "root",
								text : "所有菜单",
								expanded : true,
								iconCls : 'treeroot-icon',
								loader : Global.menuLoader,
								types : "0"
							})
						})
					},
					{
						fieldLabel : "菜单名称",
						name : "name",
						emptyText : '菜单名称能为空',
						allowBlank : false,
						blankText : '菜单名称不能为空'
					},
					{
						fieldLabel : "菜单编码",
						name : "menuKey",
						emptyText : '菜单编码值能为空',
						allowBlank : false,
						blankText : '菜单编码不能为空'
					},
					{
						xtype : "combo",
						name : "type",
						hiddenName : "type",
						fieldLabel : "菜单类型",
						allowBlank : false,
						blankText : '菜单类型不能为空',
						displayField : "title",
						valueField : "value",
						store : new Ext.data.SimpleStore({
							fields : [ 'title', 'value' ],
							data : this.type
						}),
						listeners : {
							select : this.changeType,
							scope : this
						},
						editable : false,
						mode : 'local',
						triggerAction : 'all'
					},new Ext.ux.form.SearchField({
						fieldLabel : "消息模版",
					  	name:'templateName',
					  	disabled:true,
						onTrigger2Click:this.addTemplate,
						scope:this
					}),
					{
						fieldLabel : '关联栏目',
						name : 'newsDir',
						xtype : "treecombo",
						hiddenName : "newsDirId",
						displayField : "title",
						valueField : "id",
						tree : new Ext.tree.TreePanel({
							autoScroll : true,
							rootVisible:false,
							root : new Ext.tree.AsyncTreeNode({
								id : "root",
								text : "所有栏目",
								expanded : true,
								iconCls : 'treeroot-icon',
								loader : Global.newsDirLoader,
								types : "0"
							})
						}),
						listeners : {
							select : function(v) {
								var val = v.getValue();
								if (val && val != "0") {
									this.fp.form.findField("templateName").setOriginalValue(null);
									this.fp.form.findField("templateId").setValue(null);
								}
							},
							scope : this
						}
					}, {
						xtype : "combo",
						name : "newsDocOrderBy",
						hiddenName : "newsDocOrderBy",
						fieldLabel : "关联条件",
						blankText : '查询条件不能为空',
						displayField : "title",
						valueField : "value",
						rootVisible:false,
						value:'0',
						store : new Ext.data.SimpleStore({
							fields : [ 'title', 'value' ],
							data : [['无条件','0'],[ '推荐', '1' ], [ '点击量', '2' ] ]
						}),
						editable : false,
						mode : 'local',
						triggerAction : 'all'
					}, {
						fieldLabel : "链接地址",
						name : "url",
						emptyText : '链接地址能为空',
						allowBlank : false,
						blankText : '链接地址不能为空'
					} ]
		});
		return formPanel;
	},
	addTemplate:function(button){
		var _this = this.scope;
		var type = _this.fp.findField("type").getValue();
		if(type!="click"){
			return;
		}
		var account = _this.fp.findField("account");
		if(!account.getValue()){
			Ext.Msg.alert("提示", "请选择所属微信号!!!", this);
			return;
		}
		TemplateListToAutoPanel.prototype.baseQueryParameter={type:'',accountId:account.getValue()};
		if(!_this.templatetoAutoList){
			_this.templatetoAutoList = new TemplateListToAutoPanel();
		}
		if(!_this.templateWin){
			_this.templateWin = new Ext.Window({
				title:"选择模版",
				closeAction:'hide', 
				width:900,
				height:550,
				modal:true,
				layout:"fit",
				items:[_this.templatetoAutoList]
			});
			_this.templatetoAutoList.grid.on("rowdblclick",  function(grid, index){
	            var r = _this.templatetoAutoList.store.getAt(index);
	            _this.fp.findField("templateId").setValue(r.get("id"));
	            _this.fp.findField("templateName").setValue(r.get("title"));
	            _this.fp.form.findField("newsDir").setOriginalValue(null);
	            _this.fp.form.findField("newsDocOrderBy").setOriginalValue(null);
	            _this.templateWin.hide();
	        }, _this);
		}
		_this.templatetoAutoList.store.load();
		_this.templateWin.show();
	},
	changeType : function(v) {
		var type = this.fp.findField("type").getValue();
		var urlele = this.fp.findField("url");
		var templateele = this.fp.findField("templateName");
		var newsDirteele = this.fp.findField("newsDir");
		var newsdocele = this.fp.findField("newsDocOrderBy");
		if (type == "click") {
			urlele.setOriginalValue();
			urlele.setDisabled(true);

			newsDirteele.setDisabled(false);
			newsdocele.setDisabled(false);
		} else {
			urlele.setDisabled(false);

			templateele.setOriginalValue();
			templateele.setDisabled(true);

			newsDirteele.setOriginalValue();
			newsDirteele.setDisabled(true);

			newsdocele.setOriginalValue();
			newsdocele.setDisabled(true);
			
			this.fp.findField("templateId").setValue(null)
		}
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
		return this.initWin(550, 320, "微信菜单管理", callback, autoClose);
	},
	storeMapping : [ "id", "name", "menuKey", "type", "url", "orders",
			"account", "parent", "children", "template", "newsDocOrderBy",
			"newsDir" ],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([ {
			header : "菜单名称",
			sortable : true,
			width : 160,
			dataIndex : "name"
		}, {
			header : "菜单编码",
			sortable : true,
			width : 160,
			dataIndex : "menuKey"
		}, {
			header : "所属微信号",
			sortable : true,
			width : 160,
			dataIndex : "account",
			renderer : this.objectRender("name")
		}, {
			header : "父级菜单",
			sortable : true,
			width : 160,
			dataIndex : "parent",
			renderer : this.objectRender("name")
		}, {
			header : "链接地址",
			sortable : true,
			width : 160,
			dataIndex : "url"
		}, {
			header : "菜单类型",
			sortable : true,
			width : 160,
			dataIndex : "type",
			renderer : this.typeRender
		} ]);
		this.gridButtons = [ {
			text : "发布",
			iconCls : "upload-icon",
			handler : this.fabutoweixin,
			scope : this
		}, {
			text : "上移",
			iconCls : "upload-icon",
			handler : this.swapSequence(""),
			scope : this
		}, {
			text : "下移",
			iconCls : "down",
			handler : this.swapSequence(true),
			scope : this
		} ];
		MenuGridListPanel.superclass.initComponent.call(this);
		this.on("saveobject", this.reloadLeftTree, this);
		this.on("removeobject", this.reloadLeftTree, this);
	},
	listeners : {
		render : function(e) {
		}
	}
});
// 模版栏目分类
MenuManagePanel = function() {
	this.list = new MenuGridListPanel({
		region : "center",
		width : 900
	});
	this.tree = new Ext.tree.TreePanel({
		title : "自定义菜单信息",
		region : "west",
		autoScroll : true,
		width : 145,
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
			text : "所有菜单",
			iconCls : 'treeroot-icon',
			expanded : true,
			loader : Global.menuLoader
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
	this.accountTree = new Ext.tree.TreePanel({
		title : "微信帐号信息",
		region : "west",
		autoScroll : true,
		width : 135,
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
	this.list.accountTree = this.accountTree;
	this.accountTree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		this.list.store.baseParams.accountId = id;
		this.list.store.baseParams.parentId = "";
		if (id) {
			this.list.account = {
				id : id,
				title : node.text
			};
		} else {
			this.list.account = null;
		}
		Global.menuLoader.baseParams.accountId = id;
		this.tree.root.removeAll();
		this.tree.root.reload();
		
		this.list.store.removeAll();
		this.list.store.load();
	}, this);
	MenuManagePanel.superclass.constructor.call(this, {
		id : "menuManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",
		items : [ this.accountTree, {region:"center",layout : "border",items:[this.tree, this.list]}]
	});
};
//模版消息
TemplateListToAutoPanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "templateListToAutoPanel",
    baseUrl: "template.java",
	autoLoadGridData:false,
    pageSize: 15,
    showView: false,
	showAdd: false,
    showEdit: false,
    showRemove:false,
    storeMapping : [ "id", "title", "content","type", "inputDate", "account", "newsItemList","mediaPath","thumbMediaPath"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
			header : "模版名称",
			sortable : true,
			width : 160,
			dataIndex : "title"
		},{
			header : "模版内容",
			sortable : true,
			width : 160,
			dataIndex : "content"
		},{
			header : "模版类型",
			sortable : true,
			width : 160,
			dataIndex : "type",
			renderer:function(v){
				if(v=="1"){
					return "文本消息";
				}else if(v=="2"){
					return "图文消息";
				}else if(v=="3"){
					return "音乐消息";
				}else if(v=="4"){
					return "图片消息";
				}else if(v=="5"){
					return "音频消息";
				}else if(v=="6"){
					return "视频消息";
				}
			}
		},{
			header : "详情",
			sortable : true,
			width : 160,
			dataIndex : "id",
			renderer: function(v,ele,e){
				if(e.data.type!="1"){
					return '<a style="color:blue;" href="/template.java?cmd=showadditem&id='+v+'" target="_blank">查看详情</a>';
				}else{
					return "无";
				}
			}
		},]);
        TemplateListToAutoPanel.superclass.initComponent.call(this);
    }
});
Ext.extend(MenuManagePanel, Ext.Panel, {});