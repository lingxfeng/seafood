/**
 * 文档回收站
 * @class RecycleDocManagePanel
 * @extends Disco.Ext.CrudPanel
 */
RecycleDocManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "recycleDocManagePanel",
    baseUrl: "newsDoc.java",
    autoLoadGridData: true,
    autoScroll: false,
    totalPhoto: 0,
    pageSize: 20,
    showAdd: false,
    showEdit: false,
    showView: false,
    batchRemoveMode: true,
    defaultsActions: {
        create: 'save',
        list: 'getMyRecycle',
        view: 'view',
        update: 'update',
        remove: 'remove'
    },
    baseQueryParameter: {
        //查询暂存文档
        state: 0
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
        return "逻辑删除";
    },

    toggleDetails: function(btn, pressed) {
        var view = this.grid.getView();
        view.showPreview = pressed;
        view.refresh();
    },
    createWin: function() {
        return this.initWin(900, 600, "文章管理");
    },
    storeMapping: ["id", "title", "keywords", "createDate", "author", "source", "putDate", "dir", "toBranch", "branchDir", "state", "branchState", {
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
            header: "数据状态",
            dataIndex: "state",
            renderer: this.stateRender
        }]);
        this.gridButtons = [{
            text: '恢复',
            cls: "x-btn-text-icon",
            icon: "img/core/up.gif",
            handler: this.executeCmd("restoration")
        }]
        RecycleDocManagePanel.superclass.initComponent.call(this);
    }
});