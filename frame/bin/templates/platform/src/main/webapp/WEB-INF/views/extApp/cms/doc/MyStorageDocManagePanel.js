if (typeof Global === "undefined") {
    Global = {};
}
//当前登陆人所在机构的栏目树
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
 * 文档暂存箱
 * @class MyStorageDocManagePanel
 * @extends Disco.Ext.CrudPanel
 */
MyStorageDocManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "myStorageDocManagePanel",
    baseUrl: "newsDoc.java",
    autoLoadGridData: true,
    autoScroll: false,
    totalPhoto: 0,
    pageSize: 20,
    showAdd: false,
    showView: false,
    batchRemoveMode: true,
    defaultsActions: {
        create: 'save',
        list: 'getMyStorage',
        view: 'view',
        update: 'update',
        remove: 'remove'
    },
    baseQueryParameter: {
        //查询暂存文档
        state: 4
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
    view: function() {
        var record = this.grid.getSelectionModel().getSelected();
        if (!record)
            return Ext.Msg.alert("$!{lang.get('Prompt')}", "$!{lang.get('Select first')}");
        window.open("news.java?cmd=show&id=" + record.get("id"));
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
                fieldLabel: '文章栏目',
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
    topicRender: function(value, p, record) {
        return String.format('{1}<b><a style="color:green" href="news.java?cmd=show&id={0}" target="_blank">&nbsp查看</a></b><br/>', record.id, value)
    },
    stateRender: function(v) {
        if (v == 0) {
            return "逻辑删除";
        } else if (v == 1) {
            return "发表";
        } else if (v == 2) {
            return "退回";
        } else if (v == 3) {
            return "待审核";
        } else if (v == 4) {
            return "暂存";
        } else {
            return "未知状态";
        }
    },
    toggleDetails: function(btn, pressed) {
        var view = this.grid.getView();
        view.showPreview = pressed;
        view.refresh();
    },
    storeMapping: ["id", "title", "keywords", 'iconPath', "createDate", "author", "source", "putDate", "dir", "toBranch", "branchDir", "state", "branchState", {
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
            header: "当前状态",
            dataIndex: "state",
            renderer: this.stateRender
        }]);
        var user = Global.getCurrentUser;
        if (user.branchAdmin) {
            this.gridButtons = [{
                text: '批量发布',
                cls: "x-btn-text-icon",
                icon: "img/core/up.gif",
                handler: this.executeCmd("batchPublish")
            }, {
                text: '批量发布到指定栏目',
                cls: "x-btn-text-icon",
                icon: "img/core/up.gif",
                handler: this.moveNewsDocToDir
            }]
        }
        MyStorageDocManagePanel.superclass.initComponent.call(this);
    }
});