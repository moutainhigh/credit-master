package com.zdmoney.credit.fortune;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.fortune.domain.FortuneSurport;
import com.zdmoney.credit.fortune.pub.IFortuneSurportDao;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;

@Repository
public class FortuneSurportDaoImpl extends  BaseDaoImpl<FortuneSurport> implements IFortuneSurportDao{

}
