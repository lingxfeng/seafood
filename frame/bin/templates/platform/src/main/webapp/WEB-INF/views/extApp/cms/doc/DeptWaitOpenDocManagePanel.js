//当前登陆人所在机构的栏目树
if (!Global.mySendDocOrgDirLoader) {
    Global.mySendDocOrgDirLoader = new Disco.Ext.MemoryTreeLoader({
        iconCls: 'disco-tree-node-icon',
        varName: "Global.MY_SEND_DOC_ORG_LOADER",//缓存Key
        url: "newsDir.java?cmd=getDirTreeByCurrentUserOrg&pageSize=-1&treeData=true&all=false&orgdir=true",
        listeners: {
            'beforeload': function(treeLoader, node) {
                treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
                if (typeof node.attributes.checked !== "undefined") {
                    treeLoader.baseParams.checked = false;
                }
            }
        }
    });
}
/**
 * 本机构待发表的文档
 * @class DeptWaitOpenDocManagePanel
 * @extends Disco.Ext.CrudPanel
 */
DeptWaitOpenDocManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "deptWaitOpenDocManagePanel",
    baseUrl: "newsDoc.java",
    autoLoadGridData: true,
    autoScroll: false,
    totalPhoto: 0,
    pageSize: 20,
    showAdd: false,
    showRemove: false,
    showEdit: false,
    defaultsActions: {
        create: 'save',
        list: 'getAfterAuditing',
        view: 'view',
        update: 'update',
        remove: 'remove'
    },
    edit: function() {
    },
    view: function() {
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
        form.loadRecord(record);
    },
    /**
     * 查看面板渲染之后执行
     * @param {} win
     * @param {} data
     */
    onView: function(form, data) {
        var dirObj = data.dir;
        var dir = form.findField('dir');
        if (dirObj) {
            dirObj.title || (dirObj.title = dirObj.name);
            dir.setOriginalValue(dirObj);
        }
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
                    me.executeUrl('newsDoc.java', {
                        id: me.getViewFormObj().id,
                        cmd: 'submitAndOpenDoc',
                        dirId: me.getViewFormObj().dirId
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
                name: 'dir',
                fieldLabel: '文章栏目',
                xtype: "treecombo",
                valueField: "id",
                hiddenName: "dirId",
                displayField: "title",
                tree: new Ext.tree.TreePanel({
                    rootVisible: false,
                    autoScroll: true,
                    root: new Ext.tree.AsyncTreeNode({
                        id: "root",
                        text: "所有栏目",
                        expanded: true,
                        iconCls: 'treeroot-icon',
                        loader: Global.mySendDocOrgDirLoader
                    })
                })
            }]
        });
        return formPanel;
    },
    getViewFormObj: function() {
        var form = this.viewWinPanel.getComponent(0).form;
        var obj = {};
        obj.id = form.findField('id').getValue();
        obj.dirId = form.findField('dir').getValue();
        return obj;
    },
    gridViewConfig: {
        forceFit: true,
        enableRowBody: true,
        showPreview: true
    },
    topicRender: function(value, p, record) {
        return String.format('{1}<b><a style="color:green" href="news.java?cmd=show&id={0}" target="_blank">&nbsp查看</a></b><br/>', record.id, value)
    },
    stateRender: function(v) {
        if (v == 6) {
            return "审核通过待发表";
        } else {
            return "未知状态";
        }
    },
    adviceRender: function(v) {
        if (v) {
            if (v.length > 5) {
                return '<div ext:qtitle=""ext:qtip="' + v + '">' + v.substring(0, 4) + "..." + '</div>';
            } else {
                return v;
            }
        } else {
            return "无意见"
        }
    },

    toggleDetails: function(btn, pressed) {
        var view = this.grid.getView();
        view.showPreview = pressed;
        view.refresh();
    },
    createWin: function() {
        return this.initWin(900, 600, "文章管理");
    },
    storeMapping: ["id", "title", "keywords", "advice", "createDate", "author", "source", "putDate", "dir", "toBranch", "branchDir", "state", "branchState", {
        name: "branchDirId",
        mapping: "branchDir"
    }, {
        name: "dirId",
        mapping: "dir"
    }],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "主题",
            dataIndex: 'title',
            width: 200,
            renderer: this.topicRender
        }, {
            header: "栏目",
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
        }, {
            width: 60,
            sortable: true,
            header: "审核意见",
            dataIndex: "advice",
            renderer: this.adviceRender
        }]);
        DeptWaitOpenDocManagePanel.superclass.initComponent.call(this);
    }
});