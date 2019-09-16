jQuery.extend(jQuery.jgrid,{
					defaults:{
						 recordtext: "显示 {0} - {1}条数据	共 {2}条数据",  
						 emptyrecords: "没有数据",  
						 loadtext: "正在加载...",  
						 pgtext : "当前第 {0}页	共 {1}页"  
					},
					edit : {  
						addCaption: "添加",  
						editCaption: "修改",  
						bSubmit: "保存",  
						bCancel: "取消",  
						bClose: "关闭"
						 },
					 view : {       
							caption: "查看",  
							bClose: "关闭"  
						},
					search:{
						caption:"查询",
						Find:"查询",
						Reset:"重置",
						odata:[{oper:'eq',text:"等于"},{oper:'ne',text:'不等于'},{oper:'lt',text:'小于'},{oper:'gt',text:'大于'},{oper:'cn',text:'包含'}],
						groupOps: [ { op: "AND", text: "并列" }, { op: "OR", text: "或者" } ]  
					},
					del:{
						msg:"确定要删除当前记录吗？ ",
						caption:"删除",
						bSubmit:"确定",
						bCancel:"取消"
					}
});
jQuery.extend(jQuery.jgrid.nav,{
	alertcap: "警告",  
	alerttext: "请先选择一行"
});
//初始化表格
function initGrid(curData){
	jQuery(curData.tableId).jqGrid({
					url: curData.baseUrl+'?cmd=list',
					datatype: "json",
					mtype: 'POST',
					colNames: curData.curcolNames,
					colModel: curData.curcolModel,
					prmNames:{
						oper:"cmd",
						editoper:"update",
						addoper:"save",
						deloper:"remove"
					},
					rowList: [10, 20, 30],
					viewrecords: true,
					jsonReader: {
						root: "result",
						total: "pages",
						page: "currentPage",
						records: "rowCount",
						repeatitems: false
					},
					pager: jQuery(curData.paperId),
					rowNum: 10,
					width: 'auto',
					height: 'auto',
					editurl: curData.baseUrl,//nothing is saved
					caption: curData.tableTitle,
					loadComplete : function() {
						var table = this;
						setTimeout(function(){
							updatePagerIcons(table);
							enableTooltips(table);
						}, 0);
					},
					rownumbers:true,
					altRows: true,
					multiselect: true,
			        multiboxonly: true
					});
			//navButtons
			jQuery(curData.tableId).jqGrid('navGrid',curData.paperId,
				{ 	//navbar options
					edit: true,
					editicon : 'icon-pencil blue',
					add: true,
					addicon : 'icon-plus-sign purple',
					del: true,
					delicon : 'icon-trash red',
					search: true,
					searchicon : 'icon-search orange',
					refresh: true,
					refreshicon : 'icon-refresh green',
					view: true,
					viewicon : 'icon-zoom-in grey'
				},{
					//编辑
					recreateForm: true,
					closeAfterEdit:true,
					beforeShowForm : function(e) {
							var form = $(e[0]);
							style_edit_form(form);
					}
				},{
					//新增
					recreateForm: true,
					closeAfterAdd:true,
					beforeShowForm : function(e) {
							var form = $(e[0]);
							style_edit_form(form);
					}
				},{
					//删除
					recreateForm: true
				},{
					recreateForm: true,
					multipleSearch: true,
					sopt:['eq','ne','lt','gt','cn']
				}
			);
			function updatePagerIcons(table) {
				var replacement = 
				{
					'ui-icon-seek-first' : 'icon-double-angle-left bigger-140',
					'ui-icon-seek-prev' : 'icon-angle-left bigger-140',
					'ui-icon-seek-next' : 'icon-angle-right bigger-140',
					'ui-icon-seek-end' : 'icon-double-angle-right bigger-140'
				};
				$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
					var icon = $(this);
					var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
					if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
				})
			}
			function style_edit_form(form){
				for(var i = 0,c=curData.curcolModel.length;i<c;i++){
					if(curData.curcolModel[i].sortType == "date"){
						form.find('input[name='+curData.curcolModel[i].index+']').datepicker({format:'yyyy-mm-dd' , autoclose:true})
					}
					if(curData.curcolModel[i].edittype == "checkbox"){
						var id = $(curData.tableId).jqGrid('getGridParam','selrow');
						var rowDatas = $(curData.tableId).jqGrid('getRowData', id);
						var row = rowDatas[curData.curcolModel[i].index];
						if(row && row.indexOf("checked")!=-1){
							form.find('input[name='+curData.curcolModel[i].index+']').attr("checked",true).addClass('ace ace-switch ace-switch-5').after('<span class="lbl"></span>');
						}else{
							form.find('input[name='+curData.curcolModel[i].index+']').addClass('ace ace-switch ace-switch-5').after('<span class="lbl"></span>');
						}
					}
				}
			}
}
//对错格式化
function yesnoCheck( cellvalue, options, cell ) {
					if(cellvalue==options.colModel.editoptions.value.split(":")[0]){
						return '<input type="checkbox" class="ace ace-switch ace-switch-5" checked disabled /><span class="lbl"></span>';
					}else{
						return '<input type="checkbox" class="ace ace-switch ace-switch-5" disabled /><span class="lbl"></span>';
					}
}
function enableTooltips(table) {
					$('.navtable .ui-pg-button').tooltip({container:'body'});
					$(table).find('.ui-pg-div').tooltip({container:'body'});
				}
