//分行栏目管理
if (typeof Global === "undefined") {
    Global = {};
}
//文章的栏目(根据登陆人不同得到不同部门对应的栏目)
Global.newsDocDirLoader = new Ext.tree.TreeLoader({
    iconCls: 'disco-tree-node-icon',
    gridSelModel: 'checkbox',
    varName: "Global.BRANCH_DIR_LOADER",//缓存Key
    url: "newsDir.java?cmd=getNewsDocDirTree&pageSize=-1&treeData=true&all=false&showDesk=1",
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
 * @class TopDocSequenceListPanel
 * @extends Disco.Ext.CrudPanel
 */
TopDocSequenceListPanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "topDocSequenceListPanel",
    baseUrl: "newsDoc.java",
    autoLoadGridData: true,
    autoScroll: false,
    totalPhoto: 0,
    pageSize: 20,
    showAdd: false,
    showEdit: false,
    showRemove: false,
    showRemove: false,
    showView: false,
    defaultsActions: {
        create: 'save',
        list: 'getTopDoc',
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
    edit: function() {
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
    storeMapping: ["id", "isTop", 'sequence', "title", "keywords", 'iconPath', "createDate", "author", "source", "putDate", "dir", "toBranch", "branchDir", "state", "branchState", {
        name: "branchDirId",
        mapping: "branchDir"
    }, {
        name: "dirId",
        mapping: "dir"
    }],
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
                name: 'sequence',
                allowBlank: false,
                xtype: "textfield",
                fieldLabel: '排序号',
                blankText: '排序号不能为空'
            }]
        });
        return formPanel;
    },
    createViewWinPanel: function() {
        var win = new Ext.Window({
            width: 300,
            height: 110,
            modal: true,
            closeAction: 'hide',
            buttonAlign: "center",
            title: "设置首页文章的排序号",
            buttons: [{
                text: "设置(<u>K</u>)",
                handler: function() {
                    var me = this;
                    var form = me.viewWinPanel.getComponent(0).form;
                    if (!form.isValid()) {
                        Ext.Msg.alert('提醒', '请先选择一个栏目后再发表');
                        return;
                    };
                    me.executeUrl('newsDoc.java', {
                        cmd: 'saveDocSequence',
                        id: form.findField('id').getValue(),
                        sequence: form.findField('sequence').getValue()
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
    settingSequence: function() {
        var record = this.grid.getSelectionModel().getSelected();
        if (!record) {
            this.alert("请先选择要操作的数据1！", "提示信息");
            return false;
        }
        if (!this.viewWinPanel) {
            this.viewWinPanel = this.createViewWinPanel();
        }
        this.viewWinPanel.show();
        var form = this.viewWinPanel.getComponent(0).form;
        form.clearData();
        form.reset();
        form.loadRecord(record)
    },
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
            header: "分行栏目",
            dataIndex: "branchDir",
            renderer: this.objectRender("name")
        }, {
            header: "排序号",
            sortable: true,
            width: 70,
            align: 'center',
            dataIndex: "sequence"
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
        }]);
        var user = Global.getCurrentUser;
        //分行管理员
        if (user.branchAdmin) {
            this.cm.setHidden(1, true);
        };
        this.gridButtons = [{
            text: '设置排序号',
            cls: "x-btn-text-icon",
            icon: "img/core/up.gif",
            handler: this.settingSequence
        }]
        TopDocSequenceListPanel.superclass.initComponent.call(this);
    }
});
TopDocSequenceManagePanel = Ext.extendX(Disco.Ext.TreeCascadePanel, function(superclass) {
    return {
        initComponent: function() {
            TopDocSequenceManagePanel.superclass.initComponent.call(this);
            this.tree.on({
                scope: this,
                render: this.onTreeRender
            });
        },
        onTreeRender: function(_this) {
            //_this.expandAll();
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
                this.panel = new TopDocSequenceListPanel();
                this.panel.tree = this.tree;
                this.list = this.panel.grid;
            }
            return this.panel;
        }
    }
});