NewsReviewManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "newsReviewManagePanel",
    baseUrl: "newsReview.java",
    gridSelModel: 'checkbox',
    batchRemoveMode: true,
    pageSize: 40,
    showAdd: false,
    showEdit: false,
    showView: false,
    statusRender: function(v) {
        if (v == 1) {
            return '<span style="color:green">正常</span></b>';
        } else if (v == 0) {
            return '<b><span style="color:red">待审核</span></b>';
        } else {
            return "未知";
        }
    },
    docLink: function(obj) {
        return '<a style="color:green" href="news.java?cmd=show&id=' + obj.id + '#reviewList" target="_blank">文章 </a>'
    },
    dataRender: function(v) {
        if (Ext.isEmpty(v)) {
            return ""
        }
        return '<div ext:qtitle="" ext:qtip="' + v + '">' + v + '</div>';
    },
    storeMapping: ["id", "inputUser", "owner", "content", "inputTime", "ip", "status", "doc"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "昵称",
            sortable: true,
            width: 60,
            dataIndex: "inputUser"
        }, {
            header: "登陆用户",
            sortable: true,
            width: 60,
            dataIndex: "owner",
            renderer: this.objectRender("trueName")
        }, {
            header: "评论内容",
            sortable: true,
            width: 300,
            dataIndex: "content",
            renderer: this.dataRender
        }, {
            header: "评论日期",
            sortable: true,
            width: 90,
            renderer: this.dateRender(),
            dataIndex: "inputTime"
        }, {
            width: 90,
            sortable: true,
            header: "评论者IP",
            dataIndex: "ip"
        }, {
            header: "状态",
            sortable: true,
            width: 50,
            dataIndex: "status",
            renderer: this.statusRender
        }, {
            header: "评论对应的文章",
            sortable: true,
            width: 80,
            dataIndex: "doc",
            renderer: this.docLink
        }]);
        this.gridButtons = [new Ext.Toolbar.Separator, {
            scope: this,
            text: "审核",
            cls: "x-btn-text-icon",
            icon: "img/core/up.gif",
            handler: this.executeCmd('audit')
        }];
        NewsReviewManagePanel.superclass.initComponent.call(this);
    }
});
