Ext.ns('ng');
ng.RowEditor = Ext.extend(Ext.ux.grid.RowEditor,{
	bindHandler : Ext.emptyFn,
	makeFloating : function(cfg){
        this.floating = true;
        this.el = new Ext.Layer(Ext.apply({}, cfg, {
            shadow: Ext.isDefined(this.shadow) ? this.shadow : 'sides',
            shadowOffset: this.shadowOffset,
            constrain:false,
            zindex:1000,
            shim: this.shim === false ? false : undefined
        }), this.el);
    },
	onRender: function(){
        Ext.ux.grid.RowEditor.superclass.onRender.apply(this, arguments);
        this.el.swallowEvent(['keydown', 'keyup', 'keypress']);
	},
	startEditing: function(rowIndex, doFocus){
        if(this.stopEditing(true)===false){
        	return ;
        }
        if(Ext.isObject(rowIndex)){
            rowIndex = this.grid.getStore().indexOf(rowIndex);
        }
        if(this.fireEvent('beforeedit', this, rowIndex) !== false){
        	
            this.editing = true;
            var g = this.grid, view = g.getView(),
                row = view.getRow(rowIndex),
                record = g.store.getAt(rowIndex);

            this.record = record;
            this.rowIndex = rowIndex;
            this.values = {};
            if(!this.rendered){
                this.render(view.getEditorParent());
            }
            var w = Ext.fly(row).getWidth();
            this.setSize(w-20);
            
            if(!this.initialized){
                this.initFields();
            }
            var cm = g.getColumnModel(), fields = this.items.items, f, val;
            for(var i = 1, len = cm.getColumnCount(); i < len; i++){
                val = this.preEditValue(record, cm.getDataIndex(i));
                f = fields[i-1];
                f.setValue(val);
                this.values[f.id] = Ext.isEmpty(val) ? '' : val;
            }
            this.verifyLayout(true);
            var xy = Ext.fly(row).getXY();
            xy[0] = xy[0]+20;
            if(!this.isVisible()){
                this.setPagePosition(xy);
            } else{
                this.el.setXY(xy, {duration:0.15});
            }
            if(!this.isVisible()){
                this.show().doLayout();
            }
            if(doFocus !== false){
                this.doFocus.defer(this.focusDelay, this);
            }
        }
    },
	stopEditing : function(saveChanges){
			if(this.editing && !this.isValid() && saveChanges){
	            this.showTooltip(this.getErrorText().join(''));
	            return false;
	        }
	        this.editing = false;
	        if(!this.isVisible()){
	            return;
	        }
	        if(saveChanges === false || !this.isValid()){
	            this.hide();
	            this.fireEvent('canceledit', this, saveChanges === false);
	            return;
	        }
	        var changes = {},
	            r = this.record,
	            hasChange = false,
	            cm = this.grid.colModel,
	            fields = this.items.items;
	        for(var i = 1, len = cm.getColumnCount()-1; i < len; i++){
	            if(!cm.isHidden(i)){
	                var dindex = cm.getDataIndex(i+1);
	                
	                if(!Ext.isEmpty(dindex)){
	                    var oldValue = r.data[dindex],
	                        value = this.postEditValue(fields[i].getValue(), oldValue, r, dindex);
	                    if(String(oldValue) !== String(value)){
	                        changes[dindex] = value;
	                        hasChange = true;
	                    }
	                }
	            }
	        }
	        if(hasChange && this.fireEvent('validateedit', this, changes, r, this.rowIndex) !== false){
	            r.beginEdit();
	            Ext.iterate(changes, function(name, value){
	                r.set(name, value);
	            });
	            r.endEdit();
	            this.fireEvent('afteredit', this, changes, r, this.rowIndex);
	        }
	        this.hide();
	        return true;
	    },
	    handleMouseDown : function(g, rowIndex, e){
        if(e.button !== 0 || this.isLocked()){
            return;
        }
        var view = this.grid.getView();
        if(e.shiftKey && !this.singleSelect && this.last !== false){
            var last = this.last;
            this.selectRange(last, rowIndex, e.ctrlKey);
            this.last = last; // reset the last
            view.focusRow(rowIndex);
        }else{
            var isSelected = this.isSelected(rowIndex);
            if(e.ctrlKey && isSelected){
                this.deselectRow(rowIndex);
            }else if(!isSelected || this.getCount() > 1){
                this.selectRow(rowIndex, e.ctrlKey || e.shiftKey);
                view.focusRow(rowIndex);
            }
        }
    },
    initFields: function(){
        var cm = this.grid.getColumnModel(), pm = Ext.layout.ContainerLayout.prototype.parseMargins;
        this.removeAll(false);
        for(var i = 1, len = cm.getColumnCount(); i < len; i++){
            var c = cm.getColumnAt(i),
                ed = c.getEditor();
            if(!ed){
                ed = c.displayEditor || new Ext.form.DisplayField();
            }
            if(i == 0){
                ed.margins = pm('0 1 2 1');
            } else if(i == len - 1){
                ed.margins = pm('0 0 2 1');
            } else{
                if (Ext.isIE) {
                    ed.margins = pm('0 0 2 0');
                }
                else {
                    ed.margins = pm('0 1 2 0');
                }
            }
            ed.setWidth(cm.getColumnWidth(i));
            ed.column = c;
            if(ed.ownerCt !== this){
                ed.on('focus', this.ensureVisible, this);
                ed.on('specialkey', this.onKey, this);
            }
            this.insert(i, ed);
        }
        this.initialized = true;
    },
    verifyLayout: function(force){
        if(this.el && (this.isVisible() || force === true)){
            var row = this.grid.getView().getRow(this.rowIndex);
            this.setSize(Ext.fly(row).getWidth()-20, Ext.isIE ? Ext.fly(row).getHeight() + 9 : undefined);
            var cm = this.grid.colModel, fields = this.items.items;
            for(var i = 1, len = cm.getColumnCount()-1; i < len; i++){
                if(!cm.isHidden(i)){
                    var adjust = 0;
                    if(i === (len - 1)){
                        adjust += 3; // outer padding
                    } else{
                        adjust += 1;
                    }
                    fields[i].show();
                    fields[i].setWidth(cm.getColumnWidth(i) - adjust);
                } else{
                    fields[i].hide();
                }
            }
            this.doLayout();
            this.positionButtons();
        }
    },
    onKey : Ext.emptyFn,
    /*onKey: function(f, e){
        if(e.getKey() === e.ENTER){
            if(this.stopEditing(true)){
            	this.fireEvent('keyenter',f, e);
            }
            e.stopPropagation();
        }
    },*/
	onRowClick: function(g, rowIndex, e){
        if(this.clicksToEdit == 'auto'){
            var li = this.lastClickIndex;
            this.lastClickIndex = rowIndex;
            if(li != rowIndex && !this.isVisible()){
                return;
            }
        }
        if(!e.getTarget('div.x-grid3-row-checker')){
        	this.startEditing(rowIndex, false);
       		this.doFocus.defer(this.focusDelay, this, [e.getPoint()]);
        }
        
    }
});

