// 附件管理

DocumentFileManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "documentFileManagePanel",
    //title:"电子文档管理",
    baseUrl: "attachment.java",
    gridSelModel: 'checkbox',
    batchRemoveMode: true,
    baseQueryParameter: {
        type: 3
    },
    pageSize: 20,
    showAdd: false,
    showEdit: false,
    showView:false,
   
    topicRender: function(v) {
    	
        return v+'<b><a style="color:green" href="'+v+'" target="_blank">&nbsp下载</a></b>'
    },
    storeMapping: ["id", "fileName", "oldName","path", "length", "createTime"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "文档名称",
            sortable: true,
            width: 100,
            dataIndex: "fileName"
           
        }, {
            header: "原名称",
            sortable: true,
            width: 100,
            dataIndex: "oldName"
        },{
            header: "路径",
            sortable: true,
            width: 100,
            dataIndex: "path",
            	 renderer: this.topicRender
        }, {
            header: "大小/字节",
            sortable: true,
            width: 50,
            dataIndex: "length"
        }, {
            header: "上传日期",
            sortable: true,
            width: 50,
            dataIndex: "createTime",
            renderer:this.dateRender()
        }

        ])
        DocumentFileManagePanel.superclass.initComponent.call(this);
    }
});
