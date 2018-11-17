$(function() {
    // 是否有查看放款业绩图表或者放款分布图表的权限
    if(ifAnyGranted == 'true'){
        // 绘制放款业绩走势图表和放款分布情况统计图表
        makeCharts();
    }
    
    var chart;
    /** 绘制放款业绩走势图表和放款分布情况统计图表 **/
    function makeCharts(){
        // 若图表存在，则先清空
        clear();
        
        if(!$.isEmpty(loanPerformance)){
            // 绘制放款业绩统计图表
            makeLineCharts("light", "#FFFFFF",loanPerformance.data,loanPerformance.balloonText,loanPerformance.searchVo.countType,loanPerformance.searchVo.showType);
        }
        
        if(!$.isEmpty(loanDistribute)){
            // 绘制放款分布统计图表
            makePieCharts("light", "#FFFFFF",loanDistribute.data,loanDistribute.searchVo.countType);
        }
    }
    
    /** 绘制放款分布情况统计图表（饼状图） **/
    function makePieCharts(theme,bgColor,data,countType){
        if($.isEmpty(data)){
            //$.messager.alert('警告','暂无放款分布情况统计信息！','warning');
            return;
        }
        // 图表添加时间标题
        $("#pieTitle").text(currenMonth);
        // 绘制放款分布金额饼状图表
        makePieChartsCommon(theme,bgColor,data,countType,1);
        // 绘制放款分布件数饼状图表
        makePieChartsCommon(theme,bgColor,data,countType,2);
    }
    
    /** 绘制放款分布情况统计图表（饼状图） **/
    function makePieChartsCommon(theme,bgColor,data,countType,flag) {
        // 创建图表对象
        chart = new AmCharts.AmPieChart();
        // 图表样式
        chart.theme = theme;
        // 显示3D效果
        chart.depth3D =10;
        // 百分比少于此设置的值，则不显示
        chart.hideLabelsPercent = 1;
        // 饼状图半径
        chart.radius =80;
        // 图表标题设置
        if(flag==1){
            chart.titles = [{
                "text": "放款分布情况 放款额（万）",
                "size": 15
            }];
        }else if(flag==2){
            chart.titles = [{
                "text": "放款分布情况 件数（件）",
                "size": 15
            }];
        }
        
        // 绘图数据
        chart.dataProvider = data;
        chart.titleField = "countType";
        if(flag==1){
            chart.valueField ="countMoney";
        }else if(flag==2){
            chart.valueField ="countQuantity";
        }
        chart.balloonText = "[[title]]: [[percents]]%";
        
        // 图表铭文对象
        var legend = new AmCharts.AmLegend();
        legend.align = "center";
        legend.markerType = "circle";
        // 铭文图标大小
        legend.markerSize = 10;
        // 铭文字体大小
        legend.fontSize = 10;
        chart.addLegend(legend);
        
        // 绘制图像
        if(flag==1){
            chart.write("loanDistributeAmountReport");
        }else if(flag==2){
            chart.write("loanDistributeQuantityReport");
        }
    }
    
    /** 绘制放款业绩统计图表、包括折线图表和柱状图表 **/
    function makeLineCharts(theme,bgColor,data,balloonText,countType,showType) {
        if($.isEmpty(data)){
            //$.messager.alert('警告','暂无放款业绩统计信息！','warning');
            return;
        }
        // 给图表展示框添加边框样式
        $("#columnDiv").css({"border": "1px solid black"});
        // 创建图表对象
        chart = new AmCharts.AmSerialChart();
        chart.type = "serial";
        chart.theme = theme;
        chart.titles = [{
            "text": "放款业绩走势",
            "size": 15
        }];
        // 绘图数据
        chart.dataProvider = data;
        chart.categoryField = "countType";
        chart.startDuration = 1;
        var categoryAxis = chart.categoryAxis;
        //categoryAxis.labelRotation = 90;
        categoryAxis.gridPosition ="start";
        
        // 放款额纵坐标对象
        var valueAxis1 = new AmCharts.ValueAxis();
        valueAxis1.title = "放款额（万）";
        valueAxis1.axisColor = "#FF6600";
        valueAxis1.axisThickness = 2;
        valueAxis1.gridAlpha = 0;
        chart.addValueAxis(valueAxis1);
        
        // 件数纵坐标对象
        var valueAxis2 = new AmCharts.ValueAxis();
        valueAxis2.title = "件数（件）";
        // 坐标位置设定靠右
        valueAxis2.position = "right";
        valueAxis2.axisColor = "#FCD202";
        valueAxis2.gridAlpha = 0;
        valueAxis2.axisThickness = 2;
        chart.addValueAxis(valueAxis2);
        
        // 放款额折线对象
        var graph1 = new AmCharts.AmGraph();
        graph1.valueAxis = valueAxis1;
        graph1.title = balloonText+"放款额";
        graph1.valueField = "countMoney";
        graph1.labelText = "[[value]]";
        graph1.balloonText = "[[title]] , [[category]] , [[value]]";
        
        // 放款件数折线对象
        var graph2 = new AmCharts.AmGraph();
        graph2.valueAxis = valueAxis2;
        graph2.title = balloonText+"件数";
        graph2.valueField = "countQuantity";
        graph2.labelText = "[[value]]";
        graph2.balloonText = "[[title]] , [[category]] , [[value]]";
        
        // 如果是绘制折线
        if(showType=="line"){
            graph1.type = "line";
            graph1.lineThickness = 2;
            graph1.fillAlphas = 0;
            graph1.bullet = "round";
            
            graph2.type = "line";
            graph2.lineThickness = 2;
            graph2.fillAlphas = 0;
            graph2.bullet = "round";
        // 如果是绘制柱状图
        }else if(showType="column"){
            graph1.type = "column";
            graph1.lineAlpha =0;
            graph1.fillAlphas = 0.8;
            
            graph2.type = "column";
            graph2.lineAlpha =0;
            graph2.fillAlphas = 0.8;
        }
        chart.addGraph(graph1);
        chart.addGraph(graph2);
        
        // 图表铭文对象
        var legend = new AmCharts.AmLegend();
        legend.useGraphSettings = true;
        legend.align = "center";
        chart.addLegend(legend);
        
        // 绘制图像
        chart.write("columnDiv");
    }
    
    /** 业绩展示切换显示 **/
    $("#showPerformance").click(function(){
        var container = $("#container");
        if(container.is(':hidden')){
            $(this).linkbutton({"iconCls":"icon-add"});
            container.show("slow");
        }else{
            $(this).linkbutton({"iconCls":"icon-remove"});
            container.hide("slow");
        }
    });
    
    /** 清空图表 **/
    function clear(){
        $("#columnDiv").empty();
        $("#pieTitle").empty();
        $("#loanDistributeAmountReport").empty();
        $("#loanDistributeQuantityReport").empty();
    }
})
