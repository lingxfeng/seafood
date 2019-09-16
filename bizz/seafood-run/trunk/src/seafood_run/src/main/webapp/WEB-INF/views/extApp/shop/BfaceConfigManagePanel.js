if (typeof Global === "undefined") {
    Global = {};
}

BfaceConfigManagePanel = Ext.extend(Ext.Window, {
    modal: true,
    shim: false,
    id: 'bfaceConfigManagePanel',
    width: 700,
    height: 510,
    title: '美艳到家全局配置',
    closable: false,
    maximizable: false,
    minimizable: false,
    border: false,
    buttonAlign: 'center',
    baseUrl: "systemConfig.java",
    show: function() {
        BfaceConfigManagePanel.superclass.show.call(this);
        Ext.Ajax.request({
            scope: this,
            url: this.baseUrl + "?cmd=list",
            success: function(data) {
                data = eval("(" + data.responseText + ")").result;
                if (data.length > 0) {
                    data = data[0]
                    for (var key in data) {
                        var curf = this.fp.form.findField(key + "");
                        if (curf) {
                            curf.setOriginalValue(data[key]);
                        }
                    }
                }
            }
        });
    },
    initComponent: function() {
        this.buttons = this.createButtons();
        BfaceConfigManagePanel.superclass.initComponent.call(this);
        this.buildFormItems();
        this.on('show', function() {
            this.fp.form.isValid()
        }, this);
    },
    buildFormItems: function() {
        this.fp = this.createFormPanel();
        this.add(this.fp);
    },
    createButtons: function() {
        var btns = [];
        btns.push({
            scope: this,
            text: '保存',
            iconCls: 'save',
            handler: this.submitSaveCon
        });
        btns.push({
            scope: this,
            text: '取消',
            iconCls: 'delete',
            handler: function() {
                this.hide();
            }
        });
        return btns
    },
    /**
     * 保存
     */
    submitSaveCon: function() {
        var id = this.fp.form.findField("id").getValue();
        var cmd = this.fp.form.findField("cmd");
        var method = id ? 'update' : 'save';
        cmd.setValue(method);
        this.fp.form.submit({
            scope: this,
            url: this.baseUrl,
            params: this.fp.form.getValues(),
            success: function(form, action) {
                Ext.Msg.alert('Success', "保存成功", function() {
                    this.close();
                }, this);
            }
        });
    },
    refurbishParentGrid: function() {
    },
    createFormPanel: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 120,
            fileUpload: true,
            autoScroll: true,
            labelAlign: 'right',
            defaults: {
                xtype: 'textfield'
                
            },
            items: [{
                name: "id",
                xtype: "hidden"
            }, {
                name: "cmd",
                xtype: "hidden"
            },{
                name: "jsonStrs",
                xtype: "hidden",
                value:"myPackageStr"
            },{
                xtype: "combo",
                name: "myPackageStr",
                hiddenName: "myPackageStr",
                fieldLabel: "选择模版",
                displayField: "title",
                valueField: "value",
                store: new Ext.data.SimpleStore({
                    fields: ['title', 'value'],
                    data: [["美艳模版1","meiyan"],["美艳模版2","meiyan2"]]
                }),
                editable: false,
                mode: 'local',
                triggerAction: 'all',
                emptyText: '请选择...'
            }]
        });
        var fp = formPanel.getForm();
        return formPanel;
    }
});
