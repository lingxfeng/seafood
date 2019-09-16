Ext.ns("Disco.Ext");if(typeof Global==="undefined"){Global={}}if(!Global.sytemModuleLoader){Global.sytemModuleLoader=new Ext.tree.TreeLoader({iconCls:"disco-tree-node-icon",url:"resource.java?cmd=getModules",listeners:{beforeload:function(b,a){b.baseParams.id=(a.id.indexOf("root")<0?a.id:"")}}})}if(!Global.permissionGroupLoader){Global.permissionGroupLoader=new Ext.tree.TreeLoader({iconCls:"disco-tree-node-icon",url:"permissionGroup.java?cmd=getTree",listeners:{beforeload:function(b,a){b.baseParams.permissionId=(a.id.indexOf("root")<0?a.id:"")}}})}if(typeof RoleList==="undefined"){RoleList=Ext.extend(BaseGridList,{url:"role.java?cmd=list",loadData:true,border:false,storeMapping:["id","name","title","description"],initComponent:function(){var a=new Ext.grid.CheckboxSelectionModel();this.gridConfig={sm:a},this.cm=new Ext.grid.ColumnModel([a,{header:"\u7f02\u682b\u721c",sortable:true,width:60,dataIndex:"name"},{header:"\u935a\u5d87\u041e",sortable:true,width:120,dataIndex:"title"},{header:"\u7ee0\ufffd\u6d60\ufffd",sortable:true,width:100,dataIndex:"description"}]);RoleList.superclass.initComponent.call(this)}})}ResourceList=Ext.extend(BaseGridList,{types:[["\u59af\u2033\u6f61\u95c4\u612c\u57d7","MODULE"],["URL\u95c4\u612c\u57d7","URL"],["\u93c2\u89c4\u7876\u95c4\u612c\u57d7","METHOD"],["ACL\u95c4\u612c\u57d7","ACL"]],storeMapping:["id","resStr","type","desciption"],loadData:false,border:false,url:"resource.java?cmd=list",reset:function(){this.moduleItem.reset();this.typesItem.reset();this.grid.store.reload()},resourceSearch:function(){this.grid.store.removeAll();this.grid.store.baseParams={start:0,action:this.action,type:this.typesItem.getValue(),searchKey:this.searchKey.getValue()};this.grid.store.load()},initComponent:function(){var a=new Ext.grid.CheckboxSelectionModel();this.gridConfig={sm:a},this.cm=new Ext.grid.ColumnModel([a,{header:"\u74a7\u52ec\u7c2e\u7eeb\u8bf2\u7037",sortable:true,width:70,dataIndex:"type",renderer:Disco.Ext.Util.objectRender("title")},{header:"\u74a7\u52ec\u7c2e\u93bb\u5fda\u582a",sortable:true,width:300,dataIndex:"resStr"},{header:"\u7ee0\ufffd\u6d60\ufffd",sortable:true,width:80,dataIndex:"desciption"}]);this.moduleItem=new Disco.Ext.TreeComboField({displayField:"title",width:220,listWidth:250,tree:new Ext.tree.TreePanel({autoScroll:true,root:new Ext.tree.AsyncTreeNode({id:"root",text:"\u93b5\ufffd\u93c8\u590b\u0101\u9367\ufffd",expanded:true,loader:Global.sytemModuleLoader})}),listeners:{select:function(d,b){if(!b.isLeaf()&&b.id!="root"){this.pack=b.id;this.action=""}else{this.pack="";this.action=b.id=="root"?"":b.id}this.resourceSearch()},scope:this}});this.typesItem=new Ext.form.ComboBox(Ext.apply({},{width:80,emptyText:"\u7487\u70fd\ufffd\u590b\u5ae8...",editable:false},Disco.Ext.Util.buildCombox("types","types",this.types)));this.searchKey=new Ext.form.TextField({name:"searchKey",width:80});this.tbar=["\u59af\u2033\u6f61\u951b\ufffd",this.moduleItem,"\u74a7\u52ec\u7c2e\u7eeb\u8bf2\u7037:",this.typesItem,"\u934f\u62bd\u656d\u701b\ufffd",this.searchKey,"->",{text:"\u93cc\u30e8\ue1d7",handler:this.resourceSearch,scope:this,cls:"x-btn-text-icon",icon:"images/icon-png/search.png"},{text:"\u95b2\u5d87\u7586",handler:this.reset,scope:this}];ResourceList.superclass.initComponent.call(this)}});PermissionPanel=Ext.extend(Disco.Ext.CrudPanel,{id:"permissionPanel",pageSize:20,importData:false,exportData:false,singleWindowMode:true,batchRemoveMode:true,types:[["\u59af\u2033\u6f61\u95c4\u612c\u57d7","MODULE"],["URL\u95c4\u612c\u57d7","URL"],["\u93c2\u89c4\u7876\u95c4\u612c\u57d7","METHOD"],["ACL\u95c4\u612c\u57d7","ACL"]],theStatus:[["\u935a\ue21c\u6564",0],["\u934b\u6ec5\u6564",-1]],baseUrl:"permission.java",viewWin:{title:"\u93c9\u51ae\u6aba\u7487\ufe3d\u510f\u93cc\u30e7\u6e45",width:600,height:480},createSelPermissionGroup:function(){var a=this.grid.getSelectionModel().getSelections();if(a.length==0){Ext.Msg.alert("\u93bf\u5d84\u7d94\u93bb\u612e\u305a","\u7487\u5cf0\u539b\u95ab\u590b\u5ae8\u9354\u71bb\u5158\u951b\ufffd");return}if(!this["selPermissionGroup"]){this["selPermissionGroup"]=new Ext.tree.TreePanel({region:"west",split:false,autoScroll:true,width:250,bbar:["->",{text:"\u9352\u950b\u67ca",handler:function(b){this["selPermissionGroup"].getRootNode().reload()},scope:this}],root:new Ext.tree.AsyncTreeNode({id:"root",text:"\u93c9\u51ae\u6aba\u7f01\ufffd",border:false,rootVisible:false,loader:new Ext.tree.TreeLoader({url:"permissionGroup.java?cmd=getTree",listeners:{beforeload:function(c,b){c.baseParams.permissionId=(b.id!="root"?b.attributes.id:"")},scope:this}})})})}Disco.Ext.Window.show({title:"\u7487\u70fd\ufffd\u590b\u5ae8\u93c9\u51ae\u6aba\u7f01\ufffd",autoHide:true,border:false,single:false,items:this["selPermissionGroup"],width:250,buttons:Disco.Ext.Window.YESNO,handler:function(d,g,b){var f=b.getSelectionModel().getSelectedNode();if(d=="yes"){if(f){var c=this.grid.getSelectionModel().getSelections();var e=[];Ext.each(c,function(i){e.push(i.get("id"))});var h=f.attributes.id=="root"?"":f.attributes.id;this.moveToPermissionGroup({parentId:h,id:e})}}},scope:this})},moveToPermissionGroup:function(a){Ext.Ajax.request({url:"permission.java?cmd=moveToPermissionGroup",params:a,success:function(){this.grid.getStore().removeAll();this.grid.getStore().reload()},scope:this})},choiceSelectGridData:function(grid,winName,gridList,winTitle){return function(){var theGrid=this[grid];if(!this[winName]){var glist=eval("new "+gridList);this[winName]=new Disco.Ext.GridSelectWin({border:false,title:winTitle,width:650,grid:glist});this[winName].on("select",function(datas){var ds=[];for(var i=0;i<datas.length;i++){if(theGrid.store.find("id",datas[i].id)<0){ds[ds.length]=datas[i]}}theGrid.store.loadData(ds,true)},this)}this[winName].show()}},createViewPanel:function(){if(!this.viewSelectGrid){this.viewSelectGrid=new ResourceList({title:"\u7487\u30e6\u6f48\u95c4\u612d\u588d\u9356\u546d\u60c8\u9428\u52ee\u796b\u5a67\ufffd",columnWidth:0.65,height:250,border:true,gridForceFit:false,pagingToolbar:false,url:"permission.java?cmd=loadResource"})}if(!this.viewRoleGrid){this.viewRoleGrid=new RoleList({title:"\u934f\u950b\u6e41\u7487\u30e6\u6f48\u95c4\u612e\u6b91\u7459\u6395\u58ca",columnWidth:0.35,height:250,pagingToolbar:false,url:"permission.java?cmd=loadRole"})}var a=new Ext.form.FormPanel({frame:true,labelWidth:60,labelAlign:"right",items:[{xtype:"hidden",name:"id"},{xtype:"hidden",name:"resources"},{xtype:"fieldset",title:"\u9369\u70d8\u6e70\u6dc7\u2103\u4f05",height:80,items:[{xtype:"labelfield",fieldLabel:"\u93c9\u51ae\u6aba\u935a\u5d87\u041e",name:"name"},Disco.Ext.Util.buildColumnForm({xtype:"labelfield",fieldLabel:"\u9418\u8235\ufffd\ufffd",name:"status",renderer:this.statusRender},{anchor:"100%",height:40,xtype:"labelfield",fieldLabel:"\u7ee0\ufffd\u6d60\ufffd",name:"description"})]},{xtype:"fieldset",title:"\u7487\ufe3d\u510f",anchor:"100%",height:300,items:{layout:"hbox",layoutConfig:{align:"stretch"},defaults:{flex:1},items:[this.viewSelectGrid,{xtype:"box",width:5},this.viewRoleGrid]}}]});return a},createForm:function(){var c=new Ext.grid.CheckboxSelectionModel();this.editGrid=new Ext.grid.GridPanel({title:"\u93c9\u51ae\u6aba\u9429\u7a3f\u53e7\u74a7\u52ec\u7c2e",sm:c,viewConfig:{forceFit:true},cm:new Ext.grid.ColumnModel([c,new Ext.grid.RowNumberer({header:"\u6434\u5fd3\u5f7f",width:40}),{header:"ID",dataIndex:"id",hideable:true,hidden:true},{header:"\u74a7\u52ec\u7c2e\u7eeb\u8bf2\u7037",sortable:true,width:70,dataIndex:"type"},{header:"\u74a7\u52ec\u7c2e\u93bb\u5fda\u582a",sortable:true,width:300,dataIndex:"resStr"},{header:"\u7ee0\ufffd\u6d60\ufffd",sortable:true,width:80,dataIndex:"desciption",hidden:true}]),store:new Ext.data.JsonStore({fields:["id","type","description","resStr"]}),bbar:[{text:"\u5a23\u8bf2\u59de\u74a7\u52ec\u7c2e",cls:"x-btn-text-icon",icon:"images/icons/application_form_add.png",handler:this.choiceSelectGridData("editGrid","selectWin","ResourceList","\u95ab\u590b\u5ae8\u74a7\u52ec\u7c2e"),scope:this},{text:"\u9352\u72bb\u6ace\u74a7\u52ec\u7c2e",cls:"x-btn-text-icon",icon:"images/icons/application_form_delete.png",handler:function(){Disco.Ext.Util.removeGridRows(this.editGrid)},scope:this}]});var b=new Ext.grid.CheckboxSelectionModel();this.roleGrid=new Ext.grid.GridPanel({title:"\u93c9\u51ae\u6aba\u9429\u7a3f\u53e7\u7459\u6395\u58ca",sm:b,viewConfig:{forceFit:true},cm:new Ext.grid.ColumnModel([c,new Ext.grid.RowNumberer({header:"\u6434\u5fd3\u5f7f",width:40}),{header:"ID",dataIndex:"id",hideable:true,hidden:true},{header:"\u7459\u6395\u58ca\u7f02\u682b\u721c",sortable:true,width:70,dataIndex:"name"},{header:"\u935a\u5d87\u041e",sortable:true,width:100,dataIndex:"title"},{header:"\u7ee0\ufffd\u6d60\ufffd",sortable:true,width:80,dataIndex:"desciption",hidden:true}]),store:new Ext.data.JsonStore({fields:["id","name","title","description"]}),bbar:[{text:"\u5a23\u8bf2\u59de\u7459\u6395\u58ca",cls:"x-btn-text-icon",icon:"images/icons/application_form_add.png",handler:this.choiceSelectGridData("roleGrid","selectRoleWin","RoleList","\u95ab\u590b\u5ae8\u7459\u6395\u58ca"),scope:this},{text:"\u9352\u72bb\u6ace\u7459\u6395\u58ca",cls:"x-btn-text-icon",icon:"images/icons/application_form_delete.png",handler:function(){Disco.Ext.Util.removeGridRows(this.roleGrid)},scope:this}]});var a=new Ext.form.FormPanel({frame:true,labelWidth:60,labelAlign:"right",items:[{xtype:"hidden",name:"id"},{xtype:"fieldset",title:"\u9369\u70d8\u6e70\u6dc7\u2103\u4f05",autoHeight:true,items:[{xtype:"textfield",fieldLabel:"\u93c9\u51ae\u6aba\u935a\u5d87\u041e",name:"name",anchor:"-1"},Disco.Ext.Util.twoColumnPanelBuild({xtype:"treecombo",fieldLabel:"\u7f01\ufffd",name:"parent",hiddenName:"parent",displayField:"name",valueField:"id",width:150,tree:new Ext.tree.TreePanel({autoScroll:true,root:new Ext.tree.AsyncTreeNode({id:"root",text:"\u93b5\ufffd\u93c8\u590c\u7c8d",iconCls:"treeroot-icon",expanded:true,loader:Global.permissionGroupLoader})})},Disco.Ext.Util.buildCombox("status","\u9418\u8235\ufffd\ufffd",this.theStatus,0)),{anchor:"-1",height:40,xtype:"textarea",fieldLabel:"\u7ee0\ufffd\u6d60\ufffd",name:"description"}]},{xtype:"panel",anchor:"100%",height:300,layout:"fit",items:{xtype:"tabpanel",activeTab:0,items:[this.editGrid,this.roleGrid]}}]});return a},statusRender:function(a){if(a==-1){return"<font color=red>\u934b\u6ec5\u6564</a>"}else{return"\u935a\ue21c\u6564"}},onCreate:function(){if(this.editGrid.store.getCount()>0){this.editGrid.store.removeAll()}if(this.roleGrid.store.getCount()>0){this.roleGrid.store.removeAll()}},view:function(){var a=PermissionPanel.superclass.view.call(this);if(a){this.viewSelectGrid.store.removeAll();this.viewSelectGrid.store.load({params:"id="+this.grid.getSelectionModel().getSelected().get("id")});this.viewRoleGrid.store.removeAll();this.viewRoleGrid.store.load({params:"id="+this.grid.getSelectionModel().getSelected().get("id")})}},onEdit:function(b){if(b){this.editGrid.store.removeAll();var a=this.grid.getSelectionModel().getSelected().get("resources");this.editGrid.store.removeAll();this.editGrid.store.loadData(a);this.roleGrid.store.removeAll();this.roleGrid.store.loadData(this.grid.getSelectionModel().getSelected().get("roles"))}},getGridDataAsString:function(b){b=b||this.roleGrid;var c="";for(var a=0;a<b.store.getCount();a++){var d=b.store.getAt(a);c+=d.get("id")+","}return c},save:function(){var a=this.getGridDataAsString(this.editGrid);var b=this.getGridDataAsString(this.roleGrid);this.fp.form.baseParams={resources:a,roles:b};PermissionPanel.superclass.save.call(this)},createWin:function(b,a){return this.initWin(638,520,"\u93c9\u51ae\u6aba",b,a)},storeMapping:["id","name","operation","resources","description","status","roles","parent"],initComponent:function(){var a=new Ext.grid.CheckboxSelectionModel();this.cm=new Ext.grid.ColumnModel([a,{header:"\u93c9\u51ae\u6aba\u935a\u5d87\u041e",sortable:true,width:200,dataIndex:"name"},{header:"\u74a7\u52ec\u7c2e\u93c1\ufffd",sortable:true,width:50,hidden:true,dataIndex:"resources",renderer:this.objectRender("length")},{header:"\u7ee0\ufffd\u6d60\ufffd",sortable:true,width:200,dataIndex:"description"},{header:"\u9418\u8235\ufffd\ufffd",sortable:true,width:80,dataIndex:"status",renderer:this.statusRender}]);this.gridConfig={enableDragDrop:true,sm:a,ddGroup:"gridDD"};this.gridButtons=[{text:"\u7ec9\u8bf2\u59e9\u9352\u7248\u6f48\u95c4\u612e\u7c8d",iconCls:"move",handler:this.createSelPermissionGroup,scope:this}],PermissionPanel.superclass.initComponent.call(this)}});PermissionManagePanel=Ext.extend(Ext.Panel,{layout:"border",border:false,onClickNode:function(a,b){this.grid.store.baseParams={permissionId:a.id!="root"?a.attributes.id:"",searchKey:""};this.grid.store.load()},showContextmenu:function(b,c){var a=b.getOwnerTree();b.select();var d=a.contextmenu;d.node=b;d.showAt(c.getXY())},createContextmenu:function(){return new Ext.menu.Menu({shadow:true,defaults:{scope:this},items:[{text:"\u5a23\u8bf2\u59de",scope:this,handler:function(b){var a=b.parentMenu.node;Ext.Msg.prompt("\u93c2\u677f\ue583\u93c9\u51ae\u6aba","\u7487\u75af\u7ded\u934f\u30e6\u67ca\u6fa7\u70b4\u6f48\u95c4\u612c\u6095\u7ec9\ufffd",function(c,d){if(c=="ok"){if(d.trim()==""){Ext.Msg.alert("\u7487\u75af\u7ded\u934f\u30e6\u6f48\u95c4\u612c\u6095\u7ec9\u5e2e\u7d12");return}Ext.Ajax.request({scope:this,url:"permissionGroup.java?cmd=save",params:{name:d,parent:a.attributes.id=="root"?"":a.attributes.id},success:function(e){this.tree.getRootNode().reload()}})}},this)}},{text:"\u6dc7\ue1bd\u657c",scope:this,handler:function(b){var a=b.parentMenu.node;Ext.Msg.prompt("\u6dc7\ue1bd\u657c\u93c9\u51ae\u6aba","\u7487\u75af\u7ded\u934f\u30e4\u6168\u93c0\u89c4\u6f48\u95c4\u612c\u6095\u7ec9\ufffd",function(c,d){if(c=="ok"){if(d.trim()==""){Ext.Msg.alert("\u7487\u75af\u7ded\u934f\u30e6\u6f48\u95c4\u612c\u6095\u7ec9\u5e2e\u7d12");return}Ext.Ajax.request({scope:this,url:"permissionGroup.java?cmd=update",params:{name:d,id:a.attributes.id},success:function(e){a.setText(d)}})}},this,false,a.attributes.text)}},{text:"\u9352\u72bb\u6ace",scope:this,handler:function(b){var a=b.parentMenu.node;Ext.Msg.confirm("\u93bf\u5d84\u7d94\u93bb\u612e\u305a","\u6d63\u72b5\u2491\u7039\u6c33\ue6e6\u9352\ue048\u6ace\u746d\u53c9\u6f48\u95c4\u612c\u60a7!",function(c){if(c=="yes"){Ext.Ajax.request({scope:this,url:"permissionGroup.java?cmd=remove",params:{id:a.attributes.id},success:function(d){this.grid.store.removeAll();this.grid.store.reload();this.tree.getRootNode().reload()}})}},this)}}]})},dragdrop:function(a,d,b,c){var e={moveNodeId:d.attributes.id=="root"?"":d.attributes.id,newParentId:c.attributes.id=="root"?"":c.attributes.id};Ext.Ajax.request({url:"permissionGroup.java?cmd=move",params:e,success:function(g,f){this.tree.getRootNode().reload()},scope:this})},createPermissionGroup:function(){return new Ext.tree.TreePanel({region:"west",split:false,autoScroll:true,width:250,title:"\u93c9\u51ae\u6aba\u7f01\ufffd",ddGroup:"gridDD",enableDD:true,autoScroll:true,border:false,margins:"0 2 0 0",tools:[{id:"refresh",handler:function(){this.tree.root.reload()},scope:this}],contextmenu:this.createContextmenu(),root:new Ext.tree.AsyncTreeNode({id:"root",text:"\u93c9\u51ae\u6aba\u7f01\ufffd",expanded:true,rootVisible:false,loader:new Ext.tree.TreeLoader({iconCls:"disco-tree-node-icon",url:"permissionGroup.java?cmd=getTree",listeners:{beforeload:function(b,a){b.baseParams.permissionId=(a.id!="root"?a.attributes.id:"")},scope:this}})}),listeners:{contextmenu:this.showContextmenu,beforemovenode:this.dragdrop,click:this.onClickNode,nodedragover:function(b){var c=b.target;var a=b.source;if((c.leaf)||a.grid){c.leaf=false}else{return true}return true},beforenodedrop:function(d){var c=d.target;if(d.data.grid){var a=d.data.selections;var b=[];Ext.each(a,function(e){b.push(e.get("id"))});var f=c.attributes.id=="root"?"":c.attributes.id;this.permission.moveToPermissionGroup({parentId:f,id:b})}},scope:this}})},initComponent:function(){this.tree=this.createPermissionGroup();this.permission=new PermissionPanel({region:"center",border:false});this.grid=this.permission.grid;this.items=[this.tree,this.permission];PermissionManagePanel.superclass.initComponent.call(this)}});
