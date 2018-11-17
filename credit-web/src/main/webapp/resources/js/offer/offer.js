$(function() {
    Date.prototype.format = function(format) {
        var o = {
            "M+" : this.getMonth() + 1, // month
            "d+" : this.getDate(), // day
            "h+" : this.getHours(), // hour
            "m+" : this.getMinutes(), // minute
            "s+" : this.getSeconds(), // second
            "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
            "S" : this.getMilliseconds()
        // millisecond
        };
        if (/(y+)/.test(format)){
            format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for ( var k in o){
            if (new RegExp("(" + k + ")").test(format)){
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
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
        return dt.format("yyyy-MM-dd "); // 扩展的Date的format方法(上述插件实现)
    }
    
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
        return dt.format("yyyy-MM-dd hh:mm:ss"); // 扩展的Date的format方法(上述插件实现)
    }
    var offerPerms = $("#offerParams").data("offerPerms");
    var offerPerms = offerPerms.replace("\[", "");
    offerPerms = offerPerms.replace("\]", "");
    offerPerms = offerPerms.split(",");
    var permsObj = {};
    $.each(offerPerms, function(index, value) {
        permsObj[$.trim(value)] = $.trim(value);
    });
    var isShowPayChannel = $("#isShowPayChannel").val();//是否显示划扣通道
    $.offerlist = {
        /** 表格数据源地址 * */
        dataGridUrl : global.contextPath + '/offer/offerInfo/search',
        /** 导出委托代扣报盘信息url **/
        exportUrl : global.contextPath + '/offer/offerInfo/exportOfferFile',
        /** 客户信息表格 * */
        dataGrid : $('#dataGrid'),
        /** 分页控件 * */
        pager : undefined,
        /** 查询条件数据项表单实例 * */
        searchForm : $('#searchForm'),
        /** 每页显示的记录条数，默认为10 * */
        pageSize : 10,
        /** 设置每页记录条数的列表 * */
        pageSizeList : [ 10, 20, 30, 40, 50 ],
        /** 加载表格数据 * */
        reloadDataGrid : function() {
            /** 获取查询表单数据转换成JSON对象 * */
            var searchMsg = $.offerlist.searchForm.serialize();
            /** 对参数进行解码(显示中文) * */
            searchMsg = decodeURIComponent(searchMsg);
            var queryParam = $.serializeToJsonObject(searchMsg);
            queryParam.url = $.offerlist.dataGridUrl;
            $.offerlist.dataGrid.datagrid('reloadData', queryParam);
        }
    };

    /** 分页参数（page:当前第N页，rows:一页N行） * */
    $.offerlist.pg = {
        'page' : 1,
        'rows' : $.offerlist.pageSize
    };

    $.offerlist.dataGrid.datagrid({
        pg : $.offerlist.pg,
        /** 提交方式 * */
        method : 'get',
        /** 是否显示行号 * */
        rownumbers : true,
        /** 是否单选 * */
        singleSelect : true,
        /** 是否可折叠的 * */
        collapsible : false,
        /** 自适应列宽 * */
        fitColumns : true,
        fit : true,
        // height : '100%',
        /** 是否开启分页 * */
        pagination : true,
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
                /** 列定义 * */
                {
                    field : 'id',
                    title : 'ID',
                    hidden:true
                },
                {
                    field : 'loanId',
                    title : 'loanId',
                    hidden : true
                },
                {
                    field : 'offerDate',
                    title : '报盘日期',
                    width : 8,
                    formatter : formatDatebox
                },
                {
                    field : 'name',
                    title : '借款人',
                    width : 7
                },
                {
                    field : 'bankName',
                    title : '银行',
                    width : 8
                },
                {
                    field : 'bankAcct',
                    title : '账号',
                    width : 9
                },
                {
                    field : 'amount',
                    title : '报盘金额',
                    width : 6,
                    vType : 'rmb'
                },
                {
                    field : 'actualAmount',
                    title : '回盘金额',
                    width : 6,
                    vType : 'rmb'
                },
                {
                    field : 'updateTime',
                    title : '最后变更日期',
                    width : 8,
                    formatter : formatDateboxMMSS
                },
                {
                    field : 'memo',
                    title : '备注',
                    width : 8
                },
                {
                    field : 'state',
                    title : '状态',
                    width : 8
                },
                {
                    field : 'type',
                    title : '划扣方式',
                    width : 5
                },
                {
                    field : 'paySysNo',
                    title : '划扣通道',
                    width : 7
                },
                {
                    field : 'contractNum',
                    title : '合同编号',
                    width : 10
                },
                {
                    field : 'fundsSources',
                    title : '合同来源',
                    width : 8
                    
                },
                {
                    field : 'creator',
                    title : '操作人',
                    width : 7
                },
                {
                    field : 'operate',
                    title : '操作',
                    width : 15,
                    formatter : function(value, row, index) {
                        var str = "";
                        if (row != null && row.id != null && (formatDatebox(new Date()) == formatDatebox(row.offerDate)) 
                                && (row.state != '已关闭')) {
                            if (permsObj["/offer/offerInfo/insert"] != null) {
                                str += "<a href='javascript:void(0)' class='realTime' loanId='"
                                        + row.loanId
                                        + "' offerId='"
                                        + row.id
                                        + "' amount='"
                                        + row.amount
                                        + "' actualAmount='"
                                        + row.actualAmount
                                        + "' state='"
                                        + row.state
                                        + "' name='"
                                        + row.name
                                        + "' idnum='"
                                        + row.idnum
                                        + "' contractNum='"
                                        + row.contractNum + "'></a>";
                            }
                            if (permsObj["/offer/offerInfo/updateOfferInfoState"] != null) {
                                if (row.type == '自动划扣' && row.paySysNo != '包商银行' && (row.state == '未报盘' || row.oldState == '已回盘非全额')) {// 当天的未报盘，已回盘非全额划扣类型是自动划扣
                                    str += "<a href='javascript:void(0)' class='closeOffer' loanId='"
                                        + row.loanId
                                        + "' offerId='"
                                        + row.id
                                        + "' oldState='"
                                        + row.oldState
                                        + "' amount='"
                                        + row.amount
                                        + "' actualAmount='"
                                        + row.actualAmount
                                        + "' state='"
                                        + row.state
                                        + "' name='"
                                        + row.name
                                        + "' idnum='"
                                        + row.idnum
                                        + "' contractNum='"
                                        + row.contractNum
                                        + "' ></a>";
                                }
                            }
                        }
                        if (row != null && row.id != null) {
                            str += "<a href='javascript:void(0)' class='easyui-linkbutton withdrawDeposits' loanId='"
                                    + row.loanId
                                    + "' offerId='"
                                    + row.id
                                    + "' fundsSources='"
                                    +row.fundsSources
                                    + "' isThird='"
                                    + row.isThird
                                    + "' name='"
                                    + row.name
                                    + "' ></a>";
                        }
                        return str;
                    }
                } 
            ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.offerlist.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.offerlist.pageSizeList,
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess : function(data) {
            
            /** 查看详情 **/
            $(".easyui-linkbutton.withdrawDeposits").linkbutton({
                text : '查看报盘详情',
                plain : true,
                iconCls : 'icon-search',
                onClick : function() {
                    var offerId = $(this).attr("offerId");
                    var fundsSources = $(this).attr("fundsSources");
                    var isThird = $(this).attr("isThird");
                    var name = $(this).attr("name");
                    top.$.iframeTabs.add({
                        id : "offerDetail-WithdrawDeposits-" + offerId,
                        text : "报回盘明细查询("+ name + ")",
                        url : global.contextPath + "/offer/offerInfo/offerInfoDetail?offerId=" + offerId + "&isThird=" + isThird +"&fundsSources="+fundsSources
                    });
                }
            });
            
            /** 关闭报盘 **/
            $(".closeOffer").linkbutton({
                text : '关闭',
                plain : true,
                iconCls : 'icon-cancel',
                onClick : function() {
                    var offerId = $(this).attr("offerId");
                    var tipMessage = '确认对该报盘记录进行关闭吗？';
                    $.messager.confirm("提示",tipMessage,function(r) {
                        if (r) {
                            $.ajaxPackage({
                                type : 'get',
                                url : global.contextPath + "/offer/offerInfo/updateOfferInfoState" + "?offerId=" + offerId,
                                dataType : "json",
                                success : function(data,textStatus,jqXHR) {
                                    var resCode = data.resCode;
                                    var resMsg = data.resMsg;
                                    if (resCode == '000000') {
                                        $.messager.alert('结果',"操作成功!");
                                        if ($("#searchForm").form("validate")) {
                                            $.offerlist.pg.page = 1;
                                            $.offerlist.reloadDataGrid();
                                        }
                                    } else {
                                        /** 操作失败* */
                                        $.messager.alert('提示信息',resMsg,'error');
                                    }
                                },
                                error : function(XMLHttpRequest,textStatus,errorThrown,d) {
                                    $.messager.alert('提示信息',textStatus + ' : ' + errorThrown + '!','error');
                                },
                                complete : function() { 

                                }
                            });
                        }
                    });
                }
            });
            
            /** 实时划扣 **/
            $(".realTime").linkbutton({
                text : '实时划扣',
                plain : true,
                iconCls : 'icon-ok',
                onClick : function() {
                    dataForm.form('clear');
                    var state = $(this).attr("state");
                    if (state == '已报盘') {
                        $.messager.alert('提示信息','该记录未回报盘,不能进行实时划扣', 'error');
                        return;
                    }
                    
                    $("#borrowName", addDataPanel).textbox("setValue",$(this).attr("name"));
                    $("#borrowIdum", addDataPanel).textbox("setValue",$(this).attr("idnum"));
                    $("#contractNumAdd", addDataPanel).textbox("setValue",$(this).attr("contractNum"));
                    if(isShowPayChannel == 'true'){
                    	//fasle 不显示通道走路由，true 显示通道，走通道
                    	initPaySysNoDataAdd($(this).attr("contractNum"));
                    }
                    addDataPanelWin('edit');
                }
            });
            
            /** 页面自适应 **/
            $.offerlist.dataGrid.datagrid('resize');
        }
    });
    
    /** 分页组件初始化 **/
    $.offerlist.pager = $.offerlist.dataGrid.datagrid('getPager');
    $.offerlist.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.offerlist.pg.page = pageNumber;
            $.offerlist.pg.rows = pageSize;
            $.offerlist.reloadDataGrid();
        }
    });

    var realtimeDeductPreUrl = global.contextPath + '/offer/offerInfo/insert';
    var addDataPanel = $('#addDataPanel');
    var dataForm = $('#dataForm');
    // 新增面板参数定义
    addDataPanel.window({
        width : 400,
        height : 300,
        // 定义窗口是不是模态窗口
        modal : true,
        // 定义是否显示折叠按钮
        collapsible : false,
        // 定义是否显示最小化按钮
        minimizable : false,
        // 定义是否显示最大化按钮
        maximizable : false,
        // 定义是否显示关闭按钮
        closable : true,
        // 定义是否关闭了窗口
        closed : true,
        // 定义是否窗口能被拖拽
        draggable : true,
        // 定义是否窗口可以调整尺寸
        resizable : false,
        // 如果设为 true， 当窗口能够显示阴影的时候将会显示阴影。
        shadow : true,
        // 定义如何放置窗口 true 就放在它的父容器里 false 就浮在所有元素的顶部
        inline : true,
        // 样式定义
        iconCls : 'icon-save'
    });
    
    //打开实时划扣的窗口
    function addDataPanelWin(open){
    	addDataPanel.window('open');
    	if(open == 'add'){
    		dataForm.form('clear');
    		$('#paySysNoAddStrDiv').hide();
            $('#paySysNoAddDiv').hide();
            //$("#submitBut").linkbutton("enable");
    	}
    };
    
    /** 点击查询处理 **/
    $('#searchBut').click(function() {
        if ($("#searchForm").form("validate")) {
            $.offerlist.pg.page = 1;
            $.offerlist.reloadDataGrid();
        }
    });
    
    /** 点击重置处理 **/
    $("#clearCondition").bind("click", function(envent) {
        if (!$(this).linkbutton("options").disabled) {
            $("#searchForm").form("reset");
        }
    });
    
    /** 点击实时划扣处理 **/
    $('#addBut').click(function() {
    	addDataPanelWin('add');
    });
    
    $("input",$("#contractNumAdd").next("span")).blur(function(){ 
    	if(isShowPayChannel == 'true'){
    		//true 显示通道，走通道 ；fasle 不显示通道走路由，
    		initPaySysNoDataAdd('add'); 
    	}
    });
    
    $('#paySysNoAdd').combobox({
		valueField : 'id',
		textField : 'text',
		filter : function(q,row) {
			var opts=$(this).combobox("options");
			return row[opts.textField].indexOf(q)>-1;
		},
		formatter : function(row) {
			var opts=$(this).combobox("options");
			return row[opts.textField];
		}
	});
    
    /** 根据合同编号查询划扣通道  **/
    function initPaySysNoDataAdd(contractNum) {
    	if(contractNum == 'add'){
    		contractNum = $("#contractNumAdd").val();
    	}
    	$("#submitBut").linkbutton("disable");
		$.ajaxPackage({
			type : 'post', 
			url : global.contextPath + '/offer/offerInfo/getPaySysNo',
			isShowLoadMask : true,
			data : {"contractNum" : contractNum},
			dataType : "json",
			success : function (data) { 
				/** data 服务端返回数据 **/
				if(data.length > 0){
					$('#paySysNoAdd').combobox('loadDataExt',data);
					$('#paySysNoAdd').combobox('defaultOneItem');
					$('#paySysNoAddStrDiv').show();
					$('#paySysNoAddDiv').show();
				}else{
					$('#paySysNoAddStrDiv').hide();
					$('#paySysNoAddDiv').hide();
				}
				$("#submitBut").linkbutton("enable");
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
	};
    
    /**  实时划扣提交按钮  **/
    $('#submitBut').click(function(){
        // 检查表单项是否通过验证
        if (dataForm.form('validate')) {
            var offerAmount = $('#offerAmount').val();
            if (Number(offerAmount) < 0) {
                $.messager.alert('警告', '报盘金额必须大于零', 'warning');
                return;
            }
            var searchMsg = dataForm.serialize();
            searchMsg = decodeURIComponent(searchMsg);
            var data = $.serializeToJsonObject(searchMsg);
            data.isShowPayChannel = isShowPayChannel;
            // 提交到服务端，进行处理
            $.ajaxPackage({
                type : 'post',
                url : realtimeDeductPreUrl,
                data : data,
                dataType : "json",
                success : function(data) {
                    var resCode = data.resCode;
                    var resMsg = data.resMsg;
                    if (resCode != '000000') {
                        $.messager.alert('警告', resMsg, 'warning');
                        return;
                    }
                    // 操作成功 重新加载列表数据
                    $.messager.alert('提示', resMsg,"info");
                    $.offerlist.reloadDataGrid();
                    addDataPanel.window('close');
                },
                error : function(XMLHttpRequest, textStatus,errorThrown, d) {
                    $.messager.alert('异常', textStatus + ' : ' + errorThrown + '!', 'error');
                },
                complete : function() {

                }
            });
        }
    });
    
    /** 点击委托代扣报盘导出处理 **/
    $("#exportBtn").click(function(){
        $.messager.confirm("提示","最大可导出50000条记录，请确认要导出Excel文件吗？",function(r){
            if(r){
                $.downloadFile({
                    url:$.offerlist.exportUrl,
                    isDownloadBigFile:true,
                    params:$.offerlist.searchForm.serializeObject(),
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
    
    
    
    /**===================开户银行=========================**/
    /** 从服务端获取银行数据,将数据填充到前端下拉框 **/
    /** 开户银行下拉框参数定义 **/
	$('#bankCode').combobox({
		valueField : 'id',
		textField : 'text',
		//panelHeight : 'auto',
		filter : function(q,row) {
			var opts=$(this).combobox("options");
			return row[opts.textField].indexOf(q)>-1;
		},
		formatter : function(row) {
			var opts=$(this).combobox("options");
			return row[opts.textField];
		}
	})
	function initBankInfoData() {
		$.ajaxPackage({
			type : 'post', 
			url : global.contextPath + '/offer/offerBankDic/getBankInfo',
			isShowLoadMask : false,
			dataType : "json",
			success : function (data) { 
				/** data 服务端返回数据 **/
				data.unshift({"id":"0","text":"全部"});
				$('#bankCode').combobox('loadDataExt',data);
				$('#bankCode').combobox('defaultOneItem');
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
	}
	initBankInfoData();
    /**===================开户银行=========================**/
});
