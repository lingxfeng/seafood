// 首页图片新闻管理
HomeNewsPicManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "homeNewsPicManagePanel",
    // title:"首页新闻图片管理",
    baseUrl: "homeNewsPic.java",
    gridSelModel: 'checkbox',
    batchRemoveMode: true,
    fileUpload: true,
    pageSize: 20,
    isValid: [["启用", 1], ["禁用", 2]],
    isValidFormat: function(v) {
        if (v == 1) {
            return "启用";
        } else if (v == 2) {
            return "禁用";
        }
    },
    picRender: function(v) {

        if (v != null && v != '') {
            return v + '<b><a style="color:green" href="' + v + '" target="_blank">&nbsp查看</a></b>'
        } else
            return ''
    },
    picJcrop: function(id) {
        return '<b><a style="color:red" href="homeNewsPic.java?cmd=picJcrop&id=' + id + '" target="_blank">裁剪 </a></b>'
    },
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 80,
            fileUpload: true,
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
            }, {
                fieldLabel: '文件',
                name: 'path',
                buttonText: '选择',
                xtype: "fileuploadfield"
            }, {
                xtype: "combo",
                name: "isValid",
                hiddenName: "isValid",
                fieldLabel: "状态",
                displayField: "title",
                valueField: "value",
                allowBlank: false,
                blankText: '状态不能为空',
                store: new Ext.data.SimpleStore({
                    fields: ['title', 'value'],
                    data: this.isValid
                }),
                editable: false,
                mode: 'local',
                triggerAction: 'all',
                emptyText: '请选择...'
            }, {
                xtype: "numberfield",
                fieldLabel: "排序",
                name: "sort"
            }, {
                fieldLabel: "文章地址",
                name: "docUrl",
                emptyText: '文章地址',
                allowBlank: false,
                blankText: '文章地址'
            }

            ]
        });
        return formPanel;
    },
    userRender: function(v) {
        return v ? v.trueName : "$!{lang.get('Unknown')}";
    },
    createWin: function() {
        return this.initWin(450, 250, "图片新闻管理");
    },
    storeMapping: ["id", "title", "fileName", "path", "createTime", "sequence", "isValid", "docUrl", "sort"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "标题",
            sortable: true,
            width: 100,
            dataIndex: "title"
        }, {
            header: "图片名称",
            sortable: true,
            width: 100,
            dataIndex: "fileName"
        }, {
            header: "路径",
            sortable: true,
            width: 120,
            dataIndex: "path",
            renderer: this.picRender
        }, {
            header: "上传时间",
            sortable: true,
            width: 90,
            renderer: this.dateRender(),
            dataIndex: "createTime"
        }, {
            header: "状态",
            sortable: true,
            width: 30,
            dataIndex: "isValid",
            renderer: this.isValidFormat
        }, {
            header: "排序",
            sortable: true,
            width: 30,
            dataIndex: "sort"
        }, {
            header: "操作",
            sortable: true,
            width: 20,
            hidden: true,
            dataIndex: "id",
            renderer: this.picJcrop
        }])
        HomeNewsPicManagePanel.superclass.initComponent.call(this);
    }
});
