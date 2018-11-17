$(function() {
    // 更新客服url
    var updateUrl = global.contextPath + '/operation/monthSignSetUp';
    
    // 表单对象
    var updateForm = $("#updateForm");
    
    // 设置初始值
    var executeFlag = $("#hideExecuteFlag").val();
    if(executeFlag){
        $("#executeFlag").combobox("setValue",executeFlag);
    }
    
    /** 设置处理 **/
    $("#setingBtn").click(function(){
        updateForm.form('submit', {
            url: updateUrl,
            onSubmit: function(){
            },
            success:function(data){
                data = eval('('+data+')');
                var resCode = data.resCode;
                if (resCode == '000000') {
                    $.messager.alert('提示','设置成功!','info');
                }else if(resCode == '800000'){
                    $.messager.alert('警告','设置失败，未到签单调整设置日期!','warning');
                }else{
                    $.messager.alert('异常','月末/月中签单设置异常!','error');
                }
            }
        });
    });
})
