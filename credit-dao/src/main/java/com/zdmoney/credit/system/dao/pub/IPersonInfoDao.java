package com.zdmoney.credit.system.dao.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.person.vo.PersonVo;
import com.zdmoney.credit.system.domain.PersonInfo;

public interface IPersonInfoDao extends IBaseDao<PersonInfo> {

    /**
     * 根据身份证号查找
     * 
     * @param idnum
     * @return
     */
    public PersonInfo findByIdnum(String idnum);
    
    /**
     * 通过身份证 和姓名查找
     * @param paramMap
     * @return
     */
    public PersonInfo findPerson(Map<String, String> paramMap);
    
    /**
     * 查找离职客户经理下的客户
     */
    public List<PersonVo> findLeaveOfficeEmployee(Map<String, Object> paramMap);

	/**
	 * 龙信小贷 查找客户信息
	 * @param paramMap
	 * @return
	 */
	public List<Map<String, Object>> getCustomerInfo4LXXD(Map<String, Object> paramMap);
}
