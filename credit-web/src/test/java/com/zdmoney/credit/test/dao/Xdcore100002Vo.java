package com.zdmoney.credit.test.dao;

import java.util.ArrayList;
import java.util.List;



/**
 * 功能号Xdcore100002 Vo对象 【推送息费减免结果】
 * @author wangn
 *
 */
public class Xdcore100002Vo {

	private static final long serialVersionUID = -2940996637743332278L;
    
	/**息费减免结果列表**/
	private List<XdFeeReduceEntity> listFeeReduceResult = new ArrayList<XdFeeReduceEntity>();
	
	public List<XdFeeReduceEntity> getListFeeReduceResult() {
		return listFeeReduceResult;
	}
	public void setListFeeReduceResult(List<XdFeeReduceEntity> listFeeReduceResult) {
		this.listFeeReduceResult = listFeeReduceResult;
	}
}
