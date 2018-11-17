package com.zdmoney.credit.flow.dao;

import com.zdmoney.credit.common.constant.flow.NodeStatusEnum;
import com.zdmoney.credit.common.constant.flow.NodeTypeEnum;
import com.zdmoney.credit.flow.dao.pub.ISpNodeInfoDao;
import com.zdmoney.credit.flow.domain.SpNodeInfo;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/8/2.
 * 审批环节节点
 */
@Repository
public class SpNodeInfoDaoImpl extends BaseDaoImpl<SpNodeInfo> implements ISpNodeInfoDao {
    @Override
    public List<SpNodeInfo> querySpApplyNodeInfos() {
        Map<String,Object> parmas = new HashMap<>();
        parmas.put("nodeType", NodeTypeEnum.审批节点.getCode());
        parmas.put("status", NodeStatusEnum.有效.getCode());
        return findListByMap(parmas);
    }
}
