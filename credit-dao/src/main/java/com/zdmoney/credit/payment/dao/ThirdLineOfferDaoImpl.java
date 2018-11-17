package com.zdmoney.credit.payment.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import com.zdmoney.credit.payment.dao.pub.IThirdLineOfferDao;
import com.zdmoney.credit.payment.domain.ThirdLineOffer;
import com.zdmoney.credit.payment.vo.ThirdLineOfferVo;

@Repository
public class ThirdLineOfferDaoImpl extends BaseDaoImpl<ThirdLineOffer> implements IThirdLineOfferDao {

    public List<ThirdLineOfferVo> queryAllHaTwoOfferVo() {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryAllHaTwoOfferVo");
    }

    public List<ThirdLineOffer> findHaTwoOfferByMap(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findHaTwoOfferByMap");
    }

    /**
     * 查询未报盘、已回盘失败,财务放款的债权信息
     * @param params
     */
    public List<ThirdLineOffer> queryOfferInfo() {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".queryOfferInfo");
    }

    public List<ThirdLineOffer> findOfferInfoList() {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findOfferInfoList");
    }

    public ThirdLineOffer findHaTwoOfferByFinancialType(String findHaTwoOfferByFinancialType) {
        return getSqlSession().selectOne(getIbatisMapperNameSpace() + ".findHaTwoOfferByFinancialType");
    }

    public Pager searchOfferInfoWithPg(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".searchOfferInfoListResult");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".searchOfferInfoCount");
        return doPager(pager, paramMap);
    }

	@Override
	public Pager searchOffLineLoanInfoWithPg(Map<String, Object> paramMap) {
        Pager pager = (Pager) paramMap.get("pager");
        if (pager == null) {
            pager = new Pager();
        }
        pager.setSearchDataSqlMapId(getIbatisMapperNameSpace() + ".searchOffLineLoanInfoResult");
        pager.setSearchCountSqlMapId(getIbatisMapperNameSpace() + ".searchOffLineLoanInfoCount");
        return doPager(pager, paramMap);
    }

    @Override
    public List<ThirdLineOffer> findOfferLineOfferByMap(Map<String, Object> paramMap) {
        return getSqlSession().selectList(getIbatisMapperNameSpace() + ".findOfferLineOfferByMap",paramMap);
    }
}
