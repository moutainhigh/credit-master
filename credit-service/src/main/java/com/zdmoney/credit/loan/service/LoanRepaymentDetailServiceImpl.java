package com.zdmoney.credit.loan.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.domain.OperateLog;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.service.pub.IOperateLogService;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.framework.vo.wm3.entity.RepayPlanListEntity;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2201Vo;
import com.zdmoney.credit.loan.dao.pub.ILoanRepaymentDetailDao;
import com.zdmoney.credit.loan.dao.pub.IVLoanInfoDao;
import com.zdmoney.credit.loan.domain.LoanRepaymentDetail;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanLedgerService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
@Transactional
public class LoanRepaymentDetailServiceImpl implements ILoanRepaymentDetailService{
    private static final Logger logger = Logger.getLogger(LoanRepaymentDetailServiceImpl.class);
    @Autowired 
    ILoanRepaymentDetailDao loanRepaymentDetailDaoImpl;
    @Autowired
    private IVLoanInfoService loanInfoService;
    @Autowired
    private IVLoanInfoDao vloanInfoDao;
    @Autowired
    private IOperateLogService operateLogService;
    @Autowired
    private ISequencesService sequencesService;
    @Autowired
    private ILoanLedgerService loanLedgerService;

    @Override
    public List<LoanRepaymentDetail> findByLoanIdAndRepaymentState(
            Map<String, Object> map) {
        return loanRepaymentDetailDaoImpl.findByLoanIdAndRepaymentState(map);
    }

    @Override
    public BigDecimal findByLoanIdAndNotRepaymentStateInSum(Map<String, Object> map) {
        return loanRepaymentDetailDaoImpl.findByLoanIdAndNotRepaymentStateInSum(map);
    }

    @Override
    public int update(LoanRepaymentDetail repaymentDetail) {
        return loanRepaymentDetailDaoImpl.update(repaymentDetail);
    }

    @Override
    public BigDecimal getDrawJimuRiskSumDeficit(Map<String, Object> map) {
        return loanRepaymentDetailDaoImpl.getDrawJimuRiskSumDeficit(map);
    }
    
    @Override
    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void updateNow(LoanRepaymentDetail para) {
        loanRepaymentDetailDaoImpl.update(para);
    }

    @Override
    public void updateYCXJQAllStateToSettlementByLoanId(Long id, Date tradeDate) {
//      RepaymentDetail.executeUpdate("update RepaymentDetail a set a.repaymentState =:state,a.factReturnDate=:rdate where a.loan.id=:loanid and a.repaymentState in (:whk,:bzehk,:bzfx) ",\
//      [state:RepaymentState.结清,rdate:repay.riTradeDate,loanid:loanInfo.id,whk:zdsys.RepaymentState.未还款,bzehk:zdsys.RepaymentState.不足额还款,bzfx:zdsys.RepaymentState.不足罚息 ])

        loanRepaymentDetailDaoImpl.updateYCXJQAllStateToSettlementByLoanId(id,tradeDate);
    }

    @Override
    public void updateYCXJQAllDeficitToZeroByLoanId(Long id) {
//        RepaymentDetail.executeUpdate("update RepaymentDetail a set a.deficit =0 where a.loan.id=:loanid and a.repaymentState =:whk ",\
//                [loanid:loanInfo.id,whk:zdsys.RepaymentState.结清])
        loanRepaymentDetailDaoImpl.updateYCXJQAllDeficitToZeroByLoanId(id);
    }

    @Override
    public BigDecimal getDrawRiskSumDeficit(Map<String, Object> repaymentMap) {
        return loanRepaymentDetailDaoImpl.getDrawRiskSumDeficit(repaymentMap);
    }

