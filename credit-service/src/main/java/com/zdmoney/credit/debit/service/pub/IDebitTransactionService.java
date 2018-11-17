package com.zdmoney.credit.debit.service.pub;

import java.util.List;

import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.debit.domain.DebitTransaction;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2311Vo;
import com.zdmoney.credit.framework.vo.wm3.output.WM3_2311OutputVo;
import com.zdmoney.credit.loan.domain.VLoanInfo;

/**
 * @ClassName:     IDebitTransactionService.java
 * @Description:   
 * @author         柳云华
 * @version        V1.0  
 * @Since          JDK 1.7
 * @Date           2017年3月22日 下午5:46:52
 */
public interface IDebitTransactionService {
    
    /**
     * 创建第三方划扣流水
     * @param offer
     * @return
     */
    public DebitTransaction buildDebitTransaction(DebitBaseInfo offer);
    
    /**
     * 更新状态到已报盘、已发送
     * @param transaction
     */
    public void updateOfferStateToYibaopan(DebitTransaction transaction, String serialNo);
    
    /**
     * 申请失败，更新失败原因
     * @param code
     * @param msg
     * @param offer
     * @param transaction
     */
    public void updateErrorMsgNow(String code, String msg, DebitBaseInfo offer, DebitTransaction transaction);
    
    /**
     * 回调结果通知
     * @param serialNo 交易流水号
     * @param code
     * @param msg
     * @param applyType 申请类型
     */
    public void callbackNotify(String serialNo,String code,String msg,String applyType);
    
    /**
     * 检查是否有未回盘的情况
     * 查询该loan报盘明细DebitTransaction的所有记录，查看是否有未回盘的，如果有，不能再次生成
     * 
     * @param loan
     * @return
     */
    public boolean checkHasOffer(VLoanInfo loan);
    /**
     * 得到状态是未回盘的信息
     * @param id
     * @return
     */
    public List<DebitTransaction> getWeiHuiPanTraByloan(Long id);
    /**
     * 更新状态（外贸3）
     */
    public void updateOfferStateWM3ToYibaopan(WM3_2311Vo wM3_2311Vo, WM3_2311OutputVo wM3_2311OutputVo);
    
    
    /**
     * 更新状态（发送代扣后第三方）
     */
    public void updateDebitStateToYibaopan(List<DebitTransaction> debitTransactionList);
    
    /**
     * 根据交易流水号查询报盘流水信息
     * @param serialNo
     * @return
     */
    public DebitTransaction findDebitTransactionBySerialNo(String serialNo);
}
