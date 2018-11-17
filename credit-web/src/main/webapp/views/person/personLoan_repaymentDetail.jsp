<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri ="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>还款详细信息（还款计划信息）</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<style type="text/css">
			
		</style>
	</head>
	<body data-options="" style="">
	<script type="text/javascript">
	var timer = null;
	function hiddenDiv(id,backDate){
		clearTimeout(timer);
		var value = document.getElementById(id); 
		value.innerHTML = "<a><font color='red'>"+$.DateUtil.dateFormatToStr(backDate)+"</font></a>";
	}
	
	function showDiv(id,backDate,factDate){
		timer=setTimeout(function(){
			var value = document.getElementById(id); 
			value.innerHTML = "<a><font color='red'>"+$.DateUtil.dateFormatToStr(backDate)+"</font></a>"+
							  "<a><font style='font-weight:bold;' color='red'>&nbsp;报盘日期："
							  +$.DateUtil.dateFormatToStr(factDate)+"</font></a>";
		},200);
	}
	
	$(function(){
		$.repaymentDetail = {
			dataGrid : $('#repaymentDetailDataGrid'),
			/** 还款计划数据源地址 **/
			queryDataUrl : global.contextPath + '/loanInfo/loadRepaymentDetailData' + '/<%=request.getAttribute("id")%>',
			
			/** 每页显示的记录条数，默认为10 **/
			pageSize : 1000,
			/** 设置每页记录条数的列表 **/
			pageSizeList : [1000],
			/** 分页对象 **/
			pager : undefined,
			
			reloadDataGrid : function() {
				$.repaymentDetail.dataGrid.datagrid('reloadData',{
					url : $.repaymentDetail.queryDataUrl
				});
			}
		}
		
		/** 分页参数（page:当前第N页，rows:一页N行） **/
		$.repaymentDetail.pg = {
			'page' : 1,
			'rows' : $.repaymentDetail.pageSize
		}
		
		$.repaymentDetail.dataGrid.datagrid({
			pg : $.repaymentDetail.pg,
			//是否显示行号
			rownumbers : true,
			//是否单选
			singleSelect : true,
			////是否可折叠的
			collapsible : false,
			//自适应列宽
			fitColumns : true,
			fit : true,
			//height : '100%',
			//是否开启分页
			pagination : true,
			//锁定列定义
			frozenColumns : [[
			      
			]],
			columns : [[
				//列定义
				{field : 'currentTerm',title : '期数',width : 20},
				{field : 'returnDate',title : '应还款日',width : 100,formatter:$.DateUtil.dateFormatToStr},
				{field : 'factReturnDate',title : '报盘日期',width : 100,hidden:true,formatter:$.DateUtil.dateFormatToStr},
				{field : 'backTime',title : '实际还款日',width : 180,
					formatter : function(value, row, rowIndex) {
						var realValue = "";
						if(row.factReturnDate == value){
							realValue =  $.DateUtil.dateFormatToStr(value);
						}else{
							realValue = '<a id='+row.id+' onmouseover="showDiv('+row.id+','+value+','+row.factReturnDate+')"'
										+ 'onmouseout = "hiddenDiv('+row.id+','+value+')"><font color="red">'
										+ $.DateUtil.dateFormatToStr(value)+ '</font></a>';	
						} 
						return realValue;
					}
				},
				{field : 'returneterm',title : '还款金额',width : 100,vType : 'rmb'},
				{field : 'repaymentAll',title : '当期一次性还款金额',width : 100,vType : 'rmb'},
				{field : 'repaymentState',title : '当期还款状态',width : 100},
				{field : 'deficit',title : '当期剩余欠款',width : 100,vType : 'rmb'}
			]],
			//每页显示的记录条数，默认为10
			pageSize : $.repaymentDetail.pageSize,
			//可以设置每页记录条数的列表
			pageList : $.repaymentDetail.pageSizeList,
			
			//自定义行样式
			rowStyler : function(index,row) {
				if (index % 2 == 0) {
					//return 'background-color:#AABBCC;color:#fff;';
				}
			}
		});
		
		$.parser.onComplete = function(){
			$.repaymentDetail.pager = $.repaymentDetail.dataGrid.datagrid('getPager');
			$.repaymentDetail.pager.pagination({
				onSelectPage : function(pageNumber,pageSize) {
					$.repaymentDetail.pg.page = pageNumber;
					$.repaymentDetail.pg.rows = pageSize;
					$.repaymentDetail.reloadDataGrid();
				}
			});
		}
		
		//默认进页面加载数据
		$.repaymentDetail.reloadDataGrid();
		
	})
	</script>
	<table id="repaymentDetailDataGrid" class="easyui-datagrid" data-options=""></table>
	</body>
</html>












