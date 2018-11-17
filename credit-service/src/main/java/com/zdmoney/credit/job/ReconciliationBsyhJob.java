package com.zdmoney.credit.job;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.bsyh.dao.pub.IGrantRecDetailDao;
import com.zdmoney.credit.bsyh.dao.pub.IRepayRecDetailDao;
import com.zdmoney.credit.bsyh.domain.GrantRecDetail;
import com.zdmoney.credit.bsyh.domain.RepayRecDetail;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.file.JschSftpUtils;
import com.zdmoney.credit.loan.domain.LoanBsbMapping;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.ILoanBsbMappingService;
import com.zdmoney.credit.loan.service.pub.ILoanRepaymentDetailService;
import com.zdmoney.credit.loan.service.pub.ILoanSpecialRepaymentService;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.system.service.pub.ISendMailService;
import com.zdmoney.credit.system.service.pub.ISequencesService;
import com.zdmoney.credit.system.service.pub.ISysParamDefineService;

/**
 * 包商银行对账job
 * @author YM10104
 *
 */
@Service
public class ReconciliationBsyhJob {

    private static final Logger logger = Logger.getLogger(ReconciliationBsyhJob.class);

    @Autowired
    private ILoanSpecialRepaymentService loanSpecialRepaymentService;
    @Autowired
    IVLoanInfoService VLoanInfoService;
    @Autowired
    ILoanBsbMappingService loanBsbMappingService;
    @Autowired
    ISequencesService sequencesServiceImpl;
    @Autowired
    IGrantRecDetailDao grantRecDetailDao;
    @Autowired
    ILoanRepaymentDetailService loanRepaymentDetailService;
    @Autowired
    IRepayRecDetailDao repayRecDetailDao;
    @Autowired
	private ISendMailService sendMailService;
    @Autowired
    private ISysParamDefineService sysParamDefineService;
    @Value("${bsyh.sftp.ip}")
    public String sftpIpBsyh;
    @Value("${bsyh.sftp.port}")
    public int sftpPortBsyh;
    @Value("${bsyh.sftp.name}")
    public String sftpNameBsyh;
    @Value("${bsyh.sftp.pwd}")
    public String sftpPwdBsyh;
    /**
     * 放款对账 每天早上六点
     */
    public void grantReconciliation() {
    	JschSftpUtils jschSftpUtils=new JschSftpUtils(sftpIpBsyh, sftpPortBsyh, 30000, sftpNameBsyh,sftpPwdBsyh);
    	String date = Dates.getDateTime(Dates.getBeforeDays(1), "yyyyMMdd");
    	String downloadFile ="0101020011"+"_ORDER_"+date+".txt";//文件名
    	String dirPath="/home/duizhang/zhengda/0101020011"+"/"+date.substring(0,6)+"/"+date;//文件目录
    	try {
    		boolean isLog = jschSftpUtils.login();
    		if(isLog){
    			boolean flag = jschSftpUtils.existFile(dirPath,downloadFile);//是否存在文件
    			if(flag){
    				InputStream is = jschSftpUtils.downloadToIs(dirPath, downloadFile);
    				List<String> lines = IOUtils.readLines(is, "utf-8");
    				String failLoanIds="";//放款对账失败的loanid
    				String orderNos="";//证大少帐的OrderNo
    				if(null!=lines && lines.size()>0){
    					for(String line:lines){
    						logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆放款对账文件行："+line);
    						String[] str = line.split("\\u007F\\u005E");
    						GrantRecDetail grantRecDetail=new GrantRecDetail();
    						grantRecDetail.setId(sequencesServiceImpl.getSequences(SequencesEnum.GRANT_REC_DETAIL));
    						grantRecDetail.setOrderNo(str[0]);
    						grantRecDetail.setChanlNo(str[1]);
    						grantRecDetail.setAmt(new BigDecimal(str[2]));
    						grantRecDetail.setGrantDate(Dates.parse(str[3], "yyyyMMdd"));
    						grantRecDetail.setTotalTerm(Long.valueOf(str[4]));
    						grantRecDetail.setProductNo(str[5]);
    						grantRecDetail.setLatestRepayDate(Dates.parse(str[6], "yyyyMMdd"));
    						grantRecDetail.setGrantMoney(new BigDecimal(str[7]));
    						grantRecDetail.setCreateDate(Dates.getNow());
    						grantRecDetail.setFileName(downloadFile);
    						//核对本金，放款日期，总期数，放款金额
    						LoanBsbMapping loanBsbMap = loanBsbMappingService.getByOrderNo(grantRecDetail.getOrderNo());
    						if(null==loanBsbMap){//证大没有此帐
    							logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆放款证大少帐："+grantRecDetail.getOrderNo());
    							grantRecDetail.setState("f");//对账失败
    							orderNos = orderNos+String.valueOf(grantRecDetail.getOrderNo())+",";
    						}else{
    							Long loanId = loanBsbMap.getLoanId();
    							VLoanInfo vLoanInfo = VLoanInfoService.findByLoanId(loanId);
    							BigDecimal grantMoney = vLoanInfo.getPactMoney().setScale(2);//放款金额
    							String grantMoneyDate = Dates.getDateTime(vLoanInfo.getGrantMoneyDate(), "yyyyMMdd") ;//放款日期
    							Long totalTerm = vLoanInfo.getTime();//总期数
    							BigDecimal money = vLoanInfo.getPactMoney().setScale(2);//本金,审批金额
    							if(grantMoney.compareTo(grantRecDetail.getGrantMoney())==0 && grantMoneyDate.equals(Dates.getDateTime(grantRecDetail.getGrantDate(), "yyyyMMdd")) && 
    									totalTerm==grantRecDetail.getTotalTerm() && money.compareTo(grantRecDetail.getAmt())==0){
    								grantRecDetail.setState("t");//对账成功    	
    								logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆放款对账成功。loanId:"+loanId);
    							}else{
    								logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆放款对账失败：loanId:"+loanId+",放款金额："+grantMoney+"-"+grantRecDetail.getGrantMoney()+"。放款日期:"+grantMoneyDate+"-"+Dates.getDateTime(grantRecDetail.getGrantDate(), "yyyyMMdd")
    										+"。总期数:"+totalTerm+"-"+grantRecDetail.getTotalTerm()+"。本金,审批金额:"+money+"-"+grantRecDetail.getAmt());
    								grantRecDetail.setState("f");//对账失败
    								failLoanIds = failLoanIds+String.valueOf(loanId)+","; 
    							}
    						}   					
    						grantRecDetailDao.insert(grantRecDetail);   						
    					}    					
    				}else{
    					logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆放款对账文件内容不存在");
    				}
    				//将对账失败的发送邮件通知相关人员
    				if(""!=failLoanIds || ""!=orderNos){//有失败的对账，发送邮件
    					String msg="以下债权与包银放款对账失败：借款id:"+failLoanIds+"请尽快核实。\n"+"以下债权与包银少帐：包银借据号："+orderNos+"请尽快核实。";//内容
    					String toEmail = sysParamDefineService.getSysParamValueCache("bsyh", "bsyh.email.sendResult.to");
    					String cc=sysParamDefineService.getSysParamValueCache("bsyh", "bsyh.email.sendResult.cc");//抄送
    					sendMailService.sendPaymentRiskMail(toEmail, cc, "包银放款对账失败通知", msg);
    				}
    			}else{
    				logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆放款对账文件不存在");
    			}   			
    		}else{
    			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆包银登录sftp服务器失败");
    		}
    	} catch (Exception e) {
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆获取包银放款对账文件异常" + e.getMessage(),e);
		}finally {
            jschSftpUtils.logout();
        }
    }
       
   /**
    * 还款对账  每天早上六点
    */
    public void repayReconciliation() {
    	JschSftpUtils jschSftpUtils=new JschSftpUtils(sftpIpBsyh, sftpPortBsyh, 30000, sftpNameBsyh,sftpPwdBsyh);
    	String date = Dates.getDateTime(Dates.getBeforeDays(1), "yyyyMMdd");
    	String downloadFile ="0101020011"+"_REPAY_"+date+".txt";//文件名
    	String dirPath="/home/duizhang/zhengda/0101020011"+"/"+date.substring(0,6)+"/"+date;
    	try {
    		boolean isLog = jschSftpUtils.login();
    		if(isLog){
    			boolean flag = jschSftpUtils.existFile(dirPath,downloadFile);//是否存在文件
        		if(flag){
        			InputStream is = jschSftpUtils.downloadToIs(dirPath, downloadFile);
        			List<String> lines = IOUtils.readLines(is, "utf-8");
        			if(null!=lines && lines.size()>0){
        				for(String line:lines){
        					logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆还款对账文件行："+line);
        					String[] str = line.split("\\u007F\\u005E");
        					RepayRecDetail repayRecDetail=new RepayRecDetail();
        					repayRecDetail.setId(sequencesServiceImpl.getSequences(SequencesEnum.GRANT_REC_DETAIL));
        					repayRecDetail.setOrderNo(str[0]);
        					repayRecDetail.setChanlNo(str[1]);
        					repayRecDetail.setTerm(Long.parseLong(str[2]));
        					repayRecDetail.setTradeAmt(new BigDecimal(str[3]));
        					repayRecDetail.setTotalInterest(new BigDecimal(str[4]));
        					repayRecDetail.setTotalFeeAmt(new BigDecimal(str[5]));
        					repayRecDetail.setRepayType(str[6]);
        					repayRecDetail.setTradeDate(Dates.parse(str[7], "yyyyMMdd"));   				
        					repayRecDetail.setCreateDate(Dates.getNow());
        					repayRecDetail.setFileName(downloadFile);    				
        					repayRecDetailDao.insert(repayRecDetail);
        					logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆还款对账结束："+line);
        				}        				
        			}else{
        				logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆还款对账文件内容为空");
        			}
        		}else{
        			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆还款对账文件不存在");
        		}
    		}else{
    			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆包银登录sftp服务器失败");
    		}  		
    	} catch (Exception e) {			
			logger.info("☆☆☆☆☆☆☆☆☆☆☆☆☆获取还款对账文件异常" + e.getMessage(),e);
		}finally {
            jschSftpUtils.logout();
        }
    }
}
