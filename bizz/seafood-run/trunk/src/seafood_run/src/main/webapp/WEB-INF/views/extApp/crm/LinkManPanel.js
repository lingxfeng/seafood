
LinkManPanel=Ext.extend(Disco.Ext.CrudPanel,{
    id:"linkManPanel",
    baseUrl:"linkMan.java",
    pageSize:30,
    createForm:function(){
		    var formPanel=new Ext.form.FormPanel({
		        frame:true,
		        labelWidth:70,
		        labelAlign:'right',
		        defaultType:'textfield',
		        defaults:{width:200},
		        items:[{xtype:"hidden",name:"id"},
	        		Disco.Ext.Util.columnBuild(
	        			{fieldLabel:'姓名',name:'trueName',allowBlank:false},
					    {fieldLabel:'称呼',name:'appellation'},
					    {fieldLabel:'性别',name:'sex'},
					    {fieldLabel:'电话',name:'tel'},
					    {fieldLabel:'移动电话',name:'mobile'},
					    {fieldLabel:'部门',name:'dept'},
					    {fieldLabel:'职务',name:'duty'},
					    {fieldLabel:'传真号码',name:'fax'},
					    {fieldLabel:'电子邮件',name:'email'},
					    {fieldLabel:'生日',name:'birthday',xtype:'datefield',format:'Y-m-d',maxValue:new Date()},
					    {fieldLabel:'个人电话',name:'homeTel'},
					    {fieldLabel:'QQ',name:'qq'},
					    {fieldLabel:'Msn',name:'msn'},
					  //  {fieldLabel:'natives',name:'natives'},
					    {fieldLabel:'国家',name:'nations'},
					    {fieldLabel:'爱好',name:'interest'},
					    {fieldLabel:'学位',name:'degree'},
					    {fieldLabel:'个人主页',name:'subject'},
					    {fieldLabel:'学习',name:'school'}
	        		)
		        ]
		   	 });
        return formPanel;
    },
    createWin:function()
    {
        return this.initWin(438,310,"LinkMan管理");
    },
    storeMapping:["id","trueName","appellation","sex","tel","mobile","dept","duty","fax","email","birthday","homeTel","qq","msn","natives","nations","interest","degree","subject","school"    ],
    initComponent : function(){
	    this.cm=new Ext.grid.ColumnModel([
				{header: "称谓", sortable:true,width: 300, dataIndex:"appellation"},
				{header: "性别", sortable:true,width: 300, dataIndex:"sex"},
				{header: "电话", sortable:true,width: 300, dataIndex:"tel"},
				{header: "手机", sortable:true,width: 300, dataIndex:"mobile"},
				{header: "部门", sortable:true,width: 300, dataIndex:"dept"},
				{header: "职位", sortable:true,width: 300, dataIndex:"duty"},
				{header: "传真号码", sortable:true,width: 300, dataIndex:"fax"},
				{header: "电子邮件", sortable:true,width: 300, dataIndex:"email"},
				{header: "生日", sortable:true,width: 300, dataIndex:"birthday"},
				{header: "个人电话", sortable:true,width: 300, dataIndex:"homeTel"},
				{header: "QQ", sortable:true,width: 300, dataIndex:"qq"},
				{header: "Msn", sortable:true,width: 300, dataIndex:"msn"},
				//{header: "natives", sortable:true,width: 300, dataIndex:"natives"},
				{header: "国家", sortable:true,width: 300, dataIndex:"nations"},
				{header: "爱好", sortable:true,width: 300, dataIndex:"interest"},
				{header: "学位", sortable:true,width: 300, dataIndex:"degree"},
				{header: "个人主页", sortable:true,width: 300, dataIndex:"subject"},
				{header: "学校", sortable:true,width: 300, dataIndex:"school"}
	        ]);
	    LinkManPanel.superclass.initComponent.call(this);
	}     
});