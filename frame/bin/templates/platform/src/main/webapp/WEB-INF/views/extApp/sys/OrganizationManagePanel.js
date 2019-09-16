if (typeof Global === "undefined") {
    Global = {};
}
Global.organizationLoader = new Disco.Ext.MemoryTreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.ORG_LOADER_NODES",
    url: "organization.java?cmd=getTree&pageSize=-1&treeData=true&all=false",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});

OrganizationListPanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "organizationListPanel",
    baseUrl: "organization.java",
    viewWin: {
        width: 430,
        height: 310,
        title: '组织机构信息'
    },
    orgTypeFormat: function(v) {
        if (v === 1) {
            return "部门";
        } else if (v === 2) {
            return "支行";
        } else {
            return "未知机构";
        }
    },
    onCreate: function() {
        //this.fp.form.findField("parent").setOriginalValue(this.parent || null);
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
                fieldLabel: '机构码号',
                name: 'sn',
                allowBlank: false
            }, {
                fieldLabel: '机构名称',
                name: 'title',
                allowBlank: false
            }/*, {
            fieldLabel: '上级机构',
            name: 'parent',
            xtype: "treecombo",
            hiddenName: "parent",
            displayField: "title",
            valueField: "id",
            tree: new Ext.tree.TreePanel({
            autoScroll: true,
            root: new Ext.tree.AsyncTreeNode({
            id: "root",
            text: "上海分行",
            expanded: true,
            iconCls: 'treeroot-icon',
            loader: Global.organizationLoader
            })
            })
            }*/, {
                fieldLabel: '地址',
                name: 'address'
            }, {
                name: 'principal',
                fieldLabel: '负责人'
            }, {
                fieldLabel: '电话',
                name: 'tel'
            }, {
                fieldLabel: '邮编',
                name: 'postal'
            }, {
                fieldLabel: '传真',
                name: 'fax'
            }, {
                fieldLabel: '邮箱',
                name: 'email'
            }, {
                fieldLabel: '网址',
                name: 'url'
            }, {
                name: 'sequence',
                fieldLabel: '排序号',
                xtype: 'numberfield'
            }, {
                name: 'orgType',
                allowBlank: false,
                xtype: 'radiogroup',
                fieldLabel: '机构类型',
                items: [{
                    xtype: 'radio',
                    boxLabel: '部门',
                    inputValue: 1,
                    name: 'orgType',
                    checked: true
                }, {
                    xtype: 'radio',
                    boxLabel: '支行',
                    inputValue: 2,
                    name: 'orgType'
                }]
            }), {
                name: 'intro',
                fieldLabel: '职能介绍',
                xtype: "textarea"
            }, {
                fieldLabel: '附注',
                xtype: "textarea",
                name: 'annotation'
            }]
        });
        return formPanel;
    },
    reloadTree: function() {
        if (this.tree) {
            //刷新树之前先清除本地缓存
            Global.organizationLoader.remoteObject.clearData();
            this.tree.root.reload();
        }
        if (this.fp) {
            var parentNode = this.fp.form.findField("parent");
            if (parentNode && parentNode.tree.rendered)
                parentNode.tree.root.reload();
        }
    },
    createWin: function(callback, autoClose) {
        return this.initWin(650, 400, "分行下的组织机构信息录入", callback, autoClose);
    },
    storeMapping: ["id", "sn", "title", "parent", "principal", "address", "intro", "dirPath", "tel", "fax", "sequence", "postal", "email", "url", "annotation", "orgType"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "编号",
            sortable: true,
            width: 100,
            dataIndex: "sn"
        }, {
            header: "名称",
            sortable: true,
            width: 100,
            dataIndex: "title"
        }/*, {
        width: 100,
        header: "上级",
        sortable: true,
        dataIndex: "parent",
        renderer: this.objectRender("title")
        }*/, {
            header: "电话",
            sortable: true,
            width: 100,
            dataIndex: "tel"
        }, {
            header: "机构类型",
            sortable: true,
            width: 100,
            align: 'center',
            dataIndex: "orgType",
            renderer: this.orgTypeFormat
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
        }];*/
        OrganizationListPanel.superclass.initComponent.call(this);
        this.on("removeobject", this.reloadTree, this);
        this.on("saveobject", this.reloadTree, this);
    },
    listeners: {
        render: function(e) {
            //e.create();
        }
    }
});
OrganizationManagePanel = Ext.extendX(Disco.Ext.TreeCascadePanel, function(superclass) {
    return {
        initComponent: function() {
            OrganizationManagePanel.superclass.initComponent.call(this);
            this.tree.on({
                scope: this,
                render: this.onTreeRender
            });
        },
        onTreeRender: function(_this) {
            //_this.expandAll();
        },
        queryParam: 'parentId',
        treeCfg: {
            autoScroll: true,
            title: "组织机构树",
            rootText: '上海分行',
            rootIconCls: 'treeroot-icon',
            loader: Global.organizationLoader
        },
        onTreeClick: function(node) {
            var id = (node.id != 'root' ? node.id : "");
            this.panel.parent = {
                id: id,
                title: node.text
            };
            this.list.store.baseParams.parent = id;
            superclass.onTreeClick.apply(this, arguments);
        },
        getPanel: function() {
            if (!this.panel) {
                this.panel = new OrganizationListPanel();
                this.panel.tree = this.tree;
                this.list = this.panel.grid;
            }
            return this.panel;
        }
    }
});
