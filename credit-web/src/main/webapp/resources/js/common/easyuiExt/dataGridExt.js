/**
 * 扩展EasyUI DataGrid组件
 */
/** 获取表格选中的行 * */
if (!$.dataGrid) {
	$.dataGrid = {};
}

$.extend($.fn.datagrid.defaults,{
	/** 自动填充奇数行背景色 **/
	autoRowColor : true
})

$.extend($.fn.datagrid.defaults.view,{
	/** 渲染结果为空时添加空数据提示信息 **/
	onAfterRender : function(target){
		var opts = $(target).datagrid('options');
		var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
		vc.children('div.datagrid-empty').remove();
		if (!$(target).datagrid('getRows').length){
			var d = $('<div class="datagrid-empty"></div>').html(opts.emptyMsg || '查询记录不存在').appendTo(vc);
			d.css({
				position:'absolute',
				left:0,
				top:30,
				width:'100%',
				textAlign:'center',
				color:'red'
			});
		}
	},
	/** 重写渲染DataGrid行方法 **/
	renderTable : function(_783, _784, rows, _785) {
		var _786 = $.data(_783, "datagrid");
		var opts = _786.options;
		opts = $.extend({
			
		},opts);
		if (_785) {
			if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))) {
				return "";
			}
		}
		var _787 = $(_783).datagrid("getColumnFields", _785);
		var _788 = [ "<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" ];
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			var css = opts.rowStyler ? opts.rowStyler.call(_783, _784, row)
					: "";
			var _789 = "";
			var _78a = "";
			if (typeof css == "string") {
				_78a = css;
			} else {
				if (css) {
					_789 = css["class"] || "";
					_78a = css["style"] || "";
				}
			}
			var cls = "class=\"datagrid-row "
					+ (_784 % 2 && opts.striped ? "datagrid-row-alt " : " ")
					+ _789
					
					/** 修改点 **/
					+ (opts.autoRowColor ? ((_784 % 2)  ? " datagrid-row-odd " : " datagrid-row-even ") : "")
					/** 修改点 **/
					
					+ "\"";
			var _78b = _78a ? "style=\"" + _78a + "\"" : "";
			var _78c = _786.rowIdPrefix + "-" + (_785 ? 1 : 2) + "-" + _784;
			_788.push("<tr id=\"" + _78c + "\" datagrid-row-index=\""
					+ _784 + "\" " + cls + " " + _78b + ">");
			_788.push(this.renderRow
					.call(this, _783, _787, _785, _784, row));
			_788.push("</tr>");
			_784++;
		}
		_788.push("</tbody></table>");
		return _788.join("");
	},
	/** 重写渲染DataGrid行方法 **/
	renderRow : function(_78d, _78e, _78f, _790, _791) {
		var opts = $.data(_78d, "datagrid").options;
		var cc = [];
		if (_78f && opts.rownumbers) {
			var _792 = _790 + 1;
			if (opts.pagination) {
				_792 += (opts.pageNumber - 1) * opts.pageSize;
			}
			cc
					.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">"
							+ _792 + "</div></td>");
		}
		for (var i = 0; i < _78e.length; i++) {
			var _793 = _78e[i];
			var col = $(_78d).datagrid("getColumnOption", _793);
			if (col) {
				var _794 = _791[_793];
				var css = col.styler ? (col.styler(_794, _791, _790) || "")
						: "";
				var _795 = "";
				var _796 = "";
				if (typeof css == "string") {
					_796 = css;
				} else {
					if (css) {
						_795 = css["class"] || "";
						_796 = css["style"] || "";
					}
				}
				var cls = _795 ? "class=\"" + _795 + "\"" : "";
				var _797 = col.hidden ? "style=\"display:none;" + _796
						+ "\"" : (_796 ? "style=\"" + _796 + "\"" : "");
				cc.push("<td field=\"" + _793 + "\" " + cls + " " + _797
						+ ">");
				var _797 = "";
				if (!col.checkbox) {
					if (col.align) {
						_797 += "text-align:" + col.align + ";";
					}
					if (!opts.nowrap) {
						_797 += "white-space:normal;height:auto;";
					} else {
						if (opts.autoRowHeight) {
							_797 += "height:auto;";
						}
					}
				}
				cc.push("<div style=\"" + _797 + "\" ");
				cc.push(col.checkbox ? "class=\"datagrid-cell-check\""
						: "class=\"datagrid-cell " + col.cellClass + "\"");
				cc.push(">");
				if (col.checkbox) {
					cc.push("<input type=\"checkbox\" "
							+ (_791.checked ? "checked=\"checked\"" : ""));
					cc.push(" name=\"" + _793 + "\" value=\""
							+ (_794 != undefined ? _794 : "") + "\">");
				} else {
					if (col.formatter) {
						_794 = col.formatter(_794, _791, _790);
					} else {
						
					}
					var vType = col.vType || "";
					if (vType == "rmb" && $.isNumeric(_794)) {
						/** 格式化金额 如 99,123,00 **/
						if ($.isFunction($.comdify)) {
							_794 = $.comdify(_794,2);
						}
					} else if (vType == "percent" && $.isNumeric(_794)) {
						/** 格式化百分比 如 0.123 显示 12.3% **/
						_794 = parseFloat(_794);
						_794 = (_794 * 100).toFixed(2) + '%';
					}
					cc.push(_794);
				}
				cc.push("</div>");
				cc.push("</td>");
			}
		}
		return cc.join("");
	}
})





