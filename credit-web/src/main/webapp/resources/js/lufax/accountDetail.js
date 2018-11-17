$(function() {

	// 表格数据源地址
	var dataGridUrl = global.contextPath + '/lujinsuo/public/accountDetailList';
	var rechargeUrl = global.contextPath + '/lujinsuo/public/recharge';
	var withdrawalsUrl = global.contextPath + '/lujinsuo/public/withdrawals';	
	var importDetailToExcUrl = global.contextPath + '/lujinsuo/public/importDetailToExc';
	// 表格实例
	var testDataGrid = $('#testDataGrid');
	var rechargePanel = $('#rechargePanel');
	var dataForm = $('#dataForm');
	var withdrawalsPanel = $('#withdrawalsPanel');
	var dataFormTx = $('#dataFormTx');	
	// 查询数据项表单实例
	var searchForm = $('#searchForm');
	// 每页显示的记录条数，默认为10
	var pageSize = 10;
	// 设置每页记录条数的列表
	var pageSizeList = [ 10, 20, 30, 40, 50 ];
	
	/** 操作提示 * */
	var tips = $('#tips');
	var orgtips = $('#orgtips');
	/** 分页参数（page:当前第N页，rows:一页N行） * */
	var pg = {
		'page' : 1,
		'rows' : pageSize
	};
	var tradeTypes = {
		'0' : '充值',
		'1' : '提现',
		'2' : '还款',
		'3': '垫付',
		'4' : '回购'		
	};
	var states = {
			'0' : '处理中',
			'1' : '成功',
			'2' : '失败'
	};
	
	testDataGrid.datagrid({

		pg : pg,
		// 提交方式
		method : 'get',
		// 是否显示行号
		rownumbers : true,
		// 是否单选
		singleSelect : true,
		// //是否可折叠的
		collapsible : false,
		// 自适应列宽
		fitColumns : true,
		fit : true,
		// height : '100%',
		// 是否开启分页
		pagination : true,
		// 锁定列定义
		hideColumn : [ [ {
			field : 'id',
			width : 50
		}] ],
		
		columns : [ [
		// 列定义
		{
			field : 'batchNo',
			title : '批次号',
			width : 100
		},{
			field : 'contractNum',
			title : '借款合同号',
			width : 100
		},{
			field : 'tradeDate',
			title : '交易日',
			width : 100,
			formatter : function(value, row, index) {
				if(value==null){
					return '-';
				}else{
					return $.DateUtil.dateFormatToStr(value);
				}
			},
		}, {
			field : 'tradeTime',
			title : '交易时间',
			width : 100,
			formatter : function(value, row, index) {
				if(value==null){
					return '-';
				}else{
					return value;
				}
			},
		}, {
			field : 'pay',
			title : '支',
			width : 100,
			formatter : function(value, row, index) {
				if(row.state=='0'){
					return '-';				
				}else{
					return value;	
				}
			},
		}, {
			field : 'inCome',
			title : '收',
			width : 100,
			formatter : function(value, row, index) {
				if(row.state=='0'){
					return '-';				
				}else{
					return value;	
				}
			},
		}, {
			field : 'closingBalance',
			title : '余额',
			width : 100,
			formatter : function(value, row, index) {
				if(row.state=='0'){
					return '-';				
				}else{
					return value;	
				}
			},
		}, {
			field : 'memo',
			title : '摘要',
			width : 100,
			formatter : function(value, row, index) {
				if(value==null){
					return '-';
				}else{
					return value;
				}
			},
		},{
			field : 'tradeType',
			title : '交易类型',
			width : 100,
			formatter : function(value, row, index) {
				return tradeTypes[value + ''] || '未知';
			},
		},{
			field : 'state',
			title : '状态',
			width : 100,
			formatter : function(value, row, index) {
				if(row.state=='0'){
					return '处理中(金额'+row.reqMoney+')';			
				}else{
					return states[value + ''] || '未知';	
				}
			},
		}] ],
		// 每页显示的记录条数，默认为10
		pageSize : pageSize,
		// 可以设置每页记录条数的列表
		pageList : pageSizeList,
		toolbar : '#tb',
		// 自定义行样式
		rowStyler : function(index, row) {
			if (index % 2 == 0) {
				// return 'background-color:#AABBCC;color:#fff;';
			}
		},
		// 回调函数
	    callBackFunction : function(value) {
			/** 账户总金额**/
			$("#totalAmt").html(value.calculteData.totalAmt);	
		}
	});	
	// 表格分页实例
	var pager = testDataGrid.datagrid('getPager');
	pager.pagination({
		onSelectPage : function(pageNumber, pageSize) {
			pg.page = pageNumber;
			pg.rows = pageSize;
			reloadDataGrid();
		}
	});

	 $("#clearCondition").bind("click", function(envent) {
		$("#searchForm").form("reset");
	});
	// 查询按钮添加事件
	$("#searchBut").click(function() {
		pg.page = 1;
		reloadDataGrid();
	});
	// 充值按钮
	$("#rechargeWindow").click(function() {
		// 显示窗口
		rechargePanel.window('open');
		withdrawalsPanel.window('close');
		// 重置表单数据
		dataForm.form('clear');
	});
	// 提现按钮
	$("#withdrawalsWindow").click(function() {
		return;
		// 显示窗口
		withdrawalsPanel.window('open');
		rechargePanel.window('close');
		// 重置表单数据
		dataFormTx.form('clear');
	});
	// 加载表格数据
	function reloadDataGrid() {
		$('#rechargePanel').window('close');
		$('#withdrawalsPanel').window('close');
		// 获取查询表单数据转换成JSON对象
		var searchMsg = searchForm.serialize();
		// 对参数进行解码(显示中文)
		searchMsg = decodeURIComponent(searchMsg);
		var queryParam = $.serializeToJsonObject(searchMsg);
		queryParam.url = dataGridUrl;
		$(testDataGrid).datagrid('reloadData', queryParam);
	};	
	// 充值按钮提交
	$("#rechargeSubmit").click(			
			function() {
				$.messager.confirm('提示消息', '是否确认充值？', function(r){
					if (r){
						if (dataForm.form('validate')) {
							$.ajaxPackage({
								type : 'get',
								url : rechargeUrl,
								data : dataForm.serialize(),
								dataType : "json",
								success : function(data, textStatus, jqXHR) {
									// 从服务器上获取到记录信息
									var resCode = data.resCode;
									var resMsg = data.resMsg;
									if (resCode == '000000') {
										$.messager.alert("操作提示", resMsg);
										reloadDataGrid();
									} else {
										$.messager.alert('异常信息',resMsg);
									}
								},
								error : function(XMLHttpRequest, textStatus,
										errorThrown, d) {
									$.messager.alert('异常信息', textStatus + '  :  '
											+ errorThrown + '!', 'error');
								},
								complete : function() {
									// 关闭窗口
									$('#userCodes').textbox({'readonly':false});									
								}
							});
						}
					}			
				})				
			}
		);
	// 提现按钮提交
	$("#withdrawalsSubmit").click(
		function() {
			if (dataFormTx.form('validate')) {
				$.ajaxPackage({
					type : 'get',
					url : withdrawalsUrl,
					data : dataFormTx.serialize(),
					dataType : "json",
					success : function(data, textStatus, jqXHR) {
						// 从服务器上获取到记录信息
						var resCode = data.resCode;
						var resMsg = data.resMsg;
						if (resCode == '000000') {
							$.messager.alert("操作提示", resMsg);
							reloadDataGrid();
						} else {
							$.messager.alert('异常信息',resMsg);
						}
					},
					error : function(XMLHttpRequest, textStatus,
							errorThrown, d) {
						$.messager.alert('异常信息', textStatus + '  :  '
								+ errorThrown + '!', 'error');
					},
					complete : function() {
						// 关闭窗口
						$('#userCodes').textbox({'readonly':false});									
					}
				});
			}
		}
	);
	// 导出按钮
	$("#importDetailToExc").click(
		function() {				
			$.messager.confirm('提示消息', '是否确认下载？', function(r){
				if (r){
			    	$.downloadFile({
			    		type : 'get',
						url : importDetailToExcUrl,
						params:searchForm.serializeObject(),
						hasDownloadFile : true,
						successFunc:function(data){
			                if(data== null){
			                    $.messager.alert('提示','下载成功！','info');
			                }else{
			                    if(data.resMsg!= null){
			                        $.messager.alert('提示',data.resMsg,'info');
			                    }else{
			                        $.messager.alert('异常','下载失败！','error');
			                  }
			                }
			            },
			            failFunc:function(data){
			                $.messager.alert('异常','下载失败！','error');
			            }
					})
				}
			})
		});
	
	// 默认查询表格数据
	reloadDataGrid();
})
