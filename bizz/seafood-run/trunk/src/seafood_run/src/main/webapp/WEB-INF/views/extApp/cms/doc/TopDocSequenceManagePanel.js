// 分行栏目管理
if (typeof Global === "undefined") {
    Global = {};
}
// 文章的栏目(根据登陆人不同得到不同部门对应的栏目)
/*
Global.newsDocDirLoader = new Disco.Ext.MemoryTreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.MY_SEND_DOC_DIR_LOADER",// 缓存Key
    url: "newsDir.java?cmd=getNewDirTree&pageSize=-1&treeData=true&all=true",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});*/
Global.newsDocDirLoader = new Ext.tree.MemoryTreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.DEPT_DIR_LOADER",// 缓存Key
    url: "newsDir.java?cmd=getNewDirTree&pageSize=-1&treeData=true&all=false",
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
 * 已发表的文章 当前登陆人已发表的文章或如果当前登陆人是部门、支行、分行员可看到本部门下所有撰稿人已发表的文章
 * 
 * @class TopDocSequenceListPanel
 * @extends Disco.Ext.CrudPanel
 */
TopDocSequenceListPanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "TopDocSequenceListPanel",
    baseUrl: "newsDoc.java",
    autoLoadGridData: true,
    autoScroll: false,
    totalPhoto: 0,
    pageSize: 20,
    showAdd: true,
    showEdit: true,
    showRemove: true,
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
    // 记录用户的上次操作记录
    tempCookie: {
        dir: null
    },
    edit: function() {
        var win = TopDocSequenceListPanel.superclass.edit.call(this);
        if (win) {

            this.initWinFormData(this);
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
         var record = this.grid.getSelectionModel().getSelected();

            var parentObj = record.get('dir');
            var dir = this.fp.form.findField('dir');
            // 编辑回显时如果有父级栏目及回显数据
            if (parentObj) {
                parentObj.title || (parentObj.title = parentObj.name);
                console.log(parentObj)
                console.log(dir)
                dir.setOriginalValue(parentObj);
            }
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
    storeMapping: ["id", "isTop", 'sequence', "title", "keywords", 'iconPath', "dir", "toBranch", "state", {
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
            }, {
                name: 'dir',
                allowBlank: false,
                blankText: '文章栏目不能为空',
                fieldLabel: '文章栏目',
                xtype: "treecombo",
                valueField: "id",
                hiddenName: "dirId",
                displayField: "title",
                tree: new Ext.tree.TreePanel({
                    // rootVisible: false,
                    autoScroll: true,
                    root: new Ext.tree.AsyncTreeNode({
                        id: "root",
                        text: "所有栏目",
                        expanded: true,
                        iconCls: 'treeroot-icon',
                        loader: Global.newsDocDirLoader
                    }),
                    listeners: {
                        scope: this,
                        click: function(node) {
                            this.tempCookie.dir = node;
                        }
                    }
                })

            }, {
                width: 880,
                height: 395,
                margins: {
                    left: 10
                },
                hideLabel: true,
                // id: 'content1',
                name: 'content',
                xtype: 'textarea'
            }],
            listeners: {
                scope: this,
                'render': function() {
                    var me = this;
                    setTimeout(function() {
                        // 动态加载kindeditor富文本编辑器
                        var plugins = '/plugins/editor/kindeditor/kindeditor-min.js';
                        Disco.loadJs(plugins, false, function() {
                            KindEditor.basePath = '/plugins/editor/kindeditor/';
                            var id = me.fp.form.findField('content').id;
                            me.editor = KindEditor.create('#' + id, {
                                // 所有的上传文件POST地址
                                uploadJson: '/fileUpload.java',
                                // 指定浏览远程图片的URL处理地址
                                fileManagerJson: '/fileUpload.java?cmd=showByKindeditor',
                                // true时显示浏览远程服务器按钮
                                // allowFileManager: true,
                                afterBlur: function() {
                                    this.sync();// 编辑器失去焦点时同步KindEditor的值到textarea文本框
                                }
                            });
                        }, null, Disco.ajaxCache);
                    }, 1);
                }
            }
        });
        var fp = formPanel.getForm();
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

        this.showRemove = true;
        this.batchRemoveMode = true;
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
        }]);
        this.cm.setHidden(1, false);
        this.gridButtons = [{
            text: '生成静态文件',
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
            // _this.expandAll();
        },
        treeCfg: {
            // rootVisible: false,
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
