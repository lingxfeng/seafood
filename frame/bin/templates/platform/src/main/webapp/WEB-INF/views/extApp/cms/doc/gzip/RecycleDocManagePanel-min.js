RecycleDocManagePanel=Ext.extend(Disco.Ext.CrudPanel,{gridSelModel:"checkbox",id:"recycleDocManagePanel",baseUrl:"newsDoc.java",autoLoadGridData:true,autoScroll:false,totalPhoto:0,pageSize:20,showAdd:false,showEdit:false,showView:false,batchRemoveMode:true,defaultsActions:{create:"save",list:"getMyRecycle",view:"view",update:"update",remove:"remove"},baseQueryParameter:{state:0},gridViewConfig:{forceFit:true,enableRowBody:true,showPreview:true},topicRender:function(b,c,a){return String.format('{1}<b><a style="color:green" href="news.java?cmd=show&id={0}" target="_blank">&nbsp\u93cc\u30e7\u6e45</a></b><br/>',a.id,b)},stateRender:function(a){return"\u95ab\u660f\u7deb\u9352\u72bb\u6ace"},toggleDetails:function(b,c){var a=this.grid.getView();a.showPreview=c;a.refresh()},createWin:function(){return this.initWin(900,600,"\u93c2\u56e9\u73f7\u7ee0\uff04\u608a")},storeMapping:["id","title","keywords","createDate","author","source","putDate","dir","toBranch","branchDir","state","branchState",{name:"branchDirId",mapping:"branchDir"},{name:"dirId",mapping:"dir"}],initComponent:function(){this.cm=new Ext.grid.ColumnModel([{header:"\u6d93\u5a5a\ue57d",dataIndex:"title",width:200,renderer:this.topicRender},{header:"\u93cd\u5fd5\u6d30",sortable:true,width:70,dataIndex:"dir",renderer:this.objectRender("name")},{width:70,sortable:true,header:"\u9359\u621d\u7af7\u9352\u677f\u578e\u741b\ufffd",dataIndex:"toBranch",renderer:this.booleanRender},{width:70,sortable:true,header:"\u9352\u55da\ue511\u93cd\u5fd5\u6d30",dataIndex:"branchDir",renderer:this.objectRender("name")},{header:"\u6d63\u6ec6\ufffd\ufffd",sortable:true,width:50,dataIndex:"author"},{header:"\u93be\u626e\u01f9\u93c3\u30e6\u6e61",sortable:true,width:90,dataIndex:"createDate",renderer:this.dateRender()},{width:60,sortable:true,header:"\u93c1\u7248\u5d41\u9418\u8235\ufffd\ufffd",dataIndex:"state",renderer:this.stateRender}]);this.gridButtons=[{text:"\u93ad\u3220\ue632",cls:"x-btn-text-icon",icon:"img/core/up.gif",handler:this.executeCmd("restoration")}];RecycleDocManagePanel.superclass.initComponent.call(this)}});
