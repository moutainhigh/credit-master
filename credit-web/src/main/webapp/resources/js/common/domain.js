/** 设置Document.doamin为二级域名 **/
function setDomainName() {
	/** 获取完整的域名 **/
	var domainUrl = window.location.href;
	//var domainUrl = document.domain;
	/** 解析后的二级域名 **/
	var targetDomainUrl = domainUrl.match(/http:\/\/.*?([^\.]+\.com|\.cn|\.org|\.cn|\.net|\.cn)[\/|\:].*/);
	if (!$.isEmpty(targetDomainUrl) && targetDomainUrl.length > 1) {
		try {
			targetDomainUrl = targetDomainUrl[1];
			document.domain = targetDomainUrl;
			$.myConsole.writeLog('设置二级域名：' + targetDomainUrl);
		} catch(e) {
			$.myConsole.writeLog('！！！设置二级失败，完整域名为：' + domainUrl);
		}
	} else {
		$.myConsole.writeLog('！！！获取二级域名失败，完整域名为：' + domainUrl);
	}
}
setDomainName();