package com.zdmoney.credit.payment.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.payment.domain.ThirdLineOffer;
import com.zdmoney.credit.payment.vo.ThirdLineOfferVo;

public interface IThirdLineOfferService {
    
    /**
     * 新增报盘信息
     * @param haTwoOffer
     */
    public void insert(ThirdLineOffer haTwoOffer);
    /**
     * 查询报盘信息
     * @return
     */
    public List<ThirdLineOfferVo> queryAllHaTwoOfferVo();
    /**
     * 根据备注查询报盘信息
     * @param paramMap
     */
    public List<ThirdLineOffer> findHaTwoOfferByMap(Map<String, Object> paramMap);
    
    /**
     * 跟据编号查询实体
     * @param id
     * @return
     */
    public ThirdLineOffer findById(Long id);
    
    /**
     * 创建放款本金和手续费的报盘信息
     * @return 
     */
    public List<ThirdLineOffer> createThirdLineOffer(Map<String, Object> map);
    
    /**
     * 根据债权编号更新报盘信息
     * @param loanId
     */
    public void updateState(Long loanId);
    
    /**
     * 分页查询报盘信息
     * @param paramMap
     * @return
     */
    public Pager searchOfferInfoWithPg(Map<String, Object> paramMap);
    
    /**
     * 查询第三方线下放款页面需要展示的数据
     * @param paramMap
     * @return
     */
    public Pager searchOffLineLoanInfoWithPg(Map<String, Object> paramMap);
    /**
     * 是否可以进行线上放款（查询是否进行了线下放款）
     * @param loanId
     * @return
     */
	public Boolean isApplyThirdLineGrant(Long loanId);
}
