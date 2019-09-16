/**
 * 文档采纳箱
 * @class AdoptDocManagePanel
 * @extends Disco.Ext.CrudPanel
 */
AdoptDocManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    gridSelModel: 'checkbox',
    id: "adoptDocManagePanel",
    baseUrl: "newsDoc.java",
    autoLoadGridData: true,
    autoScroll: false,
    totalPhoto: 0,
    pageSize: 20,
    showAdd: false,
    showEdit: false,
    showRemove: false,
    showView: false,
    defaultsActions: {
        create: 'save',
        list: 'findAdpotDoc',
        view: 'view',
        update: 'update',
        remove: 'remove'
    },
    topicRender: function(value, p, record) {
        return String.format('{1}<b><a style="color:green" href="news.java?cmd=show&id={0}" target="_blank">&nbsp查看</a></b><br/>', record.id, value)
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
            width: 250,
            renderer: this.topicRender
        }, {
            header: "分行栏目",
            sortable: true,
            width: 90,
            dataIndex: "branchDir",
            renderer: this.objectRender("name")
        }, {
            header: "作者",
            sortable: true,
            width: 80,
            dataIndex: "author"
        }, {
            header: "撰稿日期",
            sortable: true,
            width: 100,
            dataIndex: "createDate",
            renderer: this.dateRender()
        }]);
        AdoptDocManagePanel.superclass.initComponent.call(this);
    }
});