// 权限管理
if (typeof Global === "undefined") {
    Global = {};
}
Global.platformMenuLoader = new Ext.tree.TreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.DEPT_DIR_LOADER",// 缓存Key
    url: "systemMenu.java?cmd=getTree&type=2&pageSize=-1&isPerm=true",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});
if (!Global.sytemModuleLoader) {
    Global.sytemModuleLoader = new Ext.tree.TreeLoader({
        iconCls: 'lanyo-tree-node-icon',
        url: "resource.java?cmd=getModules",
        listeners: {
            'beforeload': function(treeLoader, node) {
                treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            }
        }
    })
}
ShopPermissionManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "shopPermissionManagePanel",
    baseUrl: "permission.java",
    viewWin: {
        width: 400,
        height: 310,
        title: '权限信息'
    },
    baseQueryParameter : {
		type : 2
	},
    showView: false,
    create: function() {
        ShopPermissionManagePanel.superclass.create.call(this);
        var tabs = this.fp.items.items[0];
        if (tabs.items.items[1]) {
            tabs.remove(tabs.items.items[1])
        }
    },
    edit: function() {
        var record = this.grid.getSelectionModel().getSelected();
        var win = ShopPermissionManagePanel.superclass.edit.call(this);
        var tabs = this.fp.items.items[0];
        if (tabs.items.items[1]) {
            tabs.remove(tabs.items.items[1])
        }
        this.seletPermissionId = record.get("id");
        tabs.add(this.editMenu());
    },
    /**
     * 编辑菜单
     * @return {}
     */
    editMenu: function(permissionId) {
        Global.platformMenuLoader.baseParams.permissionId = this.seletPermissionId;
        this.menuTree = new Ext.tree.TreePanel({
            title: "菜单信息",
            autoScroll: false,
            width: 200,
            border: false,
            margins: '0 2 0 0',
            tools: [{
                id: "refresh",
                handler: function() {
                    this.menuTree.root.reload();
                },
                scope: this
            }],
            root: new Ext.tree.AsyncTreeNode({
                id: "root",
                text: "所有菜单",
                iconCls: 'treeroot-icon',
                expanded: true,
                loader: Global.platformMenuLoader
            })
        });
        this.menuTree.on("checkchange", function(node, eventObject) {
            var id = (node.id != 'root' ? node.id : "");
            var params={
                    cmd: "disMenu",
                    menuId: id,
                    permissionId: this.seletPermissionId
                };
            if (!node.ui.checkbox.checked) {
                params.del=1;
            }
            Ext.Ajax.request({
                url: "permission.java",
                params: params
            })
        }, this);
        return this.menuTree;
    },
    createForm: function() {
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
                    title: '权限基本信息',
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
                    },{
                        xtype: "hidden",
                        name: "type",
                        value:"2"
                    }, {
                        fieldLabel: "权限名称",
                        name: "name",
                        emptyText: '权限名称不能为空',
                        allowBlank: false,
                        blankText: '权限名称不能为空'
                    }, {
                        fieldLabel: "权限代码",
                        name: "sn",
                        emptyText: '权限代码不能为空',
                        allowBlank: false,
                        blankText: '权限代码不能为空'
                    }, {
                        xtype: "textarea",
                        fieldLabel: "权限简介",
                        name: "description"
                    }]
                }]
            }]
        });
        return formPanel;
    },
    reloadLeftTree: function() {
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
    createWin: function(callback, autoClose) {
        return this.initWin(750, 550, "权限管理", callback, autoClose);
    },
    storeMapping: ["id", "name", "sn", "description", "resources", "menus", "tenant", "operation"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "权限名称",
            sortable: true,
            width: 160,
            dataIndex: "name"
        }, {
            header: "权限代码",
            sortable: true,
            width: 160,
            dataIndex: "sn"
        }, {
            header: "所属租户",
            sortable: true,
            width: 160,
            dataIndex: "tenant",
            renderer: this.objectRender("title")
        }]);
        ShopPermissionManagePanel.superclass.initComponent.call(this);
    }
});
