//自动回复管理
if (typeof Global === "undefined") {
	Global = {};
}
//微信帐号加载
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
AutoResponseGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "autoResponseGridListPanel",
	baseUrl : "autoResponse.java",
	baseQueryParameter : {
		orgType : 1
	},
	showView: false,
	type:[["文本消息","1"],["图文消息","2"],["音乐消息","3"],["图片消息","4"],["语音消息","5"],["视频消息","6"]],
	typeRender:function(v){
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
	},
	onCreate : function() {
		if (this.account) {
			this.fp.form.findField("account").setOriginalValue(this.account);
		}
	},
	edit : function() {
		var win = AutoResponseGridListPanel.superclass.edit.call(this);
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
			var account = record.get("account");
			account.title=account.name;
			this.fp.form.findField("account").setOriginalValue(account);
			var template = record.get("template");
			this.fp.form.findField("templateId").setOriginalValue(template.id);
			this.fp.form.findField("templateName").setOriginalValue(template.title);
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
					},{
						xtype : "hidden",
						name : "templateId"
					},{
						fieldLabel : "关键字词",
						name : "keyWord",
						emptyText : '关键字词不能为空',
						allowBlank : false,
						blankText : '关键字词不能为空'
					},{
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
						xtype : "combo",
						id:"msgType",
						name : "msgType",
						hiddenName : "msgType",
						fieldLabel : "消息类型",
						allowBlank : false,
						blankText : '消息类型不能为空',
						displayField : "title",
						valueField : "value",
						store : new Ext.data.SimpleStore({
							fields : [ 'title', 'value' ],
							data : this.type
						}),
						value:"1",
						editable : false,
						mode : 'local',
						triggerAction : 'all'
					},new Ext.ux.form.SearchField({
							fieldLabel : "消息模版",
						  	name:'templateName',
						  	disabled:true,
							onTrigger2Click:this.addTemplate,
							scope:this
						})]
		});
		return formPanel;
	},
	addTemplate:function(button){
		var _this = this.scope;
		var typeVal = _this.fp.findField("msgType").getValue();
		if(!_this.templatetoAutoList){
			_this.templatetoAutoList = new TemplateListToAutoPanel();
		}
		if(!_this.templateWin){
			_this.templateWin = new Ext.Window({
				title:"选择模版",
				closeAction:'hide', 
				width:500,
				height:400,
				modal:true,
				layout:"fit",
				items:[_this.templatetoAutoList]
			});
			_this.templatetoAutoList.grid.on("rowdblclick",  function(grid, index){
	            var r = _this.templatetoAutoList.store.getAt(index);
	            _this.fp.findField("templateId").setValue(r.get("id"));
	            _this.fp.findField("templateName").setValue(r.get("title"));
	            _this.templateWin.hide();
	        }, _this);
		}
		_this.templatetoAutoList.store.baseParams={type:typeVal};
		_this.templatetoAutoList.store.load();
		_this.templateWin.show();
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
		return this.initWin(350, 200, "关键字回复管理", callback, autoClose);
	},
	storeMapping : [ "id", "template","keyWord","msgType","inputDate", "account","template"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([{
			header : "关键字词",
			sortable : true,
			width : 160,
			dataIndex : "keyWord"
		},{
			header : "消息类型",
			sortable : true,
			width : 160,
			dataIndex : "msgType",
			renderer : this.typeRender
		},{
			header : "消息模版",
			sortable : true,
			width : 160,
			dataIndex : "template",
			renderer : this.objectRender("title")
		},{
			header : "添加日期",
			sortable : true,
			width : 160,
			dataIndex : "inputDate",
			renderer: this.dateRender()
		},{
			header : "详情",
			sortable : true,
			width : 160,
			dataIndex : "template",
			renderer: function(v){
				return '<a style="color:blue;" href="/template.java?cmd=showadditem&id='+v.id+'" target="_blank">查看详情</a>';
			}
		}]);
		AutoResponseGridListPanel.superclass.initComponent.call(this);
		this.on("saveobject", this.reloadLeftTree, this);
		this.on("removeobject", this.reloadLeftTree, this);
	},
	listeners : {
		render : function(e) {
		}
	}
});
AutoResponseManagePanel = function() {
	this.list = new AutoResponseGridListPanel({
		region : "center"
	});
	this.tree = new Ext.tree.TreePanel({
		title : "微信账户信息",
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
			text : "所有微信",
			iconCls : 'treeroot-icon',
			expanded : true,
			loader : Global.accountLoader
		})
	});
	this.list.tree = this.tree;
	this.tree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		this.list.store.baseParams.accountId = id;
		if (id) {
			this.list.account = {
				id : id,
				title : node.text
			};
		} else
			this.list.account = null;
		this.list.store.removeAll();
		this.list.store.load();
	}, this);
	AutoResponseManagePanel.superclass.constructor.call(this, {
		id : "autoResponseManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",
		items : [ this.tree, this.list ]
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
			header : "模版简介",
			sortable : true,
			width : 160,
			dataIndex : "content"
		},{
			header : "模版简介",
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
Ext.extend(AutoResponseManagePanel, Ext.Panel, {});
