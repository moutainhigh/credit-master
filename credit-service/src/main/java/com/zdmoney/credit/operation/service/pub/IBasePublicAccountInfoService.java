package com.zdmoney.credit.operation.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.operation.domain.BasePublicAccountInfo;

public interface IBasePublicAccountInfoService {
    
    /**
     * 按Id查询对公账户信息
     * @param id
     * @return
     */
    public BasePublicAccountInfo get(Long id);
    
    /**
     * 查询对公还款信息（带分页查询）
     * @param basePublicAccountInfo 条件实体对象
     * @return
     */
    public Pager findWithPg(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 查询对公还款信息
     * @param loanContractInfo
     * @return
     */
    public List<BasePublicAccountInfo> findListByVo(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 对公还款领取状态校验，不能认领则返回提示信息
     * @return
     */
    public String checkReceiveStatus();
    
    /**
     * 保存对公账户还款信息
     * @param params
     * @return
     */
    public int savePublicAccountImportData(Map<String, String> params);
    
    /**
     * 查询对公账户已认领结果
     * @param basePublicAccountInfo
     * @return
     */
    public List<Map<String, Object>> findPublicAccountReceiveInfo(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 查询对公账户还款认领情况表
     * @param basePublicAccountInfo
     * @return
     */
    public List<Map<String, Object>> findPublicAccountRepayReceiveInfo(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 查询对公账户信息
     * @param basePublicAccountInfo
     * @return
     */
    public List<BasePublicAccountInfo> findQueryResultList(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 带分页查询
     * @param params 条件map对象
     * @return
     */
    public Pager findWithPgByMap(Map<String,Object> params);
    
    /**
     * 更新对公账户还款信息
     * @param basePublicAccountInfo
     * @return
     */
    public int updateBasePublicAccountInfo(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 撤销认领
     * @param basePublicAccountInfo
     * @return
     */
    public int updateAccountInfoForCancel(BasePublicAccountInfo basePublicAccountInfo);
    
    /**
     * 修改状态为已导出
     * @param params
     * @return
     */
    public int updateAccountInfoForExport(Map<String,Object> params);
    
    /**
     * 查询对公账户还款领取时间范围信息
     * @return
     */
    public Map<String,Object> findBusinessAccountReceiveTime();
    
    /**
     * 更新对公账户还款领取时间范围信息
     * @param params
     */
    public void updateBusinessAccountReceiveTime(Map<String,Object> params);

    /**
     * 查询结果
     * @param basePublicAccountInfo
     * @return
     */
    List<Map<String,Object>> findQueryResultMapList(BasePublicAccountInfo basePublicAccountInfo);
}
