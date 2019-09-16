
ProductPanel=Ext.extend(Disco.Ext.CrudListPanel,{
    id:"productPanel",
    baseUrl:"product.ejf",
    viewWin:function(){
    	var size = this.getDefaultWinSize();
    	return {
    		width : size.width ,
    		height : size.height,
    		title : '产品详情'
    	}
    },
    createForm:function(){
	    var formPanel=new Ext.form.FormPanel({
	        frame:true,
	        labelWidth:70,
	        fileUpload:true,
	        labelAlign:'right',
	        items:[
	       		{xtype:"hidden",name:"id"},
	  			Disco.Ext.Util.twoColumnPanelBuild(
				    {fieldLabel:'产品名称',name:'title',allowBlank:false},
				    {fieldLabel:'产品编号',name:'sn',allowBlank:false},
				    ConfigConst.BASE.getDictionaryCombo("dir","分类","ProductDir",null,false),
				    ConfigConst.BASE.getDictionaryCombo("brand","品牌","ProductBrand"),
				    ConfigConst.BASE.getDictionaryCombo("unit","计量单","ProductUnit"),
				    {fieldLabel:'规格',name:'spec'},
				    {fieldLabel:'型号',name:'model'},
				    {fieldLabel:'等级',name:'lev'},
				    {fieldLabel:'进价',name:'buyPrice',xtype:"numberfield"},
				    {fieldLabel:'销售价',name:'salePrice',xtype:"numberfield"}
		   		),
	   		{fieldLabel:'产品描述',xtype:"textarea",name:'content',anchor:"-20",height:80}
	     ]
	    });
        return formPanel;
    },
    searchFormPanel : function(){
    	var form = new Ext.FormPanel({
    		items : [
    			{}
    		]
    	});
    	return form;
    },
    createWin:function(callback, autoClose){
    	var size = this.getDefaultWinSize();
        return this.initWin(size.width,size.heigth,"产品信息录入",callback, autoClose);
    },
    getDefaultWinSize : function(){
    	return {
    		width : 538,
    		heigth : 300
    	}
    },
    storeMapping:["id","title","sn","spec","model","lev","buyPrice","salePrice","content","dir","brand","unit"],
    initComponent : function(){
    this.cm=new Ext.grid.ColumnModel([
		{header: "产品名称", sortable:true,width: 100, dataIndex:"title"},
		{header: "产品编号", sortable:true,width: 100, dataIndex:"sn"},
        {header: "分类", sortable:true,width: 100, dataIndex:"dir",renderer:this.objectRender("title")},
        {header: "品牌", sortable:true,width: 100, dataIndex:"brand",renderer:this.objectRender("title")},
        {header: "单位", sortable:true,width: 100, dataIndex:"unit",renderer:this.objectRender("title")},
		{header: "规格", sortable:true,width: 100, dataIndex:"spec"},
		{header: "型号", sortable:true,width: 100, dataIndex:"model"},
		{header: "等级", sortable:true,width: 100, dataIndex:"lev"},
		{header: "进价", sortable:true,width: 100, dataIndex:"buyPrice"},
		{header: "销售价", sortable:true,width: 100, dataIndex:"salePrice"},
		{header: "简介", sortable:true,width: 300, dataIndex:"content"}
        ]);
    ProductPanel.superclass.initComponent.call(this);
}     
});