// 合作伙伴管理
SupportLogicManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
	id : "supportLogicManagePanel",
	// title:"网站发布管理",
	baseUrl : "supportLogic.java",
	gridSelModel : 'checkbox',
	batchRemoveMode : true,
	pageSize : 20,
	showView : false,
	edit : function() {
		var win = SupportLogicManagePanel.superclass.edit.call(this);
	},
	status : [["启用", "1"], ["停用", "0"]],
	statusFormat : function(v) {
		if (v == 1) {
			return "启用";
		} else {
			return "停用";
		}
	},
	target : [["在新窗口打开", "_blank"], ["在相同的框架中打开", "_self"],
			["在父框架集中打开", "_parent"], ["在整个窗口中打开", "_top"]],
	targetFormat : function(v) {
		if (v == "_blank") {
			return "在新窗口打开";
		} else if (v == "_self") {
			return "在相同的框架中打开";
		} else if (v == "_parent") {
			return "在父框架集中打开";
		} else if (v == "_top") {
			return "在整个窗口中打开";
		}
	},
	logoImgRender : function(value, p, record) {
		if (value != null && value != "") {
			return String.format('{1}<b><a style="color:green" href="' + value
							+ '" target="_blank">&nbsp查看</a></b><br/>', "", "")
		}
	},
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : true,
			labelWidth : 80,
			fileUpload : true,
			labelAlign : 'right',
			defaults : {
				width : 320,
				xtype : "textfield"
			},
			items : [{
						xtype : "hidden",
						name : "id"
					}, {
						fieldLabel : "名称",
						name : "name",
						emptyText : '名称不能为空',
						allowBlank : false,
						blankText : '名称不能为空'
					}, {
						fieldLabel : "链接地址",
						name : "linkUrl",
						emptyText : '链接地址不能为空',
						allowBlank : false,
						blankText : '链接地址不能为空'
					}, {
						xtype : 'fileuploadfield',
						emptyText : '单击上传按钮选择图片',
						fieldLabel : 'LOGO图标',
						allowBlank : false,
						blankText : '上传文件不能为空',
						name : 'logoImg',
						buttonCfg : {
							text : '',
							iconCls : 'upload-icon'
						}
					}, {
						fieldLabel : "LOGO图标链接地址",
						name : "logoUrl"
					}, {
						xtype : "combo",
						name : "target",
						hiddenName : "target",
						fieldLabel : "打开方式",
						displayField : "title",
						valueField : "value",
						store : new Ext.data.SimpleStore({
									fields : ['title', 'value'],
									data : this.target
								}),
						editable : false,
						mode : 'local',
						triggerAction : 'all',
						emptyText : '请选择...'
					}, {

						fieldLabel : '启用状态',
						xtype : 'radiogroup',
						name : 'status',
						items : [{
									checked : true,
									boxLabel : '启用',
									name : 'status',
									inputValue : "1"
								}, {
									boxLabel : '停用',
									name : 'status',
									inputValue : "0"
								}]

					}, {
						xtype : "datefield",
						editable : false,
						format : 'Y-m-d H:i:s',
						fieldLabel : "生效日期",
						name : "startDate",
						listeners : {
							blur : function() {
								var startDateval = formPanel.form
										.findField("startDate").value;
								var endDateval = formPanel.form
										.findField("endDate").value;
								if (startDateval != null && startDateval != ""
										&& endDateval != null
										&& endDateval != "") {
									if (startDateval > endDateval) {
										formPanel.form.findField("startDate")
												.setOriginalValue(null);
										Ext.MessageBox.show({
													title : "警告",
													msg : "生效日期不能大于失效日期！！！",
													buttons : Ext.MessageBox.OK,
													icon : Ext.MessageBox.WARNING
												});
									}
								}
							}
						}
					}, {
						xtype : "datefield",
						editable : false,
						format : 'Y-m-d H:i:s',
						fieldLabel : "失效日期",
						name : "endDate",
						listeners : {
							blur : function() {
								var startDateval = formPanel.form
										.findField("startDate").value;
								var endDateval = formPanel.form
										.findField("endDate").value;;
								if (startDateval != null && startDateval != ""
										&& endDateval != null
										&& endDateval != "") {
									if (startDateval > endDateval) {
										formPanel.form.findField("endDate")
												.setOriginalValue(null);
										Ext.MessageBox.show({
													title : "警告",
													msg : "生效日期不能大于失效日期！！！",
													buttons : Ext.MessageBox.OK,
													icon : Ext.MessageBox.WARNING
												});
									}
								}
							}
						}
					}, {
						xtype : "textarea",
						fieldLabel : "简介",
						name : "intro",
						height : 50
					}]
		});
		return formPanel;
	},
	userRender : function(v) {
		return v ? v.trueName : "$!{lang.get('Unknown')}";
	},
	createWin : function() {
		return this.initWin(448, 400, "快捷链接管理");
	},
	storeMapping : ["id", "name", "linkUrl", "target", "createTime", "intro",
			"sequence", "status", "ppt", "logoImg", "logoUrl", "startDate",
			"endDate"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([{
					header : "关键字",
					sortable : true,
					width : 100,
					dataIndex : "name"
				}, {
					header : "功能处理类",
					sortable : true,
					width : 100,
					dataIndex : "linkUrl"
				}, {
					header : "功能名称",
					sortable : true,
					width : 100,
					dataIndex : "target",
					renderer : this.targetFormat
				}, {
					header : "功能简介",
					sortable : true,
					width : 100,
					dataIndex : "logoImg",
					renderer : this.logoImgRender
				}, {
					header : "操作",
					sortable : true,
					width : 100,
					dataIndex : "logoUrl"
				}]);
		this.gridButtons = [{
					text : "上移",
					iconCls : "upload-icon",
					handler : this.swapSequence(""),
					scope : this
				}, {
					text : "下移",
					iconCls : "down",
					handler : this.swapSequence(true),
					scope : this
				}];
		SupportLogicManagePanel.superclass.initComponent.call(this);
	}
});
