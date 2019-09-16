//微信模版管理
if (typeof Global === "undefined") {
	Global = {};
}
//微信帐号数
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
TemplateGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "templateGridListPanel",
	baseUrl : "template.java",
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
			return "语音消息";
		}else if(v=="6"){
			return "视频消息";
		}
	},
	onCreate : function() {
		if (this.account) {
			this.fp.form.findField("account").setOriginalValue(this.account);
		}
		this.fp.form.findField('account').setDisabled(false);
		this.fp.form.findField("type").setDisabled(false);
	},
	edit : function() {
		var win = TemplateGridListPanel.superclass.edit.call(this);
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
			var account = record.get("account");
			account.title=account.name;
			this.fp.form.findField("account").setOriginalValue(account);
			this.fp.form.findField('account').setDisabled(true);
			this.fp.form.findField("type").setDisabled(true);
			var msgType = record.get("type");
			var mediaPath = this.fp.form.findField("mediaPath");
			var thumbMediaPath = this.fp.form.findField('thumbMediaPath');
			if(msgType=="1" || msgType=="2"){
				mediaPath.setValue('');
				mediaPath.setDisabled(true);
				thumbMediaPath.setValue('');
				thumbMediaPath.setDisabled(true);
			}else if(msgType=="3" || msgType=="6"){
				mediaPath.setDisabled(false);
				thumbMediaPath.setDisabled(false);
			}else if(msgType=="4" || msgType=="5"){
				mediaPath.setDisabled(false);
				thumbMediaPath.setValue('');
				thumbMediaPath.setDisabled(true);
			}
		}
	},
	caozuohref:function(t,obj,j){
		if(j.data.type =="2"){
			return '<a style="color:blue;" href="/template.java?cmd=showadditem&id='+j.id+'" target="_blank">查看图文</a>';
		}else{
			return '<span>无</span>';
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
			items : [{
						xtype : "hidden",
						name : "id"
					},
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
					},{
						xtype : "combo",
						name : "type",
						hiddenName : "type",
						fieldLabel : "模版类型",
						allowBlank : false,
						blankText : '模版类型不能为空',
						displayField : "title",
						valueField : "value",
						value:"1",
						store : new Ext.data.SimpleStore({
							fields : [ 'title', 'value' ],
							data : this.type
						}),
						editable : false,
						mode : 'local',
						triggerAction : 'all',
						listeners:{
							select:function(v){
								var msgType = v.value;
								var mediaPath = this.fp.form.findField("mediaPath");
								var thumbMediaPath = this.fp.form.findField('thumbMediaPath');
								if(msgType=="1" || msgType=="2"){
									mediaPath.setValue('');
									mediaPath.setDisabled(true);
									thumbMediaPath.setValue('');
									thumbMediaPath.setDisabled(true);
								}else if(msgType=="3" || msgType=="6"){
									mediaPath.setDisabled(false);
									thumbMediaPath.setDisabled(false);
								}else if(msgType=="4" || msgType=="5"){
									mediaPath.setDisabled(false);
									thumbMediaPath.setValue('');
									thumbMediaPath.setDisabled(true);
								}
							},
							scope:this
						}
					},{
						fieldLabel : "模版名称",
						name : "title",
						emptyText : '模版名称不能为空',
						allowBlank : false,
						blankText : '模版名称不能为空',
						xtype:"textfield"
					},{
						xtype : 'fileuploadfield',
						emptyText : '单击右侧按钮选择上传的多媒体',
						fieldLabel : "多媒体文件",
						name : "mediaPath",
						allowBlank : false,
						disabled:true,
						buttonCfg : {
							text : '',
							iconCls : 'upload-icon'
						}
					},{
						xtype : 'fileuploadfield',
						emptyText : '单击右侧按钮选择上传的图片',
						fieldLabel : "缩略图",
						name : "thumbMediaPath",
						allowBlank : false,
						disabled:true,
						buttonCfg : {
							text : '',
							iconCls : 'upload-icon'
						}
					},{
						xtype: "textarea",
						fieldLabel : "模版简介",
						allowBlank : false,
						name : "content"
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
		return this.initWin(550, 300, "消息模版管理", callback, autoClose);
	},
	storeMapping : [ "id", "title", "content","type", "inputDate", "account", "newsItemList","mediaPath","thumbMediaPath"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([{
			header : "模版名称",
			sortable : true,
			width : 160,
			dataIndex : "title"
		}, {
			header : "模版类型",
			sortable : true,
			width : 160,
			dataIndex : "type",
			renderer : this.typeRender
		},{
			header : "录入日期",
			sortable : true,
			width : 160,
			dataIndex : "inputDate",
			renderer: this.dateRender()
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
		}]);
		this.gridButtons = [{
			text : "编辑图文",
			iconCls: "upload-icon",
			handler : this.editeNewsItem,
			scope : this
		}];
		TemplateGridListPanel.superclass.initComponent.call(this);
	},
	editeNewsItem:function(){
		var record = this.grid.getSelectionModel().getSelected();
		if(!record){
			Ext.MessageBox.alert("警告","请选择一条记录！！");
			return;
		}
		var type = record.get("type");
		if(type !="2"){
			Ext.MessageBox.alert("警告","普通类型模版不能进行图文编辑，请重新选择！！！");
			return;
		}
		if(!this.nesItemlist){
			this.nesItemlist = new NewsItemGridListPanel();
		}
		this.nesItemlist.ptlId = record.get("id");
		if(!this.newsItemWin){
			this.newsItemWin = new Ext.Window({
				title:"图文编辑",
				closeAction:'hide', 
				width:750,
				height:400,
				modal:true,
				layout:"fit",
				items:[this.nesItemlist]
			});
		}
		this.nesItemlist.store.baseParams={tplId:record.get("id")};
		this.nesItemlist.store.load();
		this.newsItemWin.show();
	}
});
// 模版栏目分类
TemplateManagePanel = function() {
	this.list = new TemplateGridListPanel({
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
	TemplateManagePanel.superclass.constructor.call(this, {
		id : "templateManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",
		items : [ this.tree, this.list ]
	});
};

Ext.extend(TemplateManagePanel, Ext.Panel, {});

//图文消息明细
if (typeof Global === "undefined") {
	Global = {};
}
//微信帐号数
NewsItemGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "newsItemGridListPanel",
	baseUrl : "newsItem.java",
	singleWindowMode:true,
	pagingToolbar: false,
	autoLoadGridData:false,
	baseQueryParameter : {
		orgType : 1
	},
	height : 300,
	showView: false,
	onCreate:function(){
		this.fp.findField("tplId").setValue(this.ptlId);
		this.editor.html("");
		this.editor.sync();
	},
	edit : function() {
		var win = NewsItemGridListPanel.superclass.edit.call(this);
		var record = this.grid.getSelectionModel().getSelected();
		this.fp.findField("tplId").setValue(record.get("tpl").id);
		Ext.Ajax.request({
			scope : this,
			url : this.baseUrl + "?cmd=getContent&id="
					+ record.get('id'),
			success : function(response) {
				var data = Ext.decode(response.responseText);
				data != null || (data = "");
				this.editor.html(data);
				this.editor.sync();
			}
		});
	},
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 60,
            fileUpload: true,
            labelAlign: 'right',
            defaults: {
                xtype: "textfield"
            },
            items: [{
                xtype: "hidden",
                name: "id"
            },{
                xtype: "hidden",
                name: "tplId"
            },Disco.Ext.Util.twoColumnPanelBuild({
                fieldLabel: "消息名称",
                name: "title",
                emptyText : '消息名称不能为空',
				allowBlank : false,
				blankText : '消息名称不能为空'
            },{
                fieldLabel: "作&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;者",
                name: "author",
                emptyText : '作者不能为空',
				allowBlank : false,
				blankText : '作者不能为空'
            },{
                fieldLabel: "外链地址",
                name: "url"
            },{
				xtype : 'fileuploadfield',
				emptyText : '单击右侧按钮选择上传的图片',
				fieldLabel : "图文图片",
				name : "imagePath",
				buttonCfg : {
					text : '',
					iconCls : 'upload-icon'
				}
			}),{
                xtype: "textarea",
                fieldLabel: "消息简介",
                name: "description",width: 825,
                height: 50
            },{
                xtype: "textarea",
                fieldLabel: "消息内容",
                name: "content",
                width : 900,
				height : 395,
				margins : {
					left : 10
				},
				hideLabel : true,
            }],
            listeners : {
				scope : this,
				'render' : function() {
					var me = this;
					setTimeout(
							function() {
								// 动态加载kindeditor富文本编辑器
								var plugins = '/static/common/plugin/kindeditor/kindeditor-min.js';
								Disco
										.loadJs(
												plugins,
												false,
												function() {
													KindEditor.basePath = '/static/common/plugin/kindeditor/';
													var id = me.fp.form
															.findField('content').id;
													me.editor = KindEditor.create(
																	'#'+ id,
																	{
																		// 所有的上传文件POST地址
																		uploadJson : '/fileUpload.java',
																		// 指定浏览远程图片的URL处理地址
																		fileManagerJson : '/fileUpload.java?cmd=showByKindeditor',
																		// true时显示浏览远程服务器按钮
																		afterBlur : function() {
																			this.sync();// 编辑器失去焦点时同步KindEditor的值到textarea文本框
																		}
																	});
												},
												null,
												Disco.ajaxCache);
							}, 1);
				}
			}
        });
		return formPanel;
	},
	reloadLeftTree : function() {
	},
	createWin : function(callback, autoClose) {
		return this.initWin(940, 580, "图文消息管理", callback, autoClose);
	},
	storeMapping: ["id", "title", "author",  "imagePath","content","description","tpl","orders","newType","url","createDate"],
	initComponent : function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "消息名称",
            sortable: true,
            width: 100,
            dataIndex: "title"
        }, {
        	header: "创建日期",
            dataIndex: "createDate",
            sortable: true,
            width: 100,
            renderer: this.dateRender()
        },{
        	header: "所属模版",
            dataIndex: "tpl",
            sortable: true,
            width: 100,
            renderer: this.objectRender("title")
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
		} ];
        NewsItemGridListPanel.superclass.initComponent.call(this);
    }
});
