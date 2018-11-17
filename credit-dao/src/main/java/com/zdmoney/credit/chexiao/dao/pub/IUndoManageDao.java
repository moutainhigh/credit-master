package com.zdmoney.credit.chexiao.dao.pub;


import com.zdmoney.credit.chexiao.domain.LoanStateVo;
import com.zdmoney.credit.chexiao.domain.Undo;
import com.zdmoney.credit.chexiao.domain.UndoManage;
import com.zdmoney.credit.framework.dao.pub.IBaseDao;

/**
 * 撤销管理dao接口定义
 * @author YM20033
 *
 */
public interface IUndoManageDao extends IBaseDao<UndoManage> {

	public String undo(String tradeno);

	public void updateState(LoanStateVo loanStateVo);
}
