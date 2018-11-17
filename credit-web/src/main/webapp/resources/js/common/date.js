/** JS日期工具包 **/
$.dates = {
	/** 格式化日期格式（YYYY-MM-DD） **/
	formatter : function(date) {
		if (!date) {
			return '';
		}
		if (typeof date != 'object') {
			date = new Date(date);
		}
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		return y + '-' + (m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
	},
	parser : function(s) {
		if (!s) {
			return new Date();
		}
		if (typeof s == 'number') {
			return new Date(s);
		}
		var ss = (s.split('-'));
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		var d = parseInt(ss[2],10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
			return new Date(y,m-1,d);
		} else {
			return new Date();
		}
	},
	/** 格式化日期格式（YYYY-MM-DD hh:mm:ss） **/
	format : function (date) {
	    if (!date) {
			return '';
		}
		if (typeof date != 'object') {
			date = new Date(date);
		}
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		var d = date.getDate();
		var hh = date.getHours();
		var mm = date.getMinutes();
		var ss = date.getSeconds();
		return y + '-' + (m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+hh+':'+mm+':'+ss;
	}
}
