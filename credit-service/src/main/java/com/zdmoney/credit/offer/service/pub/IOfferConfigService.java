package com.zdmoney.credit.offer.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.offer.domain.OfferConfig;

/**
 * 报盘配置信息
 * @author 00236640
 * 
 */
public interface IOfferConfigService {
    
    /**
     * 新增报盘配置信息
     * @param vo
     * @return
     */
    public OfferConfig insert(OfferConfig vo);

    /**
     * 更新报盘配置信息
     * @param vo
     * @return
     */
    public int update(OfferConfig vo);
    
    /**
     * 查询报盘配置信息
     * @param vo
     * @return
     */
    public List<OfferConfig> findListByVo(OfferConfig vo);
    
    /**
     * 是否生成正常、提前结清、提前扣款报盘
     * @return
     */
    public boolean isCanCreateNormalOffer();
    
    /**
     * 是否生成逾期扣款报盘
     * @return
     */
    public boolean isCanCreateOverdueOffer();
    
    /**
     * 查询当天发送报盘的所有不重复划扣类型
     */
    public List<String> queryAllDayDebitTypeList();
    
    /**
     * 是否实时划扣
     * @return
     */
    public boolean isCanDebit();
    
    /**
     * 查询当次发送报盘的划扣类型
     * @return
     */
    public List<String> queryDebitTypeList();
    
    /**
     * 查询当天报盘的所有划扣时间
     * @return
     */
    public List<String> queryDebitTimeList();
    
    /**
     * 查询当次发送报盘的划扣类型和划扣合同来源
     */
    public Map<String,Object> queryDebitParams();
}
