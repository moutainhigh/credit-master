package com.zdmoney.credit.flow.dao;

import com.zdmoney.credit.flow.dao.pub.ISpNodeRoleDao;
import com.zdmoney.credit.flow.domain.SpNodeRole;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ym10094 on 2017/9/6.
 */
@Repository
public class SpNodeRoleDaoImpl extends BaseDaoImpl<SpNodeRole> implements ISpNodeRoleDao {
    @Override
    public List<SpNodeRole> querySpNodeRole(Long roleId) {
        Map<String,Object> params = new HashMap<>();
        params.put("roleId",roleId);
        params.put("status","0");/*0 有效 1 无效*/
        return findListByMap(params);
    }
}
