package com.zdmoney.credit.offer.service.pub;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zdmoney.credit.common.vo.core.OfferParamsVo;
import com.zdmoney.credit.common.vo.core.RepayTrailParamsVO;
import com.zdmoney.credit.common.vo.core.TrailVO;
import com.zdmoney.credit.debit.domain.DebitBaseInfo;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.system.domain.ComOrganization;
import com.zendaimoney.thirdpp.vo.enums.ThirdPartyType;
import com.zendaimoney.thirdpp.vo.enums.ZendaiSys;

/**
 * 报盘信息Service
 * @author 00232949
 *
 */
public interface IOfferInfoService {

	/**正常扣款*/
	public static final int ZHENGCHANG = 1;
	/**逾期扣款*/
	public static final int YUQI = 2;
	/**提前扣款*/
	public static final int TIQIANKOUKUAN = 3;
	/**提前结清*/
	public static final int TIQIANJIEQING = 4;
	/**实时划扣*/
	public static final int SHISHIHUAKOU=5;

	
	/**备注标示：需要再回盘时重建新的报盘文件*/
	public static final String MOMO_REBUILD = "rebuild";
	/**
	 * 根据员工号得到员工最高划扣次数
	 * 
	 * */
	public String getoffernum(String usercode);
	
	/**
	 * 根据主键查找
	 * @param offerId
	 * @return
	 */
	public OfferInfo findOfferById(Long offerId);


	/**
	 * 更新
	 * @param offerInfo
	 */
	public void updateOffer(OfferInfo offerInfo);


	/**
	 * 根据日期获取该日的报盘文件，自动报盘
	 * @param currDate
	 */
	public void realtimeDeductByDate(Date currDate);

	
	/**
	 * 实时扣款更新接口方法
	 * @param paramsVo 参数VO
	 */
	public Map<String,Object> updateOfferInfo(OfferParamsVo paramsVo);
	
	
	/**
	 * 还款试算计算方法
	 * @param paramsVo 还款试算参数对象
	 * @return
	 */
	public Map<String,Object> queryTrail(RepayTrailParamsVO paramsVo);
	public TrailVO GetRepaymentInfoForTrail(VLoanInfo loan, Date tradeDate, String repayType);
	
	/**
	 * 
	 * @param paramsVo
	 * @return
	 */
	public Map<String,Object> queryOffer(Map<String,Object> paramMap);


	/**
	 * 查找还款日正常还款的loan记录，记录中不包含申请提前还款或者提前结清的记录
	 * @param salesDepartment 营业部，如果为null则查出所有该类型记录
	 * @return
	 */
	public List<VLoanInfo> findLoansByZCHK(ComOrganization salesDepartment);

	/**
	 * 查找逾期的loan记录，记录中不包含申请过提前结清的记录
	 * @param salesDepartment 营业部，如果为null则查出所有该类型记录
	 * @return
	 */
	public List<VLoanInfo> findLoansByYQHK(ComOrganization salesDepartment);

	/**
	 * 查找申请通过的并且还款日是今天的提前还款的loan记录
	 * @param salesDepartment 营业部，如果为null则查出所有该类型记录
	 * @return
	 */
	public List<VLoanInfo> findLoansByTQHK(ComOrganization salesDepartment);

	/**
	 * 查找申请通过的并且还款日是今天的提前结清的loan记录
	 * @param salesDepartment 营业部，如果为null则查出所有该类型记录
	 * @return
	 */
	public List<VLoanInfo> findLoansByTQJQ(ComOrganization salesDepartment);

	/**
	 * 创建报盘信息 独立事物
	 * @param loan
	 * @param salesDepartment
	 * @param type 类型用于控制优先级 ；1：正常扣款，2：逾期扣款，3：提前扣款，4：提前结清
	 * @param offerAmount 报盘金额 ：如果是null则全额报盘
	 * @return
	 */
	public OfferInfo createOffer(VLoanInfo loan,ComOrganization salesDepartment, int type, BigDecimal offerAmount,String paySysNo);
	
