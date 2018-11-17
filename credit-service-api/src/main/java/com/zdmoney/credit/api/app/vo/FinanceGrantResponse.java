package com.zdmoney.credit.api.app.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ym10094 on 2016/11/10.
 */
public class FinanceGrantResponse implements Serializable {
    private static final long serialVersionUID = 3163417642726959126L;
    /**
     *预审批ID
     */
    private String prePactNo;
    /**
     *处理结果
     */
    private String status;
    /**
     *处理描述
     */
    private String msg;

    private List<FinanceGrantResultVo> listErr;

    public String getPrePactNo() {
        return prePactNo;
    }

    public void setPrePactNo(String prePactNo) {
        this.prePactNo = prePactNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<FinanceGrantResultVo> getListErr() {
        return listErr;
    }

    public void setListErr(List<FinanceGrantResultVo> listErr) {
        this.listErr = listErr;
    }

    public FinanceGrantResultVo getFinanceGrantResultVo(){
        return new FinanceGrantResultVo();
    }
    static public class FinanceGrantResultVo{
        private String pactNo;
        private String projNo;
        private String msg;

        public String getPactNo() {
            return pactNo;
        }

        public void setPactNo(String pactNo) {
            this.pactNo = pactNo;
        }

        public String getProjNo() {
            return projNo;
        }

        public void setProjNo(String projNo) {
            this.projNo = projNo;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
