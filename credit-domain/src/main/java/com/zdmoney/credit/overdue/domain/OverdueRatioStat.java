package com.zdmoney.credit.overdue.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.zdmoney.credit.framework.domain.BaseDomain;

public class OverdueRatioStat extends BaseDomain{
    /**
	 * 
	 */
	private static final long serialVersionUID = -1398753643427458309L;

	private Long id;

    private Date curDate;

    private String daikuanleixing;

    private BigDecimal shengyubenjin;

    private String yinyebu;

    private String yinyuebucode;

    private BigDecimal yuqijine;

    private BigDecimal yuqilv;

    private String yuqitianshu;

    private String yinyuebuleixing;

    private Long salesDepartmentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCurDate() {
        return curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
    }

    public String getDaikuanleixing() {
        return daikuanleixing;
    }

    public void setDaikuanleixing(String daikuanleixing) {
        this.daikuanleixing = daikuanleixing == null ? null : daikuanleixing.trim();
    }

    public BigDecimal getShengyubenjin() {
        return shengyubenjin;
    }

    public void setShengyubenjin(BigDecimal shengyubenjin) {
        this.shengyubenjin = shengyubenjin;
    }

    public String getYinyebu() {
        return yinyebu;
    }

    public void setYinyebu(String yinyebu) {
        this.yinyebu = yinyebu == null ? null : yinyebu.trim();
    }

    public String getYinyuebucode() {
        return yinyuebucode;
    }

    public void setYinyuebucode(String yinyuebucode) {
        this.yinyuebucode = yinyuebucode == null ? null : yinyuebucode.trim();
    }

    public BigDecimal getYuqijine() {
        return yuqijine;
    }

    public void setYuqijine(BigDecimal yuqijine) {
        this.yuqijine = yuqijine;
    }

    public BigDecimal getYuqilv() {
        return yuqilv;
    }

    public void setYuqilv(BigDecimal yuqilv) {
        this.yuqilv = yuqilv;
    }

    public String getYuqitianshu() {
        return yuqitianshu;
    }

    public void setYuqitianshu(String yuqitianshu) {
        this.yuqitianshu = yuqitianshu == null ? null : yuqitianshu.trim();
    }

    public String getYinyuebuleixing() {
        return yinyuebuleixing;
    }

    public void setYinyuebuleixing(String yinyuebuleixing) {
        this.yinyuebuleixing = yinyuebuleixing == null ? null : yinyuebuleixing.trim();
    }

    public Long getSalesDepartmentId() {
        return salesDepartmentId;
    }

    public void setSalesDepartmentId(Long salesDepartmentId) {
        this.salesDepartmentId = salesDepartmentId;
    }

}