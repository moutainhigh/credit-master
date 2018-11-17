package com.zdmoney.credit.riskManage.service.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.riskManage.vo.VPersonVisit;

public interface IPersonVisitService {
    
    /**
     * 分页查询客户回访信息
     * @param personVisit
     * @return
     */
    public Pager findWithPg(VPersonVisit personVisit);
    
    /**
     * 分页查询回访管理信息
     * @param personVisit
     * @return
     */
    public Pager findVisitManageInfoWithPg(VPersonVisit personVisit);
    
    /**
     * 查询客户回访记录信息
     * @param personVisit
     * @return
     */
    public List<VPersonVisit> findListByVO(VPersonVisit personVisit);
    
    /**
     * 回访管理信息查询
     * @param personVisit
     * @return
     */
    public List<VPersonVisit> findVisitManagesByVo(VPersonVisit personVisit);
    
    /**
     * 保存客户回访记录
     * @param personVisit
     */
    public void saveCustomerVisitInfo(VPersonVisit personVisit);
}
