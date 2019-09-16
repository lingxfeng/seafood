if (typeof Global === "undefined") {
    Global = {};
}
if (!Global.newsDirLoader) {
    Global.newsDirLoader = new Ext.tree.TreeLoader({
        url: "newsDir.ejf?cmd=getNewsDir&pageSize=-1&treeData=true",
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
 * 日志列表
 */
NewsDocManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "newsDocManagePanel",
    //title:"信息中心",
    baseUrl: "newsDoc.ejf",
    autoLoadGridData: false,
    totalPhoto: 0,
    pageSize: 20,
    contentTypes: [["文字", "text"], ["连接", "url"]],
    gridViewConfig: {
        forceFit: true,
        enableRowBody: true,
        showPreview: true,
        getRowClass: function(record, rowIndex, p, store) {
            if (this.showPreview) {
                p.body = "<p>文章简介：" + record.data.intro + '</p><br/>';
                return 'x-grid3-row-expanded';
            }
            return 'x-grid3-row-collapsed';
        }
    },
    selectPhoto: function(id) {
        for (var i = 0; i < this.photoList.items.getCount(); i++) {
            this.photoList.getComponent(i).body.clearOpacity();
        }
        var p = this.photoList.getComponent(id);
        p.body.setOpacity(.6);
        this.photoList.currentPhoto = id;
    },
    addPhoto: function(obj) {
        var i = this.totalPhoto++;
        var up = {
            columnWidth: .6,
            id: "photo_" + i
        };
        var title = {
            columnWidth: .4,
            id: "photo_" + i + "_title",
            xtype: "textfield"
        };
        if (obj.path) {
            up.value = obj.path;
            up.photoId = obj.id;
            up.readOnly = true;
            title.value = obj.title;
            title.readOnly = true;
        } else
            up.inputType = "file";
        var uploadField = new Ext.form.TextField(up);
        var t = new Ext.Panel({
            anchor: "95%",
            layout: "column",
            items: [{
                width: 70,
                html: "名称:"
            }, title, uploadField]
        });
        this.photoList.add(t);
        this.photoList.doLayout();
        t.body.on("click", this.selectPhoto.createDelegate(this, [t.id]));
    },
    removePhoto: function() {
        var photoId = this.photoList.currentPhoto;
        var fp = this.fp;
        if (photoId) {
            Ext.Msg.confirm("提示", "真的要删除当前选中照片吗？", function(btn) {
                if (btn == "yes") {
                    var p = this.photoList.getComponent(photoId);
                    if (p.getComponent(2).photoId) {
                        //删除服务器端的数据
                        Ext.Ajax.request({
                            url: "newsDoc.ejf?cmd=removePhoto",
                            params: {
                                id: fp.form.findField("id").getValue(),
                                photoId: p.getComponent(2).photoId
                            }
                        })
                    }
                    this.photoList.remove(photoId);
                    this.photoList.doLayout();
                }
            }, this);
        }
    },
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            fileUpload: true,
            autoScroll: true,
            labelWidth: 70,
            labelAlign: 'right',
            items: [{
                xtype: "hidden",
                name: "id"
            }, {
                xtype: "fieldset",
                anchor: "-20",
                height: 180,
                collapsible: true,
                title: "基本信息",
                layout: "form",
                items: [Disco.Ext.Util.columnPanelBuild({
                    columnWidth: .5,
                    items: {
                        name: "title",
                        fieldLabel: "标&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;题",
                        allowBlank: false
                    }
                }, {
                    columnWidth: .3,
                    items: {
                        name: "subTitle",
                        fieldLabel: "副标题"
                    }
                }, {
                    columnWidth: .2,
                    items: {
                        xtype: "combo",
                        store: new Ext.data.SimpleStore({
                            fields: ['name', 'value'],
                            data: this.contentTypes
                        }),
                        mode: "local",
                        valueField: "value",
                        displayField: "name",
                        fieldLabel: '内容性质',
                        name: 'contentTypes',
                        hiddenName: "contentTypes",
                        lazyRender: true,
                        selectOnFocus: true,
                        editable: false,
                        triggerAction: "all"
                    }
                }), Disco.Ext.Util.columnPanelBuild({
                    columnWidth: .4,
                    items: {
                        xtype: "treecombo",
                        fieldLabel: "文章目录",
                        displayField: "title",
                        name: "dirId",
                        hiddenName: "dirId",
                        width: 100,
                        tree: new Ext.tree.TreePanel({
                            root: new Ext.tree.AsyncTreeNode({
                                id: "root",
                                text: "所有目录",
                                expanded: true,
                                loader: Global.newsDirLoader
                            })
                        })
                    }
                }, {
                    columnWidth: .3,
                    items: {
                        xtype: "datefield",
                        name: "displayTime",
                        format: "Y-n-j G:i:s",
                        fieldLabel: "显示时间",
                        value: new Date()
                    }
                }, {
                    columnWidth: .3,
                    items: {
                        xtype: "datefield",
                        name: "expiredTime",
                        format: "Y-n-j G:i:s",
                        fieldLabel: "超期时间"
                    }
                }), Disco.Ext.Util.oneColumnPanelBuild({
                    xtype: "smartcombo",
                    fieldLabel: "文章模板",
                    name: "templateId",
                    hiddenName: "templateId",
                    displayField: "title",
                    valueField: "id",
                    lazyRender: true,
                    triggerAction: "all",
                    typeAhead: true,
                    editable: false,
                    store: new Ext.data.JsonStore({
                        id: "id",
                        url: "template.ejf?cmd=list",
                        root: "result",
                        totalProperty: "rowCount",
                        remoteSort: true,
                        baseParams: {
                            pageSize: "-1"
                        },
                        fields: ["id", "title"]
                    })
                }, {
                    fieldLabel: "作者",
                    name: "author"
                }, {
                    fieldLabel: "来源",
                    name: "source"
                }, {
                    xtype: "datefield",
                    name: "topTime",
                    format: "Y-n-j G:i:s",
                    fieldLabel: "置顶时间"
                }), Disco.Ext.Util.twoColumnPanelBuild({
                    name: "url",
                    fieldLabel: "连接"
                }, {
                    name: "keywords",
                    fieldLabel: "关键字"
                }), {
                    xtype: "textarea",
                    anchor: "-20",
                    name: "intro",
                    height: 50,
                    fieldLabel: "文章摘要"
                }]
            }, {
                xtype: "fieldset",
                anchor: "-20",
                //width:675,
                autoHeight: true,
                title: "文章内容",
                layout: "form",
                items: {
                    xtype: "textarea",
                    name: "content",
                    hideLabel: true,
                    listeners: {
                        "render": function(f) {
                            fckEditor = new FCKeditor("content", Ext.getBody().dom.offsetWidth - 190, 350, "Normal");
                            fckEditor.BasePath = "/fckeditor/";
                            fckEditor.Config['CustomConfigurationsPath'] = "/fckeditor/fckconfig.js"
                            fckEditor.ReplaceTextarea();
                        }
                    }
                }
            }, {
                xtype: "fieldset",
                anchor: "-20",
                collapsible: true,
                autoHeight: true,
                autoScroll: true,
                title: "图片/附件",
                layout: "fit",
                listeners: {
                    "render": function(f) {
                        this.photoList = f.findById("photoList");
                    },
                    scope: this
                },
                items: {
                    id: "photoList",
                    autoScroll: true,
                    height: 50
                },
                bbar: [{
                    text: "添加图片/附件",
                    handler: this.addPhoto,
                    scope: this
                }, {
                    text: "删除图片/附件",
                    handler: this.removePhoto,
                    scope: this
                }]
            }]
        });
        return formPanel;
    },
    create: function() {
        var win = NewsDocManagePanel.superclass.create.call(this);
        if (this.photoList) {
            var list = this.photoList;
            while (list.items && list.items.getCount() > 0)
                list.remove(0);
            this.addPhoto({});
        }
        Disco.Ext.Util.setFCKEditorContent("content");
        this.fp.form.findField("dirId").setValue(this.currentDir);
        this.fp.form.findField("author").setValue("$!{session.EASYJF_SECURITY_USER.trueName}");
    },
    edit: function() {
        var win = NewsDocManagePanel.superclass.edit.call(this);
        if (win) {
            var record = this.grid.getSelectionModel().getSelected();
            var list = this.photoList;
            while (list.items && list.items.getCount() > 0)
                list.remove(0);
            if (record.data.allResource && record.data.allResource.length > 0) {
                var ps = record.data.allResource;
                for (var i = 0; i < ps.length; i++) {
                    this.addPhoto(ps[i]);
                }
            } else
                this.addPhoto({});
            Ext.Ajax.request({
                url: this.baseUrl + "?cmd=getContent&id=" + record.get("id"),
                success: function(response) {
                    var data = Ext.decode(response.responseText);
                    Disco.Ext.Util.setFCKEditorContent("content", data);
                }
            });
        }

    },
    view: function() {
        var record = this.grid.getSelectionModel().getSelected();
        if (!record)
            return Ext.Msg.alert("$!{lang.get('Prompt')}", "$!{lang.get('Select first')}");
        window.open("news.ejf?cmd=show&id=" + record.get("id"));
    },
    topicRender: function(value, p, record) {
        return String.format('{1}<b><a style="color:green" href="news.ejf?cmd=show&id={0}" target="_blank">查看</a></b><br/>', record.id, value)
    },
    resourceRender: function(v) {
        return v && v.length ? v.length + "个" : "无";
    },
    toggleDetails: function(btn, pressed) {
        var view = this.grid.getView();
        view.showPreview = pressed;
        view.refresh();
    },
    createWin: function() {
        return this.initWin(Ext.getBody().dom.offsetWidth - 120, Ext.getBody().dom.offsetHeight - 30, "文章管理");
    },
    storeMapping: ["id", "title", "subTitle", "dir", "contentTypes", "url", "pics", "displayTime", "content", "intro", "author", "source", "sequence", "expiredTime", "readTimes", "inputTime", "template", "elite", "auditing", "reviewEnbaled", "reviewTimes", "reviews", "topTime", "collectionTimes", "htmlPath", "editor", "keywords", "staticUrl", {
        name: "op",
        mapping: "id"
    }, {
        name: "dirId",
        mapping: "dir"
    }, {
        name: "templateId",
        mapping: "template"
    }, "resources", "allResource"],
    initComponent: function() {
        this.expander = new Ext.grid.RowExpander({
            tpl: new Ext.Template('<p><b>文章简介:</b>{intro}</p><br />')
        });
        this.gridConfig = {
            plugins: this.expander
        }
        this.cm = new Ext.grid.ColumnModel([this.expander, {
            header: "主题",
            dataIndex: 'title',
            width: 250,
            renderer: this.topicRender
        }, {
            header: "栏目",
            sortable: true,
            width: 60,
            dataIndex: "dir",
            renderer: this.objectRender("title")
        }, {
            header: "显示日期",
            sortable: true,
            width: 100,
            dataIndex: "displayTime",
            renderer: this.dateRender()
        }, {
            header: "作者",
            sortable: true,
            width: 60,
            dataIndex: "author"
        }, {
            header: "审核",
            sortable: true,
            width: 40,
            dataIndex: "auditing",
            renderer: this.booleanRender
        }, {
            header: "推荐",
            sortable: true,
            width: 40,
            dataIndex: "elite",
            renderer: this.booleanRender
        }, {
            header: "编辑",
            sortable: true,
            width: 60,
            dataIndex: "editor"
        }, {
            header: "附件",
            sortable: true,
            width: 40,
            dataIndex: "resources",
            renderer: this.resourceRender
        },

        {
            header: "输入时间",
            sortable: true,
            width: 100,
            dataIndex: "inputTime",
            renderer: this.dateRender()
        }]);

        this.gridButtons = [new Ext.Toolbar.Separator, {
            text: "显示摘要",
            cls: "x-btn-text-icon",
            icon: "images/core/15.gif",
            enableToggle: true,
            handler: this.toggleDetails,
            scope: this
        }, {
            text: "审核",
            cls: "x-btn-text-icon",
            icon: "images/core/08.gif",
            handler: this.executeCmd("audit"),
            scope: this
        }, {
            text: "加精",
            cls: "x-btn-text-icon",
            icon: "images/core/24.gif",
            handler: this.executeCmd("elite"),
            scope: this
        }, {
            text: "发布文章",
            cls: "x-btn-text-icon",
            icon: "images/core/04.gif",
            handler: this.executeMulitCmd("generator"),
            scope: this
        }];
        NewsDocManagePanel.superclass.initComponent.call(this);
    }
});