// 租户管理
if (typeof Global === "undefined") {
	Global = {};
}
//模版栏目分类树
Global.tenantLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "tenant.java?cmd=getTree&pageSize=-1&isTree=1",
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
if (!Global.tenantRoleTreeLoader) {
	Global.tenantRoleTreeLoader = new Ext.tree.TreeLoader({
		iconCls : 'lanyo-tree-node-icon',
		url : "role.java?cmd=getTree&isUserTenantR=1",
		listeners : {
			'beforeload' : function(treeLoader, node) {
				treeLoader.baseParams.id = (node.id.indexOf('root') < 0
						? node.id
						: "");
			}
		}
	})
}
TenantGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "tenantGridListPanel",
    // title:"网站发布管理",
    baseUrl: "tenant.java",
    gridSelModel: 'checkbox',
    batchRemoveMode: true,
    pageSize: 20,
    showView: false,
    status : function(v) {
		if (v == -1) {
			return "停用";
		}if (v == 0) {
			return "未激活";
		}if (v == 1) {
			return "激活未审核";
		}if (v == 2) {
			return "已审核";
		}
	},
	create: function() {
		TenantGridListPanel.superclass.create.call(this);
        var tabs = this.fp.items.items[0];
        if (tabs.items.items[2]) {
            tabs.remove(tabs.items.items[2])
        }
        if (tabs.items.items[1]) {
            tabs.remove(tabs.items.items[1])
        }
    },
    onCreate:function(){
		this.fp.form.findField('code').setDisabled(false);
    },
    edit: function() {
        var record = this.grid.getSelectionModel().getSelected();
        var win = TenantGridListPanel.superclass.edit.call(this);
        var tabs = this.fp.items.items[0];
        if (tabs.items.items[2]) {
            tabs.remove(tabs.items.items[2])
        }
        if (tabs.items.items[1]) {
            tabs.remove(tabs.items.items[1])
        }
        this.fp.form.findField('code').setDisabled(false);
        this.selectTenantId = record.get("id");
        tabs.add(this.editRole());
    },
     editRole: function() {
        Global.tenantRoleTreeLoader.baseParams.tenantId = this.selectTenantId;
        this.RoleTree = new Ext.tree.TreePanel({
            title: "角色管理",
            autoScroll: false,
            width: 200,
            border: false,
            margins: '0 2 0 0',
            tools: [{
                id: "refresh",
                handler: function() {
                    this.RoleTree.root.reload();
                },
                scope: this
            }],
            root: new Ext.tree.AsyncTreeNode({
                id: "root",
                text: "所有角色",
                iconCls: 'treeroot-icon',
                expanded: true,
                loader: Global.tenantRoleTreeLoader
            })
        });
        this.RoleTree.on("checkchange", function(node, eventObject) {
            var id = (node.id != 'root' ? node.id : "");
            var params={
                    cmd: "disRole",
                    roleId: id,
                    tenantId: this.selectTenantId
                };
            if (node.ui.checkbox.checked) {
                params.isAdd=1;
            }
            Ext.Ajax.request({
                url: "tenant.java",
                params: params
            })
        }, this);
        return this.RoleTree;
    },
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: false,
            labelWidth: 80,
            labelAlign: 'left',
            fileUpload : true,
            layout: "fit",
            items: [{
                xtype: 'tabpanel',
                deferredRender: false,
                monitorResize: true,
                hideBorders: true,
                border: false,
                activeTab: 0,
                items: [{
                    title: '租户基本信息',
                    frame: true,
                    border: false,
                    layout: 'form',
                    fileUpload: true,
                    defaultType: 'textfield',
                    defaults: {
                        anchor: "-20"
                    },
                    items: [{
                xtype: "hidden",
                name: "id"
            },
            {
                fieldLabel: "租户名称",
                name: "title",
                emptyText: '租户不能为空',
				allowBlank: false,
				blankText: '租户不能为空'
            },{
                fieldLabel: "租户域名",
                name: "url",
                emptyText: '租户域名',
				allowBlank: false,
				blankText: '租户域名'
            },{
                fieldLabel: "管理员登陆名",
                name: "code",
				allowBlank: false
            },{
                 xtype: 'fileuploadfield',
                emptyText: '单击右侧上传按钮上传',
                fieldLabel: 'Logo图片',
                name: 'logo',
                buttonCfg: {
                    text: '',
                    iconCls: 'upload-icon'
                }
            },{
                fieldLabel: '状态',
                xtype: 'radiogroup',
                name: 'status',
                items: [{
                    boxLabel: '停用',
                    name: 'status',
                    inputValue: "-1"
                }, {
                    boxLabel: '未激活',
                    name: 'status',
                    inputValue: "0"
                }, {
                	  checked: true,
                    boxLabel: '激活未审核',
                    name: 'status',
                    inputValue: "1"
                }, {
              	  checked: true,
                  boxLabel: '已审核',
                  name: 'status',
                  inputValue: "2"
              }]
            } ,{
                xtype: "textarea",
                fieldLabel: "租户简介",
                name: "intro",
                height: 50
            }]
                }]
            }]
        });
        return formPanel;
    },
    createWin: function() {
        return this.initWin(548, 400, "租户管理");
    },
    storeMapping: ["id", "title", "code", "status","beginTime","url","logo"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "租户名称",
            sortable: true,
            width: 100,
            dataIndex: "title"
        }, {
            header: "管理员登陆名",
            sortable: true,
            width: 100,
            dataIndex: "code"
        }, {
            header: "租户状态",
            sortable: true,
            width: 100,
            dataIndex: "status",
            renderer: this.status
        }]);
        TenantGridListPanel.superclass.initComponent.call(this);
    }
});
// 模版栏目分类
TenantManagePanel = function() {
	this.list = new TenantGridListPanel({
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
			text : "所有租户",
			iconCls : 'treeroot-icon',
			expanded : true,
			loader : Global.tenantLoader
		})
	});
	this.list.tree = this.tree;
	this.tree.on("click", function(node, eventObject) {
		var id = (node.id != 'root' ? node.id : "");
		this.list.store.baseParams.parentId = id;
		this.list.store.removeAll();
		this.list.store.load();
	}, this);
	TenantManagePanel.superclass.constructor.call(this, {
		id : "tenantManagePanel",
		closable : true,
		border : false,
		autoScroll : true,
		layout : "border",

		items : [ this.tree, this.list ]
	});
};

Ext.extend(TenantManagePanel, Ext.Panel, {});