    @Override
    public LoanRepaymentDetail findByLoanAndReturnDate(VLoanInfo loanInfo,
            Date lastRepayDate) {
        LoanRepaymentDetail loanRepaymentDetail = new LoanRepaymentDetail();
        loanRepaymentDetail.setLoanId(loanInfo.getId());
        loanRepaymentDetail.setReturnDate(lastRepayDate);
        List<LoanRepaymentDetail> list = loanRepaymentDetailDaoImpl.findListByVo(loanRepaymentDetail);
        if(list!=null && list.size()>0){
            if(list.size()>1){
                throw new RuntimeException("得到指定还款日期的还款计划,得到过多的结果集！");
            }
            return list.get(0);
        }
        return null;
    }

    @Override
    public LoanRepaymentDetail findRepaymentDetailByLoanAndReturnDate(
            Map<String, String> map) {
        // TODO Auto-generated method stub
        return loanRepaymentDetailDaoImpl.findRepaymentDetailByLoanAndReturnDate(map);
    }
    
    @Override
    public String findRepaymentLevel(Map<String, Object> map) {
        return loanRepaymentDetailDaoImpl.findRepaymentLevel(map);
    }

    @Override
    public void executeRepayPlanUpload(){
        Map<String,Object> map = new HashMap<String, Object>();
        //正常 逾期的都是放款成功的  只发送外贸3
        map.put("operateType", "01");//（01：上传还款计划，02：上传合同资料、03：上传影像资料、04：上传分账明细,05-下载合同资料）'
        map.put("loanBelong", FundsSourcesTypeEnum.外贸3.getValue());
        List<VLoanInfo> vLoanInfoList =  loanInfoService.findGrantSuccessNotToWM3(map);
        if(CollectionUtils.isNotEmpty(vLoanInfoList)){
            int maxSize = 50;
            int dataSize = vLoanInfoList.size();
            int count = dataSize % maxSize == 0 ?(dataSize/maxSize):(dataSize/maxSize)+1;
            List<VLoanInfo> subList = new ArrayList<VLoanInfo>();
            for(int i=0;i<count;i++){
                if(i < count - 1){
                    subList = vLoanInfoList.subList(i*maxSize, (i+1)*maxSize);
                }else{
                    subList = vLoanInfoList.subList(i*maxSize, dataSize);
                }
                try{
                    logger.info("【外贸3】调用还款计划上传接口，带保存的债权数量："+subList.size());
                    repayPlanInterface(subList);
                }catch(Exception e){
                    logger.info("【外贸3】调用还款计划上传接口失败..."+e.getMessage(),e);
                }
                
            }
        }else{
            logger.info("【外贸3】调用还款计划上传接口,没有待上传的还款计划...");
        }
    }
    
