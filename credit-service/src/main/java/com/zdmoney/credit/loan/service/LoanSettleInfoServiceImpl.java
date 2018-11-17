package com.zdmoney.credit.loan.service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Assert;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.loan.dao.pub.ILoanSettleInfoDao;
import com.zdmoney.credit.loan.domain.LoanSettleInfo;
import com.zdmoney.credit.loan.domain.LoanTransferInfo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanSettleInfoService;
import com.zdmoney.credit.loan.service.pub.ILoanTransferInfoService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.domain.PersonInfo;
import com.zdmoney.credit.system.service.pub.IPersonInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoanSettleInfoServiceImpl implements ILoanSettleInfoService {
    protected static Log logger = LogFactory.getLog(LoanSettleInfoServiceImpl.class);
    @Autowired
    ISequencesService sequencesService;
    @Autowired
    ILoanSettleInfoDao loanSettleInfoDao;
    @Autowired
    IVLoanInfoService vLoanInfoService;
    @Autowired
    ILoanTransferInfoService loanTransferInfoService;
    @Autowired
    IPersonInfoService personInfoService;

    @Override
    public LoanSettleInfo bulidLoanSettleInfo(Map<String, String> map) {
        String name = map.get("name");//姓名
        String idNum = map.get("idNum");//身份证号
        String contractNum = map.get("contractNum");//合同编号
        String transferState = map.get("transferState");//转让状态
        String memo = map.get("memo");//备注

        Assert.notNullAndEmpty(name, "姓名");
        Assert.notNullAndEmpty(idNum, "身份证号");
        Assert.notNullAndEmpty(contractNum, "合同编号");
        Assert.notNullAndEmpty(transferState, "转让状态");
        Assert.notNullAndEmpty(memo, "备注");

        VLoanInfo vLoanInfoVo = new VLoanInfo();
        vLoanInfoVo.setContractNum(contractNum);
        List<VLoanInfo> loanList = vLoanInfoService.findListByVO(vLoanInfoVo);

        LoanSettleInfo loanSettleInfo = new LoanSettleInfo();
        loanSettleInfo.setId(sequencesService.getSequences(SequencesEnum.LOAN_SETTLE_INFO));
        loanSettleInfo.setIdNum(idNum);
        loanSettleInfo.setContractNum(contractNum);
        loanSettleInfo.setMemo(memo);
        loanSettleInfo.setName(name);
        loanSettleInfo.setTransferState(transferState);
        if (CollectionUtils.isEmpty(loanList)) {
            loanSettleInfo.setNote("不存在该笔债权");
            return loanSettleInfo;
        }

        VLoanInfo vLoanInfo = loanList.get(0);
        loanSettleInfo.setLoanId(vLoanInfo.getId());

        LoanSettleInfo LoanSettleInfoOld = this.findLoanSettleInfoByLoanId(vLoanInfo.getId());
        if (null != LoanSettleInfoOld) {
            //更新
            LoanSettleInfoOld.setIdNum(idNum);
            LoanSettleInfoOld.setMemo(memo);
            LoanSettleInfoOld.setName(name);
            LoanSettleInfoOld.setTransferState(transferState);
            loanSettleInfoDao.update(LoanSettleInfoOld);
            loanSettleInfo.setNote("已更新");
            return loanSettleInfo;
        }

        LoanTransferInfo loanTransferInfo = loanTransferInfoService.findLoanTransferInfoByLoanId(vLoanInfo.getId());
        if (null == loanTransferInfo || null == loanTransferInfo.getTransferBatch()) {
            loanSettleInfo.setNote("该债权未转让，无法导入。");
            return loanSettleInfo;
        }
        //校验身份证号与合同号是否匹配
        PersonInfo personInfo = personInfoService.findById(vLoanInfo.getBorrowerId());
        if (idNum.equals(personInfo.getIdnum())) {
            loanSettleInfoDao.insert(loanSettleInfo);
            loanSettleInfo.setNote("导入成功");
            return loanSettleInfo;
        }
        loanSettleInfo.setNote("身份证号与合同编号不匹配，请修改后再次导入。");
        return loanSettleInfo;
    }

    @Override
    public LoanSettleInfo findLoanSettleInfoByLoanId(Long loanId) {
        return loanSettleInfoDao.findLoanSettlenfoByLoanId(loanId);
    }

}
