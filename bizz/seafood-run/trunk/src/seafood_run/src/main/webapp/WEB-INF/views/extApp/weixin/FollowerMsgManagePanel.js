//粉丝消息管理
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
FollowerMsgGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "followerMsgGridListPanel",
	baseUrl : "follower.java",
	baseQueryParameter : {
		ismsg : 1
	},
	viewWin : {
		width : 400,
		height : 310,
		title : '粉丝信息'
	},
	showView: false,
	showAdd: false,
    showEdit: false,
    showRemove:false,
    statusFormat : function(v) {
		if (v == "1") {
			return "<span style='color:green;'>已关注</span>";
		} else{
			return "<span style='color:red;'>已取消</span>";
		}
	},
	type:[["文本消息","1"],["图文消息","2"],["音乐消息","3"],["图片消息","4"],["语音消息","5"],["视频消息","6"]],
	sendMessage:function(){
		if(!this.account){
			Ext.Msg.alert("提示", "请选中一条微信帐号！！！", this);
			return;
		}
		
		var record = this.grid.getSelectionModel().getSelected();
		if(!record){
			Ext.Msg.alert("提示", "请选则一条粉丝！！！", this);
			return;
		}
		var accountId = this.account.id;
		var datasend={
				accountId:accountId,
				followerId:record.get("id")
		};
		this.datasend= datasend;
		if(!this.msgGridList){
			this.msgGridList = new MsgGrideListManel({title:"消息记录"});
		}
		this.msgGridList.store.baseParams.accountId = this.datasend.accountId;
		this.msgGridList.store.baseParams.followerId = this.datasend.followerId;
		if(!this.sendMessageWin){
			this.sendMessageWin = new Ext.Window({  
                title:'发送信息窗口',  
                width:600,  
                height:550,  
                closeAction:'hide',  
                modal: true,
                plain:true,  
                layout:'border',
                items:[new Ext.Panel({
                	region:'center',
                	margins:'3 3 3 3',
                	cmargins:'3 3 3 3',
                	items:[this.msgGridList]
                }),new Ext.form.FormPanel({
                	title:'发送消息',
                	region:"south",
                	height:210,
                	margins:'3 3 3 3',
                	cmargins:'3 3 3 3',
                	labelAlign:'right',
                	labelWidth:70,
                	freame:true,
                	defaultType:'textfield',
                	items:[{
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
							data : this.type
						}),
						value:"1",
						editable : false,
						mode : 'local',
						triggerAction : 'all',
						listeners:{
							select:function(v){
								var msgType = v.value;
								var contentMsg = Ext.getCmp('contentMsg');
								if(msgType!="1"){
									contentMsg.setValue('');
									contentMsg.setDisabled(true);
								}else{
									contentMsg.setDisabled(false);
								}
							}
						}
					},{
						xtype : "hidden",
						name : "templateId",
						id:'templateId'
					},new Ext.ux.form.SearchField({
						width:450,
						fieldLabel : "消息模版",
						id:'templateName',
					  	name:'templateName',
					  	disabled:true,
						onTrigger2Click:this.addTemplate,
						scope:this
					}),{
						id:"contentMsg",
                        xtype: "textarea",
                        fieldLabel: "文本消息",
                        name: "contentMsg",
                        height: 100,
                        width:450
                    }]
                })],
                buttonAlign:'center',  
                buttons:[{  
                    text:'发送',
                    handler:this.senMessageTowinxin,
                    scope:this
                }]
			});
		}
		this.sendMessageWin.show();
		this.msgGridList.store.removeAll();
		this.msgGridList.store.load();
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
	senMessageTowinxin:function(){
		var msgType = Ext.getCmp("msgTypeMessage").value;
		var templateEle = Ext.getCmp('templateId').value;
		if(msgType=="1"){
			var contentmsg = Ext.getCmp("contentMsg").getValue();
			if((templateEle==null || templateEle =="")&& (contentmsg==null || contentmsg=="")){
				Ext.Msg.alert("提示", "文本类型的消息，请填写文本消息或选择模板！！！", this);
				return
			}
			if(contentmsg !=null && contentmsg!=""){
				this.datasend.contentMsg=contentmsg;
			}
		}else if(templateEle==null || templateEle ==""){
			Ext.Msg.alert("提示", "请选择模板进行发送！！！", this);
			return;
		}
		if(templateEle!=null && templateEle !=""){
			this.datasend.templateId=templateEle;
		}
		Ext.Ajax.request({
            url: '/wxCommon.java?cmd=sendMessageKf',
            params: this.datasend,
            method: 'POST',
            success: function (response, options) {
            	var returnDate = eval("("+response.responseText+")")
            	if(returnDate.errcode=="0"){
            		this.datasend.templateId="";
                	this.datasend.contentMsg="";
                	this.msgGridList.store.removeAll();
            		this.msgGridList.store.load();
            		Ext.Msg.alert("提示", "发送成功！！！", this);
            	}else{
            		console.dir(returnDate.errcode)
            		Ext.Msg.alert("提示", "发送失败:"+returnDate.errmsg, this);
            	}
            },
            failure: function (response, options) {
            	Ext.Msg.alert("提示", "发送失败！！！", this);
            },
            scope:this
        });
	},
	delMessage:function(v){
		if(!this.account){
			Ext.Msg.alert("提示", "请选中一条微信帐号！！！", this);
			return;
		}
		var accountId = this.account.id;
		Ext.Ajax.request({
            url: '/wxCommon.java?cmd=delMessage',
            params: {type:v.type,accountId:accountId},
            method: 'POST',
            success: function (response, options) {
            	var returnDate = eval("("+response.responseText+")")
            	Ext.Msg.alert("提示", "删除成功！！！", this);
            },
            failure: function (response, options) {
            	Ext.Msg.alert("提示", "发送失败！！！", this);
            },
            scope:this
        });
	},
	storeMapping: ["id", "name", "registerTime",  "modifyDate","status","weixinOpenId","nickname","sex","city","province","country","headimgurl"],
	initComponent : function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "粉丝头像",
            sortable: true,
            width: 100,
            dataIndex: "headimgurl",
            renderer:function(v){
            	return "<img src='"+v+"' style='width:40px;height:40px'>";
            }
        }, {
            header: "粉丝名称",
            sortable: true,
            width: 100,
            dataIndex: "nickname"
        }, {
            header: "性别",
            sortable: true,
            width: 100,
            dataIndex: "sex",
            renderer:function(v){
            	return v=="1"?"男":"女"
            }
        }, {
        	header: "关注日期",
            dataIndex: "registerTime",
            sortable: true,
            width: 100,
            renderer: this.dateRender()
        },  {
        	header: "取消日期",
            dataIndex: "modifyDate",
            sortable: true,
            width: 100,
            renderer: this.dateRender()
        },{
            header: "粉丝状态",
            sortable: true,
            width: 100,
            dataIndex: "status",renderer:this.statusFormat
        }]);
        this.gridButtons = [ {
			text : "消息管理",
			iconCls : "upload-icon",
			handler : this.sendMessage,
			scope : this
			},{
				text : "删除粉丝消息",
				iconCls : "upload-icon",
				menu : [ {
					text : "删除所有（慎用）",
					handler : this.delMessage,
					type:"all",
					scope : this
				}, {
					text : "删除48小时以后",
					handler : this.delMessage,
					type:"48",
					scope : this
				} ]
			}];
        FollowerMsgGridListPanel.superclass.initComponent.call(this);
    }
});
// 消息管理
FollowerMsgManagePanel = function() {
	this.list = new FollowerMsgGridListPanel({
		region : "center"
	});
	this.list.grid.on("rowdblclick",  function(grid, index){
        this.sendMessage();
    }, this.list);
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
			text : "所有微信帐号",
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
	FollowerMsgManagePanel.superclass.constructor.call(this, {
		id : "followerMsgManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",
		items : [ this.tree, this.list ]
	});
};
MsgGrideListManel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "msgGrideListManel",
	baseUrl : "followerMessage.java",
	singleWindowMode:true,
	pagingToolbar: true,
	autoLoadGridData:false,
	height : 250,
	showView: false,
	showAdd: false,
    showEdit: false,
    showRemove:false,
	storeMapping: ["id", "isFollowerMsg", "msgType",  "content","createDate","mediaId","msgId","picUrl","format","ThumbMediaId","location_X","location_Y","scale","label","titleStr","descriptionStr","urlStr"],
	initComponent : function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "发送方",
            sortable: true,
            width: 100,
            dataIndex: "isFollowerMsg",
            renderer:function(v){
            	return v=="1"?"粉丝":"客服"
            }
        }, {
        	header: "发送日期",
            dataIndex: "createDate",
            sortable: true,
            width: 100,
            renderer: this.dateRender()
        },{
        	header: "消息类型",
            dataIndex: "msgType",
            sortable: true,
            width: 100,
            renderer: function(v){
            	if(v=="text"){
            		return "文本";
            	}else if(v=="image"){
            		return "图片";
            	}
            	else if(v=="voice"){
            		return "音频";
            	}
            	else if(v=="video"){
            		return "视频";
            	}
            	else if(v=="location"){
            		return "地址";
            	}
            	else if(v=="link"){
            		return "链接";
            	}else if(v=="news"){
            		return "图文";
            	}else if(v=="music"){
            		return "音乐";
            	}
            }
        },{
            header: "消息内容",
            sortable: true,
            dataIndex: "content",
            renderer:function(v,e,obj){
            	var msgType = obj.data.msgType;
            	if(msgType=="text"){
            		return "<a target='_blank' style='color:green' href='/wxCommon.java?cmd=messageDefail&id="+obj.data.id+"'>"+v.substr(0,10)+"</a>";
            	}else{
            		return "<a target='_blank' style='color:red' href='/wxCommon.java?cmd=messageDefail&id="+obj.data.id+"'>查看详情</a>";;
            	}
            }
        }]);
        this.gridButtons = [ {
			text : "删除消息",
			iconCls : "upload-icon",
			handler : this.removeMessage,
			scope : this
			}];
        MsgGrideListManel.superclass.initComponent.call(this);
    },
    removeMessage:function(){
		var records = this.grid.getSelectionModel().getSelections();
		if(records.length==0){
			Ext.Msg.alert("提示", "请选择要删除的信息！！！", this);
			return;
		}
        var ids = "";
        for (var i = 0; i < records.length; i++) {
            ids = ids + records[i].get("id") + ",";
        }
        Ext.Ajax.request({
            url: '/wxCommon.java?cmd=delMessage',
            params: {type:"one",ids:ids},
            method: 'POST',
            success: function (response, options) {
            	this.store.removeAll();
        		this.store.load();
            	var returnDate = eval("("+response.responseText+")")
            	Ext.Msg.alert("提示", "删除成功！！！", this);
            },
            failure: function (response, options) {
            	Ext.Msg.alert("提示", "删除失败！！！", this);
            },
            scope:this
        });
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
		},]);
        TemplateListToAutoPanel.superclass.initComponent.call(this);
    }
});
Ext.extend(FollowerMsgManagePanel, Ext.Panel, {});