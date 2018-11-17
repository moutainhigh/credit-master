package com.zdmoney.credit.ems.service.pub;

import java.io.IOException;
import java.util.Date;

public interface IEMSService {

	 public void batchGroupSendUnsend(Date repayDate,Date smsDate) throws IOException;
	 public void batchGroupSendFailedRetry(Date repayDate,Date smsDate) throws IOException;
	 public void batchGroupSendFailedRetryImmediate(Date repayDate);
	 public void batchGroupUpdateResult(Date repayDate,Date smsDate) throws IOException;
	 public void generateData(Date repayDate, boolean forceRegernateRepayMind);
	 public String emailMPhone(Date repayDate) throws IOException;
	 public String emailSendResult(Date repayDate,Date smsDate) throws IOException;
}
