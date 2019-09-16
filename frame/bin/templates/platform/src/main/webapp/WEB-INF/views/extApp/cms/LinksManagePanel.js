// 首页快捷链接管理
LinksManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "linksManagePanel",
    // title:"网站发布管理",
    baseUrl: "linksManage.java",
    gridSelModel: 'checkbox',
    batchRemoveMode: true,
    pageSize: 30,
    edit: function() {
        var win = LinksManagePanel.superclass.edit.call(this);
        if (win) {
            var record = this.grid.getSelectionModel().getSelected();
            var parentObj = record.get('org');
            var parentDir = this.fp.form.findField('deptId');
            if (parentObj) {
                parentObj.title || (parentObj.title = parentDir.deptId);
                parentDir.setOriginalValue(parentObj);
            }
        }
    },
    ascription: [["校内院系", "0"], ["管理部门", "1"], ["政府机构", "2"], ["兄弟学校", "3"]],
    ascriptionFormat: function(v) {
        if (v == 0) {
            return "校内院系";
        } else if (v == 1) {
            return "管理部门";
        } else if (v == 2) {
            return "政府机构";
        } else if (v == 3) {
            return "兄弟学校";
        }
    },
    createForm: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 50,
            labelAlign: 'right',
            defaults: {
                width: 330,
                xtype: "textfield"
            },
            items: [{
                xtype: "hidden",
                name: "id"
            }, {
                fieldLabel: "名称",
                name: "name",
                emptyText: '名称不能为空',
                allowBlank: false,
                blankText: '名称不能为空'
            }, {
                fieldLabel: "网址",
                name: "linkUrl"
            }, {
                xtype: "numberfield",
                fieldLabel: "排序",
                name: "sort"
            }, {
                xtype: "combo",
                name: "ascription",
                hiddenName: "ascription",
                fieldLabel: "分类",
                displayField: "title",
                valueField: "value",
                store: new Ext.data.SimpleStore({
                    fields: ['title', 'value'],
                    data: this.ascription
                }),
                editable: false,
                mode: 'local',
                triggerAction: 'all',
                emptyText: '请选择...'
            }, {
                xtype: "textarea",
                fieldLabel: "简介",
                name: "intro",
                height: 50
            }]
        });
        return formPanel;
    },
    userRender: function(v) {
        return v ? v.trueName : "$!{lang.get('Unknown')}";
    },
    createWin: function() {
        return this.initWin(440, 245, "快捷链接管理");
    },
    storeMapping: ["id", "name", "linkUrl", "intro", "ascription", "sort", "org"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "名称",
            sortable: true,
            width: 100,
            dataIndex: "name"
        }, {
            header: "URL地址",
            sortable: true,
            width: 100,
            dataIndex: "linkUrl"
        }, {
            header: "归属",
            sortable: true,
            width: 100,
            dataIndex: "ascription",
            renderer: this.ascriptionFormat
        }, {
            header: "排序",
            sortable: true,
            width: 100,
            dataIndex: "sort"
        }])
        LinksManagePanel.superclass.initComponent.call(this);
    }
});
