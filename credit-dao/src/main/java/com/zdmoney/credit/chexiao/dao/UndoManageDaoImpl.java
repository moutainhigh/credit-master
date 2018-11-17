package com.zdmoney.credit.chexiao.dao;

import org.springframework.stereotype.Repository;

import com.zdmoney.credit.chexiao.dao.pub.IUndoManageDao;
import com.zdmoney.credit.chexiao.domain.LoanStateVo;
import com.zdmoney.credit.chexiao.domain.Undo;
import com.zdmoney.credit.chexiao.domain.UndoManage;
import com.zdmoney.credit.framework.dao.BaseDaoImpl;
/**
 * 撤销管理数据库操作
 */
@Repository
public class UndoManageDaoImpl extends BaseDaoImpl<UndoManage>implements IUndoManageDao {

	/**
	 * 执行撤销操作
	 */
	public String undo(String tradeno) {
		Undo undo = new Undo();
		undo.setTradeno(tradeno);
		String str = getSqlSession().selectOne(getIbatisMapperNameSpace()+".undo",undo); 
		getSqlSession().update(getIbatisMapperNameSpace()+".delete_orr", undo);
		return undo.getMsg();
	}

	/**
	 * 更新还款状态
	 */
	public void updateState(LoanStateVo loanStateVo) {
			getSqlSession().update(getIbatisMapperNameSpace()+".delete", loanStateVo);
			getSqlSession().update(getIbatisMapperNameSpace()+".updateState", loanStateVo);
	}
}
