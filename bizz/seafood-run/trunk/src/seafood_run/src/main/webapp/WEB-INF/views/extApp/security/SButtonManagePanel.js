// 按钮管理
SButtonManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "sButtonManagePanel",
    // title:"网站发布管理",
    baseUrl: "sButton.java",
    gridSelModel: 'checkbox',
    batchRemoveMode: true,
    pageSize: 20,
    showView: false,
    edit: function() {
        var win = SButtonManagePanel.superclass.edit.call(this);
    },
    create:function(){
    	var win = SButtonManagePanel.superclass.create.call(this);
    },
    buttonTypeFormat:function(v){
    	if(v ==1){
    		return "系统操作";
    	}else{
    		return "资源操作";
    	}
    },
    logoImgRender: function(value, p, record) {
		if(value !=null && value !=""){
			return String.format('{1}<b><a style="color:green" href="'+value+'" target="_blank">&nbsp查看</a></b><br/>', "", "")
		}       
    },
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
                fieldLabel: "按钮名称",
                name: "name",
                emptyText: '按钮名称',
				allowBlank: false,
				blankText: '按钮名称'
            },  {
                fieldLabel: "按钮代码",
                name: "code",
                emptyText: '按钮代码不能为空',
				allowBlank: false,
				blankText: '按钮代码不能为空'
            },{
                fieldLabel: "按钮路径",
                name: "url",
                emptyText: '按钮路径不能为空',
				allowBlank: false,
				blankText: '按钮路径不能为空'
            },{
                fieldLabel: '按钮类型',
                xtype: 'radiogroup',
                name: 'type',
                value:"0",
                items: [{
                    boxLabel: '资源操作',
                    name: 'type',
                    inputValue: "0"
                },{
                    checked: true,
                    boxLabel: '系统操作',
                    name: 'type',
                    inputValue: "1"
                }]
            }]
        });
        return formPanel;
    },
    createWin: function() {
        return this.initWin(448, 400, "按钮管理");
    },
    storeMapping: ["id", "name", "url",  "code","description", "sequence","type"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "名称",
            sortable: true,
            width: 100,
            dataIndex: "name"
        }, {
            header: "按钮代码",
            sortable: true,
            width: 100,
            dataIndex: "code"
        }, {
            header: "按钮访问路径",
            sortable: true,
            width: 100,
            dataIndex: "url"
        },{
            header: "按钮类型",
            sortable: true,
            width: 100,
            dataIndex: "type",
            renderer: this.buttonTypeFormat
        }]);
        SButtonManagePanel.superclass.initComponent.call(this);
    }
});
