/***
 * @describe:第三方线下放款页面js
 * 
 */

$(function() {
    $.thirdOffLinePayment = {
        /** 表格数据源地址 **/
        dataGridUrl : global.contextPath + '/loan/thirdOffLineLoan/listThirdOffLineLoan',
        /** 导入第三方线下放款文件url **/
        importThirdOffLineLoanUrl : global.contextPath + '/loan/thirdOffLineLoan/importLoanExcel',
        /** 附件查看url **/
        attachLoanInfoUrl : global.contextPath + '/payment/thirdUnderLine/AttachLoaninfo',
        /** 第三方线下代付信息表格 **/
        thirdOffLineLoanDataGrid : $('#thirdOffLineLoanDataGrid'),
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
            var searchMsg = $.thirdOffLinePayment.searchForm.serialize();
            /** 对参数进行解码(显示中文) **/
            searchMsg = decodeURIComponent(searchMsg);
            /** 字符串转换为对象 **/
            var queryParam = $.serializeToJsonObject(searchMsg);
            /** 设置查询url **/
            queryParam.url = $.thirdOffLinePayment.dataGridUrl;
            /** 加载数据信息 **/
            $.thirdOffLinePayment.thirdOffLineLoanDataGrid.datagrid('reloadData',queryParam);
        }
    };
    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.thirdOffLinePayment.pg = {
        'page' : 1,
        'rows' : $.thirdOffLinePayment.pageSize
    }
   
    /** DataGrid初始化 **/
    $.thirdOffLinePayment.thirdOffLineLoanDataGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.thirdOffLinePayment.pg,
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
            width : '8%'
        }, {
            field : 'idnum',
            title : '身份证号码',
            width : '12%'
        }, {
            field : 'fundsSource',
            title : '合同来源',
            width : '8%'
        }, {
            field : 'pactMoney',
            title : '合同金额',
            width : '8%',
            vType : 'rmb'
        }, {
            field : 'bankName',
            title : '所属银行',
            width : '12%'
        }, {
            field : 'belongBankName',
            title : '开户行',
            width : '16%'
        }, {
            field : 'accountNumber',
            title : '银行卡号',
            width : '13%'
        }, {
            field : 'operate',
            title : '操作',
            width : '10%',
            formatter:function(value,row,index){
                var elements = "<a href='javascript:void(0)' class='easyui-linkbutton' style='margin-right: 10px;' onclick='attachLoanBut("+ row.loanId + ");'>附件</a>" ;
                return elements;
            }
        } ] ],
        /** 每页显示的记录条数，默认为10 * */
        pageSize : $.thirdOffLinePayment.pageSize,
        /** 可以设置每页记录条数的列表 * */
        pageList : $.thirdOffLinePayment.pageSizeList,
        /** 工具栏 **/
        toolbar : '#tb',
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            /** 页面自适应 **/
            $.thirdOffLinePayment.thirdOffLineLoanDataGrid.datagrid('resize');
        }
    });
    
    /** 表格分页组件 **/
    $.thirdOffLinePayment.pager = $.thirdOffLinePayment.thirdOffLineLoanDataGrid.datagrid('getPager');
    $.thirdOffLinePayment.pager.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $.thirdOffLinePayment.pg.page = pageNumber;
            $.thirdOffLinePayment.pg.rows = pageSize;
            // 查询处理
            search();
        }
    });
    
    /** 点击查询 **/
    $("#searchBtn").click(function(){
        $.thirdOffLinePayment.pg.page = 1;
        // 查询处理
        search();
    });
    
    /** 查询处理 **/
    function search(){
        if(searhCheck()){
            $.thirdOffLinePayment.reloadDataGrid();
        }
    }
    
   /** 打开导入已放款文件窗口 **/
   $('#importAlreadyLoanBtn').click(function(){
       $.thirdOffLinePayment.importExcelWin.window('open');
   });
   
   /** 批量导入已放款文件 **/
   $("#batchImportAlreadyLoan").click(function(){
       var file = $("#alreadyLoanFile").filebox("getValue");
       if($.isEmpty(file)){
           $.messager.alert('警告','请选择导入文件!','warning');
           return;
       }
       $.messager.confirm("提示","确认导入已放款文件吗？",function(r){
           if(r){
               $.thirdOffLinePayment.baseFileForm.ajaxSubmit({
                   type: "post",
                   dataType : 'json',
                   url: $.thirdOffLinePayment.importThirdOffLineLoanUrl,
                   hasDownloadFile : true,
                   success: function () {
                       setTimeout(function(){
                           // 关闭批量导入回盘文件
                           $.thirdOffLinePayment.importExcelWin.window('close');
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
       if(!$.thirdOffLinePayment.searchForm.form("validate")){
           return false;
       }
       return true;
   }
   
   /** 重置处理 **/
   $('#clearBtn').click(function() {
       $.thirdOffLinePayment.searchForm.form('reset');
   })
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
       url: $.thirdOffLinePayment.attachLoanInfoUrl+ '/?loanId=' + loanId,
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