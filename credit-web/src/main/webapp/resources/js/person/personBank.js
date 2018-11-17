$(function() {
	
	$.bank  = {
		/** 客户编号 或 联系人编号 **/
		objectId : '',
		/** 银行总行数据源地址 **/
		bankInfoUrl : global.contextPath + '/offer/offerBankDic/getBankInfo',
		/** 查询客户已经绑定银行卡地址 **/
		queryMyBankInfoUrl : global.contextPath + '/offer/loanBank/queryMyBankInfo',
		/** 更新银行卡信息 **/
		updateLoanBankUrl : global.contextPath + '/offer/loanBank/updateLoanBank',
		/** 查询单条银行卡信息 **/
		loadLoanBankUrl : global.contextPath + '/offer/loanBank/loadLoanBankInfo',
		
		/** 客户记录(前台选中的行对象) **/
		personRow : undefined,
		
		/** 变更开户银行窗口 **/
		editBankWin : $('#editBankWin'),
		/** 开户银行下拉框 **/
		bankCombo : $('#bankCombo'),
		/** 变更开户银行表单 **/
		editBankForm : $('#editBankForm'),
		/** 显示我的银行卡列表窗口(绑定多个时用到) **/
		showMyBankListWin : $('#showMyBankListWin'),
		/** 显示我的银行卡列表(绑定多个时用到) **/
		bankDataGrid : $('#bankDataGrid'),
		/** 分页控件 **/
		pager : undefined,
		
		/** 每页显示的记录条数，默认为10 **/
		pageSize : 10,
		/** 设置每页记录条数的列表 **/
		pageSizeList : [10,20,30,40,50],
		
		/** 从服务端获取银行数据,将数据填充到前端下拉框 **/
		initBankInfoData : function() {
			$.ajaxPackage({
				type : 'post', 
				url : $.bank.bankInfoUrl,
				isShowLoadMask : false,
				dataType : "json",
				success : function (data) { 
					/** data 服务端返回数据 **/
					$.bank.bankCombo.combobox('loadData',data);
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					
				}
			});
		},
		/** 查询表格数据 **/
		reloadDataGrid : function() {
			$.bank.bankDataGrid.datagrid('reloadData',{
				url : $.bank.queryMyBankInfoUrl + '/' + $.bank.personRow.id
			});
		}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.bank.pg = {
		'page' : 1,
		'rows' : $.bank.pageSize
	}
	
	/** 变更开户银行信息 **/
	$('#editBankBut').click(function() {
		/** 判断是否选中客户 **/
		var row = $.dataGrid.getSelectedRow($.person.customerDataGrid);
		if (row) {
			/** 选中客户记录 **/
			$.bank.personRow = row;
			$.ajaxPackage({
				type : 'get', 
				url : $.bank.queryMyBankInfoUrl + '/' + $.bank.personRow.id,
				data : $.bank.pg,
				dataType : "json",
				success : function (data,textStatus,jqXHR) {
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					var attachment = data.attachment;
					if (resCode == '000000') {
						/** attachment 为查询结果集{"rows":[],"total":0} **/
						if (attachment.total == 0) {
							/** 客户未绑定银行卡 **/
							$.messager.alert('警告','客户未绑定银行卡,无法变更!','warning');
						} else if (attachment.total == 1) {
							/** 只存在一条银行卡信息，直接弹出修改页面 **/
							$.bank.editBankWin.window('open');
							var formData = attachment.rows[0];
							formData.objectId = row.id;
							$.bank.editBankForm.form('load',formData);
						} else {
							/** 只存在多条银行卡信息,直接列表页,进行选择修改 **/
							$.bank.showMyBankListWin.window('open');
							$.bank.bankDataGrid.datagrid('loadData',attachment);
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
					
				}
			});
		} else {
			$.messager.alert('警告','请选中需要维护的客户!','warning');
		}
	})
	
	/** 更新银行卡信息 **/
	$('#updateBankBut').click(function() {
		/** 验证表单数据项是否通过 **/
		if($.bank.editBankForm.form('validate')) {
			$.ajaxPackage({
				type : 'post', 
				url : $.bank.updateLoanBankUrl,
				data : $.bank.editBankForm.serialize(),
				dataType : "json",
				success : function (data) { 
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					if (resCode == '000000') {
						/** 操作成功 **/
						$.messager.alert('结果','操作成功');
						$.bank.reloadDataGrid();
					} else {
						/** 操作失败 **/
						$.messager.alert('结果',resMsg,'error');
					}
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					/** 关闭窗口 **/
					$.bank.editBankWin.window('close');
				}
			});
		}
	})
	
	/** 关闭修改银行卡窗口 **/
	$('#closeBankBut').click(function() {
		$.bank.editBankWin.window('close');
	})
	/** 初始化开户银行列表数据 **/
	$.bank.initBankInfoData();
	
	/** 我的银行卡表格参数定义 **/
	$.bank.bankDataGrid.datagrid({
		pg : $.bank.pg,
		/** 是否显示行号 **/
		rownumbers : true,
		/** 是否单选 **/
		singleSelect : true,
		/** 是否可折叠的 **/
		collapsible : false,
		/** 自适应列宽 **/
		fitColumns : true,
		fit : true,
		/** 是否开启分页 **/
		pagination : true,
		loadMsg : '数据加载中,请稍等...',
		toolbar : '#bankTB',
		columns : [[
		      /** 列定义 **/
		      {field : 'bankName',title : '总行名称',width : 5},
		      {field : 'bankCode',title : '总行代码',width : 5},
		      {field : 'fullName',title : '支行名称',width : 10},
		      {field : 'account',title : '账户',width : 10,formatter : function(value,row,rowIndex){
		    	  return value;
		      }}
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.bank.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.bank.pageSizeList,
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
	});
	
	$.bank.pager = $.bank.bankDataGrid.datagrid('getPager');
	$.bank.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.bank.pg.page = pageNumber;
			$.bank.pg.rows = pageSize;
			$.bank.reloadDataGrid();
		}
	});
	
	/** 开户银行下拉框参数定义 **/
	$('#bankCombo').combobox({
		valueField : 'id',
		textField : 'text',
		//panelHeight : 'auto',
		filter : function(q,row) {
			var opts=$(this).combobox("options");
			return row[opts.textField].indexOf(q)>-1;
		},
		formatter : function(row) {
			var opts=$(this).combobox("options");
			return row[opts.textField];
		}
	})
	
	
	
	/** 变更开户银行窗口面板参数定义 **/
	$.bank.editBankWin.window({
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
		iconCls : 'icon-save'
	})
	
	/** 我的银行卡窗口面板参数定义 **/
	$.bank.showMyBankListWin.window({
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
		iconCls : 'icon-save'
	})
	
	/** 查看/修改银行卡信息 **/
	$('#bank_loadDataBut').click(function(){
		var row = $.dataGrid.getSelectedRow($.bank.bankDataGrid);
		if (row) {
			$.ajaxPackage({
				type : 'get', 
				url : $.bank.loadLoanBankUrl + '/' + row.id,
				dataType : "json",
				success : function (data,textStatus,jqXHR) {
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					var attachment = data.attachment;
					if (resCode == '000000') {
						/** 操作成功 **/
						$.bank.editBankWin.window('open');
						$.bank.editBankForm.form('clear');
						var formData = attachment;
						formData.objectId = $.bank.personRow.id;
						$.bank.editBankForm.form('load',attachment);
					} else {
						/** 操作失败 **/
						$.messager.alert('异常信息',resMsg,'error');
					}
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					
				}
			});
		} else {
			$.messager.alert('警告','请选中需要修改的银行卡!','warning');
		}
	})
	
	
	
})