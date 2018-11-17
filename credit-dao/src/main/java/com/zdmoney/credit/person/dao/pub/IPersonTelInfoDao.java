package com.zdmoney.credit.person.dao.pub;

import java.util.List;

import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.loan.vo.VloanPersonOrg;
import com.zdmoney.credit.person.domain.PersonTelInfo;

/**
 * 借款人号码变更记录Dao接口定义
 * @author Ivan
 *
 */
public interface IPersonTelInfoDao extends IBaseDao<PersonTelInfo>{
	/**
     * 通过手机号码查询
     * @param phone
     * @return
     */
    public List<PersonTelInfo> findByPhone(String phone);
    /**
	  * 通过借款人ID查找债权信息
	  * @param id
	  * @return
	  */
    public List<VloanPersonOrg> findByPersonId(Long id);
}
