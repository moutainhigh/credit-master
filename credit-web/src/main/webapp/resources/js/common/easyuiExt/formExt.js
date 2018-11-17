/**
 * @author 00236633
 * 扩展EasyUI form组件
 */
(function($){
	/**
	 * form获取校验失败错误信息
	 */
    $.extend($.fn.form.methods, {
		getErrorMessage: function(jq){
			if ($.fn.validatebox){
				$(jq).find('.validatebox-text:not(:disabled)').validatebox('validate');
	            var invalidbox = $(jq).find('.validatebox-invalid');;
	            var errorMessage = {},errorMessageArray=[];
	            try{
	            invalidbox.each(function(index,valueEle){
	            	var value = $(valueEle).data('validatebox').message;
	            	if(value!=null && value!="" && !errorMessage.hasOwnProperty(value)){
	            		errorMessage[value]=value;
	            		errorMessageArray.push(value);
	            	}
				});
	            }catch(e){
	            	console.debug(e);
	            }
	            invalidbox.filter(':not(:disabled):first').focus();
	            return errorMessageArray.join(",");
	        }
	        return "";
		},
		validate:function validate(jq){
			if ($.fn.validatebox){
	            var t = $(jq);
	            t.find('.validatebox-text:not(:disabled)').validatebox('validate');
	            var invalidbox = t.find('.validatebox-invalid');
	            invalidbox.filter(':not(:disabled):first').focus();
	            //设置去掉前后空格
	            invalidbox.next().each(function(index, element){
	            	var $element = $(element);
	            	$element.val($.trim($element.val()));
	            	
	            });
	            return invalidbox.length == 0;
	        }
	        return true;
		}
    });

})(jQuery);