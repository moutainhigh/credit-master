package com.zdmoney.credit.system.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.person.vo.PersonVo;
import com.zdmoney.credit.system.dao.pub.IPersonInfoDao;
import com.zdmoney.credit.system.domain.PersonInfo;

@Repository
public class PersonInfoDaoImpl extends BaseDaoImpl<PersonInfo> implements IPersonInfoDao {

    /**
     * 数据库基本操作，对应*mapper.xml中的id
     */
    private static final String FINDBYIDNUM = ".findByIdnum";

    /**
     * 根据身份证号查找
     * 
     * @param idnum
     * @return
     */
    @Override
    public PersonInfo findByIdnum(String idnum) {
	return getSqlSession().selectOne(getIbatisMapperNameSpace() + FINDBYIDNUM, idnum);	 
    }
    
    /**
     * 通过身份证和名字查找债权人
     * @param paramMap
     * @return
     */
    public PersonInfo findPerson(Map<String, String> paramMap) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findPerson", paramMap);
	}
    
    @Override
	public List<PersonVo> findLeaveOfficeEmployee(Map<String, Object> paramMap) {
		return getSqlSession().selectList( getIbatisMapperNameSpace()+".findLeaveOfficeEmployee", paramMap);
	}


	@Override
	public List<Map<String, Object>> getCustomerInfo4LXXD(Map<String, Object> paramMap) {
		return getSqlSession().selectList(getIbatisMapperNameSpace() + ".getCustomerInfo4LXXD", paramMap);
	}
}
