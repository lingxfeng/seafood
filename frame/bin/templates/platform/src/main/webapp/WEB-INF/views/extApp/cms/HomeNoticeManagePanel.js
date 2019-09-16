// 首页公告管理
HomeNoticeManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "homeNoticeManagePanel",
    // title:"首页公告管理",
    baseUrl: "homeNotice.java",
    gridSelModel: 'checkbox',
    pageSize: 20,
    batchRemoveMode: true,
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 80,
            labelAlign: 'right',
            defaults: {
                width: 320,
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
            }, 
            {
                fieldLabel: "作者",
                name: "author"
            }, 
			 {
                xtype: 'datefield',
                format:'Y-m-d H:i:s',
                fieldLabel: "发布日期",
                editable: false,
                name: "sendTime",
                allowBlank: false,
                blankText: '发布日期不能为空'
            },{
                xtype: "numberfield",
                fieldLabel: "排序",
                name: "sort"
            } , {
            	 xtype: "textarea",
                fieldLabel: "内容",
                name: "contents",
                width: 330,
                height:130
            } ]
        });
        return formPanel;
    },
    userRender: function(v) {
        return v ? v.trueName : "$!{lang.get('Unknown')}";
    },
    createWin: function() {
        return this.initWin(500, 420, "首页公告管理");
    },
    storeMapping: ["id", "title", "author", "sendTime",  "sort","contents"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "标题",
            sortable: true,
            width: 100,
            dataIndex: "title"
        }, {
            header: "作者",
            sortable: true,
            width: 100,
            dataIndex: "author"
        }, {
            header: "发布日期",
            sortable: true,
            renderer : this.dateRender(),
            width: 100,
            dataIndex: "sendTime"
        }, {
            header: "排序",
            sortable: true,
            width: 100,
            dataIndex: "sort"
        }])
        HomeNoticeManagePanel.superclass.initComponent.call(this);
    }
});



