
$(function() {
	$.undoManage = {
		/** 表格数据源地址 * */
		dataGridUrl : global.contextPath + '/chexiao/searchUndoManage',
		
		/**执行撤销操作**/
		undo : global.contextPath + '/chexiao/undo',
		
		/**更新还款状态操作**/
		updateState : global.contextPath + '/chexiao/updateState',
		/**合同编号控件**/
		contract_num:$('#contract_num'),
		/** 客户流水信息表格 * */
		dataGrid : $('#dataGrid'),		
		/** 分页控件 * */
		pager : undefined,
		/** 查询条件数据项表单实例 * */
		searchForm : $('#searchForm'),
		/** 提交更新还款状态表单实例 * */
		dataForm : $('#dataForm'),
		/** 更改还款状态面板实例**/
		updateStatePanel : $('#updateStatePanel'),
		/** 每页显示的记录条数，默认为10 * */
		pageSize : 10,
		/** 设置每页记录条数的列表 * */
		pageSizeList : [ 10, 20, 30, 40, 50 ],
		/** 加载表格数据 * */
		reloadDataGrid : function() {
			if ($.undoManage.searchForm.form('validate')) {
				/** 获取查询表单数据转换成JSON对象 * */
				var searchMsg = $.undoManage.searchForm.serialize();
				/** 对参数进行解码(显示中文) * */
				searchMsg = decodeURIComponent(searchMsg);
				var queryParam = $.serializeToJsonObject(searchMsg);
				/** 未输入查询条件不给予查询 * */
				if ($.isEmpty(queryParam.contract_num) ) {
					$.messager.alert('提示信息', '请输入合同编号!', 'warning');
				} else {
					queryParam.url = $.undoManage.dataGridUrl;
					$.undoManage.dataGrid.datagrid('reloadData', queryParam);
				}
			}
		}
	}
	
	/** 分页参数（page:当前第N页，rows:一页N行） **/
	$.undoManage.pg = {
		'page' : 1,
		'rows' : $.undoManage.pageSize
	}
    /** 重置处理 **/
    $('#clearBtn').click(function() {
    	$.undoManage.searchForm.form('reset');
    });
	/**查询**/
	$('#searchBtn').click(function() {
		$.undoManage.pg.page = 1;
		$.undoManage.reloadDataGrid();
	});
	
	$.undoManage.dataGrid.datagrid({
		pg : $.undoManage.pg,
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
		      {field : 'id',title : '债权编号',width : 2},
		      {field : 'tradeno',title : '流水号',width : 3},
		      {field : 'name',title : '姓名',width : 1},
		      {field : 'loanstate',title : '当前状态',width : 1},
		      {
		    	  field : 'tradedate',
		    	  title : '流水生成日期',
		    	  width : 2,
		    	  formatter : $.DateUtil.dateFormatToStr
		      },
		      {field : 'amount',title : '交易金额',width : 1},
		      {field : 'trade_type',title : '交易类型',width : 1},
		      {field : 'promise_return_date',title : '约定还款日',width : 1,hidden: true},
		      {
		    	  field : 'undo',
		    	  title : '撤销操作',
		    	  width : 4,
		    	  formatter : function(value, row, index) {
		    		  var tradeno = row.tradeno;
		    		  var str = "<a href='javascript:void(0)'  class='chexiaolink' tradeno = '"+row.tradeno+"' tradedate = '"+row.tradedate+"' promise_return_date = '"+row.promise_return_date+"' loanstate = '"+row.loanstate+"' style='margin-right: 20px;'>撤销</a>";
						if(index != 0||$.undoManage.pg.page != 1){
							str = "";
						}
						return str;
				  }
		      }
		]],
		/** 每页显示的记录条数，默认为10 **/
		pageSize : $.undoManage.pageSize,
		/** 可以设置每页记录条数的列表 **/
		pageList : $.undoManage.pageSizeList,
		toolbar : '#tb',
		/** 自定义行样式 **/
		rowStyler : function(index,row) {
			if (index % 2 == 0) {
				//return 'background-color:#AABBCC;color:#fff;';
			}
		},  
		onLoadSuccess:function(data){
            $(".chexiaolink").linkbutton({
                text:'撤销',
                plain:true,
                iconCls:'pic_68',
                onClick:function(){
                	var tradeno  = $(this).attr("tradeno");
                	var tradedate  = $(this).attr("tradedate"); 
                	var promise_return_date  = $(this).attr("promise_return_date"); 
                	$.messager.confirm("提示","确认撤销吗？",function(r){
                        if(r){
                            var json = {"tradeno":tradeno,"tradedate":tradedate,"promise_return_date":promise_return_date};
                            $.ajaxPackage({
                                url:$.undoManage.undo,
                                type:"post",
                                data: json,
                                dataType:"json",
                                success:function(response, textStatus, jqXHR){
                                    var resCode = response.resCode;
                                    var resMsg = response.resMsg;
                                   if(resCode != "000000"){
                                     $.messager.alert("警告", resMsg, "warning");
                                     return;
                                   }
                                    $.messager.alert("撤销提示", resMsg, "info");
                                    setTimeout(function(){
                                        /** 刷新流水结果 **/
                                        $.undoManage.reloadDataGrid();
                                    }, 600);
                                },
                                error:function(response, textStatus, jqXHR){
                                    $.messager.alert("异常", "操作失败", "error");
                                },
                                complete:function(jqXHR,textStatus){
                                }
                            });
                        }
                    });
                
                }
            });
            /** 页面自适应 **/
            $.undoManage.dataGrid.datagrid('resize');
        }
	});
	
	 /** 打开更新还款状态面板 **/
    $("#updateBtn").click(function(){
    	var selectedRow = $.undoManage.dataGrid.datagrid('getSelected');
    	//$.undoManage.dataGrid.datagrid('loadData', { total: 0, rows: [] });
    	/** 未选中记录不给予更新 * */
    	if(!selectedRow){
            $.messager.alert('警告','请选中一条记录!','warning');
            return;
        } 
    	if(selectedRow.loanstate=="预结清"){
			  //显示窗口
	       $.undoManage.updateStatePanel.window('open');
		}else{
    		$.messager.alert('提示信息', '只有预结清客户才可以操作哦！', 'warning');
    		}
    });
	
	
	
	$.undoManage.updateStatePanel.window({
        width : 350,
        height : 150,
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
    });
	
	
	/**  提交更新处理 **/
    $("#submitBtn").click(function(){
        if ($.undoManage.dataForm.form('validate')) {
            var updateLoanState = $("#updateLoanState").combobox('getValue');
            var loanState = $.undoManage.dataGrid.datagrid('getSelected').loanstate;
            var loanid = $.undoManage.dataGrid.datagrid('getSelected').id;
            var word = "当前还款状态为  <font color='blue'>" + loanState + "</font><br/>变更后的状态为  <font color='blue'>" + updateLoanState + "</font><br/>请确认！";
            $.messager.confirm("提示",word,function(r){
                if(r){
                	var json = {"loanid":loanid,"updateLoanState":updateLoanState};
                	$.ajaxPackage({
                        url: $.undoManage.updateState,
                        type:"post",
                        data: json,
                        dataType:"json",
                        success:function(response, textStatus, jqXHR){
                        	$.undoManage.updateStatePanel.window('close');
                            var resCode = response.resCode;
                            var resMsg = response.resMsg;
                           if(resCode != "000000"){
                        	   
                             $.messager.alert("警告", resMsg, "warning");
                             return;
                           }
                            $.messager.alert("提示", "更新还款状态成功！", "info");
                            setTimeout(function(){
                                /** 刷新流水结果 **/
                                $.undoManage.reloadDataGrid();
                            }, 600);
                        },
                        error:function(response, textStatus, jqXHR){
                        	$.undoManage.updateStatePanel.window('close');
                            $.messager.alert("异常", "操作失败", "error");
                        }
                    });
        
        }
    });
        }
    });
	
	
	/**  关闭更新窗口 **/
    $("#closeBtn").click(function(){
        if($.undoManage.updateStatePanel){
        	$.undoManage.updateStatePanel.window('close');
        }
    });
    
	$.undoManage.pager = $.undoManage.dataGrid.datagrid('getPager');
	$.undoManage.pager.pagination({
		onSelectPage : function(pageNumber,pageSize) {
			$.undoManage.pg.page = pageNumber;
			$.undoManage.pg.rows = pageSize;
			$.undoManage.reloadDataGrid();
		}
	});
	$.undoManage.dataGrid.datagrid('resize');
})