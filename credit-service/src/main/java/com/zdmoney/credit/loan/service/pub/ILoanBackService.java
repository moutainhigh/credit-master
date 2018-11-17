package com.zdmoney.credit.loan.service.pub;

import java.util.List;

import com.zdmoney.credit.loan.domain.LoanBack;

/**
 * 待回购信息service
 * @author user
 *
 */
public interface ILoanBackService {
	
	/**
	 * 更新或保存推送待回购信息
	 * @return 
	 */
	public LoanBack insert(LoanBack loanBack);

	/**
	 * 查询回购信息
	 * @param loanBack
	 * @return
	 */
	public List<LoanBack> findListByVo(LoanBack loanBack);

	/**
	 * 修改批次号
	 * @param _loanback
	 */
	public int update(LoanBack _loanback);

	/**
	 * 上传回购信息
	 */
	public void uploadLoanBack();
}
