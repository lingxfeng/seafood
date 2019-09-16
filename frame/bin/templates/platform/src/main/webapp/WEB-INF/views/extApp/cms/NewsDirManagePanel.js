if (typeof Global === "undefined") {
    Global = {};
}
//栏目树
Global.newsDirLoader = new Disco.Ext.MemoryTreeLoader({
    iconCls: 'lanyo-tree-node-icon',
    varName: "Global.newsDirLoader",//缓存Key
    url: "newsDir.java?cmd=getNewsDirTree&pageSize=-1&treeData=true&all=true",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});
//文章模板
Global.templateFileDocLoader = new Disco.Ext.MemoryTreeLoader({
    iconCls: 'lanyo-tree-node-icon',
    varName: "Global.templateFileDocLoader",//缓存Key
    url: "templateFile.java?cmd=getTemplateFile&sn=doc_tpl&pageSize=-1&treeData=true",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});
//文章栏目模板
Global.templateFileDirLoader = new Disco.Ext.MemoryTreeLoader({
    iconCls: 'lanyo-tree-node-icon',
    varName: "Global.templateFileDirLoader",//缓存Key
    url: "templateFile.java?cmd=getTemplateFile&sn=dir_tpl&pageSize=-1&treeData=true",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});
NewsDirGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
    showView: false,
    gridSelModel: 'checkbox',
    id: "newsDirGridListPanel",
    baseUrl: "newsDir.java",
    baseQueryParameter: {
        orgType: 1
    },
    statusRender: function(v) {
        if (v == 1) {
            return "启用"
        } else if (v == 0) {
            return "停用"
        } else {
            return "未知"
        }
    },
    showLevelRender: function(v) {
        if (v == 1) {
            return "面向全体"
        } else if (v == 2) {
            return "教师可见"
        } else if (v == 3) {
            return "学生可见"
        } else {
            return "未知"
        }
    },
    onCreate: function() {
        this.fp.form.findField("parent").setOriginalValue(this.parentDir || null);
    },
    edit: function() {
        var win = NewsDirGridListPanel.superclass.edit.call(this);
        if (win) {
            var record = this.grid.getSelectionModel().getSelected();
            var parentObj = record.get('parent');
            var parentDir = this.fp.form.findField('parent');

            //编辑回显时如果有父级栏目及回显数据
            if (parentObj) {
                parentObj.title || (parentObj.title = parentObj.name);
                parentDir.setOriginalValue(parentObj);
            }
        }
    },
    //保存后执行
    onSave: function(form, action) {
        if (this.tree)
            this.tree.root.reload.defer(1000, this.tree.root);// 刷新左边的树
    },
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 60,
            labelAlign: 'right',
            defaults: {
                xtype: "textfield"
            },
            items: [{
                xtype: "hidden",
                name: "id"
            }, Disco.Ext.Util.columnPanelBuild({
                columnWidth: .50,
                items: {
                    fieldLabel: "栏目名称",
                    name: "name",
                    allowBlank: false
                }
            }, {
                columnWidth: .50,
                items: {
                    xtype: "treecombo",
                    fieldLabel: "父级目录",
                    name: "parent",
                    hiddenName: "parentId",
                    displayField: "name",
                    valueField: "id",
                    tree: new Ext.tree.TreePanel({
                        autoScroll: true,
                        root: new Ext.tree.AsyncTreeNode({
                            id: "root",
                            text: "根级栏目",
                            iconCls: 'lanyo-tree-node-icon',
                            expanded: true,
                            loader: Global.newsDirLoader
                        })
                    })
                }
            }, {
                columnWidth: .50,
                items: {
                    fieldLabel: "栏目编码",
                    name: "code",
                    allowBlank: false
                }
            }, {
                columnWidth: .50,
                items: [Ext.applyIf({
                    editable: false,
                    allowBlank: false,
                    listeners: {
                        select: function(c, r, o) {
                            var val = c.getValue();
                            var url = formPanel.form.findField("url");
                            var dir = formPanel.form.findField("dirTpl");
                            var doc = formPanel.form.findField("docTpl");
                            var objChange = function(obj) {
                                obj.setValue("");
                                obj.setDisabled(true);
                            };
                            if (val != "0") {
                                objChange(dir);
                                objChange(doc);
                            } else if (val == "0") {
                                dir.setDisabled(false);
                                doc.setDisabled(false);
                            }
                            if (val == '1') {
                                url.setDisabled(false);
                            } else {
                                url.setDisabled(true);
                            }
                        },
                        scope: this
                    }
                }, Disco.Ext.Util.buildCombox("types", "栏目类型", [["实体栏目", 0], ["链接栏目", 1], ["单页栏目", 2]], 0, true))]
            }, {
                columnWidth: .50,
                items: {
                    fieldLabel: "栏目图标",
                    name: "icon"
                }
            }, {
                columnWidth: .50,
                items: {
                    xtype: "treecombo",
                    fieldLabel: "文章模板",
                    name: "docTpl",
                    hiddenName: "docTplId",
                    displayField: "name",
                    valueField: "id",
                    tree: new Ext.tree.TreePanel({
                        autoScroll: true,
                        root: new Ext.tree.AsyncTreeNode({
                            id: "root",
                            text: "根节点",
                            iconCls: 'lanyo-tree-node-icon',
                            expanded: true,
                            listeners: {
                                "expand": function(node) {
                                    node.getOwnerTree().expandAll();
                                },
                                scope: this
                            },
                            loader: Global.templateFileDocLoader
                        })
                    })
                }
            }, {
                columnWidth: .50,
                items: {
                    fieldLabel: "排序编号",
                    name: "sequence"
                }
            }, {
                columnWidth: .50,
                items: {
                    xtype: "treecombo",
                    fieldLabel: "栏目模板",
                    name: "dirTpl",
                    hiddenName: "dirTplId",
                    displayField: "name",
                    valueField: "id",
                    tree: new Ext.tree.TreePanel({
                        autoScroll: true,
                        root: new Ext.tree.AsyncTreeNode({
                            id: "root",
                            text: "根节点",
                            iconCls: 'lanyo-tree-node-icon',
                            expanded: true,
                            listeners: {
                                "expand": function(node) {
                                    node.getOwnerTree().expandAll();
                                },
                                scope: this
                            },
                            loader: Global.templateFileDirLoader
                        })
                    })
                }
            }, {
                columnWidth: .50,
                items: {
                    fieldLabel: '访问级别',
                    xtype: 'radiogroup',
                    name: 'showLevel',
                    items: [{
                        checked: true,
                        boxLabel: '面向全体',
                        name: 'showLevel',
                        inputValue: 1
                    }, {
                        boxLabel: '教师可见',
                        name: 'showLevel',
                        inputValue: 2
                    }, {
                        boxLabel: '学生可见',
                        name: 'showLevel',
                        inputValue: 3
                    }]
                }
            }, {
                columnWidth: .50,
                items: {
                    fieldLabel: '栏目状态',
                    xtype: 'radiogroup',
                    name: 'status',
                    items: [{
                        checked: true,
                        boxLabel: '启用',
                        name: 'status',
                        inputValue: 1
                    }, {
                        boxLabel: '停用',
                        name: 'status',
                        inputValue: 0
                    }]
                }
            }, {
                columnWidth: 1,
                items: {
                    name: "url",
                    disabled: true,
                    fieldLabel: "目录外链"
                }
            })
            // {xtype:"checkbox",fieldLabel:"前台显示",name:"display",width:10},
            ]
        });
        return formPanel;
    },
    dirTypesRender: function(v) {
        if (v == 0)
            return "<span style='color:green;'>实体栏目</span>";
        else if (v == 1)
            return "<span style='color:blue;'>链接栏目</span>";
        else if (v == 2)
            return "<span style='color:red;'>单页栏目</span>";
        else
            return "<span style='color:red;'>未知错误</span>";

    },
    reloadLeftTree: function() {
        if (this.tree) {
            this.tree.getLoader().baseParams.orgId = null;
            this.tree.root.reload();
            this.tree.expandAll();
        }
        if (this.fp) {
            var parentNode = this.fp.form.findField("parent");
            if (parentNode && parentNode.tree.rendered) {
                parentNode.tree.root.reload();
                parentNode.tree.expandAll();
            }
        }
    },
    createWin: function(callback, autoClose) {
        return this.initWin(600, 240, "网站栏目管理", callback, autoClose);
    },
    storeMapping: ["id", "code", "name", 'types', "docNum", "url", "icon", "parent", "dirTpl", "docTpl", "sequence", "createDate", 'showLevel', "status"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "序号",
            sortable: true,
            width: 40,
            dataIndex: "code"
        }, {
            header: "名称",
            sortable: true,
            width: 120,
            dataIndex: "name"
        }, {
            header: "栏目连接",
            sortable: true,
            width: 160,
            dataIndex: "url"
        }, {
            header: "父级栏目",
            sortable: true,
            width: 70,
            dataIndex: "parent",
            renderer: this.objectRender("name")
        }, {
            header: "栏目类型",
            sortable: true,
            width: 70,
            dataIndex: "types",
            renderer: this.dirTypesRender
        }, {
            width: 60,
            header: "访问级别",
            sortable: true,
            dataIndex: "showLevel",
            renderer: this.showLevelRender
        }, {
            header: "排序",
            sortable: true,
            width: 60,
            dataIndex: "sequence"
        }, {
            width: 80,
            header: "状态",
            sortable: true,
            dataIndex: "status",
            renderer: this.statusRender
        }]);
        NewsDirGridListPanel.superclass.initComponent.call(this);
        this.on("saveobject", this.reloadLeftTree, this);
        this.on("removeobject", this.reloadLeftTree, this);
    },
    listeners: {
        render: function(e) {
            //e.create()
        }
    }
});
NewsDirManagePanel = Ext.extendX(Disco.Ext.TreeCascadePanel, function(superclass) {
    return {
        initComponent: function() {
            NewsDirManagePanel.superclass.initComponent.call(this);
        },
        treeCfg: {
            //rootVisible: false,
            title: "网站栏目信息",
            rootText: '所有栏目',
            expanded: true,
            autoScroll: true,
            rootIconCls: 'lanyo-tree-node-icon',
            loader: Global.newsDirLoader
        },
        onTreeClick: function(node) {
            var id = (node.id != 'root' ? node.id : "");
            this.panel.parentDir = {
                id: id,
                title: node.text
            };
            this.list.store.baseParams.parentId = id;
            superclass.onTreeClick.apply(this, arguments);
        },
        getPanel: function() {
            if (!this.panel) {
                this.panel = new NewsDirGridListPanel();
                this.panel.tree = this.tree;
                this.list = this.panel.grid;
            }
            return this.panel;
        }
    }
});
