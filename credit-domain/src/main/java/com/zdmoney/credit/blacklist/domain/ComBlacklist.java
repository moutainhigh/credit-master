package com.zdmoney.credit.blacklist.domain;


import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

/**
 * 黑名单表
 * @author 00232949
 *
 */
public class ComBlacklist extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7321774503658673862L;

	private Long id;

    private String idnum;

    private String name;

    private Date rejectDate;

    private Long canSubmitRequestDays;

    private String memo;

    private String loanType;

    private Long operatorId;

    private Long salesDepartmentId;

    private String comeFrome;

    private String tel;

    private String company;

    private String mphone;

    private String isDelete;

    private Long delOperatorId;

    private Date createTime;

    private Date updateTime;



	public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum == null ? null : idnum.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Date getRejectDate() {
        return rejectDate;
    }

    public void setRejectDate(Date rejectDate) {
        this.rejectDate = rejectDate;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType == null ? null : loanType.trim();
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCanSubmitRequestDays() {
		return canSubmitRequestDays;
	}

	public void setCanSubmitRequestDays(Long canSubmitRequestDays) {
		this.canSubmitRequestDays = canSubmitRequestDays;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public Long getSalesDepartmentId() {
		return salesDepartmentId;
	}

	public void setSalesDepartmentId(Long salesDepartmentId) {
		this.salesDepartmentId = salesDepartmentId;
	}

	public String getComeFrome() {
		return comeFrome;
	}

	public void setComeFrome(String comeFrome) {
		this.comeFrome = comeFrome;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getMphone() {
		return mphone;
	}

	public void setMphone(String mphone) {
		this.mphone = mphone;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	public Long getDelOperatorId() {
		return delOperatorId;
	}

	public void setDelOperatorId(Long delOperatorId) {
		this.delOperatorId = delOperatorId;
	}
}