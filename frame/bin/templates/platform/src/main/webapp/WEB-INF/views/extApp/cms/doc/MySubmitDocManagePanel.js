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
 * 我提交的文章
 * 记录当前撰稿人、部门支行管理员发布的文章  状态为：提交待审核
 * @class MySubmitDocManagePanel
 * @extends Disco.Ext.CrudPanel
 */
MySubmitDocManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "mySubmitDocManagePanel",
    baseUrl: "newsDoc.java",
    autoLoadGridData: true,
    autoScroll: false,
    totalPhoto: 0,
    pageSize: 20,
    showAdd: false,
    batchRemoveMode: true,
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
    topicRender: function(value, p, record) {
        return String.format('{1}<b><a style="color:green" href="news.java?cmd=show&id={0}" target="_blank">&nbsp查看</a></b><br/>', record.id, value)
    },
    stateRender: function(v) {
        if (v == 3) {
            return "提交待审核";
        } else if (v == null || v == 1) {
            return "已发布";
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
            header: "流程状态",
            dataIndex: "state",
            renderer: this.stateRender
        }]);
        var user = Global.getCurrentUser;
        //分行管理员
        if (user.branchAdmin) {
            this.cm.setHidden(1, true);
        };
        MySubmitDocManagePanel.superclass.initComponent.call(this);
    }
});