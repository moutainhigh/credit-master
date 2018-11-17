$(function() {
	Date.prototype.format = function (format) {  
	    var o = {  
	        "M+": this.getMonth() + 1, // month  
	        "d+": this.getDate(), // day  
	        "h+": this.getHours(), // hour  
	        "m+": this.getMinutes(), // minute  
	        "s+": this.getSeconds(), // second  
	        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter  
	        "S": this.getMilliseconds()  
	        // millisecond  
	    }  ;
	    if (/(y+)/.test(format))  
	        format = format.replace(RegExp.$1, (this.getFullYear() + "")  
	            .substr(4 - RegExp.$1.length));  
	    for (var k in o)  
	        if (new RegExp("(" + k + ")").test(format))  
	            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
	    return format;  
	};
	
	function formatDateboxMMSS(value) {  
	    if (value == null || value == '') {  
	        return '';  
	    }  
	    var dt;  
	    if (value instanceof Date) {  
	        dt = value;  
	    } else {  
	        dt = new Date(value);  
	    }  
	    return dt.format("yyyy-MM-dd hh:mm:ss"); //扩展的Date的format方法(上述插件实现)  
	} 
	$.importBuyBackLoan = {
			/** 表格数据源地址 **/
			dataGridUrl : global.contextPath + '/buyBack/buyBackLoanList',
			/** 导出信息 **/
			exportUrl : global.contextPath + '/buyBack/exportBuyBackLoanFile',
			/** 客户信息表格 **/
			dataGrid : $('#importBuyBackLoanDataGrid'),
			/** 分页控件 **/
			pager : undefined,
			/** 查询条件数据项表单实例 **/
			/** 每页显示的记录条数，默认为10 **/
			pageSize : 10,
			/** 设置每页记录条数的列表 **/
			pageSizeList : [10,20,30,40,50],
			/** 查询条件数据项表单实例 **/
	        searchForm : $('#searchForm'),
			/** 加载表格数据 **/
			reloadDataGrid : function() {
	            /** 获取查询表单数据转换成JSON对象 **/
	            var searchMsg = $.importBuyBackLoan.searchForm.serialize();
	            /** 对参数进行解码(显示中文) **/
	            searchMsg = decodeURIComponent(searchMsg);
	            /** 字符串转换为对象 **/
	            var queryParam = $.serializeToJsonObject(searchMsg);
	            /** 设置查询url **/
	            queryParam.url = $.importBuyBackLoan.dataGridUrl;
	            /** 加载数据信息 **/
				$.importBuyBackLoan.dataGrid.datagrid('reloadData',queryParam);
			}
		};
		/** 分页参数（page:当前第N页，rows:一页N行） **/
		$.importBuyBackLoan.pg = {
			'page' : 1,
			'rows' : $.importBuyBackLoan.pageSize
		};
	
	$.importBuyBackLoan.dataGrid.datagrid({
		pg : $.importBuyBackLoan.pg,
		/** 提交方式 **/
		method : 'get',
		/** 是否显示行号 **/
		rownumbers : true,
		/** 是否单选 **/
		singleSelect : true,
		/** 是否可折叠的 **/
		collapsible : false,
		/** 自适应列宽 **/
		fitColumns : false,
		fit : true,
		//height : '100%',
		/** 是否开启分页 **/
		pagination : true,
		loadMsg : '数据加载中,请稍等...',
        columns : [[{
            field : 'contractNum',
            title : '合同编号',
            width : '15%'
        }, {
            field : 'idnum',
            title : '身份证号',
            width : '15%'
        }, {
            field : 'buyBackTime',
            title : '回购时间',
            width : '14%',
            formatter:function(value,row,index){
                if(value){
                    return $.DateUtil.dateFormatToStr(value);
                }
            }
        }, {
            field : 'amount',
            title : '回购金额',
            width : '14%'
        }, {
            field : 'fundsSources',
            title : '合同来源',
            width : '14%'
        }, {
            field : 'loanBelongs',
            title : '债权去向',
            width : '14%',
            vType : 'rmb'
        }, {
            field : 'createTime',
            title : '导入时间',
            width : '14%',
            formatter: formatDateboxMMSS
        } ] ],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.importBuyBackLoan.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.importBuyBackLoan.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		}
	});
	
	
	$.importBuyBackLoan.pager = $.importBuyBackLoan.dataGrid.datagrid('getPager');
	$.importBuyBackLoan.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.importBuyBackLoan.pg.page = pageNumber;
			$.importBuyBackLoan.pg.rows = pageSize;
			$.importBuyBackLoan.reloadDataGrid();
		}
	});
	
	$.importBuyBackLoan.reloadDataGrid();
	
    /** 点击查询 **/
    $("#searchBtn").click(function(){
        $.importBuyBackLoan.pg.page = 1;
        // 查询处理
        search();
    });
    
    /** 查询处理 **/
    function search(){
        if(searhCheck()){
            $.importBuyBackLoan.reloadDataGrid();
        }
    }
    /** 查询校验 **/
    function searhCheck(){
        if(!$.importBuyBackLoan.searchForm.form("validate")){
            return false;
        }
        return true;
    }
    
    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.importBuyBackLoan.searchForm.form('reset');
    });
    
    // 批量导入窗口对象
    var importExcelWin = $("#importExcelWin");    
    var uploadUrl=global.contextPath+"/buyBack/buyBackLoanImport";
    
    // 导入文件表单对象
    var baseFileForm = $("#baseFileForm");
	
    
    /** 打开导入窗口 **/
    $('#batchImportBtnreturnLoanImport').click(function(){
        importExcelWin.window('open');
        baseFileForm.form('clear');
    });
    
    /** 批量导入处理 **/
    $("#batchImport").click(function(){
        var file = $("#uploadfile").filebox("getValue");
        if($.isEmpty(file)){
            $.messager.alert('警告','请选择导入文件!','warning');
            return;
        }
        $.messager.confirm("操作提示","确认导入吗？",function(r){
            if(r){
                baseFileForm.ajaxSubmit({
                    type: "post",
                    dataType : 'json',
                    url: uploadUrl,
                    hasDownloadFile:true,                 
                    success: function (data) {
                        setTimeout(function(){
                            importExcelWin.window('close');
                            $.importBuyBackLoan.reloadDataGrid();	
                        }, 1000);
                    },
                    error: function (data) {
                    	if(data!=null&&data.hasOwnProperty("resMsg")){
                   		 $.messager.alert('操作提示',data.resMsg,'warning');
                   	}else{
                   		 $.messager.alert('操作提示','文件上传失败!','warning');
                   	}
                    }
                });
            }
        });
    });
    /** 导出回购债权信息 **/
    $("#exportBtn").click(function(){
        $.messager.confirm("提示","最大可导出50000条记录，请确认要导出Excel文件吗？",function(r){
            if(r){
                $.downloadFile({
                    url:$.importBuyBackLoan.exportUrl,
                    isDownloadBigFile:true,
                    params:$.importBuyBackLoan.searchForm.serializeObject(),
                    successFunc:function(data){
                        if(data== null){
                            $.messager.alert('提示','下载成功！','info');
                        }else{
                            if(data.resMsg!= null){
                                $.messager.alert('警告',data.resMsg,'warning');
                            }else{
                                $.messager.alert('异常','下载失败！','error');
                            }
                        }
                    },
                    failFunc:function(data){
                        $.messager.alert('异常','下载失败！','error');
                    }
                });
            }
        });
    });
});