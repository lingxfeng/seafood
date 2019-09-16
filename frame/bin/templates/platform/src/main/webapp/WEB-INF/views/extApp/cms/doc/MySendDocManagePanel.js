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
 * 发布文章 只能发布 不显示列表 以后列表要取消 直接打开WIN进行发布 
 * 如需要编辑、删除等操作到我提交的文章中去或暂存相中
 * @class MySendDocManagePanel
 * @extends Ext.Window
 */
MySendDocManagePanel = Ext.extend(Ext.Window, {
    modal: true,
    shim: false,
    id: 'MySendDocManagePanel',
    width: 900,
    height: 620,
    title: '文章管理',
    closable: false,
    maximizable: false,
    minimizable: false,
    border: false,
    buttonAlign: 'center',
    baseUrl: "newsDoc.java",
    //记录用户的上次操作记录
    tempCookie: {
        dir: null,
        branchDir: null
    },
    show: function() {
        //读取上一次操作的记录
        if (this.tempCookie.dir) {
            this.fp.form.findField("dir").setValue(this.tempCookie.dir);
        }
        if (this.tempCookie.branchDir) {
            this.fp.form.findField("branchDir").setValue(this.tempCookie.branchDir);
        }
        var user = Global.getCurrentUser;
        if (user) {
            this.fp.form.findField('author').setValue(user.trueName);//默认作者即当前登陆人
        }

        if (this.editor) {
            this.editor.html('');
            this.editor.sync();
        }
        if (!this.isVisible()) {
            MySendDocManagePanel.superclass.show.call(this);
        }
    },
    initComponent: function() {
        this.buttons = this.createButtons();
        MySendDocManagePanel.superclass.initComponent.call(this);
        this.buildFormItems();
        this.on('show', function() {
            this.fp.form.isValid()
        }, this);
    },
    buildFormItems: function() {
        this.fp = this.createFormPanel();
        this.add(this.fp);
    },
    createButtons: function() {
        var defaultText = "送审";
        var user = Global.getCurrentUser;
        //如果当前登陆用户是分行管理员则显示发布按钮反之为送审
        if (user.branchAdmin) {
            defaultText = "发布";
        };
        var btns = [];
        if (!user.branchAdmin) {
            btns.push({
                scope: this,
                text: defaultText,
                iconCls: 'save',
                handler: this.submitSaveDoc
            });
        }
        btns.push({
            scope: this,
            text: '暂存',
            iconCls: 'clean',
            handler: this.storageDoc
        });
        btns.push({
            scope: this,
            text: '取消',
            iconCls: 'delete',
            handler: function() {
                this.hide();
            }
        });
        return btns
    },
    /**
     * 保存文章
     */
    submitSaveDoc: function() {
        this.beforeFormSubmit('save');
    },
    /**
     * 暂存文章
     */
    storageDoc: function() {
        this.beforeFormSubmit('storage');
    },
    beforeFormSubmit: function(method) {
        var b = Disco.Ext.CrudFunction.validateForm(this.fp.form);
        if (!b) {
            return false;
        }
        var id = this.fp.form.findField("id").getValue();
        method = (method == 'storage') ? method : (id ? 'update' : 'save');

        var url = this.baseUrl;
        var cmd = this.fp.form.findField("cmd");
        if (cmd == null) {
            this.fp.add({
                name: 'cmd',
                value: method,
                xtype: 'hidden'
            });
            this.fp.doLayout();
        } else {
            cmd.setValue(method);
        }

        Disco.Ext.Util.submitForm(this.fp.form, this.baseUrl + '?cmd=' + method, function(form, action) {
            this.fp.form.clearData();
            this.fp.form.reset();
            this.fp.form.isValid();
            this.hide();
            this.refurbishParentGrid();
        }, this);
    },
    refurbishParentGrid: function() {
    },
    createFormPanel: function() {
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
                    fieldLabel: "作者",
                    allowBlank: false,
                    blankText: '作者不能为空'
                }
            }, {
                columnWidth: .3,
                items: {
                    name: "source",
                    fieldLabel: "来源",
                    allowBlank: false,
                    blankText: '来源不能为空'
                }
            }, {
                columnWidth: .4,
                items: {
                    name: "putDate",
                    anchor: '93.8%',
                    editable: false,
                    format: 'Y-m-d G:i',
                    xtype: 'datefield',
                    value: new Date(),
                    fieldLabel: "撰稿日期"
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
                            var branchDir = this.fp.getForm().findField('branchDir');
                            if (checked) {
                                branchDir.setDisabled(false);
                                this.fp.form.findField('dir').allowBlank = true;
                            } else {
                                this.fp.form.findField('dir').allowBlank = false;
                                branchDir.setDisabled(true);
                            }
                        }
                    }
                }
            }, {
                columnWidth: .4,
                items: {
                    //默认在“发布到分行”没有选中时则禁用此项
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
                        }),
                        listeners: {
                            scope: this,
                            click: function(node) {
                                this.tempCookie.branchDir = node;
                            }
                        }
                    })
                }
            }), {
                width: 880,
                height: 395,
                margins: {
                    left: 10
                },
                hideLabel: true,
                //id: 'content1',
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
        var fp = formPanel.getForm();
        var user = Global.getCurrentUser;
        if (user.branchAdmin) {
            var dir = fp.findField('dir')
            //TODO bug 组件销毁后需再禁用才能限定为不必填
            dir.destroy();
            dir.setDisabled(true);
            fp.findField('toBranch').destroy();
            fp.findField('branchDir').setDisabled(false);
        }
        return formPanel;
    }
});
