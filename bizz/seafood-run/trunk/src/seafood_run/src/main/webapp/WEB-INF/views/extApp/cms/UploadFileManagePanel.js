// 附件管理

UploadFileManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "uploadFileManagePanel",
    //title:"视频文件管理",
    baseUrl: "attachment.java",
    gridSelModel: 'checkbox',
    baseQueryParameter: {
        type: 0
        //文件类型的
    },
    pageSize: 20,
    batchRemoveMode: true,
    showAdd: false,
    showEdit: false,
    showView: false,
    topicRender: function(v) {
        return v + '<b><a style="color:green" href="' + v + '" target="_blank">&nbsp下载</a></b>'
    },

    storeMapping: ["id", "fileName", "oldName", "path", "length", "createTime"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "名称",
            sortable: true,
            width: 100,
            dataIndex: "fileName"
        }, {
            header: "原始名称",
            sortable: true,
            width: 100,
            dataIndex: "oldName"
        }, {
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
            renderer: this.dateRender()
        }])

        this.gridButtons = [new Ext.Toolbar.Separator, new Ext.Toolbar.Button({
            text: "附件类型",
            iconCls: 'changeState',
            menu: [{
                text: "1附件",
                noneSelectRow: true,
                icon: "include/images/menuPanel/tag_green.gif",
                handler: function() {

                    this.store.reload({
                        'params': {
                            'type': 1
                        }
                    })
                },
                scope: this
            }, {
                text: "2媒体文件",
                noneSelectRow: true,
                icon: "include/images/menuPanel/tag_green1.gif",
                handler: function() {

                    this.store.reload({
                        'params': {
                            'type': 2
                        }
                    })
                },
                scope: this
            }, {
                text: "3FLASH",
                noneSelectRow: true,
                icon: "include/images/menuPanel/tag_green1.gif",
                handler: function() {

                    this.store.reload({
                        'params': {
                            'type': 3
                        }
                    })
                },
                scope: this
            }, {
                text: "4图片",
                icon: "include/images/menuPanel/tag_green1.gif",
                handler: function() {

                    this.store.reload({
                        'params': {
                            'type': 4
                        }
                    })
                },
                scope: this
            }]
        })]
        UploadFileManagePanel.superclass.initComponent.call(this);
    }
});
