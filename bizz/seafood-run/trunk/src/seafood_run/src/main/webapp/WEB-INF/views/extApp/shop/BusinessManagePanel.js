if (typeof Global === "undefined") {
    Global = {};
}

//模版栏目分类树
Global.shopMenuLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "shopTenantMenu.java?cmd=getTree&pageSize=-1",
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



BusinessManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "businessManagePanel",
    //title:"视频文件管理",
    baseUrl: "shopTenantMenu.java",
    gridSelModel: 'checkbox',
    showView: false,
	showAdd: false,
    showEdit: false,
    showRemove:false,
    baseQueryParameter: {
        type: 0
        //文件类型的
    },

    status: function(v) {
        if (v == -1) {
            return "停用";
        }
        if (v == 0) {
            return "正常";
        }
        if (v == 1) {
            return "禁用";
        }

    },
    typeRender: function(v) {
        if (v == "1") {
            return "平台管理员";
        } else if (v == "2") {
            return "租户管理员";
        } else {
            return "普通用户";
        }
    },
    pageSize: 20,
    create: function() {
        BusinessManagePanel.superclass.create.call(this);
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
        var win = BusinessManagePanel.superclass.edit.call(this);
        var tabs = this.fp.items.items[0];
        if (tabs.items.items[2]) {
            tabs.remove(tabs.items.items[2])
        }
        if (tabs.items.items[1]) {
            tabs.remove(tabs.items.items[1])
        }
        this.userId = record.get("id");
        tabs.add(this.editMenu());
    },
    editMenu: function() {
        Global.shopMenuLoader.baseParams.userId = this.userId;
        this.MenuTree = new Ext.tree.TreePanel({
            title: "菜单管理",
            autoScroll: false,
            width: 200,
            border: false,
            margins: '0 2 0 0',
            tools: [{
                id: "refresh",
                handler: function() {
                    this.MenuTree.root.reload();
                },
                scope: this
            }],
            root: new Ext.tree.AsyncTreeNode({
                id: "root",
                text: "所有菜单",
                iconCls: 'treeroot-icon',
                expanded: true,
                loader: Global.shopMenuLoader
            })
        });
        this.MenuTree.on("checkchange", function(node, eventObject) {
            var id = (node.id != 'root' ? node.id : "");
            var params={
                    cmd: "disMenu",
                    menuId: id,
                    userId: this.userId
                };
            if (!node.ui.checkbox.checked) {
                params.isdel=1;
            }
            Ext.Ajax.request({
                url: "shopTenantMenu.java",
                params: params
            })
        }, this);
        return this.MenuTree;
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
                    title: '用户基本信息',
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
                        name: "code",
                        value: ""
                    }, {
                        fieldLabel: "用户名称",
                        name: "name",
                        disabled:true
                    }, {
                        fieldLabel: "真实名称",
                        name: "trueName",
                        disabled:true
                    }, {
                        fieldLabel: '状态',
                        xtype: 'radiogroup',
                        disabled:true,
                        name: 'status',
                        items: [{
                            boxLabel: '停用',
                            name: 'status',
                            inputValue: "-1"
                        }, {
                            boxLabel: '正常',
                            name: 'status',
                            inputValue: "0"
                        }, {
                            checked: true,
                            boxLabel: '禁用',
                            name: 'status',
                            inputValue: "1"
                        }]
                    }, {
                        xtype: "textarea",
                        fieldLabel: "员工简介",
                        disabled:true,
                        height: 50
                    }]
                }]
            }]
        });
        return formPanel;
    },
    createWin: function() {
        return this.initWin(648, 430, "员工管理");
    },
    storeMapping: ["id", "name", "trueName", "tenant", "type", "status"],
    initComponent: function() {

        this.cm = new Ext.grid.ColumnModel([{
            header: "用户名称",
            sortable: true,
            width: 100,
            dataIndex: "name"
        }, {
            header: "用户真实姓名",
            sortable: true,
            width: 40,
            dataIndex: "trueName"
        }, {
            header: "用户类型",
            sortable: true,
            width: 40,
            dataIndex: "type",
            renderer: this.typeRender
        }, {
            header: "所属租户",
            sortable: true,
            width: 40,
            dataIndex: "tenant",
            renderer: this.objectRender("title")
        }, {
            header: " 状态",
            sortable: true,
            width: 40,
            dataIndex: "status",
            renderer: this.status
        }]);
        BusinessManagePanel.superclass.initComponent.call(this);
    }
});