/** 获取DataGrid表格选中的行 * */
$.dataGrid.getSelectedRow = function (dataGrid) {
	var selectedRow = dataGrid.datagrid('getSelected');
	return selectedRow;
}
$.extend($.fn.datagrid.methods, {
	/** 清空表格数据 **/
	clearData : function(jq,param) {
		var item = $(jq).datagrid('getRows');
		if (item) {
			for (var i = item.length - 1; i >= 0; i--) {
				var index = $(jq).datagrid('getRowIndex', item[i]);
				$(jq).datagrid('deleteRow', index);
			}
		}
		/** 分页对象 **/
		var pager = $(jq).datagrid('getPager');
		/** 分页对象初始化 **/
		pager.pagination("refresh",{
            total: 0,
            pageNumber: 0
        });
	},
	/**
	 * 内部封闭EasyUI DataGrid reload方法 定义DataGrid时必须传入pg属性 $.pg = {'page' :
	 * 1,'rows' : 10} 参数值随不同场景自行定义 参数 jq ：DataGrid实例 param
	 * 其中url参数为必传项，其余参数随机不同场景自动传入
	 */
	reloadData : function(jq,param) {
		/** 清空表格数据 **/
//		$(jq).datagrid('clearData');
		var pager = $(jq).datagrid('getPager');
		var options = $(jq).datagrid('options');
		var pagination = options.pagination;
		
		/** 申请地址 * */
		if ($.isEmpty(param)) {
			$.messager.alert('警告','DataGrid缺少参数!','warning');
			return;
		}
		var requestUrl = param.url;
		if ($.isEmpty(requestUrl)) {
			$.messager.alert('警告','DataGrid缺少url参数!','warning');
			return;
		}
		
		var pg = options.pg;
		if (pagination && $.isEmpty(pg)) {
			$.messager.alert('警告','DataGrid缺少pg（分页）参数!','warning');
			return;
		}
		
		/** 超时时间(毫秒) **/
		var timeout = options.timeout || undefined;
		
		if ($.isEmpty(param)) {
			param = {};
		}
		
		/** 拼接参数 * */
		if (pg) {
			param.page = pg.page;
			param.rows = pg.rows;
			/** 设置分页控件当前页码 * */
			pager.pagination("refresh",{pageNumber:pg.page});
		}
		$.ajaxPackage({
			type : 'get', 
			url : requestUrl,
			dataType : "json",
			data : param,
			timeout : timeout,
			success : function (data,textStatus,jqXHR) {
				var resCode = data.resCode;
				var resMsg = data.resMsg;
				var attachment = data.attachment;
				if (resCode == '000000') {
					/** 服务端返回正常，填充数据 * */
					/** 设置DataGrid页码 * */
					if (pg) {
						$(jq).datagrid('options').pageNumber = pg.page;
						$(jq).datagrid('options').pageSize = pg.rows;
					}
					$(jq).datagrid('loadData',attachment);
					
					if ($.isFunction(options.callBackFunction)) {// 如果回调函数不为空，执行回调函数
						options.callBackFunction(attachment);
					}
				} else if(resCode == '800000'){
					/** 操作警告提示 * */
					$.messager.alert('警告',resMsg,'warning');
				} else {
					/** 操作失败 * */
					$.messager.alert('异常信息',resMsg,'error');
				}
			},
			error : function (XMLHttpRequest, textStatus, errorThrown,d) {
				$.messager.alert('异常信息',textStatus + '  :  ' + errorThrown + '!','error');
			},
			complete : function() {
				
			}
		});
    }
});