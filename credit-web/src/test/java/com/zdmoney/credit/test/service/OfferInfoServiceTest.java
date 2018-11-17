package com.zdmoney.credit.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zdmoney.credit.common.util.Dates;
import com.zdmoney.credit.job.CreateOfferJob;
import com.zdmoney.credit.loan.service.pub.IVLoanInfoService;
import com.zdmoney.credit.offer.domain.OfferInfo;
import com.zdmoney.credit.offer.service.pub.IOfferCreateService;
import com.zdmoney.credit.offer.service.pub.IOfferInfoService;

@RunWith(SpringJUnit4ClassRunner.class)
// 使用junit4进行测试
@ContextConfiguration({ "/spring/*.xml" })
// 加载配置文件
public class OfferInfoServiceTest {

	@Autowired
	IOfferCreateService offerCreateService;

	@Autowired
	IOfferInfoService offerInfoService;
	@Autowired
	IVLoanInfoService vLoanInfoService;
	@Autowired
	CreateOfferJob createOfferJob;

//	@Test
	public void createOfferJob() {
		createOfferJob.createOverdueOffer();
	}
	
	@Test
	public void testSendOffer() {
		offerInfoService.realtimeDeductByDate(Dates.getCurrDate());
	}

//	@Test
	@Rollback(value = false)
	public void testateOffer() {

		// VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(30696572l);
		// offerInfoService.createOffer(vLoanInfo, null, 5, new
		// BigDecimal(100));

		OfferInfo offerInfo = offerInfoService.findOfferById(123l);
		if (offerInfo.getSpt1().equals("1")) {
			System.out.println("1111111111111111111111111111");
		}
		offerInfo.setSpt1("1");
		offerInfo.setSpt2("2");
		offerInfoService.updateOffer(offerInfo);

	}

	/*
	 * @Test public void testGetTQJQLoanByDate () {
	 * 
	 * ComOrganization org = new ComOrganization();
	 * org.setCode("0104004400050");
	 * 
	 * int i = offerCreateService.createOfferRecordWithOrgAndType(org,
	 * IOfferInfoService.ZHENGCHANG);
	 * 
	 * System.out.println(i); }
	 */

	/*
	 * @Test public void testateOffer () {
	 * 
	 * VLoanInfo vLoanInfo = vLoanInfoService.findByLoanId(30696572l);
	 * offerInfoService.createOffer(vLoanInfo, null, 5, new BigDecimal(100));
	 * 
	 * }
	 */
}
