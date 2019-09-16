if (typeof Global === "undefined") {
    Global = {};
}
if (!Global.userRoleTreeLoader) {
    Global.userRoleTreeLoader = new Ext.tree.TreeLoader({
        iconCls: 'lanyo-tree-node-icon',
        url: "role.java?cmd=getTree&isUserTenantR=1",
        listeners: {
            'beforeload': function(treeLoader, node) {
                treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            }
        }
    })
}
UserManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "userManagePanel",
    //title:"视频文件管理",
    baseUrl: "user.java",
    gridSelModel: 'checkbox',
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
    batchRemoveMode: true,
    showAdd: true,
    showEdit: true,
    showView: false,
    create: function() {
        UserManagePanel.superclass.create.call(this);
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
        var win = UserManagePanel.superclass.edit.call(this);
        var tabs = this.fp.items.items[0];
        if (tabs.items.items[2]) {
            tabs.remove(tabs.items.items[2])
        }
        if (tabs.items.items[1]) {
            tabs.remove(tabs.items.items[1])
        }
        this.selectemployeeId = record.get("id");
        tabs.add(this.editRole());
    },
    editRole: function() {
        Global.userRoleTreeLoader.baseParams.userId = this.selectemployeeId;
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
                loader: Global.userRoleTreeLoader
            })
        });
        this.RoleTree.on("checkchange", function(node, eventObject) {
            var id = (node.id != 'root' ? node.id : "");
            var params={
                    cmd: "addRole",
                    roleId: id,
                    userId: this.selectemployeeId
                };
            if (!node.ui.checkbox.checked) {
                params.cmd="delRole";
            }
            Ext.Ajax.request({
                url: "user.java",
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
                    }, {
                        fieldLabel: "登陆名称",
                        name: "code",
                        emptyText: '登陆名称不能为空',
                        allowBlank: false,
                        blankText: '登陆名称不能为空'
                    }, {
                        fieldLabel: "用户名称",
                        name: "name",
                        emptyText: '用户不能为空',
                        allowBlank: false,
                        blankText: '用户不能为空'
                    }, {
                        fieldLabel: "真实名称",
                        name: "trueName",
                        emptyText: '真实名称不能为空',
                        allowBlank: false,
                        blankText: '真实名称不能为空'
                    }, {
                        fieldLabel: '状态',
                        xtype: 'radiogroup',
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
                        name: "intro",
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
    storeMapping: ["id", "name", "trueName", "code", "tenant", "type", "status"],
    initComponent: function() {

        this.cm = new Ext.grid.ColumnModel([{
            header: "登陆名",
            sortable: true,
            width: 100,
            dataIndex: "code"
        }, {
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
        this.gridButtons = [{
			text : "初始化密码",
			cls : "x-btn-text-icon",
			icon : "img/core/up.gif",							
			handler : this.initPassword,
			scope : this
		}];
        UserManagePanel.superclass.initComponent.call(this);
    },
    initPassword:function(){
    	var record = this.grid.getSelectionModel().getSelected();
    	if(record){
    		Ext.Ajax.request({
    			url: "manage.java?cmd=initPassword&id="+record.get("id"),
    			method:"GET",
    			success : function(response) {
    				Ext.Msg.alert("提示","修改成功", this);
    			},
    			scope : this
    		});
    	}
    }
});
