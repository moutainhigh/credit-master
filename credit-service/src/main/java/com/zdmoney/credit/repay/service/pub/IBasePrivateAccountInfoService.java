package com.zdmoney.credit.repay.service.pub;

import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.repay.domain.BasePrivateAccountInfo;

public interface IBasePrivateAccountInfoService {
    
    /**
     * 按Id查询对私还款信息
     * @param id
     * @return
     */
    public BasePrivateAccountInfo get(Long id);
    
    /**
     * 查询对私还款信息（带分页查询）
     * @param BasePrivateAccountInfo 条件实体对象
     * @return
     */
    public Pager findWithPg(BasePrivateAccountInfo basePrivateAccountInfo);
    
    /**
     * 查询对私还款信息
     * @param basePrivateAccountInfo
     * @return
     */
    public List<BasePrivateAccountInfo> findListByVo(BasePrivateAccountInfo basePrivateAccountInfo);
    
    /**
     * 保存对私还款信息
     * @param sheetDataList
     */
    public void savePrivateAccountInfo(List<Map<String, String>> sheetDataList);
    
    /**
     * 查询对私还款已认领结果
     * @param basePrivateAccountInfo
     * @return
     */
    public List<Map<String, Object>> findPrivateAccountReceiveInfo(BasePrivateAccountInfo basePrivateAccountInfo);
    
    /**
     * 查询对私还款信息
     * @param basePrivateAccountInfo
     * @return
     */
    public List<BasePrivateAccountInfo> findQueryResultList(BasePrivateAccountInfo basePrivateAccountInfo);
    
    /**
     * 查询被认领客户贷款信息（分页查询）
     * @param params 条件map对象
     * @return
     */
    public Pager findWithPgByMap(Map<String,Object> params);
    
    /**
     * 更新对私还款信息
     * @param basePrivateAccountInfo
     * @return
     */
    public int updatePrivateAccountInfo(BasePrivateAccountInfo basePrivateAccountInfo);
    
    /**
     * 撤销认领
     * @param basePrivateAccountInfo
     * @return
     */
    public int updateAccountInfoForCancel(BasePrivateAccountInfo basePrivateAccountInfo);
    
    /**
     * 修改状态为已导出
     * @param params
     * @return
     */
    public int updateAccountInfoForExport(Map<String,Object> params);
    
    /**
     * 对私还款领取状态校验，不能认领则返回提示信息
     * @return
     */
    public String checkReceiveStatus();
    
    /**
     * 查询对私还款领取时间
     * @return
     */
    public Map<String,Object> findAccountReceiveTime();
    
    /**
     * 更新对私还款领取时间
     * @param params
     */
    public void updateAccountReceiveTime(Map<String,Object> params);

    /**
     * 查询结果
     * @param basePrivateAccountInfo
     * @return
     */
    public List<Map<String,Object>> findQueryResultMapList(BasePrivateAccountInfo basePrivateAccountInfo);
}
