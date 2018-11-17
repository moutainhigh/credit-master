package com.zdmoney.credit.riskManage.dao.pub;

import java.util.List;

import com.zdmoney.credit.common.util.Pager;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;
import com.zdmoney.credit.riskManage.vo.VPersonVisit;

/**
 * 客户回访查询DAO接口类
 * @author 00236640
 * @version $Id: IPersonVisitDao.java, v 0.1 2015年10月23日 下午4:07:38 00236640 Exp $
 */
public interface IPersonVisitDao extends IBaseDao<VPersonVisit>{
    
    /**
     * 回访管理分页查询
     * @param personVisit
     * @return
     */
    public Pager findVisitManageInfoWithPg(VPersonVisit personVisit);
    
    /**
     * 回访管理信息查询
     * @param personVisit
     * @return
     */
    public List<VPersonVisit> findVisitManagesByVo(VPersonVisit personVisit);
}
