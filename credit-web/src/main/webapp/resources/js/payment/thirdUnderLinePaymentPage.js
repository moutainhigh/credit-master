$(function() {
    $.thirdUnderLinePayment = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/payment/thirdUnderLine/listThirdUnderLinePayment',
        /** 生成报盘文件url **/
        createHaTwoOfferUrl : global.contextPath + '/payment/thirdUnderLine/createHaTwoOffer',
        /** 导出报盘文件url **/
        exportHaTwoOfferUrl : global.contextPath + '/payment/thirdUnderLine/exportHaTwoOffer',
        /** 导入回盘文件url **/
        importHaTwoOfferUrl : global.contextPath + '/payment/thirdUnderLine/importHaTwoOffer',
        /** 附件查看url **/
        attachLoanInfoUrl : global.contextPath + '/payment/thirdUnderLine/AttachLoaninfo',
        /** 国民信托导出url **/
        offerExportOfferUrl : global.contextPath + '/payment/thirdUnderLine/exportHaTwoGuoMinXinTouOffer',
        /** 第三方线下代付信息表格 **/
        thirdUnderLinePaymentDataGrid : $('#thirdUnderLinePaymentDataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [10,20,30,40,50],
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        // 批量导入窗口对象
        importExcelWin : $("#importExcelWin"),
        // 导入文件表单对象
        baseFileForm : $("#baseFileForm"),
        baseFileFormHaTwoOffer : $("#baseFileFormHaTwoOffer"),
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            /** 获取查询表单数据转换成JSON对象 **/
            var searchMsg = $.thirdUnderLinePayment.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 设置查询url **/
            queryParam.url = $.thirdUnderLinePayment.dataGridUrl;
            /** 加载数据信息 **/
            $.thirdUnderLinePayment.thirdUnderLinePaymentDataGrid.datagrid('reloadData',queryParam);
        }
    };
    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.thirdUnderLinePayment.pg = {
        'page' : 1,
        'rows' : $.thirdUnderLinePayment.pageSize
    }
   
    /** DataGrid初始化 **/
    $.thirdUnderLinePayment.thirdUnderLinePaymentDataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.thirdUnderLinePayment.pg,
        /** 提交方式 **/
        method : 'get',
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
        /** 数据长度超出，自动换行 **/
        nowrap : false,
        /** 禁止服务端排序 **/
        remoteSort:false,
        /** 允许多列排序 **/
        //multiSort:true,
        /** 加载提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        /** 列定义 * */
        columns : [ [ {
            field : 'loanId',
            title : '债权Id',
            hidden: true
        }, {
            field : 'name',
            title : '姓名',
            width : '7%'
        }, {
            field : 'sex',
            title : '性别',
            width : '5%'
        }, {
            field : 'loanType',
            title : '申请产品',
            width : '7%'
        }, {
            field : 'idnum',
            title : '身份证号码',
            width : '12%'
        }, {
            field : 'fundsSource',
            title : '合同来源',
            width : '7%'
        }, {
            field : 'pactMoney',
            title : '合同金额',
            width : '8%',
            vType : 'rmb'
        }, {
            field : 'belongBankName',
            title : '所属银行',
            width : '10%'
        }, {
            field : 'bankName',
            title : '开户行',
            width : '12%'
        }, {
            field : 'accountNumber',
            title : '银行卡号',
            width : '12%'
        }, {
            field : 'amount',
            title : '放款额度',
            width : '8%',
            vType : 'rmb'
        }, {
            field : 'operate',
            title : '操作',
            width : '10%',
            formatter:function(value,row,index){
                var elements = "<a href='javascript:void(0)'  class='easyui-linkbutton' style='margin-right: 13px;' onclick='showOfferDetail("+ row.id + ");'>放款流水</a>";
                elements =elements + "<a href='javascript:void(0)' class='easyui-linkbutton' style='margin-right: 10px;' onclick='attachLoanBut("+ row.loanId + ");'>附件</a>" ;
                return elements;
            }
        } ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.thirdUnderLinePayment.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.thirdUnderLinePayment.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 页面自适应 **/
            $.thirdUnderLinePayment.thirdUnderLinePaymentDataGrid.datagrid('resize');
        }
    });
    
    /** 表格分页组件 **/
    $.thirdUnderLinePayment.pager = $.thirdUnderLinePayment.thirdUnderLinePaymentDataGrid.datagrid('getPager');
    $.thirdUnderLinePayment.pager.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.thirdUnderLinePayment.pg.page = pageNumber;
            $.thirdUnderLinePayment.pg.rows = pageSize;
            // 查询处理
            search();
        }
    });
    
    /** 点击查询 **/
    $("#searchBtn").click(function(){
        $.thirdUnderLinePayment.pg.page = 1;
        // 查询处理
        search();
    });
    
    /** 查询处理 **/
    function search(){
        if(searhCheck()){
            $.thirdUnderLinePayment.reloadDataGrid();
        }
    }
    
    /** 生成报盘文件 **/
    $("#createHaTwoOfferBtn").click(function(){
        // 合同来源
        var fundsSource = $("#fundsSource").combobox("getValue");
        if($.isEmpty(fundsSource)){
            $.messager.alert('警告',"生成报盘文件时必须选择一个合同来源！",'warning');
            return;
        }
        var tips = "";
        // 打包批次号
        var batchNum = $("#batchNum").val();
        if($.isEmpty(batchNum)){
            tips = "您没有输入债权批次号，确认要生成所有债权批次号的报盘文件吗？";
        } else {
            tips = "确认生成债权批次号："+ batchNum +"的报盘记录吗？";
        }
        $.messager.confirm("提示",tips,function(r){
            if(r){
                $.ajaxPackage({
                    type: "post",
                    dataType : 'json',
                    data : $.thirdUnderLinePayment.searchForm.serializeObject(),
                    url: $.thirdUnderLinePayment.createHaTwoOfferUrl,
                    success: function (data) {
                        var resCode = data.resCode;
                        var resMsg = data.resMsg;
                        if(resCode!='000000'){
                            $.messager.alert('警告',resMsg,'warning');
                            return;
                        }
                        $.messager.alert('提示','操作成功','info');
                        // 查询处理
                        search();
                    },
                    error: function (msg) {
                        $.messager.alert('异常','生成报盘文件失败！','error');
                    }
                });
            }
        });
   });
    
    /** 导出报盘文件 **/
   $('#exportHaTwoOfferBtn').click(function(){
       // 合同来源
       var fundsSource = $("#fundsSource").combobox("getValue");
       if($.isEmpty(fundsSource)){
           $.messager.alert('警告',"导出报盘文件时必须选择一个合同来源！",'warning');
           return;
       }
       var tips = "";
       // 打包批次号
       var batchNum = $("#batchNum").val();
       if($.isEmpty(batchNum)){
           tips = "您没有输入债权批次号，确认要导出所有债权批次号的未报盘记录吗？";
       } else {
           tips = "确认导出债权批次号："+ batchNum +"的报盘记录吗？";
       }
       $.messager.confirm("提示", tips ,function(r){
           if(r){
               $.downloadFile({
                   url:$.thirdUnderLinePayment.exportHaTwoOfferUrl,
                   isDownloadBigFile:true,
                   params:$.thirdUnderLinePayment.searchForm.serializeObject(),
                   successFunc:function(data){
                       if(data == null){
                           $.messager.alert('提示','导出成功！','info');
                       }else{
                           if(data.resMsg!= null){
                               $.messager.alert('警告',data.resMsg,'warning');
                           }else{
                               $.messager.alert('异常','导出失败！','error');
                           }
                       }
                   },
                   failFunc:function(data){
                       $.messager.alert('异常','导出失败！','error');
                   }
               });
           }
       });
    });
   
   /** 打开回盘文件导入窗口 **/
   $('#importHaTwoOfferBtn').click(function(){
       $.thirdUnderLinePayment.importExcelWin.window('open');
   });
   
   /** 批量导入处理回盘文件 **/
   $("#batchImportHaTwoOffer").click(function(){
       var file = $("#noHaTwoOfferFile").filebox("getValue");
       if($.isEmpty(file)){
           $.messager.alert('警告','请选择导入文件!','warning');
           return;
       }
       $.messager.confirm("提示","确认导入回盘文件吗？",function(r){
           if(r){
               $.thirdUnderLinePayment.baseFileForm.ajaxSubmit({
                   type: "post",
                   dataType : 'json',
                   url: $.thirdUnderLinePayment.importHaTwoOfferUrl,
                   hasDownloadFile : true,
                   success: function () {
                       setTimeout(function(){
                           // 关闭批量导入回盘文件
                           $.thirdUnderLinePayment.importExcelWin.window('close');
                           // 重新刷新页面
                           search();
                       }, 1000);
                   },
                   error: function (data) {
                       $.messager.alert('警告',data.resMsg,'warning');
                   }
               });
           }
       });
   });
   
   /** 查询校验 **/
   function searhCheck(){
       if(!$.thirdUnderLinePayment.searchForm.form("validate")){
           return false;
       }
       return true;
   }
   
   /** 重置处理 **/
   $('#clearBtn').click(function() {
       $.thirdUnderLinePayment.searchForm.form('reset');
   })

    /** 导出国民信托文件 **/
    $('#offerExportOfferBtn').click(function(){
        console.log("导出国民信托文件");
        $.messager.confirm("提示","确认要导出国民信托文件吗？",function(r){
            if(r){
                $.downloadFile({
                    url:$.thirdUnderLinePayment.offerExportOfferUrl,
                    isDownloadBigFile:true,
                    params:$.thirdUnderLinePayment.searchForm.serializeObject(),
                    successFunc:function(data){
                        if(data == null){
                            $.messager.alert('提示','导出成功！','info');
                        }else{
                            if(data.resMsg!= null){
                                $.messager.alert('警告',data.resMsg,'warning');
                            }else{
                                $.messager.alert('异常','导出失败！','error');
                            }
                        }
                    },
                    failFunc:function(data){
                        $.messager.alert('异常','导出失败！','error');
                    }
                });
            }
        });
    });
});

/** 放款流水查询 **/
function showOfferDetail(offerId){
    $.offerDetail.offerDetailWin.window('open');
    $.offerDetail.pg.page = 1;
    $.offerDetail.offerId = offerId;
    $.offerDetail.pager.pagination('refresh',{
        pageNumber:$.offerDetail.pg.page,
        pageSize:$.offerDetail.pg.pageSize
    });
    $.offerDetail.reloadDataGrid();
}

 /** 附件查看 **/
function attachLoanBut(loanId){
    $.ajaxPackage({
       type: "get",
       dataType : 'json',
       data:loanId=loanId,
       url: $.thirdUnderLinePayment.attachLoanInfoUrl+ '/?loanId=' + loanId,
       success: function (data) {
           var resCode = data.resCode;
           var resMsg = data.resMsg;
           var resultUrL=data.resultUrL;
           $('#winAttach').window('open'); 
           $('#attacheLoan').attr("src",resultUrL);
       },
       error: function (msg) {
           $.messager.alert('异常','查看附件失败！','error');
       }
   });
 }