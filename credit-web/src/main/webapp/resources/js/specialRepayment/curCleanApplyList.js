$(function() {
	$.oneTimeRepayment = {
		maxClosingDate : '2099-12-31',
		/** 表格数据源地址 * */
		dataGridUrl : global.contextPath
				+ '/specialRepayment/searchRepaymentLoanToPre',
		/** 提前扣款申请状态变更 地址 **/
		changeSpecialStateUrl : global.contextPath
				+ '/specialRepayment/changeCurCleanApplyState',
		/** 查询提前扣款、提前一次性结清申请状态 地址 **/
		loadSpecialStateUrl : global.contextPath
				+ '/specialRepayment/loadCurCleanSpecialState',
		/** 客户信息表格 * */
		dataGrid : $('#dataGrid'),
		/** 分页控件 * */
		pager : undefined,
		/** 查询条件数据项表单实例 * */
		searchForm : $('#searchForm'),
		curCleanApplyStateSwitchBut : $('#curCleanApplyState'),
		/** 变更提前一次性结清、提前扣款状态 窗口 **/
		changeSpecialStateWin : $('#changeSpecialStateWin'),
		/** 变更提前一次性结清、提前扣款状态 表单 **/
		changeStateForm : $('#changeStateForm'),
		/** 每页显示的记录条数，默认为10 * */
		pageSize : 10,
		/** 设置每页记录条数的列表 * */
		pageSizeList : [ 10, 20, 30, 40, 50 ],
		/** 加载表格数据 * */
		reloadDataGrid : function() {
			if ($.oneTimeRepayment.searchForm.form('validate')) {
				/** 获取查询表单数据转换成JSON对象 * */
				var searchMsg = $.oneTimeRepayment.searchForm.serialize();
				/** 对参数进行解码(显示中文) * */
				searchMsg = decodeURIComponent(searchMsg);
				var queryParam = $.serializeToJsonObject(searchMsg);
				/** 未输入查询条件不给予查询 * */
				if ($.isEmpty(queryParam.name) && $.isEmpty(queryParam.mphone)
						&& $.isEmpty(queryParam.idnum)
						&& $.isEmpty(queryParam.salesMan)&& $.isEmpty(queryParam.contractNum)) {
					$.messager.alert('提示信息', '至少输入一个查询条件!', 'warning');
				} else {
					queryParam.url = $.oneTimeRepayment.dataGridUrl;
					$.oneTimeRepayment.dataGrid.datagrid('reloadData', queryParam);
				}
			}
		},
		init : function() {
			/** 显示变更状态窗口事件 **/
			$('#changeStateBut').click(function(){
				var selectedRow = $.dataGrid.getSelectedRow($.oneTimeRepayment.dataGrid);
				if (selectedRow) {
					var loanId = selectedRow.id;
					if ($.isEmpty(loanId)) {
						/*** 缺少借款编号参数 **/
						return;
					}
					$.ajaxPackage({
						type : 'get', 
						url : $.oneTimeRepayment.loadSpecialStateUrl + '/' + loanId,
						dataType : "json",
						success : function (data,textStatus,jqXHR) {
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							//从服务器上获取到记录信息
							var attachment = data.attachment;
							if (resCode == '000000') {
								$.oneTimeRepayment.resetChangeStateForm();
								if (attachment.currDate) {
									/** 控制自动生效日期控制，可选日期范围 **/
									$('#requestDate').datebox().datebox('calendar').calendar({
										validator: function(date){
											var d1 = $.DateUtil.parseDate(attachment.currDate);
											if (attachment.curReturnDate) {
												var d2 = $.DateUtil.parseDate(attachment.curReturnDate) - 1;
												return d1 <= date && date <= d2;
											} else {
												return d1 <= date;
											}
										}
									});
								}
								$.oneTimeRepayment.changeStateForm.form('load',attachment);
								if ($.isEmpty(attachment.curCleanApplyState)) {
									return;
								}
								if (attachment.curCleanApplyState) {
									$.oneTimeRepayment.curCleanApplyStateSwitchBut.switchbutton('check');
								}
								$.oneTimeRepayment.changeSpecialStateWin.window('open');
							} else {
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
					$.messager.alert('警告','请选中借款记录!','warning');
				}
			})
			
			 /** 控制提前一次性结清和提前扣款 单选 **/
		    $.oneTimeRepayment.curCleanApplyStateSwitchBut.switchbutton({
				onChange: function(checked){
		            if (checked){
		            	$('tr.noOneTimeSetDateDiv').show();
		            } else {
		            	$('tr.noOneTimeSetDateDiv').hide();
		            	$.oneTimeRepayment.changeSpecialStateWin.window('resize');
		            }
				}
		    })
			
			/** 提交变更状态请求 **/
			$('#submitBut').click(function(){
					/** 获取当天日期 **/
					var currDate = $('#currDate').val();
					if ($.isEmpty(currDate)) {
						$.messager.alert('警告','缺少当天日期参数!','warning');
						return;
					} 
					
					var curCleanApplyState = $.oneTimeRepayment.curCleanApplyStateSwitchBut.switchbutton('isCheckedExt')
					if ($.isEmpty(curCleanApplyState)) {
						/** 参数不存在 中断执行 **/
						return;
					}
					var loanId = $.oneTimeRepayment.changeStateForm.find('input[name=id]').val();
					if ($.isEmpty(loanId)) {
						/** 参数不存在 中断执行 **/
						$.messager.alert('警告','缺少借款编号参数!','warning');
						return;
					}
					/** 提前扣款状态 **/
					curCleanApplyState = curCleanApplyState ? 'on':'off';
					
					/** 提前扣款(自动生效时间) **/
					var requestDate = $.oneTimeRepayment.changeStateForm.find('[name=requestDate]').val();
					/** 该笔借款当期还款日 **/
					var curReturnDate = $.oneTimeRepayment.changeStateForm.find('input[name=curReturnDate]').val();
					
					if (curCleanApplyState == 'on') {
						if ($.isEmpty(curReturnDate)) {
							$.messager.alert('提示','当期还款日未获取到或已结清!','warning');
							return;
						}
						if ($.isEmpty(requestDate)) {
							$.messager.alert('提示','自动生效时间为必填项!','warning');
							return;
						}
						if ($.dates.parser(requestDate) < $.dates.parser(currDate)) {
							$.messager.alert('提示','自动生效时间必须大于当天（含当天）!','warning');
							return;
						}
						/** 自动生效时间不能大于还款日 **/
						if ($.dates.parser(requestDate) >= $.dates.parser(curReturnDate)) {
							$.messager.alert('提示','自动生效时间必须小于当期还款日（' + curReturnDate + '）!','warning');
							return;
						}
					}
					/** 请求URL 参数拼接 **/
					var requestUrl = $.oneTimeRepayment.changeSpecialStateUrl;
					/** 请求参数 **/
					var formData = {};
					formData.curCleanApplyState = curCleanApplyState;
					formData.requestDate = requestDate;
					formData.id = loanId;
					
					$.messager.confirm('确认','确认提交?',function(r){if (r) {
						$.ajaxPackage({
							type : 'get', 
							url : requestUrl,
							data : formData,
							dataType : 'json',
							success : function (data,textStatus,jqXHR) {
								var resCode = data.resCode;
								var resMsg = data.resMsg;
								var attachment = data.attachment;
								if (resCode == '000000') {
									$.messager.alert('信息','操作成功');
									$.oneTimeRepayment.reloadDataGrid();
								} else {
									$.messager.alert('警告',resMsg,'error');
								}
							},
							error : function (XMLHttpRequest, textStatus, errorThrown,d) {
								$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
							},
							complete : function() {
								$.oneTimeRepayment.changeSpecialStateWin.window('close');
							}
						});
				}});
			})
			
			/** 关闭  变更提前一次性结清、提前扣款状态 窗口  事件 **/
			$('#closeBut').click(function(){
				$.oneTimeRepayment.changeSpecialStateWin.window('close');
			})
		    
		    /** 自动取消时间 有效复选框控制 **/
		    $('#oneTimeClosingDateCheck').change(function(a){
		    	if ($(this).is(':checked')) {
		    		$('#oneTimeClosingDate').datebox({disabled:true});
		    		$('#oneTimeClosingDate').datebox('setValue',$.oneTimeRepayment.maxClosingDate);
		    	} else {
		    		$('#oneTimeClosingDate').datebox({disabled:false});
		    	}
		    })
		    
//		    $('#noOneTimeClosingDateCheck').change(function(a){
//		    	if ($(this).is(':checked')) {
//		    		$('#noOneTimeClosingDate').datebox({disabled:true});
//		    		$('#noOneTimeClosingDate').datebox('setValue',$.oneTimeRepayment.maxClosingDate);
//		    	} else {
//		    		$('#noOneTimeClosingDate').datebox({disabled:false});
//		    	}
//		    })
		},
		/** 重置Form表单数据 **/
		resetChangeStateForm : function() {
			$.oneTimeRepayment.changeStateForm.form('clear');
			$('#oneTimeClosingDate').datebox({disabled:false});
			$('#noOneTimeClosingDate').datebox({disabled:false});
			$('tr.oneTimeSetDateDiv').hide();
			$('tr.noOneTimeSetDateDiv').hide();
		}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.oneTimeRepayment.pg = {
		'page' : 1,
		'rows' : $.oneTimeRepayment.pageSize
	}
	
	$('#searchBut').click(function() {
		$.oneTimeRepayment.pg.page = 1;
		$.oneTimeRepayment.reloadDataGrid();
	})
	
	$('#clearBut').click(function() {
		$.oneTimeRepayment.searchForm.form('reset');
	})
	
	$.oneTimeRepayment.dataGrid.datagrid({
		pg : $.oneTimeRepayment.pg,
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
		/** 锁定列定义 **/
//		frozenColumns : [[
//		      {field : 'id',title : '编号',width : 50}
//		]],
		columns : [[
		      /** 列定义 **/
		      {field : 'name',title : '借款人',width : 2},
		      {field : 'loanType',title : '借款类型',width : 2},
		      {field : 'idNum',title : '身份证号',width : 4},
		      {field : 'pactMoney',title : '合同金额',width : 2,vType : 'rmb'},
		      {field : 'time',title : '借款期限',width : 2},
		      {field : 'overdueAllAmount',title : '逾期本息和',width : 2,vType : 'rmb'},
		      {field : 'fine',title : '罚息',width : 2,vType : 'rmb'},
		      {field : 'accAmount',title : '挂账金额',width : 2,vType : 'rmb'},
		      {field : 'oneTimeRepaymentAmount',title : '一次性还款金额',width : 2,vType : 'rmb'},
		      {field : 'currAmount',title : '下期金额',width : 2,vType : 'rmb'},
		      {field : 'contractNum',title : '合同编号',width : 2},
		      {field : 'spState',title : '申请状态',width : 2},
		      {field : 'loanState',title : '借款状态',width : 2}
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.oneTimeRepayment.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.oneTimeRepayment.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
	});
	
	$.oneTimeRepayment.pager = $.oneTimeRepayment.dataGrid.datagrid('getPager');
	$.oneTimeRepayment.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.oneTimeRepayment.pg.page = pageNumber;
			$.oneTimeRepayment.pg.rows = pageSize;
			$.oneTimeRepayment.reloadDataGrid();
		}
	});
	
	$.oneTimeRepayment.dataGrid.datagrid('resize');
	$.oneTimeRepayment.init();
})