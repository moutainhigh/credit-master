package com.zdmoney.credit.core.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.ParseException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zdmoney.credit.blackList.dao.pub.IBlackListDao;
import com.zdmoney.credit.blacklist.domain.ComBlacklist;
import com.zdmoney.credit.blacklist.domain.Customer;
import com.zdmoney.credit.common.util.Strings;
import com.zdmoney.credit.common.util.coreUtil.Constants;
import com.zdmoney.credit.common.util.coreUtil.JSONUtil;
import com.zdmoney.credit.common.util.coreUtil.MessageUtil;
import com.zdmoney.credit.common.vo.core.BlackListVo;
import com.zdmoney.credit.core.service.pub.IBlackListCoreService;
import com.zdmoney.credit.framework.controller.BaseController;
/**
 * 黑名相关接口
 * @author 
 *
 */
@Controller
@RequestMapping("/core/BlackListCore")
public class BlackListCoreController extends BaseController {
	
	private static final Logger logger = Logger.getLogger(BlackListCoreController.class);
	
	@Autowired
	IBlackListCoreService blackListCoreService;
	@Autowired
	IBlackListDao blackListDao;
	
	/**
	 * 用户黑名单获取接口
	 * @param request 请求信息对象
	 * @param response 响应信息对象
	 * @param params 封装请求参数
	 * @throws IOException
	 * @throws java.text.ParseException 
	 */
	@RequestMapping("/syncBlackCustomer")
	@ResponseBody
	public void syncBlackCustomer(HttpServletRequest request,HttpServletResponse response,BlackListVo params) throws IOException, java.text.ParseException {
		logger.info("syncBlackCustomer用户黑名单获取接口 :接收到的参数:"+params.toString());
		
		Map<String, Object> json = null;
        
		/**判断是否缺少必要参数**/
		if (params == null || Strings.isEmpty(params.getOrgan()) || Strings.isEmpty(params.getRiskTime())) {
            json = MessageUtil.returnErrorMessage("失败:缺少必要参数riskTime或organ");
            response.getWriter().write(JSONUtil.toJSON(json));
			return;
        }
		
        if(!"征审系统".equals(params.getOrgan())){
            json = MessageUtil.returnErrorMessage("失败:organ取值有误");
            response.getWriter().write(JSONUtil.toJSON(json));
			return;
        }
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.parse(params.getRiskTime());
        } catch (ParseException e) {
            json = MessageUtil.returnErrorMessage("失败:riskTime参数有误");
            response.getWriter().write(JSONUtil.toJSON(json));
			return;
        }

        List<Customer> customerAddList=new ArrayList<Customer>();
        List<ComBlacklist> blackListForCustomer = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date begin = sdf.parse(params.getRiskTime() + " 00:00:00");
            Date end = sdf.parse(params.getRiskTime() + " 23:59:59");
            Integer[] canSubmitRequestDays = {-1, 99999};
            
            blackListForCustomer = blackListDao.findAllByDateCreatedBetweenAndComeFromeNotEqual(begin, end, params.getOrgan(), canSubmitRequestDays);
        }catch(Exception e){
        	e.printStackTrace();
        	logger.error("syncBlackCustomer方法：失败:"+e.getMessage());
        }

        if(blackListForCustomer!=null ){
        	for(int i = 0;i < blackListForCustomer.size(); i++ ){
            Customer customer=new Customer();
            customer.setName(blackListForCustomer.get(i).getName());
            customer.setIdCard(blackListForCustomer.get(i).getIdnum());
            customer.setMobilePhone((blackListForCustomer.get(i).getMphone()!=null?blackListForCustomer.get(i).getMphone():""));
            customer.setTelePhone((blackListForCustomer.get(i).getTel()!=null?blackListForCustomer.get(i).getTel():""));
            customer.setWorkUnit((blackListForCustomer.get(i).getCompany()!=null?blackListForCustomer.get(i).getCompany():""));
            customer.setRiskCase((blackListForCustomer.get(i).getMemo()!=null?blackListForCustomer.get(i).getMemo():""));
            customer.setRiskTime(sdf.format(blackListForCustomer.get(i).getRejectDate()));
            customer.setInfoSource("信贷系统");
            customer.setCreateOrgan("证大投资咨询有限公司");
            customer.setCreator("信贷系统管理员");
            customerAddList.add(customer);
        	}
        }
        
		/* 接口入参校验   end */
        try {
        	json=blackListCoreService.syncBlackCustomerToJson(customerAddList);
		} catch (Exception e) {
			logger.error("syncBlackCustomer方法：失败:"+e);
			
			json = new HashMap<String, Object>();
			json.put("code",Constants.DATA_ERR_CODE);
			json.put("message","未知错误");
		}
		response.getWriter().write(JSONUtil.toJSON(json));
		return;

		
	}

}
