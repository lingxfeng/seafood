//分销商销售订单
OrdersPanel=Ext.extend(BaseProductBillPanel,{
    id:"ordersPanel",
    //title:"材料单管理",
    baseUrl:"orders.ejf",
    gridForceFit:true,
    viewWin:{width:738,height:600,title:"销售订单详情"},
    baseQueryParameter:{types:5},
    onCreate:function(){
        OrdersPanel.superclass.onCreate.call(this);
        this.fp.form.findField("buyTime").clearDirty();
    },
    createViewPanel:function(){
        this.createViewGrid();
        var formPanel=new Ext.form.FormPanel({
            frame:false,
            labelWidth:70,
            labelAlign:'right',
            items:[{
                xtype:'panel',
                //title:'基本信息',
                style:"margin-bottom:5px;margin-top:10px",
                border:false,
                autoHeight:true,
                items:[
                        {xtype:"hidden",name:"id"},
                        Disco.Ext.Util.columnPanelBuild(
                        {columnWidth:.2,items:{xtype:"labelfield",fieldLabel:'编号',name:'sn',allowBlank:false,readOnly:true}},
                        {columnWidth:.3,items:{xtype:"labelfield",fieldLabel:'客户',name:'client',renderer:this.objectRender("name")}},
                        {columnWidth:.5,items:  
                         Disco.Ext.Util.columnPanelBuild(
                             {columnWidth:.31,items:
                        {xtype:"labelfield",fieldLabel:'报价时间',name:'vdate',anchor:"-1",renderer:this.dateRender("Y-m-d")}},
                        {columnWidth:.23,items:
                        {xtype:"labelfield",fieldLabel:'币种',name:'moneyType'}
                        },
                        {columnWidth:.23,items:{xtype:"labelfield",fieldLabel:'税率',name:'taxRate',anchor:"-1"}},
                        {columnWidth:.23,items:{xtype:"labelfield",fieldLabel:'是否含税',name:'includeTax'}})}),
                Disco.Ext.Util.columnPanelBuild(
                    {
                    columnWidth : .2,
                    items : {xtype:"labelfield",fieldLabel:'联系人',name:'linkMan',renderer:this.objectRender("trueName")}},
                    {columnWidth:.3,items:{xtype:"labelfield",anchor:"-1",fieldLabel:'联系地址',name:'address'}},   
                    {columnWidth:.15,items:{xtype:"labelfield",anchor:"-1",fieldLabel:'联系电话',name:'tel'}},
                    {columnWidth:.35,items:{xtype:"labelfield",anchor:"-1",fieldLabel:'备注',name:'remark'}}      
                    )]
            },
            {
                xtype:'panel',
                autoHeight:true,
                items:this.viewGrid
            },{
                xtype:'fieldset',
                height:50,
                border:false,
                items : [
                Disco.Ext.Util.buildColumnForm(true,{
                    xtype : "labelfield",
                    fieldLabel : '业务员',
                    name : "seller",
                    renderer:this.objectRender("trueName")
                }, {
                    xtype : "labelfield",
                    fieldLabel : '制单',
                    name : "inputUser",
                    value : {trueName:"$!{session.EASYJF_SECURITY_USER.trueName}"},
                    renderer:this.objectRender("trueName")
                }, {
                    xtype : "labelfield",
                    fieldLabel : '审核',
                    name : "auditor",
                    renderer:this.objectRender("trueName"),
                    listeners:{render:function(c){
                    var height=this.viewPanel.el.getBox().height-(this.viewPanel.getComponent(0).el.getBox().height+this.viewPanel.getComponent(2).el.getBox().height+15);
                    this.viewGrid.setHeight(height);
               	 },delay: 200,scope:this}
              })]
            }]
        });
            return formPanel;
    },
    createForm : function(){
    	var editGrid =this.createEditGrid();
    	var objectRender = this.objectRender('name');
    	var formPanel = new Disco.Ext.CascadeForm({
    		//frame:true,
            enterNavigationKey:true,
           	navigationSequences:["client","deliveryTime",this.editGrid.id],
            tbar : this.createFormToolBar(),
			buildNorthForm : function(){
				var Uitl = Disco.Ext.Util;
				return {
					height:70,
					frame:true,
					border:false,
					layout:'form',
					style:'padding:2px;',
					bodyStyle:'padding:5px;',
					defaults:{anchor:'-20'},
					items : [
						{xtype:"hidden",name:"id"},
                        Uitl.buildColumnForm(4,
	                        {fieldLabel:'编号',name:'sn',allowBlank:false,readOnly:true},
	                        Ext.apply({},{
	                        	allowBlank:false,
	                        	choiceValue:function(o){
	                        		console.debug(o);
		                        	var fp = formPanel.form;
		                            if(!fp.findField("consigneeAddress").getValue()){
		                                fp.findField("consigneeAddress").setValue(o.address||"");
		                            }
		                            if(!fp.findField("consigneePhone").getValue()){
		                                fp.findField("consigneePhone").setValue(o.tel||"");
		                            }
	                        	}.createDelegate(this)
	                       	},ConfigConst.CRM.client),
	                        {xtype:"datefield",fieldLabel:'下单时间',name:'buyTime',format:"Y-m-d",allowBlank:false,value:new Date()},
	                        {xtype:"datefield",fieldLabel:'交货时间',name:'deliveryTime',format:"Y-m-d",allowBlank:false}
                        ),
		                Uitl.buildColumnForm(3,
		               		 {panelCfg:{columnWidth:.25},fieldLabel:'联系电话',name:'consigneePhone',allowBlank:false},
		               		 {panelCfg:{columnWidth:.25},fieldLabel:'联系地址',name:'consigneeAddress',allowBlank:false},
		               		 {panelCfg:{columnWidth:.5},fieldLabel:'备注',name:'remark',allowBlank:false}
		                )
					]
				}
			},
			buildSouthForm : function(){
				return {
	                height:45,
	                border:false,
	                frame:true,
	                region:"south",
	                style :'padding:1px;',
	                items : [
		                Disco.Ext.Util.twoColumnPanelBuild(3,Ext.apply({}, {
		                    fieldLabel : '业务员'
		                }, ConfigConst.CRM.seller), {
		                    xtype : "labelfield",
		                    fieldLabel : '制单',
		                    name : "inputUser",
		                    value : {trueName:"$!{session.EASYJF_SECURITY_USER.trueName}"},
		                    renderer:objectRender
		                }, {
		                    xtype : "labelfield",
		                    fieldLabel : '审核',
		                    name : "auditor",
		                    renderer:objectRender
		                })
	                ]
	            }
			},
			buildContentForm : function(){
    			return editGrid;
    		}
    	});
    	return formPanel;
    },
    createWin:function(callback,autoClose){
    	var vs = Ext.getBody().getViewSize();
        return this.initWin(vs.width-20,vs.height-20,"销售订单",callback,autoClose);
    },
    storeMapping:["id","sn","types","user","client","totalSum","moneyType","buyTime","deliveryTime","consigneeAddress","consigneeName","consigneeZipCode","consigneePhone","isInvoice","invoiceContent","consigneeEmail","consigneeMobile","payType","payment","deliveryType","delivery","ip","remark","status","storefront","seller","inputUser","inputTime","status","items","auditor","distributor"],
    initEditGridListeners:function(){
    	this.baseEditGridListeners = {
    		scope : this,
    		afteredit : function(e){
    			var fields = ['price','num'];
    			if(fields.contains(e.field)){
    				//superclass
    				var record = e.record ;
    				this.autoCountData(record);
    			}
    		}
    	}
    },
    initComponent : function(){
    	this.initEditGridListeners();
        this.cm=new Ext.grid.ColumnModel([
            {header: "编号", sortable:true,width: 80, locked:true,dataIndex:"sn"},
            {header:"客户名称", sortable:true,width:120, locked:true, dataIndex:"client",renderer:this.objectRender("name")},
            {header:"业务员", sortable:true,width: 60, dataIndex:"seller",renderer:this.objectRender("trueName")},
            {header: "金额", sortable:true,width: 80, dataIndex:"totalSum"},
            {header: "下单时间", sortable:true,width: 90, dataIndex:"buyTime",renderer:this.dateRender("Y-m-d")},
            {header: "交货时间", sortable:true,width: 90, dataIndex:"deliveryTime",renderer:this.dateRender("Y-m-d")},
            {header: "订单类型", sortable:true,hidden:true,width: 90, dataIndex:"types",renderer:this.objectRender("title")},
            {header: "备注", sortable:true,width: 200, dataIndex:"remark"},
            {header: "传真", sortable:true,width: 80, dataIndex:"fax",hidden:true},
            {header: "地址", sortable:true,width: 120, dataIndex:"address",hidden:true},
            {header: "邮编", sortable:true,width: 60, dataIndex:"zip",hidden:true},
            {header: "状态", sortable:true,width: 60, dataIndex:"status",renderer:this.statusRender(this.status)},
            {
                header : "操作",
                sortable : true,
                width : 100,
                dataIndex : "status",
                renderer:this.operateRender
            }
        ]);
        OrdersPanel.superclass.initComponent.call(this);
    }     
});