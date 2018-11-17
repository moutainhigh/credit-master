package com.zdmoney.credit.system.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 营业网点，组织架构实体
 * @author Ivan
 *
 */
public class ComOrganization extends BaseDomain {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1557963374884685249L;
	
	/** 主键PK **/
    private Long id;
    /** 编码 **/
//    private String code;
    /** 机构编码 **/
    private String orgCode;
    /** 机构名称 **/
    private String name;
    /** 完整的机构名称 如/证大投资/南区/福州分部/泉州/泉州安吉路营业部/三组 **/
    private String fullName;
    /** 父机构ID**/
    private String parentId;
    /** 档案袋使用的编号 **/
    private String departmentNum;
    /** 部门类型 **/
    private String departmentType;
    /** 网点评级 **/
    private String depLevel;
    /** 机构层级 **/
    private String vLevel;
    /** 备注 **/
    private String memo;
    /** 城市编号 **/
    private String cityNum;
    /** 服务电话 **/
    private String serviceTel;
    /** 客户签署区域名称 **/
    private String site;
    /** 客户所属区域 **/
    private String zoneCode;
    /** 省 **/
    private String province;  
    /** 市  **/
    private String city;  
    /** 区 **/
    private String zone; 
    /** 是否有效1：有效，2：无效 **/
    private String isValid;
    
    /** 开业日期 **/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date openDate; 
    /** 停业日期 **/
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date closeDate;  
    /** 经度 **/
    private String lng;  
    /** 纬度 **/
    private String lat; 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code == null ? null : code.trim();
//    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode == null ? null : orgCode.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public String getDepartmentNum() {
        return departmentNum;
    }

    public void setDepartmentNum(String departmentNum) {
        this.departmentNum = departmentNum == null ? null : departmentNum.trim();
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public void setDepartmentType(String departmentType) {
        this.departmentType = departmentType == null ? null : departmentType.trim();
    }

    public String getDepLevel() {
        return depLevel;
    }

    public void setDepLevel(String depLevel) {
        this.depLevel = depLevel == null ? null : depLevel.trim();
    }

    public String getvLevel() {
        return vLevel;
    }

    public void setvLevel(String vLevel) {
        this.vLevel = vLevel == null ? null : vLevel.trim();
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getCityNum() {
        return cityNum;
    }

    public void setCityNum(String cityNum) {
        this.cityNum = cityNum == null ? null : cityNum.trim();
    }

    public String getServiceTel() {
        return serviceTel;
    }

    public void setServiceTel(String serviceTel) {
        this.serviceTel = serviceTel == null ? null : serviceTel.trim();
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site == null ? null : site.trim();
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode == null ? null : zoneCode.trim();
    }

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Date getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}
    
    
    
}