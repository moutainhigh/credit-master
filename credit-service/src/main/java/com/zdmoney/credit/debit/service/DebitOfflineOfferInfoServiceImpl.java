package com.zdmoney.credit.debit.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.OfferTransactionStateEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.constant.wm.WMDebitDeductStateEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.BeanUtils;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.framework.vo.common.BaseParamVo;
import com.zdmoney.credit.framework.vo.wm3.entity.DeductMoneyDetailListEntity;
import com.zdmoney.credit.framework.vo.wm3.entity.PaidInMoneyListEntity;
import com.zdmoney.credit.framework.vo.wm3.input.WM3_2312Vo;
import com.zdmoney.credit.framework.vo.wm3.output.WM3_2312OutputVo;
import com.zdmoney.credit.system.service.pub.ISequencesService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.debit.dao.pub.IDebitOfflineOfferInfoDao;
import com.zdmoney.credit.debit.domain.DebitOfflineOfferInfo;
import com.zdmoney.credit.debit.service.pub.IDebitOfflineOfferInfoService;


@Service
public class DebitOfflineOfferInfoServiceImpl implements IDebitOfflineOfferInfoService {

	private static final Logger logger = Logger.getLogger(DebitOfflineOfferInfoServiceImpl.class);

	@Autowired
	private IDebitOfflineOfferInfoDao debitOfflineOfferInfoDao;
	
	@Autowired
	private ISequencesService sequencesService;
	
	@Override
	public List<DebitOfflineOfferInfo> findDistinctBatNoList(Map<String,Object> map) {
		return debitOfflineOfferInfoDao.findDistinctBatNoList(map);
	}

	@Override
	public DebitOfflineOfferInfo createDebitOfflineOfferInfo(DebitOfflineOfferInfo debitOfflineOfferInfo) {
		debitOfflineOfferInfo.setId(sequencesService.getSequences(SequencesEnum.DEBIT_OFFLINE_OFFER_INFO));
		String batNo = "WM3_B_" + Dates.getDateTime("yyyyMMddHHmmss") + debitOfflineOfferInfo.getId();
		debitOfflineOfferInfo.setBatNo(batNo);
		return debitOfflineOfferInfoDao.insert(debitOfflineOfferInfo);
	}

	@Override
	public List<DebitOfflineOfferInfo> findListByMap(Map<String, Object> map) {
		return debitOfflineOfferInfoDao.findListByMap(map);
	}

	@Override
	public void pushOfflineDebit4WM3() {
		Map<String,Object> params = new HashMap<>();
		params.put("states", new String[]{WMDebitDeductStateEnum.未发送.getValue(), WMDebitDeductStateEnum.扣款失败.getValue()});
		List<DebitOfflineOfferInfo> list = debitOfflineOfferInfoDao.findListByMap(params);
		if(CollectionUtils.isNotEmpty(list)){
			for(DebitOfflineOfferInfo debitOfflineOfferInfo : list){
				callOfflineDebitPort(debitOfflineOfferInfo);
			}
		}
	}

	/**
	 * 调用外贸3 线下实收WM3_2312Vo
	 * @param debitOfflineOfferInfo
	 */
	private void callOfflineDebitPort(DebitOfflineOfferInfo debitOfflineOfferInfo) {
		WM3_2312Vo vo = new WM3_2312Vo();
		vo.setBrNo(BaseParamVo.SYS_SOURCE_WM3);//合作机构号
		vo.setBatNo(debitOfflineOfferInfo.getBatNo());//专批次号
		vo.setBankNo(debitOfflineOfferInfo.getBankNo());//专户银行流水号  N
		vo.setDataCnt("1");
		List<PaidInMoneyListEntity> list = new ArrayList<PaidInMoneyListEntity>();
		vo.setList(list);
		PaidInMoneyListEntity paidInMoney = new PaidInMoneyListEntity();
		list.add(paidInMoney);
		paidInMoney.setPactNo(debitOfflineOfferInfo.getPactNo());//合同号
		paidInMoney.setRepayType(debitOfflineOfferInfo.getRepyType());//实收类型
		paidInMoney.setRepayAmt(debitOfflineOfferInfo.getRepayAmt());//实收金额
		List<DeductMoneyDetailListEntity> deductMoneyList = new ArrayList<DeductMoneyDetailListEntity>();
		paidInMoney.setListSubj(deductMoneyList);
//		DeductMoneyDetailListEntity deductMoney = new DeductMoneyDetailListEntity();
//		deductMoney.setCnt(0); // N 期次   注释：没有填 0
//		deductMoneyList.add(deductMoney);
		JSONObject grantResut = null;
		try{
			grantResut =  GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.外贸3线下实收.getCode());
			String respCode = (String)grantResut.get("respCode");//外贸3返回的结果
			String respDesc = (String)grantResut.get("respDesc");//外贸3返回的描述
			if("0000".equals(respCode)){
				String content = (String)grantResut.get("content");
				JSONObject jsonObjectContent = JSON.parseObject(content);//String 类型
				logger.info("调用【外贸3】线下实收接口成功，返回信息："+respDesc);
				JSONArray jsonArray = jsonObjectContent.getJSONArray("list");//失败的记录 []数组类型
				if(jsonArray.size() > 0){
					debitOfflineOfferInfoDao.updateById(debitOfflineOfferInfo.getId(), WMDebitDeductStateEnum.扣款失败.getValue());
					return;
				}
				debitOfflineOfferInfoDao.updateById(debitOfflineOfferInfo.getId(), WMDebitDeductStateEnum.扣款成功.getValue());
				return;
			}else{
				logger.error("调用【外贸3】线下实收接口失败：原因："+respDesc);
				debitOfflineOfferInfoDao.updateById(debitOfflineOfferInfo.getId(), WMDebitDeductStateEnum.扣款失败.getValue());
			}
		}catch(Exception e){
			logger.error("【外贸3】线下实收接口调用网关接口失败："+e.getMessage(),e);
			debitOfflineOfferInfoDao.updateById(debitOfflineOfferInfo.getId(), WMDebitDeductStateEnum.扣款失败.getValue());
		}
	}
}
