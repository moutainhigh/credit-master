package com.zdmoney.credit.system.domain;

import java.util.Date;
import java.util.List;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class ComPermission extends BaseDomain{
    private Long id;

    private String permName;

    private String permUrl;

    private String permType;

    private String position;

    private String sort;

    private Long parentId;

    private String creator;

    private String updator;

    private Date createTime;

    private Date updateTime;

    private String sp1;

    private String sp2;

    private String sp3;
    
    private List<ComPermission> children;
    /** 是否显示 1.显示 2.不显示 **/
    private Long isDisplay;
    
    private String icon;
    /** 外部系统标识（空=本系统，非空=外部系统，详细见SYS_PARAM_DEFINE表定义） **/
    private String systemFlag;

    public List<ComPermission> getChildren() {
		return children;
	}

	public void setChildren(List<ComPermission> children) {
		this.children = children;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName == null ? null : permName.trim();
    }

    public String getPermUrl() {
        return permUrl;
    }

    public void setPermUrl(String permUrl) {
        this.permUrl = permUrl == null ? null : permUrl.trim();
    }

  

    public String getPermType() {
		return permType;
	}

	public void setPermType(String permType) {
		this.permType = permType;
	}

	public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort == null ? null : sort.trim();
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getSp1() {
        return sp1;
    }

    public void setSp1(String sp1) {
        this.sp1 = sp1 == null ? null : sp1.trim();
    }

    public String getSp2() {
        return sp2;
    }

    public void setSp2(String sp2) {
        this.sp2 = sp2 == null ? null : sp2.trim();
    }

    public String getSp3() {
        return sp3;
    }

    public void setSp3(String sp3) {
        this.sp3 = sp3 == null ? null : sp3.trim();
    }

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getSystemFlag() {
		return systemFlag;
	}

	public void setSystemFlag(String systemFlag) {
		this.systemFlag = systemFlag;
	}

	public Long getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Long isDisplay) {
		this.isDisplay = isDisplay;
	}
    
}