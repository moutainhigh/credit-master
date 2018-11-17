<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta http-equiv="pragma" content="no-cache" />
  <meta http-equiv="cache-control" content="no-cache" />
  <meta http-equiv="expires" content="0" />
  <title>减免管理-减免申请</title>
  <jsp:include page="/views/common/headIncludeFile.jsp" />
  <style type="text/css">
  </style>

  <script type="text/javascript">
    importPluginsExt(['panel','combobox','window','layout','datagrid','pagination'
      ,'form','tooltip','validatebox','combogrid'],'business', function() {
      $(function() {
        importJSExt([],function(){
          /** 脚本加载成功回调方法 **/
          $.applyRelief = {
            tabsObj : $("#applyReliefTab"),
            /** 待处理 **/
            applyReliefProcessUrl : global.contextPath + "/applyReliefRepayManager/applyReliefProcessed",
            /** 已完成 **/
            applyReliefFinishUrl : global.contextPath + "/applyReliefRepayManager/applyReliefFinish"
          }
          $("#jumpApplyReliefProcess").attr("src",$.applyRelief.applyReliefProcessUrl);
          $("#jumpApplyReliefFinish").attr("src",$.applyRelief.applyReliefFinishUrl);
          /** 默认选中第一个选项卡 **/
          $.applyRelief.tabsObj.tabs('unselect',0);
          $.applyRelief.tabsObj.tabs('select',0);

          /** 脚本加载成功回调方法 **/
        });
      });
    });
  </script>
</head>
<body data-options="" style="margin:0px;">
<jsp:include page="/views/common/initPageMast.jsp" />
<div class="easyui-tabs" id="applyReliefTab" data-options="fit:true">
  <div title="待处理" id="applyReliefProcess" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px" data-options="fit:true">
    <iframe id = "jumpApplyReliefProcess" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="yes" width="100%" height="100%"></iframe>
  </div>
  <div title="已完成"  id="applyReliefFinish" data-options="iconCls:'icon-large-smartart',tools:''" style="padding:0px" data-options="fit:true">
    <iframe  id = "jumpApplyReliefFinish" frameborder="0" border="0" marginwidth="0" marginheight="0" scrolling="yes" width="100%" height="100%"></iframe>
  </div>
</div>
</body>
</html>
