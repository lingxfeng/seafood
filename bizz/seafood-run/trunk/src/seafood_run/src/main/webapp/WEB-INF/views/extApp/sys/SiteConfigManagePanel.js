if (typeof Global === "undefined") {
    Global = {};
}

MedConfigManagePanel = Ext.extend(Ext.Window, {
    modal: true,
    shim: false,
    id: 'medConfigManagePanel',
    width: 700,
    height: 510,
    title: '站点全局配置',
    closable: false,
    maximizable: false,
    minimizable: false,
    border: false,
    buttonAlign: 'center',
    baseUrl: "systemConfig.java",
    show: function() {
        MedConfigManagePanel.superclass.show.call(this);
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
        MedConfigManagePanel.superclass.initComponent.call(this);
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
                value:"yqhyjf,bqxxjf,mrqdjf,ljqdjf07,ljqdjf15,ljqdjf30,lxqdjf03,lxqdjf07"
            }, Disco.Ext.Util.twoColumnPanelBuild({
                width: 400,
                fieldLabel: "邀请好友积分",
                name: "yqhyjf"
            }, {
                width: 400,
                fieldLabel: "补全信息积分",
                name: "bqxxjf"
            }, {
                width: 400,
                fieldLabel: "每日签到积分",
                name: "mrqdjf"
            }, {
                width: 400,
                fieldLabel: "累计签到7天积分",
                name: "ljqdjf07"
            }, {
                width: 400,
                fieldLabel: "累计签到15天积分",
                name: "ljqdjf15"
            }, {
                width: 400,
                fieldLabel: "累计签到30天积分",
                name: "ljqdjf30"
            }, {
                width: 400,
                fieldLabel: "连续签到3天积分",
                name: "lxqdjf03"
            }, {
                width: 400,
                fieldLabel: "连续签到7天积分",
                name: "lxqdjf07"
            })]
        });
        var fp = formPanel.getForm();
        return formPanel;
    }
});
