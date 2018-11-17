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
	    };  
	    if (/(y+)/.test(format))  
	        format = format.replace(RegExp.$1, (this.getFullYear() + "")  
	            .substr(4 - RegExp.$1.length));  
	    for (var k in o)  
	        if (new RegExp("(" + k + ")").test(format))  
	            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
	    return format;  
	};  
	function formatDatebox(value) {  
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
	
	
	//表格数据源地址
	var dataGridUrl = global.contextPath + '/system/baseMessage/listAllMessage';
	
	var dataViewGridUrl = global.contextPath + '/system/baseMessage/listOneMessage';
	//新增 修改地址
	var insertUrl = global.contextPath + '/system/baseMessage/insert';
	//查询所有员工数据信息
	var loadDataComEmployeeUrl = global.contextPath + '/system/user/findEmployeeByEmployeeTypeNotCheckDataGrid';
	
	var updateFlagUrl = global.contextPath + '/system/baseMessage/updatebaseMessageByState';
	
	var deleteUrl = global.contextPath + '/system/baseMessage/delete';	
	
	
	//表格实例
	var messageDataGrid = $('#messageDataGrid');
	var addDataPanel = $('#addDataPanel');
	var viewDataPanel = $('#viewDataPanel');
	//新增|修改数据项表单实例
	var dataForm = $('#dataForm');
	//每页显示的记录条数，默认为10
	var pageSize = 10;
	//设置每页记录条数的列表
	var pageSizeList = [10,20,30,40,50];
	$('#addBut').click(function(){
		addButEvent();
	})
	
	$('#delBut').click(function(){
		delButEvent();
	})
	$('#delButs').click(function(){
		delButEvents();
	})
	
	$('#updateBut').click(function(){
		updateButEvent();
	})
	
	$('#viewBut').click(function(){
		viewButEvent();
	})
	$('#updateButs').click(function(){
		updateButEvents();
	})
	messageDataGrid.datagrid({
		url : dataGridUrl,
		//提交方式
		method : 'get',
		//是否显示行号
		rownumbers : false,
		//是否单选
		singleSelect : false,
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
		      {field:"ck",checkbox:"true",width : 50}
		]],
		columns : [[
		      //列定义
		      {field : 'id',title : 'ID',width : 100 , hidden:true},
		      {field : 'senderName',title : '发件人',width : 100},
		      {field : 'title',title : '标题',width : 100},
		      {field : 'sendTime',title : '发送时间',width : 100 ,formatter: formatDatebox},		
		      {field : 'state',title : '状态',width : 100,hidden:true},
		      {
		    	  field : 'controlle',
		    	  title : '操作',
		    	  width : 100,
		    	  formatter:function(value, row, index) {
		    		  if(row.type=='2'){//回盘提醒
		    			  var element = "";
		    			  element = "<a href='javascript:void(0)' class='easyui-linkbutton' onclick='reliefPenaltyTab();'>减免申请</a>";
		    			  return element;
		    		  }      					
      			  }
		      }
		]],
		//每页显示的记录条数，默认为10
		pageSize : pageSize,
		//可以设置每页记录条数的列表
		pageList : pageSizeList,
		toolbar : '#tb',
		//自定义行样式
		rowStyler : function(index,row) {
			 if (row.state =='1'){
	                return 'background-color:#6293BB;color:#fff;font-weight:bold;';
	            }
		}
	
	});

	//客户经理下拉表格
	$(".employee.easyui-combogrid").combogrid({
		url:global.contextPath + '/system/user/findEmployeeByEmployeeTypeNotCheckDataGrid',
		panelWidth : 620,
		panelHeight : 250,
		idField : 'id',
		textField : 'name',
		method:"POST",
		pageSize : 6, 
		pageList : [6], 
		rownumbers : true,
		fitColumns: true,
		striped : true,
		loadMsg:"加载中...",
		editable : false,
		pagination : true, 
		pg :  {'page' : 1,'rows' : 6},
		columns : [[
		      { field : 'name', title : '姓名', width : 60 },
		      { field : 'userCode', title : '工号', width : 60 },
		      { field : 'orgName', title : '营业网点', width : 60 }
		]],
		loader:function(param,success,error){
			var $self = $(this),
			    opts =$self.datagrid("options"),
			    isCanQuery = opts.isCanQuery||false;
			if(isCanQuery){
				$.ajaxPackage({
					url:opts.url,
					async:true,
					type:opts.method,
					data:param,
					dataType:"json",
					isShowLoadMask:false,
					success:function(response, textStatus, jqXHR){
						if(response.resCode == "000000"){
							success(response.attachment);
							var $combogrids = $(".salesman.easyui-combogrid");
							for(var i = 0 ; i < $combogrids.length; i++){
								if($($combogrids[i]).combogrid("grid")[0] == $self[0] ){
									$($combogrids[i]).combogrid("setValue","");
								}
							}
						}else{
							$.messager.alert("提示", response.resMsg, "error", function(){
								error.apply(this, arguments);
							});
						}
					},
					error:function(response, textStatus, jqXHR){
						$.messager.alert("提示", "查询失败", "error", function(){
							error.apply(this, arguments);
						});
					},
					complete:function(jqXHR,textStatus){
//						$(".easyui-linkbutton.salesman.query",opts.toolbar).linkbutton("enable");
					}
				});
			}
			return isCanQuery;
		}
	});
    //查询客户经理
    $(".easyui-linkbutton.employee.query").bind("click",function(envent){
    	if($(this).linkbutton("options").disabled==false){
	    	if($($(this).linkbutton("options").formId).form("validate")){
	    		var params = $($(this).linkbutton("options").formId).serializeObject()||{};
	    		params.inActive="t";
	    		var dataGrid = $($(this).linkbutton("options").dataGridId).combogrid("grid");
	    		$(dataGrid).datagrid("options").isCanQuery = true;
	    		$(dataGrid).datagrid("load",params);
	    	}
    	}
    });
    //清空客户经理
    $(".easyui-linkbutton.employee.clear").bind("click",function(envent){
    	if($(this).linkbutton("options").disabled==false){
    		 $($(this).linkbutton("options").dataGridId).combogrid("setValue","");
    	}
    });
	
	//新增面板参数定义
	addDataPanel.window({
		width : 600,
		height : 500,
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
	})
	
	
	viewDataPanel.window({
		width : 600,
		height : 500,
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
	})
	
	
	//查询按钮添加事件
	function searchButEvent() {
		reloadDataGrid();
	}	
	
	//新增按钮添加事件
	function addButEvent() {
		dataForm.form('clear');
		addDataPanel.window('open');
		addDataPanel.window("center");
	}
	
	/**
	 * 标记未读
	 */
	function updateButEvent(){
		var ids = [];
		var rows = $('#messageDataGrid').datagrid('getSelections');
		for(var i=0; i<rows.length; i++){
		    ids.push(rows[i].id);
		}
		if(ids.length==0){
			$.messager.alert('结果',"请选择相应的消息标识为未读！");
			return;	
		}
		$.ajaxPackage({
			type : 'get', 
			url : updateFlagUrl+ '?ids=' + ids,
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				if (resCode == '000000') {
					$.messager.alert('结果',"操作成功!");
					 parent.$.loadMessageCount();//更新首页 消息中心条数
					 $('#messageDataGrid').datagrid('reload'); 
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
	}
	
	function updateButEvents(){
		var ids = [];
		ids[0]=$("#viewId").val();		
		$.ajaxPackage({
			type : 'get', 
			url : updateFlagUrl+ '?ids=' + ids,
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				if (resCode == '000000') {
					$.messager.alert('结果',"操作成功!");
					 parent.$.loadMessageCount();//更新首页 消息中心条数
					 $('#messageDataGrid').datagrid('reload');  
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
	}
	
	/**
	 * 标记删除
	 */
	function delButEvent(){
		
		var ids = [];
		var rows = $('#messageDataGrid').datagrid('getSelections');
		for(var i=0; i<rows.length; i++){
		    ids.push(rows[i].id);
		}
		if(ids.length==0){
			$.messager.alert('结果',"请选择相应的消息进行删除！");
			return;	
		}
		$.ajaxPackage({
			type : 'get', 
			url : deleteUrl+ '?ids=' + ids,
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				if (resCode == '000000') {
					$.messager.alert('结果',"操作成功!");
					 parent.$.loadMessageCount();//更新首页 消息中心条数
					 $('#messageDataGrid').datagrid('reload'); 
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
	}
	
	function delButEvents(){
		var ids = [];
		ids[0]=$("#viewId").val();
		$.ajaxPackage({
			type : 'get', 
			url : deleteUrl+ '?ids=' + ids,
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				if (resCode == '000000') {
					$.messager.alert('结果',"操作成功!");						
					 $('#messageDataGrid').datagrid('reload'); 
					 viewDataPanel.window('close');				
					 
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
	}
	
	/**
	 * 查看
	 */
	function viewButEvent(){
		var ids = [];
		var rows = $('#messageDataGrid').datagrid('getSelections');
		for(var i=0; i<rows.length; i++){
		    ids.push(rows[i].id);
		}
		if(ids.length==0){
			$.messager.alert('结果',"请选择相应的消息进行查看！");
			return;	
		}
		if(ids.length>1){
			$.messager.alert('结果',"只能请选择一条消息进行查看，不能同时选择多条信息进行查看！");
			return;	
		}		
		$.ajaxPackage({
			type : 'get', 
			url : dataViewGridUrl+ '?ids=' + ids,
			dataType : "json",
			success : function (data,textStatus,jqXHR) {				
				var resCode = data.resCode;				
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					$('#dataFormView').form('load',attachment);
					 viewDataPanel.window('open');
					 viewDataPanel.window("center");
					 $('#messageDataGrid').datagrid('reload');
					 parent.$.loadMessageCount();//更新首页 消息中心条数
				} else {
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
		
	}
	
	
	//全局关闭按钮事件
	$('a[name=closeBut]').click(function(){
		//找到按钮属于的Window对象
		var ownWin = $(this).parents("div.easyui-window:eq(0)");
		if (ownWin.length) {			
			ownWin.window('close');
			
		}
	})
	
		//指定某Window下提交按钮添加事件
	$(addDataPanel).find('a[name=submitBut]:eq(0)').click(function(){
		var type=document.getElementsByName("type")[0].value;
		if(type !=0 && type !=1 && type !=2){
			$.messager.alert('结果',"消息类型选择不正确！");
			return;
		}
		
		var receiverValue=$(".employee.easyui-combogrid").combogrid("getValue");
		if(receiverValue==""){
			$.messager.alert('结果',"收件人不能为空");
			return;
		}		
		
		//检查表单项是否通过验证
		if (dataForm.form('validate')) {	
			//提交到服务端，进行处理
			$.ajaxPackage({
				type : 'post', 
				url : insertUrl,
				data : dataForm.serialize(),			
				dataType : "json",
				success : function (data) { 
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					if (resCode == '000000') {
						//操作成功 重新加载列表数据
						resMsg = '操作成功';
						$.messager.alert('结果',resMsg);
						 $('#messageDataGrid').datagrid('reload');
						 parent.$.loadMessageCount();//更新首页 消息中心条数
					} else {
						$.messager.alert('提示信息',resMsg,'error');
					}
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					//关闭窗口
					addDataPanel.window('close');
				}
			});
		}
	});
	
	//获取选中的行
	function getSelectedRow(dataGrid) {
		var selectedRow = dataGrid.datagrid('getSelected');
		return selectedRow;
	}
	
	//加载表格数据
	function reloadDataGrid() {
		//获取查询表单数据转换成JSON对象
		var searchMsg = messageDataGrid.serialize();
		//对参数进行解码(显示中文)
		searchMsg = decodeURIComponent(searchMsg);
		var queryParam = $.serializeToJsonObject(searchMsg);  
		queryParam.r = new Date().getTime();
		$(testDataGrid).datagrid('reload',queryParam);
	}
	
})
//跳转减免申请页面
function reliefPenaltyTab(){
	var tab = {};
//		    tab.id = 'personDetail_' + id;
	tab.text = '减免申请';
	tab.iconCls = "pic_1";
	tab.url = global.contextPath + '/applyReliefRepayManager/applyReliefRepayment';
	// ** 调用父级添加选项卡方法 **//*
	parent.$.iframeTabs.add(tab);
}
