// 广告图片管理
AdManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "adManagePanel",
    // title:"广告图片管理",
    baseUrl: "adManage.java",
    gridSelModel: 'checkbox',
    gridSelModel: 'checkbox',
    fileUpload: true,
    pageSize: 20,
    batchRemoveMode: true,
    position: [["左侧", 1], ["右侧", 2], ["中部通栏", 3], ["中部滚动通栏", 4]],
    positionFormat: function(v) {
        if (v == 1) {
            return "左侧";
        } else if (v == 2) {
            return "右侧";
        } else if (v == 3) {
            return "中部通栏";
        } else if (v == 4) {
            return "中部滚动通栏";
        }
    },
    isValid: [["启用", 1], ["禁用", 2]],
    isValidFormat: function(v) {
        if (v == 1) {
            return "启用";
        } else if (v == 2) {
            return "禁用";
        }
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
                fieldLabel: "名称",
                name: "advertName",
                emptyText: '名称不能为空',
                allowBlank: false,
                blankText: '名称不能为空'
            }, {
                fieldLabel: "链接地址",
                name: "advertUrl"
            }, {
                xtype: "combo",
                name: "position",
                hiddenName: "position",
                fieldLabel: "位置",
                displayField: "title",
                valueField: "value",
                store: new Ext.data.SimpleStore({
                    fields: ['title', 'value'],
                    data: this.position
                }),
                editable: false,
                mode: 'local',
                triggerAction: 'all'
            }, {
                xtype: 'fileuploadfield',
                emptyText: '选择上传文件',
                fieldLabel: '广告背景图片',
                name: "advertPath",
                buttonText: '选择',
                listeners: {
                    'fileselected': function(fb, v) { //undefined
                        var suffix = "";
                        var ext = "";
                        if (v != null) {
                            suffix = v.split(".")[1];
                            ext = suffix.toUpperCase();
                        }
                        if (suffix != '' && suffix != 'undefined') {
                            if (ext != "BMP" && ext != "PNG" && ext != "GIF" && ext != "JPG" && ext != "JPEG") {
                                Ext.Msg.alert("信息提示", "只能上传图片,图片限于bmp,png,gif,jpeg,jpg格式");
                                fb.setValue("");
                            }

                        }
                    }

                }// END listeners
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
            }

            ]
        });
        return formPanel;
    },
    userRender: function(v) {
        return v ? v.trueName : "$!{lang.get('Unknown')}";
    },
    createWin: function() {
        return this.initWin(450, 250, "首页广告管理");
    },
    storeMapping: ["id", "advertName", "advertPath", "advertUrl", "position", "createTime", "isValid", "sort"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([
        {
            header: "名称",
            sortable: true,
            width: 100,
            dataIndex: "advertName"
        }, {
            header: "路径",
            sortable: true,
            width: 100,
            dataIndex: "advertPath"
        }, {
            header: "链接地址",
            sortable: true,
            width: 100,
            dataIndex: "advertUrl"
        }, {
            header: "位置",
            sortable: true,
            width: 100,
            dataIndex: "position",
            renderer: this.positionFormat
        }, {
            header: "上传时间",
            sortable: true,
            width: 100,
            dataIndex: "createTime",
            renderer: this.dateRender()
        }, {
            header: "状态",
            sortable: true,
            width: 100,
            dataIndex: "isValid",
            renderer: this.isValidFormat
        }, {
            header: "排序",
            sortable: true,
            width: 40,
            dataIndex: "sort"
        }

        ])
        AdManagePanel.superclass.initComponent.call(this);
    }
});
