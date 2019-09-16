// 分享记录
if (typeof Global === "undefined") {
	Global = {};
}
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
FollowerGridPanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "followerGridPanel",
    baseUrl: "follower.java",
    gridSelModel: 'checkbox',
    pageSize: 20,
    showView: false,
	showAdd: false,
    showEdit: false,
    showRemove:false,
    storeMapping: ["id", "nickname", "headimgurl","sex"],
    initComponent: function() {
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
        }]);
        FollowerGridPanel.superclass.initComponent.call(this);
    }
});
// 地区信息
ShareHistoryGridPanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "shareHistoryGridPanel",
    baseUrl: "shareHistory.java",
	autoLoadGridData:false,
    pageSize: 15,
    showView: false,
	showAdd: false,
    showEdit: false,
    showRemove:false,
	storeMapping : [ "id", "follower", "createDate", "shareUrl", "account"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
			header : "分享地址",
			sortable : true,
			width : 160,
			dataIndex : "shareUrl"
		},{
			header : "分享日期",
			sortable : true,
			width : 160,
			dataIndex : "createDate",
			renderer: this.dateRender()
		}]);
        ShareHistoryGridPanel.superclass.initComponent.call(this);
    }
});
ShareHistoryManagePanel = function() {
	this.list = new FollowerGridPanel({
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
	
	this.areaList = new ShareHistoryGridPanel({
		width:500,
		region : "east"
	});
	this.list.areaList = this.areaList;
	this.list.grid.on("rowclick",  function(grid, index){
            var r = this.list.store.getAt(index);
            this.areaList.store.removeAll();
            this.areaList.store.baseParams.followerId = r.get("id");
            this.areaList.store.load();
        }, this);
	ShareHistoryManagePanel.superclass.constructor.call(this, {
		id : "shareHistoryManagePanel",
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

Ext.extend(ShareHistoryManagePanel, Ext.Panel, {});