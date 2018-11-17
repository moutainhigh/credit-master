package com.zdmoney.credit.repay.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.AbsUploadStatusEnum;
import com.zdmoney.credit.common.constant.GatewayConstant;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.common.exception.PlatformException;
import com.zdmoney.credit.common.exception.ResponseEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.framework.vo.abs.entity.DeduteResultEntity;
import com.zdmoney.credit.framework.vo.abs.input.Abs100008Vo;
import com.zdmoney.credit.repay.dao.pub.IAbsDeductResultUploadDao;
import com.zdmoney.credit.repay.domain.AbsDeductResultUpload;
import com.zdmoney.credit.repay.service.pub.IAbsDeductResultUploadService;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class AbsDeductResultUploadServiceImpl implements IAbsDeductResultUploadService {
	
	private Logger logger = Logger.getLogger(AbsDeductResultUploadServiceImpl.class);

	@Autowired
	private IAbsDeductResultUploadDao absDeductResultUploadDao;
	@Autowired
	private ISequencesService sequencesServiceImpl;
	@Override
	public List<AbsDeductResultUpload> getDeductResutList2Upload(Map<String, Object> params) {		
		return absDeductResultUploadDao.findDeductResutListByMap(params);
	}

	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveDeductResultList(List<AbsDeductResultUpload> list) {
		try{
			if(list != null && list.size() > 0){
				for(AbsDeductResultUpload absDeductResultUpload : list){
					absDeductResultUpload.setId(sequencesServiceImpl.getSequences(SequencesEnum.ABS_DEDUCT_RESULT_UPLOAD));
					absDeductResultUpload.setBatNo("");
					String id = absDeductResultUpload.getId().toString();
					String serialId = Dates.getDateTime("yyyyMMdd") + Strings.getFileSeq4Title(id, 12);
					absDeductResultUpload.setSerialNo(serialId);
					absDeductResultUpload.setStatus(AbsUploadStatusEnum.待上传.name());
					absDeductResultUploadDao.insert(absDeductResultUpload);
				}
			}
		}catch(Exception e){
			logger.error("插入证大扣款信息报错...",e);
		}
	}

	@Override
	public List<AbsDeductResultUpload> getDeductResutListByStatus(Map<String, Object> map) {
		List<AbsDeductResultUpload> list = absDeductResultUploadDao.findListByMap(map);
		return list;
	}

	@Override
	public void uploadDeductList2Abs(List<AbsDeductResultUpload> uploadList) {
		try{
			if(CollectionUtils.isEmpty(uploadList)){
				logger.warn("扣款明细信息不存在，上传任务结束。");
				return;
			}
			Abs100008Vo absVo = new Abs100008Vo();
			List<DeduteResultEntity> entityList = new ArrayList<DeduteResultEntity>();
			String batNo = "BAT"+ Dates.getDateTime("yyyyMMddHHmmssSSS");
			for(AbsDeductResultUpload absDeductResultUpload : uploadList){
				absDeductResultUpload.setBatNo(batNo);
				absDeductResultUploadDao.update(absDeductResultUpload);
				DeduteResultEntity entity = new DeduteResultEntity();
				BeanUtils.copyProperties(entity, absDeductResultUpload);
				entityList.add(entity);
			}
			absVo.setBatNo(batNo);
			absVo.setSysSource(GatewayConstant.SYS_SOURCE);
			absVo.setProjNo(GatewayConstant.PROJ_NO);
			absVo.setListDeduteResult(entityList);
			// 调用数信扣款结果上传接口
			JSONObject jsonObject = GatewayUtils.callCateWayInterface(absVo, GatewayFuncIdEnum.扣款结果上传.getCode());
			if(!GatewayUtils.checkResultStatus(jsonObject)){
				throw new PlatformException(ResponseEnum.FULL_MSG, "数信方响应,接口调用失败。");
			}
			JSONArray jsonArray = GatewayUtils.getErrJSONArray(jsonObject);
			Map<String, String> errMap = new HashMap<String, String>();
			if(jsonArray != null && jsonArray.size() > 0){
				for(int i=0; i<jsonArray.size(); i++){
					JSONObject jsonErr = jsonArray.getJSONObject(i);
					errMap.put(jsonErr.getString("pactNo"), jsonErr.getString("dealDesc"));
				}
			}
			for(AbsDeductResultUpload absDeductResultUpload : uploadList){
				if(errMap.containsKey(absDeductResultUpload.getPactNo())){
					absDeductResultUpload.setStatus(AbsUploadStatusEnum.失败.name());
					absDeductResultUpload.setErrDesc(errMap.get(absDeductResultUpload.getPactNo()));
				}else{
					absDeductResultUpload.setStatus(AbsUploadStatusEnum.成功.name());
					absDeductResultUpload.setErrDesc("上传成功");
				}
				absDeductResultUploadDao.update(absDeductResultUpload);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("上传扣款结果失败..."+e.getMessage());
		}
	}
}
