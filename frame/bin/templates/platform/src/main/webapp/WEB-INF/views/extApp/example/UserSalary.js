/**
 * 用户薪水
 * @class UserSalary
 * @extends Disco.Ext.CrudRowEditorPanel
 */
UserSalary = Ext.extend(Disco.Ext.CrudRowEditorPanel, {
			border : false,
			storeMapping : ['id',{
						name : 'name',
						type : 'string',
						allowBlank : false
					}, {
						name : 'email',
						type : 'string',
						allowBlank : false
					}, {
						name : 'registerTime',
						type : 'date',
						dateFormat: 'Y-m-dTH:i:s',
						allowBlank : false
					}, {
						name : 'salary',
						type : 'float',
						allowBlank : false
					},{
						name : 'sex',
						type:'bool'
					}, {
						name : 'active',
						type : 'bool'
					}],
			baseUrl : 'userSalary.ejf',
			initComponent : function(){
				//this.gridSm = new Ext.grid.CheckboxSelectionModel();
				this.columns = [{
						id : 'name',
						header : '姓名',
						dataIndex : 'name',
						width : 220,
						sortable : true,
						editor : {
							xtype : 'textfield',
							allowBlank : false
						}
					}, {
						header : '邮箱',
						dataIndex : 'email',
						width : 150,
						sortable : true,
						editor : {
							xtype : 'textfield',
							allowBlank : false,
							vtype : 'email'
						}
					}, {
						xtype : 'datecolumn',
						header : '注册日期',
						dataIndex : 'registerTime',
						format : 'Y-m-d H:i:s',
						width : 100,
						sortable : true,
						editor : {
							xtype : 'datefield',
							allowBlank : false,
							format : 'Y-m-d H:i:s'
							// minValue: '01/01/2006',
							// minText: 'Can\'t have a start date before the
							// company existed!',
							// maxValue: (new
							// Date().add(Date.DAY,10)).format('m/d/Y')
						}
					}, {
						xtype : 'numbercolumn',
						header : '薪水',
						dataIndex : 'salary',
						format : '$0,0.00',
						width : 100,
						sortable : true,
						editor : {
							xtype : 'numberfield',
							allowBlank : false,
							minValue : 1,
							maxValue : 150000
						}
					}, {
						xtype : 'booleancolumn',
						header : '性别',
						dataIndex : 'sex',
						width : 100,
						sortable : true,
						trueText : '男',
						falseText : '女',
						editor : {
							xtype : 'combo',
							mode: 'local',
							triggerAction : 'all',
							valueField:'value',
							displayField:'text',
							disableChoice : true,
							store : new Ext.data.SimpleStore({
								fields : ['text','value'],
								data : [['男',true],['女',false]]
							})
						}
					}, {
						xtype : 'booleancolumn',
						header : '是否活跃',
						dataIndex : 'active',
						align : 'center',
						width : 50,
						trueText : '是',
						falseText : '否',
						editor : {
							style:'text-align:center;',
							xtype : 'checkbox',
							listeners:{
								scope : this,
								afterrender:function(field){
									field.wrap.setStyle('textAlign','center');
								}
							}
						}
					}];
			UserSalary.superclass.initComponent.call(this);
			}
	});
