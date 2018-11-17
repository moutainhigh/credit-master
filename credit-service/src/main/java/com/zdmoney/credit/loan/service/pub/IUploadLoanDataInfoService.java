package com.zdmoney.credit.loan.service.pub;

import java.util.List;

import com.zdmoney.credit.loan.domain.UploadLoanDataInfo;
import com.zdmoney.credit.video.vo.DownLoadVideoDirVo;

/**
 * @author 10098  2017年3月1日 上午11:32:27
 */
public interface IUploadLoanDataInfoService {

	/**
	 * 龙信小贷把放款成功的债权信息数据插入到上传表中
	 */
	public void pushLoanData2UploadInfo();

	/**
	 * 获取需要上传的 债权信息
	 * @return
	 */
	public List<UploadLoanDataInfo> findUploadLoanDataInfo2LXXD();

	/**
	 * 更新债权上传信息表
	 * @param uploadLoanDataInfo
	 * @return
	 */
	public int updateLoanDataInfoByVo(UploadLoanDataInfo uploadLoanDataInfo);

	/**
	 * 龙信小贷 上传文件 添加 放款通知书 
	 * @param downLoanList
	 * @param uploadLoanDataInfo
	 */
	public void addDownLoadDirs4LXXD(List<DownLoadVideoDirVo> downLoanList, UploadLoanDataInfo uploadLoanDataInfo);

	/**
	 * 上传文件至龙心小贷线程
	 * @param batchNum
	 */
	public void uploadLoanDataThread(String batchNum);
	
	/**
	 * 根据批次号保存 待上传的债权信息
	 */
	public void pushLoanData2UploadInfoByBatchNum(String batchNum);

	/**
	 * @param batchNum
	 * @return
	 */
	List<UploadLoanDataInfo> findUploadLoanDataInfo2LXXDByBatchNum(String batchNum);
}
