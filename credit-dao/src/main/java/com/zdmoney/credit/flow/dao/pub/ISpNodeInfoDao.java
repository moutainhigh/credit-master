package com.zdmoney.credit.flow.dao.pub;

import com.zdmoney.credit.flow.domain.SpNodeInfo;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

import java.util.List;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批环节节点
 */
public interface ISpNodeInfoDao extends IBaseDao<SpNodeInfo> {

    public List<SpNodeInfo> querySpApplyNodeInfos();
}
