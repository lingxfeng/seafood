if (typeof Global === "undefined") {
    Global = {};
}

SiteConfigManagePanel = Ext.extend(Ext.Window, {
    modal: true,
    shim: false,
    id: 'siteConfigManagePanel',
    width: 500,
    height: 420,
    title: '站点配置',
    closable: false,
    maximizable: false,
    minimizable: false,
    border: false,
    buttonAlign: 'center',
    baseUrl: "systemConfig.java",
    show: function() {
    	SiteConfigManagePanel.superclass.show.call(this);
    	Ext.Ajax.request({
            scope: this,
            url: this.baseUrl + "?cmd=list",
            success: function(data) {
                data = eval("("+data.responseText+")").result;
                if(data.length>0){
                	data = data[0]
                	for(var key in data){
                    	var curf = this.fp.form.findField(key+"");
                    	if(curf){
                    		curf.setOriginalValue(data[key]);
                    	}
                    }
                }
            }
        });
    },
    initComponent: function() {
        this.buttons = this.createButtons();
        SiteConfigManagePanel.superclass.initComponent.call(this);
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
     * 保存文章
     */
    submitSaveCon: function() {
        this.beforeFormSubmit('save');
    },
    beforeFormSubmit: function(method) {
        var id = this.fp.form.findField("id").getValue();
        method = id ? 'update' : 'save';
        Ext.Ajax.request({
        	scope: this,
        	method:"POST",
            url: this.baseUrl + "?cmd="+method,
            params: this.fp.form.getValues(),
            success: function(data,options) {
            	var id = options.params.id;
            	this.fp.form.findField("id").setOriginalValue(id);
            	this.hide();
            }
        });
    },
    refurbishParentGrid: function() {
    },
    createFormPanel: function() {
        var formPanel = new Ext.form.FormPanel({
            frame: true,
            labelWidth: 70,
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
            	width: 400,
                fieldLabel: "站点名称",
                name: "siteName"
            }, {
            	width: 400,
                fieldLabel: "域名",
                name: "domain"
            },  {
            	width: 400,
                fieldLabel: "站长",
                name: "webmaster"
            }, {
            	width: 400,
                fieldLabel: "邮箱",
                name: "email"
            },{
            	width: 400,
                fieldLabel: "备案号",
                name: "icp"
            },  {
            	width: 400,
                fieldLabel: "关键字",
                name: "keywords"
            },  {
            	width: 400,
            	 xtype: "textarea",
                fieldLabel: "版权信息",
                name: "copyright"
            },  {
            	width: 400,
            	 xtype: "textarea",
                fieldLabel: "站点描述",
                name: "description"
            }]
        });
        var fp = formPanel.getForm();
        return formPanel;
    }
});
