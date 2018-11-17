$(function(){
	
	/** 获取身份证信息（性别，生日） **/
	function getInfoByIdCard(val) {
		/** 生日 **/
	    var birthday;
	    /** 性别 **/
	    var sex;
	    if (15 == val.length) { //15位身份证号码
	    	birthday = val.charAt(6) + val.charAt(7);
	        if (parseInt(birthday) < 10) {
	        	birthday = '20' + birthday;
	        } else {
	        	birthday = '19' + birthday;
	        }
	        birthday = birthday + '-' + val.charAt(8) + val.charAt(9) + '-' + val.charAt(10) + val.charAt(11);
	        if (parseInt(val.charAt(14) / 2) * 2 != val.charAt(14)) {
	        	sex = '男';
	        } else {
	        	sex = '女';
	        }
	    }
	    if (18 == val.length) { //18位身份证号码
	    	birthday = val.charAt(6) + val.charAt(7) + val.charAt(8) + val.charAt(9) + '-' + val.charAt(10) + val.charAt(11) + '-' + val.charAt(12) + val.charAt(13);
	        if (parseInt(val.charAt(16) / 2) * 2 != val.charAt(16)) {
	        	sex = '男';
	        } else {
	        	sex = '女';
	        }
	    }
	    var result = {};
	    result.sex = sex;
	    result.birthday = birthday;
	    return result;
	}
	
	var saveOrUpdateUrl = global.contextPath + '/person/saveOrUpdate';
	var enumDataUrl = global.contextPath + '/system/enum/get/sex,married,edLevel,addressPriority,houseType,carType,cType,officialRank,payType,profession,enterpriseType,premisesType,industryType';
	var loadPersonDataUrl = global.contextPath + '/person/loadPersonData' + '/' + $('#personId').val();
	
	/** 下拉框空数据项 **/
	var emptyItem = {'id':'','text':'(空)'};
	var personDetailForm = $('#personDetailForm');
	
	/** 默认展开全部面板 **/
	$('#accordion').accordion('select','房产信息');
	$('#accordion').accordion('select','车辆信息');
	$('#accordion').accordion('select','职业信息');
	$('#accordion').accordion('select','企业信息');
	
	$('#submitBut').click(function(){
		if (personDetailForm.form('validate')) {
			var sex = $('#sex').combobox('getValue');
			var idCard = $('#idnum').val();
			if ($.isEmpty(idCard)) {
				$('#idnum').focus();
				$.messager.alert('提示信息','缺少身份证号信息!','warning');
				return;
			}
			var finalSex = getInfoByIdCard(idCard).sex;
			if (sex != finalSex) {
				$('#sex').focus();
				$.messager.alert('提示信息','性别请选择' + finalSex + '!','warning');
				return;
			}
			$.ajaxPackage({
				type : 'post', 
				url : saveOrUpdateUrl,
				data : personDetailForm.serialize(),
				dataType : "json",
				success : function (data) { 
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					if (resCode == '000000') {
						//操作成功 重新加载列表数据
						$.messager.alert('结果','操作成功');
						window.setTimeout(function(){
							/** 跳转到详细页 **/
							window.location.href = loadPersonDataUrl;
						}, 1000);
					} else {
						//操作失败
						$.messager.alert('结果',resMsg,'error');
					}
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					
				}
			});
		}
})
	
	/** 从服务端获取枚举值，将数据填充到前端下拉框 **/
	function initComBoxData() {
		$.ajaxPackage({
			type : 'post', 
			url : enumDataUrl,
			dataType : "json",
			success : function (enumData) { 
				/** enumData 服务端返回枚举数据 **/
				
				/** 性别枚举值 **/
				var sex = enumData.sex;
				/** 婚姻状况枚举值 **/
				var married = enumData.married;
				/** 学历枚举值 **/
				var edLevel = enumData.edLevel;
				/** 优先联系地址枚举值 **/
				var addressPriority = enumData.addressPriority;
				addressPriority.unshift(emptyItem);
				/** 房产类型枚举值 **/
				var houseType = enumData.houseType;
				houseType.unshift(emptyItem);
				/** 购车类型枚举值 **/
				var carType = enumData.carType;
				carType.unshift(emptyItem);
				/** 单位性质枚举值 **/
				var cType = enumData.cType;
				/** 职位级别枚举值 **/
				var officialRank = enumData.officialRank;
				/** 发薪方式枚举值 **/
				var payType = enumData.payType;
				/** 职业类型枚举值 **/
				var profession = enumData.profession;
				/** 私营企业类型枚举值 **/
				var enterpriseType = enumData.enterpriseType;
				enterpriseType.unshift(emptyItem);
				/** 经营场所枚举值 **/
				var premisesType = enumData.premisesType;
				/** 所属行业枚举值 **/
				var industryType = enumData.industryType;
				premisesType.unshift(emptyItem);
			 	
				if (!sex || !married || !edLevel || !addressPriority || !houseType || !carType || 
						!cType || !officialRank || !payType || !profession || !enterpriseType || !premisesType) {
					$.messager.alert('异常信息','加载枚举数据异常!','error');
				} else {
					$('#sex').combobox('loadData',sex);
					$('#married').combobox('loadData',married);
					$('#edLevel').combobox('loadData',edLevel);
					$('#addressPriority').combobox('loadData',addressPriority);
					$('#houseType').combobox('loadData',houseType);
					$('#personCarInfo_carType').combobox('loadData',carType);
					$('#cType').combobox('loadData',cType);
					$('#officialRank').combobox('loadData',officialRank);
					$('#payType').combobox('loadData',payType);
					$('#profession').combobox('loadData',profession);
					$('#personEntrepreneurInfo_enterpriseType').combobox('loadData',enterpriseType);
					$('#personEntrepreneurInfo_premisesType').combobox('loadData',premisesType);
					$('#industryType').combobox('loadData',industryType);
				}
				
	//			data.married.push({'id':'','text':'(空)'});
	//			
	//			$('#married').combobox('loadData',data.married);
				
				
				
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
	}
	
	initComBoxData();
	
})