Ext.namespace('ng');
ng.CrudMultiEditorPanel = Ext.extend(Ext.Panel,{
	layout : 'fit',
	entityIdName : 'id',
	border : false,
	/**
	 * @cfg {String} baseUrl
	 * 配置数据对应的地址,包括了增加删除修改等...
	 */
	baseUrl : null,
	/**
	 * @cfg {Array} storeMapping
	 * 配置gridStore的fields:如 ["id","name",....] 
	 */
	storeMapping : [],
	/**
	 * 是否显示分页控件
	 * @type Boolean
	 */
	pagingToolbar : true,
	/**
	 * 构建表格的列信息返回一个数组/或者是一个Ext.grid.ColumnModel对象及子类
	 */
	buildColumn : function(){return [];},
	/**
	 * @type {Array} hideButtons
	 * 	不显示的按钮
	 */
	hideButtons : [],
	/**
	 * 提交数据过滤
	 * @return {Boolean}
	 */
	getFilterDataFn : function(){return true;},
	/**
	 * 获取查询面板
	 * @return {} 
	 */
	getSearchPanel : function(){return null},
	gridPageSize : 20,
	/**
	 * 构建grid的store
	 * @return {Ext.data.Store}
	 */
	getGridStore : function(){
		if(!this._gridStore){
			this._gridStore = new Ext.data.JsonStore({
				root : 'result',
				autoLoad : true,
				autoDestroy : true,
				totalProperty : 'rowCount',
				pageSize : this.gridPageSize,
				pruneModifiedRecords : true,//清除所有已修改信息记录 ,在store执行remove时会清除被修改后的临时存储数据
				url : Ext.urlAppend(this.baseUrl,Ext.urlEncode({cmd:'list'})),
				fields : this.storeMapping
			});
		} 
		return this._gridStore ;
	},
	onCopyHanlder:function(){
		var records = this.grid.getSelections();
		if(records && records.length){
			this._copyRecords = records;
		}
	},
	onPasteHanlder:function(){
		if(this._copyRecords){
			Ext.Msg.prompt("操作提示","请输入行号!",function(btn,value){
				if(btn=='ok'){
					var rowIndex = window.parseInt(value);
					var s = this.grid.getStore(),count = s.getCount();
					rowIndex = rowIndex< 0 ? 0 : rowIndex ;
					rowIndex = Math.min(rowIndex,count);
					if(Ext.isNumber(rowIndex)){
						Ext.each(this._copyRecords,function(record,i){
							var data = Ext.apply({},record.data);
							var record = new Ext.data.Record(data,null);
							s.insert(rowIndex+i,record);
							record.set("id",null);
						},this);	
					}else{
						Disco.Ext.Msg.alert("请输入合法的行号!",null,function(){
							this.onPasteHanlder();
						},this);
					}
				}
			},this);
		}
	},
	onCopy : function(){
		this.stopEditing();
		this.onCopyHanlder();
	},
	onPaste : function(){
		this.stopEditing();
		this.onPasteHanlder();
	},
	buildGrid : function(){
		this.store = this.getGridStore();
		var columns = this.buildColumns();
		var checkboxSel = new Ext.grid.CheckboxSelectionModel();
		columns.unshift(checkboxSel);
		this.rowEditor = new ng.RowEditor;
		
		/*this.rowEditor.on('keyenter',function(){
			var s = this.store,recordType = s.recordType;
			var rowIndex = this.rowEditor.rowIndex;
    		s.insert(rowIndex+1,new recordType({}));
    		this.rowEditor.startEditing(rowIndex+1);
		},this);*/
		
		var grid = new Ext.grid.GridPanel({
			border : false,
			columns : columns,
			sm : checkboxSel,
			loadMask : true,
			enableHdMenu : false,
			plugins : [this.rowEditor],
			/*keys : [
				{
					ctrl : true ,
					scope : this,
					key : Ext.EventObject.C,
					fn : this.onCopyHanlder
				},{
					ctrl : true ,
					scope : this,
					key : Ext.EventObject.V,
					fn : this.onPasteHanlder
				}
			],*/
			bbar : this.pagingToolbar ? new Ext.ux.PagingComBo({
				rowComboSelect : true,
				pageSize : this.gridPageSize,
				store : this.store,
				displayInfo : true
			}) : null,
			store : this.store,
			viewConfig : {forceFit:true}
		});
		return grid;
	},
	//private 
	getSelectIds : function(){
		 var g = this.grid,sm=g.getSelectionModel();
		 var records = sm.getSelections();
		 var ids = Ext.invoke(records,'get','id');
		 return ids;
	},
	//private
	successHanlder:function(response,option){
		var data = Ext.decode(response.responseText);
		if(Ext.getObjVal(data,'success')){
			Disco.Ext.Msg.alert("操作成功",null,function(){
				this.store.removeAll();
				this.store.load();
			},this);
		}
	},
	onCreate : function(){
		var recordType = this.store.recordType;
		this.store.insert(0,new recordType({}));
		this.rowEditor.startEditing(0);
	},
	onUpdate : function(){
		this.stopEditing();
		var g = this.grid,s = g.getStore();
		var mrs = s.getModifiedRecords();
		if(mrs.length){
			var ps = Ext.arr2obj(Ext.pluck(mrs,'data'),s.fields.keys,this.getFilterDataFn);
    		Ext.Ajax.request({
    			url : Lanyo_Ajax.formatUrl(this.baseUrl,'saveOrUpdate'),
    			scope : this,
		 		params : ps,
		 		success : this.successHanlder
    		});
		}
	},
	onDestroy : function(){
		this.stopEditing();
		var ids = this.getSelectIds();
		if(ids && ids.length){
			Ext.Ajax.request({
				scope : this,
		 		url : Lanyo_Ajax.formatUrl(this.baseUrl,'destroy'),
		 		params : {id:ids},
		 		success : this.successHanlder
		 	});
		}
	},
	onRemove : function(){
		 this.stopEditing();
		 var ids = this.getSelectIds();
		 for (var i = ids.length-1; i >=0; i--) {
		 	if(!ids[i]){ids.removeAt(i);}
		 }
		 if(ids && ids.length){
		 	Ext.Msg.confirm("操作提示","是否要删除选中数据?",function(btn){
		 		if(btn=='yes'){
		 			Ext.Ajax.request({
				 		url : Ext.urlAppend(this.baseUrl,Ext.urlEncode({cmd:'remove'})),
				 		scope : this,
				 		params : {id:ids},
				 		success : this.successHanlder
				 	});	
		 		}
		 	},this);
		 }else{
		 	this.stopEditing();
			var records = this.grid.getSelections();
			Ext.each(records,function(record){
				this.store.remove(record);
			},this);
		 }
	},
	//private 
	stopEditing:function(){
		if(this.rowEditor){
			this.rowEditor.stopEditing.apply(this.rowEditor,Ext.args());
		}
	},
	onAdvanceSearch : function(){
		this.stopEditing();
		var sp = this.getSearchPanel();
		if(!sp) return false;
		Disco.Ext.Window.show({
			items : sp,
			width : 480,
			height : 160,
			single : false,
			title : '高级查询',
			buttons : Disco.Ext.Window.YESNO,
			scope : this,
			handler : function(btn,win,fp){
				if(btn=='yes'){
					var values = sp.getForm().getValues();
					this.store.baseParams = values;
					this.store.removeAll();
					this.store.load();
					win.hide();
				}else{
					win.hide();
				}
			}
		});
	},
	//private 
	getImportFormPanel : function(){
		if(!this._importFormPanel){
			this._importFormPanel = new Ext.form.FormPanel({
				id : "crudExportPanel",
				fileUpload : true,
				border : false,
				bodyStyle : 'padding:5px;',
				items : [ {
					xtype : "fieldset",
					title : "选择数据文件",
					autoHeight : true,
					items : {
						xtype : "textfield",
						hideLabel : true,
						inputType : "file",
						name : "file",
						anchor : "-20",
						regex : /.xls$/,
						regexText : '有效格式为(*.xls),请选择xls格式的文件!'
					}
				}, {
					xtype : "fieldset",
					title : "导入说明",
					html : "点击浏览,选择你要上传的文件!</br>有效格式为(*.xls),如:项目.xls",
					height : 70
				}]
			});
		}
		return this._importFormPanel;
	}, 
	onImport : function(){
		this.stopEditing();
		var url = Lanyo_Ajax.formatUrl(this.baseUrl,"import");
		var queryUrl =url.substring(url.indexOf('?')+1);
		Disco.Ext.Window.show({
			single : false ,
			width : 300,
			height : 220,
			scope : this,
			items : this.getImportFormPanel(),
			buttons : Disco.Ext.Window.YESNO,
			handler : function(btn,win,fp){
				if(btn=='yes'){
					var fp = fp.getForm();
					if(fp.isValid()){
						fp.baseParams = Ext.urlDecode(queryUrl);
						fp.submit({
							scope : this,
							waitMsg : '导入中...',
							url  : url,
							success : function(form,action){
								win.hide();
								if(Ext.getObjVal(action,'result.success')){
									Disco.Ext.Msg.alert("操作成功",null,function(){
										this.store.load();
									},this);
								}
							}
						});
					}
				}else{
					win.hide();
				}
			}
		});
	},
	getButtons : function(){
		return [
			{text : '添加',iconCls:'add',itemId:'add',handler:this.onCreate,scope:this},
			{text : '修改',iconCls:'edit',itemId:'edit',handler:this.onUpdate,scope:this},
			{text : '作废',iconCls:'recycle',itemId:'destroy',handler:this.onDestroy,scope:this},
			{text : '删除',iconCls:'delete',itemId:'remove',handler:this.onRemove,scope:this},
			{text : '复制',iconCls:'copy',itemId:'copy',handler:this.onCopy,scope:this},
			{text : '黏贴',iconCls:'paste',itemId:'paste',handler:this.onPaste,scope:this},
			{text : '进阶查询',iconCls:'advance-search-icon',itemId:'advanceSearch',handler:this.onAdvanceSearch,scope:this},
			{text : '导入资料',iconCls:'import-icon',itemId:'import',handler:this.onImport,scope:this},
			'->',{
				width : 100,
				itemId : "search",
				xtype : "searchfield",
				paramName : 'searchKey',
				store : this.getGridStore()
			}
		];
	},
	buildTbar : function(){
		var buttons = this.getButtons();
		if(this.hideButtons.length){
			var tempBtns = [];
			Ext.each(buttons,function(btn){
				if(!this.hideButtons.contains(btn.itemId)){
					tempBtns.push(btn);
				}
			},this);
			return tempBtns;
		}else{
			return buttons;
		}
	},
	beforeDestroy : function(){
		if(this.rowEditor){
			this.rowEditor.stopEditing(false);
			Ext.destroy(this.rowEditor);
		}
		if(this._searchPanel){
			Ext.destroy(this._searchPanel);
		}
		if(this._importFormPanel){
			Ext.destroy(this._importFormPanel);	
		}
		delete this._copyRecords; 
		ng.CrudMultiEditorPanel.superclass.beforeDestroy.call(this);
	},
	initEvents:function(){
		ng.CrudMultiEditorPanel.superclass.initEvents.call(this);
	},
	initComponent : function(){
		this.grid = this.buildGrid();
		this.items = this.grid;
		this.tbar = this.buildTbar();
		this.grid.getStore().on('load',function(){this._copyRecords=[]},this);
		ng.CrudMultiEditorPanel.superclass.initComponent.call(this);
	}
});



