// 分享记录
if (typeof Global === "undefined") {
	Global = {};
}
//获取所有微信公众帐号
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
//组别面板 （沿用 因此名字未变）
FollowerGridPanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "followerGridPanel",
    baseUrl: "followerGroup.java",
    gridSelModel: 'checkbox',
    pageSize: 20,
    showView: false,
	showAdd: true,
    showEdit: true,
    showRemove:true,
    storeMapping: ["id", "name", "code","account","count"],
    onCreate : function() {
		if (this.account) {
			this.fp.form.findField("accountId").setOriginalValue(this.account.id);
		}
		this.fp.form.findField('code').setDisabled(false);
	},
	edit : function() {
		var win = FollowerGridPanel.superclass.edit.call(this);
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
//			var parentObj = record.get('parent');
//			var parentDir = this.fp.form.findField('parent');
//			// 编辑回显时如果有父级栏目及回显数据
//			if (parentObj) {
//				parentObj.title || (parentObj.title = parentObj.name);
//				parentDir.setOriginalValue(parentObj);
//			}
			if (this.account) {
				this.fp.form.findField("accountId").setOriginalValue(this.account.id);
			}
			this.fp.form.findField('code').setDisabled(true);
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
					},{
						xtype : "hidden",
						name : "accountId"
					},{
						fieldLabel : "组别名称",
						name : "name",
						emptyText : '组别名称不能为空',
						allowBlank : false,
						blankText : '组别名称不能为空'
					},{
						fieldLabel : "组别编码",
						name : "code",
						emptyText : '组别编码不能为空',
						allowBlank : false,
						blankText : '组别编码不能为空'
					}]
		});
		return formPanel;
	},
	createWin : function(callback, autoClose) {
		return this.initWin(350, 140, "分组信息管理", callback, autoClose);
	},
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "组别名称",
            sortable: true,
            width: 50,
            dataIndex: "name"
        }, {
            header: "组别编码",
            sortable: true,
            width: 50,
            dataIndex: "code"
        }, {
            header: "粉丝数量",
            sortable: true,
            width: 50,
            dataIndex: "count"
        }]);
        FollowerGridPanel.superclass.initComponent.call(this);
    }
});
// 地区信息
ShareHistoryGridPanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "shareHistoryGridPanel",
    baseUrl: "followerGroup.java",
	autoLoadGridData:false,
    pageSize: 20,
    showView: false,
	showAdd: false,
    showEdit: false,
    showRemove:false,
	storeMapping : [ "id", "trueName", "weixinOpenId","account","followerGroup"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
			header : "真实姓名",
			sortable : true,
			width : 160,
			dataIndex : "trueName"
		},{
			header : "微信OpenId",
			sortable : true,
			width : 160,
			dataIndex : "weixinOpenId"
		}]);
		this.gridButtons = [{
			text : "添加新粉丝",
			cls : "x-btn-text-icon",
			icon : "images/core/up.gif",							
			handler : this.createResouce,
			scope : this
		},"-",{
			text : "移动粉丝到新分组",
			cls : "x-btn-text-icon",
			icon : "images/core/up.gif",							
			handler : this.removeToNewGroup,
			scope : this
		},"-",{
			text : "移至未分组",
			cls : "x-btn-text-icon",
			icon : "images/core/up.gif",							
			handler : this.removeToNoGroup,
			scope : this
		},"-",{
			text : "移至黑名单",
			cls : "x-btn-text-icon",
			icon : "images/core/up.gif",							
			handler : this.removeToBlacklist,
			scope : this
		},"-",{
			text : "移至星标组",
			cls : "x-btn-text-icon",
			icon : "images/core/up.gif",							
			handler : this.removeToStarGroup,
			scope : this
		}];	
        ShareHistoryGridPanel.superclass.initComponent.call(this);
    },
    removeToNoGroup : function(){
    	var record = this.grid.getSelectionModel().getSelected();
    	if(!record){
    		Ext.MessageBox.alert("警告","请选择一条数据！");
    		return;
    	}
        Ext.Ajax.request({
            url: "followerGroup.java",
            params: {
                cmd: "basicGroupOperation",
                id: record.get("id"),
                groupId: this.groupId,
                type : 0
            },
            scope: this,
            success: function(req) {
            	var respText = Ext.util.JSON.decode(req.responseText);
            	if(respText.success == false){
            		 Ext.Msg.alert('错误', respText.errors.msg);
            		 return;
            	}else{
            		Ext.Msg.alert('信息', "操作成功！！");
            		this.grid.getStore().reload();
            		this.followerList.store.removeAll();
                    this.followerList.store.load();
           		    return;
            	}
                
            },
            failure: function(resp,opts) {   
                var respText = Ext.util.JSON.decode(resp.responseText);   
                Ext.Msg.alert('错误',"出错了 ");   
         } 
        })
      },   
      removeToBlacklist : function(){
    	var record = this.grid.getSelectionModel().getSelected();
    	console.dir(record);
    	if(!record){
    		Ext.MessageBox.alert("警告","请选择一条数据！");
    		return;
    	}
        Ext.Ajax.request({
            url: "followerGroup.java",
            params: {
                cmd: "basicGroupOperation",
                id: record.get("id"),
                groupId: this.groupId,
                type : 1
            },
            scope: this,
            success: function(req) {
            	var respText = Ext.util.JSON.decode(req.responseText);
            	if(respText.success == false){
            		 Ext.Msg.alert('错误', respText.errors.msg);
            		 return;
            	}else{
            		Ext.Msg.alert('信息', "操作成功！！");
            		this.grid.getStore().reload();
            		this.followerList.store.removeAll();
                    this.followerList.store.load();
           		    return;
            	}
                
            },
            failure: function(resp,opts) {   
                var respText = Ext.util.JSON.decode(resp.responseText);   
                Ext.Msg.alert('错误',"出错了 ");   
         } 
        })
      }, 
      removeToStarGroup : function(){
    	var record = this.grid.getSelectionModel().getSelected();
    	console.dir(record);
    	if(!record){
    		Ext.MessageBox.alert("警告","请选择一条数据！");
    		return;
    	}
        Ext.Ajax.request({
            url: "followerGroup.java",
            params: {
                cmd: "basicGroupOperation",
                id: record.get("id"),
                groupId: this.groupId,
                type : 2
            },
            scope: this,
            success: function(req) {
            	var respText = Ext.util.JSON.decode(req.responseText);
            	if(respText.success == false){
            		 Ext.Msg.alert('错误', respText.errors.msg);
            		 return;
            	}else{
            		Ext.Msg.alert('信息', "操作成功！！");
            		this.grid.getStore().reload();
            		this.followerList.store.removeAll();
                    this.followerList.store.load();
           		    return;
            	}
                
            },
            failure: function(resp,opts) {   
                var respText = Ext.util.JSON.decode(resp.responseText);   
                Ext.Msg.alert('错误',"出错了 ");   
         } 
        })
    },
    createResouce: function() {
    	console.dir(this.groupId);
    	if(!this.groupId){
    		Ext.MessageBox.alert("警告","请先选择组别！！");
			return;
    	}
        if (!this.curAddResourceGrid) {
            this.curAddResourceGrid = this.createResouceGride();
        }
        if (!this.addResourceWin) {
            this.addResourceWin = new Ext.Window({
                title: "粉丝列表",
                closeAction: 'hide',
                width: 500,
                height: 420,
                modal: true,
                items: [new Ext.form.FormPanel({
                    frame: true,
                    labelWidth: 100,
                    fileUpload: true,
                    baseParms:{
                    	groupId : this.groupId
                    },
                    items: [{
                        xtype: "hidden",
                        name: "id"
                    }]
                }), this.curAddResourceGrid],
                buttonAlign:'center',
                buttons: [{
                    text: '确定添加',
                    handler: this.addResouce,
                    scope: this
                }]
            })
        }
        this.createResoucestore.baseParams.groupId= this.groupId
        this.createResoucestore.reload();
        this.addResourceWin.show();
    },
    createResouceGride: function() {
        var cm = new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(), {
            header: '姓名',
            dataIndex: 'trueName',
            sortable: true,
            width: 100
        }, {
            header: '微信OpenId',
            dataIndex: 'weixinOpenId',
            sortable: true,
            width: 110
        }]);
        var pagingToolbar = new Ext.PagingToolbar({
            pageSize: 10,
            store: this.createResoucestore,
            displayInfo: true,
            displayMsg: '第{0}-第{1}条，一共{2}条',
            emptyMsg: "没有数据"
        });
        var grid = new Ext.grid.GridPanel({
            height: 320,
            title: "资源信息",
            store: this.createResoucestore,
            cm: cm,
            frame: false,
            pageSize: 10,
            autoHeight: false,
            viewConfig: {
                forceFit: true
            },
            bbar: pagingToolbar
        });
        return grid;
    },
    addResouce: function() {
        var records = this.curAddResourceGrid.getSelectionModel().getSelections();
        var ids = "";
        for (var i = 0; i < records.length; i++) {
            ids = ids + records[i].get("id") + ",";
        }
        if (ids != "") {
            Ext.Ajax.request({
                url: "followerGroup.java",
                params: {
                    cmd: "addFollower",
                    ids: ids,
                    groupId: this.groupId
                },
                scope: this,
                success: function(req) {
                	var respText = Ext.util.JSON.decode(req.responseText);
                	if(respText.success == false){
                		 Ext.Msg.alert('错误', respText.errors.msg);
                		 return;
                	}
                    this.addResourceWin.hide();
                    //this.resoucestore.reload();
                    this.grid.getStore().reload();
                    this.followerList.store.removeAll();
                    this.followerList.store.load();
                },
                failure: function(resp,opts) {   
                    var respText = Ext.util.JSON.decode(resp.responseText);   
                    Ext.Msg.alert('错误',"出错了 ");   
             } 
            })
        }
        scope:this
    },
    createResoucestore: new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: '/followerGroup.java?cmd=getFollower&pageSize=10'
        }),//你请求数据的路径
        autoLoad: true,
        reader: new Ext.data.JsonReader({
            totalProperty: 'rowCount',
            root: 'result'
        }, [{
            name: "id"
        }, {
            name: 'trueName'
        }, {
            name: 'weixinOpenId'
        }])
    }),
    removeToNewGroup: function() {
    	var record = this.grid.getSelectionModel().getSelected();
    	if(!record){
    		Ext.MessageBox.alert("警告","请选择一条数据！");
    		return;
    	}
        if (!this.addResourceGrid) {
            this.addResourceGrid = this.resouceGride();
        }
        if (!this.addResourceWin1) {
            this.addResourceWin1 = new Ext.Window({
                title: "当前分组类表",
                closeAction: 'hide',
                width: 500,
                height: 420,
                autoFlsh : true,
                modal: true,
                items: [new Ext.form.FormPanel({
                	id:"valuePanel",
                    frame: true,
                    labelWidth: 100,
                    fileUpload: true,
                    items: [{
                        xtype: "hidden",
                        name: "id"
                    }]
                }), this.addResourceGrid],
                buttonAlign:'center',
                buttons: [{
                    text: '确定移动',
                    handler: this.quedingyidong,
                    scope: this
                }]
            })
        }
        this.resoucestore.baseParams.id= record.get("id");
        this.resoucestore.reload();
        this.addResourceWin1.show();
    },
    quedingyidong:function() {
        var gridRecord = this.addResourceGrid.getSelectionModel().getSelected();
        var record = this.grid.getSelectionModel().getSelected();
        if(!gridRecord){
        	return Ext.MessageBox.alert("提示","请选择分组");
        }
        Ext.Ajax.request({
            url: "followerGroup.java",
            method: 'GET',
            params: {
                cmd: "changeFollowerGroup",
                groupId: gridRecord.get("id"),
                abcid: record.get("id")
            },
            scope: this,
            success: function(req) {
            	var respText = Ext.util.JSON.decode(req.responseText);
            	if(respText.success == false){
            		 Ext.Msg.alert('错误', respText.errors.msg);
            		 return;
            	}
                this.addResourceWin1.hide();
                this.resoucestore.reload();
                this.grid.getStore().reload();
                this.followerList.store.removeAll();
                this.followerList.store.load();
            },
            failure: function(resp,opts) {   
                var respText = Ext.util.JSON.decode(resp.responseText);   
                Ext.Msg.alert('错误',"出错了 ");   
         } 
        })
    },
    resouceGride: function() {
        var cm = new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(), {
            header: '组名',
            dataIndex: 'name',
            sortable: true,
            width: 100
        }, {
            header: '编码',
            dataIndex: 'code',
            sortable: true,
            width: 110
        }]);
        var pagingToolbar = new Ext.PagingToolbar({
            pageSize: 10,
            store: this.resoucestore,
            displayInfo: true,
            displayMsg: '第{0}-第{1}条，一共{2}条',
            emptyMsg: "没有数据"
        });
        var grid1 = new Ext.grid.GridPanel({
            height: 320,
            title: "分类信息",
            store: this.resoucestore,
            cm: cm,
            frame: false,
            pageSize: 10,
            autoHeight: false,
            viewConfig: {
                forceFit: true
            },
            bbar: pagingToolbar
        });
        return grid1;
    },
    resoucestore: new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: '/followerGroup.java?cmd=getGroup&pageSize=10'
        }),//你请求数据的路径
        reader: new Ext.data.JsonReader({
            totalProperty: 'rowCount',
            root: 'result'
        }, [{
            name: "id"
        }, {
            name: 'name'
        }, {
            name: 'code'
        }])
    })
});
GroupManagePanel = function() {
	this.list = new FollowerGridPanel({
		region : "center"
	});
	
	this.tree = new Ext.tree.TreePanel({
		title : "分组信息管理",
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
	
	this.areaList = new ShareHistoryGridPanel({
		width:1000,
		region : "east"
	});
	this.areaList.followerList = this.list;
	this.list.areaList = this.areaList;
	this.list.grid.on("rowclick",  function(grid, index){
            var r = this.list.store.getAt(index);
            this.areaList.groupId=r.get("id");
            this.areaList.store.removeAll();
            this.areaList.store.baseParams.groupId = r.get("id");//组别Id 传给粉丝界面
            this.areaList.store.baseParams.div = "div";
            this.areaList.store.load();
        }, this);
	GroupManagePanel.superclass.constructor.call(this, {
		id : "groupManagePanel",
		closable : true,
		border : true,
		autoScroll : true,
		layout : 'border',
     	defaults : {
		style : {
			borderStyle : 'solid',
			borderWidth : '1px'
			}
	    },

		items : [ this.tree,this.list, this.areaList ]
	});
};

Ext.extend(GroupManagePanel, Ext.Panel, {});