// 模版管理
if (typeof Global === "undefined") {
	Global = {};
}
//模版栏目分类树
Global.shopMenuLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "shopMenu.java?cmd=getTree&pageSize=-1&treeData=true&all=true",
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
ShopMenuGridPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "shopMenuGridPanel",
	baseUrl : "shopMenu.java",
	baseQueryParameter : {
		orgType : 1
	},
	viewWin : {
		width : 400,
		height : 310,
		title : '模版信息'
	},
	showView: false,
	onCreate : function() {
		if (this.parent) {
			this.fp.form.findField("parent").setOriginalValue(this.parent);
		}
	},
	edit : function() {
		var win = ShopMenuGridPanel.superclass.edit.call(this);
		if (win) {
			var record = this.grid.getSelectionModel().getSelected();
			var dirObj = record.get('parent');
			if (dirObj) {
				this.fp.form.findField('parent').setOriginalValue(dirObj);
			}
		}
	},
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
            },{
            	xtype : "treecombo",
				fieldLabel : "父菜单",
				name : "parent",
				hiddenName : "parent",
				displayField : "title",
				valueField:"id",
				tree : new Ext.tree.TreePanel({
						autoScroll:true,
						root: new Ext.tree.AsyncTreeNode({
								id : "root",
								text : "所有菜单",
								iconCls : 'treeroot-icon',
								expanded : true,
								loader : Global.shopMenuLoader
							})})
				},{
				fieldLabel : '菜单名称',
				name : 'title',
				allowBlank:false
			},{
				fieldLabel : '菜单编码',
				name : 'sn',
				allowBlank:false
			},{
				fieldLabel : '排序',
				name : 'sequence'
			},{
				fieldLabel : '菜单路径',
				name : 'url'
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
		return this.initWin(550, 250, "模版管理", callback, autoClose);
	},
	storeMapping : ["id","sn", "title","url","vrtype","roles","permissions","types","appClass","sequence","status","parent","parent","otherScripts","icon","pack","theRoles","fee","system","params"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([
            {
    	        header: "菜单名称",
    	        sortable: true,
    	        width: 100,
    	        dataIndex: "title"
            },{
    	        header: "菜单编码",
    	        sortable: true,
    	        width: 100,
    	        dataIndex: "sn"
            },{
            	header : "父级",
    			sortable : true,
    			width : 120,
    			dataIndex : "parent",
    			renderer : this.objectRender("title")
    		}]);
		ShopMenuGridPanel.superclass.initComponent.call(this);
	}
});
// 模版栏目分类
ShopMenuManagePanel = function() {
	this.list = new ShopMenuGridPanel({
		region : "center"
	});
	this.tree = new Ext.tree.TreePanel({
		title : "模版信息",
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
			text : "所有栏目",
			iconCls : 'treeroot-icon',
			expanded : true,
			loader : Global.shopMenuLoader
		})
	});
	this.list.tree = this.tree;
	this.tree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		this.list.store.baseParams.parentId = id;
		if (id) {
			this.list.parent = {
				id : id,
				title : node.text
			};
		} else
			this.list.parent = null;
		this.list.store.removeAll();
		this.list.store.load();
	}, this);
	ShopMenuManagePanel.superclass.constructor.call(this, {
		id : "shopMenuManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",

		items : [ this.tree, this.list ]
	});
};

Ext.extend(ShopMenuManagePanel, Ext.Panel, {});