/**
 * 销售利润
 * @class SellingProfitPanel
 * @extends Ext.Panel
 */
SellingProfitPanel = Ext.extend(ng.CrudMultiEditorPanel,{
	baseUrl : 'sellingProfit.ejf', 
	storeMapping :  ['id','project','sys','object','strIsActive',{name : 'code',allowBlank : false},'type','unit',{type : 'num',name : 'num'},{type : 'number',name : 'income'},{type : 'number',name : 'cost'}],
	buildColumns : function(){
		this.depotLocationEditor=new Disco.Ext.SmartCombox(Ext.applyIf({disableCreateObject:true,returnObject:true},ConfigConst.BASE.getDictionaryCombo(
														"type", "产品归属",
														"sellingProfitType")));
		return [
			{
				header:'No',dataIndex:'id',
				editor : new Ext.form.DisplayField({style:'padding-left:5px;font:11px arial,tahoma,helvetica,sans-serif;'})
			},{
				header:'项目',dataIndex:'project',editor : new Ext.form.TextField
			},
			{header:'作废',dataIndex:'strIsActive',editor : new Ext.form.DisplayField({style:'padding-left:5px;font:11px arial,tahoma,helvetica,sans-serif;'})},
			{header:'产品归属',dataIndex:'type',editor:this.depotLocationEditor,renderer:Disco.Ext.Util.comboxRender},
			{header:'SYS',dataIndex:'sys',editor : new Ext.form.TextField},
			{header:'细类产品代码',dataIndex:'code',editor : new Ext.form.TextField({allowBlank:false})},
			{header:'计量单位',dataIndex:'unit',editor : new Ext.form.TextField},
			{header:'销售数量',dataIndex:'num',editor : new Ext.form.NumberField()},
			{header:'销售收入',dataIndex:'income',editor : new Ext.form.NumberField()},
			{header:'销售成本',dataIndex:'cost',editor : new Ext.form.NumberField()}
		];
	}
});

