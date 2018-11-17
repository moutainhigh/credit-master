$(function() {
	$.repaymentInput = {
		/** 表格数据源地址 **/
		dataGridUrl : global.contextPath + '/loanInfo/searchRepaymentLoan',
		/** 查询借款还款汇总信息地址 **/
		searchRepayInfoUrl : global.contextPath + '/loanInfo/searchRepayInfo',
		/** 还款申请地址 **/
		repayInputUrl : global.contextPath + '/offer/repayInfo/repaymentInput',
		/** Excel导入信贷系统地址 **/
		importCreditExcelUrl : global.contextPath + '/offer/repayInfo/importRepayInfoFile',
		/** Excel导入车企贷系统地址 **/
		importCarExcelUrl : global.contextPath + '/offer/repayInfo/importCarRepayInfoFile',
		/** Excel导入证方系统地址 **/
		importZfExcelUrl : global.contextPath + '/offer/repayInfo/importZfRepayInfoFile',
		/** 客户信息表格 **/
		dataGrid : $('#dataGrid'),
		/** 还款窗口 **/
		repayWin : $('#repayWin'),
		/** Excel 导入窗口 **/
		importExcelWin : $('#importExcelWin'),
		/** 导入信贷系统Form **/
		creditFileForm : $('#creditFileForm'),
		/** 导入车企贷系统Form **/
		carFileForm : $('#carFileForm'),
		/** 导入证方系统Form **/
		zfFileForm : $('#zfFileForm'),
		/** 还款表单窗口 **/
		repayForm : $('#repayForm'),
		/** 某笔借款数据(前台选中的行对象) **/
		loanRow : undefined,
		/** 分页控件 **/
		pager : undefined,
		/** 查询条件数据项表单实例 **/
		searchForm : $('#searchForm'),
		/** 每页显示的记录条数，默认为10 **/
		pageSize : 10,
		/** 设置每页记录条数的列表 **/
		pageSizeList : [10,20,30,40,50],
		/** 加载表格数据 **/
		reloadDataGrid : function(noConditionTip) {
			if ($.repaymentInput.searchForm.form('validate')) {
				/** 获取查询表单数据转换成JSON对象 **/
				var searchMsg = $.repaymentInput.searchForm.serialize();
				/** 对参数进行解码(显示中文) **/
				searchMsg = decodeURIComponent(searchMsg);
				var queryParam = $.serializeToJsonObject(searchMsg);  
				/** 未输入查询条件不给予查询 **/
				if ($.isEmpty(queryParam.name) && $.isEmpty(queryParam.idnum) && $.isEmpty(queryParam.contractNum)) {
					if (typeof noConditionTip == 'boolean' && noConditionTip) {
						
					} else {
						$.messager.alert('提示信息','至少输入一个查询条件!','warning');
					}
				} else {
					queryParam.url = $.repaymentInput.dataGridUrl;
					$.repaymentInput.dataGrid.datagrid('reloadData',queryParam);
				}
			}
		},
		init : function() {
			/** 工具栏：还款按钮事件 **/
			$('#repayBut').click(function() {
				var row = $.dataGrid.getSelectedRow($.repaymentInput.dataGrid);
				if (row) {
					$.repaymentInput.loanRow = row;
					$.ajaxPackage({
						type : 'get', 
						url : $.repaymentInput.searchRepayInfoUrl + '/' + $.repaymentInput.loanRow.id,
						dataType : 'json',
						success : function (data,textStatus,jqXHR) {
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							//从服务器上获取到记录信息
							var attachment = data.attachment;
							if (resCode == '000000') {
								var curDate = $.repaymentInput.repayForm.find('input[name=curDate]').val();
								$.repaymentInput.repayForm.form('clear');
								$('#tradeType').combobox('defaultOneItem');
								/** 逾期利息 **/
								var overInterest = attachment.overInterest;
							    /** 逾期本金 **/
								var overCorpus = attachment.overCorpus;
//							    /** 逾期应还本息 = 逾期利息 + 逾期本金 **/
//								attachment.overTotalAmount = overInterest + overCorpus;
								/** 转换日期格式 **/
								attachment.overDueDate = $.dates.formatter(attachment.overDueDate);
								attachment.fineDate = $.dates.formatter(attachment.fineDate);
								attachment.currDate = $.dates.formatter(attachment.currDate);
								
								/** 是否申请提前结清 **/
								var requestState = attachment.requestState;
								if (requestState == '已申请') {
									/** 一次性还款金额 **/
									$('#isOneTimeFlag').html('一次性还款金额');
									attachment.curBX = attachment.oneTimeRepayment;
								} else {
									/** 当期应还本息和 **/
									$('#isOneTimeFlag').html('当期应还本息和');
									attachment.curBX = attachment.currAmount;
								}
								attachment.curDate = curDate;
								$.repaymentInput.repayForm.form('load',attachment);
								
								/** 动态设置validType="numberRangeValid[0.01,99999999]数值范围 **/
								/** 第一种方式 **/
								/*$('#amount').numberbox({
									validType:['numberRangeValid[0.01,' + attachment.allAmount + ']']
								})*/
								/** 第二种方式 **/
								//$('#amount').next().find('.textbox-text').validatebox('options').validParams=[0.01,attachment.allAmount];
								
								$.repaymentInput.repayWin.window('open');
								/** 格式化金额（千分位） **/
								$.repaymentInput.comdify();
							} else {
								$.messager.alert('警告',resMsg,'error');
							}
						},
						error : function (XMLHttpRequest, textStatus, errorThrown,d) {
							$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
						},
						complete : function() {
							
						}
					});
				} else {
					$.messager.alert('警告','请选中需要还款的客户!','warning');
				}
			})
			
			/** 发送还款申请事件 **/
			$('#repaySubmitBut').click(function(){
				if ($.repaymentInput.repayForm.form('validate')) {
					/** 一次性结清总金额 **/
					/*var allAmount = $.repaymentInput.repayForm.find('input[name=allAmount]').val();
					if (allAmount <= 0) {
						$.messager.alert('警告','该笔借款已结清!','warning');
						return;
					}*/
					/** 还款录入金额 **/
					var amount = $.repaymentInput.repayForm.find('input[name=amount]').val();
					if (parseFloat(amount) < 0.01) {
						$.messager.alert('警告','还款金额不能小于0.01元,请修改!','warning');
						return;
					}
					/*if (parseFloat(amount) > parseFloat(allAmount)) {
						$.messager.alert('警告','还款金额超过一次性结清总金额[' + allAmount + '元],请修改!','warning');
						return;
					}*/
					
					$.messager.confirm('确认','确定还款操作?',function(r){ if (r) {
						$.ajaxPackage({
							type : 'post', 
							url : $.repaymentInput.repayInputUrl,
							dataType : 'json',
							data : $.repaymentInput.repayForm.serialize(),
							success : function (data,textStatus,jqXHR) {
								var resCode = data.resCode;
								var resMsg = data.resMsg;
								//从服务器上获取到记录信息
								var attachment = data.attachment;
								if (resCode == '000000') {
									$.repaymentInput.reloadDataGrid();
									$.messager.alert('还款录入','还款成功!');
								} else {
									$.repaymentInput.reloadDataGrid();
									$.messager.alert('警告',resMsg,'error');
								}
							},
							error : function (XMLHttpRequest, textStatus, errorThrown,d) {
								$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
							},
							complete : function() {
								$.repaymentInput.repayWin.window('close');
							}
						});
					}});
				}
			})
			
			/** 关闭窗口事件 **/
			$('#repayCloseBut').click(function(){
				$.repaymentInput.repayWin.window('close');
			})
			
			/**  显示导入窗口事件 **/
			$('#importBut').click(function(){
				$.repaymentInput.importExcelWin.window('open');
				$.repaymentInput.creditFileForm.form('clear');
				$.repaymentInput.carFileForm.form('clear');
				$.repaymentInput.zfFileForm.form('clear');
			})
			
			/**  导入Excel（信贷系统）事件 **/
			$('#importExcelBut').click(function(){
				/** 判断是否选中文件 **/
				var fileName = $.repaymentInput.creditFileForm.find('input[type=file]').val();
				if ($.isEmpty(fileName)) {
					$.messager.alert('提示信息','请选择上传的文件!','warning');
					return;
				}
				$.messager.confirm('上传文件','确定上传文件?',function(r){
					if (r) {
						$.repaymentInput.creditFileForm.ajaxSubmit({
				            type : 'post',
				            dataType : 'json',
				            url : $.repaymentInput.importCreditExcelUrl,
				            hasDownloadFile : true,
				            success: function (data) {
				            	$.messager.alert('信息','操作成功');
				            	$.repaymentInput.importExcelWin.window('close');
					            $.repaymentInput.reloadDataGrid(true);
				            },
				            error: function (data) {
				            	$.messager.alert('警告',data.resCode + ':' + data.resMsg,'warning');
				            }
				        });
					}}
				);
			})
			
			/**  导入Excel（车企贷系统）事件 **/
			$('#importCarExcelBut').click(function(){
				/** 判断是否选中文件 **/
				var fileName = $.repaymentInput.carFileForm.find('input[type=file]').val();
				if ($.isEmpty(fileName)) {
					$.messager.alert('提示信息','请选择上传的文件!','warning');
					return;
				}
				$.messager.confirm('上传文件','确定上传文件?',function(r){
					if (r) {
						$.repaymentInput.carFileForm.ajaxSubmit({
				            type : 'post',
				            dataType : 'json',
				            url : $.repaymentInput.importCarExcelUrl,
				            hasDownloadFile : true,
				            success: function (data) {
				            	$.messager.alert('信息','操作成功');
				            	$.repaymentInput.importExcelWin.window('close');
				            },
				            error: function (data) {
				            	$.messager.alert('警告',data.resCode + ':' + data.resMsg,'warning');
				            }
				        });
					}}
				);
			})
			
			/**  还款录入导入（证方系统） **/
			$('#importZfRepayInfoBtn').click(function(){
				/** 判断是否选中文件 **/
				var file = $("#zfRepayInfoFile").filebox("getValue");
				if ($.isEmpty(file)) {
					$.messager.alert('警告','请选择上传的文件！','warning');
					return;
				}
				$.messager.confirm('提示','确认上传文件吗？',function(r){
					if (r) {
						$.repaymentInput.zfFileForm.ajaxSubmit({
				            type : 'post',
				            dataType : 'json',
				            url : $.repaymentInput.importZfExcelUrl,
				            hasDownloadFile : true,
				            success: function (data) {
				            	$.messager.alert('提示','操作成功','info');
				            	$.repaymentInput.importExcelWin.window('close');
				            },
				            error: function (data) {
				            	$.messager.alert('警告',data.resCode + ':' + data.resMsg,'warning');
				            }
				        });
					}}
				);
			})
		},
		/** 格式化金额（千分位） **/
		comdify : function() {
			$('input.thousand').each(function(){
				var value = $(this).val();
				value = $.comdify(value);
				value = '￥' + value;
				$(this).val(value);
			})
		}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.repaymentInput.pg = {
		'page' : 1,
		'rows' : $.repaymentInput.pageSize
	}
	
	$('#searchBut').click(function() {
		$.repaymentInput.pg.page = 1;
		$.repaymentInput.reloadDataGrid();
	})
	
	$('#clearBut').click(function() {
		$.repaymentInput.searchForm.form('reset');
	})
	
	$.repaymentInput.dataGrid.datagrid({
		pg : $.repaymentInput.pg,
		/** 提交方式 **/
		method : 'get',
		/** 是否显示行号 **/
		rownumbers : true,
		/** 是否单选 **/
		singleSelect : true,
		/** 是否可折叠的 **/
		collapsible : false,
		selectOnCheck : true,
		checkOnSelect : true,
		/** 自适应列宽 **/
		fitColumns : true,
		fit : true,
		//height : '100%',
		/** 是否开启分页 **/
		pagination : true,
		loadMsg : '数据加载中,请稍等...',
		/** 锁定列定义 **/
		frozenColumns : [[
		     //{field : '',checkbox:'true',title:'123'}
		]],
		columns : [[
		      /** 列定义 **/
		      {field : 'crmOrgName',title : '管理营业部',width:3},
		      {field : 'name',title : '借款人',width:1},
		      {field : 'loanType',title : '借款类型',width:1},
		      {field : 'idNum',title : '身份证号',width:3},
		      {field : 'pactMoney',title : '合同金额',width:1,vType : 'rmb'},
		      {field : 'time',title : '借款期限',width:1},
		      {field : 'loanState',title : '还款状态',width:1},
		      {field : 'isOneTime',title : '提前结清申请',width:1},
		      {field : 'reliefOfFine',title : '减免金额',width:1,vType : 'rmb'},
		      {field : 'contractNum',title : '合同编号',width:2}
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.repaymentInput.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.repaymentInput.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
	});
	
	$.repaymentInput.pager = $.repaymentInput.dataGrid.datagrid('getPager');
	$.repaymentInput.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.repaymentInput.pg.page = pageNumber;
			$.repaymentInput.pg.rows = pageSize;
			$.repaymentInput.reloadDataGrid();
		}
	});
	
	$.repaymentInput.dataGrid.datagrid('resize');
	
	$.repaymentInput.repayWin.window({
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
	$.repaymentInput.init();
})