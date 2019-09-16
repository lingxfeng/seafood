//菜单树
Global.menuTreeLoader = new Ext.tree.TreeLoader({
    iconCls: 'disco-tree-node-icon',
    varName: "Global.DEPT_DIR_LOADER",// 缓存Key
    url: "sMenu.java?cmd=getTree",
    listeners: {
        'beforeload': function(treeLoader, node) {
            treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : "");
            if (typeof node.attributes.checked !== "undefined") {
                treeLoader.baseParams.checked = false;
            }
        }
    }
});
SMenuGridManagePanel = Ext.extend(Disco.Ext.CrudPanel, {
	id : "sMenuGridManagePanel",
	baseUrl : "sMenu.java",
	gridSelModel : 'checkbox',
	batchRemoveMode : true,
	dirtyFormCheck:false,
	pageSize : 20,
	showView : false,
	create:function(){
		SMenuGridManagePanel.superclass.create.call(this);
		this.fp.form.findField("parent").setValue(this.parent);
		var tabs = this.fp.items.items[0];
		if(tabs.items.items[1]){
			tabs.remove(tabs.items.items[1]);
		}
	},
	edit:function(){
		SMenuGridManagePanel.superclass.edit.call(this);
		var tabs = this.fp.items.items[0];
		if(tabs.items.items[1]){
			tabs.remove(tabs.items.items[1]);
		}
		Ext.Ajax.request({
			method:"post",
			url:"sButton.java?cmd=getTree",
			success:function(response){
				this.sPermissons.items = eval("("+response.responseText+")");
				this.setCheckbox();
			},
			scope:this
		});
	},
	setCheckbox:function(){
		var tabs = this.fp.items.items[0];
		var record = this.grid.getSelectionModel().getSelected();
		var sPermissions = record.get("sPermissions");
		var items = this.sPermissons.items;
		for(var i = 0;i<items.length;i++){
			for(var j = 0;j<sPermissions.length;j++){
				var buttonId = sPermissions[j].permissionstr.split(":")[1];
				if(buttonId == items[i].inputValue){
					items[i].checked=true;
					items[i].pId = sPermissions[j].id
				}
			}
		}
		tabs.add(this.sPermissons);
	},
	sPermissons:{
		xtype:"checkboxgroup",
		title:"菜单按钮",
		columns:3,
		fieldLabel:"所有按钮",
		defaults:{
			xtype:"checkbox",
			name:"sPermissionIds"
		},
		items:[]
	},
	save:function(){
		var tab2 = this.fp.items.items[0].items.items[1];
		if(tab2){
			var sBIdsAdd ="";
			var sPIdsdel = "";
			var checkSPermissions = tab2.items.items;
			for(var i =0;i<checkSPermissions.length;i++){
				if(checkSPermissions[i].checked && !checkSPermissions[i].pId){
					sBIdsAdd = sBIdsAdd + checkSPermissions[i].inputValue+",";
				}else if(!checkSPermissions[i].checked && checkSPermissions[i].pId){
					sPIdsdel = sPIdsdel + checkSPermissions[i].pId+",";
				}
			}
			var sBIdsAddele = this.fp.form.findField("sBIdsAdd");
			if(!sBIdsAddele){
				sBIdsAddele = new Ext.form.Hidden({
	                name: "sBIdsAdd"
	            });
				this.fp.add(sBIdsAddele);
	            this.fp.doLayout();
			}
			sBIdsAddele.setValue(sBIdsAdd);
			var sPIdsdelele = this.fp.form.findField("sPIdsdel");
			if(!sPIdsdelele){
				sPIdsdelele = new Ext.form.Hidden({
					name: "sPIdsdel"
				});
				this.fp.add(sPIdsdelele);
				this.fp.doLayout();
			}
			sPIdsdelele.setValue(sPIdsdel);
		}
		SMenuGridManagePanel.superclass.save.call(this);
	},
	sMenuButton:{title:"操作"},
	status : [ [ "启用", "1" ], [ "禁用", "0" ] ],
	statusFormat : function(v) {
		if (v == 1) {
			return "启用";
		} else {
			return "停用";
		}
	},
	isSystemFormat:function(v){
		return v == "1"?"是":"否";
	},
	createForm : function() {
		var formPanel = new Ext.form.FormPanel({
			frame : false,
			labelWidth : 70,
			labelAlign : 'right',
			layout : "fit",
			items : [ {
				xtype : 'tabpanel',
				deferredRender : false,
				monitorResize : true,
				hideBorders : true,
				border : false,
				activeTab : 0,
				items : [ {
					title : '角色基本信息',
					frame : true,
					border : false,
					layout : 'form',
					defaults : {
						width : 400,
						xtype : "textfield"
					},
					items : [ {
						xtype : "hidden",
						name : "id"
					}, Disco.Ext.Util.twoColumnPanelBuild(
							{ 	
								fieldLabel: "菜单名称",
				                name: "title",
				                emptyText: '菜单名称不能为空',
								allowBlank: false,
								blankText: '菜单名称不能为空'
				            },{ 	
				            	fieldLabel: "菜单编号",
				                name: "sn",
				                emptyText: '菜单编号不能为空',
								allowBlank: false,
								blankText: '菜单编号不能为空'
				            },{ 
				            	fieldLabel: "后台处理类",
				                name: "actionClass",
				                emptyText: '后台处理类不能为空',
								allowBlank: false,
								blankText: '后台处理类不能为空'
				            },{ 
				            	fieldLabel: "应用程序类",
				                name: "appClass"
				            },{ 
				            	fieldLabel: "脚本地址",
				                name: "url"
				            },{ 
				            	fieldLabel: "所在包",
				                name: "pack"
				            },{
				                fieldLabel: '上级菜单',
				                name: 'parent',
				                xtype: "treecombo",
				                hiddenName: "parentId",
				                displayField: "title",
				                valueField: "id",
				                tree: new Ext.tree.TreePanel({
				                    autoScroll: true,
				                    root: new Ext.tree.AsyncTreeNode({
				                        id: "root",
				                        text: "所有栏目",
				                        expanded: true,
				                        iconCls: 'treeroot-icon',
				                        loader: Global.menuTreeLoader,
				                        types:"0"
				                    })
				                })
				            },{
				            	fieldLabel: "是否平台菜单",
				                name: "isSysTem",
				                xtype: 'radiogroup',
				                items: [{
				                    checked: true,
				                    boxLabel: '是',
				                    name: 'isSysTem',
				                    inputValue: "1"
				                }, {
				                    boxLabel: '否',
				                    name: 'isSysTem',
				                    inputValue: "0"
				                }]
				            
				            },{ 	
				            	fieldLabel: "启用状态",
				                name: "status",
				                xtype: 'radiogroup',
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
				            }
					) ]
				}]
			} ]
		});
		return formPanel;
	},
	createWin : function() {
		return this.initWin(550, 400, "角色管理");
	},
	storeMapping : [ "id", "title", "sn", "sequence", "actionClass",
			"appClass", "dirPath", "vrtype", "url", "otherScripts", "pack",
			"icon", "status", "parent", "isSysTem", "sPermissions"],
	reloadLeftTree : function() {
		if (this.tree) {
			this.tree.root.reload();
		}
		if (this.fp) {
			var parentNode = this.fp.form.findField("parent");
			if (parentNode && parentNode.tree.rendered) {
				parentNode.tree.root.reload();
			}
		}
	},
	initComponent : function() {
		this.cm = new Ext.grid.ColumnModel([{
			header : "名称",
			sortable : true,
			width : 100,
			dataIndex : "title"
		}, {
			header : "编号",
			sortable : true,
			width : 100,
			dataIndex : "sn"
		}, {
			header : "路径",
			sortable : true,
			width : 100,
			dataIndex : "url"
		}, {
			header : "后台处理类",
			sortable : true,
			width : 100,
			dataIndex : "actionClass"
		},{
			header : "是否平台菜单",
			sortable : true,
			width : 100,
			dataIndex : "isSysTem",
			renderer : this.isSystemFormat
		},{
			header : "启用状态",
			sortable : true,
			width : 100,
			dataIndex : "status",
			renderer : this.statusFormat
		} ]);
		this.gridButtons = [{
			text : "上移",
			iconCls: "upload-icon",						
			handler : this.swapSequence(""),
			scope : this
		}, {
			text : "下移",
			iconCls: "down",				
			handler : this.swapSequence(true),
			scope : this
		}
		/*, new Ext.Toolbar.Separator(),{text:"重新计算系统菜单项",scope:this,handler:this.reCalMenuAndRoles}*/
		];
		SMenuGridManagePanel.superclass.initComponent.call(this);
		this.on("saveobject", this.reloadLeftTree, this);
		this.on("removeobject", this.reloadLeftTree, this);
	}
});
//系统菜单管理
SMenuManagePanel = function() {
	this.list = new SMenuGridManagePanel({
		region : "center"
	});
	this.tree = new Ext.tree.TreePanel({
		title : "菜单信息",
		region : "west",
		autoScroll:true,
		width : 200,
		border : false,
		margins : '0 2 0 0',
		tools:[{id:"refresh",handler:function(){this.tree.root.reload();},scope:this}],
		root : new Ext.tree.AsyncTreeNode({
			id : "root",
			text : "所有菜单",
			iconCls : 'treeroot-icon',
			expanded : true,
			loader : Global.menuTreeLoader
		})
	});
	this.list.tree=this.tree;
	this.tree.on("click", function(node, eventObject) {
				var id = (node.id != 'root' ? node.id : "");
				this.list.parentId = id;
				this.list.store.baseParams.parentId = id;
				if(id){
					this.list.parent={id:id,title:node.text};
				}
				else this.list.parent=null;
				this.list.store.removeAll();
				this.list.store.load();
			}, this);
	SMenuManagePanel.superclass.constructor.call(this, {
				id : "sMenuManagePanel",
				closable : true,
				border:false,
				autoScroll : true,
				layout : "border",

				items : [this.tree, this.list]
			});
};

Ext.extend(SMenuManagePanel, Ext.Panel, {});
