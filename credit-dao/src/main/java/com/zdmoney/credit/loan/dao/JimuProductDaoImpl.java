package com.zdmoney.credit.loan.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.loan.dao.pub.IJimuProductDao;
import com.zdmoney.credit.loan.domain.JimuProduct;

/**
 * 积木盒子产品数据库操作
 * @author 00234770
 * @date 2015年8月31日 下午5:25:39 
 *
 */
@Repository
public class JimuProductDaoImpl extends BaseDaoImpl<JimuProduct>implements IJimuProductDao {

	
	/**
	 * 通过time查找
	 * @param time
	 * @return
	 */
	@Override
	public JimuProduct findByTime(Long time) {
		return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findByTime",  time);
	}

}
