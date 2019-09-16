/**
 * 网站发布管理
 * 
 * @class DynamicStaticPairManagePanel
 * @extends Disco.Ext.CrudPanel
 */
DynamicStaticPairManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "dynamicStaticPairPanel",
    // title:"网站发布管理",
    baseUrl: "publish.java",
    pageSize: 20,
    viewWin: {
        width: 600,
        height: 185,
        title: "详情查看"
    },
    createForm: function() {
        console.log(ConfigConst.BASE)
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 60,
            labelAlign: 'right',
            defaults: {
                width: 400,
                xtype: "textfield"
            },
            items: [{
                xtype: "hidden",
                name: "id"
            }, Disco.Ext.Util.columnPanelBuild({
                columnWidth: .60,
                items: [{
                    name: "title",
                    anchor: '98%',
                    fieldLabel: "页面名称"
                }]
            }, {
                columnWidth: .40,
                items: [Ext.applyIf({
                    anchor: '94%'
                }, ConfigConst.BASE.getDictionaryCombo("intervals", "自动周期", "pageRelease", "value"))]
            }), {
                fieldLabel: "动态地址",
                name: "url"
            }, {
                fieldLabel: "静态地址",
                name: "path"
            }]
        });
        return formPanel;
    },
    createWin: function() {
        return this.initWin(500, 160, "页面静态化配置......");
    },
    storeMapping: ["id", "url", "path", "title", "intervals", "status", "vdate"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "名称",
            sortable: true,
            width: 200,
            dataIndex: "title"
        }, {
            header: "动态地址",
            sortable: true,
            width: 350,
            dataIndex: "url"
        }, {
            header: "静态地址",
            sortable: true,
            width: 180,
            dataIndex: "path"
        }, {
            header: "发布周期",
            sortable: true,
            width: 70,
            dataIndex: "intervals"
        }, {
            header: "上次发布时间",
            sortable: true,
            width: 120,
            dataIndex: "vdate",
            renderer: this.dateRender()
        }]);
        this.gridButtons = [new Ext.Toolbar.Separator(), {
            text: "静态生成",
            iconCls: 'changeState',
            // pressed : true,
            handler: this.executeMulitCmd("generator"),
            scope: this
        }];
        DynamicStaticPairManagePanel.superclass.initComponent.call(this);
    }
});