	/**
	 * 检查是否有未回盘的情况
	 * 查询该loan报盘明细OfferTransaction的所有记录，查看是否有未回盘的，如果有，不能再次生成
	 * 
	 * @param loan
	 * @return
	 */
	public boolean checkHasOffer(VLoanInfo loan);

	/**
	 * 按照批次（一个系统一个第三方扣款机构）发送tpp
	 * @param offerList
	 * @param tppType
	 * @param tppSysNum
	 * @return
	 */
//	public String sendOffer(List<OfferInfo> offerList, ThirdPartyType tppType,ZendaiSys tppSysNum);
	
	/**
	 * 根据系统名和第三方机构分组
	 * @param needSendOfferList
	 * @return
	 */
	public Map<ZendaiSys, Map<ThirdPartyType, List<OfferInfo>>> groupOffer(List<OfferInfo> needSendOfferList);

	/**
	 * 根据报盘日期和多个状态得到报盘文件（只包含自动划扣）
	 * @param loanId 
	 * @param currDate
	 * @param states
	 * @return
	 */
	public List<OfferInfo> findOfferByOfferDateAndStates(Long loanId, Date currDate,String[] states);


	/**
	 * 发送报盘到tpp2.0
	 * @param offerList
	 */
	public void sendOfferToTpp2(List<OfferInfo> offerList);


	/**
	 * 发送报盘到tpp1.0
	 * @param value
	 * @param key
	 * @param tppSysNum
	 */
	public void sendOfferToTpp1(List<OfferInfo> value, ThirdPartyType key,ZendaiSys tppSysNum);
	
	/**
	 * 委托代扣回盘查询
	 * @param params
	 * @return
	 */
	public List<OfferInfo> findOfferReturnList(Map<String,Object> params);

	/**
	 * 发送单条报盘到tpp2.0（实时划扣使用）
	 * @param offerList
	 */
	public void sendOfferToTpp2(OfferInfo offer);
	
	/**
	 * 发送单条报盘到tpp1.0（实时划扣使用）
	 * @param value
	 * @param key
	 * @param tppSysNum
	 */
	public void sendOfferToTpp1(OfferInfo offer);

	/**
	 * 创建报盘信息 非独立事物
	 * @param loan
	 * @param salesDepartment
	 * @param type 类型用于控制优先级 ；1：正常扣款，2：逾期扣款，3：提前扣款，4：提前结清
	 * @param offerAmount 报盘金额 ：如果是null则全额报盘
	 * @return
	 */
	public OfferInfo createRealtimeOffer(VLoanInfo loan,
			ComOrganization organization, int shishihuakou2,
			BigDecimal offerAmount);
	
	public int updateOfferInfo(Map<String, Object> params); 
	
	/**
     * 获取代扣通道
     * @param loan
     * @return
     */
    public String getTppType(VLoanInfo loan);
    
	/**
     * 获取代扣通道
     * @param loanId
     * @return
     */
    public String getTppType(Long loanId);
    
    /**
	 * 初始化银行关系字典表
	 */
	public void initBankRelateTppMap();
	
	/**
	 * 获取Tpp银行代码
	 * @param bankCode 信贷系统银行代码
	 * @return
	 */
	public String getTppBankCode(String bankCode);
	
	/**
	 * 是否发送TPP（合同来源是外贸信托不发送）
	 * @param offerInfo
	 * @return
	 */
	public boolean isSendToTpp(OfferInfo offerInfo);

	/**
	 * 外贸2是否回盘
	 * @param loanInfo
	 * @return
	 */
	public boolean isExistWaiMao2BackOfferInfo(VLoanInfo loanInfo);
	/**
	 * 创建第三方报盘信息 非独立事物
	 * @param loan
	 * @param salesDepartment
	 * @param type 类型用于控制优先级 ；1：正常扣款，2：逾期扣款，3：提前扣款，4：提前结清
	 * @param offerAmount 报盘金额 ：如果是null则全额报盘
	 * @return
	 */
	public DebitBaseInfo createDebitOffer(VLoanInfo loan,ComOrganization salesDepartment, int type, BigDecimal offerAmount,String paySysNo);
	/**
	 * 发送报盘到外贸3
	 */
	public void sendOffer(List<DebitBaseInfo> debitBaseInfoList);
}
