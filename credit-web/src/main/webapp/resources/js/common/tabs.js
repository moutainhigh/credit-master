$(function() {
	/** 选项卡对象 **/
	$.iframeTabs = $('#tabsPanel');
	if ($.iframeTabs.length) {
		
	} else {
//		$.messager.alert('警告','初始化异常!','warning');
	}
	
	/** 添加选项卡 **/
	$.iframeTabs.add = function(tab) {
			if (tab && $.iframeTabs.length) {
				var tabId = tab.id;
				var tabText = tab.text;
				var tabUrl = tab.url;
				var tabIconCls = tab.iconCls || '';
				var tabClosable = tab.closable;
				if (typeof tabClosable == 'undefined') {
					tabClosable = true;
				}
				
				/** 如果是http开头的URL或相对地址已存在包含应用上下文 跳过加应用上下文 **/
				if (!$.isEmpty(tabUrl)) {
					if (tabUrl.toLowerCase().indexOf('http://') != 0 &&
							(tabUrl.indexOf(global.contextPath) == -1 || tabUrl.indexOf(global.contextPath) > 0)) {
						tabUrl = global.contextPath + tabUrl;
					}
				}
				
				var isExistTab = false;
		
				if (!$.isEmpty(tabId)) {
					var tabs = $.iframeTabs.tabs("tabs");
					var tabIndex = -1;
					for (var i=0; i<tabs.length; i++) {
						var tab = tabs[i];
						if (tab.panel('options').id == tabId) {
							tabIndex = i;
							isExistTab = true;
							break;
						}
					}
					if (tabIndex != -1) {
						$.iframeTabs.tabs('select',tabIndex);
					}
				} else {
					if ($.iframeTabs.tabs('exists',tabText)) {
						isExistTab = true;
						$.iframeTabs.tabs('select',tabText);
					}
				}
				
				if(isExistTab == false){
					var content = '';
					if ($.isEmpty(tabUrl)) {
						content = tabText;
					} else {
						content = '<iframe scrolling="auto" frameborder="0"  src="'+tabUrl+'"  style="width:100%;height:100%;padding:0px;"></iframe>';
					}
					$.iframeTabs.tabs('add',{
						id : tabId,
						title : tabText,
						iconCls : tabIconCls,
						content: content,
						closable: tabClosable
					});
				}
			}
	};
	$.iframeTabs.close=function(tab){
			if (tab && $.iframeTabs.length) {
				var tabId = tab.id;
				var tabText = tab.text;
				var which = null;
				if(tabId != null){
					var tabs = $.iframeTabs.tabs("tabs");
					var tabIndex = -1;
					for(var i=0; i<tabs.length; i++){
						var tab = tabs[i];
						if (tab.panel('options').id == tabId){
							which = i;
							break;
						}
					}
				}else if(tab.text!=null){
					which=tab.text;
				}
				if(which!=null){
					$.iframeTabs.tabs("close",which);
				}
			}
	};
});