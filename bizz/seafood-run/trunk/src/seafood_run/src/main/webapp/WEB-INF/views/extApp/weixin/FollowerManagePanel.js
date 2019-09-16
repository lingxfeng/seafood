//粉丝管理
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
//模版加载
Global.templateLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "template.java?cmd=getTree",
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
FollowerGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "followerGridListPanel",
	baseUrl : "follower.java",
	baseQueryParameter : {
		orgType : 1
	},
	viewWin : {
		width : 400,
		height : 310,
		title : '粉丝信息'
	},
	showView: true,
	showAdd: false,
    showEdit: false,
    showRemove:false,
	status:[["已关注","1"],["已取消","-2"]],
    statusFormat : function(v) {
		if (v == "1") {
			return "<span style='color:green;'>已关注</span>";
		} else{
			return "<span style='color:red;'>已取消</span>";
		}
	},
	edit : function() {
		var win = FollowerGridListPanel.superclass.edit.call(this);
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
			var sex = record.get("sex");
			var sexName = sex=="1"?"男":"女";
			this.fp.form.findField("sex").setValue(sexName);
			var countrystr = record.get("countrystr")+"-"+record.get("provincestr")+record.get("citystr");
			this.fp.form.findField("diqu").setValue(countrystr);
		}
	},
	type:[["文本消息","1"],["图文消息","2"],["音乐消息","3"],["图片消息","4"],["语音消息","5"]],
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 80,
            fileUpload: true,
            labelAlign: 'right',
            defaults: {
                width: 320,
                xtype: "textfield"
            },
            items: [{
                xtype: "hidden",
                name: "id"
            }, {
                fieldLabel: "粉丝名称",
                disabled:true,
                name: "nickname"
            }, {
                fieldLabel: "性别",
                disabled:true,
                name: "sex"
            }, {
                fieldLabel: "所在地区",
                disabled:true,
                name: "diqu"
            }, {
                fieldLabel: "最后所在经度",
                disabled:true,
                name: "longitude"
            },{
                fieldLabel: "最后所在纬度",
                disabled:true,
                name: "latitude"
            }, {
                fieldLabel: "关注日期",
                name: "registerTime",
                disabled:true,
                xtype:"datefield",
                format:'Y-m-d H:i:s'
            },{
                fieldLabel: "取消关注日期",
                name: "modifyDate",
                xtype:"datefield",
                disabled:true,
                format:'Y-m-d H:i:s'
            },{
                fieldLabel: '粉丝状态',
                disabled:true,
                xtype: 'radiogroup',
                name: 'status',
                items: [{
                    checked: true,
                    boxLabel: '已关注',
                    name: 'status',
                    inputValue: "1"
                }, {
                    boxLabel: '已取消关注',
                    name: 'status',
                    inputValue: "0"
                }]
            
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
		return this.initWin(450, 300, "粉丝详细信息", callback, autoClose);
	},
	sendMessage:function(v){
		if(!this.account){
			Ext.Msg.alert("提示", "请选中一条微信帐号！！！", this);
			return;
		}
		var accountId = this.account.id;
		Global.templateLoader.baseParams={
				accountId:accountId,
				msgType:"1"
		}
		var datasend={
				accountId:accountId,
				type:v.type
		};
		if(v.type=="select"){
			var selects = this.grid.getSelectionModel().getSelections();
			if(!selects || selects.length<1){
				Ext.Msg.alert("提示", "请选则要发送的粉丝！！！", this);
				return;
			}
			var foollowers = "";
			for(var i =0;i<selects.length;i++){
				foollowers = foollowers+selects[i].id+","
			}
			datasend.foollowers=foollowers;
		}
		this.datasend= datasend;
		if(!this.sendMessageWinQun){
			this.sendMessageWinQun = new Ext.Window({  
                title:'发送信息窗口',  
                width:580,  
                height:280,  
                closeAction:'hide',
                modal: true,
                plain:true,  
                layout:'form',
                items:[new Ext.form.FormPanel({
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
                        height: 110,
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
		this.sendMessageWinQun.show();
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
		_this.templatetoAutoList.store.baseParams={type:typeVal};
		_this.templatetoAutoList.store.load();
		_this.templateWin.show();
	},
	senMessageTowinxin:function(){
		var templateEle = Ext.getCmp('templateId');
		if(templateEle.value==null || templateEle.value==""){
			var msgType = Ext.getCmp("msgTypeMessage").value;
			var contentmsg = Ext.getCmp("contentMsg").getValue();
			if(msgType=="1" && (contentmsg==null || contentmsg=="")){
				Ext.Msg.alert("提示", "文本类型的消息，请填写文本消息或选择模板！！！", this);
				return;
			}else if(msgType!="1"){
				Ext.Msg.alert("提示", "请选择模板进行发送！！！", this);
				return;
			}
			if(contentmsg !=null && contentmsg!=""){
				this.datasend.contentMsg=contentmsg;
			}
		}
		if(templateEle!=null && templateEle !=""){
			this.datasend.templateId=templateEle.value;
		}
		Ext.Ajax.request({
            url: '/wxCommon.java?cmd=sendMessage',
            params: this.datasend,
            method: 'POST',
            success: function (response, options) {
            	var returnDate = eval("("+response.responseText+")")
            	if(returnDate.errcode=="0"){
            		Ext.Msg.alert("提示", "发送成功！！！", this);
                	this.sendMessageWinQun.hide();
            	}else{
            		Ext.Msg.alert("提示", "发送失败！！！", this);
            	}
            },
            failure: function (response, options) {
            	Ext.Msg.alert("提示", "发送失败！！！", this);
            },
            scope:this
        });
	},
	updataWxUser:function(){
		if(!this.account){
			Ext.Msg.alert("提示", "请选中一条微信帐号！！！", this);
			return;
		}
		Ext.Ajax.request({
            url: '/follower.java?cmd=updateWxFollower&accountId='+this.account.id,
            params: this.datasend,
            method: 'POST',
            success: function (response, options) {
            	var returnDate = eval("("+response.responseText+")")
            	Ext.Msg.alert("提示", "更新成功！！！", this);
            },
            failure: function (response, options) {
            	Ext.Msg.alert("提示", "发送失败！！！", this);
            },
            scope:this
        });
	},
	storeMapping: ["id", "name", "registerTime",  "modifyDate","status","weixinOpenId","nickname","sex","city","province","country","headimgurl","countrystr","provincestr","citystr","longitude","latitude"],
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
			text : "同步粉丝（慎用）",
			iconCls: "upload-icon",
			handler : this.updataWxUser,
			scope : this
		},{
			text : "信息群发",
			iconCls : "upload-icon",
			menu : [ {
				text : "所选发送",
				handler : this.sendMessage,
				type:"select",
				scope : this
			}, {
				text : "全部发送",
				handler : this.sendMessage,
				type:"all",
				scope : this
			} ]
		}];
        FollowerGridListPanel.superclass.initComponent.call(this);
    },
	listeners : {
		render : function(e) {
		}
	}
});
// 模版栏目分类
FollowerManagePanel = function() {
	this.list = new FollowerGridListPanel({
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
	FollowerManagePanel.superclass.constructor.call(this, {
		id : "followerManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",
		items : [ this.tree, this.list ]
	});
};
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
Ext.extend(FollowerManagePanel, Ext.Panel, {});