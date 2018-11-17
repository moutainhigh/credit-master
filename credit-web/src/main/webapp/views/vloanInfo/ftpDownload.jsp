<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ include file="/views/common/headIncludeFile.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<title>附件查看</title>
<jsp:include page="/views/common/headIncludeFile.jsp" />

<style type="text/css">
</style>


<script type="text/javascript">

importPluginsExt(['panel','combobox','window','layout','datagrid','pagination'
                  ,'form','tooltip','validatebox','combogrid'],'business', function() {
		$(function() {
			var urlJs = [];
			
			importJSExt(urlJs,function(){
				/** 脚本加载成功回调方法 **/
				$(function() {

		var fjTree = global.contextPath + '/system/vloanInfo/ftpDownload';
		var imageview = global.contextPath + '/system/vloanInfo/download';
		var downloadFile = global.contextPath + '/system/vloanInfo/downloadFile';
		// 初始化图片树

		function loadMuneTreeContent() {
			var appNoA= '<%=request.getAttribute("appNo")%>';
			$.ajaxPackage({
				type : 'get',
				url : fjTree + '/' + appNoA,
				dataType : "json",
				success : function(data, textStatus, jqXHR) {
					if (data.resCode == '000000') {
						$('#ftpDownloadTree').tree('loadData',
								data.attachment[0].children);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown, d) {
					$.messager.alert('异常信息', textStatus + '  :  ' + errorThrown
							+ '!', 'error');
				},
				complete : function() {

				}
			});
			$("#readPdfIframe").hide();
		}
		;
		loadMuneTreeContent();

		$('#ftpDownloadTree').tree(
				{
					/** 单击树结点事件 **/
					onClick : function(node) {
						var fileId = node.id;
						var a = fileId.substr(fileId.lastIndexOf("."));
						
						if (!/\.(gif|jpg|jpeg|png|GIF|JPG|PNG)$/.test(a)) {
							alert("图片类型必须是.gif,jpeg,jpg,png中的一种")
							$('#downFile').show();
							return false;
						} else {

							if ($('#ftpDownloadTree').tree('isLeaf',
									node.target)) {
								var filesrc = global.contextPath
										+ '/system/vloanInfo/download' + '/'
										+ fileId+','+a;
								$("#fileImg").attr('src', filesrc);
								$('#appNo').val(fileId+','+a);
								$('#downFile').show();
							} else {

								alert("请选择子节点");
							}
						}
					}
				})

		// 下载
		$("#downFile").click(
				function() {
					var fileId = $('#appNo').val();
					var filesrc = global.contextPath
							+ '/system/vloanInfo/downloadFile' + '/' + fileId;
					window.location.href = filesrc;
				})
	})
			});
		});
	});
	
</script>
</head>
<body class="easyui-layout">
	<!--附件列表 -->
	<jsp:include page="/views/common/initPageMast.jsp" />
	<input type="hidden" name="appNo" id="appNo" />
	<div class="easyui-panel" data-options="region:'west'"
		style="width: 300px;">
		<ul id="ftpDownloadTree" class="easyui-tree" data-options="fit:true"></ul>
	</div>
	<div data-options="region:'center',fit:true">
		<iframe id="readPdfIframe" name="readPdfIframe"></iframe>
		<img src="" name="fileImg" id="fileImg" />
		<input type="button" value="下载" id="downFile" style="display: none"/>
	</div>

</body>
</html>
