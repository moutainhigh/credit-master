package com.zdmoney.credit.loan.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.zdmoney.credit.common.util.ValidatorUtil;
import com.zdmoney.credit.loan.vo.VLoanFilesInfo;

public class LoanFilesInfoAddValidator implements Validator  {

	@Override
	public boolean supports(Class<?> clazz) {
		
		return VLoanFilesInfo.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		VLoanFilesInfo vLoanFilesInfo = (VLoanFilesInfo)target;
		//住址证明
		notNullValidator(errors, "住址证明数量", vLoanFilesInfo.getAddressCertificationCount(),vLoanFilesInfo.getAddressCertification(),vLoanFilesInfo.getAddressCertificationOther());
		
		//资产证明
		notNullValidator(errors, "资产证明数量", vLoanFilesInfo.getAssetCertificationCount(),vLoanFilesInfo.getAssetCertification(),vLoanFilesInfo.getAssetCertificationOther());

		//经营证明
		notNullValidator(errors, "经营证明数量", vLoanFilesInfo.getBusinessCertificationCount(),vLoanFilesInfo.getBusinessCertification(),vLoanFilesInfo.getBusinessCertificationOther());

		//身份证明
		notNullValidator(errors, "身份证明", vLoanFilesInfo.getIdCertificationCount(),vLoanFilesInfo.getIdCertification(),vLoanFilesInfo.getIdCertificationOther());

		//收入证明
		notNullValidator(errors, "收入证明", vLoanFilesInfo.getIncomeCertificationCount(),vLoanFilesInfo.getIncomeCertification(),vLoanFilesInfo.getIncomeCertificationOther());

		//其他证明
		notNullValidator(errors, "其他证明", vLoanFilesInfo.getOtherCertificationCount(),vLoanFilesInfo.getOtherCertification(),vLoanFilesInfo.getOtherCertificationOther());

		//工作证明
		notNullValidator(errors, "工作证明", vLoanFilesInfo.getWorkCertificationCount(),vLoanFilesInfo.getWorkCertification(),vLoanFilesInfo.getWorkCertificationOther());
	}
	
	public static void notNullValidator(Errors errors,String fieldName,Long value,String reference1,String reference2){
		if(value!=null){
			if(value<0){
				errors.reject("Min", fieldName+"必须大于等于0");
			}
		}else{
			if((reference1!=null&&reference1.length()>0) || (reference2!=null && reference2.length()>0)){
				errors.reject("NotNull",fieldName+"不能为空");
			}
		}
	}

}
