//图文消息明细
if (typeof Global === "undefined") {
	Global = {};
}
//微信帐号数
Global.templateLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "template.java?cmd=getTree&msgType=2",//只查询图文消息模版
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
NewsItemGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "newsItemGridListPanel",
	baseUrl : "newsItem.java",
	baseQueryParameter : {
		orgType : 1
	},
	viewWin : {
		width : 400,
		height : 310,
		title : '粉丝信息'
	},
	showView: false,
	newType:[["图文","1"],["外部链接","2"]],
    newTypeFormat : function(v) {
		if (v == "1") {
			return "图文";
		} else{
			return "外部链接";
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
            }, {
                fieldLabel: "消息名称",
                name: "title",
                emptyText : '消息名称不能为空',
				allowBlank : false,
				blankText : '消息名称不能为空'
            },{
                fieldLabel: "作者",
                name: "author",
                emptyText : '作者不能为空',
				allowBlank : false,
				blankText : '作者不能为空'
            },{
                fieldLabel: "orders",
                name: "orders",
                emptyText : 'orders不能为空',
				allowBlank : false,
				blankText : 'orders不能为空'
            },{
                fieldLabel: "路径",
                name: "url",
                emptyText : '路径不能为空',
				allowBlank : false,
				blankText : '路径不能为空'
            },{
				xtype : "combo",
				name : "newType",
				hiddenName : "newType",
				fieldLabel : "消息类型",
				allowBlank : false,
				blankText : '消息类型不能为空',
				displayField : "title",
				valueField : "value",
				store : new Ext.data.SimpleStore({
					fields : [ 'title', 'value' ],
					data : this.newType
				}),
				editable : false,
				mode : 'local',
				triggerAction : 'all'
					
            },{
				fieldLabel : '所属模版',
				name : 'tpl',
				xtype : "treecombo",
				hiddenName : "tplId",
				displayField : "title",
				valueField : "id",
				allowBlank : false,
				blankText : '所属模版不能为空',
				tree : new Ext.tree.TreePanel({
					autoScroll : true,
					root : new Ext.tree.AsyncTreeNode({
						id : "root",
						text : "所有模版",
						expanded : true,
						iconCls : 'treeroot-icon',
						loader : Global.templateLoader,
						types : "0"
					})
				})
            },{
				xtype : 'fileuploadfield',
				emptyText : '单击右侧按钮选择上传的图片',
				fieldLabel : "图文图片",
				name : "imagePath",
				buttonCfg : {
					text : '',
					iconCls : 'upload-icon'
				}
			},{
                xtype: "textarea",
                fieldLabel: "消息内容",
                name: "content",
                height: 50
            },{
                xtype: "textarea",
                fieldLabel: "消息描述",
                name: "description",
                height: 50
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
		return this.initWin(550, 350, "图文消息管理", callback, autoClose);
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
        },{
        	header: "消息类型",
            dataIndex: "newType",
            sortable: true,
            width: 100,
            renderer: this.newTypeFormat
        }]);
        NewsItemGridListPanel.superclass.initComponent.call(this);
    },
	listeners : {
		render : function(e) {
		}
	}
});
// 模版栏目分类
NewsItemManagePanel = function() {
	this.list = new NewsItemGridListPanel({
		region : "center"
	});
	this.tree = new Ext.tree.TreePanel({
		title : "消息模版信息",
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
			text : "所有模版",
			iconCls : 'treeroot-icon',
			expanded : true,
			loader : Global.templateLoader
		})
	});
	this.list.tree = this.tree;
	this.tree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		this.list.store.baseParams.tplId = id;
		if (id) {
			this.list.tpl = {
				id : id,
				title : node.text
			};
		} else
			this.list.tpl = null;
		this.list.store.removeAll();
		this.list.store.load();
	}, this);
	NewsItemManagePanel.superclass.constructor.call(this, {
		id : "newsItemManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",
		items : [ this.tree, this.list ]
	});
};

Ext.extend(NewsItemManagePanel, Ext.Panel, {});