    /**
     * 调用外贸3还款计划上传接口
     * @param vLoanInfoList
     */
    @SuppressWarnings("rawtypes")
    public void repayPlanInterface(List<VLoanInfo> vLoanInfoList){
        logger.info("【外贸3】调用还款计划上传接口...");
        WM3_2201Vo vo = new WM3_2201Vo();
        vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);//合作机构号
        List<RepayPlanListEntity> itemList  = new ArrayList<RepayPlanListEntity>();
        HashSet<String> set = new HashSet<String>();   
        SimpleDateFormat sdf = new SimpleDateFormat("yyyMMdd");
        for(VLoanInfo vLoanInfo :vLoanInfoList){
            String conNum = vLoanInfo.getContractNum();
            long loanId = vLoanInfo.getId();
            Map<String,Object> mapDetail = new HashMap<String, Object>();
            mapDetail.put("loanId", loanId);
            List<LoanRepaymentDetail> loanRepaymentDetailList =  loanRepaymentDetailDaoImpl.findListByMap(mapDetail);
            for(LoanRepaymentDetail loanRepaymentDetail : loanRepaymentDetailList){
                long cnt = loanRepaymentDetail.getCurrentTerm();//期次
                Date endDate =loanRepaymentDetail.getReturnDate();//账单日
                BigDecimal totalAmt = loanRepaymentDetail.getReturneterm();//期供金额  每期还款金额
                BigDecimal normInt = loanRepaymentDetail.getCurrentAccrual();//当期利息
                BigDecimal prcpAmt = totalAmt.subtract(normInt);//当期本金     每期还款金额 - 当期利息
                RepayPlanListEntity repayPlanListEntity = new RepayPlanListEntity();
                repayPlanListEntity.setPactNo(conNum);
                repayPlanListEntity.setCnt(Integer.parseInt(String.valueOf(cnt)));
                repayPlanListEntity.setEndDate(sdf.format(endDate));
                repayPlanListEntity.setTotalAmt(totalAmt);
                repayPlanListEntity.setNormInt(normInt);
                repayPlanListEntity.setPrcpAmt(prcpAmt);
                itemList.add(repayPlanListEntity);
            }
        }
        Iterator it = itemList.iterator();
        while(it.hasNext()){
            RepayPlanListEntity tempobj = (RepayPlanListEntity)it.next();  
            set.add(tempobj.getPactNo());
        }
        vo.setDataCnt(itemList.size());//总的记录数  多个 loanId 对应的还款计划总条数
        vo.setList(itemList);//包括多个 合同编号 ，合同编号-期次 唯一
        JSONObject grantResut = null;
        try{
             grantResut = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.外贸3还款计划上传.getCode());
        }catch(Exception e){
            logger.warn("【外贸3】还款计划上传接口调用网关接口失败："+e.getMessage(),e);
            throw new PlatformException(ResponseEnum.FULL_MSG,"【外贸3】还款计划上传接口调用网关接口失败:"+e.getMessage());
        }
        logger.info("【外贸3】还款计划上传接口,外贸3返回的数据："+grantResut);
        String respCode = (String)grantResut.get("respCode");//外贸3返回的结果
        String respDesc = (String)grantResut.get("respDesc");
        if("0000".equals(respCode)){
            String content = (String)grantResut.get("content");//string 类型
            JSONObject jsonObjectContent = JSON.parseObject(content);
            logger.info("调用【外贸3】还款计划上传接口成功，返回信息："+respDesc);
            //String dataCnt = jsonObjectContent.get("dataCnt").toString();
            JSONArray jsonArray = jsonObjectContent.getJSONArray("list");//数组  失败的记录
            for(Object obj:jsonArray){
                JSONObject json = JSONObject.parseObject(obj.toString());
                String pactNo = json.getString("pactNo");
                //String cnt = json.getString("cnt");
                //String dealDesc = json.getString("dealDesc");
                set.remove(pactNo);//移除掉失败的
            }
            saveOperateLog(set);//只保存上传成功的 记录 以合同号为主
        }else{
            logger.info("调用【外贸3】还款计划上传接口失败：原因："+respDesc);
            throw new PlatformException(ResponseEnum.FULL_MSG,respDesc);
        }
    }
    
    
    /**
     * 保存操作日志   只保存上传给外贸3,并且返回成功的。
     * @param set
     */
    public void saveOperateLog(HashSet<String> set){
        logger.info("【外贸3】还款计划上传接口-保存操作日志!");
        Map<String,Object> map =new  HashMap<String,Object>();
        if(set.size()>0){
            logger.info("待保存到操作日子表中合同号有："+set);
            map.put("contractNums", set);
            List<VLoanInfo> vLoanInfoList = vloanInfoDao.findListByMap(map);
            for(VLoanInfo vLoanInfo : vLoanInfoList){
                OperateLog log = new OperateLog();
                log.setId(sequencesService.getSequences(SequencesEnum.OPERATE_LOG));
                log.setLoanId(vLoanInfo.getId());
                log.setOperateType("01");//01：上传还款计划，02：上传合同资料、03：上传影像资料、04：上传分账明细 
                log.setOperateDate(new Date());
                log.setStatus("1");
                operateLogService.save(log);
            }
        }
    }

	@Override
	public List<LoanRepaymentDetail> findListByMap(Map<String, Object> map) {
		return loanRepaymentDetailDaoImpl.findListByMap(map);
	}

	@Override
	public List<LoanRepaymentDetail> findLoanRepaymentDetailListByLoanId(Long loanId) {
		return loanRepaymentDetailDaoImpl.findByLoanId(loanId);
	}
}
