$(function(){	
	$('#searchBut').click(function(){
		if($("#conditionForm").form("validate")){
			var name=$('#name').val();
			var mphone=$('#mphone').val();
			var idnum=$('#idnum').val();
			var querySelectvalue = $('#repayType').combobox('getValue');
			var grantMoneyDateStart=$("#grantMoneyDateStart").datebox("getValue");
			//合同编号
			var contractNum = $("#contractNum").val();
			if ($.isEmpty(grantMoneyDateStart)){
				$.messager.alert('提示信息','时间选项必填','warning');
				return;
			}
			if ($.isEmpty(idnum) && $.isEmpty(contractNum)){
				$.messager.alert('提示信息','请输入身份证号码或者合同编号进行查询!','warning');
				return;
			} 
		
			var params={};
			var url = global.contextPath;			
			params.name=name;
			params.mphone=mphone;
			params.idnum=idnum;
			var queryString="?name="+name+"&mphone="+mphone+"&idnum="+idnum+"&querySelectvalue="+querySelectvalue+"&grantMoneyDateStart="+grantMoneyDateStart+"&contractNum="+contractNum;		
			if(querySelectvalue=="正常"){//新浪财富				
				url+="/repay/repayInfo/repayTrailNormal"+queryString;
			}else if(querySelectvalue=="一次性"){//挖财财富
				url+="/repay/repayInfo/repayTrailOneTime"+queryString;
			}
			$("#listRepayTrailDefault").attr("src",url);
			
			
		}
	});
	

    //清空查询条件
    $("#clearCondition").bind("click",function(envent){
    	if($(this).linkbutton("options").disabled==false){
    		$("#conditionForm").form("reset");
    	}
    });
	
});