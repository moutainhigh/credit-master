$(function() { 
	var exportUrl =global.contextPath+'/data/cpcs/export';
	var uploadUrl =global.contextPath+'/data/cpcs/upload';
	var myDate = new Date();
	var year = myDate.getFullYear();
	var month = myDate.getMonth()+1;
	//导出征信报文
	$("#exportBtn").click(function(){
		var params = {};
		var state = $.trim($("#state").combobox('getValue')); 
		var countDateStart = $.trim($('#countDateStart').datebox('getValue'));
		var countDateEnd = $.trim($('#countDateEnd').datebox('getValue'));
		var monthDate = $.trim($('#monthDate').datebox('getValue'));
		params.countDateStart = countDateStart;
		params.countDateEnd = countDateEnd;
		params.state = state;
		params.monthDate = monthDate;
		var yearValue = monthDate.split("-")[0];
		var monthValue = monthDate.split("-")[1];
		if(state=="1"){
			if((monthDate==null || monthDate=="")){
				$.messager.alert('提示信息', '统计日期不能为空！', 'warning');
				return;
			}
			if(yearValue<=year){
				if(month>monthValue){
					
				}else{
					$.messager.alert('提示信息', '统计日期月份只能小于当前月份！', 'warning');
					return;
				}
			}else{
				$.messager.alert('提示信息', '统计日期月份只能小于当前月份！', 'warning');
				return;
			}
		}
		if(state=="2"){
			if((countDateStart==null || countDateStart=="") || (countDateEnd==null || countDateEnd=="")){
				$.messager.alert('提示信息', '统计日期不能为空！', 'warning');
				//alert('查询条件不能为空！！！');
				return;
			}
		}
        $.messager.confirm("提示","确认导出征信报文吗？",function(r){
            if(r){
            	$.ajaxPackage({
        			type : 'post',
        			url : exportUrl,
        			data : params,
        			dataType : "json",
        			success : function(data, textStatus, jqXHR) {
        				if(data.url!=null && data.url!=""){
        					window.location.href=data.url+'?monthDate='+monthDate+"&state="+state+"&countDateStart="+countDateStart+"&countDateEnd="+countDateEnd;
        				}else{
        					$.messager.alert('提示信息', '找不到访问地址！', 'warning');
        				}
        			},
        			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
        				console.info(XMLHttpRequest.status+"====="+XMLHttpRequest.readyState+"======"+textStatus+"========"+errorThrown);
        				console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
        			},
        			complete : function() {
        				// 显示窗口
        				//addEmployeePanel.window('open');
        			}
        		});
            }
        });
    });
	 
	$("#importBtn").click(function(){
		//alert(1111111111);
		var params = {};
		var state = $.trim($("#state").combobox('getValue')); 
		var countDateStart = $.trim($('#countDateStart').datebox('getValue'));
		var countDateEnd = $.trim($('#countDateEnd').datebox('getValue'));
		var monthDate = $.trim($('#monthDate').datebox('getValue'));
		params.countDateStart = countDateStart;
		params.countDateEnd = countDateEnd;
		params.state = state;
		params.monthDate = monthDate;
		var yearValue = monthDate.split("-")[0];
		var monthValue = monthDate.split("-")[1];
		if(state=="1"){
			if((monthDate==null || monthDate=="")){
				//alert('查询条件不能为空！！！');
				$.messager.alert('提示信息', '统计日期不能为空！', 'warning');
				return;
			}
			if(yearValue<=year){
				if(yearValue<year){
					
				}else{
					if(month>monthValue){
						
					}else{
						$.messager.alert('提示信息', '统计日期月份只能小于当前月份！', 'warning');
						return;
					}
				}
			}else{
				$.messager.alert('提示信息', '统计日期月份只能小于当前月份！', 'warning');
				return;
			}
		}
		if(state=="2"){
			if((countDateStart==null || countDateStart=="") || (countDateEnd==null || countDateEnd=="")){
				$.messager.alert('提示信息', '统计日期不能为空！', 'error');
				//alert('查询条件不能为空！！！');
				return;
			}
		}
        $.messager.confirm("提示","确认上传征信报文吗？",function(r){
            if(r){
            	$.ajaxPackage({
        			type : 'post',
        			url : uploadUrl,
        			data : params,
        			dataType : "json",
        			success : function(data, textStatus, jqXHR) {
        				if(data.result!=null || data.result!=""){
        					$.messager.alert('提示信息',  data.result, 'info');
        				}
        			},
        			error : function(XMLHttpRequest, textStatus, errorThrown, d) {
        				console.info(XMLHttpRequest.status+"====="+XMLHttpRequest.readyState+"======"+textStatus+"========"+errorThrown);
        				console.info('异常信息', textStatus + '  :  ' + errorThrown + '!', 'error');
        			},
        			complete : function() {
        				// 显示窗口
        				//addEmployeePanel.window('open');
        			}
        		});
            }
        });
    });
	
	 /** 格式化业务月份**/
    formatDateBox("monthDate");
    /** 日期控件格式化 **/
    function formatDateBox(id){
        $('#'+id).datebox({
            onShowPanel: function () {//显示日趋选择对象后再触发弹出月份层的事件，初始化时没有生成月份层
                span.trigger('click'); //触发click事件弹出月份层
                if (!tds) setTimeout(function () {//延时触发获取月份对象，因为上面的事件触发和对象生成有时间间隔
                    tds = p.find('div.calendar-menu-month-inner td');
                    tds.click(function (e) {
                        // 禁止冒泡执行easyui给月份绑定的事件
                        e.stopPropagation();
                        // 得到年份
                        var year = /\d{4}/.exec(span.html())[0];
                        // 月份
                        var month = $(this).attr('abbr');
                        month = parseInt(month, 10);
                        // 隐藏日期对象、设置日期的值
                        $('#'+id).datebox('hidePanel').datebox('setValue', year + '-' + month);
                    });
                }, 0)
            },
            parser: function (s) {//配置parser，返回选择的日期
                if (!s){
                    return new Date();
                } 
                var arr = s.split('-');
                return new Date(parseInt(arr[0], 10), parseInt(arr[1], 10)-1, 1);
            },
            formatter: function (d) {
                var y = d.getFullYear();
                var m = d.getMonth() + 1;
                return y + '-' + (m<10?('0'+m):m);
            }//配置formatter，只返回年月
        });
        var p = $('#'+id).datebox('panel');//日期选择对象
        var tds = false; //日期选择对象中月份
        var span = p.find('span.calendar-text'); //显示月份层的触发控件
    }
	
    
    $("#state").combobox({  
	       onChange: function () {  
	           var state = $("#state").combobox('getText'); 
	           //alert(state);
	           if (state == "数据共享") { 
	        	   $("#title1").css('display','none');
	       		   $("#title2").css('display','block');
	       		   $("#countDateStart").datebox('setValue', '');
	       		   $("#countDateEnd").datebox('setValue', '');
	           }  else {  
	        	   $("#title1").css('display','block');
	       		   $("#title2").css('display','none');
	       		   $("#monthDate").datebox('setValue', '');
	           }  
	       }  
	}); 
    
});
