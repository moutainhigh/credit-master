package com.zdmoney.credit.overdue.domain;

import com.zdmoney.credit.framework.domain.BaseDomain;

import java.math.BigDecimal;
import java.util.Date;

public class OverdueStat extends BaseDomain{

    private BigDecimal id;

    private Date curDate;

    private BigDecimal shengyubenjin;

    private String yinyebu;

    private String yinyuebucode;

    private BigDecimal yuqijine;

    private BigDecimal yuqilv;

    private String yinyuebuleixing;

    private String yingyebubianhao;

    private BigDecimal daizhangbenjin;

    private BigDecimal daizhangbilv;

    private BigDecimal salesDepartmentId;

    private String fundsSources;

    private Date createTime;

    private Date updateTime;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public Date getCurDate() {
        return curDate;
    }

    public void setCurDate(Date curDate) {
        this.curDate = curDate;
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

    public String getYinyuebuleixing() {
        return yinyuebuleixing;
    }

    public void setYinyuebuleixing(String yinyuebuleixing) {
        this.yinyuebuleixing = yinyuebuleixing == null ? null : yinyuebuleixing.trim();
    }

    public String getYingyebubianhao() {
        return yingyebubianhao;
    }

    public void setYingyebubianhao(String yingyebubianhao) {
        this.yingyebubianhao = yingyebubianhao == null ? null : yingyebubianhao.trim();
    }

    public BigDecimal getDaizhangbenjin() {
        return daizhangbenjin;
    }

    public void setDaizhangbenjin(BigDecimal daizhangbenjin) {
        this.daizhangbenjin = daizhangbenjin;
    }

    public BigDecimal getDaizhangbilv() {
        return daizhangbilv;
    }

    public void setDaizhangbilv(BigDecimal daizhangbilv) {
        this.daizhangbilv = daizhangbilv;
    }

    public BigDecimal getSalesDepartmentId() {
        return salesDepartmentId;
    }

    public void setSalesDepartmentId(BigDecimal salesDepartmentId) {
        this.salesDepartmentId = salesDepartmentId;
    }

    public String getFundsSources() {
        return fundsSources;
    }

    public void setFundsSources(String fundsSources) {
        this.fundsSources = fundsSources == null ? null : fundsSources.trim();
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
}