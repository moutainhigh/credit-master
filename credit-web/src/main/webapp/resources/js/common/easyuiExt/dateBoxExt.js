/**
 * 扩展EasyUI DateBox组件
 */

/**
 * 修改日期控件默认配置
 **/
if ($.fn.datebox){
	//定义日期控件日历板面清除按钮文本
	$.fn.datebox.defaults.clearButtonText = '清除';
	//日期控件日历板面添加清除按钮
	$.fn.datebox.defaults.buttons.splice(1, 0, {
		text:  function(target){return $(target).datebox('options').clearButtonText;},
		handler: function(target){
			//清除文本
			$(target).datebox('clear');
			//隐藏日历板面
			$(this).closest('div.combo-panel').panel('close');
		}
	});
	
	$.extend($.fn.datebox.methods, {
		/** 在日期控件输入框中添加清除图标 **/
	    addClearButton: function(jq, iconCls){
	        return jq.each(function(){
	            var thisjq = $(this);
	            var opts = thisjq.datebox('options');
	            opts.icons = [];
	            opts.icons.unshift({
	                iconCls: iconCls,
	                handler: function(e){
	                    $(e.data.target).datebox('clear').datebox('textbox').focus();
	                    $(this).css('visibility','hidden');
	                }
	            });
	            thisjq.datebox({"onChange":function(date){
	                var tempjq = $(this);
	                var value = $.trim(tempjq.datebox("getValue"));
	                if(value!=""){
	                    tempjq.datebox('getIcon',0).css('visibility','visible');
	                }else{
	                    tempjq.datebox('getIcon',0).css('visibility','hidden');
	                }
	            }});
	            thisjq.datebox('getIcon',0).css('visibility','hidden');
	        });
	    }
	});
	
}
