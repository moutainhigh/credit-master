package com.zdmoney.credit.common.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EasyUI树结构
 * @author Ivan
 *
 */
public class EasyUITree implements Serializable {
	private static final long serialVersionUID = -2449789444154887325L;
	/** 结点编号 **/
	private String id;
	/** 结点名称 **/
	private String text;
	/** 结点图标 **/
	private String iconCls;
	/** 结点默认收缩 **/
	private String state = "";
	/** 结点默认不选中（复选框） **/
	private boolean checked = false;
	/** 结点附加额外属性 **/
	private Map<String,Object> attributes = new HashMap<String,Object>();
	/** 包含的子结点 **/
	private List<EasyUITree> children = new ArrayList<EasyUITree>();
	
	public EasyUITree() {
		
	}
	
	public EasyUITree(String id,String text) {
		this.id = id;
		this.text = text;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	public List<EasyUITree> getChildren() {
		return children;
	}
	public void setChildren(List<EasyUITree> children) {
		this.children = children;
	}
	
}
