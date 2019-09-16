Ext.ns("Global.Portal");Global.Portal.Models=[{id:1,colCfg:[1],icon:"images/portalStyle/style1.gif"},{id:2,colCfg:[0.5,0.5],icon:"images/portalStyle/style2.gif"},{id:3,colCfg:[0.3,0.7],icon:"images/portalStyle/style3.gif"},{id:4,colCfg:[0.7,0.3],icon:"images/portalStyle/style4.gif"},{id:5,colCfg:[1/3,1/3,1/3],icon:"images/portalStyle/style5.gif"},{id:6,colCfg:[0.25,0.25,0.5],icon:"images/portalStyle/style6.gif"},{id:7,colCfg:[0.5,0.25,0.25],icon:"images/portalStyle/style7.gif"},{id:8,colCfg:[0.25,0.5,0.25],icon:"images/portalStyle/style8.gif"}];Ext.ux.PortletPanel=Ext.extend(Ext.ux.Portlet,{height:320,collapsible:false,layout:"fit",loadSettingUrl:"",saveSettingUrl:"",getSettingForm:Ext.emptyFn,onMinus:function(){this.tools.minus.hide();this.tools.plus.show();this.collapse(false)},onPlus:function(){this.tools.minus.show();this.tools.plus.hide();this.expand(false)},onMaximize:function(){this.exitSettingForm();this.viewMode="max";var a=[];if(this.getSettingForm!=Ext.emptyFn){a.push({id:"gear",scope:this,handler:function(){var c=this.getSettingForm("win");if(c){var b=function(){c.buttons[0].disable();c.el.mask(Ext.LoadMask.prototype.msg,"x-mask-loading");c.load({scope:this,url:this.loadSettingUrl,success:function(d,e){c.el.unmask();c.buttons[0].enable();this.fireEvent("loadSetting",this,d,e)}})};if(c.rendered){b.call(this)}else{c.on("render",b,this)}}if(!this.maxWin.getItem(c)){this.maxWin.add(c)}this.maxWin.getLayout().setActiveItem(c)}})}this.maxWin=new Ext.Window({modal:true,width:800,height:500,activeItem:0,border:false,layout:"card",title:this.title,layoutOnCardChange:true,resizable:false,tools:a,items:this.buildContent(),listeners:{scope:this,close:function(){this.viewMode="min";delete this.maxWin;var b=null;if(b=this.getSettingForm("win")){delete this[b.panelName];b.destroy()}}}}).show()},onMinimize:function(){this.viewMode="min"},onClose:function(){if(this.viewMode=="max"){this.onMinimize()}else{delete this.lastOwnerct;if(this.ownerCt){this.ownerCt.remove(this)}else{this.destroy()}}},setVisableTools:function(a){var b=Array.prototype.slice.call(arguments,1);Ext.each(b,function(c){var d=null;if(d=this.tools[c]){d[a?"show":"hide"]()}},this)},onGear:function(){var b=this.getSettingForm();if(b){var a=function(){b.buttons[0].disable();b.el.mask(Ext.LoadMask.prototype.msg,"x-mask-loading");b.load({scope:this,url:this.loadSettingUrl,success:function(c,d){b.el.unmask();b.buttons[0].enable();this.fireEvent("loadSetting",this,c,d)}})};if(b.rendered){a.call(this)}else{b.on("render",a,this)}if(!this.main.getItem(b)){this.main.add(b)}this.main.getLayout().setActiveItem(b)}},onLoadSettingSuccess:function(c,a,b){},onSaveSettingSuccess:function(c,a,b){},onSaveSetting:function(a){var b=a.refOwner;b.el.mask(Ext.LoadMask.prototype.msg,"x-mask-loading");b.getForm().submit({scope:this,url:this.saveSettingUrl,success:function(c,d){b.el.unmask();this.fireEvent("savesetting",this,c,d)}})},exitSettingForm:function(){if(this.viewMode=="max"){this.autoHeight=false;this.maxWin.getLayout().setActiveItem(0)}else{this.autoHeight=false;this.main.autoHeight=false;this.main.getLayout().setActiveItem(0)}},createTools:function(){return[{id:"gear",handler:this.onGear,hidden:this.getSettingForm===Ext.emptyFn,scope:this},{id:"minus",handler:this.onMinus,scope:this},{id:"plus",handler:this.onPlus,hidden:true,scope:this},{id:"maximize",handler:this.onMaximize,hidden:!this.maximize,scope:this},{id:"close",handler:this.onClose,scope:this}]},beforeDestroy:function(){Ext.destroy(this.mask);Ext.ux.PortletPanel.superclass.beforeDestroy.call(this)},afterRender:function(){Ext.ux.PortletPanel.superclass.afterRender.call(this);this.mask=Ext.getBody().createChild({cls:"ext-el-mask"});this.mask.enableDisplayMode("block");this.mask.hide()},buildContent:function(){return this.createContent()},createContent:function(){return{}},initComponent:function(){this.addEvents("loadSetting","savesetting");this.tools=this.createTools();this.items={ref:"main",layout:"card",activeItem:0,items:this.createContent()};this.on("loadSetting",this.onLoadSettingSuccess,this);this.on("savesetting",this.onSaveSettingSuccess,this);Ext.ux.PortletPanel.superclass.initComponent.call(this)}});Ext.reg("portletpanel",Ext.ux.PortletPanel);Ext.ux.PortalPanelColumn=Ext.extend(Ext.ux.PortalColumn,{defaultType:"portletpanel"});Ext.reg("portalpanelcolumn",Ext.ux.PortalPanelColumn);Ext.ux.ImgPortletPanel=Ext.extend(Ext.ux.PortletPanel,{portletType:"img",bodyStyle:"text-align:center;",refreshImg:function(a){var g=a,j=g.getResizeEl();var k=j.getStyleSize();var b=k.width;var f=g.imgMinWidth;var c=g.imgMaxWidth;if(b<f){b=f;j.setStyle("overflow","auto")}else{if(c&&b>c){b=c}j.setStyle("overflow","hidden")}if(Ext.isEmpty(g.lastBodySize)){g.lastBodySize=k;g.el.update(String.format('<img src="{0}"/>',Ext.urlAppend(this.url,Ext.urlEncode({width:b,height:k.height}))))}else{var d=false;if(b&&(g.lastBodySize.width!=b)){var i=Math.abs(g.lastBodySize.width-b);if(i>20){d=true;g.lastBodySize.width=b}}if(!d&&k.height&&(g.lastBodySize.height!=k.height)){var e=Math.abs(g.lastBodySize.height-k.height);if(i>20){d=true;g.lastBodySize.height=k.height}}if(d){g.el.update(String.format('<img src="{0}"/>',Ext.urlAppend(this.url,Ext.urlEncode({width:b,height:k.height}))))}}},onImgCmpResize:function(c,a,b){this.refreshImg(c,a,b)},buildContent:function(){return{xtype:"box",imgMinWidth:null,imgMaxWidth:null,listeners:{scope:this,resize:this.onImgCmpResize}}},createContent:function(){return{imgMinWidth:null,imgMaxWidth:650,xtype:"box",ref:"/imgCmp",listeners:{scope:this,resize:this.onImgCmpResize}}}});Ext.reg("imgportletpanel",Ext.ux.ImgPortletPanel);Ext.ux.GridPortletPanel=Ext.extend(Ext.ux.PortletPanel,{portletType:"grid",url:null,columns:[],storeMapping:[],gridConfg:{},gridViewConfig:{},gridAutoLoad:true,gridInitData:null,buildGrid:function(){this.store=this.store||new Ext.data.JsonStore({url:this.url,root:"result",totalProperty:"rowCount",fields:this.storeMapping,autoLoad:(this.url&&this.gridAutoLoad)});return new Ext.grid.GridPanel(Ext.apply({columns:this.buildColumns(),viewConfig:Ext.apply({forceFit:true},this.gridViewConfig),store:this.store},this.gridConfg))},buildContent:function(){var a=this.buildGrid();if(a&&!this.gridAutoLoad&&this.gridInitData){a.getStore().loadData(this.gridInitData)}return a},createContent:function(){this.grid=this.buildContent();return this.grid},onMaximize:function(){Ext.ux.GridPortletPanel.superclass.onMaximize.call(this);var a=this.grid.getColumnModel();this.maxWin.mon(a,"hiddenchange",function(b,c,d){this.maxWin.getItem(0).getColumnModel().setHidden(c,d)},this)},initComponent:function(){Ext.ux.GridPortletPanel.superclass.initComponent.call(this)}});Global.Portal.PanelMC=new Ext.util.MixedCollection;Disco.Ext.Util.SelectStyleWin=(function(){var b,c;var a=function(){b.hide()};var d=function(){if(Ext.isFunction(b.callback)){var e=c.getSelectedRecords()[0];b.callback.call(b.scope||this,e);b.hide()}};c=new Ext.DataView({singleSelect:true,itemSelector:"div.item",overClass:"over",cls:"portal-styleSetting-dataView",tpl:new Ext.XTemplate('<tpl for=".">','<div class="item"><img src="{icon}"/></div>',"</tpl>"),store:new Ext.data.JsonStore({data:Global.Portal.Models,fields:["id","colCfg","icon"]})});b=new Ext.Window({layout:"fit",cls:"portal-styleSetting-window",modal:true,resizable:false,constrain:true,closeAction:"hide",bodyStyle:"padding:5px;",height:204,width:340,items:c,buttons:[{text:"\u5e94\u7528",handler:d},{text:"\u53d6\u6d88",handler:a}],listeners:{hide:function(){Ext.del(b,"callback","scope")}}});return{show:function(f){b.show();f=f||{};Ext.apply(b,{callback:f.callback,scope:f.scope});if(f.selId){var g=c;var e=g.getStore().getById(f.selId);if(Ext.isEmpty(e)){e=0}c.select(e)}}}})();Disco.Ext.Util.SelectPortalWin=(function(){var b=773,i=495;var f,d,a,k=new Ext.util.MixedCollection(),c=false,j=false;var e=function(){var l=k;if(Ext.isFunction(f.callback)){f.callback.call(f.scope||window,l,a.getStore());f.hide()}};var g=function(o,n,q,r){var m=o.getStore();var l=m.getAt(n);var p=l.get("id");if(k.key(p)){k.removeKey(p)}else{k.add(l.data)}o.refreshNode(n);j=true};var h=function(){f.hide()};a=new Ext.DataView({trackOver:true,overClass:"over",itemSelector:"div.item",loadingText:Ext.LoadMask.prototype.msg,onDblClick:Ext.emptyFn,store:new Ext.data.JsonStore({id:"id",data:[{type:"1",icon:"images/20woso21.png",title:"\u6d4f\u89c8\u5668\u4f7f\u7528\u6bd4\u4f8b",id:"BrowserPic",text:"\u6d4f\u89c8\u5668\u8fd1\u671f\u7684\u4f7f\u7528\u60c5\u51b5"},{type:"1",icon:"images/20woso21.png",title:"\u9879\u76ee\u91cc\u7a0b\u7891\u4fe1\u606f",id:"ProjectMilestonesGrid",text:"\u6839\u636e\u6765\u6e90\u6570\u636e\u663e\u793a\u4e00\u4e2a\u751f\u6210\u56fe\u8868"},{type:"1",icon:"images/20woso21.png",title:"\u5feb\u6377\u952e\u5bfc\u822a",id:"ShortCutPanel"}],fields:["id","icon","title","text","type"],filterBy:function(m,l){this.snapshot=this.snapshot||this.data;this.data=this.queryBy(m,l||this);this.fireEvent("datachanged",this)}}),listeners:{scope:this,click:g,dblclick:e},tpl:new Ext.XTemplate('<div class="selectWinDataView">','<tpl for=".">','<div class="item <tpl if="this.ifExist(id)">x-view-selected</tpl>">','<img src="{icon}"/>','<div class="right-content"><div class="title">{title}</div>',"<p>{text}</p>",'<p style="text-align:right;margin-top: 12px;padding-right:20px;"><tpl if="!this.ifExist(id)"><a class="operating" href="javascript:;" cmd="operating">\u6dfb\u52a0</a></tpl><tpl if="this.ifExist(id)"><a class="operating" style="color:#CFCDCD;" cmd="operating">\u53d6\u6d88</a></tpl></p>',"</div>",'<div style="clear:left"></div></div>',"</tpl>","</div>",{ifExist:function(l){return !!k.key(l)}})});d=new Ext.DataView({trackOver:true,overClass:"over",singleSelect:true,itemSelector:"div.item",loadingText:Ext.LoadMask.prototype.msg,tpl:'<div class="selectWinDataViewType"><tpl for="."><div class="item"><span>{text}</span></div></tpl></div>',listeners:{scope:this,afterrender:function(){d.select(0)},selectionchange:function(n,m){var p=m[0];a.getStore().clearFilter();var o=n.indexOf(p);if(o>=0){var l=n.getStore().getAt(o);if(l.get("type")!="all"){a.getStore().filter("type",l.get("id"))}}}},store:new Ext.data.JsonStore({id:"id",fields:["id","text","type"],data:[{text:"\u6240\u6709",type:"all"}]})});f=new Ext.Window({width:773,height:495,layout:"hbox",constrain:true,resizable:false,layoutConfig:{align:"stretch",pack:"start"},tbar:["->",{xtype:"textfield",emptyText:"\u641c\u7d22...",listeners:{scope:this,specialkey:function(m,n){if(n.getKey()==n.ENTER){var l=m.getValue();a.getStore().clearFilter();a.getStore().filterBy(function(p,o){var s=p.get("title");var r=p.get("text");var q=new RegExp(l,"i");if(q.test(s)||q.test(r)){return true}else{return false}},this)}}}}],items:[{width:200,items:d},{flex:1,autoScroll:true,items:a}],closeAction:"hide",modal:true,border:false,buttons:[{handler:e,text:"\u4fdd\u5b58"},{handler:h,text:"\u53d6\u6d88"}]});return{show:function(n,m){j=false;var l=Disco_RIA.getCfg("portalConfig");k.clear();if(l){k.addAll(l)}if(a.rendered){a.refresh()}f.callback=n;f.scope=m;f.show()}}})();Disco.Ext.Portal=function(a){var b=parseInt(a.portalMode);a.portalMode=isNaN(b)?5:b;a.currentColIndex=0;this.allMode=new Ext.util.MixedCollection(false);this.allMode.addAll(this.modeCfg);Disco.Ext.Portal.superclass.constructor.apply(this,arguments)};Ext.extend(Disco.Ext.Portal,Ext.ux.Portal,{autoScroll:true,portalIdPrefix:"",defaultType:"portalpanelcolumn",defaults:{style:"padding:10px 0 10px 17px"},modeCfg:Global.Portal.Models,selectStyleWin:Disco.Ext.Util.SelectStyleWin,selectPortalWin:Disco.Ext.Util.SelectPortalWin,getPortalModeCfg:function(b){var a;b=b||this.portalMode;if(a=this.allMode.key(b)){return a.colCfg}return[1]},getPortals:function(){var a=[],b=window.Global.Config.portals||[];var e=window.Global.Config.portalSeatCfg||{};for(var c=0;c<b.length;c++){if(b[c]&&Global.Portal.PanelMC.key(b[c])){var d=e[b[c]];if(d){a.push(Ext.apply({config:true,item:b[c]},d))}else{a.push(b[c])}}}return a},buildPortalColumCfg:function(){var b=[];var a=this.getPortalModeCfg();Ext.each(a,function(c){b.push({columnWidth:c})},this);return b},onSelectStyleHandler:function(a){var b=a.get("id");if(this.portalMode!=b&&this.allMode.key(b)){this.getTopToolbar().items.key("saveMode").enable();this.updatePortalMode(b)}},savePortalSeat:function(){Ext.getCmp("main").savePersonality(function(){this.getTopToolbar().items.key("saveMode").disable()}.createDelegate(this))},onPortalStyle:function(){var a={selId:this.portalMode,callback:this.onSelectStyleHandler,scope:this};this.selectStyleWin.show(a)},updatePortalMode:function(a){Ext.Ajax.request({url:"manage.java?cmd=changePortalMode",params:{portalMode:a},scope:this,success:function(b,c){Disco_RIA.setCfg("portalMode",a);this.updatePortalModeHandler(a)}})},updatePortalModeHandler:function(f){this.portalMode=f||Disco_RIA.getCfg("portalMode");var c=this.buildPortalColumCfg(),b=[],e=[];var d=Disco_RIA.getCfg("portalConfig")||[];var a=Ext.arr2Map("id",d);this.items.each(function(h){var g=[];h.items.each(function(j){var k=j.id.substring(this.portalIdPrefix.length);if(a[k]){var i=a[k];g.push(Ext.apply({config:true,item:j},i))}b.push.apply(b,g)},this);e.push(this.remove(h,false))},this);this.add(c);if(!b.length&&d.length){Ext.each(d,function(g){this.addPortal(g.id,g.id,g.col,g.row,false)},this)}else{Ext.each(b,function(g){this.addPortal(g.item,g.id,g.col,g.row,false)},this)}this.doLayout();Ext.destroy(e);this.getTopToolbar().items.key("saveMode").enable()},refreshPortal:function(){var b=Disco_RIA.getCfg("portalConfig");var c=new Ext.util.MixedCollection();this.items.each(function(d){d.items.each(function(e){c.add(e.id.substring(this.portalIdPrefix.length),e)},this)},this);var a=[];c.eachKey(function(d){if(b.indexOf(d)<0){a.push(d)}},this);Ext.each(a,function(d){var e=c.removeKey(d);if(e){e.ownerCt.remove(e)}});Ext.each(b,function(d){this.addPortal(d.id,d.id,d.col,d.row)},this)},getAutoInserCol:function(){if(this.items.getCount()==1){return 0}var c=[],a;this.items.each(function(d){if(d.items){c.push(d.items.getCount())}},this);var b=Math.min.apply(Math,c);return c.indexOf(b)==-1?0:c.indexOf(b)},addPortal:function(a,e,c,b,g){if(!Ext.isObject(a)&&!Ext.isFunction(a)){a=Global.Portal.PanelMC.key(a)}c=parseInt(c);if(!Ext.isNumber(c)){c=this.getAutoInserCol()}b=Ext.num(parseInt(b),0);if(c>=this.items.getCount()){c=this.items.getCount()-1}var h=this.portalIdPrefix+e;var f=this.findById(h);if(!f){var d=this.getItem(c);if(!d){d=this.getItem(this.getAutoInserCol())}if(a){if(a.events){d.add(a)}else{d.add(new a({id:h}))}}if(g!==false){d.doLayout()}}},selecPortalHandler:function(a){this.getTopToolbar().items.key("saveMode").enable();Disco_RIA.setCfg("portalConfig",a.items);this.refreshPortal()},onPortalSettingHanlder:function(){this.selectPortalWin.show(this.selecPortalHandler,this)},buildTbar:function(){return{items:[{text:"\u8bbe\u7f6e\u6a21\u5757",iconCls:"cog_edit",handler:this.onPortalSettingHanlder,scope:this},{text:"\u4fdd\u5b58\u6a21\u5757",disabled:true,itemId:"saveMode",iconCls:"script_save",handler:this.savePortalSeat,scope:this},{text:"\u5feb\u6377\u8bbe\u7f6e",icon:"/images/icons/bell_link.png",handler:this.onShortCutHanlder,scope:this},{text:"\u6a21\u5757\u6837\u5f0f",iconCls:"connect",handler:this.onPortalStyle,scope:this}]}},onDrop:function(){this.getTopToolbar().items.key("saveMode").enable()},onShortCutHanlder:function(){Disco.Ext.Portal.ShortCutManageWin.show()},initComponent:function(){this.items=this.buildPortalColumCfg();this.tbar=this.buildTbar();this.on("drop",this.onDrop,this);Disco.Ext.Portal.superclass.initComponent.call(this)}});Ext.reg("ux.portal",Disco.Ext.Portal);var ProjectMilestonesGrid=Ext.extend(Ext.ux.GridPortletPanel,{title:"\u9879\u76ee\u91cc\u7a0b\u7891\u4fe1\u606f",loadSettingUrl:"data/portal/portalConf.json",saveSettingUrl:"data/success.json",storeMapping:["taskId","projectName","taskName","workContext","taskType","taskPlanEndTime"],getSettingForm:function(a){var b=(a||"")+"settingForm";if(!this[b]){this[b]=new Ext.FormPanel({panelName:b,buttonAlign:"center",defaultType:"textfield",bodyStyle:"padding:20px;",defaults:{anchor:"-20"},waitMsgTarget:true,autoHeight:true,items:[{xtype:"hidden",name:"id"},{name:"title",fieldLabel:"Title"},{columns:1,name:"projectId",xtype:"radiogroup",fieldLabel:"Project",items:[{boxLabel:"Follow Global Project Setting",inputValue:"seq_001"},{boxLabel:"Choose Specific Project",inputValue:"seq_002"}]},{xtype:"combo",store:[],anchor:null,displayField:"",width:190,valueField:""},{xtype:"combo",store:[],anchor:null,displayField:"",width:190,valueField:"",fieldLabel:"Object",name:"object"},{valueField:"id",displayField:"text",xtype:"lovcombo",mode:"local",width:190,anchor:null,disableChoice:true,store:new Ext.data.JsonStore({data:[],fields:["id","text","checked"]}),name:"columns"},{fieldLabel:"Query",name:"query"},{fieldLabel:"Orader",name:"orader"},{width:60,anchor:null,xtype:"numberfield",fieldLabel:"Page Size",name:"pageSize"}],buttons:[{text:"Save",ref:"/saveBtn",handler:this.onSaveSetting,scope:this},{text:"Cancel",ref:"/cancelBtn",handler:this.exitSettingForm,scope:this}],listeners:{show:{scope:this,delay:100,fn:function(c){this.autoHeight=true;c.ownerCt.autoHeight=true;c.ownerCt.syncSize();this.syncSize()}}}})}return this[b]},onLoadSettingSuccess:function(a,b,k){var e=a.grid;var i=e.getColumnModel();var f=b;var d=f.findField("columns");var h=f.findField("columns").getStore();var c=[];var j=[];Ext.each(i.config,function(g){var l=!!!g.hidden;if(l){j.push(g.dataIndex)}c.push({id:g.dataIndex,text:g.header})},this);h.loadData(c);d.setValue(j.join(","))},onSaveSettingSuccess:function(e,d,b){var f=d.findField("title").getValue();e.setTitle(f);var c=d.findField("columns").getValue().split(",");if(!c.length){c=["projectName"]}var a=this.grid.getColumnModel();this.exitSettingForm();Ext.each(a.config,function(g,h){var j=(c.indexOf(g.dataIndex)<0);this.setHidden(h,j)},a)},columns:[{header:"\u9879\u76ee\u540d\u79f0",dataIndex:"projectName"},{header:"\u4efb\u52a1\u540d\u79f0",dataIndex:"taskName"},{header:"\u5de5\u4f5c\u5185\u5bb9",dataIndex:"workContext"},{header:"\u4efb\u52a1\u7c7b\u578b",dataIndex:"taskType"},{header:"\u8ba1\u5212\u7ed3\u675f\u65f6\u95f4",dataIndex:"taskPlanEndTime"}],buildColumns:function(){return this.columns},initComponent:function(){ProjectMilestonesGrid.superclass.initComponent.call(this)}});Global.Portal.PanelMC.add("ProjectMilestonesGrid",ProjectMilestonesGrid);var BrowserPic=Ext.extend(Ext.ux.PortletPanel,{title:"\u6d4f\u89c8\u5668\u4f7f\u7528\u6bd4\u4f8b",getStore:function(){if(!this._store){this._store=new Ext.data.JsonStore({autoLoad:true,url:"data/chart/browser.json",fields:["name","value"]})}return this._store},beforedestroy:function(){if(this._store){this._store.destroy();delete this._store}BrowserPic.superclass.beforedestroy.call(this)},createContent:function(){return new Disco.Ext.chart.PicChart({chartTitle:"\u6d4f\u89c8\u5668\u4f7f\u7528\u6bd4\u4f8b",store:this.getStore()})}});Global.Portal.PanelMC.add("BrowserPic",BrowserPic);var RiskCharts=Ext.extend(Ext.ux.ImgPortletPanel,{title:"\u6d4f\u89c8\u5668\u4f7f\u7528",url:"portalDemo.java?cmd=getPortalReport&img=browser.jpg"});Global.Portal.PanelMC.add("RiskCharts",RiskCharts);var WorkloadCharts=Ext.extend(Ext.ux.ImgPortletPanel,{title:"\u5de5\u4f5c\u91cf\u7edf\u8ba1\u56fe",url:"portalDemo.java?cmd=getPortalReport&img=baobiao2.jpg"});Global.Portal.PanelMC.add("WorkloadCharts",WorkloadCharts);Disco.loadJs("/plugins/disco-ajax/plugins/ShortCutPanel.js",false);var DiscoIntro=Ext.extendX(Ext.ux.PortletPanel,function(a){return{title:"DiscoEDP2.0\u7b80\u4ecb",buildContent:function(){return{xtype:"box",style:"padding:5px;background:White;",html:"<p style='text-align:center;color:red;font-size:20px'>\u589e\u5f3a\u7528\u6237\u4f53\u9a8c \u63d0\u9ad8\u5f00\u53d1\u6548\u7387 \u964d\u4f4e\u5f00\u53d1\u6210\u672c <br/><hr/></p><p style='text-indent:24px;'>  \u84dd\u6e90\u4f01\u4e1a\u5e94\u7528\u5feb\u901f\u5e73\u53f0(DiscoEDP)\u662f\u7531\u6210\u90fd\u84dd\u6e90\u4fe1\u606f\u6280\u672f\u6709\u9650\u516c\u53f8\u81ea\u4e3b\u7814\u53d1\u7684\u4e00\u6b3e\u4f01\u4e1a\u5e94\u7528\u5feb\u901f\u5f00\u53d1\u5e73\u53f0\uff0c\u5e73\u53f0\u901a\u8fc7\u6574\u5408\u4e3b\u6d41\u7684\u5f00\u6e90\u6846\u67b6\u53ca\u6280\u672f\uff0c\u63d0\u4f9b\u4e00\u5957\u4ece\u540e\u7aef\u6570\u636e\u6574\u5408\u5230\u524d\u7aef\u754c\u9762\u5c55\u793a\u4e3a\u4e00\u4f53\u7684\u4f01\u4e1a\u5e94\u7528\u5f00\u53d1\u89e3\u51b3\u65b9\u6848\uff0c\u8ba9\u7528\u6237\u8f7b\u677e\u5b9e\u73b0\u5404\u79cd\u4f01\u4e1a\u5e94\u7528\u7684\u5feb\u901f\u5f00\u53d1\uff0c\u964d\u4f4e\u5f00\u53d1\u6210\u672c\u53ca\u98ce\u9669\uff0c\u63d0\u9ad8\u6548\u76ca\u3002\u5e73\u53f0\u4e2d\u63d0\u4f9b\u4e86\u5305\u62ec\u5e38\u7528\u754c\u9762\u63a7\u4ef6\u3001\u57fa\u7840\u5e94\u7528\u8f6f\u4ef6\u9aa8\u67b6\u3001\u5feb\u901f\u4ee3\u7801\u751f\u6210\u5de5\u5177\u53ca\u5404\u79cd\u5178\u578b\u5e94\u7528\u62bd\u8c61\u7b49\u3002<br/>\u3000\u3000\u84dd\u6e90\u4f01\u4e1a\u5e94\u7528\u5feb\u901f\u5e73\u53f0(DiscoEDP)\u540e\u7aef\u4ee5EJS(Disco+JPA+Spring)\u6784\u67b6\u4e3a\u6838\u5fc3\uff0c\u524d\u7aef\u4ee5Ajax\u3001ExtJS\u53caDiscoRIA\u6846\u67b6\u4e3a\u4e3b\u4f53\u3002\u5728\u5b9e\u73b0\u4f01\u4e1a\u5e94\u7528\u5feb\u901f\u5f00\u53d1\u7684\u540c\u65f6\uff0c\u5f3a\u5927\u7684\u524d\u7aefUI\u529f\u80fd\u53ef\u4ee5\u4e3a\u5e94\u7528\u63d0\u9ad8\u7528\u6237\u4f53\u9a8c\u3002<br/>\u3000\u3000\u5e73\u53f0\u6d89\u53ca\u5230\u7684\u4e3b\u8981\u6280\u672f\uff1aSpring2.5\u53ca\u4ee5\u4e0a\u3001JPA1.0\u53ca\u4ee5\u4e0a\u3001   Hibernate 3.2\u53ca\u4ee5\u4e0a\u3001 Disco 1.2\u53ca\u4ee5\u4e0a\u3001ExtJS2.2\u53ca\u4ee5\u4e0a\u3001DiscoRIA 1.0\u53ca\u4ee5\u4e0a</p><p style='text-align:right;'><a style='margin-right:5px;color:#0000ff;' target='_blank' href='http://www.discotech.com/disco-edp.html'>\u66f4\u591a\u4fe1\u606f...</a></p>"}},createContent:function(){return{xtype:"box",html:"<p style='text-align:center;color:red;font-size:20px'>\u589e\u5f3a\u7528\u6237\u4f53\u9a8c \u63d0\u9ad8\u5f00\u53d1\u6548\u7387 \u964d\u4f4e\u5f00\u53d1\u6210\u672c <br/><hr/></p><p style='text-indent:24px;'>  \u84dd\u6e90\u4f01\u4e1a\u5e94\u7528\u5feb\u901f\u5e73\u53f0(DiscoEDP)\u662f\u7531\u6210\u90fd\u84dd\u6e90\u4fe1\u606f\u6280\u672f\u6709\u9650\u516c\u53f8\u81ea\u4e3b\u7814\u53d1\u7684\u4e00\u6b3e\u4f01\u4e1a\u5e94\u7528\u5feb\u901f\u5f00\u53d1\u5e73\u53f0\uff0c\u5e73\u53f0\u901a\u8fc7\u6574\u5408\u4e3b\u6d41\u7684\u5f00\u6e90\u6846\u67b6\u53ca\u6280\u672f\uff0c\u63d0\u4f9b\u4e00\u5957\u4ece\u540e\u7aef\u6570\u636e\u6574\u5408\u5230\u524d\u7aef\u754c\u9762\u5c55\u793a\u4e3a\u4e00\u4f53\u7684\u4f01\u4e1a\u5e94\u7528\u5f00\u53d1\u89e3\u51b3\u65b9\u6848\uff0c\u8ba9\u7528\u6237\u8f7b\u677e\u5b9e\u73b0\u5404\u79cd\u4f01\u4e1a\u5e94\u7528\u7684\u5feb\u901f\u5f00\u53d1\uff0c\u964d\u4f4e\u5f00\u53d1\u6210\u672c\u53ca\u98ce\u9669\uff0c\u63d0\u9ad8\u6548\u76ca\u3002\u5e73\u53f0\u4e2d\u63d0\u4f9b\u4e86\u5305\u62ec\u5e38\u7528\u754c\u9762\u63a7\u4ef6\u3001\u57fa\u7840\u5e94\u7528\u8f6f\u4ef6\u9aa8\u67b6\u3001\u5feb\u901f\u4ee3\u7801\u751f\u6210\u5de5\u5177\u53ca\u5404\u79cd\u5178\u578b\u5e94\u7528\u62bd\u8c61\u7b49\u3002<br/>\u3000\u3000\u84dd\u6e90\u4f01\u4e1a\u5e94\u7528\u5feb\u901f\u5e73\u53f0(DiscoEDP)\u540e\u7aef\u4ee5EJS(Disco+JPA+Spring)\u6784\u67b6\u4e3a\u6838\u5fc3\uff0c\u524d\u7aef\u4ee5Ajax\u3001ExtJS\u53caDiscoRIA\u6846\u67b6\u4e3a\u4e3b\u4f53\u3002\u5728\u5b9e\u73b0\u4f01\u4e1a\u5e94\u7528\u5feb\u901f\u5f00\u53d1\u7684\u540c\u65f6\uff0c\u5f3a\u5927\u7684\u524d\u7aefUI\u529f\u80fd\u53ef\u4ee5\u4e3a\u5e94\u7528\u63d0\u9ad8\u7528\u6237\u4f53\u9a8c\u3002<br/>\u3000\u3000\u5e73\u53f0\u6d89\u53ca\u5230\u7684\u4e3b\u8981\u6280\u672f\uff1aSpring2.5\u53ca\u4ee5\u4e0a\u3001JPA1.0\u53ca\u4ee5\u4e0a\u3001   Hibernate 3.2\u53ca\u4ee5\u4e0a\u3001 Disco 1.2\u53ca\u4ee5\u4e0a\u3001ExtJS2.2\u53ca\u4ee5\u4e0a\u3001DiscoRIA 1.0\u53ca\u4ee5\u4e0a</p><p style='text-align:right;'><a style='margin-right:5px;color:#0000ff;' target='_blank' href='http://www.disco.org.cn/disco-edp.html'>\u66f4\u591a\u4fe1\u606f...</a></p>"}}}});Global.Portal.PanelMC.add("DiscoIntro",DiscoIntro);
