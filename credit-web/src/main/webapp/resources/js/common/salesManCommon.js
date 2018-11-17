/**
 * 客户经理下拉框初始化
 */
$(function() {
	
	function _createToolBarSearchPart (name) {
		var searchHtml = 
			'<div id="T_Toolbar_' + name + '">'+
			'<form id="T_Toolbar_form_' + name + '">'+
				'<table cellpadding="1">'+
					'<tr>'+
						'<td style="width:40px;">姓名：</td>'+
						'<td style="width:110px"><input class="easyui-textbox" type="text"  style="width:100px" name="name" ></input></td>'+
						'<td style="width:40px;">工号：</td>'+
						'<td style="width:110px"><input class="easyui-textbox" type="text" style="width:100px" name="userCode" ></input></td>'+
						'<td>'+
							'<a href="#" class="easyui-linkbutton" onclick="$.clientManager_' + name + '.reloadDataGrid();"  iconCls="icon-search" plain="true">查询</a>'+
							'<a href="#" class="easyui-linkbutton" onclick="$.clientManager_' + name + '.clearSelectedValue();"  iconCls="icon-clear" plain="true">清空已选中值</a>'+
						'</td>'+
					'</tr>'+
				'</table>'+
			'</form>'+
		'</div>';
		$(document.body).append($(searchHtml));
		$.parser.parse('#T_Toolbar_' + name); 
	}
	/** 查找需要转换的集合 **/
	var collectSalesMan = $('input.custComboGrid');
	
	collectSalesMan.each(function(index){
		var jsonObj = eval('(' + $(this).attr('configValue') + ')');
		if ($.isEmpty(jsonObj)) {
			$.messager.alert('提示信息', '客户经理下拉框初始化，缺少参数！', 'warning');
			return;
		}
		
		eval('$.clientManager_' + index + ' = $.clientManager_' + index + ' || {};');
		eval('var tmp_clientManager = $.clientManager_' + index + ';');
		
		var width = jsonObj.width || 100;
		
		tmp_clientManager.baseQueryParam = jsonObj.baseParm || {};
//		alert($(this).prop('id'));
		
		_createToolBarSearchPart(index);
		
		$.extend(tmp_clientManager,{
			container : $(this),
			/** 数据源地址 **/
			dataGridUrl : global.contextPath + '/system/user/findEmployeeByEmployeeTypeDataGrid',
			/** 分页对象 **/
			pager : undefined,
			/** DataGrid表格对象 **/
			dataGrid : undefined,
			toolBarName : 'T_Toolbar_' + index,
			toolBar : $('#' + tmp_clientManager.toolBarName),
			/** 查询条件表单 **/
			searchForm : $('#T_Toolbar_form_' + index),
			_reload : function(){
				var queryData = undefined;
				if (tmp_clientManager.searchForm.length > 0) {
					var searchMsg = tmp_clientManager.searchForm.serialize();
					/** 对参数进行解码(显示中文) * */
					searchMsg = decodeURIComponent(searchMsg);
					queryData = $.serializeToJsonObject(searchMsg);
				}
				tmp_clientManager.container.combogrid('setValue','');
				queryData = queryData || {};
				queryData.url = tmp_clientManager.dataGridUrl;
				queryData = $.mergeMap(tmp_clientManager.baseQueryParam,queryData);
				tmp_clientManager.dataGrid.datagrid('reloadData',queryData);
			},
			/** 刷新DataGrid表格数据 **/
			reloadDataGrid : function() {
				tmp_clientManager.pg.page = 1;
				tmp_clientManager._reload();
			},
			/** 清空已选中的数据 **/
			clearSelectedValue : function() {
				tmp_clientManager.container.combogrid('setValue','');
			},
			pg : {
				'page' : 1,
				'rows' : 6
			}
		})
		
		tmp_clientManager.container.combogrid({
			width : width,
			panelWidth : 600,
			panelHeight : 250,
			toolbar : '#' + tmp_clientManager.toolBarName,
			idField : 'id',
			textField : 'name',
			pageSize : tmp_clientManager.pg.rows, //每页显示的记录条数，默认为10  
			pageList : [tmp_clientManager.pg.rows], //可以设置每页记录条数的列表  
			//fit: true, //自动大小 
			rownumbers : true, //序号 
			fitColumns: true,
			striped : true,
			editable : false,
			pagination : true, //是否分页 
			pg : tmp_clientManager.pg,
			columns : [[
			      { field : 'name', title : '姓名', width : 60 },
			      { field : 'userCode', title : '工号', width : 60 },
			      { field : 'orgName', title : '营业网点', width : 60 }
			]]
		});
		tmp_clientManager.dataGrid = tmp_clientManager.container.combogrid('grid');
		tmp_clientManager.pager = tmp_clientManager.dataGrid.datagrid('getPager');
		tmp_clientManager.pager.pagination({
			onSelectPage : function(pageNumber,pageSize) {
				tmp_clientManager.pg.page = pageNumber;
				tmp_clientManager.pg.rows = pageSize;
				tmp_clientManager._reload();
			}
		});
	})
	
})