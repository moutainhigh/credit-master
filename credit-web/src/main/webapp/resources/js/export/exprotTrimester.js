$(function() {
	// 表单对象
	var conditionForm = $('#conditionForm');

	// 导出数据对应地址
	var exportLoanStatus = '/export/exp/download/exportLoanStatusDetailTrimester';

	$('#submitBtn').click(function() {

		$('#submitBtn').attr("disabled", true);

		var params = {};
		var reportDate = $.trim($('#reportDate').datebox('getValue'));
		params.reportDate = reportDate;

		$.downloadFile({
			url : global.contextPath + exportLoanStatus,
			isDownloadBigFile : true,
			params : params,
			successFunc : function(data) {
				if (data == null) {
					$.messager.alert('提示', '下载成功！', 'info');
				} else {
					if (data.resMsg != null) {
						$.messager.alert('警告', data.resMsg, 'warning');
					} else {
						$.messager.alert('异常', '下载失败！', 'error');
					}
				}
			},
			failFunc : function(data) {
				$.messager.alert('异常', '下载失败！', 'error');
			}
		});
	})
})