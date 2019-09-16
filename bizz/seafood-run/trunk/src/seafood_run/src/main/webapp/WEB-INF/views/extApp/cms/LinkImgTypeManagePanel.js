if (typeof Global === "undefined") {
    Global = {};
}

// 首页公告管理
LinkImgTypeManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "linkImgTypeManagePanel",
    // title:"首页公告管理",
    baseUrl: "linkImgType.java",
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
                width: 180,
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
                fieldLabel: "编码",
                name: "code",
                emptyText: '编码不能为空',
                allowBlank: false,
                blankText: '编码不能为空'
            },{
                fieldLabel: "说明",
                name: "text",
                emptyText: '说明不能为空',
                allowBlank: false,
                blankText: '说明不能为空'
            }]
        });
        return formPanel;
    },
    createWin: function() {
        return this.initWin(400, 200, "图片组类型管理");
    },
    create: function() {
    	LinkImgTypeManagePanel.superclass.create.call(this);
        this.fp.form.findField('code').setDisabled(false);
    },
    edit: function() {
        var win = LinkImgTypeManagePanel.superclass.edit.call(this);
        if (win) {
            this.fp.form.findField('code').setDisabled(true);
        }
    },
    storeMapping: ["id", "title","code", "text"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "标题",
            sortable: true,
            width: 100,
            dataIndex: "title"
        },{
            header: "编码",
            sortable: true,
            width: 100,
            dataIndex: "code"
        },{
            header: "说明",
            sortable: true,
            width: 100,
            dataIndex: "text"
        }])
        LinkImgTypeManagePanel.superclass.initComponent.call(this);
    }
});
