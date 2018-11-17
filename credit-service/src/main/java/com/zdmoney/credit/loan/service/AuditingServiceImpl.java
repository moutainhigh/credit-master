package com.zdmoney.credit.loan.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.LoanFlowStateEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.loan.dao.pub.ILoanInitialInfoDao;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.domain.LoanInitialInfo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.loan.service.pub.IAuditingService;

/**
 * 功能：对于一个借款的流程控制
 * @author 00232949
 *
 */
@Service
public class AuditingServiceImpl implements IAuditingService{

	@Autowired
	private ILoanBaseDao loanBaseDao;
	@Autowired
	private ILoanInitialInfoDao loanInitialInfoDao;
	
	
	@Override
	public void setNextFlowState(VLoanInfo vloan,boolean toAdvanced) {
		LoanBase loan = loanBaseDao.findByLoanId(vloan.getId());
		LoanInitialInfo loanInitialInfo = loanInitialInfoDao.findByLoanId(loan.getId());
		
		if(loan.getLoanFlowState().equals(LoanFlowStateEnum.申请.getValue()) 
				|| loan.getLoanFlowState().equals(LoanFlowStateEnum.交件退回.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.交件审核.getValue());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.交件审核.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.审核.getValue());
            //add by zhanghao on 自动分配审核人员
//            submitAutoAssign(loan,null);
		}else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.审核.getValue())){
			if(toAdvanced){
				loan.setLoanFlowState(LoanFlowStateEnum.高级审批.getValue());
			} else{
		        loan.setLoanFlowState(LoanFlowStateEnum.中心经理审批.getValue());
		    }
		}else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.中心经理审批.getValue())){
			if(vloan.getMoney().compareTo(new BigDecimal(80000))<=0) {
				
                if(toAdvanced) {
                	loan.setLoanFlowState(LoanFlowStateEnum.高级审批.getValue());
                }
                else{
                    loan.setLoanFlowState(LoanFlowStateEnum.合同签订.getValue());
                    loanInitialInfo.setAuditDate( new Date());
                    loanInitialInfo.setApproveMoney(vloan.getMoney());
                    loanInitialInfo.setApproveTime(vloan.getTime());
                }
            } else loan.setLoanFlowState(LoanFlowStateEnum.高级审批.getValue());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.高级审批.getValue())){
			if(vloan.getMoney().compareTo(new BigDecimal(80000))<=0) {
				
				loan.setLoanFlowState(LoanFlowStateEnum.合同签订.getValue());
                loanInitialInfo.setAuditDate( new Date());
                loanInitialInfo.setApproveMoney(vloan.getMoney());
                loanInitialInfo.setApproveTime(vloan.getTime());
		                
            } else{
            	loan.setLoanFlowState(LoanFlowStateEnum.高级协审.getValue());
            }
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.高级协审.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.合同签订.getValue());
            loanInitialInfo.setAuditDate( new Date());
            loanInitialInfo.setApproveMoney(vloan.getMoney());
            loanInitialInfo.setApproveTime(vloan.getTime());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.合同签订.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.合同确认.getValue());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.合同确认.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.财务审核.getValue());
			loanInitialInfo.setGrantMoneyDate(new Date());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.财务审核.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.财务放款.getValue());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.财务放款.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.正常.getValue());
		}
		
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.审核退回.getValue())
				||loan.getLoanFlowState().equals(LoanFlowStateEnum.审核退回.getValue())
				||loan.getLoanFlowState().equals(LoanFlowStateEnum.审批退回.getValue())
				||loan.getLoanFlowState().equals(LoanFlowStateEnum.高级审批退回.getValue())
				||loan.getLoanFlowState().equals(LoanFlowStateEnum.高级协审退回.getValue())){
			
			loan.setLoanFlowState(LoanFlowStateEnum.审核.getValue());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.审批回退.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.中心经理审批.getValue());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.高审回退审核.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.高级审批.getValue());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.高级协审回退审核.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.高级协审.getValue());
		}
		
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.网点经理退回.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.合同确认.getValue());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.财务审核退回.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.财务审核.getValue());
            if (loan.getFundsSources().equals("挖财2")){
            	//TODO:此处无这个字段
//            	loanInitialInfo.batchNum = "";
            }
		}
		
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.财务审核回退.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.财务审核.getValue());
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.财务放款退回.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.财务放款.getValue());
			if (loan.getFundsSources().equals("挖财2")){
            	//TODO:此处无这个字段
//            	loanInitialInfo.batchNum = "";
            }
		}
		else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.财务放款回退.getValue())){
			loan.setLoanFlowState(LoanFlowStateEnum.财务放款.getValue());
		}
		setLoanState(loan);
		
		vloan.setLoanState(loan.getLoanState());
		vloan.setLoanFlowState(loan.getLoanFlowState());
		
		loanBaseDao.update(loan);
		loanInitialInfoDao.update(loanInitialInfo);
	}
	
	 /**
     * 初审自动分配(因这部分功能已近在征审系统，这里不再实现)
     * @param loan
     */
	private void submitAutoAssign(LoanBase loan, Object object) {
		 /*boolean isRun =false;
	        try {
	            if (!loan.assessor) {
	                isRun = synLock(application)
	                if (isRun) {
	                    def isChannel2 = false;
	                    if(loan.salesDepartment.departmentType.equals(SalesDepartmentType.渠道2)){isChannel2=true}
	                    firstTrialAutoAssign(loan,isChannel2);
	                } else {
	                    this.firstTempAssign(loan, null)
	                }
	            }else{
	                def employee = Employee.get(loan.assessor)
	                if(employee.dempLevel){
	                    loan.empLevel = employee.dempLevel
	                    loan.save()
	                }
	            }
	        } catch (Exception e) {
	            System.out.println("自动分单出错" + e)
	            logger.error("自动分单出错", e)
	        }finally{
	            if(isRun){
	                application.setAttribute("synchronizeAutoAssign",true)
	            }
	        }
		*/
	}
	
	//根据流程状态设置借款状态
    private void setLoanState(LoanBase loan){
    	if(loan.getLoanFlowState().equals(LoanFlowStateEnum.申请.getValue())){
    		loan.setLoanState(LoanStateEnum.申请.getValue());
    	}
    	else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.交件审核.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.审核.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.中心经理审批.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.审批回退.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.高级审批.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.高审回退审核.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.高级协审.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.高级协审回退审核.getValue())){
    		loan.setLoanState(LoanStateEnum.审核中.getValue());
    	}
    	else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.交件退回.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.审核退回.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.审批退回.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.高级审批退回.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.高级协审退回.getValue())){
    		loan.setLoanState(LoanStateEnum.退回.getValue());
    	}
    	else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.合同签订.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.合同确认.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.网点经理退回.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.财务审核.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.财务审核退回.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.财务审核回退.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.财务放款.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.财务放款退回.getValue())
    			||loan.getLoanFlowState().equals(LoanFlowStateEnum.财务放款回退.getValue())){
    		loan.setLoanState(LoanStateEnum.通过.getValue());
    	}
    	else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.正常.getValue())){
    		loan.setLoanState(LoanStateEnum.正常.getValue());
    	}
    	else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.拒绝.getValue())){
    		loan.setLoanState(LoanStateEnum.拒绝.getValue());
    	}
    	else if(loan.getLoanFlowState().equals(LoanFlowStateEnum.取消.getValue())){
    		loan.setLoanState(LoanStateEnum.取消.getValue());
    	}
    	
    }
}
