if (typeof Global === "undefined") {
	Global = {};
}
if (!Global.sytemModuleLoader) {
	Global.sytemModuleLoader = new Ext.tree.TreeLoader(
			{
				url : "onlineEditFile.java?cmd=getContents",
				listeners : {
					'beforeload' : function(treeLoader, node) {
						treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id
								: "");
					}
				}
			})
}
SystemFileEditPanel = Ext.extend(Ext.Panel, {
	id : "systemFileEditPanel",
	title : "文件源代码内容编辑器",
	baseUrl : "onlineEditFile.java?cmd=getFileDetails",
	layout : "fit",
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			border : false,
			trackResetOnLoad : true,
			autoScroll : true,
			frame : true,
			bodyStyle : 'margin:0;padding:0',
			defaults : {
				border : false
			},
			items : [ {
				name : "filePath",
				xtype : 'hidden'
			}, {
				height : (Ext.getBody().dom.offsetHeight) - 120,
				name : 'fileSource',
				hideLabel : true,
				xtype : 'textarea',
				anchor : "100%",
				allowBlank : false,
				blankText : '把源代码内容清空是很危险的事情,建议不要这样做,并不要进行保存操作'
			} ],
			buttons : [ {
				id : "btnSave",
				text : "保存源代码",
				handler : this.save,
				iconCls : 'save',
				scope : this
			}, {
				id : "btnReset",
				text : "取消修改",
				iconCls : 'clean',
				handler : this.reset,
				scope : this
			} ]
		});
		return formPanel;
	},
	save : function() {
		this.fp.form.submit({
			url : "onlineEditFile.java",
			params : {
				cmd : "saveSource",
				ajax : true
			},
			waitMsg : "正在保存，请稍候...",
			success : function(form, action) {
				Ext.Msg.alert("提示", "成功保存！");
			}
		});
	},
	reset : function() {
		this.fp.form.reset();
	},
	initComponent : function() {
		SystemFileEditPanel.superclass.initComponent.call(this);
		this.fp = this.createForm();
		this.add(this.fp);
	}

});
// 信息分类栏目管理
OnlineEditFileManagePanel = function() {
	this.panel = new SystemFileEditPanel({
		region : "center"
	});
	this.tree = new Ext.tree.TreePanel({
		title : "网站目录结构",
		/**
		 * tbar : [{ xtype : 'textfield', width : 150, value : '12312', name :
		 * 'searchFile' }, '&nbsp&nbsp', { scope : this, text : '查找', // pressed :
		 * true, name : 'searchFile', handler : function() { alert(123); } }],
		 */
		region : "west",
		autoScroll : true,
		collapseFirst : false,
		collapsible : true,
		split : true,
		width : 210,
		minWidth : 210,
		maxWidth : 300,
		tools : [ /**
					 * { id : "plus", handler : function() {
					 * this.tree.root.reload(); }, scope : this }, { id :
					 * "minus", handler : function() { this.tree.root.reload(); },
					 * scope : this }, { id : "help", qtip : "帮助系统", handler :
					 * function() { this.tree.root.reload(); }, scope : this },
					 */
		{
			id : "refresh",
			qtip : "单击刷新网站目录结构",
			handler : function() {
				this.tree.root.reload();
			},
			scope : this
		} ],
		root : new Ext.tree.AsyncTreeNode({
			id : "root",
			text : "网站根目录",
			icon : "img/item/tage.gif",
			expanded : true,
			loader : Global.sytemModuleLoader
		})
	});
	this.tree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		if (node.isLeaf() && node.id != "root") {
			this.panel.fp.form.load({
				waitTitle : "请稍候",
				waitMsg : "正在加载页面源代码，请稍候...",
				url : 'onlineEditFile.java?cmd=getFileDetails',
				params : {
					pack : node.id
				}
			})
		}
	}, this);
	OnlineEditFileManagePanel.superclass.constructor.call(this, {
		id : "newsDirManagePanel",
		closable : true,
		autoScroll : true,
		border : false,
		layout : "border",
		items : [ this.tree, this.panel ]
	});

};
Ext.extend(OnlineEditFileManagePanel, Ext.Panel, {});