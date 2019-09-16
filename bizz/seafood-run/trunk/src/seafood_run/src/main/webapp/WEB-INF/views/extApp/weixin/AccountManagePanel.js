// 微信账号管理
if (typeof Global === "undefined") {
	Global = {};
}
AccountGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "accountGridListPanel",
	baseUrl : "account.java",
	showView: false,
	type:[["订阅号","1"],["服务号","2"]],
	typeRender:function(v){
		if(v=="1"){
			return "订阅号";
		}else{
			return "服务号";
		}
	
	},
	onCreate : function() {
		if (this.sUser) {
			this.fp.form.findField("sUser").setOriginalValue(this.sUser);
		}
	},
	edit : function() {
		var win = AccountGridListPanel.superclass.edit.call(this);
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
			var sUser = record.get("sUser");
			sUser.title=sUser.name;
			console.dir(sUser)
			this.fp.form.findField("sUser").setOriginalValue(sUser);
		}
	},
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : true,
			labelWidth : 60,
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
						fieldLabel : "帐号名称",
						name : "name",
						emptyText : '帐号名称不能为空',
						allowBlank : false,
						blankText : '帐号名称不能为空'
					},{
						fieldLabel : "账号编码",
						name : "code",
						emptyText : '账号编码不能为空',
						allowBlank : false,
						blankText : '账号编码不能为空'
					},{
						fieldLabel : "服务令牌",
						name : "token",
						emptyText : '服务令牌不能为空',
						allowBlank : false,
						blankText : '服务令牌不能为空'
					},{
						fieldLabel : "微信号码",
						name : "number",
						emptyText : '微信号码不能为空',
						allowBlank : false,
						blankText : '微信号码不能为空'
					},{
						xtype : "combo",
						name : "type",
						hiddenName : "type",
						fieldLabel : "帐号类型",
						allowBlank : false,
						blankText : '帐号类型不能为空',
						displayField : "title",
						valueField : "value",
						store : new Ext.data.SimpleStore({
							fields : [ 'title', 'value' ],
							data : this.type
						}),
						editable : false,
						mode : 'local',
						triggerAction : 'all'
					},{
						fieldLabel : "原始ID号",
						name : "accountid"
					},{
						fieldLabel : "应用ID号",
						name : "appid"
					},{
						fieldLabel : "应用密钥",
						name : "appsecret"
					},{
						xtype : 'fileuploadfield',
						emptyText : '单击右侧按钮选择上传的多媒体',
						fieldLabel : "默认图片",
						name : "imgPath",
						buttonCfg : {
							text : '',
							iconCls : 'upload-icon'
						}
					},{
						xtype : 'fileuploadfield',
						fieldLabel : "二维码",
						name : "qrcodeImg",
						buttonCfg : {
							text : '',
							iconCls : 'upload-icon'
						}
					},{
						fieldLabel : "处理类",
						name : "handlerName",
						allowBlank : true
					}),{
						xtype: "textarea",
						fieldLabel : "平台简介",
						height:110,
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
		return this.initWin(750, 320, "微信公众帐号管理", callback, autoClose);
	},
	storeMapping : [ "id", "name", "token", "number", "accountid", "type",
			"description", "appid","appsecret","addtoekntime","tenant","doMain","imgPath","handlerName","code","qrcodeImg"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([{
			header : "帐号名称",
			sortable : true,
			width : 160,
			dataIndex : "name"
		},{
			header : "微信号码",
			sortable : true,
			width : 160,
			dataIndex : "number"
		},{
			header : "帐号类型",
			sortable : true,
			width : 160,
			dataIndex : "type",
			renderer : this.typeRender
		},{
			header : "服务令牌",
			sortable : true,
			width : 160,
			dataIndex : "token"
		},{
			header : "二维码",
			sortable : true,
			width : 160,
			dataIndex : "qrcodeImg",
			renderer:function(v){
				return '<img width=100 height=100 src="'+v+'"/>';
			}
		}]);
		AccountGridListPanel.superclass.initComponent.call(this);
		this.on("saveobject", this.reloadLeftTree, this);
		this.on("removeobject", this.reloadLeftTree, this);
	},
	listeners : {
		render : function(e) {
		}
	}
});
