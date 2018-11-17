package com.zdmoney.credit.flow.dao.pub;

import com.zdmoney.credit.flow.domain.SpNodeRole;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

import java.util.List;

/**
 * Created by ym10094 on 2017/9/6.
 */
public interface ISpNodeRoleDao extends IBaseDao<SpNodeRole> {

    public List<SpNodeRole> querySpNodeRole(Long roleId);
}
