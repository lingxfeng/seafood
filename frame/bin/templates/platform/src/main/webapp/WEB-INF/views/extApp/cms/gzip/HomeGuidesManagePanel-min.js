Global.homeguidesloader=new Disco.Ext.MemoryTreeLoader({iconCls:"disco-tree-node-icon",varName:"Global.HOME_GUIDES_LOADER",url:"homeGuides.java?cmd=getTree&pageSize=-1&treeData=true&all=false",listeners:{beforeload:function(b,a){b.baseParams.id=(a.id.indexOf("root")<0?a.id:"");if(typeof a.attributes.checked!=="undefined"){b.baseParams.checked=false}}}});Global.mySendDocBranchDirLoader=new Disco.Ext.MemoryTreeLoader({iconCls:"disco-tree-node-icon",varName:"Global.MY_SEND_DOC_BRANCH_DIR_LOADER",url:"newsDir.java?cmd=getBranchDirTree&pageSize=-1&treeData=true&all=false&orgType=null",listeners:{beforeload:function(b,a){b.baseParams.id=(a.id.indexOf("root")<0?a.id:"");if(typeof a.attributes.checked!=="undefined"){b.baseParams.checked=false}}}});HomeGuidesManagePanel=Ext.extend(Disco.Ext.CrudPanel,{id:"homeGuidesManagePanel",baseUrl:"homeGuides.java",gridSelModel:"checkbox",pageSize:20,batchRemoveMode:true,parentname:function(a){if(a.name==null){return"<font color='red' >\u6d93\ufffd\u7efe\u0446\u5f4d\u9357\ufffd </font>"}else{return a.name}},edit:function(){var d=HomeGuidesManagePanel.superclass.edit.call(this);if(d){var a=this.grid.getSelectionModel().getSelected();var c=a.get("parent");var b=this.fp.form.findField("parentId");if(c){c.name||(c.name=b.parentId);b.setOriginalValue(c)}}},createForm:function(){var a=new Ext.form.FormPanel({frame:true,labelWidth:80,labelAlign:"right",defaults:{width:320,xtype:"textfield"},items:[{xtype:"hidden",name:"id"},{fieldLabel:"\u935a\u5d87\u041e",name:"name",emptyText:"\u935a\u5d87\u041e\u6d93\u5d88\u5158\u6d93\u8679\u2516",allowBlank:false,blankText:"\u935a\u5d87\u041e\u6d93\u5d88\u5158\u6d93\u8679\u2516"},{fieldLabel:"URL\u9366\u677f\u6f43",name:"linkUrl"},{fieldLabel:"\u9352\u55da\ue511\u93cd\u5fd5\u6d30",name:"bankDirId",xtype:"treecombo",hiddenName:"bankDirId",displayField:"title",valueField:"id",tree:new Ext.tree.TreePanel({rootVisible:false,autoScroll:true,root:new Ext.tree.AsyncTreeNode({id:"root",text:"\u93b5\ufffd\u93c8\u590b\u722e\u9429\ufffd",expanded:true,iconCls:"treeroot-icon",loader:Global.mySendDocBranchDirLoader})})},{xtype:"numberfield",fieldLabel:"\u93ba\u6391\u7c2d",name:"sort"},{fieldLabel:"\u6d93\u5a44\u9a87\u7035\u8270\u57c5",name:"parentId",xtype:"treecombo",hiddenName:"parentId",displayField:"name",valueField:"id",tree:new Ext.tree.TreePanel({rootVisible:false,autoScroll:true,root:new Ext.tree.AsyncTreeNode({id:"root",text:"\u93b5\ufffd\u93c8\u590b\u722e\u9429\ufffd",expanded:true,iconCls:"treeroot-icon",loader:Global.homeguidesloader})})}]});return a},userRender:function(a){return a?a.trueName:"$!{lang.get('Unknown')}"},createWin:function(){return this.initWin(450,260,"\u68e3\u682d\u3009\u7035\u8270\u57c5\u7ee0\uff04\u608a")},storeMapping:["id","name","linkUrl","target","createTime","sort","parent"],initComponent:function(){this.cm=new Ext.grid.ColumnModel([{header:"\u935a\u5d87\u041e",sortable:true,width:100,dataIndex:"name"},{header:"URL\u9366\u677f\u6f43",sortable:true,width:100,dataIndex:"linkUrl"},{header:"\u6d93\u5a44\u9a87\u7035\u8270\u57c5",sortable:true,width:100,dataIndex:"parent",renderer:this.parentname},{header:"\u93ba\u6391\u7c2d",sortable:true,width:50,dataIndex:"sort"}]);HomeGuidesManagePanel.superclass.initComponent.call(this)}});
