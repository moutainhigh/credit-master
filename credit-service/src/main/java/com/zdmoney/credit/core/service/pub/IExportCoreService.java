package com.zdmoney.credit.core.service.pub;


public interface IExportCoreService {
	public void exportBorrowerCSV() throws Exception;
	public void exportLoanCSV() throws Exception;
	public void exportContactCSV() throws Exception;
	public void exportTelCSV() throws Exception;
	public void exportAddressCSV() throws Exception;
	public void exportRepaymentDetialCSV() throws Exception;
	public void exportRepayInfoCSV() throws Exception;
	public void exportFlowCSV() throws Exception;
	public void exportPersonTotalCSV() throws Exception;
	public void exportDepartmentCSV() throws Exception;
	public void updateIsSendForLoan() throws Exception;
	public void sendEmail() throws Exception;
}
