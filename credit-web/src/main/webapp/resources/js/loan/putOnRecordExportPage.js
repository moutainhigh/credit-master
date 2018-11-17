/**
 * Created by ym10094 on 2016/9/12.
 * 备案查询-导出 初始化
 */
$(function(){
    $.initParams ={
        exportUrl :'',
        searchForm :$('#searchForm'),
        /** 银行卡所属区域下拉框切换处理 **/
        execute: function (newValue, oldValue,ishow){
        if ($.initParams.searchForm.form('validate')) {
            var url = global.contextPath;
            if (newValue == "01") {
                // 深圳地区
                url += "/loan/put/querySZputOnRecord" ;
                $.initParams.exportUrl = global.contextPath + "/loan/put/exportSZputOnRecord";
            } else if (newValue == "99") {
                // 异地（非深圳地区）
                url += "/loan/put/queryNotSZputOnRecord" ;
                $.initParams.exportUrl = global.contextPath + "/loan/put/exportNotSZputOnRecord";
            }
            if(ishow){
                $("#putOnRecorDefault").attr("src", url);
            }
        }
        else{
            $.messager.alert("提示","请选择正确的输入条件！","info");
        }
    }
    };

    $('#regionType').combobox({
            onChange:function(newValue, oldValue){
                $.initParams.execute(newValue, oldValue,true);
            }
    }
    );

    /** 查询处理 **/
    $('#searchButs').click(function() {
        var newValue = $('#regionType').combobox('getValue');
        $.initParams.execute(newValue, null,true);
    });



    /** 重置 **/
    $("#clearCondition").bind("click", function(envent) {
        if (!$(this).linkbutton("options").disabled) {
            $("#searchForm").form("reset");;
        }
    });

    /**导出**/
    $("#eloanExportBtn").click( function () {
            var newValue = $('#regionType').combobox('getValue');
            $.initParams.execute(newValue, null,false);
            exportSubmit($.initParams.exportUrl)
        }
    );
    /** 导出文件 **/
    function exportSubmit(url){
        console.log("url："+url);
        var params = {};
       var searchMsg = $("#searchForm").serialize();
        params = $.serializeToJsonObject(searchMsg);
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
})