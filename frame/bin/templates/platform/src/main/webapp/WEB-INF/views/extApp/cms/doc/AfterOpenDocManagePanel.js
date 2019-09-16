//分行栏目管理
if (typeof Global === "undefined") {
    Global = {};
}
//文章的栏目(根据登陆人不同得到不同部门对应的栏目)
Global.newsDocDirLoader = new Ext.tree.TreeLoader({
    iconCls: 'disco-tree-node-icon',
    gridSelModel: 'checkbox',
    varName: "Global.BRANCH_DIR_LOADER",//缓存Key
    url: "newsDir.java?cmd=getNewsDocDirTree&pageSize=-1&treeData=true&all=false",
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
/**
 * 已发表的文章
 * 当前登陆人已发表的文章或如果当前登陆人是部门、支行、分行员可看到本部门下所有撰稿人已发表的文章
 * @class AfterOpenDocGridPanel
 * @extends Disco.Ext.CrudPanel
 */
AfterOpenDocGridPanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "afterOpenDocGridPanel",
    baseUrl: "newsDoc.java",
    autoLoadGridData: true,
    autoScroll: false,
    totalPhoto: 0,
    pageSize: 20,
    showAdd: false,
    showRemove: false,
    showView: false,
    searchWin: {
        width: 630,
        height: 195,
        title: "高级查询"
    },
    defaultsActions: {
        create: 'save',
        list: 'getAfterOpenDoc',
        view: 'view',
        update: 'update',
        remove: 'remove'
    },
    gridViewConfig: {
        forceFit: true,
        enableRowBody: true,
        showPreview: true
    },
    //记录用户的上次操作记录
    tempCookie: {
        dir: null
    },
    searchFormPanel: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 80,
            labelAlign: "right",
            items: [{
                xtype: "fieldset",
                title: "查询条件",
                height: 115,
                layout: 'column',
                items: [{
                    columnWidth: .50,
                    layout: 'form',
                    defaultType: 'textfield',
                    items: [{
                        fieldLabel: "文章标题",
                        name: "title",
                        anchor: '90%'
                    }, {
                        fieldLabel: "文章作者",
                        name: "author",
                        anchor: '90%'
                    }, {
                        fieldLabel: "撰稿日期(始)",
                        name: "createStartTime",
                        anchor: '90%',
                        xtype: 'datefield',
                        format: 'Y-m-d'
                    }]
                }, {
                    columnWidth: .50,
                    layout: 'form',
                    defaultType: 'textfield',
                    defaults: {
                        width: 130
                    },
                    items: [{
                        anchor: '90%',
                        fieldLabel: '发布到首页',
                        name: 'isTop',
                        xtype: 'radiogroup',
                        items: [{
                            boxLabel: '是',
                            name: 'isTop',
                            inputValue: 1
                        }, {
                            boxLabel: '否',
                            name: 'isTop',
                            inputValue: 0
                        }]
                    }, {
                        fieldLabel: "文章来源",
                        name: "source",
                        anchor: '90%'
                    }, {
                        fieldLabel: "撰稿日期(末)",
                        name: "createEndTime",
                        anchor: '90%',
                        xtype: 'datefield',
                        format: 'Y-m-d'
                    }]
                }]
            }]
        });
        return formPanel;
    },
    edit: function() {
        var me = this;
        if (!this.editWin) {
            Disco.Ext.Util.listObject('NewsDocEditManagePanel', function(obj) {
                this.editWin = obj;
                this.editWin.show();
                this.editWin.refurbishParentGrid = function() {
                    //保存之后刷新grid
                    me.grid.store.reload();
                };
                this.initWinFormData(this.editWin);
            }.createDelegate(this), 'cms/doc/NewsDocEditManagePanel.js')
        } else {
            this.editWin.show();
            this.initWinFormData(this.editWin);
        }
    },
    moveNewsDocToDir: function() {
        var record = this.grid.getSelectionModel().getSelected();
        if (!record) {
            this.alert("请先选择要操作的数据！", "提示信息");
            return false;
        }
        if (!this.viewWinPanel) {
            this.viewWinPanel = this.createViewWinPanel();
        }
        this.viewWinPanel.show();
        var form = this.viewWinPanel.getComponent(0).form;
        form.clearData();
        form.reset();
        this.onView(form, record.data);
        form.findField('branchDir').setValue({
            id: record.get('branchDir').id,
            title: record.get('branchDir').name
        })
    },
    createViewWinPanel: function() {
        var win = new Ext.Window({
            width: 300,
            height: 110,
            modal: true,
            closeAction: 'hide',
            buttonAlign: "center",
            title: "选择栏目后发表",
            buttons: [{
                text: "发表(<u>K</u>)",
                handler: function() {
                    var me = this;
                    var form = me.viewWinPanel.getComponent(0).form;
                    if (!form.isValid()) {
                        Ext.Msg.alert('提醒', '请先选择一个栏目后再发表');
                        return;
                    };
                    var vals = me.getViewFormObj();
                    me.executeUrl('newsDoc.java', {
                        cmd: 'moveNewsDocToDir',
                        mulitId: vals.mulitId,
                        branchDirId: vals.branchDirId
                    }, function() {
                        me.grid.store.reload()
                        me.viewWinPanel.hide();
                    })();
                },
                iconCls: 'save',
                scope: this
            }]
        });
        win.add(this.createForm());
        return win;
    },
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 70,
            fileUpload: true,
            autoScroll: true,
            labelAlign: 'right',
            defaults: {
                xtype: 'textfield'
            },
            items: [{
                name: "id",
                xtype: "hidden"
            }, {
                name: 'branchDir',
                fieldLabel: '文章栏目1',
                xtype: "treecombo",
                valueField: "id",
                allowBlank: false,
                blankText: '文章栏目不能为空',
                hiddenName: "branchDirId",
                displayField: "title",
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
            }]
        });
        return formPanel;
    },
    getViewFormObj: function() {
        var mulitId = "";
        var rs = this.grid.getSelectionModel().getSelections();
        for (var i = 0; i < rs.length; i++) {
            mulitId += rs[i].get("id") + ",";
        }
        var form = this.viewWinPanel.getComponent(0).form;
        var obj = {};
        obj.mulitId = mulitId;
        obj.branchDirId = form.findField('branchDir').getValue();
        return obj;
    },
    initWinFormData: function(_this) {
        var record = this.grid.getSelectionModel().getSelected();
        if (!record) {
            return Ext.Msg.alert("提醒", "请选择需编辑的数据");
        }
        Ext.Ajax.request({
            scope: this,
            url: this.baseUrl + "?cmd=getContent&id=" + record.get('id'),
            success: function(response) {
                var data = Ext.decode(response.responseText);
                data != null || (data = "");
                _this.editor.html(data);
                _this.editor.sync();
            }
        });
        //编辑回显时如果有父级栏目及回显数据
        var dirObj = record.get('dir');
        var newsDir = _this.fp.form.findField('dir');
        if (dirObj) {
            dirObj.title || (dirObj.title = dirObj.name);
            newsDir.setOriginalValue(dirObj);
        }
        var branchDirObj = record.get('branchDir');
        var branchDir = _this.fp.form.findField('branchDir');
        if (branchDirObj) {
            branchDirObj.title || (branchDirObj.title = branchDirObj.name);
            branchDir.setOriginalValue(branchDirObj);
        }
        _this.fp.form.loadRecord(record);
    },
    topicRender: function(value, p, record) {
        return String.format('{1}<b><a style="color:green" href="news.java?cmd=show&id={0}" target="_blank">&nbsp查看</a></b><br/>', record.id, value)
    },
    stateRender: function(v) {
        return "已发表";
    },
    toggleDetails: function(btn, pressed) {
        var view = this.grid.getView();
        view.showPreview = pressed;
        view.refresh();
    },
    createWin: function() {
        return this.initWin(900, 600, "文章管理");
    },
    storeMapping: ["id", "isTop", "title", "keywords", 'iconPath', "createDate", "author", "source", "putDate", "dir", "toBranch", "branchDir", "state", "branchState", {
        name: "branchDirId",
        mapping: "branchDir"
    }, {
        name: "dirId",
        mapping: "dir"
    }],
    initComponent: function() {
        var user = Global.getCurrentUser;
        //如果当前登陆用户是分行管理员则显示发布按钮反之为送审
        if (user.types == 2 || user.types == 5) {
            this.showRemove = true;
            this.batchRemoveMode = true;
        }
        this.cm = new Ext.grid.ColumnModel([{
            header: "主题",
            dataIndex: 'title',
            width: 200,
            renderer: this.topicRender
        }, {
            header: "栏目1",
            sortable: true,
            width: 70,
            dataIndex: "dir",
            renderer: this.objectRender("name")
        }, {
            width: 70,
            sortable: true,
            header: "发布到分行",
            dataIndex: "toBranch",
            renderer: this.booleanRender
        }, {
            width: 70,
            sortable: true,
            header: "分行栏目",
            dataIndex: "branchDir",
            renderer: this.objectRender("name")
        }, {
            header: "作者",
            sortable: true,
            width: 50,
            dataIndex: "author"
        }, {
            header: "发表至首页",
            sortable: true,
            width: 70,
            align: 'center',
            dataIndex: "isTop",
            renderer: this.booleanRender
        }, {
            header: "撰稿日期",
            sortable: true,
            width: 90,
            dataIndex: "createDate",
            renderer: this.dateRender()
        }, {
            width: 60,
            sortable: true,
            header: "流程状态",
            dataIndex: "state",
            renderer: this.stateRender
        }]);
        var user = Global.getCurrentUser;
        //分行管理员
        if (user.branchAdmin) {
            this.cm.setHidden(1, true);
        };
        if (user.types == 2 || user.types == 5) {
            this.gridButtons = [{
                text: '发表至首页',
                cls: "x-btn-text-icon",
                icon: "img/core/up.gif",
                handler: this.executeCmd("setTop")
            }, {
                text: '批量迁移至指定栏目',
                cls: "x-btn-text-icon",
                icon: "img/core/up.gif",
                handler: this.moveNewsDocToDir
            }]
        }
        AfterOpenDocGridPanel.superclass.initComponent.call(this);
    }
});
AfterOpenDocManagePanel = Ext.extendX(Disco.Ext.TreeCascadePanel, function(superclass) {
    return {
        initComponent: function() {
            AfterOpenDocManagePanel.superclass.initComponent.call(this);
        },
        treeCfg: {
            //rootVisible: false,
            title: "文章栏目信息",
            rootText: '所有栏目',
            expanded: true,
            autoScroll: true,
            rootIconCls: 'treeroot-icon',
            loader: Global.newsDocDirLoader
        },
        onTreeClick: function(node) {
            var id = (node.id != 'root' ? node.id : "");
            this.list.store.baseParams.dirId = id;
            superclass.onTreeClick.apply(this, arguments);
        },
        getPanel: function() {
            if (!this.panel) {
                this.panel = new AfterOpenDocGridPanel();
                this.panel.tree = this.tree;
                this.list = this.panel.grid;
            }
            return this.panel;
        }
    }
});