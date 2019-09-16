if (typeof Global === "undefined") {
    Global = {};
}
if (!Global.templateDirLoader) {
    Global.templateDirLoader = new Ext.tree.TreeLoader({
        iconCls: 'lanyo-tree-node-icon',
        url: "templateDir.java?cmd=getTemplateDir&pageSize=-1&treeData=true",
        listeners: {
            'beforeload': function(treeLoader, node) {
                treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            }
        }
    })
}

/**
 * 信息模板管理
 * 
 * @class TemplateListPanel
 * @extends Disco.Ext.CrudPanel
 */
TemplateListPanel = Ext.extend(Disco.Ext.CrudPanel, {
    baseUrl: "templateFile.java",
    pageSize: 20,
    showView: false,// 用来定义是否显示查看面板
    statusRender: function(v) {
        if (v == 1)
            return "<span style='color:green;'>正常</span>";
        else if (v == 0)
            return "<span style='color:blue;'>锁定</span>";
        else if (v == -1)
            return "<span style='color:red;'>回收站</span>";
        else
            return "<span style='color:wheat;'>未知错误</span>";
    },
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 60,
            fileUpload: true,
            labelAlign: 'right',
            defaults: {
                xtype: "textfield"
            },
            items: [{
                xtype: "hidden",
                name: "id"
            }, {
                xtype: "fieldset",
                title: '模板基本信息',
                height: 110,
                defaults: {
                    xtype: "textfield",
                    anchor: "100%"
                },
                items: [Disco.Ext.Util.twoColumnPanelBuild({
                    fieldLabel: "模板名称",
                    name: "name",
                    anchor: '100%'
                }, {
                    xtype: "treecombo",
                    fieldLabel: "模板类别",
                    collapsible: true,
                    allowBlank: false,
                    displayField: "name",
                    name: "dirId",
                    anchor: '100%',
                    hiddenName: "dirId",
                    tree: new Ext.tree.TreePanel({
                        rootVisible: false,
                        root: new Ext.tree.AsyncTreeNode({
                            id: "root",
                            text: "根级分类",
                            expanded: true,
                            listeners: {
                                "expand": function(node) {
                                    node.getOwnerTree().expandAll();
                                },
                                scope: this
                            },
                            loader: Global.templateDirLoader
                        })
                    })
                }), {
                    fieldLabel: '模板简介',
                    name: "description"
                }, {
                    xtype: 'fileuploadfield',
                    emptyText: '单击右侧上传按钮选择需上传的模板文件......',
                    fieldLabel: '上传模板',
                    // anchor : "-6",
                    name: 'url',
                    buttonCfg: {
                        text: '',
                        iconCls: 'upload-icon'
                    }
                }]
            }]
        });
        return formPanel;
    },
    create: function() {
        TemplateListPanel.superclass.create.call(this);
        this.fp.form.findField("dirId").setValue(this.currentDir);// 设置栏目
    },
    edit: function() {
        var win = TemplateListPanel.superclass.edit.call(this);
        if (win) {
            /*var record = this.grid.getSelectionModel().getSelected();
            Ext.Ajax.request({
                url: this.baseUrl + "?cmd=getContent&id=" + record.get("id"),
                success: function(response) {
                    var data = Ext.decode(response.responseText);
                }
            });*/
        }
    },
    createWin: function() {
        return this.initWin(650, 195, "模板内容管理");
    },
    storeMapping: ["id", "name", "content", "description", "createTime", "status", "url", "dir", {
        name: "dirId",
        mapping: "dir"
    }],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "模板名称",
            sortable: true,
            width: 80,
            dataIndex: "name"
        }, {
            header: "模板分类",
            sortable: true,
            width: 80,
            dataIndex: "dir",
            renderer: this.objectRender("name")
        }, {
            header: "模板路径",
            sortable: true,
            width: 300,
            dataIndex: "url"
        }, {
            header: "简介",
            sortable: true,
            hidden: true,
            width: 300,
            dataIndex: "description"
        }, {
            header: "上传日期",
            sortable: true,
            width: 95,
            dataIndex: "createTime",
            renderer: this.dateRender()
        }, {
            header: "模板状态",
            sortable: true,
            width: 60,
            dataIndex: "status",
            renderer: this.statusRender
        }])
        TemplateListPanel.superclass.initComponent.call(this);
    }
});

