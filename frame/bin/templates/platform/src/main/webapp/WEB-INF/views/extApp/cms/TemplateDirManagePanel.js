if (typeof Global === "undefined") {
    Global = {};
}
if (!Global.templateDirLoader) {
    Global.templateDirLoader = new Ext.tree.TreeLoader({
        url: "templateDir.java?cmd=getTemplateDir&pageSize=-1&treeData=true",
        iconCls: 'lanyo-tree-node-icon',
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
TemplateDirListPanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "templateDirListPanel",
    baseUrl: "templateDir.java",
    pageSize: 15,
    viewCmd: "edit",
    batchRemoveMode: true,// 是否是批量删除模式
    viewWin: {
        title: '查看',
        width: 500,
        height: 278
    },
    statusRender: function(v) {
        if (v == 1)
            return "<span style='color:green;'>正常</span>";
        else if (v == 0)
            return "<span style='color:blue;'>锁定</span>";
        else if (v == -1)
            return "<span style='color:red;'>删除</span>";
        else
            return "<span style='color:red;'>未知</span>";
    },
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 60,
            labelAlign: 'right',
            items: [{
                xtype: "hidden",
                name: "id"
            }, {
                xtype: "fieldset",
                title: "基本信息",
                height: 85,
                layout: "form",
                items: [Disco.Ext.Util.twoColumnPanelBuild({
                    fieldLabel: '分类编号',
                    xtype: "textfield",
                    allowBlank: false,
                    //helpText: "客户的编号如果不填写,默认由系统自动生成一个不可重复的编号",
                    name: 'code'
                }, {
                    allowBlank: false,
                    fieldLabel: '分类名称',
                    xtype: "textfield",
                    name: 'name'
                }, {
                    xtype: "treecombo",
                    fieldLabel: "父级分类",
                    collapsible: true,
                    // allowBlank : false,
                    displayField: "name",
                    name: "parentId",
                    hiddenName: "parentId",
                    width: 100,
                    tree: new Ext.tree.TreePanel({
                        rootVisible: true,
                        root: new Ext.tree.AsyncTreeNode({
                            id: "root",
                            text: "所有目录",
                            expanded: true,
                            listeners: {
                                "expand": function(node) {
                                    node.getOwnerTree().expandAll();
                                },
                                scope: this
                            },
                            iconCls: 'lanyo-tree-node-icon',
                            loader: Global.templateDirLoader
                        }),
                        // 事件监听器，模板分类编辑时不能选择自己属于自己的分类
                        listeners: {
                            beforeclick: function(node, e) {
                                //无分类时节点的id为-1[JAVA生成的id]
                                if (node.id < 0)
                                    return false;

                                //如果没有选择行肯定是新增
                                var record = this.grid.getSelectionModel().getSelected();
                                if (typeof record == "undefined")
                                    return;

                                var id = this.fp.form.findField("id").getValue();//表单id主键
                                var newId = node.id;//当前表单中被选中的分类ID												
                                var oldId = record.get("id");//当前选中行记录分类的id

                                //如果id有值并且表格行被选中则是编辑反之是添加
                                if (id && oldId && newId == oldId) {
                                    //编辑
                                    Ext.Msg.alert("警告", "模板分类的父级分类不能是自身");
                                    return false;
                                }
                            },
                            scope: this
                        }
                    })
                }, Disco.Ext.Util.buildCombox("status", "分类状态", [["正常", 1], ["锁定", 0], ["删除", -1]], 1, false))]
            }, {
                width: 505,
                hideLabel: true,
                xtype: "textarea",
                name: "description"
            }]
        });
        return formPanel;
    },
    create: function() {
        TemplateDirListPanel.superclass.create.call(this);
        this.fp.form.findField("parentId").setValue(this.parent && this.parent.id ? this.parent : null);
    },
    view: function() {
        TemplateDirListPanel.superclass.view.call(this);
        var record = this.grid.getSelectionModel().getSelected();
    },
    save: function(callback, autoClose, ignoreBeforeSave) {
        TemplateDirListPanel.superclass.save.call(this, callback, autoClose, ignoreBeforeSave);
    },
    //保存后执行
    onSave: function(form, action) {
        if (this.tree)
            this.tree.root.reload.defer(1000, this.tree.root);// 刷新左边的树
    },
    edit: function() {
        TemplateDirListPanel.superclass.edit.call(this);
        var record = this.grid.getSelectionModel().getSelected();
    },
    removeCallBack: function() {
        TemplateDirListPanel.superclass.removeCallBack.call(this);
        if (this.tree)
            this.tree.root.reload.defer(100, this.tree.root);// 刷新左边的树
    },
    createWin: function(callback, autoClose) {
        return this.initWin(530, 240, "模板分类管理", callback, autoClose);
    },
    millisecondToDate: function(msd) {
        var time = parseFloat(msd) / 1000;
        if (null != time && "" != time) {
            if (time > 60 && time < 60 * 60) {
                time = parseInt(time / 60.0) + "分钟" + parseInt((parseFloat(time / 60.0) - parseInt(time / 60.0)) * 60) + "秒";
            } else if (time >= 60 * 60 && time < 60 * 60 * 24) {
                time =
                        parseInt(time / 3600.0) + "小时" + parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) + "分钟"
                                + parseInt((parseFloat((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60) - parseInt((parseFloat(time / 3600.0) - parseInt(time / 3600.0)) * 60)) * 60) + "秒";
            } else {
                time = parseInt(time) + "秒";
            }
        }
        return time;
    },
    whileformatTime: function() {
        var _this = this;
        for (var i = 0; i < _this.grid.getStore().getCount(); i++) {
            var newval = _this.grid.getStore().getAt(i).data.sequence;
            _this.grid.getStore().getAt(i).set('sequence', parseInt(newval) + 1000);
        }
    },
    dateRenderer: function(d) {
        var time = d;
        v = this.millisecondToDate(time);
        return v;
    },
    storeMapping: ["id", "code", "name", "description", "sequence", {
        name: "parentId",
        mapping: "parent"
    }, "inputDate", "status"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "分类编号",
            sortable: true,
            width: 80,
            dataIndex: "code"
        }, {
            header: "分类名称",
            sortable: true,
            width: 100,
            dataIndex: "name"
        }, {
            header: "父级分类",
            sortable: true,
            width: 100,
            dataIndex: "parent",
            renderer: this.objectRender("name")
        }, {
            header: "分类简介",
            sortable: true,
            width: 300,
            hidden: true,
            dataIndex: "description"
        }, {
            header: "排序号",
            sortable: true,
            width: 50,
            scope: this,
            dataIndex: "sequence",
            renderer: this.dateRenderer
        }, {
            header: "创建日期",
            sortable: true,
            width: 80,
            dataIndex: "inputDate",
            renderer: this.dateRender("Y-m-d H:i:s")
        }, {
            header: "分类状态",
            sortable: true,
            width: 60,
            dataIndex: "status",
            renderer: this.statusRender
        }]);
        this.gridButtons = [new Ext.Toolbar.Separator, {
            text: "上移",
            cls: "x-btn-text-icon",
            icon: "img/core/up.gif",
            handler: this.swapSequence(""),
            scope: this
        }, new Ext.Toolbar.Separator, {
            text: "下移",
            cls: "x-btn-text-icon",
            icon: "img/core/down.gif",
            handler: this.swapSequence(true),
            scope: this
        }];
        TemplateDirListPanel.superclass.initComponent.call(this);
        this.grid.getStore().on('load', function() {
            var _this = this;
            setInterval(function() {
                _this.whileformatTime();
            }, 1000);
        }, this);
    }
});

