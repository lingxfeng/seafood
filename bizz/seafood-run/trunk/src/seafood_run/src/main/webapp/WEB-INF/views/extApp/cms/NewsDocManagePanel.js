if (typeof Global === "undefined") {
	Global = {};
}
// 文章的栏目(根据登陆人不同得到不同部门对应的栏目)
/*
 * Global.newsDocDirLoader = new Disco.Ext.MemoryTreeLoader({ iconCls:
 * 'disco-tree-node-icon', varName: "Global.MY_SEND_DOC_DIR_LOADER",// 缓存Key
 * url: "newsDir.java?cmd=getNewDirTree&pageSize=-1&treeData=true&all=true",
 * listeners: { 'beforeload': function(treeLoader, node) {
 * treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id : ""); if
 * (typeof node.attributes.checked !== "undefined") {
 * treeLoader.baseParams.checked = false; } } } });
 */
Global.newsDocDirLoader = new Ext.tree.TreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.DEPT_DIR_LOADER",// 缓存Key
	url : "newsDir.java?cmd=getNewDirTree&pageSize=-1&treeData=true&all=true",
	listeners : {
		'beforeload' : function(treeLoader, node) {
			treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id
					: "");
			if (typeof node.attributes.checked !== "undefined") {
				treeLoader.baseParams.checked = false;
			}
		}
	}
});
Global.pptLoader = new Disco.Ext.MemoryTreeLoader({
	iconCls : 'disco-tree-node-icon',
	varName : "Global.PPT_LOADER",// 缓存Key
	url : "linkImgType.java?cmd=getPPt",
	listeners : {
		'beforeload' : function(treeLoader, node) {
			treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id
					: "");
			if (typeof node.attributes.checked !== "undefined") {
				treeLoader.baseParams.checked = false;
			}
		}
	}
});

Global.newsDocTemplateLoader = new Ext.tree.TreeLoader(
		{
			iconCls : 'disco-tree-node-icon',
			varName : "Global.TEMPLATEFILE_LOADER",// 缓存Key
			url : "templateFile.java?cmd=getTemplateFileTree&pageSize=-1&treeData=true&dir=wenzhang",
			listeners : {
				'beforeload' : function(treeLoader, node) {
					treeLoader.baseParams.id = (node.id.indexOf('root') < 0 ? node.id
							: "");
					if (typeof node.attributes.checked !== "undefined") {
						treeLoader.baseParams.checked = false;
					}
				}
			}
		});
/**
 * 已发表的文章 当前登陆人已发表的文章或如果当前登陆人是部门、支行、分行员可看到本部门下所有撰稿人已发表的文章
 * 
 * @class NewsDocManageListPanel
 * @extends Disco.Ext.CrudPanel
 */
