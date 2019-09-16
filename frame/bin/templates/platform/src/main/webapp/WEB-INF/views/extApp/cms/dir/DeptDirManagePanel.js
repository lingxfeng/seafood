//部门栏目管理
if (typeof Global === "undefined") {
    Global = {};
}
//部门下的栏目树
Global.deptDirLoader = new Ext.tree.TreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.DEPT_DIR_LOADER",//缓存Key
    url: "newsDir.java?cmd=getDeptDirTree&pageSize=-1&treeData=true&all=false&orgType=1",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});
//部门树
Global.deptTreeLoader = new Disco.Ext.MemoryTreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.DEPT_TREE_LOADER",//缓存Key
    url: "organization.java?cmd=getTree&pageSize=-1&treeData=true&all=false&orgType=1",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});
DpetDirGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "dpetDirGridListPanel",
    baseUrl: "newsDir.java",
    baseQueryParameter: {
        orgType: 1
    },
    viewWin: {
        width: 400,
        height: 310,
        title: '部门分类信息'
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
    onCreate: function() {
        this.fp.form.findField("parent").setOriginalValue(this.parentDir || null);
    },
    edit: function() {
        var win = DpetDirGridListPanel.superclass.edit.call(this);
        if (win) {
            var record = this.grid.getSelectionModel().getSelected();
            var parentObj = record.get('parent');
            var parentDir = this.fp.form.findField('parent');
            this.reloadTreeByOrgId(parentDir, record.get('org').id);

            //编辑回显时如果有父级栏目及回显数据
            if (parentObj) {
                parentObj.title || (parentObj.title = parentObj.name);
                parentDir.setOriginalValue(parentObj);
            }
        }
    },
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 70,
            labelAlign: 'right',
            defaultType: 'textfield',
            defaults: {
                anchor: "-20"
            },
            items: [{
                xtype: "hidden",
                name: "id"
            }, Disco.Ext.Util.twoColumnPanelBuild({
                fieldLabel: '栏目序号',
                name: 'sequence',
                xtype: 'numberfield',
                allowBlank: false
            }, {
                fieldLabel: '栏目名称',
                name: 'name',
                allowBlank: false
            }, {
                fieldLabel: '所属部门',
                name: 'org',
                xtype: "treecombo",
                hiddenName: "orgId",
                displayField: "title",
                valueField: "id",
                allowBlank: false,
                tree: new Ext.tree.TreePanel({
                    rootVisible: false,
                    autoScroll: true,
                    root: new Ext.tree.AsyncTreeNode({
                        id: "root",
                        text: "所有部门",
                        expanded: true,
                        iconCls: 'treeroot-icon',
                        loader: Global.deptTreeLoader
                    }),
                    listeners: {
                        scope: this,
                        'click': function(node) {
                            //栏目节点变为可用并且值重新设定
                            var parentDir = this.fp.form.findField('parent');
                            this.reloadTreeByOrgId(parentDir, node.id);
                        }
                    }
                })
            }, {
                disabled: true,
                fieldLabel: '上级栏目',
                name: 'parent',
                xtype: "treecombo",
                hiddenName: "parentId",
                displayField: "title",
                valueField: "id",
                tree: new Ext.tree.TreePanel({
                    //rootVisible: false,
                    autoScroll: true,
                    root: new Ext.tree.AsyncTreeNode({
                        id: "root",
                        text: "所有栏目",
                        expanded: true,
                        iconCls: 'treeroot-icon',
                        loader: Global.deptDirLoader
                    })
                })
            }, {
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
            }, {
                fieldLabel: '私有栏目',
                name: 'isPrivateDir',
                xtype: 'radiogroup',
                items: [{
                    boxLabel: '是',
                    name: 'isPrivateDir',
                    inputValue: 1
                }, {
                    checked: true,
                    boxLabel: '否',
                    name: 'isPrivateDir',
                    inputValue: 0
                }]
            }), {
                fieldLabel: '附注',
                xtype: "textarea",
                name: 'description'
            }]
        });
        return formPanel;
    },
    /**
     * 根据机构ID重新加载该机构下的栏目树
     * @param {Object} tree
     * @param {String} orgId
     */
    reloadTreeByOrgId: function(parentDir, orgId) {
        !parentDir.disabled || parentDir.setDisabled(false);
        parentDir.reset();
        parentDir.setValue(null);
        var tree = parentDir.tree;

        //给栏目树加载器设置一个查询参数为部门的ID
        tree.root.attributes.loader.baseParams = {
            orgId: orgId
        }
        //如果当前栏目的树对象已渲染则直接重新加载
        if (tree.rendered) {
            tree.root.reload();
            tree.expandAll();
        }
    },
    reloadLeftTree: function() {
        if (this.tree) {
            //Global.deptDirLoader.remoteObject.clearData();
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
        return this.initWin(500, 250, "部门栏目管理", callback, autoClose);
    },
    findDeptDirByOrgId: function(node, e) {
        var orgId = node.id;
        if (orgId == 'root') {
            orgId = null;
        }
        //重新加载grid
        this.grid.getStore().baseParams.orgId = orgId;
        this.grid.getStore().baseParams.parentId = null;
        this.grid.getStore().load();

        //重新加载tree
        this.tree.getLoader().baseParams = {
            orgId: orgId
        };
        this.tree.root.reload();
        this.tree.expandAll();
        this.tree.root.select();
    },
    storeMapping: ["id", "code", "name", "org", "intro", "dirPath", "tel", "fax", "sequence", "parent", "status", 'isPrivateDir'],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "序号",
            sortable: true,
            width: 100,
            dataIndex: "code"
        }, {
            header: "名称",
            sortable: true,
            width: 160,
            dataIndex: "name"
        }, {
            header: "上级栏目",
            sortable: true,
            width: 100,
            dataIndex: "parent",
            renderer: this.objectRender("name")
        }, {
            header: "所属部门",
            sortable: true,
            width: 100,
            dataIndex: "org",
            renderer: this.objectRender("title")
        }, {
            header: "电话",
            sortable: true,
            width: 100,
            dataIndex: "tel"
        }, {
            header: "排序",
            sortable: true,
            width: 100,
            dataIndex: "sequence"
        }, {
            width: 65,
            sortable: true,
            header: "私有栏目",
            dataIndex: "isPrivateDir",
            renderer: this.booleanRender
        }, {
            flex: 1,
            header: "简介",
            sortable: true,
            dataIndex: "intro"
        }, {
            width: 80,
            header: "状态",
            sortable: true,
            dataIndex: "status",
            renderer: this.statusRender
        }]);
        this.gridButtons = ['-', '&nbsp;&nbsp;&nbsp;', {
            type: 'tbtext',
            xtype: 'tbtext',
            text: '部门筛选：'
        }, {
            name: 'org',
            type: 'combo',
            xtype: "treecombo",
            hiddenName: "orgId",
            displayField: "title",
            text: '所有部门',
            valueField: "id",
            tree: new Ext.tree.TreePanel({
                autoScroll: true,
                root: new Ext.tree.AsyncTreeNode({
                    id: "root",
                    text: "所有部门",
                    expanded: true,
                    iconCls: 'treeroot-icon',
                    loader: Global.deptTreeLoader
                }),
                listeners: {
                    scope: this,
                    click: this.findDeptDirByOrgId
                }
            })
        }]
        this.gridButtons = ['&nbsp;&nbsp;&nbsp;', '-', {
            type: 'tbtext',
            xtype: 'tbtext',
            text: '按部门筛选：'
        }, {
            name: 'org',
            type: 'combo',
            width: 200,
            xtype: "treecombo",
            hiddenName: "orgId",
            displayField: "title",
            text: '所有部门',
            valueField: "id",
            tree: new Ext.tree.TreePanel({
                autoScroll: true,
                root: new Ext.tree.AsyncTreeNode({
                    id: "root",
                    text: "所有部门",
                    expanded: true,
                    iconCls: 'treeroot-icon',
                    loader: Global.deptTreeLoader
                }),
                listeners: {
                    scope: this,
                    click: this.findDeptDirByOrgId
                }
            })
        }/*, {
        text: "上移",
        cls: "x-btn-text-icon",
        icon: "img/ria/up.gif",
        handler: this.swapSequence(""),
        scope: this
        }, {
        text: "下移",
        cls: "x-btn-text-icon",
        icon: "img/ria/down.gif",
        handler: this.swapSequence(true),
        scope: this
        }, "-"*/];
        DpetDirGridListPanel.superclass.initComponent.call(this);
        this.on("saveobject", this.reloadLeftTree, this);
        this.on("removeobject", this.reloadLeftTree, this);
    },
    listeners: {
        render: function(e) {
            //e.create()
        }
    }
});
DeptDirManagePanel = Ext.extendX(Disco.Ext.TreeCascadePanel, function(superclass) {
    return {
        initComponent: function() {
            DeptDirManagePanel.superclass.initComponent.call(this);
        },
        treeCfg: {
            //rootVisible: false,
            title: "部门栏目信息",
            rootText: '所有栏目',
            expanded: true,
            rootIconCls: 'treeroot-icon',
            loader: Global.deptDirLoader
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
                this.panel = new DpetDirGridListPanel();
                this.panel.tree = this.tree;
                this.list = this.panel.grid;
            }
            return this.panel;
        }
    }
});