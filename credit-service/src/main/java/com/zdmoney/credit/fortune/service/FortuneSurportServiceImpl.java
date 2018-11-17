package com.zdmoney.credit.fortune.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.credit.common.constant.system.SequencesEnum;
import com.zdmoney.credit.fortune.domain.FortuneSurport;
import com.zdmoney.credit.fortune.pub.IFortuneSurportDao;
import com.zdmoney.credit.fortune.service.pub.IFortuneSurportService;
import com.zdmoney.credit.loan.domain.VLoanInfo;
import com.zdmoney.credit.system.service.pub.ISequencesService;

@Service
public class FortuneSurportServiceImpl implements IFortuneSurportService{

	@Autowired
	private IFortuneSurportDao fortuneSurportDao;
	@Autowired
	private ISequencesService sequencesService;
	
	@Override
	public void createFortuneSurport(VLoanInfo loanInfo, Date nextRepayDate) {
//		Sql sql = new Sql(dataSource)
//        String s = "insert into fortune_surport (id,loan_id,loan_type,send_time,cur_return_date) values("+DBUtils.getDBSequenceSQL('fortune_surport_id')+","+loan.id+",'"+loan.loanType+"','"+ new Date().format("yyyy-MM-dd HH:mm:ss")+"','"+nextRepayDate.format("yyyy-MM-dd")+"')";
//        sql.execute(s);
		FortuneSurport fortuneSurport = new FortuneSurport();
		fortuneSurport.setId(sequencesService.getSequences(SequencesEnum.FORTUNE_SURPORT));
		fortuneSurport.setCreateTime(new Date());
		fortuneSurport.setCreator("admin");
		fortuneSurport.setLoanId(loanInfo.getId());
		fortuneSurport.setLoanType(loanInfo.getLoanType());
		fortuneSurport.setSendTime(new Date());
		fortuneSurport.setCurReturnDate(nextRepayDate);
		fortuneSurportDao.insert(fortuneSurport);
		
	}

}
