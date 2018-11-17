/**
 * Created by ym10098 on 2016/11/29.
 */
$(function() {
    $.exportActualSubAccountDetailExcel = {
        /** 表格数据源地址 **/
        exportExcelUrl: global.contextPath+"/offer/trustOffer/exportActualSubAccountDetailExcel",
        /** 数据表格对象 **/
        dataGrid : $('#dataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 查询条件数据项表单实例 **/
        searchForm : $('#searchForm'),
        /** 查询校验 **/
        validate : function(){
            if (!$.exportActualSubAccountDetailExcel.searchForm.form('validate')) {
                return false;
            }
            return true;
        }
    }



    /** 重置处理 **/
    $('#clearBtn').click(function() {
        $.exportActualSubAccountDetailExcel.searchForm.form('reset');
    });
    
    /** 导出分账明细报表 **/
    $("#exportBtn").click( function () {
        if (!$.exportActualSubAccountDetailExcel.validate()) {
            return;
        }
        $.messager.confirm("提示", "确认导出实分账明细表吗？", function (r) {
            var params = {};
            var searchMsg = $.exportActualSubAccountDetailExcel.searchForm.serialize();
            searchMsg = decodeURIComponent(searchMsg);
            params = $.serializeToJsonObject(searchMsg);
            if (r) {
            	ajaxSubmit($.exportActualSubAccountDetailExcel.exportExcelUrl,params);
            }
        });
    });
    /** 提交异步请求 **/
    function ajaxSubmit(url, params, isRefresh){
        $.downloadFile({
            url:url,
            isDownloadBigFile:true,
            params:params,
            successFunc:function(data){
                if(data == null){
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
    
    /** 网格数据对象初始化 **/
    $.exportActualSubAccountDetailExcel.dataGrid.datagrid({        
        /** 工具栏 **/
        toolbar : '#tb'
    });
})
