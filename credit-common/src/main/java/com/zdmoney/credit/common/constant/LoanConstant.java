package com.zdmoney.credit.common.constant;

/**
 * 所有的枚举定义
 * @author 00232949
 *
 */
//	enum LoanState {申请,审核中,通过,拒绝,退回,取消,正常,逾期,结清,坏账,预结清,正常费用减免}
//	enum LoanFlowState{申请,交件审核,交件退回,审核任务分配,审核,审核退回,审核回退,审核拒绝,中心经理审批,审批退回,审批回退,
//	高级审批,高级审批退回,高审回退审核,高级协审,高级协审退回,高级协审回退审核,
//	风控经理审批,风控审批退回, 风控经理回退,总助审批,总助审批退回,总助回退,总经理审批,总经理审批退回,总经理回退,
//	合同签订,合同确认,网点经理退回,财务审核,财务审核退回,财务审核回退,财务放款,财务放款退回,财务放款回退,
//	正常,拒绝, 取消,恢复签约,重新提交,复议申请,复议批复,区域总审批,信贷综合管理部审批,信贷综合管理部经理审批,分管领导审批}
	
	enum LoanType{随薪贷,随意贷,随房贷,助学贷,车贷,薪生贷,随车贷,随房贷A,随房贷B,公积金贷,保单贷,网购达人贷A,网购达人贷B,淘宝商户贷,学历贷,卡友贷}
	enum LoanTypeReport{随薪贷,随意贷,随意贷A,随意贷B,随意贷C,随房贷,助学贷,车贷,薪生贷,随车贷,随房贷A,随房贷B,公积金贷,保单贷,网购达人贷A,网购达人贷B,淘宝商户贷,学历贷,卡友贷}
	enum RedemptionType{证大年丰,证大月收,证大季喜,证大岁悦,证大月投}
	enum RepaymentState {未还款,结清,不足额还款,部分免息结清,不足罚息}   //暂不记录逾期状态，凡是还款日期在当前日期之前的，均属于逾期
	enum SalesDepartmentType{个贷,渠道,互联网,电销,渠道2}
	enum PlanState{申请,提交,通过,退回,作废}
	enum ReadAndWrite{read,write}

	//
	enum TradeType{现金,转账,通联代扣,富友代扣,上海银联代扣,冲正补记,冲正,挂账,保证金,系统使用保证金,风险金};//交易类型 1-现金 2-转账 3-通联代扣 4-冲正补记 5-冲正…
	enum TradeKind{正常交易,冲正交易} //交易种类

//	enum OfferState{未报盘,已报盘,已回盘}
//	enum OrCurrencyType{CNY}
//	enum OfferType{自动划扣,实时划扣}
	enum BusiType{}
	enum IsEdit{是,否}
	enum IsSuc{成功,失败}
//	enum SpecialRepaymentType{一次性还款, 减免,提前扣款,结算单,正常费用减免 }
//	enum SpecialRepaymentState{申请,审批,通过,拒绝,取消,结束,申请结算单,撤销结算单,结算单已打印,区域总审批,信贷综合管理部审批,信贷综合管理部经理审批,分管领导审批,总经理审批}
	enum ReportRepaymentState{提前还清,正常结清,逾期结清,预结清}
//	enum MessageState{已读,未读,已删除}
	enum MessageType{系统通知,信件}
	enum LogLevel{正常,错误,警告}
	enum LogType{变更,新增,删除,其它}
	enum OrgType{学校,车行}
	enum CaseState{客服催收中,未分派,部门催收中,作业完成_未全部收回,作业完成_全部收回,结案_全部收回,结案_坏账} //案件状态
	enum TaskState{进行中,正常移交,异常移交,完成_未全部收回,完成_全部收回}
	enum CaseFrom{系统生成,手工创建}//案件来源
	enum RecordType{电话,外访}//  催收记录类型
	enum TaskType{客服催收,部门催收}
	enum AddressType{家庭,公司,其它}
	enum TelType{家庭电话,手机,公司电话,传真,其它}
	enum Priority{高,中,低}

	enum PreditInfoType{信用卡,信用贷款,房贷,车贷}

	enum FlowType{个人流水,对公流水}
	enum HouseType{商品房,非商品房}

	enum PreditResult{通过,拒绝,建议拒绝}

	enum SuspendState{正常,挂起}

	enum HighState{签订回退,终审回退,门店重提,普通申请}//初审优先级，签订回退>终审回退>门店重提>普通申请

	enum NFCSType{贷后管理}

	enum DepLevel {A,B,C,D,O}
	enum DempLevel {A,B,C,D,O}
//	enum FundsSourcesType {证大P2P, 证大爱特, 证大爱特2, 积木盒子, 向上360, 华澳信托, 国民信托}
//	enum TrustGrantState {融资成功, 融资失败, 融资处理中} //信托融资放款状态

	enum RepayType{onetime,normal}
	enum SpecialRepaymentTypeCore{onetime, reduction, advanceDeduct}
	enum SpecialRepaymentStateCore{application,cancel,over}
	enum IsSucCore{succ,fail}
	enum OfferStateCore{unreport,reported,returned}
