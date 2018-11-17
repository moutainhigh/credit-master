$.myConsole = window.console || {};

$.myConsole.isDebug = true;

$.myConsole.writeLog = function(msg) {
	if ($.isFunction($.myConsole.log)) {
		if ($.myConsole.isDebug) {
			$.myConsole.log(msg);
		}
	}
}

$.myConsole.writeWarnLog = function(msg) {
	if ($.isFunction($.myConsole.log)) {
		if ($.myConsole.isDebug) {
			$.myConsole.warn(msg);
		}
	}
}
