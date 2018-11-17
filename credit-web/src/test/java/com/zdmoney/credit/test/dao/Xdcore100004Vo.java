package com.zdmoney.credit.test.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;




/**
 * 功能号Xdcore100002 Vo对象 【推送息费减免结果】
 * @author wangn
 *
 */
public class Xdcore100004Vo {


	/**待回购合计**/
	@NotNull(message="待回购合计不能为空")
	private BigDecimal repTotal;
	/**待回购本金**/
	@NotNull(message="待回购本金不能为空")
	private BigDecimal repAmt;
	/**待回购利息**/
	@NotNull(message="待回购利息不能为空")
	private BigDecimal repIntst;
	/**回购信息列表**/
	List<ButBackEntity> listBuyBack = new ArrayList<ButBackEntity>();

	public BigDecimal getRepTotal() {
		return repTotal;
	}

	public void setRepTotal(BigDecimal repTotal) {
		this.repTotal = repTotal;
	}

	public BigDecimal getRepAmt() {
		return repAmt;
	}

	public void setRepAmt(BigDecimal repAmt) {
		this.repAmt = repAmt;
	}

	public BigDecimal getRepIntst() {
		return repIntst;
	}

	public void setRepIntst(BigDecimal repIntst) {
		this.repIntst = repIntst;
	}

	public List<ButBackEntity> getListBuyBack() {
		return listBuyBack;
	}

	public void setListBuyBack(List<ButBackEntity> listBuyBack) {
		this.listBuyBack = listBuyBack;
	}


	
}