SellingProfitPanel = Ext.extend(ng.CrudMultiEditorPanel,{
	baseUrl : 'sellingProfit.ejf', 
	storeMapping :  ['id','project','sys','object','strIsActive',{name : 'code',allowBlank : false},'type','unit',{type : 'num',name : 'num'},{type : 'number',name : 'income'},{type : 'number',name : 'cost'}],
	buildColumns : function(){
		this.depotLocationEditor=new Disco.Ext.SmartCombox(Ext.applyIf({disableCreateObject:true,returnObject:true},ConfigConst.BASE.getDictionaryCombo(
														"type", "产品归属",
														"sellingProfitType")));
		return [
			{
				header:'No',dataIndex:'id',
				editor : new Ext.form.DisplayField({style:'padding-left:5px;font:11px arial,tahoma,helvetica,sans-serif;'})
			},{
				header:'项目',dataIndex:'project',editor : new Ext.form.TextField
			},
			{header:'作废',dataIndex:'strIsActive',editor : new Ext.form.DisplayField({style:'padding-left:5px;font:11px arial,tahoma,helvetica,sans-serif;'})},
			{header:'产品归属',dataIndex:'type',editor:this.depotLocationEditor,renderer:Disco.Ext.Util.comboxRender},
			{header:'SYS',dataIndex:'sys',editor : new Ext.form.TextField},
			{header:'细类产品代码',dataIndex:'code',editor : new Ext.form.TextField({allowBlank:false})},
			{header:'计量单位',dataIndex:'unit',editor : new Ext.form.TextField},
			{header:'销售数量',dataIndex:'num',editor : new Ext.form.NumberField()},
			{header:'销售收入',dataIndex:'income',editor : new Ext.form.NumberField()},
			{header:'销售成本',dataIndex:'cost',editor : new Ext.form.NumberField()}
		];
	},
	/**
	 * 创建查询面板
	 */
	getSearchPanel:function(){
		if(!this._searchPanel){
			this._searchPanel = new Ext.FormPanel({
				border : false ,
				bodyStyle : 'padding:5px;',
				labelAlign:'right',
				items : [
					Disco.Ext.Util.buildColumnForm({
						fieldLabel : '项目',
						name : 'project'
					},new Disco.Ext.SmartCombox(Ext.applyIf({disableCreateObject:true,returnObject:true},ConfigConst.BASE.getDictionaryCombo(
						"type", "产品归属",
						"sellingProfitType"))
					)),
						{
						anchor : '-20',
						fieldLabel : '销售收入',
						xtype: 'compositefield',
						items : [
							{
								flex : 1,
								allowDecimals:false,
		                        xtype     : 'numberfield',
		                        name      : 'startIncome',
		                        hideLabel :　true
		                    },{
                               xtype: 'displayfield',
                               value: '到'
                           },{
                           		flex : 1,
                           		allowDecimals:false,
		                        xtype     : 'numberfield',
		                        name      : 'endIncome',
		                        hideLabel :　true
		                    }

						]
					},{
						anchor : '-20',
						fieldLabel : '销售成本',
						xtype: 'compositefield',
						items : [
							{
								flex : 1,
								allowDecimals:false,
		                        xtype     : 'numberfield',
		                        name      : 'startCost',
		                        hideLabel :　true
		                    },{
                               xtype: 'displayfield',
                               value: '到'
                           },{
                           		flex : 1,
                           		allowDecimals:false,
		                        xtype     : 'numberfield',
		                        name      : 'endCost',
		                        hideLabel :　true
		                    }

						]
					
					}
				]
			});	
		};
		return this._searchPanel;
	}
	
});
//{name:"productSn",mapping:"product.sn"}
ProductCrudMultiEditorPanelxDemo = Ext.extend(ng.CrudMultiEditorPanel,{
	baseUrl : 'productCrudMultiEditor.ejf', 
	hideButtons : ['advanceSearch'],//隐藏
	storeMapping : ["id","product","productSn",{name:"productTitle",mapping:"product.title"}, {name:"brand",mapping:"product.brand"}, {name:"spec",mapping:"product.spec"}, {name:"model",mapping:"product.model"},{name:"unit",mapping:"product.unit"}, "price","num", "remark","totalAmount","vdate", "totalAmount","salePrice"],
	autoCountData:function(record){
		var p = record.get("price");
		var n = record.get("num");
		var total = p && n ? p * n : 0;
		record.set("totalAmount",parseFloat(total));
	},
	autoAddLastRow:function(){
   		var g = this.grid,s = g.store;
   		if(s.getCount()<1 || Ext.getObjVal(s.getAt(s.getCount()-1).get("product"),'id')){
   			var sm = g.getSelectionModel();
			s.add(new s.recordType({color:''}));
		}
   	},
	selectRowData:function(r){
   		var rowIndex = this.rowEditor.rowIndex;
   		
   		this.rowEditor.stopEditing();
		
		var obj = Ext.apply({}, r);
		var record = this.grid.store.getAt(rowIndex);
		this.selectRowDataHandler(obj,record);
		this.autoCountData(record);
		this.autoAddLastRow();
		
		this.rowEditor.startEditing(rowIndex,true);
   	},
   	selectRowDataHandler : function(obj,record) {
		obj.toString = function(){return this.id?this.id:this};
		record.set("product", obj);
		record.set("productTitle", obj.title);
		record.set("productSn", obj.sn);
		record.set("spec", obj.spec);
		record.set("brand", obj.brand);
        record.set("colorSn", obj.colorSn);
		record.set("model", obj.model);
		record.set("unit", obj.unit);
		record.set("price", obj.salePrice);
		record.set("marketPrice", obj.marketPrice);
	},
	readOnlyRender : Disco.Ext.Util.readOnlyRender,
	objectRender : Disco.Ext.Util.objectRender,
	buildColumns : function(){
		this.productEditor=new ProductComboBox(Ext.apply({},{
				 returnObject:true,
				 name:"productInput",
				 hiddenName:"productInput",
				 displayField:"sn",
				 valueField:"id",			
				 width:300,	
				 choiceValue:this.selectRowData.createDelegate(this)
				 },ConfigConst.CRM.product));
		var moenyRender = function(v){
			v = parseFloat(v);
			if(Ext.isNumber(v))
				return v.toFixed(2);
			return 0.00;
		};
		return [
	 		 {header:"产品编号",dataIndex:"productSn",editor:this.productEditor},
	 		 {header:"产品名称",dataIndex:"productTitle",renderer:this.readOnlyRender()},
	 		 {header:"品牌",dataIndex:"brand",editor:new Ext.form.LabelField({renderer:this.objectRender("title")}),renderer:this.objectRender("title",Disco.Ext.Util.readOnlyGridCellColor)},
			 {header:"单位",dataIndex:"unit",renderer:this.objectRender("title",Disco.Ext.Util.readOnlyGridCellColor)},
			 {header:"单价",dataIndex:"price",renderer:this.readOnlyRender()},
			 {header:"数量",dataIndex:"num",editor:new Ext.form.NumberField({allowDecimals:false})},
			 {header:"价格合计",dataIndex:"totalAmount",renderer:this.readOnlyRender(moenyRender)},
			 {header:"备注",dataIndex:"remark",editor:new Ext.form.TextField()}
		];
	},
	onRowEditorAfteredit : function(rowEditor,changes,r,rowIndex){
		var record = this.grid.getStore().getAt(rowIndex);
		if(record){
			this.autoCountData(record);
		}
	},
	getFilterDataFn : function(data){
		return !!Ext.getObjVal(data,'product.id');
	},
	initComponent : function(){
		ProductCrudMultiEditorPanelxDemo.superclass.initComponent.call(this);
		this.rowEditor.on('afteredit',this.onRowEditorAfteredit,this);
	}
}); 

//Y-m-dTH:i:s