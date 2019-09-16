// 权限管理
if (typeof Global === "undefined") {
    Global = {};
}
Global.platformMenuLoader = new Ext.tree.TreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.DEPT_DIR_LOADER",// 缓存Key
    url: "systemMenu.java?cmd=getTree&pageSize=-1&isPerm=true&type=2",
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
PermissionManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "permissionManagePanel",
    baseUrl: "permission.java",
    viewWin: {
        width: 400,
        height: 310,
        title: '权限信息'
    },
    showView: false,
    create: function() {
        PermissionManagePanel.superclass.create.call(this);
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
        var win = PermissionManagePanel.superclass.edit.call(this);
        var tabs = this.fp.items.items[0];
        if (tabs.items.items[2]) {
            tabs.remove(tabs.items.items[2])
        }
        if (tabs.items.items[1]) {
            tabs.remove(tabs.items.items[1])
        }
        this.seletPermissionId = record.get("id");
        tabs.add(this.editMenu());
        this.curRessouceGride = this.resouceGride();
        tabs.add(this.curRessouceGride);
        this.resoucestore.baseParams.permissionId = record.get("id");
        this.resoucestore.load();
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

    resoucestore: new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: '/resource.java?cmd=list&pageSize=16'
        }),//你请求数据的路径
        reader: new Ext.data.JsonReader({
            totalProperty: 'rowCount',
            root: 'result'
        }, [{
            name: "id"
        }, {
            name: 'type'
        }, {
            name: 'resStr'
        }, {
            name: 'desciption'
        }])
    }),
    resouceGride: function() {
        var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(),{
            header: '资源描述',
            dataIndex: 'resStr',
            sortable: true,
            width: 100
        }, {
            header: '资源简介',
            dataIndex: 'desciption',
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
        var grid = new Ext.grid.GridPanel({
            height: 200,
            title: "资源管理",
            store: this.resoucestore,
            cm: cm,
            frame: false,
            pageSize: 10,
            autoHeight: false,
            viewConfig: {
                forceFit: true
            },
            listeners: {
                'itemclick': function(view, record, item, index, e) {
                    Ext.MessageBox.alert("标题", record.data.id);
                }
            },
            tbar: [{
                text: '新增',
                iconCls: "add",
                handler: this.createResouce,
                scope: this
            }, "-", {
                text: '删除',
                iconCls: "delete",
                handler: this.deleteResouce,
                scope: this
            }],
            bbar: pagingToolbar
        });
        return grid;
    },
    createResouce: function() {
        if (!this.curAddResourceGrid) {
            this.curAddResourceGrid = this.createResouceGride();
        }
        if (!this.addResourceWin) {
            this.addResourceWin = new Ext.Window({
                title: "添加资源",
                closeAction: 'hide',
                width: 500,
                height: 420,
                modal: true,
                items: [new Ext.form.FormPanel({
                    frame: true,
                    labelWidth: 100,
                    fileUpload: true,
                    items: [{
                        xtype: "hidden",
                        name: "id"
                    }, {
                        xtype: "treecombo",
                        fieldLabel: "包名",
                        name: "parent",
                        displayField: "title",
                        valueField: "id",
                        width: 320,
                        tree: new Ext.tree.TreePanel({
                            autoScroll: true,
                            root: new Ext.tree.AsyncTreeNode({
                                id: "root",
                                text: "所有菜单",
                                iconCls: 'treeroot-icon',
                                expanded: true,
                                loader: Global.sytemModuleLoader
                            })
                        }),
                        listeners: {
                            select: function(c, node) {
                                if (!node.isLeaf() && node.id != "root") {
                                    this.createResoucestore.baseParams.pack = node.id;
                                    this.createResoucestore.baseParams.action = "";
                                } else {
                                    this.createResoucestore.baseParams.pack = "";
                                    this.createResoucestore.baseParams.action = node.id == "0" ? "" : node.id;
                                }
                                this.createResoucestore.load();
                            },
                            scope: this
                        }
                    },new Ext.ux.form.SearchField({
						fieldLabel : "查询",
					  	name:'templateName',
					  	width: 320,
						onTrigger2Click:this.addResource,
						scope:this
					})]
                }), this.curAddResourceGrid],
                buttonAlign:'center',
                buttons: [{
                    text: '确定添加',
                    handler: this.addResouce,
                    scope: this
                }]
            })
        }
        this.createResoucestore.reload();
        this.addResourceWin.show();
    },
    addResource:function(){
    	var _this = this.scope;
    	_this.createResoucestore.baseParams.pack = this.getValue();
    	_this.createResoucestore.baseParams.action = "";
    	_this.createResoucestore.load();
    },
    createResouceGride: function() {
        var cm = new Ext.grid.ColumnModel([new Ext.grid.CheckboxSelectionModel(), {
            header: '资源描述',
            dataIndex: 'resStr',
            sortable: true,
            width: 100
        }, {
            header: '资源简介',
            dataIndex: 'desciption',
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
    createResoucestore: new Ext.data.Store({
        proxy: new Ext.data.HttpProxy({
            url: '/resource.java?cmd=list&pageSize=10'
        }),//你请求数据的路径
        reader: new Ext.data.JsonReader({
            totalProperty: 'rowCount',
            root: 'result'
        }, [{
            name: "id"
        }, {
            name: 'type'
        }, {
            name: 'resStr'
        }, {
            name: 'desciption'
        }])
    }),
    addResouce: function() {
        var records = this.curAddResourceGrid.getSelectionModel().getSelections();
        var ids = "";
        for (var i = 0; i < records.length; i++) {
            ids = ids + records[i].get("id") + ",";
        }
        if (ids != "") {
            Ext.Ajax.request({
                url: "permission.java",
                params: {
                    cmd: "disResource",
                    ids: ids,
                    permissionId: this.seletPermissionId
                },
                scope: this,
                success: function(req) {
                    this.addResourceWin.hide();
                    this.resoucestore.reload();
                }
            })
        }
    },
    deleteResouce: function() {
        var records = this.curRessouceGride.getSelectionModel().getSelections();
        var ids = "";
        for (var i = 0; i < records.length; i++) {
            ids = ids + records[i].get("id") + ",";
        }
        if (ids != "") {
            Ext.Ajax.request({
                url: "permission.java",
                params: {
                    cmd: "disResource",
                    ids: ids,
                    del: 1,
                    permissionId: this.seletPermissionId
                },
                scope: this,
                success: function(req) {
                    this.resoucestore.reload();
                }
            })
        }
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
        PermissionManagePanel.superclass.initComponent.call(this);
    }
});
