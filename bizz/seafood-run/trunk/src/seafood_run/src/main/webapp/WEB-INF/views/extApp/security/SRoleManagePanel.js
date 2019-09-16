//角色控制
if (typeof Global === "undefined") {
	Global = {};
}
Global.sMenuTreeLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",
	url : "sMenu.java?cmd=getTree",
	listeners : {
		'beforeload' : function(treeLoader, node) {
			treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id: "");
			if (typeof node.attributes.checked !== "undefined") {
				treeLoader.baseParams.checked = false;
			}
		}
	}
});
SRoleManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
    id: "sRoleManagePanel",
    baseUrl: "sRole.java",
    gridSelModel: 'checkbox',
    pageSize: 20,
    showView: false,
    status:[["启用","1"],["禁用","0"]],
    statusFormat:function(v){
    	if(v ==1){
    		return "启用";
    	}else{
    		return "停用";
    	}
    },
    deletepermissincheckele:function(){
    	this.fp.items.items[0].items.items[1].items.items[1].removeAll();
        this.fp.items.items[0].items.items[1].items.items[2].removeAll();
        this.win.un("beforehide",this.deletepermissincheckele,this);
    },
    edit: function() {
        var win = SRoleManagePanel.superclass.edit.call(this);
        if(!this.win.hasListener("beforehide")){
        	this.win.on("beforehide",this.deletepermissincheckele,this)
        }
        if(this.systemPersissionItems.length<1){
        	Ext.Ajax.request({
        		method:"post",
    			url:"sRole.java?cmd=systemPermissions",
    			success:function(response){
    				this.systemPersissionItems = eval("("+response.responseText+")");
    				this.innitSystemPermission();
    			},
    			scope:this
        	});
        }else{
        	this.innitSystemPermission();
        }
    },
    systemPersissionItems:[],
    innitSystemPermission:function(){
    	if(this.systemPersissionItems.length<1){
    		return;
    	}
    	var systemps = this.fp.items.items[0].items.items[1].items.items[2];
    	var record = this.grid.getSelectionModel().getSelected();
    	var sPermissions = record.get('sPermissions');
    	var roleId = record.get("id");
    	for(var i = 0;i<this.systemPersissionItems.length;i++){
    		var curcheckele = this.systemPersissionItems[i];
    		curcheckele.roleId = roleId;
    		if(sPermissions.indexOf(curcheckele.id)!=-1){
    			curcheckele.checked=true;
    		}else{
    			curcheckele.checked=false;
    		}
    		var checkboxele = new Ext.form.Checkbox(curcheckele);
    		checkboxele.on("check",this.sPermissionsSave,this)
    		systemps.add(checkboxele);
		}
    	this.fp.doLayout();
    },
    // 保存权限信息
    sPermissionsSave:function(ele){
    	if(!ele.roleId){
    		return;
    	}
    	Ext.Ajax.request({
			method:"post",
			url:"sRole.java?cmd=update",
			params:{
				roleId:ele.roleId,
				id:ele.id,
				savetype:"permission",
				checked:ele.checked
			},
			success:function(response){
				var record = this.grid.getSelectionModel().getSelected();
				var sPermissions = record.get('sPermissions');
				if(ele.checked){
					sPermissions.push(ele.id)
				}else{
					for(var i = 0;i<sPermissions.length;i++){
						if(sPermissions[i]==ele.id){
							sPermissions.splice(i,1);
							return;
						}
					}
				}
			},
			scope:this
		});
    },
    // 树节点点击监听函数
    menuTreeListener:function(node){
		var roleId = this.fp.form.findField("id").getValue();
		if(!roleId){
			return
		}
		var menuId = node.id;
		var record = this.grid.getSelectionModel().getSelected();
		var sPermissions = record.get('sPermissions');
		var spermission = this.fp.items.items[0].items.items[1].items.items[1];
		spermission.removeAll();
		var menuPermissions = node.attributes.sPermission;
		for(var i =0;i<menuPermissions.length;i++){
			var checked=false;
			if(sPermissions.indexOf(menuPermissions[i].id)!=-1){
				checked=true;
			}
			var checkboxele = new Ext.form.Checkbox({
				id:menuPermissions[i].id,
				boxLabel:menuPermissions[i].name,
				roleId:roleId,
				checked:checked
			});
			checkboxele.on("check",this.sPermissionsSave,this)
			spermission.add(checkboxele);
		}
		this.fp.doLayout();
    },
    createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : false,
			labelWidth : 80,
			labelAlign : 'right',
			layout : "fit",
			items : [{
				xtype:'tabpanel',
				deferredRender : false,
				monitorResize:true,
				border:true,
				activeTab:0,
				items:[{title:'角色基本信息',frame: true,border:false,layout:'form', defaults: {
	                width: 320,
	                xtype: "textfield"
	            },
	            items:[{
						xtype : "hidden",
						name : "id"
					},{
		                fieldLabel: "角色名",
		                name: "rName",
		                emptyText: '角色名不能为空',
						allowBlank: false,
						blankText: '角色名不能为空'
		            },{
		                fieldLabel: "角色代码",
		                name: "code",
		                emptyText: '角色代码不能为空',
						allowBlank: false,
						blankText: '角色代码不能为空'
		            },{
		                fieldLabel: '启用状态',
		                xtype: 'radiogroup',
		                name: 'status',
		                items: [{
		                    checked: true,
		                    boxLabel: '启用',
		                    name: 'status',
		                    inputValue: "1"
		                }, {
		                    boxLabel: '禁用',
		                    name: 'status',
		                    inputValue: "0"
		                }]
		            },{
		                xtype: "textarea",
		                fieldLabel: "描述",
		                name: "description",
		                height: 50
		            }]},
		            {
						title:'角色权限',
						layout:"border",
						items:[new Ext.tree.TreePanel({
							title : "菜单信息",
							autoScroll : true,
							region:"west",
							width:200,
							margins : '0 2 0 0',
							tools : [ {
								id : "refresh",
								handler : function() {
									this.tree.root.reload();
								},
								scope : this
							} ],
							root : new Ext.tree.AsyncTreeNode({
								id : "root",
								text : "所有菜单",
								iconCls : 'treeroot-icon',
								expanded : true,
								loader : Global.sMenuTreeLoader
							}),
							listeners:{
								click:this.menuTreeListener,
								scope:this
							}
						}),{id:"sPermission",
							title:"资源权限",
							region:"center",
							defaults:{
								xtype:"checkbox",
								width:100
						}},{
							id:"systemPermission",
							title:"系统权限",
							width:110,
							region:"east",
							defaults:{
								xtype:"checkbox",
								width:100
							}
						}]
					}]
			}]
		});
		return formPanel;
	},
    createWin: function() {
        return this.initWin(448, 400, "角色管理");
    },
    storeMapping: ["id", "rName", "createDate", "description","sequence","status","sPermissions","code"],
    initComponent: function() {
        this.cm = new Ext.grid.ColumnModel([{
            header: "名称",
            sortable: true,
            width: 100,
            dataIndex: "rName"
        },{
            header: "角色代码",
            sortable: true,
            width: 100,
            dataIndex: "code"
        },{
            header: "创建日期",
            sortable: true,
            width: 100,
            dataIndex: "createDate",
            renderer: this.dateRender()
        },{
            header: "启用状态",
            sortable: true,
            width: 100,
            dataIndex: "status",renderer:this.statusFormat
        }]);
        SRoleManagePanel.superclass.initComponent.call(this);
    }
});
