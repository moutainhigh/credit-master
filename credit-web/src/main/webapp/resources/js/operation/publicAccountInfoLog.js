$(function() {
    $.publicAccountLog = {
        /** 查询日志url **/
        dataGridUrl : global.contextPath + '/operation/searchLogInfo',
        /** 显示日志信息窗口 **/
        showLogWin : $('#showLogWin'),
        /** 日志信息表格 **/
        logDataGrid : $('#logDataGrid'),
        /** 分页控件 **/
        pager : undefined,
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [10],
        /** 查询处理 **/
        reloadLogDataGrid : function() {
            var objectId = undefined;
            if ($.publicAccountLog.objectId) {
                objectId = $.publicAccountLog.objectId;
            }
            if (!$.isEmpty(objectId)) {
                $.publicAccountLog.logDataGrid.datagrid('reloadData',{
                    url : $.publicAccountLog.dataGridUrl + '?objectId=' + objectId+"&logName=PublicAccountReceiveInfoController"
                });
            } else {
                $.messager.alert('异常信息','缺少objectId参数!','error');
            }
        }
    }
    
    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.publicAccountLog.pg = {
        'page' : 1,
        'rows' : $.publicAccountLog.pageSize
    };
    
    /** 表格对象初始化 **/
    $.publicAccountLog.logDataGrid.datagrid({
        pg : $.publicAccountLog.pg,
        /** 是否显示行号 **/
        rownumbers : true,
        /** 是否单选 **/
        singleSelect : true,
        /** 是否可折叠的 **/
        collapsible : false,
        /** 自适应列宽 **/
        fitColumns : true,
        fit : true,
        /** 是否开启分页 **/
        pagination : true,
        loadMsg : '数据加载中,请稍等...',
        /** 数据长度超出，自动换行 **/
        nowrap : false,
        columns : [[
          /** 列定义 **/
          {   field : 'creator',
              title : '操作者',
              width : '5%'
          },
          {   field : 'createTime',
              title : '操作时间',
              width : '10%',
              formatter:function(value,row,index){
                  return $.DateUtil.dateFormatToFullStr(value);
              }
          },
          {   field : 'content',
              title : '日志内容',
              width : '82%'
          }
        ]],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.publicAccountLog.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.publicAccountLog.pageSizeList,
        /** 自定义行样式 **/
        rowStyler : function(index,row) {
            if (index % 2 == 0) {
            }
        }
    });
    
    /** 分页处理 **/
    $.publicAccountLog.pager = $.publicAccountLog.logDataGrid.datagrid('getPager');
    $.publicAccountLog.pager.pagination({
        onSelectPage : function(pageNumber,pageSize) {
            $(this).pagination('refresh',{
                pageNumber:pageNumber,
                pageSize:pageSize
            });
            $.publicAccountLog.pg.page = pageNumber;
            $.publicAccountLog.pg.rows = pageSize;
            $.publicAccountLog.pageSize = pageSize;
            $.publicAccountLog.reloadLogDataGrid();
        }
    });
    
    /** 日志信息窗口面板参数定义 **/
    $.publicAccountLog.showLogWin.window({
        width : $(window).width() * 9 / 10,
        height : 360,
        //定义窗口是不是模态窗口
        modal : true,
        // 面板距父窗口左边的距离
        left:($(window).width() / 10) * 0.5,
        // 面板距父窗口顶部的距离
        top:($(window).height() - 360) * 0.5,
        //定义是否显示折叠按钮
        collapsible : false,
        //定义是否显示最小化按钮
        minimizable : false,
        //定义是否显示最大化按钮
        maximizable : true,
        //定义是否显示关闭按钮
        closable : true,
        //定义是否关闭了窗口
        closed : true,
        //定义是否窗口能被拖拽
        draggable : true,
        //定义是否窗口可以调整尺寸
        resizable : true,
        //如果设为 true， 当窗口能够显示阴影的时候将会显示阴影。
        shadow : true,
        //定义如何放置窗口  true 就放在它的父容器里 false 就浮在所有元素的顶部
        inline : true
        //样式定义
        //iconCls : 'icon-large-picture'
    });
})
