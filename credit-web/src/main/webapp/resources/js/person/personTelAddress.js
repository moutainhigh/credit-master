$(function() {
	
	$.telAddress  = {
		/** 电话，地址所绑定的对象类型(zdsys.Borrower：客户  zdsys.Contact：客户的联系人) **/
		className : '',
		/** 客户编号 或 联系人编号 **/
		objectId : '',
		/** 编辑联系电话信息页面地址（内嵌页） **/
		editTelInfoUrl : global.contextPath + '/personTel/jumpEditPage',
		/** 编辑联系地址信息页面地址（内嵌页） **/
		editAddressInfoUrl : global.contextPath + '/personAddress/jumpEditPage',
		/** 更新联系电话信息地址 **/
		telSaveOrUpdateUrl : global.contextPath + '/personTel/saveOrUpdate',
		/** 更新联系地址信息地址 **/
		addressSaveOrUpdateUrl : global.contextPath + '/personAddress/saveOrUpdate',
		/** 查询已绑定的联系电话数据 **/
		queryTelDataGridUrl : global.contextPath + '/personTel/search',
		/** 查询已绑定的联系地址数据 **/
		queryAddressDataGridUrl : global.contextPath + '/personAddress/search',
		
		/** 客户记录(前台选中的行对象) **/
		personRow : undefined,
		
		/** 显示联系人窗口 **/
		editTelAddressWin : $('#editTelAddressWin'),
		
		/** 联系电话数据表格 **/
		telDataGrid : $('#telDataGrid'),
		/** 联系地址数据表格 **/
		addressDataGrid : $('#addressDataGrid'),
		
		/** 每页显示的记录条数，默认为10 **/
		pageSize : 10,
		/** 设置每页记录条数的列表 **/
		pageSizeList : [10,20,30,40,50],
		
		saveItem : function (index) {
			var dataGridType = $(this).datagrid('options').dataGridType;
			if ($.isEmpty(dataGridType)) {
				$.messager.alert('提示信息','表格缺少参数!','warning');
				return ;
			}
			/** 请求地址 **/
			var requestUrl = '';
			if (dataGridType == 'tel') {
				requestUrl = $.telAddress.telSaveOrUpdateUrl;
			} else if (dataGridType == 'address') {
				requestUrl = $.telAddress.addressSaveOrUpdateUrl;
			}
			/** 临时存放表格对象，方便AJAX 回调方法中使用 **/
			$.telAddress.dataGridTmp = $(this);
			$.telAddress.dataGridTmpIndex = index;
			
			var row = $(this).datagrid('getRows')[index];
			var formObj = $(this).datagrid('getRowDetail',index).find('form:eq(0)');
			if (formObj.form('validate')) {
				/** 验证手机号和固话格式 **/
				if (dataGridType == 'tel') {
					var ss = formObj.serialize();
					var obj = $.serializeToJsonObject(decodeURIComponent(ss));
					/** 电话类型 **/
					var telType = obj.telType;
					/** 电话号码 **/
					var content = obj.content;
					if (telType == '手机') {
						if (!$.fn.validatebox.defaults.rules.mobile.validator(content)) {
							$.messager.alert('警告','输入手机号码格式不正确!','warning');
							return;
						}
					} else {
						if (!$.fn.validatebox.defaults.rules.tel.validator(content)) {
							$.messager.alert('警告','输入固话格式不正确（XXXX-XXXXXXX）!','warning');
							return;
						}
					}
				}
				
				$.ajaxPackage({
					type : 'get', 
					url : requestUrl,
					data : formObj.serialize(),
					dataType : "json",
					success : function (data,textStatus,jqXHR) {
						var resCode = data.resCode;
						var resMsg = data.resMsg;
						var attachment = data.attachment;
						if (resCode == '000000') {
							$.messager.alert('异常信息','操作成功');
							$.telAddress.dataGridTmp.datagrid('updateRow',{
								index: $.telAddress.dataGridTmpIndex,
								row: attachment
							});
							
							/** 刷新联系人数据 **/
							if (!$.contact.showContactWin.is(':hidden')) {
								$.contact.reloadContactDataGrid();
							}
						} else {
							/** 操作失败 **/
							$.messager.alert('异常信息',resMsg,'error');
						}
					},
					error : function (XMLHttpRequest, textStatus, errorThrown,d) {
						$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
					},
					complete : function() {
						$.telAddress.dataGridTmp.datagrid('collapseRow',$.telAddress.dataGridTmpIndex);
					}
				});
			}
        },
		cancelItem : function(index) {
            var row = $(this).datagrid('getRows')[index];
            if (row.isNewRecord){
                $(this).datagrid('deleteRow',index);
            } else {
                $(this).datagrid('collapseRow',index);
            }
        },
        
        destroyItem : function() {
            var row = $('#dg').datagrid('getSelected');
            if (row){
                $.messager.confirm('Confirm','Are you sure you want to remove this user?',function(r){
                    if (r){
                        var index = $('#dg').datagrid('getRowIndex',row);
                        $.post('destroy_user.php',{id:row.id},function(){
                            $('#dg').datagrid('deleteRow',index);
                        });
                    }
                });
            }
        },
        newItem : function() {
        	var formData = {};
        	formData.objectId = $.telAddress.objectId;
        	formData.isNewRecord = true;
        	formData.className = $.telAddress.className;
        	if ($.isEmpty(formData.objectId)) {
        		return;
        	}
        	$(this).datagrid('appendRow',formData);
        	var index = $(this).datagrid('getRows').length - 1;
        	$(this).datagrid('expandRow', index);
        	$(this).datagrid('selectRow', index);
        },
		
        /** 查询联系电话表格数据 **/
		reloadTelDataGrid : function() {
			var queryData = {};
			/** 分页参数 **/
			queryData.page = $.telAddress.telPG.page;
			queryData.rows = $.telAddress.telPG.rows;
			/** 电话及联系人对象 zdsys.Borrower zdsys.Contact **/
			queryData.className = $.telAddress.className;
			/** 客户编号 或 联系人编号 **/
			queryData.objectId = $.telAddress.objectId;
			queryData.url = $.telAddress.queryTelDataGridUrl;
			
			$.telAddress.telDataGrid.datagrid('reloadData',queryData);
		},
        
        /** 查询联系地址表格数据 **/
		reloadAddressDataGrid : function() {
			var queryData = {};
			/** 分页参数 **/
			queryData.page = $.telAddress.addressPG.page;
			queryData.rows = $.telAddress.addressPG.rows;
			/** 电话及联系人对象 zdsys.Borrower zdsys.Contact **/
			queryData.className = $.telAddress.className;
			/** 客户编号 或 联系人编号 **/
			queryData.objectId = $.telAddress.objectId;
			queryData.url = $.telAddress.queryAddressDataGridUrl;
			
			$.telAddress.addressDataGrid.datagrid('reloadData',queryData);
		}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.telAddress.telPG = {
		'page' : 1,
		'rows' : $.telAddress.pageSize
	}
	
	$.telAddress.addressPG = {
		'page' : 1,
		'rows' : $.telAddress.pageSize
	}
	
	/** 新增电话信息事件 **/
	$('#telNewItem').click(function(){
		$.telAddress.newItem.call($.telAddress.telDataGrid);
	})
	
	/** 新增地址信息事件 **/
	$('#addressNewItem').click(function(){
		$.telAddress.newItem.call($.telAddress.addressDataGrid);
	})
	
	/** 联系电话表格参数定义 **/
	$.telAddress.telDataGrid.datagrid({
		pg : $.telAddress.telPG,
		/** 是否显示行号 **/
		rownumbers : true,
		dataGridType : 'tel',
		/** 是否单选 **/
		singleSelect : true,
		/** 是否可折叠的 **/
		collapsible : false,
		/** 自适应列宽 **/
		fitColumns : true,
		fit : true,
		/** 是否开启分页 **/
		pagination : true,
		toolbar : '#telToolbar',
		loadMsg : '数据加载中,请稍等...',
		columns : [[
		      /** 列定义 **/
		      {field : 'telType',title : '类型',width : 5},
		      {field : 'content',title : '号码',width : 10},
		      {field : 'priority',title : '优先级',width : 5},
		      {field : 'valid',title : '有效性',width : 10,formatter : function(value,row,rowIndex){
		    	  if (value == 't') {
		    		  return '有效';
		    	  } else if (value == 'f') {
		    		  return '无效';
		    	  } else {
		    		  return '';
		    	  }
		      }},
		      {field : 'memo',title : '备注',width : 10}
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.telAddress.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.telAddress.pageSizeList
	});
	
	$.telAddress.telDataGrid.datagrid({
		view : detailview,
		detailFormatter : function(index,row){
			return '<div class="ddv editContentPanel"></div>';
		},
		onExpandRow: function(index,row){
			var ddv = $(this).datagrid('getRowDetail',index).find('div.ddv');
			ddv.panel({
				border : false,
				cache : true,
				href : $.telAddress.editTelInfoUrl + '?index='+index,
				onLoad : function(){
		        	var formObj = $.telAddress.telDataGrid.datagrid('getRowDetail',index).find('form');
					$.telAddress.telDataGrid.datagrid('fixDetailRowHeight',index);
					$.telAddress.telDataGrid.datagrid('selectRow',index);
					$(formObj).form('load',row);
				}
			});
			$(this).datagrid('fixDetailRowHeight',index);
		}
	});
	
	var p1 = $.telAddress.telDataGrid.datagrid('getPager');
	$(p1).pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.telAddress.telPG.page = pageNumber;
			$.telAddress.telPG.rows = pageSize;
			$.telAddress.reloadTelDataGrid();
		}
	});
	
	/** 联系地址表格参数定义 **/
	$.telAddress.addressDataGrid.datagrid({
		pg : $.telAddress.addressPG,
		/** 是否显示行号 **/
		rownumbers : true,
		/** 是否单选 **/
		singleSelect : true,
		dataGridType : 'address',
		/** 是否可折叠的 **/
		collapsible : false,
		/** 自适应列宽 **/
		fitColumns : true,
		fit : true,
		/** 是否开启分页 **/
		pagination : true,
		toolbar : '#addressToolbar',
		loadMsg : '数据加载中,请稍等...',
		columns : [[
		      /** 列定义 **/
		      {field : 'addressType',title : '类型',width : 5},
		      {field : 'content',title : '地址',width : 10},
		      {field : 'postcode',title : '邮编',width : 5},
		      {field : 'priority',title : '优先级',width : 5},
		      {field : 'valid',title : '有效性',width : 10,formatter : function(value,row,rowIndex){
		    	  if (value == 't') {
		    		  return '有效';
		    	  } else if (value == 'f') {
		    		  return '无效';
		    	  } else {
		    		  return '';
		    	  }
		      }},
		      {field : 'memo',title : '备注',width : 10}
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.telAddress.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.telAddress.pageSizeList
	});
	
	$.telAddress.addressDataGrid.datagrid({
		view : detailview,
		detailFormatter : function(index,row){
			return '<div class="ddv editContentPanel"></div>';
		},
		onExpandRow: function(index,row){
			var ddv = $(this).datagrid('getRowDetail',index).find('div.ddv');
			ddv.panel({
				border : false,
				cache : true,
				href : $.telAddress.editAddressInfoUrl + '?index='+index,
				onLoad : function(){
		        	var formObj = $.telAddress.addressDataGrid.datagrid('getRowDetail',index).find('form');
					$.telAddress.addressDataGrid.datagrid('fixDetailRowHeight',index);
					$.telAddress.addressDataGrid.datagrid('selectRow',index);
					$(formObj).form('load',row);
				}
			});
			$(this).datagrid('fixDetailRowHeight',index);
		}
	});
	
	var p2 = $.telAddress.addressDataGrid.datagrid('getPager');
	$(p2).pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.telAddress.addressPG.page = pageNumber;
			$.telAddress.addressPG.rows = pageSize;
			$.telAddress.reloadAddressDataGrid();
		}
	});
	
	/** 我的联系人窗口面板参数定义 **/
	$.telAddress.editTelAddressWin.window({
		//定义窗口是不是模态窗口
		modal : true,
		//定义是否显示折叠按钮
		collapsible : false,
		//定义是否显示最小化按钮
		minimizable : false,
		//定义是否显示最大化按钮
		maximizable : false,
		//定义是否显示关闭按钮
		closable : true,
		//定义是否关闭了窗口
		closed : true,
		//定义是否窗口能被拖拽
		draggable : true,
		//定义是否窗口可以调整尺寸
		resizable : false,
		//如果设为 true， 当窗口能够显示阴影的时候将会显示阴影。
		shadow : true,
		//定义如何放置窗口  true 就放在它的父容器里 false 就浮在所有元素的顶部
		inline : true,
		//样式定义
		iconCls : 'icon-save',
		onBeforeClose: function () { //当面板关闭之前触发的事件
			$('div.ddv').remove();
		}
	})
	
	/** 变更电话、地址信息 **/
	$('#editTelAddressBut').click(function(){
		var row = $.dataGrid.getSelectedRow($.person.customerDataGrid);
		if (row) {
			$.telAddress.personRow = row;
			$.telAddress.objectId = row.id;
			/** 操作客户的电话及地址 **/
			$.telAddress.className = 'zdsys.Borrower';
			$.telAddress.editTelAddressWin.window('open');
			/** 查询联系电话数据 **/
			$.telAddress.reloadTelDataGrid();
			/** 查询联系地址数据 **/
			$.telAddress.reloadAddressDataGrid();
		} else {
			$.messager.alert('警告','请选中需要维护的客户!','warning');
		}
	})
	
	
})