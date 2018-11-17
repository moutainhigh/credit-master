/**
 * Created by YM10098 on 2017/7/24.
 */
$(function() {
    $.contactInfo = {
        /** 通话详情解析数据源地址 **/
        contactInfoUrl : global.contextPath + '/personTel/search',
        /** 远程调用征审通话记录地址 **/
        contactRecordUrl: $("#contactRecordUrl").val(),
        /** 通话详情列表 **/
        contactInfoGrid : $('#contactInfoGrid'),
        /** 电话，地址所绑定的对象类型(zdsys.Borrower：客户  zdsys.Contact：客户的联系人) **/
        className : '',
        /** 客户编号 或 联系人编号 **/
        objectId : '',
        /** 债权编号 **/
        appNo : '',
        /**通话详情窗口**/
        contactInfoWin : $('#contactInfoWin'),
        /** 通话记录窗口 **/
        contactRecordWin: $('#contactRecordWin'),
        /** 通话记录iframe **/
        contactRecordFrame: $('#contactRecordFrame'),
        /** 分页控件 **/
        pager : undefined,
        /** 每页显示的记录条数，默认为10 **/
        pageSize : 10,
        /** 设置每页记录条数的列表 **/
        pageSizeList : [10,20,30,40,50],
        /** 加载表格数据 **/
        reloadDataGrid : function() {
            var queryParam = {};
            queryParam.page = $.contactInfo.pg.page;
            queryParam.rows = $.contactInfo.pg.rows;
            queryParam.objectId = $.contactInfo.objectId;
            queryParam.className = $.contactInfo.className;
            queryParam.telType = '手机';
            queryParam.url = $.contactInfo.contactInfoUrl;
            $.contactInfo.contactInfoGrid.datagrid('reloadData',queryParam);
        }
    }
    /** 分页参数（page:当前第N页，rows:一页N行） **/
    $.contactInfo.pg = {
        'page' : 1,
        'rows' : $.contactInfo.pageSize
    }

    /** DataGrid初始化 **/
    $.contactInfo.contactInfoGrid.datagrid({
        /** 分页参数对象 **/
        pg : $.contactInfo.pg,
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
        /** 加载提示信息 **/
        loadMsg : '数据加载中,请稍等...',
        columns : [ [
            // 列定义
            {
                field : 'content',
                title : '电话号码',
                width : '30%'
            },{
                field : 'operate',
                title : '操作',
                width : '60%',
                formatter:function(value,row,index){
                    if(row){
                        var elements = "";
                        elements = "<a href='javascript:void(0)' class='contactRecord' tel-num = '" +row.content+ "'>通话详情解析</a>";
                        return elements;
                    }
                }
            }] ],
        /** 每页显示的记录条数，默认为10 **/
        pageSize : $.contactInfo.pageSize,
        /** 可以设置每页记录条数的列表 **/
        pageList : $.contactInfo.pageSizeList,
        /** 自定义行样式 * */
        rowStyler : function(index, row) {
            if (index % 2 == 0) {
            }
        },
        onLoadSuccess:function(data){
            $(".contactRecord").click(function(){
                var mobile = $(this).attr('tel-num');
                var appNo = $.contactInfo.appNo;
                var url = $.contactInfo.contactRecordUrl;
                url += "?mobile="+ mobile +'&appNo='+appNo;
                window.open(url);
                //$.contactInfo.contactRecordFrame.attr("src", url);
                //$.contactInfo.contactRecordWin.window('open');
            })
        }
    });
    /** 表格分页组件 **/
    $.contactInfo.pager = $.contactInfo.contactInfoGrid.datagrid('getPager');
    $.contactInfo.pager.pagination({
        onSelectPage : function(pageNumber, pageSize) {
            $.contactInfo.pg.page = pageNumber;
            $.contactInfo.pg.rows = pageSize;
            $.contactInfo.reloadDataGrid();
        }
    });

    /** 我的通话详情窗口面板参数定义 **/
    $.contactInfo.contactInfoWin.window({

        //定义窗口是不是模态窗口
        modal : true,
        //定义是否显示折叠按钮
        collapsible : false,
        //定义是否显示最小化按钮
        minimizable : false,
        //定义是否显示最大化按钮
        maximizable : false,
        //定义是否显示关闭按钮
        closable : true,
        //定义是否关闭了窗口
        closed : true,
        //定义是否窗口能被拖拽
        draggable : true,
        //定义是否窗口可以调整尺寸
        resizable : false,
        //如果设为 true， 当窗口能够显示阴影的时候将会显示阴影。
        shadow : true,
        //定义如何放置窗口  true 就放在它的父容器里 false 就浮在所有元素的顶部
        inline : true,
        //样式定义
        iconCls : 'icon-search'
    })
    /** 我的通话详情窗口面板参数定义
    $.contactInfo.contactRecordWin.window({

        //定义窗口是不是模态窗口
        modal : true,
        //定义是否显示折叠按钮
        collapsible : false,
        //定义是否显示最小化按钮
        minimizable : false,
        //定义是否显示最大化按钮
        maximizable : false,
        //定义是否显示关闭按钮
        closable : true,
        //定义是否关闭了窗口
        closed : true,
        //定义是否窗口能被拖拽
        draggable : true,
        //定义是否窗口可以调整尺寸
        resizable : false,
        //如果设为 true， 当窗口能够显示阴影的时候将会显示阴影。
        shadow : true,
        //定义如何放置窗口  true 就放在它的父容器里 false 就浮在所有元素的顶部
        inline : true,
        //样式定义
        iconCls : 'icon-search'
    })
     **/
    $("#contactInfo").click(function(){
        var row = $.dataGrid.getSelectedRow($.vloanInfo.dataGrid);
        if(row){
            $.contactInfo.objectId = row.borrowerId;
            $.contactInfo.appNo = row.appNo;
            /** 操作客户的电话及地址 **/
            $.contactInfo.className = 'zdsys.Borrower';
            $.contactInfo.reloadDataGrid();
            $.contactInfo.contactInfoWin.window('open');
        }else{
            $.messager.alert('警告','请选择需要查看的数据!','warning');
        }
    })
})