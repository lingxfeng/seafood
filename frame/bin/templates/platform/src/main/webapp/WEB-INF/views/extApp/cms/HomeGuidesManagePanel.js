// 首页导航管理

Global.homeguidesloader = new Disco.Ext.MemoryTreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.HOME_GUIDES_LOADER",
    url: "homeGuides.java?cmd=getTree&pageSize=-1&treeData=true&all=false",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});

//分行栏目树
Global.mySendDocBranchDirLoader = new Disco.Ext.MemoryTreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.MY_SEND_DOC_BRANCH_DIR_LOADER",//缓存Key
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

HomeGuidesManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "homeGuidesManagePanel",
    //title:"首页导航管理",
    baseUrl: "homeGuides.java",
    gridSelModel: 'checkbox',
    pageSize: 20,
    batchRemoveMode: true,
    parentname: function(v) {
        if (v.name == null) {
            return "<font color='red' >一级菜单 </font>";
        } else
            return v.name;
    },
    edit: function() {
        var win = HomeGuidesManagePanel.superclass.edit.call(this);
        if (win) {
            var record = this.grid.getSelectionModel().getSelected();
            var parentObj = record.get('parent');
            var parentDir = this.fp.form.findField('parentId');
            if (parentObj) {
                parentObj.name || (parentObj.name = parentDir.parentId);
                parentDir.setOriginalValue(parentObj);
            }
        }
    },
    createForm: function() {

        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 80,
            labelAlign: 'right',
            defaults: {
                width: 320,
                xtype: "textfield"
            },
            items: [{
                xtype: "hidden",
                name: "id"
            }, {
                fieldLabel: "名称",
                name: "name",
                emptyText: '名称不能为空',
                allowBlank: false,
                blankText: '名称不能为空'
            }, {
                fieldLabel: "URL地址",
                name: "linkUrl"
            }, {
                fieldLabel: '分行栏目',
                name: 'bankDirId',
                xtype: "treecombo",
                hiddenName: "bankDirId",
                displayField: "title",
                valueField: "id",
                tree: new Ext.tree.TreePanel({
                    rootVisible: false,
                    autoScroll: true,
                    root: new Ext.tree.AsyncTreeNode({
                        id: "root",
                        text: "所有栏目",
                        expanded: true,
                        iconCls: 'treeroot-icon',
                        loader: Global.mySendDocBranchDirLoader
                    })
                })
            }, {
                xtype: "numberfield",
                fieldLabel: "排序",
                name: "sort"
            }, {
                fieldLabel: '上级导航',
                name: 'parentId',
                xtype: "treecombo",
                hiddenName: "parentId",
                displayField: "name",
                valueField: "id",
                // allowBlank: false,
                tree: new Ext.tree.TreePanel({
                    rootVisible: false,
                    autoScroll: true,
                    root: new Ext.tree.AsyncTreeNode({
                        id: "root",
                        text: "所有栏目",
                        expanded: true,
                        iconCls: 'treeroot-icon',
                        loader: Global.homeguidesloader
                    })
                })

            }

            ]
        });
        return formPanel;
    },
    userRender: function(v) {
        return v ? v.trueName : "$!{lang.get('Unknown')}";
    },
    createWin: function() {
        return this.initWin(450, 260, "首页导航管理");
    },
    storeMapping: ["id", "name", "linkUrl", "target", "createTime", "sort", "parent"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "名称",
            sortable: true,
            width: 100,
            dataIndex: "name"
        }, {
            header: "URL地址",
            sortable: true,
            width: 100,
            dataIndex: "linkUrl"
        }, {
            header: "上级导航",
            sortable: true,
            width: 100,
            dataIndex: "parent",
            renderer: this.parentname
        }, {
            header: "排序",
            sortable: true,
            width: 50,
            dataIndex: "sort"
        }])
        HomeGuidesManagePanel.superclass.initComponent.call(this);
    }
});
