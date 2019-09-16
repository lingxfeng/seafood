// 附件管理

MaiDouManagerPanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "maiDouManagerPanel",
    //title:"视频文件管理",
    baseUrl: "member.java",
    gridSelModel: 'checkbox',
    baseQueryParameter: {
        type: 0
        //文件类型的
    },
    pageSize: 20,
    batchRemoveMode: false,
    showAdd: false,
    showEdit: false,
    showView: false,
    exportMaiDouUserInfo : function() {
    	window.location.href=this.baseUrl + "?cmd=export";
	},

    storeMapping: ["id", "name", "trueName", "email", "sex", "mobileTel","inviteCode","registerTime"],
    initComponent: function() {
    	
        this.cm = new Ext.grid.ColumnModel([{
            header: "登录名",
            sortable: true,
            width: 100,
            dataIndex: "name"
        }, {
            header: "真实姓名",
            sortable: true,
            width: 100,
            dataIndex: "trueName"
        }, {
            header: "邮箱",
            sortable: true,
            width: 100,
            dataIndex: "email",
            renderer: this.topicRender
        }, {
            header: "性别",
            sortable: true,
            width: 50,
            dataIndex: "sex"
        }, {
            header: "手机号码",
            sortable: true,
            width: 50,
            dataIndex: "mobileTel"
        }, {
            header: "体验卡激活码",
            sortable: true,
            width: 50,
            dataIndex: "inviteCode"
        }, {
            header: "注册日期",
            sortable: true,
            width: 50,
            dataIndex: "registerTime",
            renderer: this.dateRender()
        }])
        this.gridButtons = [ 
			{
			text : "导出",
			iconCls: "upload-icon",
			handler : this.exportMaiDouUserInfo,
			scope : this
		}];
        
        
        MaiDouManagerPanel.superclass.initComponent.call(this);
    }
});
