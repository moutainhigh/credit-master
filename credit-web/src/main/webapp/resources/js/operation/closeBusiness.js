(function($) {
	function check() {
        $.messager.alert('会话超时', '会话超时，系统处理中，请稍等！', 'warning');
        window.setTimeout(function() {
            window.location.reload(true);
            //top.window.location.href =global.contextPath+"/views/index.jsp";
        }, 200);
    }
	
     /** 关门营业部 **/
    $('#shutshop').combobox({
        url: global.contextPath +'/csclose/searchShutShop',
        valueField: 'id',
        textField: 'name',
        onLoadSuccess: function(data) {
            for (i = 0; i < data.length; i++) {
                var sel = data[i];
                if (sel.editType == "1") {
                    $('#shutshop').combobox('select', sel.id);
                    var va = sel.id
                    val = va.toString()
                    da = { "name": sel.name, "Id": val, "editType": sel.editType }
                    datasave(da)
                }
            }
        }
    });
    var array1 = [];

    function datasave(ids) {
        array1.push(ids)
    }
    
    $('#saveclose').click(function change() {
        var a = $('#shutshop').combobox('getValues');
        var b = $('#shutshop').combobox('getText');
        var c = b.split(",")
            //声明准备保存的关门营业部
        var array2 = [];
        for (i = 0; i < a.length; i++) {
            var id = a[i];
            var name = c[i];
            array2.push({ "name": name, "Id": id, "editType": "1" });
        }
        //去除的关门营业部
        var minus = [];
        for (var i = 0; i < array1.length; i++) {
            var obj = array1[i];
            var num = obj.Id;
            var isExist = false;
            for (var j = 0; j < array2.length; j++) {
                var aj = array2[j];
                var n = aj.Id;
                if (n == num) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) { minus.push(obj); }
        }
        console.log(minus.length);

        //新增的关门营业部
        var add = [];
        for (var i = 0; i < array2.length; i++) {
            var obj = array2[i];
            var num = obj.Id;
            var isExist = false;
            for (var j = 0; j < array1.length; j++) {
                var aj = array1[j];
                var n = aj.Id;
                if (n == num) {
                    isExist = true;
                    break;
                }
            }
            if (!isExist) { add.push(obj); }
        }
        console.log(add.length);
        var ml = minus.length;
        var al = add.length;

        //判断没新增没去除
        if (ml == 0 && al == 0) {
            $.messager.alert('提示', '关门营业部未变化!', 'info');
            return;
        }
        //判断有新增没去除
        else if (ml == 0 && al != 0) {
            var arr = [];
            for (i = 0; i < add.length; i++) {
                var msg = add[i].name;
                arr.push(msg)
            }
            var addM = arr;
            if (addM.length > 5){
            	addM = arr.slice(0, 5);
            	addM.push('...');
            }
            $.messager.confirm('确认', '新增：' + addM, function(r) {
                if (r) {
                    var dd = JSON.stringify(add);
                    var data1 = { "allOrgType": dd }
                    console.log(data1)
                    $.ajax({
                        type: 'post',
                        url: global.contextPath +'/csclose/editCloseDepartment',
                        dataType: "json",
                        data: data1,
                        success: function(e) {
                            if (e.resCode == '900002') {
                                check()
                            }
                            var a = $('#shutshop').combobox('getValues');
                            var b = $('#shutshop').combobox('getText');
                            var c = b.split(",")
                            array1 = [];
                            for (i = 0; i < a.length; i++) {
                                var id = a[i];
                                var name = c[i];
                                array1.push({ "name": name, "Id": id, "editType": "1" });
                            };
                            $('#orgId').combobox({
                                url: global.contextPath +'/csclose/searchShutedShop',
                                method: 'post',
                                valueField:'id',
                                textField:'name',
                                loadFilter: function(e) {
                                	var content = e;
                                    var o = [{ 'id': "", 'name': '--请选择--' }];
                                    $('#orgId').combobox("select", "");
                                    return o.concat(content);
                                }
                                });
                        }
                    });
                }
            });
        }

        //判断只有去除
        else if (ml != 0 && al == 0) {
            var arr = [];
            for (i = 0; i < minus.length; i++) {
                var msg = minus[i].name;
                minus[i].editType = "3";
                arr.push(msg)
            }
            var deleM =arr;
            if (arr.length > 5){
            	deleM = arr.slice(0, 5);
            	deleM.push('...');
            }
            $.messager.confirm('确认', '删除：' + deleM, function(r) {
                if (r) {
                    for (i = 0; i < minus.length; i++) {
                        minus[i].editType = "3";
                    }
                    //console.log(minus)
                    var dd = JSON.stringify(minus);
                    var data1 = { "allOrgType": dd }
                    console.log(data1)
                    $.ajax({
                        type: 'post',
                        url: global.contextPath +'/csclose/editCloseDepartment',
                        dataType: "json",
                        data: data1,
                        success: function(e) {
                            if (e.resCode == '900002') {
                                check();
                            }
                            var a = $('#shutshop').combobox('getValues');
                            var b = $('#shutshop').combobox('getText');
                            var c = b.split(",")
                            array1 = [];
                            for (i = 0; i < a.length; i++) {
                                var id = a[i];
                                var name = c[i];
                                array1.push({ "name": name, "Id": id, "editType": "1" });
                            }
                            $('#orgId').combobox({
                                url: global.contextPath +'/csclose/searchShutedShop',
                                method: 'post',
                                valueField:'id',
                                textField:'name',
                                loadFilter: function(e) {
                                	var content = e;
                                    var o = [{ 'id': "", 'name': '--请选择--' }];
                                    $('#orgId').combobox("select", "");
                                    return o.concat(content);
                                }
                                });
                        }
                    });
                }
            });
        } else {
            var arr = [];
            var arr1 = [];
            for (i = 0; i < minus.length; i++) {
                var msg = minus[i].name;
                arr.push(msg)
            }
            for (j = 0; j < add.length; j++) {
                var msg1 = add[j].name;
                arr1.push(msg1)
            }
            var deleM =arr;
            var addM = arr1;
            if (arr1.length > 5){
            	addM = arr.slice(0, 5);
            	addM.push('...');
            }
            if (arr.length > 5){
            	deleM = arr.slice(0, 5);
            	deleM.push('...');
            }
            $.messager.confirm('确认', '新增: ' + addM + "<br>" + '删除：' + deleM, function(r) {
                if (r) {
                    //console.log(add)
                    for (k = 0; k < minus.length; k++) {
                        minus[k].editType = "3";
                        var minu = minus[k];
                        add.push(minu)
                    }
                    console.log(add)
                    var dd = JSON.stringify(add);
                    var data1 = { "allOrgType": dd }
                    console.log(data1)
                    $.ajax({
                        type: 'post',
                        url: global.contextPath +'/csclose/editCloseDepartment',
                        dataType: "json",
                        data: data1,
                        success: function(e) {
                            if (e.resCode == '900002') {
                                check()
                            }
                            var a = $('#shutshop').combobox('getValues');
                            var b = $('#shutshop').combobox('getText');
                            var c = b.split(",")
                            array1 = [];
                            for (i = 0; i < a.length; i++) {
                                var id = a[i];
                                var name = c[i];
                                array1.push({ "name": name, "Id": id, "editType": "1" });
                            }
                            $('#orgId').combobox({
                                url: global.contextPath +'/csclose/searchShutedShop',
                                method: 'post',
                                valueField:'id',
                                textField:'name',
                                loadFilter: function(e) {
                                	var content = e;
                                    var o = [{ 'id': "", 'name': '--请选择--' }];
                                    $('#orgId').combobox("select", "");
                                    return o.concat(content);
                                }
                                });
                        }
                    });
                }
            });
        }
    });
    
    

})(jQuery)
