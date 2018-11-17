/**
 * 扩展EasyUI ValidateBox组件
 */
$.extend($.fn.validatebox.methods, {    
    remove: function(jq, newposition){
        return jq.each(function(){    
        	$(this).data("textbox").textbox.find(".textbox-text").removeClass('validatebox-text validatebox-invalid')
        });    
    }
	/*
    reduce: function(jq, newposition){    
        return jq.each(function(){    
           var opt = $(this).data().validatebox.options;  
           $(this).addClass("validatebox-text").validatebox(opt);  
        });    
    }*/   
}); 

$(function(){
	/** 扩展easyui表单的验证规则 **/
	$.extend($.fn.validatebox.defaults.rules, {
		CHS : {
			/** 验证汉字 **/
			validator : function (value) {
				return /^[\u0391-\uFFE5]+$/.test(value);
			},
			message : '只能输入汉字!'
		},
		mobile : {
			/** 移动手机号码验证 **/
			validator : function (value) {
				/** value值为文本框中的值 **/
				if ($.trim) {
					value = $.trim(value);
				}
				var reg = /^1\d{10}$/;
				return reg.test(value);
			},
			message : '输入手机号码格式不正确!'
		},
		idCard : {
			/** 验证身份证 **/
			validator : function(value) {
				if ($.trim) {
					value = $.trim(value);
				}
            	return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value) || /^\d{18}(\d{2}[A-Za-z0-9])?$/i.test(value);
            },
            message : '身份证号码格式不正确!'
		},
		date : {
			/** 验证日期格式 **/
			validator : function(value) {
				return $.DateUtil.isDateFormat(value);
            },
            message : '格式不正确（YYYY-MM-DD）如2010-01-01!'
		},
		email : {
			/** 邮箱地址验证 **/
			validator : function (value) {
				/** value值为文本框中的值 **/
				if ($.trim) {
					value = $.trim(value);
				}
				var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/;
				return reg.test(value);
			},
			message : '输入邮箱格式不正确!'
		},
		tel : {
			/** 固定电话验证 **/
			validator : function (value) {
				/** value值为文本框中的值 **/
				if ($.trim) {
					value = $.trim(value);
				}
				var reg = /^(\d{3,4}\-)?\d{7,8}(\-\d{2,6})?$/;
				var reg1 = /^(\d{10})$/;
				return reg.test(value) || reg1.test(value);
			},
			message : '输入格式不正确（XXXX-XXXXXXX）!'
		},
		postCode : {
			/** 国内邮编验证 **/
			validator : function (value) {
				if ($.trim) {
					value = $.trim(value);
				}
				var reg = /^[1-9]\d{5}$/;
				return reg.test(value);
			},
			message : '邮编必须是非0开始的6位数字!'
		},
		account : {
			/** 用户账号验证(只能包括 _ 数字 字母) **/
			validator : function (value, param) {
				if (value.length < param[0] || value.length > param[1]) {
					$.fn.validatebox.defaults.rules.account.message = '用户名长度必须在' + param[0] + '至' + param[1] + '范围';
					return false;
				} else {
					if (!/^[\w]+$/.test(value)) {
						$.fn.validatebox.defaults.rules.account.message = '用户名只能数字、字母、下划线组成.';
						return false;
					} else {
						return true;
					}
				}
			},
			message : ''
		},
	    comboboxValidator: {
	    	//下拉选择必须是有效值
	        validator: function(value,param){
	        	try{
	        		dataCache = $(param[0]).combobox('options').dataCache;
	        		if ($.isArray(dataCache)) {
	        			return $.inArray($.trim(value),dataCache) >= 0 ? true : false;
	        		} else {
	        			var comboboxData = $(param[0]).data('combobox');
		        		if(comboboxData.validatorData==null){
		        			comboboxData.validatorData = {};
		        		}
		        		if($.isEmptyObject(comboboxData.validatorData)){
			        		$.each(comboboxData.data,function(index,obj){
		        				comboboxData.validatorData[$.trim(obj.text)]=obj.value;
		        			})
		        		}
			            return comboboxData.validatorData[$.trim(value)]!=null;
	        		}
	        	}catch(e){
	        		console.log(e);
	        	}
	        	return true; 
	        },    
	        message: '必须选择有效的{1}'   
	    },
	    maxLength: {    
	    	//最大长度验证
	        validator: function(value,param){
	        	var result = true;
	        	try{
	        		value=value||"";
	        		if(value.length > param[0]){
	        			result = false
	        		}
	        	}catch(e){
	        		console.debug(e);
	        	}
	            return result;    
	        },    
	        message: "{1}长度最多为{0}个字符!"   
	    },
	    minLength: { 
	    	//最小长度验证
	        validator: function(value, param){
	        	var result = true;
	        	try{
	        		value=value||"";
	        		if(value.length < param[0]){
	        			result = false
	        		}
	        	}catch(e){
	        		console.debug(e);
	        	}
	            return result;    
	        },    
	        message: '{1}长度至少为{0}'   
	    },
	    numer:{
	    	//数字验证
	        validator: function(value, param){
	        	try{
	        		value=value||"";
		            return  /^(-?\d+)(\.\d+)?$/.test(value);
	        	}catch(e){
	        		console.debug(e);
	        	}
	            return true;  
	        },    
	        message: '{0}必须是数字'
	    },
	    numberRangeValid:{
	    	//数字范围验证
	        validator: function(value, param){
	        	value = $.unComdify(value);
	        	if ($.isNumeric(value) && $.isNumeric(param[0]) && $.isNumeric(param[1])) {
	        		value = parseFloat(value);
	        		var r1 = parseFloat($.unComdify(param[0]));
	        		var r2 = parseFloat($.unComdify(param[1]));
	        		if (r1 <= value && r2 >= value) {
	        			return true;
	        		} else {
	        			return false;
	        		}
	        	} else {
	        		return false;
	        	}
	        },    
	        message: '数值范围必须在{0}-{1}之间'
	    },
	    integerNumer:{
	    	//整数验证 包括0
	        validator: function(value, param){
	        	try{
	        		value=value||"";
		            return  /^-?\d+$/.test(value);
	        	}catch(e){
	        		console.debug(e);
	        	}
	            return true;  
	        },    
	        message: '{0}必须是整数'
	    },
	    positiveIntegerNumer:{
	    	//正整数验证
	        validator: function(value, param){
	        	try{
	        		value=value||"";
	        		return  /^\+?[0-9][0-9]*$/.test(value);
	        	}catch(e){
	        		console.debug(e);
	        	}
	            return true;  
	        },    
	        message: '{0}必须是正整数'
	    },
	    negativeIntegerNumer:{
	    	//负整数验证
	        validator: function(value, param){
	        	try{
	        		value=value||"";
	        		return  /^\-[1-9][0-9]*$/.test(value);
	        	}catch(e){
	        		console.debug(e);
	        	}
	            return true;  
	        },    
	        message: '{0}必须是负整数'
	    },
	    floatNumer:{
	    	//浮点数验证
	        validator: function(value, param){
	        	try{
	        		value=value||"";
	        		return  /^(-?\d+)\.\d+$/.test(value);
	        	}catch(e){
	        		console.debug(e);
	        	}
	            return true;  
	        },    
	        message: '{0}必须是小数'
	    },
	    positiveFloatNumer:{
	    	//正浮点数验证
	        validator: function(value, param){
	        	try{
	        		value=value||"";
	        		return  /^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$/.test(value);
	        	}catch(e){
	        		console.debug(e);
	        	}
	            return true;  
	        },    
	        message: '{0}必须是正小数'
	    },	    
	    equals: {    
	        validator: function(value,param){
	        	var result = true;
	        	try{
	        		value=value||"";
		            if( value.length != param[0]){
		            	result = false;
		            }
	        	}catch(e){
	        		console.debug(e);
	        	}
	            return result;
	        },    
	        message: '{1}长度必须为{0}个字符!'   
	    },
	    dateRangeValid : {
	    	field : {'<=':'小于等于','<':'小于','>=':'大于等于','>':'大于'},
	    	validator : function(value,param) { //参数value为当前文本框的值，也就是endDate
	    		var start = 0;
	    		if ($.DateUtil.isDateFormat(value)) {
	    			var start = $.DateUtil.parseDate(value).getTime();
	    		} else {
	    			$.fn.validatebox.defaults.rules.dateRangeValid.message = $.fn.validatebox.defaults.rules.date.message;
	    			return false;
	    		}
	    		var end = undefined;
//	    		if (param.length == 2) {
	    			var expression = param[0];
	    			var s = $.fn.validatebox.defaults.rules.dateRangeValid.field[expression];
	    			$.fn.validatebox.defaults.rules.dateRangeValid.message = '必须' + s + param[2];
	    			var v = param[1];
	    			if (v == 'sysdate') {
	    				end = new Date().getTime();
	    			} else if (v.indexOf('#') == 0) {
	    				var tmp = undefined;
	    				if ($(v).prop('class').indexOf('easyui-') == 0) {
	    					tmp = $(v).datetimebox('getValue');
	    				} else {
	    					tmp = $(v).val();
	    				}
	    				if ($.isEmpty(tmp)) {
//	    					end = new Date().getTime();
	    					return true;
	    				} else {
//	    					end = new Date(tmp).getTime();
	    					end = $.DateUtil.parseDate(tmp).getTime();
	    				}
	    			}
	    			return eval(start + expression + end);
//	    		} else {
//	    			return false;
//	    		}
	    		return false;
	    	},  
	    	message : '结束时间要大于开始时间!'  
	    },
	    /**验证提前结清一次性还款金额**/
	    allCleanApplyAmt:{
	    	validator : function(value,param) {
	    		var onetimeRepaymentAmount=$("#onetimeRepaymentAmountCur").val();//一次性还款金额
	    		var accAmount=$("#accAmount").val();//挂账金额
	    		var overdueAllAmount=$("#overdueAllAmount").val();//逾期本息和
	    		var fine=$("#fine").val();//罚息
	    		onetimeRepaymentAmount=parseFloat(onetimeRepaymentAmount);
	    		accAmount=parseFloat(accAmount);
	    		overdueAllAmount=parseFloat(overdueAllAmount);
	    		fine=parseFloat(fine);
	    		if(value.replace(/,/g,'')==(onetimeRepaymentAmount+overdueAllAmount+fine-accAmount).toFixed(2)){//一次性还款金额+逾期本息和+罚息-挂账金额
	    			return true;
	    		}else{
	    			return false;
	    		}
	    	},
	    	message : '提前结清金额有误，请重新输入金额!'
	    }
	})
	
	/** 表单验证文字提示 **/
//	if ($.fn.validatebox){
//		$.fn.validatebox.defaults.missingMessage = '该输入项为必输项';
//		$.fn.validatebox.defaults.rules.email.message = '请输入有效的电子邮件地址';
//		$.fn.validatebox.defaults.rules.url.message = '请输入有效的URL地址';
//		$.fn.validatebox.defaults.rules.length.message = '输入内容长度必须介于{0}和{1}之间';
//		$.fn.validatebox.defaults.rules.remote.message = '请修正该字段';
//	}
})