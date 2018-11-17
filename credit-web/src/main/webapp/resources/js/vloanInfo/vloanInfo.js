//** 查看客户详细资料 **//*
function personInfoTab(rowIndex) {
	var name = $('#VloaninfoDataGrid').datagrid('getRows')[rowIndex].name;
	var id = $('#VloaninfoDataGrid').datagrid('getRows')[rowIndex].borrowerId;
	var tab = {};
	tab.id = 'personDetail_' + id;
	tab.text = name + ' - 详细资料';
	tab.iconCls = "pic_1";
	tab.url = global.contextPath + '/person/viewPersonDetailPage' + '/' + id;
	// ** 调用父级添加选项卡方法 **//*
	parent.$.iframeTabs.add(tab);
}

function loanbaseTab(rowIndex) {
	var name = $('#VloaninfoDataGrid').datagrid('getRows')[rowIndex].name;
	var id = $('#VloaninfoDataGrid').datagrid('getRows')[rowIndex].id;
	var tab = {};
	tab.id = 'loanDetail_' + id;
	tab.text = name + ' - 借款详细信息';
	tab.iconCls = "pic_1";
	tab.url = global.contextPath + '/loanInfo/viewPersonLoanDetailPage' + '/'
			+ id;
	// ** 调用父级添加选项卡方法 **//*
	parent.$.iframeTabs.add(tab);
}
$(function() {

	// 表格数据源地址
	$.vloanInfo = {
		dataGrid : $('#VloaninfoDataGrid'),
	}
	var vloanInfoList = global.contextPath + '/system/vloanInfo/vloanInfoList';
	var dataGridUrl = global.contextPath + '/system/vloanInfo/search';
	var ftpDownload = $('#csUrl').val()+'/video/jumpIndex';
	var newPicUrl = $('#picUrl').val()+'/api/filedata';
	var userCode = $("#userCode").val();//登录人工号
    var userName = $("#userName").val();//登录人姓名
	// 存储系统的地址
	var storeServerUrl = $("#storeServerUrl").val();

	// 查询数据项表单实例
	var searchForm = $('#searchForm');
	// 每页显示的记录条数，默认为10
	var pageSize = 10;
	// 设置每页记录条数的列表
	var pageSizeList = [ 10, 20, 30, 40, 50 ];
	/** 操作提示 * */
	var tips = $('#tips');

	var VloaninfoDataGrid = $('#VloaninfoDataGrid');

	var pg = {
			'page' : 1,
			'rows' : pageSize
		}
	VloaninfoDataGrid
			.datagrid({
				pg : pg,
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
				hideColumn : [ [ {
					field : 'id'
				} ] ],
				columns : [ [
						// 列定义
						{
							field : 'name',
							title : '借款人',
							width : 50,
							formatter : function(value, row, rowIndex) {
								//渤海信托员工 不能查看详情
								if(row.userCode =='bhxt'){
									return value;
								} else {
									return '<a name="idclick" onclick="personInfoTab('
									+ rowIndex
									+ ');" href="javascript:void(0);" id="idclick">'
									+ value + '</a>';	
								}
							}
						},
						{
							field : 'loanType',
							title : '借款类型',
							width : 50,
							formatter : function(value, row, rowIndex) {
								//渤海信托员工 不能查看详情
								if(row.userCode =='bhxt'){
									return value;
								} else {
									return '<a name="loanbaseclick" onclick="loanbaseTab('
										+ rowIndex
										+ ');" href="javascript:void(0);" id="loanbaseclick">'
										+ value + '</a>';
								}
							}
						}, {
							field : 'personInfoName',
							title : '客户经理',
							width : 50
						}, {
							field : 'salesManName',
							title : '客服',
							width : 50
						}, {
							field : 'idnum',
							title : '身份证号',
							width : 50
						}, {
							field : 'profession',
							title : '职业类型',
							width : 50
						}, {
							field : 'purpose',
							title : '用途',
							width : 50
						}, {
							field : 'pactMoney',
							title : '合同金额',
							width : 50
						}, {
							field : 'money',
							title : '审批金额',
							width : 50
						}, {
							field : 'time',
							title : '借款期限',
							width : 25
						}
						, {
							field : 'loanState',
							title : '状态',
							width : 25
						}
						,{
							field : 'contractNum',
							title : '合同编号',
							width : 50
						},{
							field : 'appNo',
							title : '申请件号',
							width : 60
						},{
							field : 'repaymentLevel',
							title : '账户类别',
							width : 45
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
				}
			});

	// 表格分页实例
	var testDataGridPG = VloaninfoDataGrid.datagrid('getPager');
	testDataGridPG.pagination({
		onSelectPage : function(pageNumber, pageSize) {
			pg.page = pageNumber;
			pg.rows = pageSize;
			reloadDataGrid();
		}
	});

	// 加载表格数据
	function reloadDataGrid() {
		// 获取查询表单数据转换成JSON对象
		var searchMsg = searchForm.serialize();
		/** 对参数进行解码(显示中文) * */
		searchMsg = decodeURIComponent(searchMsg);
			
		var queryParam = $.serializeToJsonObject(searchMsg);
		queryParam.url = dataGridUrl;
		console.info(queryParam);
		$(VloaninfoDataGrid).datagrid('reloadData', queryParam);

	}

	/** 查询条件必须输入其中之一的校验 * */
	function check() {
		var name = $.trim($("#name").val());
		var idNum = $.trim($("#idNum").val());
		var mphone = $.trim($("#mphone").val());
		var loanState = $('#loanState').combobox('getValue');
		var salesmanId = $.trim($('input[name=salesmanId]').val());
		var loanType = $('#loanType').combobox('getValue');
		var salesDepartmentId = $('#salesDepartmentId').combobox('getValue');
		var signSalesDepId = $('#signSalesDepId').combobox('getValue');
		var contractNum = $.trim($("#contractNum").val());
		var appNo = $.trim($("#appNo").val());
		if ($.isEmpty(name) && $.isEmpty(idNum) && $.isEmpty(loanState)&& $.isEmpty(mphone)&& $.isEmpty(loanType)
		   && $.isEmpty(salesDepartmentId) && $.isEmpty(signSalesDepId)&& $.isEmpty(salesmanId) && $.isEmpty(contractNum) && $.isEmpty(appNo))
		{
			return false;
		}
		
		// 防止输入空白查询
		//$("#name").val(name);
		//$("#idNum").val(idNum);
		return true;
	}
	// 查看附件
	$('#detileBut').click(function() {
		var selectedRow = getSelectedRow(VloaninfoDataGrid);
		if (!selectedRow) {
			$.messager.alert('警告', '请选择一条记录查看！', 'warning');
			return;
		}
		var loanId = selectedRow.id;
		var name = selectedRow.name;
		var appNo = selectedRow.appNo || loanId;
		
		var tab = {};
		tab.id = 'attachment_' + loanId;
		tab.text = name + ' - 附件信息';
		tab.iconCls = "pic_6";
		if(appNo.length == 14){
			var nodeKey = 'ourCatalogueQuery';//录单查询-业务环节编号
			var sysName = 'credit';//系统编号credit 
			var appNo = appNo;//借款编号
			var operator = userName;//操作人姓名 当前登录人
			var jobNumber = userCode;//工号 当前登录人
			var dataSources = '1' ;//1表示PC端，2表示app端
			tab.url = newPicUrl +"?nodeKey="+nodeKey+"&sysName="+sysName+"&appNo="+appNo+"&operator="+operator+"&jobNumber="+jobNumber+"&dataSources="+dataSources;
		}
		if(appNo.length != 14){
			tab.url = ftpDownload + '/' + loanId;
		}
		// ** 调用父级添加选项卡方法 **//*
		parent.$.iframeTabs.add(tab);
	})
	 $("#clearCondition").bind("click",function(envent){
	    	$("#searchForm").form("reset");
     });
	// 获取选中的行
	function getSelectedRow(dataGrid) {
		var selectedRow = dataGrid.datagrid('getSelected');
		return selectedRow;
	}

	// 查询按钮添加事件
	$("#searchBut").click(function() {
		if (searchForm.form('validate')) {
			pg.page = 1;
			var flag = check();
			if (flag) {
				reloadDataGrid();
			} else {
				$.messager.alert('警告', '请必须输入一个查询条件！', 'warning');
			}
		}
	});
	
    /** 查看音/视频 **/
    $('#showVideoBtn').click(function() {
        var tabId = 'showVideo';
        var text = '音/视频信息';
        var iconCls = 'video_5';
        var requestUrl = '/upload/fileDetailForXinDai';
        // 打开音/视频详细页面
        addTab(tabId, text, iconCls, requestUrl);
    });
    
    /** 管理音/视频 **/
    $('#editVideoBtn').click(function() {
        var tabId = 'editVideo';
        var text = '音/视频管理';
        var iconCls = 'video_3';
        var requestUrl = '/upload/fileDetailForXinDaiManage';
        // 打开音/视频管理页面
        addTab(tabId, text, iconCls, requestUrl);
    });
    
    /** 打开存储系统页面通用方法 **/
    function addTab(tabId, text, iconCls, requestUrl){
        var selectedRow = getSelectedRow(VloaninfoDataGrid);
        if (!selectedRow) {
            $.messager.alert('警告', '请选择一条记录查看！', 'warning');
            return;
        }
        // 债权编号
        var loanId = selectedRow.id;
        // 申请件号，如果为空就取债权编号
        var appNo = selectedRow.appNo || loanId;
        if($.isEmpty(appNo)){
            $.messager.alert('警告', '该笔借款的申请件号为空！', 'warning');
            return;
        }
        // 客户姓名
        var name = selectedRow.name;
        // 客户证件号码
        var idnum = selectedRow.idnum;
        // 放款时间
        var grantMoneyDate = "";
        if(selectedRow.grantMoneyDate){
            grantMoneyDate = $.DateUtil.dateFormatToStr(selectedRow.grantMoneyDate);
        }
        var tab = {};
        tab.id = tabId + '_' + loanId;
        tab.text = name + ' - ' + text;
        iconCls = iconCls || 'video_1'
        tab.iconCls = iconCls;
        // 此地址需要配置到数据库中
        var url = storeServerUrl + requestUrl;
        url = url + "?customerName="+escape(encodeURIComponent(name))+"&customerNo="+idnum+"&applicationCaseNo="+appNo +"&loanDateStr="+ grantMoneyDate;
        tab.url = url;
        // ** 调用父级添加选项卡方法 **//*
        parent.$.iframeTabs.add(tab);
    }
})
