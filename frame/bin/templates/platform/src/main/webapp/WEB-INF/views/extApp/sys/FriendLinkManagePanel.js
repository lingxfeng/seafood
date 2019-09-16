// 网站友情连接管理
FriendLinkManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "friendLinkManagePanel",
    // title:"网站发布管理",
    baseUrl: "friendLink.java",
    pageSize: 20,
    status: [["申请", 0], ["新加", 1], ["审核通过", 2], ["删除", -2]],
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
                fieldLabel: "名称",
                name: "title"
            }, {
                fieldLabel: "类型",
                name: "types"
            }, {
                fieldLabel: "网址",
                name: "url"
            }, {
                fieldLabel: "图片地址",
                name: "pic"
            }, {
                xtype: "checkbox",
                fieldLabel: "是否锁定",
                name: "locked",
                width: 10
            }, {
                fieldLabel: "申请人",
                name: "proposer"
            }, {
                fieldLabel: "email",
                name: "email"
            }, {
                fieldLabel: "联系方式",
                name: "contract"
            }, {
                xtype: "numberfield",
                fieldLabel: "排序",
                name: "sequence"
            }, {
                xtype: "combo",
                name: "status",
                hiddenName: "status",
                fieldLabel: "状态",
                displayField: "title",
                valueField: "value",
                store: new Ext.data.SimpleStore({
                    fields: ['title', 'value'],
                    data: this.status
                }),
                editable: false,
                mode: 'local',
                triggerAction: 'all',
                emptyText: '请选择...'
            }, {
                xtype: "textarea",
                fieldLabel: "申请原因",
                name: "reason",
                height: 50
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
        return this.initWin(438, 450, "友情连接管理");
    },
    storeMapping: ["id", "title", "types", "url", "pic", "intro", "locked", "sequence", "proposer", "email", "reason", "contract", "inputTime", "status", "opUser"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "名称",
            sortable: true,
            width: 100,
            dataIndex: "title"
        }, {
            header: "类型",
            sortable: true,
            width: 100,
            dataIndex: "types"
        }, {
            header: "状态",
            sortable: true,
            width: 100,
            dataIndex: "status"
        }, {
            header: "图片",
            sortable: true,
            width: 100,
            dataIndex: "pic"
        }, {
            header: "申请人",
            sortable: true,
            width: 100,
            dataIndex: "proposer"
        }, {
            header: "操作人",
            sortable: true,
            width: 100,
            dataIndex: "opUser"
        }, {
            header: "网址",
            sortable: true,
            width: 120,
            dataIndex: "url"
        }])
        FriendLinkManagePanel.superclass.initComponent.call(this);
    }
});
