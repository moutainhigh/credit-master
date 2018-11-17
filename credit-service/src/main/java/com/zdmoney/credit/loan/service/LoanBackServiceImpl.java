package com.zdmoney.credit.loan.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zdmoney.credit.common.constant.GatewayConstant;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.credit.common.constant.GatewayFuncIdEnum;
import com.zdmoney.credit.common.gateway.GatewayUtils;
import com.zdmoney.credit.common.util.CollectionUtils;
import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.framework.vo.abs.entity.BuyBackEntity;
import com.zdmoney.credit.framework.vo.abs.entity.ListErrEntity;
import com.zdmoney.credit.framework.vo.abs.input.Abs100006Vo;
import com.zdmoney.credit.framework.vo.abs.output.Abs100006OutputVo;
import com.zdmoney.credit.job.LoanBackUploadJob;
import com.zdmoney.credit.loan.dao.LoanBackDaoImpl;
import com.zdmoney.credit.loan.dao.pub.ILoanBackDao;
import com.zdmoney.credit.loan.domain.LoanBack;
import com.zdmoney.credit.loan.service.pub.ILoanBackService;
/**
 * 待回购信息Service封装层
 * @author user
 *
 */
@Service
public class LoanBackServiceImpl implements ILoanBackService {
	private static final Logger logger = Logger.getLogger(LoanBackServiceImpl.class);
	@Autowired
	@Qualifier("loanBackDaoImpl")
	private ILoanBackDao loanBackDaoImpl;
	
	/**
	 * 保存待回购信息
	 */
	@Override
	public LoanBack insert(LoanBack loanBack) {
		return loanBackDaoImpl.insert(loanBack);
	}

	/**
	 * 查询回购信息
	 */
	@Override
	public List<LoanBack> findListByVo(LoanBack loanBack) {
		return loanBackDaoImpl.findListByVo(loanBack);
	}

	/**
	 * 修改批次号
	 */
	@Override
	public int update(LoanBack _loanback) {
		return loanBackDaoImpl.update(_loanback);
	}



     /**
      * 回购信息上传
      */
	@Override
	public void uploadLoanBack() {
		String batno = "BLU"+Dates.getDateTime(new Date(), "yyyyMMddHHmmss");
	   	Abs100006Vo vo = new Abs100006Vo();
	   	List<BuyBackEntity> listBuyBack = new ArrayList<BuyBackEntity>();
	   	logger.info("查找上传信息.........");
	   	//查找已回购，未上传的回购信息
	   	LoanBack loanBack = new LoanBack();
	   	loanBack.setBbState(1);
	   	loanBack.setpState(0);
	   	List<LoanBack> listLoanBack = loanBackDaoImpl.findListByVo(loanBack);
	   	if(CollectionUtils.isEmpty(listLoanBack)){
	   		logger.info("没有未上传信息");
	   		return;
	   	}
	   	logger.info("封装上传信息.........");
	   	//将查询到的信息封装
	   	vo.setSysSource("A02");
		vo.setBatNo(batno);
	    vo.setProjNo(GatewayConstant.PROJ_NO);
	    vo.setRepDate(Dates.getDateTime(listLoanBack.get(0).getRepDate(), "yyyyMMdd"));
	    BigDecimal repAmt = new BigDecimal(0);
	    BigDecimal repIntst = new BigDecimal(0);
	    for(LoanBack _loanback : listLoanBack){
		    BuyBackEntity buyBack = new BuyBackEntity();
		    buyBack.setPactNo(_loanback.getPactNo());
		    buyBack.setRepAmt(_loanback.getRepAmt());
		    listBuyBack.add(buyBack);
		    repAmt = repAmt.add(_loanback.getRepAmt());
		    repIntst = repIntst.add(_loanback.getRepIntst());
	     }
	     vo.setListBuyBack(listBuyBack);
	     vo.setRepAmt(repAmt);
	     vo.setRepIntst(repIntst);
	     vo.setRepTotal(repAmt.add(repIntst));
	     logger.info("上传信息.........");
	     //发送请求
	     JSONObject jsonObject = GatewayUtils.callCateWayInterface(vo, GatewayFuncIdEnum.上传待回购信息.getCode());
	     if(!"0000".equals(jsonObject.getString("resCode"))){
	    	 logger.info("上传失败........");
	    	 return;
	     }
	     logger.info("接受响应数据........");
	     Abs100006OutputVo oVo = JSONObject.toJavaObject(jsonObject, Abs100006OutputVo.class);
	     logger.info("响应数据"+oVo.toString());
	     List<ListErrEntity> listErr = oVo.getListErr();
	     if(!listErr.isEmpty()){
	    	 //移除上传错误数据
		     for(ListErrEntity err:listErr){
		    	 for(LoanBack _loanback : listLoanBack){
		    		 if(_loanback.getPactNo()==err.getPactNo()){
		    			 listLoanBack.remove(_loanback);
		    		 }
		    	 }
		     }
	     }
	    //保存上传正确信息
	    for(LoanBack _loanback : listLoanBack){
		    	//保存批次号
				logger.info("遍历保存批次号........");
				_loanback.setBatNo(batno);
				//更改上传状态
				logger.info("遍历更改上传状态........");
				_loanback.setpState(1);
				loanBackDaoImpl.update(_loanback);
		}
	    logger.info("上传完成........");
	}
    
}
