// 模版管理
if (typeof Global === "undefined") {
	Global = {};
}
//模版栏目分类树
Global.roleTreeLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "tenantRole.java?cmd=getTree&pageSize=-1&treeData=true&all=true&&isMenu=true",
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
Global.permissionLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "permission.java?cmd=getTree&rolePer=1&type=0",
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
RoleManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
	gridSelModel : 'checkbox',
	id : "roleManagePanel",
	baseUrl : "role.java",
	baseQueryParameter : {
		orgType : 1
	},
	theStatus : [["启用", 0],["停用", -1]],
	theStatusRender:function(v){
		return v=="0"?"启用":"停用";
	},
	showView: false,
	create: function() {
        RoleManagePanel.superclass.create.call(this);
        var tabs = this.fp.items.items[0];
        if (tabs.items.items[2]) {
            tabs.remove(tabs.items.items[2])
        }
        if (tabs.items.items[1]) {
            tabs.remove(tabs.items.items[1])
        }
    },
    edit: function() {
        var record = this.grid.getSelectionModel().getSelected();
        var win = RoleManagePanel.superclass.edit.call(this);
        var tabs = this.fp.items.items[0];
        if (tabs.items.items[2]) {
            tabs.remove(tabs.items.items[2])
        }
        if (tabs.items.items[1]) {
            tabs.remove(tabs.items.items[1])
        }
        this.selectRoleId = record.get("id");
        tabs.add(this.editPermission());
    },
    editPermission: function() {
        Global.permissionLoader.baseParams.roleId = this.selectRoleId;
        this.PermissionTree = new Ext.tree.TreePanel({
            title: "权限管理",
            autoScroll: false,
            width: 200,
            border: false,
            margins: '0 2 0 0',
            tools: [{
                id: "refresh",
                handler: function() {
                    this.PermissionTree.root.reload();
                },
                scope: this
            }],
            root: new Ext.tree.AsyncTreeNode({
                id: "root",
                text: "所有权限",
                iconCls: 'treeroot-icon',
                expanded: true,
                loader: Global.permissionLoader
            })
        });
        this.PermissionTree.on("checkchange", function(node, eventObject) {
            var id = (node.id != 'root' ? node.id : "");
            var params={
                    cmd: "addPermission",
                    permissionId: id,
                    roleId: this.selectRoleId
                };
            if (!node.ui.checkbox.checked) {
                params.cmd="delPermission";
            }
            Ext.Ajax.request({
                url: "role.java",
                params: params
            })
        }, this);
        return this.PermissionTree;
    },
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
            frame: false,
            labelWidth: 80,
            labelAlign: 'left',
            layout: "fit",
            items: [{
                xtype: 'tabpanel',
                deferredRender: false,
                monitorResize: true,
                hideBorders: true,
                border: false,
                activeTab: 0,
                items: [{
                    title: '角色基本信息',
                    frame: true,
                    border: false,
                    layout: 'form',
                    fileUpload: true,
                    defaultType: 'textfield',
                    defaults: {
                        anchor: "-20"
                    },
                    items: [
					{
						xtype : "hidden",
						name : "id"
					},
					Disco.Ext.Util.twoColumnPanelBuild(
	                        {
                                xtype : "textfield",
                                fieldLabel : '角色名称',
                                name : 'title',
                                width : 150
                            },{
                                xtype : "textfield",
                                fieldLabel : '角色编码',
                                name : 'name',
                                width : 150
                            },
                         Disco.Ext.Util.buildCombox("status","状态",this.theStatus,0)),
                         {
				        anchor:"-20",
				        height:50,
				        xtype : "textarea",
				        fieldLabel : "简介",
				        allowBlank:false,
				        name : 'description'
				}]
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
			var dirNode = this.fp.form.findField("parent");
			if (dirNode && dirNode.tree.rendered) {
				dirNode.tree.root.reload();
				dirNode.tree.expandAll();
			}
		}
	},
	createWin : function(callback, autoClose) {
		return this.initWin(650, 450, "角色管理", callback, autoClose);
	},
    storeMapping : ["id", "name", "title", "description", "allPermissions", "permissions","users","status","parent","tenant"],
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([{
            header : "角色名称",
            sortable : true,
            width : 100,
            dataIndex : "title"
        }, {
            header : "角色编码",
            sortable : true,
            width : 120,
            dataIndex : "name"
        }, {
            header : "所属租户",
            sortable : true,
            width : 100,
            dataIndex : "tenant",
            renderer:this.objectRender("title")
        }, {
            header : "状态",
            sortable : true,
            width : 80,
            dataIndex : "status",renderer:this.theStatusRender
        }]);
		RoleManagePanel.superclass.initComponent.call(this);
	}
});
