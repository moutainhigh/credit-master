$(function() {
	$.reliefPenalty = {
		/** 表格数据源地址 * */
		dataGridUrl : global.contextPath
				+ '/specialRepayment/searchReliefPenalty',
		/** 查询罚息减免申请状态 地址 **/
		loadReliefPenaltyStateUrl : global.contextPath
				+ '/specialRepayment/loadReliefPenaltyState',
		changeReliefPenaltyStateUrl : global.contextPath
				+ '/specialRepayment/changeReliefPenaltyState',
		importExcelUrl : global.contextPath + '/specialRepayment/importReliefPenaltyStateFile',
		/** 客户信息表格 * */
		dataGrid : $('#dataGrid'),
		importExcelWin : $('#importExcelWin'),
		reliefPenaltySwitchBut : $('#reliefPenaltySwitchBut'),
		fileForm : $('#fileForm'),
		/** 分页控件 * */
		pager : undefined,
		/** 查询条件数据项表单实例 * */
		searchForm : $('#searchForm'),
		/** 变更提前一次性结清、提前扣款状态 窗口 **/
		changeSpecialStateWin : $('#changeSpecialStateWin'),
		/** 变更提前一次性结清、提前扣款状态 表单 **/
		changeStateForm : $('#changeStateForm'),
		/** 每页显示的记录条数，默认为10 * */
		pageSize : 10,
		/** 设置每页记录条数的列表 * */
		pageSizeList : [ 10, 20, 30, 40, 50 ],
		/** 加载表格数据 * */
		reloadDataGrid : function(noConditionTip) {
			if ($.reliefPenalty.searchForm.form('validate')) {
				/** 获取查询表单数据转换成JSON对象 * */
				var searchMsg = $.reliefPenalty.searchForm.serialize();
				/** 对参数进行解码(显示中文) * */
				searchMsg = decodeURIComponent(searchMsg);
				var queryParam = $.serializeToJsonObject(searchMsg);
				/** 未输入查询条件不给予查询 * */
				if ($.isEmpty(queryParam.idnum)&& $.isEmpty(queryParam.contractNum)) {
					if (typeof noConditionTip == 'boolean' && noConditionTip) {
						
					} else {
						$.messager.alert('提示信息', '请输入身份证号码或者合同编号进行查询!', 'warning');
					}
				} else {
					queryParam.url = $.reliefPenalty.dataGridUrl;
					$.reliefPenalty.dataGrid.datagrid('reloadData', queryParam);
				}
			}
		},
		init : function() {
			/** 显示变更状态窗口事件 **/
			$('#changeStateBut').click(function(){
				var selectedRow = $.dataGrid.getSelectedRow($.reliefPenalty.dataGrid);
				if (selectedRow) {
					var loanId = selectedRow.id;
					if ($.isEmpty(loanId)) {
						/*** 缺少借款编号参数 **/
						return;
					}
					$.ajaxPackage({
						type : 'get', 
						url : $.reliefPenalty.loadReliefPenaltyStateUrl + '/' + loanId,
						dataType : "json",
						success : function (data,textStatus,jqXHR) {
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							//从服务器上获取到记录信息
							var attachment = data.attachment;
							if (resCode == '000000') {
								$.reliefPenalty.resetChangeStateForm();
								$.reliefPenalty.changeStateForm.form('load',attachment);
								if ($.isEmpty(attachment.reliefPenaltyState)) {
									return;
								}
								if (attachment.reliefPenaltyState) {
									$.reliefPenalty.reliefPenaltySwitchBut.switchbutton('check');
								}
								$.reliefPenalty.changeSpecialStateWin.window('open');
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
			
			/** 提交变更状态请求 **/
			$('#submitBut').click(function(){
					/** 罚息减免申请状态 **/
					var reliefPenaltyState = $.reliefPenalty.reliefPenaltySwitchBut.switchbutton('isCheckedExt');
					if ($.isEmpty(reliefPenaltyState)) {
						/** 参数不存在 中断执行 **/
						return;
					}
					var loanId = $.reliefPenalty.changeStateForm.find('input[name=id]').val();
					if ($.isEmpty(loanId)) {
						/** 参数不存在 中断执行 **/
						$.messager.alert('警告','缺少借款编号参数!','warning');
						return;
					}
					
					/** 罚息减免申请状态 **/
					reliefPenaltyState = reliefPenaltyState ? 'on':'off';
					/** 罚息减免金额 **/
					var money = $.reliefPenalty.changeStateForm.find('input[name=money]').val();
					if (reliefPenaltyState == 'on') {
						if ($.isEmpty(money)) {
							$.messager.alert('提示','减免金额为必填项!','warning');
							return;
						}
						if (parseFloat(money) <= 0 || parseFloat(money) > 99999) {
							$.messager.alert('提示','减免金额必须在[0.01-99999]之间!','warning');
							return;
						}
					}
					/** 请求URL 参数拼接 **/
					var requestUrl = $.reliefPenalty.changeReliefPenaltyStateUrl;
					/** 请求参数 **/
					var formData = {};
					formData.reliefPenaltyState = reliefPenaltyState;
					formData.money = money;
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
									$.messager.alert('警告','操作成功');
									$.reliefPenalty.reloadDataGrid();
								} else {
									$.messager.alert('警告',resMsg,'error');
								}
							},
							error : function (XMLHttpRequest, textStatus, errorThrown,d) {
								$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
							},
							complete : function() {
								$.reliefPenalty.changeSpecialStateWin.window('close');
							}
						});
				}});
			})
			
			/** 关闭  变更提前一次性结清、提前扣款状态 窗口  事件 **/
			$('#closeBut').click(function(){
				$.reliefPenalty.changeSpecialStateWin.window('close');
			})
			
			/** 控制提前一次性结清和提前扣款 单选 **/
			$.reliefPenalty.reliefPenaltySwitchBut.switchbutton({
				onChange: function(checked){
		            if (checked){
		            	$('#moneyDateDiv').show();
		            } else {
		            	$('#moneyDateDiv').hide();
		            	$.reliefPenalty.changeSpecialStateWin.window('resize');
		            }
				}
		    })
			
			/**  显示导入窗口事件 **/
			$('#importBut').click(function(){
				$.reliefPenalty.importExcelWin.window('open');
				$.reliefPenalty.fileForm.form('clear');
			})
			
			/**  导入Excel（信贷系统）事件 **/
			$('#importExcelBut').click(function(){
				/** 判断是否选中文件 **/
				var fileName = $.reliefPenalty.fileForm.find('input[type=file]').val();
				if ($.isEmpty(fileName)) {
					$.messager.alert('提示信息','请选择上传的文件!','warning');
					return;
				}
				$.messager.confirm('上传文件','确定上传文件?',function(r){
					if (r) {
						$.reliefPenalty.fileForm.ajaxSubmit({
				            type : 'post',
				            dataType : 'json',
				            url : $.reliefPenalty.importExcelUrl,
				            hasDownloadFile : true,
				            success: function (data) {
				            	$.messager.alert('信息','操作成功');
				            	$.reliefPenalty.importExcelWin.window('close');
				            	$.reliefPenalty.reloadDataGrid(true);
				            },
				            error: function (data) {
				            	$.messager.alert('警告',data.resCode + ':' + data.resMsg,'warning');
				            }
				        });
					}}
				);
			})
		},
		/** 重置Form表单数据 **/
		resetChangeStateForm : function() {
			$.reliefPenalty.changeStateForm.form('clear');
			$('#moneyDateDiv').hide();
		}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.reliefPenalty.pg = {
		'page' : 1,
		'rows' : $.reliefPenalty.pageSize
	}
	
	$('#searchBut').click(function() {
		$.reliefPenalty.pg.page = 1;
		$.reliefPenalty.reloadDataGrid();
	})
	
	$('#clearBut').click(function() {
		$.reliefPenalty.searchForm.form('reset');
	})
	
	$.reliefPenalty.dataGrid.datagrid({
		pg : $.reliefPenalty.pg,
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
		      {field : 'repaymentAmount',title : '应还逾期总额',width : 2,vType : 'rmb'},
		      {field : 'fine',title : '罚息',width : 2,vType : 'rmb'},
		      {field : 'accAmount',title : '挂账金额',width : 2,vType : 'rmb'},
		      {field : 'overdueCorpusAndInterest',title : '逾期本息和',width : 2,vType : 'rmb'},
		      {field : 'contractNum',title : '合同编号',width : 2,vType : 'rmb'},
		      {field : 'time',title : '借款期限',width : 2},
		      {field : 'loanState',title : '借款状态',width : 2},
		      {field : 'reliefRequestState',title : '减免金额',width : 2,vType : 'rmb'}
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.reliefPenalty.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.reliefPenalty.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		},
		onClickRow : function(index, row) {
			var tipText = '';
			if (row.reliefRequestState == '未申请') {
				tipText = '减免申请';
			} else {
				tipText = '减免取消';
			}
			if ($('#changeStateBut').length) {
				$('#changeStateBut').linkbutton({text : tipText});
			}
		}
	});
	
	$.reliefPenalty.pager = $.reliefPenalty.dataGrid.datagrid('getPager');
	$.reliefPenalty.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.reliefPenalty.pg.page = pageNumber;
			$.reliefPenalty.pg.rows = pageSize;
			$.reliefPenalty.reloadDataGrid();
		}
	});
	
	$.reliefPenalty.dataGrid.datagrid('resize');
	$.reliefPenalty.init();
})