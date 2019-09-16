
SUserManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
	id : "sUserManagePanel",
	// title:"网站发布管理",
	baseUrl : "sUser.java",
	gridSelModel : 'checkbox',
	batchRemoveMode : true,
	pageSize : 20,
	showView : false,
	create:function(){
		SUserManagePanel.superclass.create.call(this);
		var tabs = this.fp.items.items[0];
		if(tabs.items.items[1]){
			tabs.remove(tabs.items.items[1])
		}
	},
	edit:function(){
		SUserManagePanel.superclass.edit.call(this);
		var tabs = this.fp.items.items[0];
		if(tabs.items.items[1]){
			tabs.remove(tabs.items.items[1])
		}
		Ext.Ajax.request({
			method:"post",
			url:"sRole.java?cmd=getTree",
			success:function(response){
				this.roles.items = eval("("+response.responseText+")");
				tabs.add(this.roles);
				this.setCheckbox();
			},
			scope:this
		});
		
	},
	setCheckbox:function(){
		var record = this.grid.getSelectionModel().getSelected();
		var sRoles = record.get("sRoles");
		for(var i = 0;i<sRoles.length;i++){
			this.fp.findById("roleId_"+sRoles[i]).setValue(true);
		}
	},
	roles:{
		xtype:"fieldset",
		title:"用户角色",
		fieldLabel:"所有角色",
		defaults:{
			xtype:"checkbox",
			name:"roleids"
		},
		items:[]
	},
	resetPassword:function(){
		var record = this.grid.getSelectionModel().getSelected();
		if(record){
			Ext.Ajax.request({
				method:"post",
				url:"sUser.java?cmd=update&type=resetPw&id="+record.get("id"),
				success:function(response){
					Ext.MessageBox.show({
						title : "通知",
						msg : "密码重置成功",
						icon : Ext.MessageBox.QUESTION,
						buttons : Ext.MessageBox.OK
					});
				},
				scope:this
			});
		}else{
			Ext.MessageBox.show({
				title : "通知",
				msg : "情选择一条记录",
				icon : Ext.MessageBox.WARNING,
				buttons : Ext.MessageBox.OK
			});
		}
	},
	status : [ [ "启用", "1" ], [ "禁用", "0" ] ],
	statusFormat : function(v) {
		if (v == 1) {
			return "启用";
		} else {
			return "停用";
		}
	},
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : false,
			labelWidth : 80,
			labelAlign : 'left',
			layout : "fit",
			items : [ {
				xtype : 'tabpanel',
				deferredRender : false,
				monitorResize : true,
				hideBorders : true,
				border : false,
				activeTab : 0,
				items : [ {
					title : '用户基本信息',
					frame : true,
					border : false,
					layout : 'form',
					defaults : {
						width : 320,
						xtype : "textfield"
					},
					items : [ {
						xtype : "hidden",
						name : "id"
					}, {
						fieldLabel : "用户名",
						name : "name",
						emptyText : '用户名不能为空',
						allowBlank : false,
						blankText : '用户名不能为空'
					}, {
						fieldLabel : "登录名",
						name : "loginName",
						emptyText : '登录名不能为空',
						allowBlank : false,
						blankText : '登录名不能为空'
					}, {
						fieldLabel : '启用状态',
						xtype : 'radiogroup',
						name : 'status',
						items : [ {
							checked : true,
							boxLabel : '启用',
							name : 'status',
							inputValue : "1"
						}, {
							boxLabel : '禁用',
							name : 'status',
							inputValue : "0"
						} ]

					}, {
						xtype : "textarea",
						fieldLabel : "描述",
						name : "description",
						height : 50
					} ]
				}]
			} ]
		});
		return formPanel;
	},
	userRender : function(v) {
		return v ? v.trueName : "$!{lang.get('Unknown')}";
	},
	createWin : function() {
		return this.initWin(448, 400, "用户管理");
	},
	storeMapping : [ "id", "name", "loginName", "password", "createDate",
			"description", "sequence", "status","sRoles" ],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([{
			header : "名称",
			sortable : true,
			width : 100,
			dataIndex : "name"
		}, {
			header : "登录名",
			sortable : true,
			width : 100,
			dataIndex : "loginName"
		}, {
			header : "创建日期",
			sortable : true,
			width : 100,
			dataIndex : "createDate",
			renderer : this.dateRender()
		}, {
			header : "启用状态",
			sortable : true,
			width : 100,
			dataIndex : "status",
			renderer : this.statusFormat
		} ]);
		 this.gridButtons = [{
				text : "重置密码",
				handler : this.resetPassword,
				iconCls: "x-btn-text refresh",
				scope : this
			}];
		SUserManagePanel.superclass.initComponent.call(this);
	}
});