NewsDocManageListPanel = Ext
		.extend(
				Disco.Ext.CrudPanel,
				{
					gridSelModel : 'checkbox',
					id : "newsDocManageListPanel",
					baseUrl : "newsDoc.java",
					autoLoadGridData : true,
					autoScroll : false,
					totalPhoto : 0,
					pageSize : 20,
					showAdd : true,
					showEdit : true,
					showRemove : true,
					showView : false,
					defaultsActions : {
						create : 'save',
						list : 'list',
						view : 'view',
						update : 'update',
						remove : 'remove'
					},
					gridViewConfig : {
						forceFit : true,
						enableRowBody : true,
						showPreview : true
					},
					// 记录用户的上次操作记录
					tempCookie : {
						dir : null,
						template : null,
						tree : null
					},
					edit : function() {
						var win = NewsDocManageListPanel.superclass.edit
								.call(this);
						if (win) {
							this.initWinFormData(this);
						}
					},
					initWinFormData : function(_this) {
						var record = this.grid.getSelectionModel()
								.getSelected();
						if (!record) {
							return Ext.Msg.alert("提醒", "请选择需编辑的数据");
						}
						Ext.Ajax.request({
							scope : this,
							url : this.baseUrl + "?cmd=getContent&id="
									+ record.get('id'),
							success : function(response) {
								var data = Ext.decode(response.responseText);
								data != null || (data = "");
								_this.editor.html(data);
								_this.editor.sync();
							}
						});
						var record = this.grid.getSelectionModel()
								.getSelected();
						var parentObj = record.get('dir');
						var dir = this.fp.form.findField('dir');
						// 编辑回显时如果有父级栏目及回显数据
						if (parentObj) {
							parentObj.title
									|| (parentObj.title = parentObj.name);
							dir.setOriginalValue(parentObj);
						}
						var parentObj = record.get('dir');
						var dir = this.fp.form.findField('dir');
						// 编辑回显时如果有父级栏目及回显数据
						if (parentObj) {
							parentObj.title
									|| (parentObj.title = parentObj.name);
							dir.setOriginalValue(parentObj);
						}
						var imgType = record.get("imgType");
						console.dir(imgType)
						if(imgType){
							imgType.title = imgType.title||imgType.text;
							this.fp.form.findField("imgType").setOriginalValue(imgType)
						}

					},
					topicRender : function(value, p, record) {
						return String
								.format(
										'<b><a style="color:green" href="news.java?cmd=show&id={0}" target="_blank">&nbsp查看</a></b>{1}<br/>',
										record.id, value)
					},
					statusRender : function(v) {
						if (v == 2) {
							return "<span style='color:green;'>已发布</span>";
						} else if (v == 1) {
							return "<span style='color:blue;'>待审核</span>";
						} else {
							return "<span style='color:wheat;'>未知状态</span>";
						}

					},
					toggleDetails : function(btn, pressed) {
						var view = this.grid.getView();
						view.showPreview = pressed;
						view.refresh();
					},
					create : function() {
						NewsDocManageListPanel.superclass.create.call(this);
						if (typeof this.editor == "object")
							this.editor.html("");
					},
					createWin : function() {
						return this.initWin(980, 700, "文章管理");
					},
					storeMapping : [ "id", "isTop", "sequence", "elite",
							"imgType", "title", "keywords", "source",
							"description", 'iconPath', "dir", "docType",
							"toBranch", "status", "createDate",{
								name : "dirId",
								mapping : "dir"
							}, {
								name : 'docTypeId',
								mapping : 'docType'
							} ],
					createForm : function() {
						var formPanel = new Ext.form.FormPanel(
								{
									frame : true,
									labelWidth : 60,
									fileUpload : true,
									autoScroll : true,
									labelAlign : 'right',
									defaults : {
										xtype : 'textfield'
									},
									items : [
											{
												name : "id",
												xtype : "hidden"
											},
											{
												width : 870,
												name : "title",
												emptyText : '文章标题',
												fieldLabel : "文章标题",
												allowBlank : false,
												blankText : '文章标题不能为空'
											},
											{
												width : 870,
												name : "keywords",
												emptyText : '文章简短标题在30个字符内',
												fieldLabel : "简短标题"
											},
											{
												width : 870,
												name : "description",
												xtype : 'textarea',
												emptyText : '文章简短标题在300个字符内',
												fieldLabel : "文章描述"
											},
											Disco.Ext.Util.columnPanelBuild({
												columnWidth : .33,
												items : {
													width : 850,
													name : 'iconPath',
													buttonText : '选择',
													fieldLabel : '文章图标',
													xtype : "fileuploadfield"
												}
											}, {
												columnWidth : .33,
												items : {
													width : 870,
													name : "url",
													fieldLabel : "外链地址"
												}
											}, {
												columnWidth : .34,
												items : [ ConfigConst.BASE
														.getDictionaryCombo(
																"docTypeId",
																"文章类型",
																"docTypes",
																"id") ]

											}),
											Disco.Ext.Util.columnPanelBuild({
												columnWidth : .33,
												items : {
													fieldLabel : '是否置顶',
													xtype : 'radiogroup',
													name : 'isTop',
													items : [ {
														boxLabel : '置顶',
														name : 'isTop',
														inputValue : 1
													}, {
														checked : true,
														boxLabel : '不置顶',
														name : 'isTop',
														inputValue : 0
													} ]
												}
											}, {
												columnWidth : .33,
												items : {
													fieldLabel : '是否推荐',
													xtype : 'radiogroup',
													name : 'elite',
													items : [ {
														boxLabel : '推荐',
														name : 'elite',
														inputValue : 1
													}, {
														checked : true,
														boxLabel : '不推荐',
														name : 'elite',
														inputValue : 0
													} ]
												}
											}, {
												columnWidth : .34,
												items : {
													name : "source",
													emptyText : '梦坊',
													fieldLabel : "文章来源"
												}
											}),
											Disco.Ext.Util
													.columnPanelBuild(
															{
																columnWidth : .33,
																items : {
																	name : 'template',
																	allowBlank : true,
																	blankText : '文章模板不能为空',
																	fieldLabel : '文章模板',
																	xtype : "treecombo",
																	valueField : "id",
																	hiddenName : "templateId",
																	displayField : "text",
																	tree : new Ext.tree.TreePanel(
																			{
																				autoScroll : true,
																				root : new Ext.tree.AsyncTreeNode(
																						{
																							id : "root",
																							text : "所有模板",
																							expanded : true,
																							iconCls : 'treeroot-icon',
																							loader : Global.newsDocTemplateLoader
																						}),
																				listeners : {
																					scope : this,
																					click : function(
																							node) {
																						this.tempCookie.template = node;
																					}
																				}
																			})

																}
															},
															{
																columnWidth : .33,
																items : {
																	name : 'dir',
																	allowBlank : false,
																	blankText : '文章栏目不能为空',
																	fieldLabel : '文章栏目',
																	xtype : "treecombo",
																	valueField : "id",
																	hiddenName : "dirId",
																	displayField : "title",
																	tree : new Ext.tree.TreePanel(
																			{
																				autoScroll : true,
																				root : new Ext.tree.AsyncTreeNode(
																						{
																							id : "root",
																							text : "所有栏目",
																							expanded : true,
																							iconCls : 'treeroot-icon',
																							loader : Global.newsDocDirLoader
																						}),
																				listeners : {
																					scope : this,
																					click : function(
																							node) {
																						this.tempCookie.tree = node;
																					}
																				}
																			})

																}
															},
															{
																columnWidth : .34,
																items : {
																	name : 'imgType',
																	allowBlank : true,
																	blankText : '图片库',
																	fieldLabel : '图片库',
																	xtype : "treecombo",
																	valueField : "id",
																	hiddenName : "imgTypeId",
																	displayField : "title",
																	tree : new Ext.tree.TreePanel(
																			{
																				autoScroll : true,
																				root : new Ext.tree.AsyncTreeNode(
																						{
																							id : "root",
																							text : "所有图库",
																							expanded : true,
																							iconCls : 'treeroot-icon',
																							loader : Global.pptLoader
																						}),
																				listeners : {
																					scope : this,
																					click : function(
																							node) {
																						this.tempCookie.dir = node;
																					}
																				}
																			})

																}
															}), {
												width : 940,
												height : 395,
												margins : {
													left : 10
												},
												hideLabel : true,
												name : 'content',
												xtype : 'textarea'
											} ],
									listeners : {
										scope : this,
										'render' : function() {
											var me = this;
											setTimeout(
													function() {
														// 动态加载kindeditor富文本编辑器
														var plugins = '/static/common/plugin/kindeditor/kindeditor-min.js';
														Disco
																.loadJs(
																		plugins,
																		false,
																		function() {
																			KindEditor.basePath = '/static/common/plugin/kindeditor/';
																			var id = me.fp.form
																					.findField('content').id;
																			me.editor = KindEditor
																					.create(
																							'#'
																									+ id,
																							{
																								// 所有的上传文件POST地址
																								uploadJson : '/fileUpload.java',
																								// 指定浏览远程图片的URL处理地址
																								fileManagerJson : '/fileUpload.java?cmd=showByKindeditor',
																								// true时显示浏览远程服务器按钮
																								afterBlur : function() {
																									this
																											.sync();// 编辑器失去焦点时同步KindEditor的值到textarea文本框
																								}
																							});
																		},
																		null,
																		Disco.ajaxCache);
													}, 1);
										}
									}
								});
						var fp = formPanel.getForm();
						return formPanel;

					},
					settingSequence : function() {
						var record = this.grid.getSelectionModel()
								.getSelected();
						if (!record) {
							this.alert("请先选择要操作的数据1！", "提示信息");
							return false;
						}
						if (!this.viewWinPanel) {
							this.viewWinPanel = this.createViewWinPanel();
						}
						this.viewWinPanel.show();
						var form = this.viewWinPanel.getComponent(0).form;
						form.clearData();
						form.reset();
						form.loadRecord(record)
					},
					mulisStatic : function() {
						Ext.Ajax.request({
							waitMsg : "正在执行操作，请稍候...",
							url : this.baseUrl + "?cmd=static",
							method : 'POST',
							params : "type=mulit",
							success : function(response) {
								var r = Ext.decode(response.responseText);
								Ext.Msg.alert("提示", r.data, this);
							},
							scope : this
						});
					},
					initComponent : function() {

						this.showRemove = true;
						this.batchRemoveMode = true;
						this.cm = new Ext.grid.ColumnModel([ {
							header : "排序号",
							sortable : true,
							width : 40,
							align : 'center',
							dataIndex : "sequence"
						}, {
							header : "主题",
							dataIndex : 'title',
							width : 200,
							renderer : this.topicRender
						}, {
							header : "栏目",
							sortable : true,
							width : 70,
							dataIndex : "dir",
							renderer : this.objectRender("name")
						}, {
							header : "日期",
							sortable : true,
							width : 70,
							dataIndex : "createDate",
							renderer: this.dateRender("Y-m-d H:i:s")
						},{
							header : "是否置顶",
							sortable : true,
							width : 70,
							align : 'center',
							dataIndex : "isTop",
							renderer : this.booleanRender
						}, {
							header : "文章类型",
							sortable : true,
							width : 70,
							dataIndex : "docType",
							renderer : function(obj) {
								if (obj) {
									return obj.title;
								}
							}
						}, {
							header : "是否推荐",
							sortable : true,
							width : 70,
							align : 'center',
							dataIndex : "elite",
							renderer : this.booleanRender
						}, {
							width : 60,
							align : 'center',
							sortable : true,
							header : "文章状态",
							dataIndex : "status",
							renderer : this.statusRender
						} ]);
						this.cm.setHidden(1, false);
						this.gridButtons = [
								{
									text : "页面静态化",
									iconCls : "upload-icon",
									menu : [
											{
												text : "生成所选",
												handler : this
														.executeCmd("static&type=single"),
												scope : this
											}, {
												text : "全部生成(慎重)",
												handler : this.mulisStatic,
												scope : this
											} ]
								}, {
									text : '发布文章',
									iconCls : "upload-icon",
									handler : this.executeCmd('up')
								}, {
									text : '取消发布',
									iconCls : "down",
									handler : this.executeCmd('down')
								}, {
									text : "上移",
									iconCls : "upload-icon",
									handler : this.swapSequence(""),
									scope : this
								}, {
									text : "下移",
									iconCls : "down",
									handler : this.swapSequence(true),
									scope : this
								} ]
						NewsDocManageListPanel.superclass.initComponent
								.call(this);
					}
				});
NewsDocManagePanel = Ext.extendX(Disco.Ext.TreeCascadePanel, function(
		superclass) {
	return {
		initComponent : function() {
			NewsDocManagePanel.superclass.initComponent.call(this);
			this.tree.on({
				scope : this,
				render : this.onTreeRender
			});
		},
		onTreeRender : function(_this) {
			// _this.expandAll();
		},
		treeCfg : {
			// rootVisible: false,
			title : "文章栏目信息",
			rootText : '所有栏目',
			expanded : true,
			autoScroll : true,
			rootIconCls : 'treeroot-icon',
			loader : Global.newsDocDirLoader
		},
		onTreeClick : function(node) {
			var id = (node.id != 'root' ? node.id : "");
			this.list.store.baseParams.dirId = id;
			superclass.onTreeClick.apply(this, arguments);
		},
		getPanel : function() {
			if (!this.panel) {
				this.panel = new NewsDocManageListPanel();
				this.panel.tree = this.tree;
				this.list = this.panel.grid;
			}
			return this.panel;
		}
	}
});
