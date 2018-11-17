<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<meta http-equiv="expires" content="0"/>
		<title>营业网点管理</title>
		<jsp:include page="/views/common/headIncludeFile.jsp" />
		
		<link type="text/css"  rel="stylesheet"  href="<%=request.getContextPath() %>/resources/css/sysIcon.css"/>
		<script type="text/javascript">
			importPluginsExt(['tabs','window','panel','layout','form'],'business', function() {
				$(function() {
					var urlJs = [];
					urlJs.push(global.contextPath + '/resources/js/system/comOrganizationManager.js');
					importJSExt(urlJs,function(){
						/** 脚本加载成功回调方法 **/
						
					});
				})
			});
		</script>
		<style type="text/css">
			
		</style>
	</head>
	<body class="easyui-layout">
		<jsp:include page="/views/common/initPageMast.jsp" />
		<!-- tree 右键菜单项 -->
		<div id="treeMenu" class="easyui-menu">
			<div name="append"  data-options="iconCls:'icon-add'">新增子结点</div>
			<div class="menu-sep"></div>
			<div name="remove"  data-options="iconCls:'icon-remove'">删除结点</div>
		</div>
		<!-- tree 右键菜单项 -->
		<div class="easyui-panel" title="营业网点" style="padding:5px;margin: 0px;" data-options="region:'west',width:250">
			<ul id="easyUITree" class="easyui-tree" data-options="lines:true"></ul>
		</div>
		<div class="easyui-panel" title="营业网点信息编辑" style="padding:20px;margin: 0px;" data-options="region:'center',fit:true" >
			<div>
				<span style="color:red;">
					如需新增或删除营业网点，在左侧树右键进行操作
				</span>
			</div>
			<div id="tips"></div>
			<form method="post"  id="dataForm">
				<input type="hidden" name="id" />
				<input type="hidden" name="parentId" />
				<input type="hidden" name="vLevel" id="vLevel" />
				<table  cellspacing="3"  cellpadding="3">
					<tr>
						<td>名称:</td>
						<td><input class="easyui-textbox" type="text"  name="name" data-options="required:true" style="width:250px;" validType="maxLength[15,'营业网点名称 ']" ></input></td>
					</tr>
					<tr id="parentIdTr" style="display:none;">
						<td>所属层级:</td>
						<td>
							<select class="easyui-combobox" name="parentIds" id="parentIds" style="width:250px;" data-options="editable:false,panelHeight:'300px',valueField: 'id',textField: 'fullName'">
								
							</select>
						</td>
					</tr>
					<tr id="departmentTypeTr" style="display:none;">
						<td>部门类型:</td>
						<td>
							<select class="easyui-combobox" name="departmentType" id="departmentType" style="width:250px;" data-options="editable:false,panelHeight:'auto'">
								<option value="0">空</option>
								<option value="个贷">个贷</option>
								<option value="渠道">渠道</option>
								<option value="渠道2">渠道2</option>
								<option value="电销">电销</option>
								<option value="互联网">互联网</option>
							</select>
						</td>
					</tr>
					<tr id="depLevelTr" style="display:none;">
						<td>网点评级:</td>
						<td>
							<select class="easyui-combobox" name="depLevel" id="depLevel" style="width:250px;" data-options="editable:false,panelHeight:'auto'">
								<option value=" ">空</option>
								<option value="A">A</option>
								<option value="B">B</option>
								<option value="C">C</option>
								<option value="D">D</option>
								<option value="O">O</option>
							</select>
						</td>
					</tr>
					<tr id="serviceTelTr">
						<td>服务电话:</td>
						<td><input class="easyui-textbox" type="text"  id="serviceTel" name="serviceTel" validType="tel" style="width:250px;"></input></td>
					</tr>
					<tr id="siteTr">
						<td>签署区域名称:</td>
						<td><input class="easyui-textbox" type="text" name="site" id="site" validType="maxLength[80,' ']"  data-options="" style="width:250px;"></input></td>
					</tr>
					<tr id="zoneCodeTr">
						<td>所属区域编号:</td>
						<td><input class="easyui-textbox" type="text" name="zoneCode"  id="zoneCode" data-options="validType:['numer[\' \']','equals[4,\' \']']" style="width:250px;"></input></td>
					</tr>
					<tr id="cityNumTr">
						<td>城市编号:</td>
						<td><input class="easyui-textbox" type="text" name="cityNum"  id="cityNum"  data-options="required:true,validType:['numer[\' \']','equals[3,\' \']']" style="width:250px;"></input></td>
					</tr>
					<!-- 新增开始 -->
					<tr id="provinceTr" style="display:none;">
						<td>省:</td>
						<td><input class="easyui-textbox" type="text"  name="province" id="province" style="width:250px;" validType="maxLength[16,'省 ']" ></input></td>
					</tr>
					<tr id="cityTr" style="display:none;">
						<td>市:</td>
						<td><input class="easyui-textbox" type="text"  name="city" id="city" style="width:250px;" validType="maxLength[16,'市 ']" ></input></td>
					</tr>
					<tr id="zoneTr" style="display:none;">
						<td>区（县）:</td>
						<td><input class="easyui-textbox" type="text"  name="zone" id="zone" style="width:250px;" validType="maxLength[16,'区（县） ']" ></input></td>
					</tr>	
					<tr id="isValidTr" style="display:none;">
						<td>是否有效:</td>
						<td>
							<select class="easyui-combobox" name="isValid" id="isValid" data-options="editable:false,panelHeight:'auto'" style="width:250px;" >
								<option value="1">是</option>
								<option value="2">否</option>
							</select>
						</td>
					</tr>
					<tr id="openDateTr" style="display:none;">
						<td>开业日期:</td>
						<td><input class="easyui-datebox"  name="openDate" style="width:250px"  data-options="validType:'date'" pattern="yyyy-MM-dd" /></td>
					</tr>
					<tr id="closeDateTr" style="display:none;">
						<td>停业日期:</td>
						<td><input class="easyui-datebox"  name="closeDate" style="width:250px"  data-options="validType:'date'" pattern="yyyy-MM-dd"/></td>
					</tr>
					<tr id="lngTr" style="display:none;">
						<td>经度:</td>
						<td><input class="easyui-textbox" type="text"  name="lng"  style="width:250px;"></input></td>
					</tr>
					<tr id="latTr" style="display:none;">
						<td>纬度:</td>
						<td><input class="easyui-textbox" type="text"  name="lat"  style="width:250px;"></input></td>
					</tr>
					<!-- 新增结束 -->
					<tr id="memoTr">
						<td>备注:</td>
						<td><input class="easyui-textbox" type="text" name="memo" validType="length[0,100]"  invalidMessage="不能超过100个字符！" data-options="multiline:true" style="width:250px;height:60px;"></input></td>
					</tr>
				</table>
			</form>
			<div style="text-align:left;padding-top:10px;padding-left:100px;">
				<a href="javascript:void(0)" name="submitBut" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" >保存</a>
			</div>
		</div>
	</body>
</html>












