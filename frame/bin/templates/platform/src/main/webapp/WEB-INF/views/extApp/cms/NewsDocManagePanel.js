//分行栏目管理
if (typeof Global === "undefined") {
    Global = {};
}
//文章的栏目
Global.newsDocDirLoader = new Ext.tree.TreeLoader({
    iconCls: 'lanyo-tree-node-icon',
    gridSelModel: 'checkbox',
    varName: "Global.newsDocDirLoader",//缓存Key
    url: "newsDir.java?cmd=getNewsDirTree&pageSize=-1&treeData=true&all=true",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});
//文章模板
Global.templateFileDocLoader = new Disco.Ext.MemoryTreeLoader({
    iconCls: 'lanyo-tree-node-icon',
    varName: "Global.templateFileDocLoader",//缓存Key
    url: "templateFile.java?cmd=getTemplateFile&sn=doc_tpl&pageSize=-1&treeData=true",
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
 * @class NewsDocGridPanel
 * @extends Disco.Ext.CrudPanel
 */
NewsDocGridPanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "newsDocGridPanel",
    baseUrl: "newsDoc.java",
    fileUpload: true,
    autoLoadGridData: true,
    autoScroll: false,
    totalPhoto: 0,
    pageSize: 20,
    searchWin: {
        width: 630,
        height: 195,
        title: "高级查询"
    },
    gridViewConfig: {
        forceFit: true,
        enableRowBody: true,
        showPreview: true
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
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 70,
            autoScroll: true,
            fileUpload: true,
            labelAlign: 'right',
            defaults: {
                xtype: 'textfield'
            },
            items: [{
                name: "id",
                xtype: "hidden"
            }, Disco.Ext.Util.columnPanelBuild({
                columnWidth: .70,
                items: {
                    width: 680,
                    name: "title",
                    emptyText: '文章标题',
                    fieldLabel: "文章标题",
                    allowBlank: false,
                    blankText: '文章标题不能为空'
                }
            }, {
                columnWidth: .30,
                items: {
                    fieldLabel: '支持评论',
                    xtype: 'radiogroup',
                    name: 'reviewEnbaled',
                    items: [{
                        boxLabel: '可评论',
                        name: 'reviewEnbaled',
                        inputValue: 1
                    }, {
                        checked: true,
                        boxLabel: '不可评论',
                        name: 'reviewEnbaled',
                        inputValue: 0
                    }]
                }
            }), Disco.Ext.Util.columnPanelBuild({
                columnWidth: .30,
                items: {
                    name: 'dir',
                    anchor: '100%',
                    allowBlank: false,
                    blankText: '文章栏目不能为空',
                    fieldLabel: '文章栏目',
                    xtype: "treecombo",
                    valueField: "id",
                    hiddenName: "dirId",
                    displayField: "name",
                    tree: new Ext.tree.TreePanel({
                        //rootVisible: false,
                        autoScroll: true,
                        root: new Ext.tree.AsyncTreeNode({
                            id: "root",
                            text: "所有栏目",
                            expanded: true,
                            iconCls: 'treeroot-icon',
                            listeners: {
                                "expand": function(node) {
                                    node.getOwnerTree().expandAll();
                                },
                                scope: this
                            },
                            loader: Global.newsDocDirLoader
                        })
                    })
                }
            }, {
                columnWidth: .27,
                items: {
                    name: "sequence",
                    fieldLabel: "排序编号"
                }
            }, {
                columnWidth: .43,
                items: {
                    name: "url",
                    fieldLabel: "外链地址"
                }
            }), Disco.Ext.Util.columnPanelBuild({
                columnWidth: .30,
                items: {
                    xtype: "treecombo",
                    fieldLabel: "文章模板",
                    name: "tplFile",
                    anchor: '100%',
                    hiddenName: "tplFileId",
                    displayField: "name",
                    valueField: "id",
                    tree: new Ext.tree.TreePanel({
                        autoScroll: true,
                        root: new Ext.tree.AsyncTreeNode({
                            id: "root",
                            text: "根节点",
                            iconCls: 'lanyo-tree-node-icon',
                            expanded: true,
                            listeners: {
                                "expand": function(node) {
                                    node.getOwnerTree().expandAll();
                                },
                                scope: this
                            },
                            loader: Global.templateFileDocLoader
                        })
                    })
                }
            }, {
                columnWidth: .27,
                items: {
                    fieldLabel: '是否置顶',
                    xtype: 'radiogroup',
                    name: 'isTop',
                    items: [{
                        boxLabel: '置顶',
                        name: 'isTop',
                        inputValue: 1
                    }, {
                        checked: true,
                        boxLabel: '不置顶',
                        name: 'isTop',
                        inputValue: 0
                    }]
                }
            }, {
                columnWidth: .43,
                items: {
                    fieldLabel: '文章状态',
                    xtype: 'radiogroup',
                    name: 'status',
                    items: [{
                        checked: true,
                        boxLabel: '已发布',
                        name: 'status',
                        inputValue: 2
                    }, {
                        checked: true,
                        boxLabel: '待审核',
                        name: 'status',
                        inputValue: 1
                    }, {
                        boxLabel: '锁定',
                        name: 'status',
                        inputValue: 0
                    }, {
                        boxLabel: '回收站',
                        name: 'status',
                        inputValue: -1
                    }]
                }
            }), Disco.Ext.Util.columnPanelBuild({
                columnWidth: 1,
                items: {
                    width: 680,
                    name: 'iconPath',
                    buttonText: '选择',
                    fieldLabel: '文章图标',
                    xtype: "fileuploadfield"
                }
            }), {
                width: 780,
                height: 372,
                margins: {
                    left: 10
                },
                hideLabel: true,
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
                            var id = me.fp.form.findField('content').id;
                            me.editor = KindEditor.create('#' + id, {
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
    create: function() {
        NewsDocGridPanel.superclass.create.call(this);
        if (this.editor) {
            this.editor.html('');
            this.editor.sync();
        }
    },
    edit: function() {
        var win = NewsDocGridPanel.superclass.edit.call(this);
        if (win) {
            var record = this.grid.getSelectionModel().getSelected();
            Ext.Ajax.request({
                scope: this,
                url: this.baseUrl + "?cmd=getContent&id=" + record.get("id"),
                success: function(response) {
                    var data = Ext.decode(response.responseText);
                    data != null || (data = "");
                    this.editor.html(data);
                    this.editor.sync();
                }
            });
        }
    },
    topicRender: function(value, p, record) {
        return String.format('{1}<b><a style="color:green" href="news.java?cmd=show&id={0}" target="_blank">&nbsp查看</a></b><br/>', record.id, value)
    },
    statusRender: function(v) {
        if (v == 2) {
            return "<span style='color:green;'>已发布</span>";
        } else if (v == 1) {
            return "<span style='color:blue;'>待审核</span>";
        } else if (v == 0) {
            return "<span style='color:green;'>已锁定</span>";
        } else if (v == -1) {
            return "<span style='color:red;'>回收站</span>";
        } else {
            return "<span style='color:wheat;'>未知状态</span>";
        }

    },
    toggleDetails: function(btn, pressed) {
        var view = this.grid.getView();
        view.showPreview = pressed;
        view.refresh();
    },
    createWin: function() {
        return this.initWin(800, 570, "文章管理");
    },
    storeMapping: ["id", "title", 'iconPath', "tplFile", "dir", "reviewTimes", "status", "isTop", "url", "sequence", "createUser", "reviewEnbaled", "createDate", "count", {
        name: "dirId",
        mapping: "dir"
    }, {
        name: "tplFileId",
        mapping: "tplFile"
    }],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "主题",
            dataIndex: 'title',
            width: 160,
            renderer: this.topicRender
        }, {
            header: "栏目",
            sortable: true,
            width: 70,
            align: 'center',
            dataIndex: "dir",
            renderer: this.objectRender("name")
        }, {
            width: 50,
            sortable: true,
            header: "排序号",
            align: 'center',
            dataIndex: "sequence"
        }, {
            header: "是否置顶",
            sortable: true,
            width: 60,
            align: 'center',
            dataIndex: "isTop",
            renderer: this.booleanRender
        }, {
            header: "是否可评",
            sortable: true,
            width: 60,
            align: 'center',
            dataIndex: "reviewEnbaled",
            renderer: this.booleanRender
        }, {
            header: "评论数",
            sortable: true,
            width: 50,
            align: 'center',
            dataIndex: "reviewTimes"
        }, {
            header: "创建者",
            sortable: true,
            width: 60,
            align: 'center',
            dataIndex: "createUser",
            renderer: this.objectRender("trueName")
        }, {
            header: "创建日期",
            sortable: true,
            width: 90,
            align: 'center',
            dataIndex: "createDate",
            renderer: this.dateRender()
        }, {
            width: 60,
            align: 'center',
            sortable: true,
            header: "文章状态",
            dataIndex: "status",
            renderer: this.statusRender
        }]);
        NewsDocGridPanel.superclass.initComponent.call(this);
    }
});
NewsDocManagePanel = Ext.extendX(Disco.Ext.TreeCascadePanel, function(superclass) {
    return {
        initComponent: function() {
            NewsDocManagePanel.superclass.initComponent.call(this);
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
                this.panel = new NewsDocGridPanel();
                this.panel.tree = this.tree;
                this.list = this.panel.grid;
            }
            return this.panel;
        }
    }
});