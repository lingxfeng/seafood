if (typeof Global === "undefined") {
    Global = {};
}
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
//分行栏目树
if (!Global.mySendDocBranchDirLoader) {
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
}
/**
 * 待审核中的文章
 * 部门支行、或分行管理员审核人看的 可以 审核、回退  状态为：待审核、审核中
 * @class WaitAuditingDocManagePanel
 * @extends Disco.Ext.CrudPanel
 */
WaitAuditingDocManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "waitAuditingDocManagePanel",
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
        list: 'getMyWaitAuditing',
        view: 'view',
        update: 'update',
        remove: 'remove'
    },
    //不需编辑
    edit: function() {
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
        if (v == 2) {
            return "审核中";
        } else if (v == 3) {
            return "待审核";
        } else {
            return "待审核";
        }
    },
    toggleDetails: function(btn, pressed) {
        var view = this.grid.getView();
        view.showPreview = pressed;
        view.refresh();
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
        this.changeDocState(record.data);
        form.clearDirty();
    },
    getViewFormId: function() {
        var form = this.viewWinPanel.getComponent(0).form;
        return form.findField('id').getValue();
    },
    /**
     * 审核人打开查看面板之后，把文档状态改为"审核中"
     */
    changeDocState: function(data) {
        if (data.state == 2 || data.branchState == 2)
            return;
        Ext.Ajax.request({
            method: 'POST',
            url: 'newsDoc.java?cmd=changeDocState&id=' + data.id,
            success: function(response) {
                var r = Ext.decode(response.responseText);
                if (!r.success)
                    Disco_RIA.ajaxErrorHandler(r);
                else {
                    this.grid.store.reload();
                }
            },
            scope: this
        });
    },
    createViewWinPanel: function() {
        var win = new Ext.Window({
            width: 900,
            height: 620,
            closeAction: 'hide',
            buttonAlign: "center",
            title: "查看文档详情",
            buttons: [{
                text: "通过审核(<u>K</u>)",
                handler: function() {
                    var me = this;
                    Ext.MessageBox.show({
                        title: '审核意见',
                        width: 300,
                        buttons: Ext.MessageBox.OKCANCEL,
                        multiline: true,
                        fn: function(btn, text) {
                            if ('ok' == btn) {
                                me.executeUrl('newsDoc.java', {
                                    id: me.getViewFormId(),
                                    cmd: 'submitAuditing',
                                    advice: text
                                }, function() {
                                    me.grid.store.reload()
                                    me.viewWinPanel.hide();
                                })();
                            }
                        }
                    });
                },
                iconCls: 'save',
                scope: this
            }, {
                text: "退回(<u>X</u>)",
                iconCls: 'clean',
                handler: function() {
                    var me = this;
                    Ext.MessageBox.show({
                        title: '退回原因',
                        width: 300,
                        buttons: Ext.MessageBox.OKCANCEL,
                        multiline: true,
                        fn: function(btn, text) {
                            if ('ok' == btn) {
                                me.executeUrl('newsDoc.java', {
                                    id: me.getViewFormId(),
                                    cmd: 'backUpDoc',
                                    advice: text
                                }, function() {
                                    me.grid.store.reload()
                                    me.viewWinPanel.hide();
                                })();
                            }
                        }
                    });
                },
                scope: this
            }, {
                text: "关闭(<u>X</u>)",
                iconCls: 'delete',
                handler: function() {
                    this.viewWinPanel.hide();
                },
                scope: this
            }]
        });
        win.add(this.createForm());
        return win;
    },
    /**
     * 查看面板渲染之后执行
     * @param {} win
     * @param {} data
     */
    onView: function(form, data) {
        Ext.Ajax.request({
            scope: this,
            url: this.baseUrl + "?cmd=getContent&id=" + data.id,
            success: function(response) {
                var data = Ext.decode(response.responseText);
                data != null || (data = "");
                this.editor.html(data);
                this.editor.sync();
            }
        });

        //编辑回显时如果有父级栏目及回显数据
        var dirObj = data.dir;
        var newsDir = form.findField('dir');
        if (dirObj) {
            dirObj.title || (dirObj.title = dirObj.name);
            newsDir.setOriginalValue(dirObj);
        }

        var branchDirObj = data.branchDir;
        var branchDir = form.findField('branchDir');
        if (branchDirObj) {
            branchDirObj.title || (branchDirObj.title = branchDirObj.name);
            branchDir.setOriginalValue(branchDirObj);
        }
    },
    /**
     * 查看面板保存按钮的回调
     */
    viewSave: function() {
        alert('点击了保存')
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
                width: 780,
                name: "title",
                emptyText: '文章标题',
                fieldLabel: "文章标题",
                allowBlank: false,
                blankText: '文章标题不能为空'
            }, {
                width: 780,
                name: "keywords",
                emptyText: '文章简短标题在30个字符内',
                fieldLabel: "简短标题"
            }, {
                width: 780,
                name: "iconPath",
                emptyText: '文章对应的头条图片URL地址',
                fieldLabel: "头条图片"
            }, Disco.Ext.Util.columnPanelBuild({
                columnWidth: .3,
                items: {
                    name: "author",
                    fieldLabel: "作者"
                }
            }, {
                columnWidth: .3,
                items: {
                    name: "source",
                    fieldLabel: "来源"
                }
            }, {
                columnWidth: .4,
                items: {
                    name: "putDate",
                    anchor: '93.8%',
                    editable: false,
                    format: 'Y-m-d G:i',
                    xtype: 'datefield',
                    fieldLabel: "发布日期1"
                }
            }), Disco.Ext.Util.columnPanelBuild({
                columnWidth: .3,
                items: {
                    name: 'dir',
                    allowBlank: false,
                    blankText: '文章栏目不能为空',
                    fieldLabel: '文章栏目',
                    xtype: "treecombo",
                    valueField: "id",
                    hiddenName: "dirId",
                    displayField: "title",
                    tree: new Ext.tree.TreePanel({
                        //rootVisible: false,
                        autoScroll: true,
                        root: new Ext.tree.AsyncTreeNode({
                            id: "root",
                            text: "所有栏目",
                            expanded: true,
                            iconCls: 'treeroot-icon',
                            loader: Global.mySendDocOrgDirLoader
                        }),
                        listeners: {
                            scope: this,
                            click: function(node) {
                                this.tempCookie.dir = node;
                            }
                        }
                    })
                }
            }, {
                columnWidth: .3,
                items: {
                    name: "toBranch",
                    xtype: 'checkbox',
                    inputValue: 1,
                    fieldLabel: "发布到分行",
                    listeners: {
                        scope: this,
                        check: function(me, checked) {
                            var form = this.viewWinPanel.getComponent(0).form;
                            var branchDir = form.findField('branchDir');
                            if (checked) {
                                branchDir.setDisabled(false);
                            } else {
                                branchDir.setDisabled(true);
                            }
                        }
                    }
                }
            }, {
                columnWidth: .4,
                items: {
                    //默认在发布到分行没有选择中禁用此项
                    disabled: true,
                    allowBlank: false,
                    fieldLabel: '分行栏目',
                    name: 'branchDir',
                    xtype: "treecombo",
                    hiddenName: "branchDirId",
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
                            loader: Global.mySendDocBranchDirLoader
                        })
                    })
                }
            }), {
                width: 880,
                height: 395,
                margins: {
                    left: 10
                },
                hideLabel: true,
                id: 'content_waitAuditing',
                name: 'content',
                xtype: 'textarea'
            }],
            listeners: {
                scope: this,
                'render': function() {
                    var me = this;
                    setTimeout(function() {
                        //动态加载kindeditor富文本编辑器
                        var plugins = '/plugins/editor/kindeditor/kindeditor-min.js';
                        Disco.loadJs(plugins, false, function() {
                            KindEditor.basePath = '/plugins/editor/kindeditor/';
                            me.editor = KindEditor.create('#content_waitAuditing', {
                                //所有的上传文件POST地址
                                uploadJson: '/fileUpload.java',
                                //指定浏览远程图片的URL处理地址
                                fileManagerJson: '/fileUpload.java?cmd=showByKindeditor',
                                //true时显示浏览远程服务器按钮
                                //allowFileManager: true,
                                afterBlur: function() {
                                    this.sync();//编辑器失去焦点时同步KindEditor的值到textarea文本框
                                }
                            });
                        }, null, Disco.ajaxCache);
                    }, 1);
                }
            }
        });
        return formPanel;
    },
    createWin: function() {
        return this.initWin(900, 620, "文章管理");
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
            header: "流程状态",
            dataIndex: "state",
            renderer: this.stateRender
        }]);
        WaitAuditingDocManagePanel.superclass.initComponent.call(this);
    }
});