$(function(){ 
	
	/** 表单所有录入元素 **/
	var allFiled = ['departmentTypeTr','','depLevelTr','serviceTelTr','siteTr','zoneCodeTr','memoTr','cityNumTr'
	                 ,'provinceTr','cityTr','zoneTr','isValidTr','openDateTr','closeDateTr','lngTr','latTr'];
	/** 营业网点层级 **/
	var orgLevel = ['V100','V101','V102','V103','V104','V105'];
	
	function getLevelName(level){
		var msg = '';
		if (level == 'V100') {
			msg = '公司';
		} else if (level == 'V101') {
			msg = '区域';
		} else if (level == 'V102') {
			msg = '分部';
		} else if (level == 'V103') {
			msg = '城市';
		} else if (level == 'V104') {
			msg = '营业部';
		} else if (level == 'V105') {
			msg = '团队/组';
		}
		return msg;
	}
	
	/** 各级别对应的录入元素 **/
	var vLevelFiled = {};
	vLevelFiled['V100'] = ['','','','serviceTelTr','siteTr','zoneCodeTr','memoTr','cityNumTr'];
	vLevelFiled['V101'] = ['','','','serviceTelTr','siteTr','zoneCodeTr','memoTr','cityNumTr'];
	vLevelFiled['V102'] = ['','','','serviceTelTr','siteTr','zoneCodeTr','memoTr','cityNumTr'];
	vLevelFiled['V103'] = ['','','','serviceTelTr','siteTr','zoneCodeTr','memoTr','cityNumTr'];
	vLevelFiled['V104'] = ['departmentTypeTr','','depLevelTr','serviceTelTr','siteTr','zoneCodeTr','memoTr','cityNumTr'
	                       ,'provinceTr','cityTr','zoneTr','isValidTr','openDateTr','closeDateTr','lngTr','latTr'];
	vLevelFiled['V105'] = ['','','','','','','memoTr'];
	
	
	/** 查询营业网点子级结点 请求地址 **/
	var loadChildNodeUrl = global.contextPath + '/system/comOrganization/loadChildNode';
	/** 查询某营业网点信息 请求地址 **/
	var loadOrgDataUrl = global.contextPath + '/system/comOrganization/loadOrgData';
	/** 新增、修改营业网点信息 请求地址 **/
	var saveOrUpdateDataUrl = global.contextPath + '/system/comOrganization/saveOrUpdateData';
	/** 删除营业网点信息 请求地址 **/
	var deleteDataUrl = global.contextPath + '/system/comOrganization/deleteData';
	
	/** 树 **/
	var easyUITree = $('#easyUITree');
	/** 表单 **/
	var dataForm = $('#dataForm');
	/** 树菜单 **/
	var treeMenu = $('#treeMenu');
	/** 保存右键选中的结点 **/
	var selectedNode = undefined;
	/** 操作提示 **/
	var tips = $('#tips');
	
	easyUITree.tree({
		/** 单击树结点事件 **/
		onClick : function(node) {
			var parentId = node.id;
			if ($.isEmpty(parentId)) {
				return ;
			}
			/**加载子结点数据，追加到树结构中**/
			initTree(node, parentId);
			
			/**加载结点数据，填充到右侧表单中，提供修改和删除功能**/
			selectedAndLoadData(parentId);
		},
		/** 右击事件 **/
		onContextMenu:function(e,node) {
			selectedNode = node;
			e.preventDefault();
			/** 选中结点 **/
			easyUITree.tree('select',node.target);
			/** 弹出菜单项 **/
			
			var curVLevel = selectedNode.attributes.vLevel;
			var levelIndex = orgLevel.indexOf(curVLevel);
			if (levelIndex == -1) {
				$.messager.alert('提示信息','层级信息读取有误!','warning');
					return;
				}
			levelIndex++;
			/** 此次添加的层级ID **/
			var addOrgLevel = orgLevel[levelIndex];
			if ($.isEmpty(addOrgLevel)) {
				/** 无法添加子结点 **/
				$('#treeMenu div[name=append]').hide();
				$('#treeMenu .menu-sep').hide();
			} else {
				$('#treeMenu div[name=append]').show();
				$('#treeMenu .menu-sep').show();
				$('#treeMenu div[name=append] .menu-text').html('新增' + getLevelName(addOrgLevel));
			}
			$('#treeMenu div[name=remove] .menu-text').html('删除' + getLevelName(curVLevel));
			
			treeMenu.menu('show',{
				left : e.pageX,
				top : e.pageY
			});
		}
	})
	
	/** 添加子结点事件（添加营业网点数据） **/
	$(treeMenu).find('div[name=append]').click(function(){
		if ($.isEmpty(selectedNode)) {
			$.messager.alert('提示信息','缺少选中结点信息!','warning');
		} else {
			var curVLevel = selectedNode.attributes.vLevel;
			var levelIndex = orgLevel.indexOf(curVLevel);
			if (levelIndex == -1) {
				$.messager.alert('提示信息','层级信息读取有误!','warning');
				return;
			}
			levelIndex++;
			/** 此次添加的层级ID **/
			var addOrgLevel = orgLevel[levelIndex];
			if ($.isEmpty(addOrgLevel)) {
				$.messager.alert('提示信息','超出层级范围，无法添加!','warning');
				return;
			}
			
			clearForm();
			var formData = {};
			formData.parentId = selectedNode.id;
			dataForm.form('load',formData);
			$('#parentIdTr').hide();
			
			if(curVLevel == 'V103'){
				//初始化是否有效字段 默认是=1 默认选择第一项
				$('#isValid').combobox('defaultOneItem');
			}
			
			var path = getParentPath(selectedNode)
			path += '/*';
			tips.html('<p>当前操作：新增网点(<span style="color:red;">' + path + '</span>)</p>');
			$('#depLevel').combobox('select','O');
			$('#departmentType').combobox('select','个贷');
			
			showAndHideElement(allFiled,false);
			showAndHideElement(vLevelFiled[addOrgLevel],true);
			$('#vLevel').val(addOrgLevel);
		}
	})
	
	/** 删除结点事件（删除营业网点数据） **/
	$(treeMenu).find('div[name=remove]').click(function(){
		if ($.isEmpty(selectedNode)) {
			$.messager.alert('提示信息','缺少选中结点信息!','warning');
		} else {
			var path = getParentPath(selectedNode)
			$.messager.confirm('请确认', path + '<br/>确认删除?', function(r){
				if (r) {
					var nodeId = selectedNode.id;
					$.ajaxPackage({
						type : 'get', 
						url : deleteDataUrl + '/' + nodeId,
						dataType : "json",
						success : function (data,textStatus,jqXHR) {
							var resCode = data.resCode;
							var resMsg = data.resMsg;
							var attachment = data.attachment;
							if (resCode == '000000') {
								/**操作成功**/
								var operNode = {};
								operNode.id = attachment.id;
								operNode.action = 'delete';
								reloadNode(operNode);
								$.messager.alert('提示信息','操作成功');
							} else {
								/**操作失败**/
								$.messager.alert('提示信息',resMsg,'error');
							}
						},
						error : function (XMLHttpRequest, textStatus, errorThrown,d) {
							$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
						},
						complete : function() {
							
						}
					});
				}
			});
		}
	})
	
	
	/** 初始化树结点 **/
	function initTree(parentNode,parentId,callBackFun) {
		if (parentNode) {
			if (parentNode.attributes.isLoad == '1') {
				return ;
			}
		}
		$.ajaxPackage({
			type : 'get', 
			url : loadChildNodeUrl + '/' + parentId,
			dataType : "json",
			success : function (data,textStatus,jqXHR) {
				var resCode = data.resCode;
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					/**操作成功**/
					easyUITree.tree('append', {
						parent : ($.isEmpty(parentNode) == true?null:parentNode.target),
						data : attachment
					});
					if (parentNode) {
						parentNode.attributes.isLoad = '1';
					}
					if ($.isFunction(callBackFun)) {
						callBackFun();
					}
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
	}
	
	/** 加载某营业网点信息 **/
	function loadOrgData(id) {
		$.ajaxPackage({
			type : 'get', 
			url : loadOrgDataUrl + '/' + id,
			dataType : "json",
			success : function (data,textStatus,jqXHR) {
				var resCode = data.resCode;
				var resMsg = data.resMsg;
				var comOrganization = data.attachment.comOrganization;
				var areaList = data.attachment.areaList;
				if (resCode == '000000') {
					/**操作成功**/
					clearForm();
					$('#parentIds').combobox('loadData',[]);
					if (areaList) {
						$('#parentIds').combobox('loadData',areaList);
						$('#parentIds').combobox('setValue',comOrganization.parentId);
						$('#parentIdTr').show();
					} else {
						$('#parentIdTr').hide();
					}
					if ($.isEmpty(comOrganization.depLevel)) {
						$('#depLevel').combobox('select',' ');
					}
					
					if(!$.isEmpty(comOrganization.vLevel) && 'V104' == comOrganization.vLevel){
						comOrganization.openDate = $.DateUtil.dateFormatToStr(comOrganization.openDate);
						comOrganization.closeDate = $.DateUtil.dateFormatToStr(comOrganization.closeDate);
					}
					
					showAndHideElement(allFiled,false);
					dataForm.form('load',comOrganization);
					var vLevel = comOrganization.vLevel;
					showAndHideElement(vLevelFiled[vLevel],true);
				} else {
					/**操作失败**/
					$.messager.alert('提示信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
	}
	
	/** 加载某营业网点信息 **/
	function saveOrUpdateData() {
		var vLevel = $('#vLevel').val();
		if (vLevel == 'V105') {
			$('#cityNum').validatebox('remove');
		}
		if (dataForm.form('validate')) {
			if (vLevel == 'V104') {
				/** 为营业部时 部门类型 网点评级 服务电话 签署区域名称 省 市 区（县） 为必填项 **/
				var departmentType = dataForm.find('input[name=departmentType]').val();
				var depLevel = dataForm.find('input[name=depLevel]').val();
				var serviceTel = dataForm.find('input[name=serviceTel]').val();
				var site = dataForm.find('input[name=site]').val();
				var province = dataForm.find('input[name=province]').val();
				var city = dataForm.find('input[name=city]').val();
				var zone = dataForm.find('input[name=zone]').val();
				if ($.isEmpty(departmentType) || departmentType == '0') {
					$.messager.alert('提示信息','部门类型为必选项!','warning');
					return;
				}
				if ($.isEmpty(depLevel) || depLevel == '0') {
					$.messager.alert('提示信息','网点评级为必选项!','warning');
					return;
				}
				if ($.isEmpty(serviceTel)) {
					$.messager.alert('提示信息','服务电话为必填项!','warning');
					return;
				}
				if ($.isEmpty(site)) {
					$.messager.alert('提示信息','签署区域名称为必填项!','warning');
					return;
				}
				if ($.isEmpty(province)) {
					$.messager.alert('提示信息','省为必填项!','warning');
					return;
				}
				if ($.isEmpty(city)) {
					$.messager.alert('提示信息','市为必填项!','warning');
					return;
				}
				if ($.isEmpty(zone)) {
					$.messager.alert('提示信息','区（县）为必填项!','warning');
					return;
				}
			}
			
			$.ajaxPackage({
				type : 'post', 
				url : saveOrUpdateDataUrl,
				data : dataForm.serialize(),
				dataType : "json",
				success : function (data,textStatus,jqXHR) {
					var resCode = data.resCode;
					var resMsg = data.resMsg;
					var attachment = data.attachment;
					if (resCode == '000000') {
						/** 操作成功 **/
						/**  刷新树结构 **/
						var node = reloadNode(attachment);
						$.messager.alert('提示信息','操作成功');
					} else {
						/**操作失败**/
						$.messager.alert('提示信息',resMsg,'error');
					}
				},
				error : function (XMLHttpRequest, textStatus, errorThrown,d) {
					$.messager.alert('提示信息',textStatus + '  :  ' + errorThrown + '!','error');
				},
				complete : function() {
					
				}
			});
		}
	}
	
	/** 新增、修改、删除成功后刷新树结构 (静态方式添加树结点) **/
	function reloadNode(operNode) {
		var nodeId = operNode.id;
		var currNode = easyUITree.tree('find',nodeId);
		if (operNode.action == 'delete') {
			if ($.isEmpty(currNode)) {
				$.messager.alert('提示信息','未找到删除的结点，请重新刷新页面后重试!','warning');
			} else {
				easyUITree.tree('remove', currNode.target);
			}
		} else {
			if ($.isEmpty(currNode)) {
				/** 新增结点 **/
				/** 获取父结点编号 **/
				var nodeParentId = operNode.parentId;
				var parentNode = easyUITree.tree('find',nodeParentId);
				if ($.isEmpty(parentNode)) {
					/** 不存在父结点 ！！！！ **/
//					$.messager.alert('提示信息','未找到父结点，请重新刷新页面后重试!','warning');
				} else {
					if (parentNode.attributes.isLoad == '1') {
						easyUITree.tree('append', {
							parent : parentNode.target,
							data : getInitNode(operNode)
						});
						selectedAndLoadData(nodeId);
					} else {
						/** 动态加载结点 **/
						initTree(parentNode, nodeParentId,function() {
							selectedAndLoadData(nodeId);
						});
					}
				}
			} else {
				if (currNode.target) {
					/** 修改结点text属性 **/
					easyUITree.tree('update',{
						target : currNode.target,
						text : operNode.name
					})
					var flagStep = true;
					/** JS 父结点 **/
					var parentNode = easyUITree.tree('getParent',currNode.target);
					/** 后端 父ID **/
					var parentId = operNode.parentId;
					if ($.isEmpty(parentNode)) {
						
					} else {
						if (!(parentNode.id == parentId)) {
							/** 营业网点归属关系出现变更的现象 **/
							var parentNodeTmp = easyUITree.tree('find',parentId);
							if (!$.isEmpty(parentNodeTmp)) {
								if (parentNodeTmp.attributes.isLoad == '1') {
									easyUITree.tree('append',{
										parent: parentNodeTmp.target,
										data: currNode
									});
								} else {
									flagStep = false;
								}
							} else {
								flagStep = false;
							}
							easyUITree.tree('remove',currNode.target);
						} else {
							
						}
					}
					if (flagStep) {
						selectedAndLoadData(nodeId);
					}
				}
			}
		}
	}
	
	/** 结点结点同时加载结点详细信息 **/
	function selectedAndLoadData(nodeId) {
		var nodeTmp = easyUITree.tree('find',nodeId);
		if (!$.isEmpty(nodeTmp)) {
			easyUITree.tree('select',nodeTmp.target);
			loadOrgData(nodeId);
			
			/** 在页面上添加新增和修改提示信息 **/
			var path = getParentPath(nodeTmp)
			tips.html('<p>当前操作：修改网点(<span style="color:red;">' + path + '</span>)</p>');
		} else {
			$.messager.alert('提示信息','结点编号[' + nodeId + ']未找到!','warning');
		}
	}
	
	/** 获取父结点路径 如 /证大投资/北区/江苏分部 **/
	function getParentPath (node) {
		var path = '';
		var parentNode = easyUITree.tree('getParent',node.target);
		if (parentNode) {
			var pathTmp = getParentPath(parentNode);
			path = pathTmp + '/' + node.text;
			return path;
		} else {
			return '/' + node.text;
		}
	}
	
	/** 生成初始结点对象 **/
	function getInitNode(node) {
		var returnNode = {};
		returnNode.id = node.id;
		returnNode.text = node.name;
		returnNode.iconCls = 'pic_4';
		returnNode.attributes = {};
		returnNode.attributes.vLevel = node.vLevel;
		return returnNode;
	}
	
	/** 重置Form表单数据项 **/
	function clearForm() {
		dataForm.form('clear');
		var formData = {};
		formData.departmentType = '0';
		dataForm.form('load',formData);
	}
	
	/** 表单元素隐藏及显示处理 **/
	function showAndHideElement(arr,state) {
		for (var i=0;i<arr.length;i++) {
			var elementId = arr[i];
			if (!$.isEmpty(elementId)) {
				if (state) {
					/** 显示元素 **/
					$('#' + elementId).show();
					if (elementId == 'serviceTelTr') {
						$('#serviceTel').textbox({
							required : true
						})
					} else if (elementId == 'zoneCodeTr') {
						$('#zoneCode').textbox({
							required : true
						})
					} else if (elementId == 'siteTr') {
						$('#site').textbox({
							required : true
						})
					}else if (elementId == 'provinceTr') {
						$('#province').textbox({
							required : true
						})
					}else if (elementId == 'cityTr') {
						$('#city').textbox({
							required : true
						})
					}else if (elementId == 'zoneTr') {
						$('#zone').textbox({
							required : true
						})
					}
				} else {
					/** 隐藏元素 **/
					$('#' + elementId).hide();
					if (elementId == 'serviceTelTr') {
						$('#serviceTel').textbox({
							required : false
						})
					} else if (elementId == 'zoneCodeTr') {
						$('#zoneCode').textbox({
							required : false
						})
					} else if (elementId == 'siteTr') {
						$('#site').textbox({
							required : false
						})
					}else if (elementId == 'provinceTr') {
						$('#province').textbox({
							required : false
						})
					}else if (elementId == 'cityTr') {
						$('#city').textbox({
							required : false
						})
					}else if (elementId == 'zoneTr') {
						$('#zone').textbox({
							required : false
						})
					}
				}
			}
		}
	}
	
	$('a[name=submitBut]').click(function(){
		saveOrUpdateData();
	})
	
	/** 调用初始化树结点方法 **/
	initTree(null,0,function () {
		/** 默认选中第一个结点数据 **/
		var defaultNodeId = easyUITree.tree('getRoot').id;
		if (!$.isEmpty(defaultNodeId)) {
			selectedAndLoadData(defaultNodeId);
		}
	});
	clearForm();
	
})
