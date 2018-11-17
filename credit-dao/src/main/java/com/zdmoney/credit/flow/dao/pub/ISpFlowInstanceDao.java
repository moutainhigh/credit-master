package com.zdmoney.credit.flow.dao.pub;

import com.zdmoney.credit.flow.domain.SpFlowInstance;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批流程实例
 */
public interface ISpFlowInstanceDao extends IBaseDao<SpFlowInstance>{
    public SpFlowInstance querySpFlowInstanceByApply(Long applyId);
}
