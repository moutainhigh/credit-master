package com.zdmoney.credit.system.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.person.vo.PersonVo;
import com.zdmoney.credit.system.domain.PersonInfo;

/**
 * 借款人信息
 * 
 * @author 00232949
 *
 */
public interface IPersonInfoService {

    /**
     * 根据id查找
     * 
     * @param borrowerId
     * @return
     */
    public PersonInfo findById(Long borrowerId);

    /**
     * 带分页查询
     * 
     * @param personInfo
     *            条件实体对象
     * @return
     */
    public Pager findWithPg(PersonInfo personInfo);

    /**
     * 更新客户资料信息（基本信息、车辆信息、房屋信息、企业信息、手机和地址变更记录）
     * 
     * @param personInfo
     * @return
     */
    public void saveOrUpdate(PersonInfo personInfo);

    /**
     * 
     * 更新客户基本信息
     * 
     * @param personInfo
     */
    public void updatePersonInfo(PersonInfo personInfo);
    
    /**
     * 
     * 跟據姓名+身份證號查詢
     * 
     * @param name
     * @param idNum
     */
    public PersonInfo findByIdNumAndName(String name,String idNum);
    
	
	/**
	 * 核心接口中保存或者更新客户信息
	 * @param personInfo 
	 * @return
	 */
	public PersonInfo saveOrUpdateCore(PersonInfo personInfo);
	 /**
	  * 查找离职客户经理下的客户信息
	  * @return
	  */
	 public List<PersonVo> findLeaveOfficeEmployee(Map<String, Object> paramMap);

	/**
	 * 龙信小贷 -查找客户信息
	 * @param customerParams
	 * @return
	 */
	public List<Map<String, Object>> getCustomerInfo4LXXD(Map<String, Object> customerParams);
}
