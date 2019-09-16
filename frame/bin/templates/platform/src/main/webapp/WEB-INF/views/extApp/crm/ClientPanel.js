
ClientPanel=Ext.extend(Disco.Ext.CrudPanel,{
    id:"clientPanel",
    baseUrl:"client.java",
    pageSize : 20,
   /* batchRemoveMode : true,
    gridSelModel : 'checkbox',
    gridRowNumberer : true,*/
    viewWin : {
    	width : 538,
    	height : 340,
    	title : "客户信息"
    },
    createForm:function(){
	    var formPanel=new Ext.form.FormPanel({
	        frame:true,
	        labelWidth:70,
	        labelAlign:'right',
	        defaultType:'textfield',
            defaults:{width:200},
            items:[
            	{xtype:"hidden",name:"id"},
			    Disco.Ext.Util.twoColumnPanelBuild(
			    {fieldLabel:'编号',name:'sn',allowBlank:false},
			    {fieldLabel:'客户名称',name:'name',allowBlank:false},
			    {fieldLabel:'简写',name:'shortName'},
			    ConfigConst.BASE.getDictionaryCombo("trade","行业","ClientTrad"),
			    ConfigConst.BASE.getDictionaryCombo("source","来源","ClientSource"),
			  //  Ext.apply({},{fieldLabel:'业务员',name:'seller',hiddenName:"seller"},ConfigConst.CRM.user),
			    {fieldLabel:'公司地址',name:'address'},
			    {fieldLabel:'邮政编码',name:'zip'},
			    {fieldLabel:'联系电话',name:'tel'},
			    {fieldLabel:'传真号码',name:'fax'},
			    {fieldLabel:'联系人',name:'linkMan'},
			    {fieldLabel:'电子邮件',name:'email'},
			    Ext.apply({},{fieldLabel:'是否共享',name:'shared',hiddenName:"shared"},ConfigConst.BASE.yesNo)),
			    {xtype:"textarea",fieldLabel:'厂商简介',name:'intro',anchor:"-20"}
			   ]
	    });
        return formPanel;
    },
    createWin:function(callback, autoClose)
    {
        return this.initWin(538,340,"客户信息录入",callback, autoClose);
    },
    storeMapping:["id","sn","name","shortName","address","zip","tel","fax","linkMan","email","intro","lastVisitTime","hot","hotInfo","haveLost","lostTime","lostReason","satisfaction","loyal","customerValue","shared","source","trade","seller","inputTime"],
    /**
     * 设置是否是热点客户
     */
    onHot:function(){
    	var records = this.grid.getSelections();
    	if(records && records.length){
    		Ext.Ajax.request({
	    		scope : this ,
	    		url  : this.formatUrl('hot') ,
	    		params : {
	    			id : records[0].get('id'),
	    			hot : !records[0].get('hot')
	    		},
	    		success : function(response,options){
	    			var ret = Ext.decode(response.responseText);
	    			if(Ext.getObjVal(ret,'success')){
	    				//操作成功刷新数据
	    				Disco.Ext.Msg.alert("操作成功!","操作提示",this.refresh,this);
	    			}
	    		}
	    	});
    	}
    },
    onRowSelection:function(record,index,sel){
    	ClientPanel.superclass.onRowSelection.apply(this,arguments);
    	var record =this.grid.getSelectionModel().getSelected();
    	if(record){
    		if(record.get('hot')){
    			this.hideOperaterItem('hot');
    			this.showOperaterItem('unHot');
    		}else{
    			this.showOperaterItem('hot');
    			this.hideOperaterItem('unHot');
    		}
    	}else{
    		this.hideOperaterItem('hot','unHot');
    	}
    },
	initComponent : function(){
		//向grid当中输入两个按钮
		/*this.gridButtons = [{
			scope : this,
			text : '热点客户',
			id : 'hot',
			hidden : true,
			handler : this.onHot,
			showInToolbarOnly : true,
			icon : 'images/icons/heart.png'
		},{
			id : 'unHot',
			text : '非热点客户',
			hidden : true,
			scope : this,
			handler : this.onHot,
			showInToolbarOnly : true,
			icon : 'images/icons/heart_delete.png'
		},{
			text : '过滤数据',			
			scope : this,
			menu : {
				listeners : {
					scope : this,
					itemclick:function(item){
						
					}
				},
				items :  [{text : '取消过滤',itemId : 'cancel'},{text : '热点客户',itemId : 'hot'},{text : '非热点客户',itemId : 'unHot'}]
			},
			iconCls : 'search-icon',
			//handler : this.onHot,
			showInToolbarOnly : true
		}];*/
		var comboxRender = Disco.Ext.Util.comboxRender;
		var dateRender = Ext.util.Format.dateRenderer('Y-m-d H:i:s');
	    this.cm=new Ext.grid.ColumnModel([
					{header: "编号", sortable:true,width: 300, dataIndex:"sn"},
					{
						header: "<img src='images/icons/heart.png'/>", 
						sortable:true,width: 100, dataIndex:"hot",
						renderer:function(v){
							return v ? "<img src='images/icons/heart.png'/>" : '';
						}
					},
					{header: "厂家名称", sortable:true,width: 300, dataIndex:"name"},
					{header: "厂商缩写", sortable:true,width: 300, dataIndex:"shortName"},
					{header: "公司地址", sortable:true,width: 300, dataIndex:"address"},
					{header: "邮政编码", sortable:true,width: 300, dataIndex:"zip"},
					{header: "联系电话", sortable:true,width: 300, dataIndex:"tel"},
					{header: "传真号码", sortable:true,width: 300, dataIndex:"fax"},
					{header: "联系人", sortable:true,width: 300, dataIndex:"linkMan"},
					{header: "电子邮件", sortable:true,width: 300, dataIndex:"email"},
					{header: "行业", sortable:true,width: 300, dataIndex:"trade",renderer:comboxRender},
					{header: "来源", sortable:true,width: 300, dataIndex:"source",renderer:comboxRender},
					{header: "厂商简介", sortable:true,width: 300, dataIndex:"intro"},
					{header: "录入时间", sortable:true,width: 300, dataIndex:"inputTime",renderer:dateRender}
	    ]);
	    ClientPanel.superclass.initComponent.call(this);
	}     
});