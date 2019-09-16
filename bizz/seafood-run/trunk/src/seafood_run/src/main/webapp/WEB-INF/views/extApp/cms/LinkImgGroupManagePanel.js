if (typeof Global === "undefined") {
    Global = {};
}
// 文章的栏目(根据登陆人不同得到不同部门对应的栏目)

Global.pptLoader = new Disco.Ext.MemoryTreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.PPT_LOADER",// 缓存Key
    url: "linkImgType.java?cmd=getPPt",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});

// 首页公告管理
LinkImgGroupManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "linkImgGroupManagePanel",
    // title:"首页公告管理",
    baseUrl: "linkImgGroup.java",
    gridSelModel: 'checkbox',
    batchRemoveMode: true,
    autoLoadGridData: true,
    autoScroll: false,
    totalPhoto: 0,
    pageSize: 20,
    showAdd: true,
    showEdit: true,
    showRemove: true,
    showView: false,
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            fileUpload: true,
            labelWidth: 70,
            labelAlign: 'right',
            defaults: {
                width: 280,
                xtype: "textfield"
            },
            items: [{
                xtype: "hidden",
                name: "id"
            }, {
                fieldLabel: "标题",
                name: "title",
                emptyText: '标题不能为空',
                allowBlank: false,
                blankText: '标题不能为空'
            }, {
                fieldLabel: "链接地址",
                name: "linkUrl",
                emptyText: '链接不能为空',
                allowBlank: false,
                blankText: '链接不能为空'
            }, {
                fieldLabel: "说明",
                name: "text",
                emptyText: '说明不能为空',
                allowBlank: false,
                blankText: '说明不能为空'
            }, {
                fieldLabel: "图片",
                name: "imgPath",
                emptyText: '图片不能为空',
                allowBlank: false,
                buttonText: '选择',
                xtype: "fileuploadfield"
            }, {
                width: 150,
                name: 'type',
                allowBlank: false,
                blankText: '所属图片分类',
                fieldLabel: '所属分类',
                xtype: "treecombo",
                valueField: "id",
                hiddenName: "typeId",
                displayField: "text",
                tree: new Ext.tree.TreePanel({
                    // rootVisible: false,
                    autoScroll: true,
                    root: new Ext.tree.AsyncTreeNode({
                        id: "root",
                        text: "所有图片分类",
                        expanded: true,
                        iconCls: 'treeroot-icon',
                        loader: Global.pptLoader
                    }),
                    listeners: {
                        scope: this,
                        click: function(node) {
                            this.tempCookie.dir = node;
                        }
                    }
                })

            }/*, {
            width: 880,
            height: 395,
            margins: {
             left: 10
            },
            hideLabel: true,
            // id: 'content1',
            name: 'imgJson',
            xtype: 'textarea'
            }*/]
            /*,
                        listeners: {
                        scope: this,
                        'render': function() {
                            var me = this;
                            setTimeout(function() {
                                // 动态加载kindeditor富文本编辑器
                                var plugins = '/plugins/editor/kindeditor/kindeditor-min.js';
                                Disco.loadJs(plugins, false, function() {
                                    KindEditor.basePath = '/plugins/editor/kindeditor/';
                                    var id = me.fp.form.findField('imgJson').id;
                                    me.editor = KindEditor.create('#' + id, {
                                        // 所有的上传文件POST地址
                                        uploadJson: '/fileUpload.java',
                                        // 指定浏览远程图片的URL处理地址
                                        fileManagerJson: '/fileUpload.java?cmd=showByKindeditor',
                                        // true时显示浏览远程服务器按钮
                                        // allowFileManager: true,
                                        items: ['image', 'multiimage', 'source', '|', 'undo', 'redo', '|', 'preview', 'flash', 'media', 'clearhtml', 'about'],
                                        afterBlur: function() {
                                            this.sync();// 编辑器失去焦点时同步KindEditor的值到textarea文本框
                                        }
                                    });
                                    //me.editor.readonly(true);
                                }, null, Disco.ajaxCache);
                            }, 1);
                        }
                        }*/
        });
        return formPanel;
    },
    userRender: function(v) {
        return v ? v.trueName : "$!{lang.get('Unknown')}";
    },
    create: function() {
        LinkImgGroupManagePanel.superclass.create.call(this);
        if (typeof this.editor == "object")
            this.editor.html("");
    },
    createWin: function() {
        return this.initWin(500, 300, "分类管理");
    },

    storeMapping: ["id", "title", "linkUrl", "text", "imgPath", "type","sequence"],
    tempCookie: {
        dir: null
    },
    edit: function() {
        var win = LinkImgGroupManagePanel.superclass.edit.call(this);
        if (win) {
            this.initWinFormData(this);
        }
    },
    initWinFormData: function(_this) {
        var record = this.grid.getSelectionModel().getSelected();
        if (!record) {
            return Ext.Msg.alert("提醒", "请选择需编辑的数据");
        }
        var parentObj = record.get('type');
        console.dir(parentObj)
        var dir = this.fp.form.findField('type');
        // 编辑回显时如果有父级栏目及回显数据
        if (parentObj) {
        	parentObj.text = parentObj.text|| parentObj.title;
            dir.setOriginalValue(parentObj);
        }

        _this.fp.form.loadRecord(record);
    },
    topicRender: function(value, p, record) {
        return String.format('{1}<b><a style="color:green" href="news.java?cmd=show&id={0}" target="_blank">&nbsp查看</a></b><br/>', record.id, value)
    },
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "排序",
            sortable: true,
            width: 100,
            dataIndex: "sequence"
        },{
            header: "标题",
            sortable: true,
            width: 100,
            dataIndex: "title"
        },{
            header: "链接地址",
            sortable: true,
            width: 100,
            dataIndex: "linkUrl"
        }]);
        this.gridButtons = [{
			text : "上移",
			iconCls: "upload-icon",						
			handler : this.swapSequence(""),
			scope : this
		}, {
			text : "下移",
			iconCls: "down",				
			handler : this.swapSequence(true),
			scope : this
		}
		/*, new Ext.Toolbar.Separator(),{text:"重新计算系统菜单项",scope:this,handler:this.reCalMenuAndRoles}*/
		];
        LinkImgGroupManagePanel.superclass.initComponent.call(this);
    }
});

BannerImgManagePanel = Ext.extendX(Disco.Ext.TreeCascadePanel, function(superclass) {
    return {
        initComponent: function() {
            BannerImgManagePanel.superclass.initComponent.call(this);
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
            title: "图片分类信息",
            rootText: '所有分类',
            expanded: true,
            autoScroll: true,
            rootIconCls: 'treeroot-icon',
            loader: Global.pptLoader
        },
        onTreeClick: function(node) {
            var id = (node.id != 'root' ? node.id : "");
            this.list.store.baseParams.typeId = id;
            superclass.onTreeClick.apply(this, arguments);
        },
        getPanel: function() {
            if (!this.panel) {
                this.panel = new LinkImgGroupManagePanel();
                this.panel.tree = this.tree;
                this.list = this.panel.grid;
            }
            return this.panel;
        }
    }
});