// 系统菜单管理
TemplateFileManagePanel = function() {
    this.list = new TemplateListPanel({
        region: "center",
        layout: 'fit',
        border: true,
        margins: '4 4 4 4'
    });
    this.btn_add = Ext.apply({}, {
        scope: this,
        handler: function() {
            var currentNode = this.list.tree.getSelectionModel().getSelectedNode();
            Disco.Ext.Util.addObject("TemplateDirListPanel", function() {
                this.list.tree.root.reload();
            }.createDelegate(this), "cms/TemplateDirManagePanel.js", "", function(win, scope) {
                // 新增时指定父分类
                var parent = {};
                if (currentNode && currentNode.id != "root") {
                    parent.id = currentNode.id;
                    parent.name = currentNode.text;
                } else {
                    parent = "";
                }
                scope.fp.form.findField("parentId").setValue(parent);
            });
        }
    }, ConfigConst.buttons.add);

    this.btn_edit = Ext.apply({}, {
        disabled: true,
        scope: this,
        handler: function() {
            Disco.Ext.Util.editObject("TemplateDirListPanel", function() {
                this.list.tree.root.reload()
            }.createDelegate(this), "cms/TemplateDirManagePanel.js", "", this.list.tree.getSelectionModel().getSelectedNode().id, function(win, obj, scope) {
            }.createDelegate(this))
        }
    }, ConfigConst.buttons.edit);
    this.btn_remove = Ext.apply({}, {
        disabled: true,
        scope: this,
        handler: function() {
            Disco.Ext.Util.removeObject("TemplateDirListPanel", function() {
                this.list.tree.root.reload()
            }.createDelegate(this), "cms/TemplateDirManagePanel.js", "", this.list.tree.getSelectionModel().getSelectedNode().id)
        }
    }, ConfigConst.buttons.remove);
    if (!this.menu) {
        this.menu = new Ext.menu.Menu({
            items: [this.btn_add, this.btn_edit, this.btn_remove]
        });
    }
    // 让一系列的菜单项变成可用状态
    this.enableOperaterItem = function() {
        for (var i = 0; i < arguments.length; i++) {
            var o = this.menu.items.get(arguments[i]);
            if (o && o.enable)
                o.enable();
        }
    };
    // 禁用一系列的菜单项
    this.disableOperaterItem = function() {
        for (var i = 0; i < arguments.length; i++) {
            var o = this.menu.items.get(arguments[i]);
            if (o && o.disable)
                o.disable();
        }
    };
    // 树右键菜单
    this.showTreeMenu = function(node, e) {
        var evn = e ? e : node;
        evn.preventDefault();
        if (node && node.id && !isNaN(parseInt(node.id)) && node.id > 0) {
            this.tree.getSelectionModel().select(node);
            this.enableOperaterItem("tb_edit", "tb_remove", "tb_add");
        } else {
            // 当是根节点或节点没有ID值是则只能[新增]不能删除或修改
            this.enableOperaterItem("tb_add");
            this.disableOperaterItem("tb_edit", "tb_remove");
        }
        this.menu.showAt(evn.getPoint());
    }
    this.tree = new Ext.tree.TreePanel({
        title: "模板分类",
        region: "west",
        margins: '4 0 4 0',
        autoScroll: true,
        width: 210,
        bbar: [{
            text: '新建'
        }, {
            text: '删除'
        }, {
            text: '上移'
        }, {
            text: '下移'
        }],
        tools: [{
            id: "refresh",
            handler: function() {
                this.tree.root.reload();
            },
            scope: this
        }],
        root: new Ext.tree.AsyncTreeNode({
            id: "root",
            text: "模板树",
            iconCls: 'lanyo-tree-node-icon',
            expanded: true,
            listeners: {
                "expand": function(node) {
                    node.getOwnerTree().expandAll();
                },
                scope: this
            },
            loader: Global.templateDirLoader
        })
    });
    this.list.tree = this.tree;
    // 树节点右键菜单
    this.tree.on("contextmenu", this.showTreeMenu, this);
    this.tree.on("click", function(node, eventObject) {
        var id = (node.id != 'root' ? node.id : "");
        this.list.dirId = id;
        this.list.store.baseParams.dirId = id;
        if (id) {
            this.list.currentDir = {
                id: id,
                name: node.text
            };
        } else
            this.list.currentDir = null;
        this.list.store.removeAll();
        this.list.store.load();
    }, this);
    TemplateFileManagePanel.superclass.constructor.call(this, {
        id: "templateListPanel",
        // title : "信息栏目分类管理",
        closable: true,
        border: false,
        autoScroll: true,
        layout: "border",
        items: [this.tree, this.list]
    });

};
Ext.extend(TemplateFileManagePanel, Ext.Panel, {});