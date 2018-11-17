package com.zdmoney.credit.loan.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.constant.FundsSourcesTypeEnum;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.LoanStateEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2501Vo;
import com.zdmoney.credit.loan.dao.pub.ILoanBaseDao;
import com.zdmoney.credit.system.dao.pub.IPersonInfoDao;
import com.zdmoney.credit.system.domain.PersonInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.loan.dao.pub.ILoanBankDao;
import com.zdmoney.credit.loan.domain.LoanBank;
import com.zdmoney.credit.loan.domain.LoanBase;
import com.zdmoney.credit.loan.service.pub.ILoanBankService;
import com.zdmoney.credit.offer.dao.pub.IOfferBankDicDao;
import com.zdmoney.credit.offer.domain.OfferBankDic;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class LoanBankServiceImpl implements ILoanBankService{
	
	@Autowired
	ILoanBankDao loanBankDao;
	
	@Autowired @Qualifier("offerBankDicDaoImpl")
	IOfferBankDicDao offerBankDicDaoImpl;
	@Autowired
	ISequencesService sequencesService;
	@Autowired
	ILoanBaseDao loanBaseDao;
	@Autowired
	IPersonInfoDao personInfoDao;
	@Override
	public LoanBank findById(Long giveBackBankId) {
		return loanBankDao.get(giveBackBankId);
	}
	
	/**
	 * 跟据Map 查询结果集
	 * @param paramMap 查询参数
	 * @return
	 */
	@Override
	public Pager findWithPgByMap(Map<String,Object> paramMap) {
		return loanBankDao.findWithPgByMap(paramMap);
	}
	
	/**
	 * 新增或更新操作
	 * @param loanBank
	 * @return
	 */
	@Override
	@Transactional
	public LoanBank saveOrUpdate(LoanBank loanBank) {
		Long id = loanBank.getId();
		if (Strings.isEmpty(id)) {
			/** 新增操作 **/
		} else {
			/** 更新操作 **/
			/** 跟据总行代码，查询名字 **/
			String bankCode = loanBank.getBankCode();
			if (Strings.isEmpty(bankCode)) {
				/** 缺少总行代码参数 **/
				throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,new Object[]{"总行代码"});
			}
			OfferBankDic offerBankDic = new OfferBankDic();
			offerBankDic.setCode(bankCode);
			offerBankDic = offerBankDicDaoImpl.get(offerBankDic);
			if (offerBankDic == null) {
				throw new PlatformException(ResponseEnum.VALIDATE_ISNULL,new Object[]{"总行代码:" + bankCode});
			}
			loanBank.setBankName(offerBankDic.getName());
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("givebackBankId",id);
			List<LoanBase> loanBaseList = loanBaseDao.findListByMap(params);
			List<LoanBase> updateList = new ArrayList<>();
			if(CollectionUtils.isNotEmpty(loanBaseList)){
				for(LoanBase lb : loanBaseList){
					if(FundsSourcesTypeEnum.外贸3.getValue().equals(lb.getFundsSources()) && !LoanStateEnum.结清.getValue().equals(lb.getLoanState())){
						updateList.add(lb);
					}
				}
			}
			if(updateList.size() > 0){
				for(LoanBase updateBase: updateList){
					sendUpdateBankInfo2WM3(loanBank, updateBase);
				}
			}

			loanBankDao.update(loanBank);
		}
		return loanBank;
	}

	/**
	 * 调用外贸3 更新银行信息接口
	 * @param loanBank
	 */
	public void sendUpdateBankInfo2WM3(LoanBank loanBank, LoanBase loanBase) {
		PersonInfo personInfo = personInfoDao.get(loanBase.getBorrowerId());
		//外贸3还款账户维护-删除原账户
		Map<String, Object> params = new HashMap<>();
		params.put("bankId",loanBank.getId());
		LoanBank oldLoanBank = loanBankDao.findLoanBankByWm3(params);
		WM3_2501Vo delVo = new WM3_2501Vo();
		delVo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
		delVo.setPactNo(loanBase.getContractNum());
		delVo.setOpType("2"); //2-删除
		delVo.setNewAcno(oldLoanBank.getAccount());
		delVo.setNewAcname(personInfo.getName());
		delVo.setNewAcbankno(oldLoanBank.getBankCode());
		delVo.setNewAcbankname(oldLoanBank.getBankName());
		delVo.setIdType("0"); //0-身份证
		delVo.setIdNo(personInfo.getIdnum());
		delVo.setIfAuth("0"); //是否有扣款授权书  1-是 0-否
		delVo.setAcType("11"); // 11-个人借记卡账户
		com.alibaba.fastjson.JSONObject res = GatewayUtils.callCateWayInterface(delVo, GatewayFuncIdEnum.外贸3还款账号单笔维护.getCode());
		GatewayUtils.getReponseContentJSONObject(res, false);

		//外贸3还款账户维护-新增账户
		Map<String, Object> newparams = new HashMap<>();
		newparams.put("bankCode",loanBank.getBankCode());
		LoanBank newLoanBank = loanBankDao.findLoanBankByWm3(newparams);
		WM3_2501Vo addVo = new WM3_2501Vo();
		addVo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);
		addVo.setPactNo(loanBase.getContractNum());
		addVo.setOpType("1"); //1-新增
		addVo.setNewAcno(loanBank.getAccount());
		addVo.setNewAcname(personInfo.getName());
		addVo.setNewAcbankno(newLoanBank.getBankCode());
		addVo.setNewAcbankname(loanBank.getBankName());
		addVo.setIdType("0"); //0-身份证
		addVo.setIdNo(personInfo.getIdnum());
		addVo.setIfAuth("1"); //是否有扣款授权书  1-是 0-否
		addVo.setAcType("11"); // 11-个人借记卡账户
		com.alibaba.fastjson.JSONObject addRes = GatewayUtils.callCateWayInterface(addVo, GatewayFuncIdEnum.外贸3还款账号单笔维护.getCode());
		GatewayUtils.getReponseContentJSONObject(addRes, false);
	}


	/**
	 * 核心接口中新增或更新操作
	 * @param loanBank
	 * @return
	 */
	@Override
	public LoanBank saveOrUpdateCore(LoanBank loanBank) {
		Long id = loanBank.getId();
		
		if (Strings.isEmpty(id)) {
			/**新增银行信息**/
			loanBank.setId(sequencesService.getSequences(SequencesEnum.LOAN_BANK));
			loanBankDao.insert(loanBank);
		} else {
			/**更新银行信息**/
			loanBankDao.update(loanBank);
		}
		return loanBank;
	}

	@Override
	public String findNumByLoanId(Long loanId) {
		// TODO Auto-generated method stub
		return loanBankDao.findNumByLoanId(loanId);
	}
	
	
}