TemplateDirManagePanel = function() {
    this.list = new TemplateDirListPanel({
        region: "center",
        layout: 'fit',
        border: true,
        margins: '4 4 4 4'
    });
    this.tree = new Ext.tree.TreePanel({
        title: "模板分类",
        region: "west",
        margins: '4 0 4 0',
        autoScroll: true,
        width: 210,
        tools: [{
            id: "refresh",
            handler: function() {
                this.tree.root.reload();
            },
            scope: this
        }],
        root: new Ext.tree.AsyncTreeNode({
            id: "root",
            text: "模板树",
            iconCls: 'lanyo-tree-node-icon',
            expanded: true,
            listeners: {
                "expand": function(node) {
                    node.getOwnerTree().expandAll();
                },
                scope: this
            },
            loader: Global.templateDirLoader
        })
    });
    this.list.tree = this.tree;
    this.tree.on("click", function(node, eventObject) {
        var id = (node.id != 'root' && node.id > 0 ? node.id : "");
        this.list.parent = {
            id: id,
            name: node.text
        };
        this.list.store.baseParams.parentId = id;
        this.list.store.removeAll();
        this.list.store.load();
    }, this);
    TemplateDirManagePanel.superclass.constructor.call(this, {
        id: "templateDirManagePanel",
        // title : "信息栏目分类管理",
        closable: true,
        border: false,
        autoScroll: true,
        layout: "border",
        items: [this.tree, this.list]
    });

};
Ext.extend(TemplateDirManagePanel, Ext.Panel, {});