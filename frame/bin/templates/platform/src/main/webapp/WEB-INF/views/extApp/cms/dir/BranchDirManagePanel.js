//分行栏目管理
if (typeof Global === "undefined") {
    Global = {};
}
//分行下的栏目树
Global.branchDirLoader = new Ext.tree.TreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.BRANCH_DIR_LOADER",//缓存Key
    url: "newsDir.java?cmd=getBranchDirTree&pageSize=-1&treeData=true&all=false&orgType=null",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});
BranchDirGridListPanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "branchDirGridListPanel",
    baseUrl: "newsDir.java",
    baseQueryParameter: {
        orgType: null
    },
    viewWin: {
        width: 400,
        height: 310,
        title: '分行分类信息'
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
        var win = BranchDirGridListPanel.superclass.edit.call(this);
        if (win) {
            var record = this.grid.getSelectionModel().getSelected();
            var parentObj = record.get('parent');
            var parentDir = this.fp.form.findField('parent');
            this.reloadTreeByOrgId(parentDir, null);

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
                fieldLabel: '上级栏目',
                name: 'parent',
                xtype: "treecombo",
                hiddenName: "parentId",
                displayField: "title",
                valueField: "id",
                tree: new Ext.tree.TreePanel({
                    autoScroll: true,
                    root: new Ext.tree.AsyncTreeNode({
                        id: "root",
                        text: "所有栏目",
                        expanded: true,
                        iconCls: 'treeroot-icon',
                        loader: Global.branchDirLoader
                    })
                })
            }, {
                fieldLabel: '首页显示',
                name: 'showDesk',
                xtype: 'radiogroup',
                items: [{
                    checked: true,
                    boxLabel: '显示',
                    name: 'showDesk',
                    inputValue: 1
                }, {
                    boxLabel: '不显示',
                    name: 'showDesk',
                    inputValue: 0
                }]
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
    },
    reloadLeftTree: function() {
        if (this.tree) {
            //Global.branchDirLoader.remoteObject.clearData();
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
        return this.initWin(500, 250, "分行栏目管理", callback, autoClose);
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
    showDeskRender: function(v) {
        if (v == 1) {
            return "显示";
        } else if (v == 0) {
            return "不显示";
        } else {
            return "未知";
        }
    },
    storeMapping: ["id", "code", "name", "intro", "dirPath", "tel", "fax", "sequence", "parent", "status", "showDesk"],
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
            header: "首页显示",
            sortable: true,
            width: 100,
            dataIndex: "showDesk",
            renderer: this.showDeskRender
        }, {
            header: "排序",
            sortable: true,
            width: 100,
            dataIndex: "sequence"
        }, {
            header: "简介",
            sortable: true,
            width: 300,
            dataIndex: "intro"
        }, {
            width: 80,
            header: "状态",
            sortable: true,
            dataIndex: "status",
            renderer: this.statusRender
        }]);
        /*this.gridButtons = [{
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
        }, "-"];*/
        BranchDirGridListPanel.superclass.initComponent.call(this);
        this.on("saveobject", this.reloadLeftTree, this);
        this.on("removeobject", this.reloadLeftTree, this);
    },
    listeners: {
        render: function(e) {
            //e.create()
        }
    }
});
BranchDirManagePanel = Ext.extendX(Disco.Ext.TreeCascadePanel, function(superclass) {
    return {
        initComponent: function() {
            BranchDirManagePanel.superclass.initComponent.call(this);
        },
        treeCfg: {
            //rootVisible: false,
            title: "分行栏目信息",
            rootText: '所有栏目',
            autoScroll: true,
            expanded: true,
            rootIconCls: 'treeroot-icon',
            loader: Global.branchDirLoader
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
                this.panel = new BranchDirGridListPanel();
                this.panel.tree = this.tree;
                this.list = this.panel.grid;
            }
            return this.panel;
        }
    }
});
