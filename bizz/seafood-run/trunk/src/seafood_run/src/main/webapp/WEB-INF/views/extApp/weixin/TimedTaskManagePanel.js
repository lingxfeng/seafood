// 微信账号管理
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

//所选群
Global.group = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "followerGroup.java?cmd=getTree",
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


TimedTaskManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "timedTaskManagePanel",
	baseUrl : "timedTask.java",
	showView: false,
	
	type :[["订阅号","1"],["服务号","2"]],
	hourType:[["0","00"],["1","01"],["2","02"],["3","03"],["4","04"],["5","05"],["6","06"],["7","07"],["8","08"],["9","09"],["10","10"],["11","11"],["12","12"],
	["13","13"],["14","14"],["15","15"],["16","16"],["17","17"],["18","18"],["19","19"],["20","20"],["21","21"],["22","22"],["23","23"]],
	dateType:[["1","01"],["2","02"],["3","03"],["4","04"],["5","05"],["6","06"],["7","07"],["8","08"],["9","09"],["10","10"],["11","11"],["12","12"],
	["13","13"],["14","14"],["15","15"],["16","16"],["17","17"],["18","18"],["19","19"],["20","20"],["21","21"],["22","22"],["23","23"],
	["24","24"],["25","25"],["26","26"],["27","27"],["28","28"],["29","29"],["30","30"],["31","31"]],
	minuteType:[["0","00"],["15","15"],["30","30"],["45","45"]],
	msgtype:[["文本消息","1"],["图文消息","2"],["音乐消息","3"],["图片消息","4"],["语音消息","5"],["视频消息","6"]],
	sentType:[["发送所有粉丝","1"],["发送群","2"],["发送24小时关注","3"]],
	typeRender:function(v){
		if(v=="1"){
			return "订阅号";
		}else{
			return "服务号";
		}
	
	},
	fslx:function(v){
		if(v=="1"){
			return "发送给所有关注的人";
		}else if(v=="2"){
			return "发送群用户";
		}else if(v=="3"){
			return "发送24小时之内有操作的人";
		}
	
	},
	onCreate : function() {
		if (this.sUser) {
			this.fp.form.findField("sUser").setOriginalValue(this.sUser);
		}
	},
	edit : function() {
		var win = TimedTaskManagePanel.superclass.edit.call(this);
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
			var account = record.get("account");
			account.title=account.name;
			this.fp.form.findField("account").setOriginalValue(account);
			
			var template = record.get("template");
			Ext.getCmp('templateId').setValue(template.id);
	        Ext.getCmp('templateName').setValue(template.title);
			
		}
	},
	
	addTemplate:function(button){
		var _this = this.scope;
		var typeVal = Ext.getCmp('msgTypeMessage');
		if(!_this.templatetoAutoList){
			_this.templatetoAutoList = new TemplateListToAutoPanel();
		}
		if(!_this.templateWin){
			_this.templateWin = new Ext.Window({
				title:"添加模版",
				closeAction:'hide', 
				width:500,
				height:400,
				modal:true,
				layout:"fit",
				items:[_this.templatetoAutoList]
			});
			_this.templatetoAutoList.grid.on("rowdblclick",  function(grid, index){
	            var r = _this.templatetoAutoList.store.getAt(index);
	            
	            Ext.getCmp('templateId').setValue(r.get('id'));
	            Ext.getCmp('templateName').setValue(r.get('title'));
	            _this.templateWin.hide();
	        }, _this);
		}
		_this.templatetoAutoList.store.baseParams={type:typeVal.value};
		_this.templatetoAutoList.store.load();
		_this.templateWin.show();
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
					},{
						xtype : "hidden",
						name : "templateId",
						id:'templateId'
					},
					Disco.Ext.Util.twoColumnPanelBuild({
						fieldLabel : "标题",
						name : "title",
						emptyText : '帐号名称不能为空',
						allowBlank : false,
						blankText : '帐号名称不能为空'
					},{
						fieldLabel : '选择账号',
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
						name : "state",
						hiddenName : "state",
						fieldLabel : "发送类型",
						allowBlank : false,
						displayField : "title",
						valueField : "value",
						store : new Ext.data.SimpleStore({
							fields : [ 'title', 'value' ],
							data : this.sentType
						}),
						value:"1",
						editable : false,
						mode : 'local',
						triggerAction : 'all',
						listeners:{
                        	select:function(v){
                        		var qz=Ext.getCmp("qz");
                        		if(v.value=="2"){
                        			qz.allowBlank = false;
                        			qz.setDisabled(false);
                        		}else{
                        			qz.allowBlank = true;
                        			qz.setValue('');
									qz.setDisabled(true);
                        		}
                        	}
                        }
					},{
						fieldLabel : '选择群',
						id:"qz",
						name : 'followerGroup',
						disabled : true,
						xtype : "treecombo",
						hiddenName : "followerGroupId",
						displayField : "title",
						valueField : "id",
						blankText : '发送类型为发送群所选群不能为空',
						tree : new Ext.tree.TreePanel({
							autoScroll : true,
							rootVisible:false,
							root : new Ext.tree.AsyncTreeNode({
								id : "root",
								text : "选择群",
								expanded : true,
								iconCls : 'treeroot-icon',
								loader : Global.group,
								types : "0"
							})
						})
					},{
                    	xtype : "combo",
						name : "date",
						hiddenName : "date",
						fieldLabel : "日",
						displayField : "title",
						valueField : "value",
						store : new Ext.data.SimpleStore({
							fields : [ 'title', 'value' ],
							data : this.dateType
						}),
						editable : false,
						mode : 'local',
						triggerAction : 'all'
                    },{
                    	xtype : "combo",
						name : "hour",
						hiddenName : "hour",
						fieldLabel : "时",
						allowBlank : false,
						displayField : "title",
						valueField : "value",
						store : new Ext.data.SimpleStore({
							fields : [ 'title', 'value' ],
							data : this.hourType
						}),
						value:"0",
						editable : false,
						mode : 'local',
						triggerAction : 'all'
                    },{
						xtype : "combo",
						name : "minute",
						hiddenName : "minute",
						fieldLabel : "分钟",
						allowBlank : false,
						displayField : "title",
						valueField : "value",
						store : new Ext.data.SimpleStore({
							fields : [ 'title', 'value' ],
							data : this.minuteType
						}),
						value:"0",
						editable : false,
						mode : 'local',
						triggerAction : 'all'
					},{
                    	id:"msgTypeMessage",
						xtype : "combo",
						name : "msgType",
						hiddenName : "msgType",
						fieldLabel : "消息类型",
						allowBlank : false,
						width:450,
						displayField : "title",
						valueField : "value",
						store : new Ext.data.SimpleStore({
							fields : [ 'title', 'value' ],
							data : this.msgtype
						}),
						value:"1",
						editable : false,
						mode : 'local',
						triggerAction : 'all'
						/*listeners:{
							select:function(v){
								var msgType = v.value;
								var contentMsg = Ext.getCmp('description');
								if(msgType!="1"){
									contentMsg.setValue('');
									contentMsg.setDisabled(true);
								}else{
									contentMsg.setDisabled(false);
								}
							}
						}*/
					},new Ext.ux.form.SearchField({
						width:450,
						fieldLabel : "消息模版",
						id:'templateName',
					  	name:'templateId',
					  	disabled:true,
						onTrigger2Click:this.addTemplate,
						scope:this
					})),{
						xtype: "textarea",
						fieldLabel : "描述",
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
	storeMapping : [ "id", "title", "state", "account", "template", "date", "hour", "minute","description"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([{
			header : "标题",
			sortable : true,
			width : 160,
			dataIndex : "title"
		},{
			header : "发送类型",
			sortable : true,
			width : 160,
			dataIndex : "state",
			renderer : this.fslx
		},{
			header : "帐号类型",
			sortable : true,
			width : 160,
			dataIndex : "type",
			renderer : this.typeRender
		},{
			header : "账号名称",
			sortable : true,
			width : 160,
			dataIndex : "account",
			renderer : this.objectRender("name")
		},{
			header : "模板名称",
			sortable : true,
			width : 160,
			dataIndex : "template",
			renderer : this.objectRender("title")
		},{
			header : "天",
			sortable : true,
			width : 160,
			dataIndex : "date"
		},{
			header : "小时",
			sortable : true,
			width : 160,
			dataIndex : "hour"
		},{
			header : "分钟",
			sortable : true,
			width : 160,
			dataIndex : "minute"
		},{
			header : "描述",
			sortable : true,
			width : 160,
			dataIndex : "description"
		}]);
		TimedTaskManagePanel.superclass.initComponent.call(this);
		this.on("saveobject", this.reloadLeftTree, this);
		this.on("removeobject", this.reloadLeftTree, this);
	},
	listeners : {
		render : function(e) {
		}
	}
});

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
		}]);
        TemplateListToAutoPanel.superclass.initComponent.call(this);
    }
});
